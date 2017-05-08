package com.aisino.common.model.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by 为 on 2017-5-8.
 */
public class KpRequestyl {

    @XmlRootElement(name = "REQUEST_FPKJXX")
    public static class RequestFpkjxx{
        @XmlAttribute(name = "class")
        private String className = "REQUEST_FPKJXX";

        @XmlElement(name = "FPKJXX_FPTXX",type = FpkjxxFptxx.class)
        private FpkjxxFptxx fpkjxxFptxx;

        @XmlElement(name = "FPKJXX_XMXXS",type = FpkjxxXmxxs.class)
        private FpkjxxXmxxs fpkjxxXmxxs;

        @XmlElement(name = "FPKJXX_DDXX",type = FpkjxxDdxx.class)
        private FpkjxxDdxx fpkjxxDdxx;

        @XmlElement(name = "FPKJXX_DDMXXXS",type = FpkjxxDdmxxxs.class)
        private FpkjxxDdmxxxs fpkjxxDdmxxxs;

        @XmlElement(name = "FPKJXX_ZFXX",type = FpkjxxZfxx.class)
        private FpkjxxZfxx fpkjxxZfxx;

        @XmlElement(name = "FPKJXX_WLXX",type = FpkjxxWlxx.class)
        private FpkjxxWlxx fpkjxxWlxx;


        public static class Builder{
            private FpkjxxFptxx fpkjxxFptxx;

            private FpkjxxXmxxs fpkjxxXmxxs;

            private FpkjxxDdxx fpkjxxDdxx;

            private FpkjxxDdmxxxs fpkjxxDdmxxxs;

            private FpkjxxZfxx fpkjxxZfxx;

            private FpkjxxWlxx fpkjxxWlxx;

            public Builder setFpkjxxFptxx(FpkjxxFptxx fpkjxxFptxx) {
                this.fpkjxxFptxx = fpkjxxFptxx;
                return this;
            }

            public Builder setFpkjxxXmxxs(FpkjxxXmxxs fpkjxxXmxxs) {
                this.fpkjxxXmxxs = fpkjxxXmxxs;
                return this;
            }

            public Builder setFpkjxxDdxx(FpkjxxDdxx fpkjxxDdxx) {
                this.fpkjxxDdxx = fpkjxxDdxx;
                return this;
            }

            public Builder setFpkjxxDdmxxxs(FpkjxxDdmxxxs fpkjxxDdmxxxs) {
                this.fpkjxxDdmxxxs = fpkjxxDdmxxxs;
                return this;
            }

            public Builder setFpkjxxZfxx(FpkjxxZfxx fpkjxxZfxx) {
                this.fpkjxxZfxx = fpkjxxZfxx;
                return this;
            }

            public Builder setFpkjxxWlxx(FpkjxxWlxx fpkjxxWlxx) {
                this.fpkjxxWlxx = fpkjxxWlxx;
                return this;
            }

            public RequestFpkjxx build() {
                RequestFpkjxx requestFpkjxx = new RequestFpkjxx();
                requestFpkjxx.fpkjxxFptxx = fpkjxxFptxx;
                requestFpkjxx.fpkjxxXmxxs = fpkjxxXmxxs;
                requestFpkjxx.fpkjxxDdxx = fpkjxxDdxx;
                requestFpkjxx.fpkjxxDdmxxxs = fpkjxxDdmxxxs;
                requestFpkjxx.fpkjxxZfxx = fpkjxxZfxx;
                requestFpkjxx.fpkjxxWlxx = fpkjxxWlxx;
                return requestFpkjxx;
            }

        }
    }

    @XmlRootElement(name = "FPKJXX_FPTXX")
    public static class FpkjxxFptxx{
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
        @XmlElement(name = "SWJG_D")
        private String swjgd;

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




        public static class Builder{

            //发票请求唯一流水号
            private String fpqqlsh;

