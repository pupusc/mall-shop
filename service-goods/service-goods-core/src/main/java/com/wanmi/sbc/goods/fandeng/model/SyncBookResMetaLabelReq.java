package com.wanmi.sbc.goods.fandeng.model;

import lombok.Data;

import java.io.*;

/**
 * @author Liang Jun
 * @date 2022-03-15 22:56:00
 */
@Data
public class SyncBookResMetaLabelReq implements Serializable {
    //标签类型：1：满减，2.包邮，3其他
    private Integer type;
    private String name;
}
