package com.wanmi.sbc.goods.fandeng.model;

import lombok.Data;

import java.io.*;
import java.util.List;

/**
 * @author Liang Jun
 * @desc 单本同步
 * @date 2022-03-15 22:39:00
 */
@Data
public class SyncBookResReqVO implements Serializable {
    /**
     * 12：纸质书
     */
    private Integer resourceType = 12;

    /**
     * 纸质书列表
     */
    private List<SyncBookResMetaReq> paperBooks;
}
