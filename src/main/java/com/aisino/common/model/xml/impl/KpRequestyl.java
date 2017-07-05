package com.aisino.common.model.xml.impl;


import com.aisino.common.model.xml.BaseXmlModel;
import com.aisino.e9.entity.parameter.pojo.Parameter;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by 为 on 2017-5-8.
 */
public class KpRequestyl {

    @XmlRootElement(name = "REQUEST_FPKJXX")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class RequestFpkjxx extends BaseXmlModel {

        @XmlElement(name = "FPKJXX_FPTXX", type = FpkjxxFptxx.class)
        private FpkjxxFptxx fpkjxxFptxx;

        @XmlElement(name = "FPKJXX_XMXXS", type = FpkjxxXmxxs.class)
        private FpkjxxXmxxs fpkjxxXmxxs;

        @XmlElement(name = "FPKJXX_DDXX", type = FpkjxxDdxx.class)
        private FpkjxxDdxx fpkjxxDdxx;

        @XmlElement(name = "FPKJXX_DDMXXXS", type = FpkjxxDdmxxxs.class)
        private FpkjxxDdmxxxs fpkjxxDdmxxxs;

        @XmlElement(name = "FPKJXX_ZFXX", type = FpkjxxZfxx.class)
        private FpkjxxZfxx fpkjxxZfxx;

        @XmlElement(name = "FPKJXX_WLXX", type = FpkjxxWlxx.class)
        private FpkjxxWlxx fpkjxxWlxx;

        public FpkjxxFptxx getFpkjxxFptxx() {
            return fpkjxxFptxx;
        }

        public void setFpkjxxFptxx(FpkjxxFptxx fpkjxxFptxx) {
            this.fpkjxxFptxx = fpkjxxFptxx;
        }

        public FpkjxxXmxxs getFpkjxxXmxxs() {
            return fpkjxxXmxxs;
        }

        public void setFpkjxxXmxxs(FpkjxxXmxxs fpkjxxXmxxs) {
            this.fpkjxxXmxxs = fpkjxxXmxxs;
        }

        public FpkjxxDdxx getFpkjxxDdxx() {
            return fpkjxxDdxx;
        }

        public void setFpkjxxDdxx(FpkjxxDdxx fpkjxxDdxx) {
            this.fpkjxxDdxx = fpkjxxDdxx;
        }

        public FpkjxxDdmxxxs getFpkjxxDdmxxxs() {
            return fpkjxxDdmxxxs;
        }

        public void setFpkjxxDdmxxxs(FpkjxxDdmxxxs fpkjxxDdmxxxs) {
            this.fpkjxxDdmxxxs = fpkjxxDdmxxxs;
        }

        public FpkjxxZfxx getFpkjxxZfxx() {
            return fpkjxxZfxx;
        }

        public void setFpkjxxZfxx(FpkjxxZfxx fpkjxxZfxx) {
            this.fpkjxxZfxx = fpkjxxZfxx;
        }

        public FpkjxxWlxx getFpkjxxWlxx() {
            return fpkjxxWlxx;
        }

        public void setFpkjxxWlxx(FpkjxxWlxx fpkjxxWlxx) {
            this.fpkjxxWlxx = fpkjxxWlxx;
        }
    }