            //平台编码
            private String dsptbm;

            //开票方识别码
            private String nsrsbh;

            //开票方名称
            private String nsrmc;

            //开票方电子档案号
            private String nsrdzdah;

            //税务机构代码
            private String swjgd;

            //代开标志
            private String dkbz;

            //票样代码
            private String pydm;

            //主要开票项目
            private String kpxm;

            //编码表版本号
            private String bmbbbh;

            //销货方识别号
            private String xhfnsrsbh;

            //销货方名称
            private String xhfmc;

            //销货方地址
            private String xhfdz;

            //销货方电话
            private String xhfdh;

            //销货方银行账号
            private String xhfyhzh;

            //购货方名称
            private String ghfmc;

            //购货方识别号
            private String ghfnsrsbh;

            //购货方省份
            private String ghfsf;

            //购货方地址
            private String ghfdz;

            //购货方固定电话
            private String ghfgddh;

            //购货方手机
            private String ghfsj;

            //购货方邮箱
            private String ghfemail;

            //01
            private String ghfqylx;

            //01
            private String ghfyhzh;

            //行业代码
            private String hydm;

            //行业名称
            private String hymc;

            //开票员
            private String kpy;

            //收款员
            private String sky;

            //复核人
            private String fhr;

            //开票日期
            private String kprq;

            //开票类型
            private String kplx;

            //原发票代码
            private String yfpdm;

            //原发票号码
            private String yfphm;

            //操作代码
            private String czdm;

            //清单标志
            private String qdbz;

            //清单发票项目名称
            private String qdxmmc;

            //冲红原因
            private String chyy;

            //特殊冲红标志
            private String tschbz;

            //价税合计金额
            private String kphjje;

            //合计不含税金额
            private String hjbhsje;

            //合计税额
            private String hjse;

            //备注
            private String bz;

            //备用字段1
            private String byzd1;
            //备用字段2
            private String byzd2;
            //备用字段3
            private String byzd3;
            //备用字4
            private String byzd4;
            //备用字段5
            private String byzd5;

            public Builder setFpqqlsh(String fpqqlsh) {
                this.fpqqlsh = fpqqlsh;
                return this;
            }

            public Builder setDsptbm(String dsptbm) {
                this.dsptbm = dsptbm;
                return this;
            }

            public Builder setNsrsbh(String nsrsbh) {
                this.nsrsbh = nsrsbh;
                return this;
            }

            public Builder setNsrmc(String nsrmc) {
                this.nsrmc = nsrmc;
                return this;
            }

            public Builder setNsrdzdah(String nsrdzdah) {
                this.nsrdzdah = nsrdzdah;
                return this;
            }

            public Builder setSwjgd(String swjgd) {
                this.swjgd = swjgd;
                return this;
            }

            public Builder setDkbz(String dkbz) {
                this.dkbz = dkbz;
                return this;
            }

            public Builder setPydm(String pydm) {
                this.pydm = pydm;
                return this;
            }

            public Builder setKpxm(String kpxm) {
                this.kpxm = kpxm;
                return this;
            }

            public Builder setBmbbbh(String bmbbbh) {
                this.bmbbbh = bmbbbh;
                return this;
            }

            public Builder setXhfnsrsbh(String xhfnsrsbh) {
                this.xhfnsrsbh = xhfnsrsbh;
                return this;
            }

            public Builder setXhfmc(String xhfmc) {
                this.xhfmc = xhfmc;
                return this;
            }

            public Builder setXhfdz(String xhfdz) {
                this.xhfdz = xhfdz;
                return this;
            }

            public Builder setXhfdh(String xhfdh) {
                this.xhfdh = xhfdh;
                return this;
            }

            public Builder setXhfyhzh(String xhfyhzh) {
                this.xhfyhzh = xhfyhzh;
                return this;
            }

            public Builder setGhfmc(String ghfmc) {
                this.ghfmc = ghfmc;
                return this;
            }

