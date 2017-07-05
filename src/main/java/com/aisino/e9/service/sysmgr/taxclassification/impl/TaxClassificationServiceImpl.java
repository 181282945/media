package com.aisino.e9.service.sysmgr.taxclassification.impl;

import com.aisino.configuration.RedisCacheConfig;
import com.aisino.core.service.impl.BaseServiceImpl;
import com.aisino.e9.dao.sysmgr.taxclassification.TaxClassificationMapper;
import com.aisino.e9.entity.taxclassification.pojo.TaxClassification;
import com.aisino.e9.service.sysmgr.taxclassification.TaxClassificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;


/**
 * Created by 为 on 2017-6-13.
 */
@Service("taxClassificationService")
public class TaxClassificationServiceImpl extends BaseServiceImpl<TaxClassification, TaxClassificationMapper> implements TaxClassificationService {

    Logger logger = LoggerFactory.getLogger(RedisCacheConfig.class);


    @Override
    public void createTaxClassifiJS() {
        List<TaxClassification> allTaxClassification = this.findAll();
        StringBuilder sb = new StringBuilder();
        sb.append("var allTaxClassification = [");
        for (int i = 0; i < allTaxClassification.size() - 1; i++) {
            sb.append("'");
            sb.append(allTaxClassification.get(i).getCode());
            sb.append(":");
            sb.append(allTaxClassification.get(i).getName());
            sb.append(":");
            sb.append(allTaxClassification.get(i).getKeyword());
            sb.append("'");
            sb.append(",");
        }
        sb.append("'");
        sb.append(allTaxClassification.get(allTaxClassification.size() - 1).getCode());
        sb.append(":");
        sb.append(allTaxClassification.get(allTaxClassification.size() - 1).getName());
        sb.append(":");
        sb.append(allTaxClassification.get(allTaxClassification.size() - 1).getKeyword());
        sb.append("'");
        sb.append("];");

//        InputStream inputStream = this.getClass().getResourceAsStream("/static/js/base/invoice/invoiceinfo/u/taxclassification.js");
        logger.info(ClassLoader.getSystemResource("").getPath());
        logger.info(this.getClass().getResource("").getPath());
        File file = new File(ClassLoader.getSystemResource("").getPath() + "static/js/base/invoice/invoiceinfo/u/taxclassification.js");
        try {
            BufferedWriter bf = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            bf.write(sb.toString());
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//
//        String root = System.getProperty("user.dir");
//        String path = root +"\\out.txt";
//        URL url = ClassLoader.getSystemResource("");
//        URL base = TaxClassificationServiceImpl.class.getResource("");
//        System.out.println(ClassLoader.getSystemResource("").getPath()+"static/js/base/invoice/invoiceinfo/u/taxclassification.js");
//        System.out.println(TaxClassificationServiceImpl.class.getResource("").getFile());
//    }

}