    @XmlRootElement(name = "FPKJXX_FPTXX")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FpkjxxFptxx extends BaseXmlModel {
        @XmlAttribute(name = "class")
        private String className = "FPKJXX_FPTXX";

        //发票请求唯一流水号
        @XmlElement(name = "FPQQLSH")
        private String fpqqlsh;

        //平台编码
        @XmlElement(name = "DSPTBM")
        private String dsptbm;

        //开票方识别码
        @XmlElement(name = "NSRSBH")
        private String nsrsbh;

        //开票方名称
        @XmlElement(name = "NSRMC")
        private String nsrmc;

        //开票方电子档案号
        @XmlElement(name = "NSRDZDAH")
        private String nsrdzdah;

        //税务机构代码
        @XmlElement(name = "SWJG_DM")
        private String swjgdm;

        //代开标志
        @XmlElement(name = "DKBZ")
        private String dkbz;

        //票样代码
        @XmlElement(name = "PYDM")
        private String pydm;

        //主要开票项目
        @XmlElement(name = "KPXM")
        private String kpxm;

        //编码表版本号
        @XmlElement(name = "BMB_BBH")
        private String bmbbbh;

        //销货方识别号
        @XmlElement(name = "XHF_NSRSBH")
        private String xhfnsrsbh;

        //销货方名称
        @XmlElement(name = "XHFMC")
        private String xhfmc;

        //销货方地址
        @XmlElement(name = "XHF_DZ")
        private String xhfdz;

        //销货方电话
        @XmlElement(name = "XHF_DH")
        private String xhfdh;

        //销货方银行账号
        @XmlElement(name = "XHF_YHZH")
        private String xhfyhzh;

        //购货方名称
        @XmlElement(name = "GHFMC")
        private String ghfmc;

        //购货方识别号
        @XmlElement(name = "GHF_NSRSBH")
        private String ghfnsrsbh;

        //购货方省份
        @XmlElement(name = "GHF_SF")
        private String ghfsf;

        //购货方地址
        @XmlElement(name = "GHF_DZ")
        private String ghfdz;

        //购货方固定电话
        @XmlElement(name = "GHF_GDDH")
        private String ghfgddh;

        //购货方手机
        @XmlElement(name = "GHF_SJ")
        private String ghfsj;

        //购货方邮箱
        @XmlElement(name = "GHF_EMAIL")
        private String ghfemail;

        //01
        @XmlElement(name = "GHFQYLX")
        private String ghfqylx;

        //01
        @XmlElement(name = "GHF_YHZH")
        private String ghfyhzh;

        //行业代码
        @XmlElement(name = "HY_DM")
        private String hydm;

        //行业名称
        @XmlElement(name = "HY_MC")
        private String hymc;

        //开票员
        @XmlElement(name = "KPY")
        private String kpy;

        //收款员
        @XmlElement(name = "SKY")
        private String sky;

        //复核人
        @XmlElement(name = "FHR")
        private String fhr;

        //开票日期
        @XmlElement(name = "KPRQ")
        private String kprq;

        //开票类型
        @XmlElement(name = "KPLX")
        private String kplx;

        //原发票代码
        @XmlElement(name = "YFP_DM")
        private String yfpdm;

        //原发票号码
        @XmlElement(name = "YFP_HM")
        private String yfphm;

        //操作代码
        @XmlElement(name = "CZDM")
        private String czdm;

        //清单标志
        @XmlElement(name = "QD_BZ")
        private String qdbz;

        //清单发票项目名称
        @XmlElement(name = "QDXMMC")
        private String qdxmmc;

        //冲红原因
        @XmlElement(name = "CHYY")
        private String chyy;

        //特殊冲红标志
        @XmlElement(name = "TSCHBZ")
        private String tschbz;

        //价税合计金额
        @XmlElement(name = "KPHJJE")
        private String kphjje;

        //合计不含税金额
        @XmlElement(name = "HJBHSJE")
        private String hjbhsje;

        //合计税额
        @XmlElement(name = "HJSE")
        private String hjse;

        //备注
        @XmlElement(name = "BZ")
        private String bz;

        //备用字段1
        @XmlElement(name = "BYZD1")
        private String byzd1;
        //备用字段2
        @XmlElement(name = "BYZD2")
        private String byzd2;
        //备用字段3
        @XmlElement(name = "BYZD3")
        private String byzd3;
        //备用字4
        @XmlElement(name = "BYZD4")
        private String byzd4;
        //备用字段5
        @XmlElement(name = "BYZD5")
        private String byzd5;


        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getFpqqlsh() {
            return fpqqlsh;
        }

        public void setFpqqlsh(String fpqqlsh) {
            this.fpqqlsh = fpqqlsh;
        }

        public String getDsptbm() {
            return dsptbm;
        }

        public void setDsptbm(String dsptbm) {
            this.dsptbm = dsptbm;
        }

        public String getNsrsbh() {
            return nsrsbh;
        }

        public void setNsrsbh(String nsrsbh) {
            this.nsrsbh = nsrsbh;
        }

        public String getNsrmc() {
            return nsrmc;
        }

        public void setNsrmc(String nsrmc) {
            this.nsrmc = nsrmc;
        }

        public String getNsrdzdah() {
            return nsrdzdah;
        }

        public void setNsrdzdah(String nsrdzdah) {
            this.nsrdzdah = nsrdzdah;
        }

        public String getSwjgdm() {
            return swjgdm;
        }

        public void setSwjgdm(String swjgdm) {
            this.swjgdm = swjgdm;
        }

        public String getDkbz() {
            return dkbz;
        }

        public void setDkbz(String dkbz) {
            this.dkbz = dkbz;
        }

        public String getPydm() {
            return pydm;
        }

        public void setPydm(String pydm) {
            this.pydm = pydm;
        }

        public String getKpxm() {
            return kpxm;
        }

        public void setKpxm(String kpxm) {
            this.kpxm = kpxm;
        }

        public String getBmbbbh() {
            return bmbbbh;
        }

        public void setBmbbbh(String bmbbbh) {
            this.bmbbbh = bmbbbh;
        }

        public String getXhfnsrsbh() {
            return xhfnsrsbh;
        }

        public void setXhfnsrsbh(String xhfnsrsbh) {
            this.xhfnsrsbh = xhfnsrsbh;
        }

        public String getXhfmc() {
            return xhfmc;
        }

        public void setXhfmc(String xhfmc) {
            this.xhfmc = xhfmc;
        }

        public String getXhfdz() {
            return xhfdz;
        }

        public void setXhfdz(String xhfdz) {
            this.xhfdz = xhfdz;
        }

        public String getXhfdh() {
            return xhfdh;
        }

        public void setXhfdh(String xhfdh) {
            this.xhfdh = xhfdh;
        }

        public String getXhfyhzh() {
            return xhfyhzh;
        }

        public void setXhfyhzh(String xhfyhzh) {
            this.xhfyhzh = xhfyhzh;
        }

        public String getGhfmc() {
            return ghfmc;
        }

        public void setGhfmc(String ghfmc) {
            this.ghfmc = ghfmc;
        }

        public String getGhfnsrsbh() {
            return ghfnsrsbh;
        }

        public void setGhfnsrsbh(String ghfnsrsbh) {
            this.ghfnsrsbh = ghfnsrsbh;
        }

        public String getGhfsf() {
            return ghfsf;
        }

        public void setGhfsf(String ghfsf) {
            this.ghfsf = ghfsf;
        }

        public String getGhfdz() {
            return ghfdz;
        }

        public void setGhfdz(String ghfdz) {
            this.ghfdz = ghfdz;
        }

        public String getGhfgddh() {
            return ghfgddh;
        }

        public void setGhfgddh(String ghfgddh) {
            this.ghfgddh = ghfgddh;
        }

        public String getGhfsj() {
            return ghfsj;
        }

        public void setGhfsj(String ghfsj) {
            this.ghfsj = ghfsj;
        }

        public String getGhfemail() {
            return ghfemail;
        }

        public void setGhfemail(String ghfemail) {
            this.ghfemail = ghfemail;
        }

        public String getGhfqylx() {
            return ghfqylx;
        }

        public void setGhfqylx(String ghfqylx) {
            this.ghfqylx = ghfqylx;
        }

        public String getGhfyhzh() {
            return ghfyhzh;
        }

        public void setGhfyhzh(String ghfyhzh) {
            this.ghfyhzh = ghfyhzh;
        }

        public String getHydm() {
            return hydm;
        }

        public void setHydm(String hydm) {
            this.hydm = hydm;
        }

        public String getHymc() {
            return hymc;
        }

        public void setHymc(String hymc) {
            this.hymc = hymc;
        }

        public String getKpy() {
            return kpy;
        }

        public void setKpy(String kpy) {
            this.kpy = kpy;
        }

        public String getSky() {
            return sky;
        }

        public void setSky(String sky) {
            this.sky = sky;
        }

        public String getFhr() {
            return fhr;
        }

        public void setFhr(String fhr) {
            this.fhr = fhr;
        }

        public String getKprq() {
            return kprq;
        }

        public void setKprq(String kprq) {
            this.kprq = kprq;
        }

        public String getKplx() {
            return kplx;
        }

        public void setKplx(String kplx) {
            this.kplx = kplx;
        }

        public String getYfpdm() {
            return yfpdm;
        }

        public void setYfpdm(String yfpdm) {
            this.yfpdm = yfpdm;
        }

        public String getYfphm() {
            return yfphm;
        }

        public void setYfphm(String yfphm) {
            this.yfphm = yfphm;
        }

        public String getCzdm() {
            return czdm;
        }

        public void setCzdm(String czdm) {
            this.czdm = czdm;
        }

        public String getQdbz() {
            return qdbz;
        }

        public void setQdbz(String qdbz) {
            this.qdbz = qdbz;
        }

        public String getQdxmmc() {
            return qdxmmc;
        }

        public void setQdxmmc(String qdxmmc) {
            this.qdxmmc = qdxmmc;
        }

        public String getChyy() {
            return chyy;
        }

        public void setChyy(String chyy) {
            this.chyy = chyy;
        }

        public String getTschbz() {
            return tschbz;
        }

        public void setTschbz(String tschbz) {
            this.tschbz = tschbz;
        }

        public String getKphjje() {
            return kphjje;
        }

        public void setKphjje(String kphjje) {
            this.kphjje = kphjje;
        }

        public String getHjbhsje() {
            return hjbhsje;
        }

        public void setHjbhsje(String hjbhsje) {
            this.hjbhsje = hjbhsje;
        }

        public String getHjse() {
            return hjse;
        }

        public void setHjse(String hjse) {
            this.hjse = hjse;
        }

        public String getBz() {
            return bz;
        }

        public void setBz(String bz) {
            this.bz = bz;
        }

        public String getByzd1() {
            return byzd1;
        }

        public void setByzd1(String byzd1) {
            this.byzd1 = byzd1;
        }

        public String getByzd2() {
            return byzd2;
        }

        public void setByzd2(String byzd2) {
            this.byzd2 = byzd2;
        }

        public String getByzd3() {
            return byzd3;
        }

        public void setByzd3(String byzd3) {
            this.byzd3 = byzd3;
        }

        public String getByzd4() {
            return byzd4;
        }

        public void setByzd4(String byzd4) {
            this.byzd4 = byzd4;
        }

        public String getByzd5() {
            return byzd5;
        }

        public void setByzd5(String byzd5) {
            this.byzd5 = byzd5;
        }


        //----------------------枚举----------------------------

        public enum CzdmType {
            NORMAL("10", "正票正常开具"), REDO("11", "正票错票重开"), RETURN_RED("20", "退货折让红票"), REDO_RED("21", "错票重开红票"), RED("22", "换票冲红");

            //状态代码
            private String code;
            //状态名称
            private String name;

            //构造方法
            CzdmType(String code, String name) {
                this.code = code;
                this.name = name;
            }

            //根据code获取状态名称
            public static String getNameByCode(String code) {
                for (CzdmType item : CzdmType.values()) {
                    if (item.getCode().equals(code)) {
                        return item.getName();
                    }
                }
                return "";
            }

            public static Parameter[] getParams() {
                Parameter[] czdmTypeParams = new Parameter[CzdmType.values().length];

                for (int i = 0; i < czdmTypeParams.length; i++) {
                    czdmTypeParams[i] = new Parameter(CzdmType.values()[i].getCode(), CzdmType.values()[i].getName());
                }
                return czdmTypeParams;
            }


            //-----------------------------------getter and setter---------------------------------------------------------


            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }


    }