            public Builder setGhfnsrsbh(String ghfnsrsbh) {
                this.ghfnsrsbh = ghfnsrsbh;
                return this;
            }

            public Builder setGhfsf(String ghfsf) {
                this.ghfsf = ghfsf;
                return this;
            }

            public Builder setGhfdz(String ghfdz) {
                this.ghfdz = ghfdz;
                return this;
            }

            public Builder setGhfgddh(String ghfgddh) {
                this.ghfgddh = ghfgddh;
                return this;
            }

            public Builder setGhfsj(String ghfsj) {
                this.ghfsj = ghfsj;
                return this;
            }

            public Builder setGhfemail(String ghfemail) {
                this.ghfemail = ghfemail;
                return this;
            }

            public Builder setGhfqylx(String ghfqylx) {
                this.ghfqylx = ghfqylx;
                return this;
            }

            public Builder setGhfyhzh(String ghfyhzh) {
                this.ghfyhzh = ghfyhzh;
                return this;
            }

            public Builder setHydm(String hydm) {
                this.hydm = hydm;
                return this;
            }

            public Builder setHymc(String hymc) {
                this.hymc = hymc;
                return this;
            }

            public Builder setKpy(String kpy) {
                this.kpy = kpy;
                return this;
            }

            public Builder setSky(String sky) {
                this.sky = sky;
                return this;
            }

            public Builder setFhr(String fhr) {
                this.fhr = fhr;
                return this;
            }

            public Builder setKprq(String kprq) {
                this.kprq = kprq;
                return this;
            }

            public Builder setKplx(String kplx) {
                this.kplx = kplx;
                return this;
            }

            public Builder setYfpdm(String yfpdm) {
                this.yfpdm = yfpdm;
                return this;
            }

            public Builder setYfphm(String yfphm) {
                this.yfphm = yfphm;
                return this;
            }

            public Builder setCzdm(String czdm) {
                this.czdm = czdm;
                return this;
            }

            public Builder setQdbz(String qdbz) {
                this.qdbz = qdbz;
                return this;
            }

            public Builder setQdxmmc(String qdxmmc) {
                this.qdxmmc = qdxmmc;
                return this;
            }

            public Builder setChyy(String chyy) {
                this.chyy = chyy;
                return this;
            }

            public Builder setTschbz(String tschbz) {
                this.tschbz = tschbz;
                return this;
            }

            public Builder setKphjje(String kphjje) {
                this.kphjje = kphjje;
                return this;
            }

            public Builder setHjbhsje(String hjbhsje) {
                this.hjbhsje = hjbhsje;
                return this;
            }

            public Builder setHjse(String hjse) {
                this.hjse = hjse;
                return this;
            }

            public Builder setBz(String bz) {
                this.bz = bz;
                return this;
            }

            public Builder setByzd1(String byzd1) {
                this.byzd1 = byzd1;
                return this;
            }

            public Builder setByzd2(String byzd2) {
                this.byzd2 = byzd2;
                return this;
            }

            public Builder setByzd3(String byzd3) {
                this.byzd3 = byzd3;
                return this;
            }

            public Builder setByzd4(String byzd4) {
                this.byzd4 = byzd4;
                return this;
            }

            public Builder setByzd5(String byzd5) {
                this.byzd5 = byzd5;
                return this;
            }


