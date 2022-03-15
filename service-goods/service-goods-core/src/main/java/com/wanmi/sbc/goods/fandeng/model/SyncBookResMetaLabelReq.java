package com.wanmi.sbc.goods.fandeng.model;

import lombok.Data;

import java.io.*;

/**
 * @author Liang Jun
 * @date 2022-03-15 22:56:00
 */
@Data
public class SyncBookResMetaLabelReq implements Serializable {
    private Integer type;
    private String name;
}
