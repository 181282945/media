package com.aisino.e9.service.rpc.kdniao;

/**
 * Created by 为 on 2017-6-12.
 */
public interface KdniaoService {
    String getOrderTracesByJson(String expCode, String expNo) throws Exception;
}
