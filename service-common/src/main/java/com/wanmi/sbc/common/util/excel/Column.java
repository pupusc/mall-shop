package com.wanmi.sbc.common.util.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by mac on 2017/5/6.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Column {

    private String header;
    private ColumnRender render;

    // 合并单元格, 先不用了
    //private int rowSpan;
    //private int cellSpan;
}