    @XmlRootElement(name = "FPKJXX_XMXXS")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FpkjxxXmxxs extends BaseXmlModel {
        @XmlAttribute(name = "class")
        private String className = "FPKJXX_XMXX;";

        @XmlAttribute(name = "size")
        private String size;


        @XmlElement(name = "FPKJXX_XMXX", type = FpkjxxXmxx.class)
        private List<FpkjxxXmxx> fpkjxxXmxxList;

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public List<FpkjxxXmxx> getFpkjxxXmxxList() {
            return fpkjxxXmxxList;
        }

        public void setFpkjxxXmxxList(List<FpkjxxXmxx> fpkjxxXmxxList) {
            this.fpkjxxXmxxList = fpkjxxXmxxList;
        }
    }

    /**
     * 项目信息（ 发票明细）（ 多条）
     */
    @XmlRootElement(name = "FPKJXX_XMXX")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FpkjxxXmxx extends BaseXmlModel {

        //项目名称
        @XmlElement(name = "XMMC")
        private String xmmc;

        //项目单位
        @XmlElement(name = "XMDW")
        private String xmdw;

        //规格型号
        @XmlElement(name = "GGXH")
        private String ggxh;

        //项目数量
        @XmlElement(name = "XMSL")
        private String xmsl;

        //含税标志
        @XmlElement(name = "HSBZ")
        private String hsbz;

        //项目单价
        @XmlElement(name = "XMDJ")
        private String xmdj;

        //发票行性质
        @XmlElement(name = "FPHXZ")
        private String fphxz;

        //商品编码
        @XmlElement(name = "SPBM")
        private String spbm;

        //自行编码
        @XmlElement(name = "ZXBM")
        private String zxbm;

        //优惠政策标识
        @XmlElement(name = "YHZCBS")
        private String yhzcbs;

        //零税率标识
        @XmlElement(name = "LSLBS")
        private String lslbs;

        //增值税特殊管理
        @XmlElement(name = "ZZSTSGL")
        private String zzstsgl;

        //项目金额
        @XmlElement(name = "XMJE")
        private String xmje;

        //税率
        @XmlElement(name = "SL")
        private String sl;

        //税额
        @XmlElement(required = true,name = "SE")
        private String se;

        //备用字段
        @XmlElement(required = true,name = "BYZD1")
        private String byzd1;

        //备用字段2
        @XmlElement(required = true,name = "BYZD2")
        private String byzd2;

        //备用字段3
        @XmlElement(required = true,name = "BYZD3")
        private String byzd3;

        //备用字段4
        @XmlElement(required = true,name = "BYZD4")
        private String byzd4;

        //备用字段5
        @XmlElement(required = true,name = "BYZD5")
        private String byzd5;


        public String getXmmc() {
            return xmmc;
        }

        public void setXmmc(String xmmc) {
            this.xmmc = xmmc;
        }

        public String getXmdw() {
            return xmdw;
        }

        public void setXmdw(String xmdw) {
            this.xmdw = xmdw;
        }

        public String getGgxh() {
            return ggxh;
        }

        public void setGgxh(String ggxh) {
            this.ggxh = ggxh;
        }

        public String getXmsl() {
            return xmsl;
        }

        public void setXmsl(String xmsl) {
            this.xmsl = xmsl;
        }

        public String getHsbz() {
            return hsbz;
        }

        public void setHsbz(String hsbz) {
            this.hsbz = hsbz;
        }

        public String getXmdj() {
            return xmdj;
        }

        public void setXmdj(String xmdj) {
            this.xmdj = xmdj;
        }

        public String getFphxz() {
            return fphxz;
        }

        public void setFphxz(String fphxz) {
            this.fphxz = fphxz;
        }

        public String getSpbm() {
            return spbm;
        }

        public void setSpbm(String spbm) {
            this.spbm = spbm;
        }

        public String getZxbm() {
            return zxbm;
        }

        public void setZxbm(String zxbm) {
            this.zxbm = zxbm;
        }

        public String getYhzcbs() {
            return yhzcbs;
        }

        public void setYhzcbs(String yhzcbs) {
            this.yhzcbs = yhzcbs;
        }

        public String getLslbs() {
            return lslbs;
        }

        public void setLslbs(String lslbs) {
            this.lslbs = lslbs;
        }

        public String getZzstsgl() {
            return zzstsgl;
        }

        public void setZzstsgl(String zzstsgl) {
            this.zzstsgl = zzstsgl;
        }

        public String getXmje() {
            return xmje;
        }

        public void setXmje(String xmje) {
            this.xmje = xmje;
        }

        public String getSl() {
            return sl;
        }

        public void setSl(String sl) {
            this.sl = sl;
        }

        public String getSe() {
            return se;
        }

        public void setSe(String se) {
            this.se = se;
        }

        public String getByzd1() {
            return byzd1;
        }

        public void setByzd1(String byzd1) {
            this.byzd1 = byzd1;
        }

        public String getByzd2() {
            return byzd2;
        }

        public void setByzd2(String byzd2) {
            this.byzd2 = byzd2;
        }

        public String getByzd3() {
            return byzd3;
        }

        public void setByzd3(String byzd3) {
            this.byzd3 = byzd3;
        }

        public String getByzd4() {
            return byzd4;
        }

        public void setByzd4(String byzd4) {
            this.byzd4 = byzd4;
        }

        public String getByzd5() {
            return byzd5;
        }

        public void setByzd5(String byzd5) {
            this.byzd5 = byzd5;
        }
    }

