package com.sunjung.core.controller;

import com.google.gson.Gson;
import com.sunjung.common.dto.jqgrid.JqgridFilters;
import com.sunjung.core.dto.ResultDataDto;
import com.sunjung.core.entity.BaseEntity;
import com.sunjung.core.mybatis.specification.PageAndSort;
import com.sunjung.core.util.GenericeClassUtils;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ZhenWeiLai on 2017/4/1.
 */
public abstract class BaseController<T extends BaseEntity> {

    @InitBinder("pageAndSort")
    public void initBinderPageAndSort(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pageAndSort.");
    }

    @ModelAttribute
    public JqgridFilters populateModel(@RequestParam(value = "filters",required = false) String filters) {
        Gson gson = new Gson();
        JqgridFilters jqgridFilters = gson.fromJson(filters, JqgridFilters.class);
        return jqgridFilters;
    }

    // 异常信息拦截，返回
    @ExceptionHandler(Exception.class)   //在Controller类中添加该注解方法即可(注意：添加到某个controller，只针对该controller起作用)
    public void exceptionHandler(Exception ex, HttpServletResponse response, HttpServletRequest request) throws IOException {
        ex.printStackTrace();
        ResultDataDto resultDataDto = new ResultDataDto(ex);
        response.setContentType(MediaType.TEXT_HTML_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(resultDataDto));
//		 return "redirect:/home.html";
    }

    private PageAndSort pageAndSort;

    protected Class<T> entityClass = (Class<T>) GenericeClassUtils.getSuperClassGenricType(this.getClass(), 0);

    protected ModelMap getModelMap(){

        ModelMap modelMap = new ModelMap();

        modelMap.addAttribute("pageAndSort",pageAndSort);
        return modelMap;
    }

    /**
     * 在每一个类中需要重置
     * @return
     */
    protected String getActionNamespace(){
        return "";
    }
    /**
     * 在每一个类中需要重置
     * @return
     */
    protected String getTypeName(){
        return "";
    }

    public PageAndSort getPageAndSort() {
        return pageAndSort;
    }

    public void setPageAndSort(PageAndSort pageAndSort) {
        this.pageAndSort = pageAndSort;
    }
}