             public FpkjxxFptxx build() {
                 FpkjxxFptxx fpkjxxFptxx = new FpkjxxFptxx();
                 fpkjxxFptxx.fpqqlsh = this.fpqqlsh;
                 fpkjxxFptxx.dsptbm = this.dsptbm;

                 fpkjxxFptxx.nsrsbh = this.nsrsbh;
                 fpkjxxFptxx.nsrmc = this.nsrmc;
                 fpkjxxFptxx.nsrdzdah = this.nsrdzdah;
                 fpkjxxFptxx.swjgd = this.swjgd;

                 fpkjxxFptxx.dkbz = this.dkbz;
                 fpkjxxFptxx.pydm = this.pydm;
                 fpkjxxFptxx.kpxm = this.kpxm;
                 fpkjxxFptxx.bmbbbh = this.bmbbbh;
                 fpkjxxFptxx.xhfnsrsbh = this.xhfnsrsbh;
                 fpkjxxFptxx.xhfmc = this.xhfmc;

                 fpkjxxFptxx.xhfdz = this.xhfdz;
                 fpkjxxFptxx.xhfdh = this.xhfdh;
                 fpkjxxFptxx.xhfyhzh = this.xhfyhzh;
                 fpkjxxFptxx.ghfmc = this.ghfmc;
                 fpkjxxFptxx.ghfnsrsbh = this.ghfnsrsbh;
                 fpkjxxFptxx.ghfsf = this.ghfsf;
                 fpkjxxFptxx.ghfdz = this.ghfdz;
                 fpkjxxFptxx.ghfgddh = this.ghfgddh;
                 fpkjxxFptxx.ghfsj = this.ghfsj;
                 fpkjxxFptxx.ghfemail = this.ghfemail;
                 fpkjxxFptxx.ghfqylx = this.ghfqylx;


                 fpkjxxFptxx.ghfyhzh = this.ghfyhzh;
                 fpkjxxFptxx.hydm = this.hydm;
                 fpkjxxFptxx.hymc = this.hymc;
                 fpkjxxFptxx.kpy = this.kpy;
                 fpkjxxFptxx.sky = this.sky;
                 fpkjxxFptxx.fhr = this.fhr;

                 fpkjxxFptxx.kprq = this.kprq;
                 fpkjxxFptxx.kplx = this.kplx;
                 fpkjxxFptxx.yfpdm = this.yfpdm;
                 fpkjxxFptxx.yfphm = this.yfphm;
                 fpkjxxFptxx.czdm = this.czdm;

                 fpkjxxFptxx.qdbz = this.qdbz;
                 fpkjxxFptxx.qdxmmc = this.qdxmmc;
                 fpkjxxFptxx.chyy = this.chyy;
                 fpkjxxFptxx.tschbz = this.tschbz;
                 fpkjxxFptxx.kphjje = this.kphjje;
                 fpkjxxFptxx.hjbhsje = this.hjbhsje;

                 fpkjxxFptxx.hjse = this.hjse;
                 fpkjxxFptxx.bz = this.bz;
                 fpkjxxFptxx.byzd1 = this.byzd1;
                 fpkjxxFptxx.byzd2 = this.byzd2;
                 fpkjxxFptxx.byzd3 = this.byzd3;

                 fpkjxxFptxx.byzd4 = this.byzd4;
                 fpkjxxFptxx.byzd5 = this.byzd5;

                return fpkjxxFptxx;
            }

        }



    }

    @XmlRootElement(name = "FPKJXX_XMXXS")
    public static class FpkjxxXmxxs{
        @XmlAttribute(name = "class")
        private String className = "FPKJXX_XMXX;";

        @XmlAttribute(name = "size")
        private String size = "1;";

        @XmlElement(name = "FPKJXX_XMXX",type = FpkjxxXmxx.class)
        private FpkjxxXmxx fpkjxxXmxx;

    }

    @XmlRootElement(name = "FPKJXX_XMXX")
    public static class FpkjxxXmxx{
        @XmlAttribute(name = "class")
        private String className = "FPKJXX_XMXX;";

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
        @XmlElement(name = "SE")
        private String se;

        //备用字段
        @XmlElement(name = "BYZD1")
        private String byzd1;

        //备用字段2
        @XmlElement(name = "BYZD2")
        private String byzd2;

        //备用字段3
        @XmlElement(name = "BYZD3")
        private String byzd3;

        //备用字段4
        @XmlElement(name = "BYZD4")
        private String byzd4;