    @XmlRootElement(name = "FPKJXX_DDXX")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FpkjxxDdxx extends BaseXmlModel {
        @XmlAttribute(name = "class")
        private String className = "FPKJXX_DDXX";

        //订单号
        @XmlElement(required = true,name = "DDH")
        private String ddh;

        //退货单号
        @XmlElement(required = true,name = "THDH")
        private String thdh;

        //订单时间
        @XmlElement(required = true,name = "DDDATE")
        private String dddate;


        public String getDdh() {
            return ddh;
        }

        public void setDdh(String ddh) {
            this.ddh = ddh;
        }

        public String getThdh() {
            return thdh;
        }

        public void setThdh(String thdh) {
            this.thdh = thdh;
        }

        public String getDddate() {
            return dddate;
        }

        public void setDddate(String dddate) {
            this.dddate = dddate;
        }
    }


    /**
     * (DDMXXX)订单明细信息（多条）
     */
    @XmlRootElement(name = "FPKJXX_DDMXXXS")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FpkjxxDdmxxxs extends BaseXmlModel {
        @XmlAttribute(name = "class")
        private String className = "FPKJXX_DDMXXX;";

        @XmlAttribute(name = "size")
        private String size = "0";

        @XmlElement(required = true,name = "FPKJXX_DDMXXX", type = FpkjxxDdmxxx.class)
        private List<FpkjxxDdmxxx> fpkjxxDdmxxxList;

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public List<FpkjxxDdmxxx> getFpkjxxDdmxxxList() {
            return fpkjxxDdmxxxList;
        }

        public void setFpkjxxDdmxxxList(List<FpkjxxDdmxxx> fpkjxxDdmxxxList) {
            this.fpkjxxDdmxxxList = fpkjxxDdmxxxList;
        }
    }

