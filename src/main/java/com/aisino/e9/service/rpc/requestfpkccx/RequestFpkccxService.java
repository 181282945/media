package com.aisino.e9.service.rpc.requestfpkccx;

import com.aisino.common.model.xml.impl.RequestFpkccx;

/**
 * Created by 为 on 2017-6-8.
 */
public interface RequestFpkccxService {

    /**
     * 库存查询
     */
    String requestCnt(RequestFpkccx requestFpkccx);
}