        //备用字段5
        @XmlElement(name = "BYZD5")
        private String byzd5;

    }

    @XmlRootElement(name = "FPKJXX_DDXX")
    public static class FpkjxxDdxx{
        @XmlAttribute(name = "class")
        private String className = "FPKJXX_DDXX";

        //订单号
        @XmlElement(name = "DDH")
        private String ddh;

        //退货单号
        @XmlElement(name = "THDH")
        private String thdh;

        //订单时间
        @XmlElement(name = "DDDATE")
        private String dddate;








    }

    @XmlRootElement(name = "FPKJXX_DDMXXXS")
    public static class FpkjxxDdmxxxs{
        @XmlAttribute(name = "class")
        private String className = "FPKJXX_DDMXXX;";

        @XmlAttribute(name = "size")
        private String size = "1;";
    }

    @XmlRootElement(name = "FPKJXX_DDMXXX")
    public static class FpkjxxDdmxxx{

        //订单名称
        @XmlElement(name = "DDMC")
        private String ddmc;

        //单位
        @XmlElement(name = "DW")
        private String dw;

        //规格型号
        @XmlElement(name = "GGXH")
        private String ggxh;

        //数量
        @XmlElement(name = "SL")
        private String sl;

        //单价
        @XmlElement(name = "DJ")
        private String dj;

        //金额
        @XmlElement(name = "JE")
        private String je;

        //备用字段
        @XmlElement(name = "BYZD1")
        private String byzd1;

        //备用字段2
        @XmlElement(name = "BYZD2")
        private String byzd2;

        //备用字段3
        @XmlElement(name = "BYZD3")
        private String byzd3;

        //备用字段4
        @XmlElement(name = "BYZD4")
        private String byzd4;

        //备用字段5
        @XmlElement(name = "BYZD5")
        private String byzd5;

    }

    @XmlRootElement(name = "FPKJXX_ZFXX")
    public static class FpkjxxZfxx{
        @XmlAttribute(name = "class")
        private String className = "FPKJXX_ZFXX";

        //支付方式
        @XmlElement(name = "ZFFS")
        private String zffs;

        //支付流水号
        @XmlElement(name = "ZFLSH")
        private String zflsh;

        //支付平台
        @XmlElement(name = "ZFPT")
        private String zfpt;
    }

    @XmlRootElement(name = "FPKJXX_WLXX")
    public static class FpkjxxWlxx{
        @XmlAttribute(name = "class")
        private String className = "FPKJXX_WLXX";

        //承运公司
        @XmlElement(name = "CYGS")
        private String cygs;

        //送货时间
        @XmlElement(name = "SHSJ")
        private String shsj;

        //物流单号
        @XmlElement(name = "WLDH")
        private String wldh;

        //送货地址
        @XmlElement(name = "SHDZ")
        private String shdz;
    }


    public static void main(String[] args) {
        try {
            JAXBContext jc = JAXBContext.newInstance(RequestFpkjxx.class);
            Marshaller ms = jc.createMarshaller();
            RequestFpkjxx.Builder builder = new RequestFpkjxx.Builder();
            builder.setFpkjxxDdmxxxs(new FpkjxxDdmxxxs());
            builder.setFpkjxxDdxx(new FpkjxxDdxx());
            FpkjxxFptxx.Builder builder1 =new FpkjxxFptxx.Builder();
            builder1.setByzd1("asdf");
            builder1.setByzd2("asdfsdf");
            builder1.setByzd3("asdfsdf");
            builder1.setByzd4("asdfsdf");
            builder1.setByzd5("asdfsdf");
            builder1.setChyy("asdfsdf");
            builder.setFpkjxxFptxx(builder1.build());
            RequestFpkjxx requestFpkjxx = builder.build();
            ms.marshal(requestFpkjxx,System.out);
//            System.out.println(requestFpkjxx);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


}
