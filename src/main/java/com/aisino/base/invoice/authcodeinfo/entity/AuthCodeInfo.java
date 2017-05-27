package com.aisino.base.invoice.authcodeinfo.entity;

import com.aisino.core.entity.BaseInvoiceEntity;
import com.aisino.core.entity.annotation.BaseEntityMapper;
import com.aisino.core.entity.annotation.IsNotNull;
import org.apache.ibatis.type.Alias;

/**
 * Created by 为 on 2017-5-8.
 */
@Alias("AuthCodeInfo")
@BaseEntityMapper(tableName = "invoice_authcodeinfo")
public class AuthCodeInfo extends BaseInvoiceEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -7683611911435462963L;
	//企业税号
    @IsNotNull(description = "企业税号")
    private String taxNo;
    //平台编码 - DSPTBM
    @IsNotNull(description = "平台编码")
    private String platformCode;
    //授权码 -
    @IsNotNull(description = "授权码")
    private String authCode;
    //注册码 -
    @IsNotNull(description = "注册码")
    private String regiCode;
    //开票方电子档案号
    private String elecNmber;
    //税务机构代码
    private String taxCode;
    //编码表版本号(税控分类) - BMB_BBH
    @IsNotNull(description = "编码表版本号")
    private String codeTableVer;
    //用户账号
    @IsNotNull(description = "用户账号")
    private String usrno;


    //------------------getter and setter----------------------------


    public String getTaxNo() {
        return taxNo;
    }

    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getRegiCode() {
        return regiCode;
    }

    public void setRegiCode(String regiCode) {
        this.regiCode = regiCode;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getCodeTableVer() {
        return codeTableVer;
    }

    public void setCodeTableVer(String codeTableVer) {
        this.codeTableVer = codeTableVer;
    }

    public String getUsrno() {
        return usrno;
    }

    public void setUsrno(String usrno) {
        this.usrno = usrno;
    }

    public String getElecNmber() {
        return elecNmber;
    }

    public void setElecNmber(String elecNmber) {
        this.elecNmber = elecNmber;
    }
}
