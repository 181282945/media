package com.sunjung.core.controller;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZhenWeiLai on 2017/4/1.
 */
public abstract class BaseController {
    public final static Map<String,String> map = new HashMap<>();
    protected void registration(){
        map.put(this.getClass().getName(),"UserSign");
    }
}
