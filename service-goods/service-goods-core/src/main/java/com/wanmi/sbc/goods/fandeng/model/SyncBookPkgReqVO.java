package com.wanmi.sbc.goods.fandeng.model;

import lombok.Data;

import java.io.*;
import java.util.List;

/**
 * @author Liang Jun
 * @desc 同步书单
 * @date 2022-03-15 22:59:00
 */
@Data
public class SyncBookPkgReqVO implements Serializable {
    /**
     * 4：书单
     */
    private Integer resourceType = 4;

    /**
     * 书单搜索内容
     */
    private List<SyncBookPkgMetaReq> bookPackages;
}
