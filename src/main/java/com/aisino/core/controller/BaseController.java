package com.aisino.core.controller;

import com.aisino.core.entity.BaseEntity;
import com.google.gson.Gson;
import com.aisino.common.dto.jqgrid.JqgridFilters;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by ZhenWeiLai on 2017/4/1.
 */
public abstract class BaseController<T extends BaseEntity> {

    protected ModelAndView generalMav(String PATH,String MODULE_NAME,String VIEW_NAME,String UPDATE_URL,String ADD_URL,String DELETE_URL,String SEARCH_URL){
        ModelAndView mav = new ModelAndView(PATH + VIEW_NAME);
        mav.addObject("MODULE_NAME",MODULE_NAME);
        mav.addObject("UPDATE_URL",UPDATE_URL);
        mav.addObject("ADD_URL",ADD_URL);
        mav.addObject("DELETE_URL",DELETE_URL);
        mav.addObject("SEARCH_URL",SEARCH_URL);
        return mav;
    }


    /**
     * 去空格,""转NULL
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @InitBinder("pageAndSort")
    public void initBinderPageAndSort(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pageAndSort.");
    }

    /**
     * 转换json为实体
     * @param filters
     * @return
     */
    @ModelAttribute
    public JqgridFilters populateModel(@RequestParam(value = "filters",required = false) String filters) {
        Gson gson = new Gson();
        JqgridFilters jqgridFilters = gson.fromJson(filters, JqgridFilters.class);
        return jqgridFilters == null ? new JqgridFilters():jqgridFilters;
    }

}
