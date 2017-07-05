package com.aisino.e9.service.invoice.invoiceinfo.impl;

import com.aisino.base.invoice.userinfo.service.impl.CuzSessionAttributes;
import com.aisino.common.util.IdWorker;
import com.aisino.core.util.CloneUtils;
import com.aisino.e9.entity.invoice.invoiceinfo.pojo.InvoiceInfo;
import com.aisino.e9.entity.invoice.order.orderdetail.pojo.OrderDetail;
import com.aisino.e9.entity.invoice.order.orderinfo.pojo.OrderInfo;
import com.aisino.e9.service.invoice.invoiceinfo.InvoiceInfoService;
import com.aisino.e9.service.invoice.order.orderdetail.OrderDetailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by 为 on 2017-6-22.
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SimpleSplitMergeHelper {

    @Autowired
    private CuzSessionAttributes cuzSessionAttributes;

    @Autowired
    private IdWorker idWorker;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private InvoiceInfoService invoiceInfoService;


    //拆分合并入口方法
    public Set<String> mergeAndSplit(List<OrderInfo> orderInfos) {
        Map<String, List<OrderInfoClue>> taxNoOrderInfosMap = groupByTaxno(orderInfos);
        BigDecimal maxAmount = new BigDecimal(cuzSessionAttributes.getEnInfo().getMaxAmount());

        List<OrderDetail> addDetails = new ArrayList<>();//此集合,需要add到数据
        List<OrderDetail> updateDetails = new ArrayList<>();//此集合,需要add到数据
        Set<Integer> rmDetailIds = new HashSet<>();//待删除的明细
        List<SplitInvoce> splitInvoces = new ArrayList<>();//拆分中间表数据
        List<MergeInvoce> mergeInvoces = new ArrayList<>();//合并中间表数据
        List<RawInvoce> rawInvoces = new ArrayList<>();//原装中间表数据

        for (String taxno : taxNoOrderInfosMap.keySet()) {
            List<OrderInfoClue> orderInfoClues = taxNoOrderInfosMap.get(taxno);
            //总金额倒序
            Collections.sort(orderInfoClues, new Comparator<OrderInfoClue>() {
                @Override
                public int compare(OrderInfoClue o1, OrderInfoClue o2) {
                    return new BigDecimal(o2.getTotalAmount()).compareTo(new BigDecimal(o1.getTotalAmount()));
                }
            });

            for (OrderInfoClue orderInfoClue : orderInfoClues) {
                //单价倒叙
                Collections.sort(orderInfoClue.getOrderDetails(), new Comparator<OrderDetail>() {
                    @Override
                    public int compare(OrderDetail o1, OrderDetail o2) {
                        return new BigDecimal(o2.getItemPrice()).compareTo(new BigDecimal(o1.getItemPrice()));
                    }
                });
            }


            List<OrderInfoClue> splitClue = new ArrayList<>();//拆分候选名单
            List<OrderInfoClue> mergeClue = new ArrayList<>();//合并候选名单
            List<OrderInfoClue> rawClue = new ArrayList<>();//不需要处理的
            //分组需要合并还是需要拆分的
            for (OrderInfoClue orderInfoClue : orderInfoClues) {
                if (orderInfoClue.getOrderDetails().size()>0){//如果不存在可拆分合并的明细,那么排除
                    if (orderInfoClues.size() > 1 && new BigDecimal(orderInfoClue.getTotalAmount()).compareTo(maxAmount) < 0) {//订单数量大于1,并且不当前不饱满限额,列入合并候选名单
                        mergeClue.add(orderInfoClue);
                    } else if (new BigDecimal(orderInfoClue.getTotalAmount()).compareTo(maxAmount) > 0) {
                        splitClue.add(orderInfoClue);
                    } else rawClue.add(orderInfoClue);
                }
            }

            //-----------------------------------------------------合并处理
            this.merge(mergeClue);
            /**
             * 合并处理结束后,把无法合并的放入不需要处理的候选名单,从合并处理候选名单中移除
             * 根据合并候选名单:
             * 如果已经被合并过后的,订单时合并订单,明细,是被合并订单的所有明细集合,这些明细集合的orderno并没有被改变
             * 所以可以根据这些orderno判断哪些订单被合并,然后根据这些orderno 去更新数据库的被合并订单的合并状态
             */
            Iterator<OrderInfoClue> mergeClueIterator = mergeClue.iterator();
            while (mergeClueIterator.hasNext()) {
                OrderInfoClue orderInfoClue = mergeClueIterator.next();
                //处理无法合并的订单
                if (OrderDetail.SplitMergeMark.RAW.equals(orderInfoClue.getSplitMergeMark())) {
                    rawClue.add(CloneUtils.clone(orderInfoClue));
                } else if (OrderDetail.SplitMergeMark.MERGE.equals(orderInfoClue.getSplitMergeMark())) {
                    //被合并的,写到同一个发票信息中
                    MergeInvoce mergeInvoce = new MergeInvoce(String.valueOf(idWorker.nextId()));
                    for (OrderDetail orderDetail : orderInfoClue.getOrderDetails()) {
                        orderDetail.setSplitMergeMark(OrderDetail.SplitMergeMark.MERGE.getCode());
                        orderDetail.setSerialNo(mergeInvoce.getSerialNo());
                        mergeInvoce.getOrderNos().add(orderDetail.getOrderNo());
                        updateDetails.add(orderDetail);//待更新合并后的发票流水号
                    }
                    mergeInvoces.add(mergeInvoce);
                }
                mergeClueIterator.remove();
            }

            //-------------------------------------------------处理拆分

            Iterator<OrderInfoClue> splitClueIterator = splitClue.iterator();
            while (splitClueIterator.hasNext()) {
                OrderInfoClue orderInfoClue = splitClueIterator.next();
                splitInvoces.addAll(split(orderInfoClue, addDetails, rmDetailIds));
            }

            //-----------------------------------------处理原装

            for (OrderInfoClue orderInfoClue : rawClue) {
                String serialNo = String.valueOf(idWorker.nextId());
                for (OrderDetail orderDetail : orderInfoClue.getOrderDetails()) {
                    orderDetail.setSerialNo(serialNo);
                    orderDetail.setSplitMergeMark(OrderDetail.SplitMergeMark.RAW.getCode());
                    updateDetails.add(orderDetail);
                }
                RawInvoce rawInvoce = new RawInvoce(serialNo, orderInfoClue.getOrderInfo().getOrderNo());
                rawInvoces.add(rawInvoce);
            }
        }
        return ((SimpleSplitMergeHelper) AopContext.currentProxy()).submit(addDetails, updateDetails, rmDetailIds, splitInvoces, rawInvoces, mergeInvoces);
    }

    /**
     * 更新集合数据
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Set<String> submit(List<OrderDetail> addDetails, List<OrderDetail> updateDetails, Set<Integer> rmDetailIds, List<SplitInvoce> splitInvoces, List<RawInvoce> rawInvoces, List<MergeInvoce> mergeInvoces) {

        Set<String> serialNoSet = new HashSet<>();
        for (Integer detailId : rmDetailIds)
            orderDetailService.deleteById(detailId,cuzSessionAttributes.getEnInfo().getShardingId());
        for (OrderDetail orderDetail : updateDetails)
            orderDetailService.updateEntity(orderDetail,cuzSessionAttributes.getEnInfo().getShardingId());
        for (OrderDetail orderDetail : addDetails)
            orderDetailService.addEntity(orderDetail);
        for (SplitInvoce splitInvoce : splitInvoces)
            serialNoSet.add(splitInvoce.getSerialNo());
        for (MergeInvoce mergeInvoce : mergeInvoces)
            serialNoSet.add(mergeInvoce.getSerialNo());
        for (RawInvoce rawInvoce : rawInvoces)
            serialNoSet.add(rawInvoce.getSerialNo());
        for(String serialNo : serialNoSet){//为了保持事务一致性,先保存发票,待开票成功后再更新发票信息
            InvoiceInfo invoiceInfo = new InvoiceInfo();
            invoiceInfo.setTaxNo(cuzSessionAttributes.getEnInfo().getTaxno());
            invoiceInfo.setSerialNo(serialNo);
            invoiceInfo.setShardingId(cuzSessionAttributes.getEnInfo().getShardingId());
            invoiceInfoService.addEntity(invoiceInfo);
        }
        return serialNoSet;
    }


    //根据税号分组
    private Map<String, List<OrderInfoClue>> groupByTaxno(List<OrderInfo> orderInfos) {
        if (cuzSessionAttributes.getEnInfo().getMaxAmount() == null)
            throw new RuntimeException("发票限额未设置!");
        //根据购方税号,分组订单主档
        Map<String, List<OrderInfoClue>> taxNoOrderInfosMap = new HashMap<>();

        for (OrderInfo orderInfo : orderInfos) {
            if (orderInfo.getBuyerTaxno() == null)
                throw new RuntimeException("此功能不能缺省购方税号!");
            if (taxNoOrderInfosMap.get(orderInfo.getBuyerTaxno()) == null)
                taxNoOrderInfosMap.put(orderInfo.getBuyerTaxno(), new ArrayList<OrderInfoClue>());
            List<OrderDetail> orderDetails = orderDetailService.findByOrderNo(orderInfo.getOrderNo());
            BigDecimal orderInfoAmount = new BigDecimal(0);
            Iterator<OrderDetail> iterator = orderDetails.iterator();
            while (iterator.hasNext()) {
                OrderDetail orderDetail = iterator.next();
                //如果已经有发票号码,那么应该存在代开发票,所以不拆分合并此类明细
                if (StringUtils.trimToNull(orderDetail.getSerialNo())!=null){
                    iterator.remove();
                    continue;
                }

                BigDecimal resultAmount = null;//计算折扣单价
                BigDecimal orginAmount = new BigDecimal(orderDetail.getAmount());
                if (OrderDetail.TaxIncludedFlag.INCLUDE.getCode().equals(orderDetail.getTaxIncluded())) {//如果含税
                    resultAmount = orginAmount;
                } else if (OrderDetail.TaxIncludedFlag.NOT_INCLUDED.getCode().equals(orderDetail.getTaxIncluded())) {//不含税
                    resultAmount = orginAmount.add(orginAmount.multiply(new BigDecimal(orderDetail.getTaxRate())));
                }
                orderInfoAmount = orderInfoAmount.add(resultAmount);
            }
            OrderInfoClue orderInfoClue = new OrderInfoClue(orderInfo, orderDetails, orderInfoAmount.toString());
            taxNoOrderInfosMap.get(orderInfo.getBuyerTaxno()).add(orderInfoClue);
        }
        return taxNoOrderInfosMap;
    }

    /**
     * 拆分方法
     */
    private List<SplitInvoce> split(OrderInfoClue orderInfoClue, List<OrderDetail> addDetails, Set<Integer> rmDetailIds) {
        BigDecimal orderTotalAmount = new BigDecimal(orderInfoClue.getTotalAmount());//订单金额
        BigDecimal maxAmount = new BigDecimal(cuzSessionAttributes.getEnInfo().getMaxAmount());
        BigDecimal[] result = orderTotalAmount.divideAndRemainder(maxAmount);
        //计算最少需要多少张发票
        int invoiceQty = result[1].compareTo(new BigDecimal(0)) == 1 ? Integer.parseInt(result[0].add(new BigDecimal("1")).setScale(0).toString()) : Integer.parseInt(result[0].setScale(0).toString());
        List<SplitInvoce> splitInvoces = new ArrayList<>(invoiceQty);
        for (int i = 0; i < invoiceQty; i++) {//创建跟发票数量相等的订单主档
            String serialNo = String.valueOf(idWorker.nextId());
            splitInvoces.add(new SplitInvoce(serialNo, maxAmount));//构建拆分发票帮助类
        }

        //需要拆分的明细
        List<OrderDetail> orderDetails = CloneUtils.clone((ArrayList) orderInfoClue.getOrderDetails());
        while (!orderDetails.isEmpty()) {//只要不为空,一直拆分
            for (int l = 0; l < splitInvoces.size(); l++) {
                for (int i = 0; i < orderDetails.size(); i++) {//循环往新创建的发票填充订单明细,总额不能超过限额,考虑折扣行
                    if (orderDetails.get(i) == null)
                        continue;
                    BigDecimal detailTaxAmount = null;
                    BigDecimal orginDetailAmount = new BigDecimal(orderDetails.get(i).getAmount());
                    if (OrderDetail.TaxIncludedFlag.INCLUDE.getCode().equals(orderDetails.get(i).getTaxIncluded())) {//如果含税
                        detailTaxAmount = orginDetailAmount;
                    } else if (OrderDetail.TaxIncludedFlag.NOT_INCLUDED.getCode().equals(orderDetails.get(i).getTaxIncluded())) {//不含税
                        detailTaxAmount = orginDetailAmount.add(orginDetailAmount.multiply(new BigDecimal(orderDetails.get(i).getTaxRate())));
                    }
                    //如果单条明细总额能放入发票余额,那么直接关联发票,并删除此明细
                    if (OrderDetail.InvoiceNature.NORMAL.getCode().equals(orderDetails.get(i).getInvoiceNature()) && detailTaxAmount.compareTo(splitInvoces.get(l).getBalance()) <= 0) {
                        //保存合并后的明细 替代单号,待保存
                        OrderDetail orderDetailTemp = CloneUtils.clone(orderDetails.get(i).setSerialNo(splitInvoces.get(l).getSerialNo()));
                        splitInvoces.get(l).getOrderNos().add(orderDetailTemp.getOrderNo());
                        rmDetailIds.add(orderDetailTemp.getId());//待删除的旧数据
                        orderDetailTemp.setId(null);
                        orderDetailTemp.setSplitMergeMark(OrderDetail.SplitMergeMark.SPLIT.getCode());//标识为已拆分
                        addDetails.add(orderDetailTemp);//待新增的新数据
                        splitInvoces.get(l).setBalance(splitInvoces.get(l).getBalance().subtract(new BigDecimal(orderDetails.get(i).getAmount())));
                        orderDetails.set(i, null);//删除已经合并的明细行
                        //如果是被折扣行,那么找出折扣行相加比较是否超出限额,如果不超过,同时合并折扣行与被折扣行
                    } else if (OrderDetail.InvoiceNature.DISCOUNTLINE.getCode().equals(orderDetails.get(i).getInvoiceNature())) {
                        for (int j = 0; j < orderDetails.size(); j++) {
                            if (orderDetails.get(j) == null || !OrderDetail.InvoiceNature.DISCOUNT.getCode().equals(orderDetails.get(j).getInvoiceNature()) || orderDetails.get(j).getDiscountLine() == null)
                                continue;
                            if (orderDetails.get(j).getDiscountLine().equals(orderDetails.get(i).getRowNum())) {
                                BigDecimal discountAmount = null;
                                BigDecimal orginAmount = new BigDecimal(orderDetails.get(i).getAmount()).add(new BigDecimal(orderDetails.get(j).getAmount()));
                                BigDecimal orderDetailTempAmount = null;
                                BigDecimal orginOrderDetailTempAmount = new BigDecimal(orderDetails.get(i).setSerialNo(splitInvoces.get(l).getSerialNo()).getAmount());
                                BigDecimal orderDetailTempAmount2 = null;
                                BigDecimal orginOrderDetailTempAmount2 = new BigDecimal(orderDetails.get(j).setSerialNo(splitInvoces.get(l).getSerialNo()).getAmount());
                                if (OrderDetail.TaxIncludedFlag.INCLUDE.getCode().equals(orderDetails.get(i).getTaxIncluded())) {//如果含税
                                    discountAmount = orginAmount;
                                    orderDetailTempAmount = orginOrderDetailTempAmount;
                                    orderDetailTempAmount2 = orginOrderDetailTempAmount2;

                                } else if (OrderDetail.TaxIncludedFlag.NOT_INCLUDED.getCode().equals(orderDetails.get(i).getTaxIncluded())) {//不含税
                                    discountAmount = orginAmount.add(orginAmount.multiply(new BigDecimal(orderDetails.get(i).getTaxRate())));
                                    orderDetailTempAmount = orginOrderDetailTempAmount.add(orginOrderDetailTempAmount.multiply(new BigDecimal(orderDetails.get(i).getTaxRate())));
                                    orderDetailTempAmount2 = orginOrderDetailTempAmount2.add(orginOrderDetailTempAmount2.multiply(new BigDecimal(orderDetails.get(j).getTaxRate())));
                                }
                                //如果被折行,折扣行总额相不超过
                                if (discountAmount.compareTo(splitInvoces.get(l).getBalance()) <= 0) {//如果全部打折
                                    //保存合并后的明细 替代单号,待保存
                                    OrderDetail orderDetailTemp = CloneUtils.clone(orderDetails.get(i).setSerialNo(splitInvoces.get(l).getSerialNo()));
                                    splitInvoces.get(l).getOrderNos().add(orderDetailTemp.getOrderNo());
                                    splitInvoces.get(l).setBalance(splitInvoces.get(l).getBalance().subtract(orderDetailTempAmount));
                                    rmDetailIds.add(orderDetailTemp.getId());//待删除的旧数据
                                    orderDetailTemp.setId(null);
                                    orderDetailTemp.setSplitMergeMark(OrderDetail.SplitMergeMark.SPLIT.getCode());//标识为已拆分
                                    addDetails.add(orderDetailTemp);//待新增的新数据
                                    OrderDetail orderDetailTemp2 = CloneUtils.clone(orderDetails.get(j).setSerialNo(splitInvoces.get(l).getSerialNo()));
                                    splitInvoces.get(l).getOrderNos().add(orderDetailTemp2.getOrderNo());
                                    splitInvoces.get(l).setBalance(splitInvoces.get(l).getBalance().subtract(orderDetailTempAmount2));
                                    rmDetailIds.add(orderDetailTemp2.getId());//待删除的旧数据
                                    orderDetailTemp2.setId(null);
                                    orderDetailTemp2.setSplitMergeMark(OrderDetail.SplitMergeMark.SPLIT.getCode());//标识为已拆分
                                    addDetails.add(orderDetailTemp2);//待新增的新数据
                                    orderDetails.set(j, null);//删除已经拆分的明细行
                                    orderDetails.set(i, null);//删除已经拆分的明细行
                                } else {//如果总额超过
                                    for (int k = 0; k < orderDetails.size(); k++) {
                                        if (orderDetails.get(k) == null || !OrderDetail.InvoiceNature.DISCOUNT.getCode().equals(orderDetails.get(k).getInvoiceNature()) || orderDetails.get(k).getDiscountLine() == null)
                                            continue;
                                        if (orderDetails.get(k).getDiscountLine().equals(orderDetails.get(i).getRowNum())) {//找到对应的折扣行
                                            if (orderDetails.get(k).getItemNum().equals(orderDetails.get(i).getItemNum())) {//如果全部打折
                                                BigDecimal discountPrice = null;//计算折扣单价
                                                BigDecimal orginPrice = new BigDecimal(orderDetails.get(i).getItemPrice()).add(new BigDecimal(orderDetails.get(k).getItemPrice()));
                                                BigDecimal discountLineItemPrice = null;
                                                BigDecimal orginDiscountLineItemPrice = new BigDecimal(orderDetails.get(i).getItemPrice());
                                                BigDecimal discountItemPrice = null;
                                                BigDecimal orginDiscountItemPrice = new BigDecimal(orderDetails.get(k).getItemPrice());
                                                if (OrderDetail.TaxIncludedFlag.INCLUDE.getCode().equals(orderDetails.get(i).getTaxIncluded())) {//如果含税
                                                    discountPrice = orginPrice;
                                                    discountLineItemPrice = orginDiscountLineItemPrice;
                                                    discountItemPrice = orginDiscountItemPrice;
                                                } else if (OrderDetail.TaxIncludedFlag.NOT_INCLUDED.getCode().equals(orderDetails.get(i).getTaxIncluded())) {//不含税
                                                    discountPrice = orginPrice.add(orginPrice.multiply(new BigDecimal(orderDetails.get(i).getTaxRate())));
                                                    discountLineItemPrice = orginDiscountLineItemPrice.add(orginDiscountLineItemPrice.multiply(new BigDecimal(orderDetails.get(i).getTaxRate())));
                                                    discountItemPrice = orginDiscountItemPrice.add(orginDiscountItemPrice.multiply(new BigDecimal(orderDetails.get(k).getTaxRate())));
                                                }
                                                BigDecimal detailNum = splitInvoces.get(l).getBalance().divideAndRemainder(discountPrice)[0];//取还能放下多少数量
                                                if (detailNum.compareTo(new BigDecimal("0")) != 0) {
                                                    //-------------被折扣行
                                                    OrderDetail splitedDiscountLineDetail = CloneUtils.clone(orderDetails.get(i));//折扣行
                                                    splitedDiscountLineDetail.setSerialNo(splitInvoces.get(l).getSerialNo());
                                                    splitedDiscountLineDetail.setItemNum(detailNum.toString());//拆分数量
//                                                    BigDecimal discountLineItemPrice = new BigDecimal(splitedDiscountLineDetail.getItemPrice());
                                                    splitedDiscountLineDetail.setAmount(detailNum.multiply(orginDiscountLineItemPrice).toString());//拆分金额
//                        rmDetailIds.add(splitedDetail.getId());//待删除的旧数据,这里不用删,因为剩余的会在上一个判断中删除
                                                    splitedDiscountLineDetail.setId(null);
                                                    orderDetails.get(i).setItemNum(new BigDecimal(orderDetails.get(i).getItemNum()).subtract(detailNum).toString());//设置剩余的数量
                                                    orderDetails.get(i).setAmount(new BigDecimal(orderDetails.get(i).getAmount()).subtract(discountLineItemPrice).toString());//设置剩余总额
                                                    addDetails.add(splitedDiscountLineDetail);
                                                    splitInvoces.get(l).setBalance(splitInvoces.get(l).getBalance().subtract(discountLineItemPrice));//计算余额
                                                    //-------------折扣行
                                                    OrderDetail splitedDiscountDetail = CloneUtils.clone(orderDetails.get(k));//折扣行
                                                    splitedDiscountDetail.setSerialNo(splitInvoces.get(l).getSerialNo());
                                                    splitedDiscountDetail.setItemNum(detailNum.toString());//拆分数量
                                                    BigDecimal discountItemAmount = detailNum.multiply(orginDiscountItemPrice);
                                                    splitedDiscountDetail.setAmount(discountItemAmount.toString());//拆分金额
//                        rmDetailIds.add(splitedDetail.getId());//待删除的旧数据,这里不用删,因为剩余的会在上一个判断中删除
                                                    splitedDiscountDetail.setId(null);
                                                    orderDetails.get(i).setItemNum(new BigDecimal(orderDetails.get(k).getItemNum()).subtract(detailNum).toString());//设置剩余的数量
                                                    orderDetails.get(i).setAmount(new BigDecimal(orderDetails.get(k).getAmount()).subtract(discountItemAmount).toString());//设置剩余总额
                                                    addDetails.add(splitedDiscountDetail);
                                                    splitInvoces.get(l).setBalance(splitInvoces.get(l).getBalance().subtract(detailNum.multiply(discountItemPrice)));//计算余额
                                                }

                                            } else {//如果部分打折,按照数量拆分,然后添加到待拆分的集合中,继续处理
                                                OrderDetail discountDetail = CloneUtils.clone(orderDetails.get(k));//折扣行

                                                //-------------折扣行
                                                OrderDetail discountLineDetail = CloneUtils.clone(orderDetails.get(i));//被折扣行
//                                        splitedDiscountLineDetail.setSerialNo(splitInvoce.getSerialNo());//可以不设置,最终会循环到上面的处理
                                                BigDecimal discountItemNum = new BigDecimal(discountDetail.getItemNum());//打折的数量
                                                BigDecimal allNum = new BigDecimal(discountLineDetail.getItemNum());//被折扣行总数量
                                                BigDecimal noDiscountNum = allNum.subtract(discountItemNum);//没有打折的数量
                                                discountLineDetail.setItemNum(noDiscountNum.toString());//设置为没有打折的数量
                                                BigDecimal discountLineItemPrice = new BigDecimal(discountLineDetail.getItemPrice());//被折扣行单价
                                                BigDecimal noDisCountAmount = noDiscountNum.multiply(discountLineItemPrice);
                                                discountLineDetail.setAmount(noDisCountAmount.toString());//拆分金额,这里是没有被打折的总金额
                                                discountLineDetail.setInvoiceNature(OrderDetail.InvoiceNature.NORMAL.getCode());//标示为正常发票性质
//                        rmDetailIds.add(splitedDetail.getId());//待删除的旧数据,这里不用删,因为剩余的会在上一个判断中删除
//                                        splitedDiscountLineDetail.setId(null);
                                                orderDetails.get(i).setItemNum(new BigDecimal(orderDetails.get(i).getItemNum()).subtract(noDiscountNum).toString());//设置剩余的数量,这里的数量取打折数量
                                                orderDetails.get(i).setAmount(new BigDecimal(orderDetails.get(i).getAmount()).subtract(noDisCountAmount).toString());//设置剩余总额
                                                orderDetails.add(discountLineDetail);//设置回待拆分集合
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {//如果单条明细总额已经超过限额,那么按照单价拆分明细
                        if (OrderDetail.InvoiceNature.NORMAL.getCode().equals(orderDetails.get(i).getInvoiceNature())) {//分折扣行处理
                            BigDecimal itemPrice = null;
                            BigDecimal orginPrice = new BigDecimal(orderDetails.get(i).getItemPrice());
                            if (OrderDetail.TaxIncludedFlag.INCLUDE.getCode().equals(orderDetails.get(i).getTaxIncluded())) {//如果含税
                                itemPrice = orginPrice;
                            } else if (OrderDetail.TaxIncludedFlag.NOT_INCLUDED.getCode().equals(orderDetails.get(i).getTaxIncluded())) {//不含税
                                itemPrice = orginPrice.add(orginPrice.multiply(new BigDecimal(orderDetails.get(i).getTaxRate())));
                            }
                            BigDecimal detailNum = splitInvoces.get(l).getBalance().divideAndRemainder(itemPrice)[0];//取还能放下多少数量
                            if (detailNum.compareTo(new BigDecimal("0")) != 0) {
                                OrderDetail splitedDetail = CloneUtils.clone(orderDetails.get(i));//已经被拆分的明细
                                splitedDetail.setSerialNo(splitInvoces.get(l).getSerialNo());
                                splitedDetail.setItemNum(detailNum.toString());//拆分数量
                                splitedDetail.setAmount(detailNum.multiply(orginPrice).toString());//拆分金额
                                splitInvoces.get(l).getOrderNos().add(splitedDetail.getOrderNo());
                                splitInvoces.get(l).setBalance(splitInvoces.get(l).getBalance().subtract(new BigDecimal(detailNum.multiply(itemPrice).toString())));//注意,要减去含税金额
//                        rmDetailIds.add(splitedDetail.getId());//待删除的旧数据,这里不用删,因为剩余的会在上一个判断中删除
                                splitedDetail.setId(null);
                                orderDetails.get(i).setItemNum(new BigDecimal(orderDetails.get(i).getItemNum()).subtract(detailNum).toString());//设置剩余的数量
                                orderDetails.get(i).setAmount(new BigDecimal(orderDetails.get(i).getAmount()).subtract(detailNum.multiply(orginPrice)).toString());//设置剩余总额
                                addDetails.add(splitedDetail);
                            } else {
                                boolean newInvoiceFlag = true;
                                for (SplitInvoce splitInvoce2 : splitInvoces) {
                                    if (splitInvoce2.getBalance().compareTo(orginPrice) > 0) {
                                        newInvoiceFlag = false;
                                    }
                                }
                                if (newInvoiceFlag) {
                                    splitInvoces.add(new SplitInvoce(String.valueOf(idWorker.nextId()), maxAmount));
                                }
                            }
                        }
                    }
                }
                orderDetails.removeAll(Collections.singleton(null));
            }
        }
        return splitInvoces;
    }


    /**
     * 合并方法
     * 把能合并的往其中一个订单中合并,然后删除被合并的,合并其他订单的订单,标记为合并
     * 因订单明细,依然保留了合并前的单号,这里不做处理,因为返回后需要根据这些被合并的订单号,更新被合并的订单标识
     * 这里不创建新的订单,待返回以后根据合并标识创建新订单
     */
    private void merge(List<OrderInfoClue> orderInfoClues) {
        boolean continueFlag = false;//循环标识
        for (int i = 0; i < orderInfoClues.size(); i++) {
            for (int j = 0; j < orderInfoClues.size(); j++) {
                if (orderInfoClues.get(i) != null && orderInfoClues.get(j) != null && i != j) {//不比较自己
                    BigDecimal totalAmountTem = new BigDecimal(orderInfoClues.get(i).getTotalAmount()).add(new BigDecimal(orderInfoClues.get(j).getTotalAmount()));
                    if (totalAmountTem.compareTo(new BigDecimal(cuzSessionAttributes.getEnInfo().getMaxAmount())) <= 0) {//可以合并
                        List<OrderDetail> orderDetails = CloneUtils.clone((ArrayList<OrderDetail>) orderInfoClues.get(j).getOrderDetails());//拷贝被合并的明细
                        /**
                         * 这里不更新明细单号,为了后面可以根据原始明细单号,统计哪些订单已经被合并
                         * 补充,把发票号码写入明细表,所以这里更不需要更改明细的订单号
                         */
//                    for(OrderDetail orderDetail : orderDetails){//更新被合并后的明细订单号
//                        orderDetail.setOrderNo(orderInfoClue.getOrderInfo().getOrderNo());
//                    }
                        orderInfoClues.get(i).setTotalAmount(totalAmountTem.toString());
                        orderInfoClues.get(i).getOrderDetails().addAll(orderDetails);
                        orderInfoClues.get(i).setSplitMergeMark(OrderDetail.SplitMergeMark.MERGE);//标识已经合并
                        orderInfoClues.set(j, null);//删除已经被合并的订单
                        continueFlag = true;
                    }
                }
            }
        }
        orderInfoClues.removeAll(Collections.singleton(null));
        if (continueFlag)//如果循环标识成立继续比较
            merge(orderInfoClues);
    }


    /**
     * 拆分订单后发票帮助类
     */
    public static class SplitInvoce implements Serializable {

        public SplitInvoce(String serialNo, BigDecimal balance) {
            this.serialNo = serialNo;
            this.balance = balance;
            this.orderNos = new HashSet<>();
        }

        //发票流水号
        private String serialNo;
        //发票限额
        private BigDecimal balance;

        private Set<String> orderNos;

        //-----------------getter and setter----------------------------


        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance.setScale(6, RoundingMode.HALF_UP);
        }

        public Set<String> getOrderNos() {
            return orderNos;
        }

        public void setOrderNos(Set<String> orderNos) {
            this.orderNos = orderNos;
        }
    }

    /**
     * 合并订单后发票帮助类
     */
    public static class MergeInvoce implements Serializable {

        public MergeInvoce(String serialNo) {
            this.serialNo = serialNo;
            this.orderNos = new HashSet<>();
        }

        //发票流水号
        private String serialNo;

        private Set<String> orderNos;

        //-----------------getter and setter----------------------------


        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public Set<String> getOrderNos() {
            return orderNos;
        }

        public void setOrderNos(Set<String> orderNos) {
            this.orderNos = orderNos;
        }
    }

    /**
     * 原装订单后发票帮助类
     */
    public static class RawInvoce implements Serializable {

        public RawInvoce(String serialNo, String orderNo) {
            this.serialNo = serialNo;
            this.orderNo = orderNo;
        }

        //发票流水号
        private String serialNo;

        private String orderNo;

        //-----------------getter and setter----------------------------


        public String getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(String serialNo) {
            this.serialNo = serialNo;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }
    }

    /**
     * 线索数据内部类
     */
    public static class OrderInfoClue implements Serializable {

        public OrderInfoClue(OrderInfo orderInfo, List<OrderDetail> orderDetails, String totalAmount) {
            this.orderInfo = orderInfo;
            //根据单价倒叙
            Collections.sort(orderDetails, new Comparator<OrderDetail>() {
                @Override
                public int compare(OrderDetail o1, OrderDetail o2) {
                    return new BigDecimal(o2.getItemPrice()).compareTo(new BigDecimal(o1.getItemPrice()));
                }
            });
            this.orderDetails = orderDetails;
            this.totalAmount = totalAmount;
            this.splitMergeMark = OrderDetail.SplitMergeMark.RAW;
        }


        private OrderInfo orderInfo;
        private String totalAmount;

        private OrderDetail.SplitMergeMark splitMergeMark;

        private List<OrderDetail> orderDetails;

        //-------------getter and setter--------------------------------------


        public OrderInfo getOrderInfo() {
            return orderInfo;
        }

        public void setOrderInfo(OrderInfo orderInfo) {
            this.orderInfo = orderInfo;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public List<OrderDetail> getOrderDetails() {
            return orderDetails;
        }

        public OrderDetail.SplitMergeMark getSplitMergeMark() {
            return splitMergeMark;
        }

        public void setSplitMergeMark(OrderDetail.SplitMergeMark splitMergeMark) {
            this.splitMergeMark = splitMergeMark;
        }
    }


}