    @XmlRootElement(name = "FPKJXX_DDMXXX")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FpkjxxDdmxxx extends BaseXmlModel {

        //订单名称
        @XmlElement(required = true,name = "DDMC")
        private String ddmc;

        //单位
        @XmlElement(required = true,name = "DW")
        private String dw;

        //规格型号
        @XmlElement(required = true,name = "GGXH")
        private String ggxh;

        //数量
        @XmlElement(required = true,name = "SL")
        private String sl;

        //单价
        @XmlElement(required = true,name = "DJ")
        private String dj;

        //金额
        @XmlElement(required = true,name = "JE")
        private String je;

        //备用字段
        @XmlElement(required = true,name = "BYZD1")
        private String byzd1;

        //备用字段2
        @XmlElement(required = true,name = "BYZD2")
        private String byzd2;

        //备用字段3
        @XmlElement(required = true,name = "BYZD3")
        private String byzd3;

        //备用字段4
        @XmlElement(required = true,name = "BYZD4")
        private String byzd4;

        //备用字段5
        @XmlElement(required = true,name = "BYZD5")
        private String byzd5;


        public String getDdmc() {
            return ddmc;
        }

        public void setDdmc(String ddmc) {
            this.ddmc = ddmc;
        }

        public String getDw() {
            return dw;
        }

        public void setDw(String dw) {
            this.dw = dw;
        }

        public String getGgxh() {
            return ggxh;
        }

        public void setGgxh(String ggxh) {
            this.ggxh = ggxh;
        }

        public String getSl() {
            return sl;
        }

        public void setSl(String sl) {
            this.sl = sl;
        }

        public String getDj() {
            return dj;
        }

        public void setDj(String dj) {
            this.dj = dj;
        }

        public String getJe() {
            return je;
        }

        public void setJe(String je) {
            this.je = je;
        }

        public String getByzd1() {
            return byzd1;
        }

        public void setByzd1(String byzd1) {
            this.byzd1 = byzd1;
        }

        public String getByzd2() {
            return byzd2;
        }

        public void setByzd2(String byzd2) {
            this.byzd2 = byzd2;
        }

        public String getByzd3() {
            return byzd3;
        }

        public void setByzd3(String byzd3) {
            this.byzd3 = byzd3;
        }

        public String getByzd4() {
            return byzd4;
        }

        public void setByzd4(String byzd4) {
            this.byzd4 = byzd4;
        }

        public String getByzd5() {
            return byzd5;
        }

        public void setByzd5(String byzd5) {
            this.byzd5 = byzd5;
        }
    }

