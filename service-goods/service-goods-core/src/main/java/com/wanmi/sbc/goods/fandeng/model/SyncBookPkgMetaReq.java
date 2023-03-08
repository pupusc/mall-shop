package com.wanmi.sbc.goods.fandeng.model;

import lombok.Data;

import java.io.*;
import java.util.Date;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-03-15 23:02:00
 */
@Data
public class SyncBookPkgMetaReq implements Serializable {
    /**
     * 书单id
     */
    private String packageId;
    /**
     * 书单标题
     */
    private String title;
    /**
     * 书单副标题
     */
    private String subTitle;
    /**
     * 书单封面
     */
    private String coverImage;
    /**
     * 书籍数量
     */
    private Integer bookCount;

    /**
     * 书单类型1：365书单2：电子书单3：非凡书单4:实体书书单
     */
    private Integer type = 4;

    /**
     * 跳转地址
     */
    private String jumpUrl;
    /**
     * 实体书书单的书单描述
     */
    private String content;

    /**
     * 已购买的数量（书单对应的所有的书的购买数量的总和仅实体书书单拥有。）
     */
    private Integer payCount;

    /**
     * 发布状态1发布，0不发布
     */
    private Integer publishStatus;

    /**
     * 书单发布开始时间
     */
    private Date resPublishStart;

    /**
     * 书单发布结束时间
     */
    private Date resPublishEnd;

    /**
     * 子列表信息
     */
    private List<Item> items;


    @Data
    public static class Item {

        private String tid;

        private String title;

        private String coverImageUrl;

//        /**
//         * 0展示 1不展示
//         */
//        private Integer hasShow;
    }
}
