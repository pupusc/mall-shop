package com.wanmi.ares.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * Created by mac on 2017/5/6.
 */
public interface ColumnRender<T> {
    void render(HSSFCell row, T object);
}
