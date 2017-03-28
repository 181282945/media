package com.sunjung.core.mybatis.specification;

import com.sunjung.core.util.Delimiter;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by ZhenWeiLai on 2017/3/28.
 */
public class PageAndSort {

    public static final Integer MAX_PAGE_SIZE=3000;
    public static final Integer SHORT_PAGE_SIZE=5;

    public PageAndSort() {
        super();
    }

    public PageAndSort(Integer page, Integer rp) {
        super();
        this.page = page;
        this.rp = rp;
    }

    public PageAndSort(Integer page, Integer rp, String sortName) {
        super();
        this.page = page;
        this.rp = rp;
        this.sortName = sortName;
    }

    public PageAndSort(Integer page, Integer rp, String sortName, String sortOrder) {
        super();
        this.page = page;
        this.rp = rp;
        this.sortName = sortName;
        this.sortOrder = sortOrder;
    }

    public static PageAndSort createMaxPageDto(){
        PageAndSort pageAndSort=new PageAndSort();
        pageAndSort.setRp(MAX_PAGE_SIZE).setPage(1);
        return pageAndSort;
    }
    public static PageAndSort generateFlexiPageDto(Integer page, Integer rp, String orderBy) {
        PageAndSort pageAndSort = new PageAndSort(page, rp);
        if (!StringUtils.isBlank(orderBy)) {
            String[] orderBys = orderBy.split("_");
            pageAndSort.setSortName(orderBys[0]);
            pageAndSort.setSortOrder(orderBys[1]);
        }
        return pageAndSort;
    }

    /**
     * 当前页
     */
    private Integer page;

    /**
     * 每页显示，默认:10
     */
    private Integer rp = 10;

    /**
     * 总记录数
     */
    private Long rowCount;

    /**
     * 开始序号
     */
    private Long seq;

    /**
     * 排序字段
     */
    private String sortName;

    /**
     * 排序(asc/desc)
     */
    private String sortOrder = "desc";

    public static final String SORTORDER_ACS = "asc";

    /**
     * 数据开始坐标，Mysql从0开始
     */
    public Long getSeq(){
        return this.seq;
    }

    /**
     * 总页数
     */
    public Long getTotalPage() {
        if (null == rowCount) {
            return 0L;
        }
        long totalPage = (rowCount / rp);
        long remainder = rowCount % rp;
        if (rowCount > 0 && totalPage == 0) {
            totalPage = 1;
            return totalPage;
        }
        if (remainder > 0) {
            totalPage++;
            return totalPage;
        }
        return totalPage;
    }

    // -------------------------- getter and setter -----------------------------
    public Integer getRp() {
        return rp;
    }

    public PageAndSort setRp(Integer rp) {
        this.rp = rp;
        return this;
    }

    public Integer getPage() {
        return page;
    }

    public PageAndSort setPage(Integer page) {
        this.page = page;
        return this;
    }

    public String getSortName() {
        return sortName;
    }

    public PageAndSort setSortName(String sortName) {
        this.sortName = sortName;
        return this;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public PageAndSort setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public Long getRowCount() {
        return rowCount;
    }

    public PageAndSort setRowCount(Long rowCount) {
        this.rowCount = rowCount;
        this.seq = (this.getPage()-1)*this.getRp()*1L;
        return this;
    }

    public String getSortString(){

        if (null == sortName) {
            return null;
        }
        String[] fields = this.getSortName().split(Delimiter.SEMICOLON.getDelimiter());
        String[] fieldsorts = this.getSortOrder().split(Delimiter.SEMICOLON.getDelimiter());
        if(fields.length!=fieldsorts.length){
            throw new RuntimeException("排序规则不一致");
        }

        String sql = "";
        for(int index=0;index<fields.length;index++){
            sql = sql+" "+fields[index]+" "+fieldsorts[index];
        }

        return sql;
    }

    public PageAndSort setSeq(Long seq) {
        this.seq = seq;
        return this;
    }

}
