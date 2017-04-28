package com.aisino.common.util.excel.reader;

import java.util.List;

/**
 * Created by 为 on 2017-4-28.
 */
public interface IRowReader {

    /**业务逻辑实现方法
     * @param sheetIndex
     * @param curRow
     * @param rowlist
     */
    public  void getRows(int sheetIndex,int curRow, List<String> rowlist);
}
