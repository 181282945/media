package com.aisino.e9.service.rpc.requestt;

import com.aisino.common.model.xml.impl.Requestt;

/**
 * Created by 为 on 2017-6-8.
 */
public interface RequesttService {

    /**
     * 根据请求接口代码创建请求报文
     */
    Requestt careateRequestt(String interfaceCode);
}