    @XmlRootElement(name = "FPKJXX_ZFXX")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FpkjxxZfxx extends BaseXmlModel {
        @XmlAttribute(name = "class")
        private String className = "FPKJXX_ZFXX";

        //支付方式
        @XmlElement(required = true,name = "ZFFS")
        private String zffs;

        //支付流水号
        @XmlElement(required = true,name = "ZFLSH")
        private String zflsh;

        //支付平台
        @XmlElement(required = true,name = "ZFPT")
        private String zfpt;

        public FpkjxxZfxx() {
        }

        public FpkjxxZfxx(String zffs, String zflsh, String zfpt) {
            this.zffs = zffs;
            this.zflsh = zflsh;
            this.zfpt = zfpt;
        }


        public String getZffs() {
            return zffs;
        }

        public void setZffs(String zffs) {
            this.zffs = zffs;
        }

        public String getZflsh() {
            return zflsh;
        }

        public void setZflsh(String zflsh) {
            this.zflsh = zflsh;
        }

        public String getZfpt() {
            return zfpt;
        }

        public void setZfpt(String zfpt) {
            this.zfpt = zfpt;
        }
    }

    @XmlRootElement(name = "FPKJXX_WLXX")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class FpkjxxWlxx extends BaseXmlModel {
        @XmlAttribute(name = "class")
        private String className = "FPKJXX_WLXX";

        //承运公司
        @XmlElement(required = true,name = "CYGS")
        private String cygs;

        //送货时间
        @XmlElement(required = true,name = "SHSJ")
        private String shsj;

        //物流单号
        @XmlElement(required = true,name = "WLDH")
        private String wldh;

        //送货地址
        @XmlElement(required = true,name = "SHDZ")
        private String shdz;

        public FpkjxxWlxx() {
        }

        public FpkjxxWlxx(String cygs, String shsj, String wldh, String shdz) {
            this.cygs = cygs;
            this.shsj = shsj;
            this.wldh = wldh;
            this.shdz = shdz;
        }


        public String getCygs() {
            return cygs;
        }

        public void setCygs(String cygs) {
            this.cygs = cygs;
        }

        public String getShsj() {
            return shsj;
        }

        public void setShsj(String shsj) {
            this.shsj = shsj;
        }

        public String getWldh() {
            return wldh;
        }

        public void setWldh(String wldh) {
            this.wldh = wldh;
        }

        public String getShdz() {
            return shdz;
        }

        public void setShdz(String shdz) {
            this.shdz = shdz;
        }
    }


//    public static void main(String[] args) {
//        try {
//            JAXBContext jc = JAXBContext.newInstance(RequestFpkjxx.class);
//            Marshaller ms = jc.createMarshaller();
//            RequestFpkjxx.Builder builder = new RequestFpkjxx.Builder();
//            builder.setFpkjxxDdmxxxs(new FpkjxxDdmxxxs());
//            builder.setFpkjxxDdxx(new FpkjxxDdxx());
//            FpkjxxFptxx.Builder builder1 =new FpkjxxFptxx.Builder();
//            builder1.setByzd1("asdf");
//            builder1.setByzd2("asdfsdf");
//            builder1.setByzd3("asdfsdf");
//            builder1.setByzd4("asdfsdf");
//            builder1.setByzd5("asdfsdf");
//            builder1.setChyy("asdfsdf");
//            builder.setFpkjxxFptxx(builder1.build());
//            RequestFpkjxx requestFpkjxx = builder.build();
//            ms.marshal(requestFpkjxx,System.out);
////            System.out.println(requestFpkjxx);
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//    }


}
