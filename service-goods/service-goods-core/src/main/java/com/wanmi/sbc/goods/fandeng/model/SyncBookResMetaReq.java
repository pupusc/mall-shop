package com.wanmi.sbc.goods.fandeng.model;

import lombok.Data;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-03-15 22:45:00
 */
@Data
public class SyncBookResMetaReq implements Serializable {
    /**
     * xxx
     */
    private String bookId;
    /**
     * 商品主图（实体书和营销图合并后的图片）
     */
    private String icon;
    /**
     * 商品小标
     */
    private String smallIcon;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 商品标题
     */
    private String title;
    /**
     * 商品副标题
     */
    private String subtitle;
    /**
     * 作者名称
     */
    private String authorName;
    /**
     * 出版社
     */
    private String publishHouse;
    /**
     * 入选销量排行榜第13名。。。。
     */
    private String rankAndDec;
    /**
     * 心选榜分值:10分点亮5颗星
     */
    private Double heartPick;
    /**
     * 点击心选榜的跳转链接
     */
    private String heartJumpUrl;
    /**
     * 资源跳转链接
     */
    private String jumpUrl;
    /**
     * 促销价
     */
    private Double promotePrice;
    /**
     * 会员价
     */
    private Double memberPrice;
    /**
     * 售价
     */
    private Double sellPrice;
    /**
     * 定价
     */
    private Double reservePrice;
    /**
     * 文本
     */
    private String content;
    /**
     * 分值：近90天详情页浏览次数1000+该书近180天销量该搜索结果下所有实体书近180天销量
     */
    private Double sortFactor;
    /**
     * 标签列表（顺序商城控制）
     */
    private List<SyncBookResMetaLabelReq> labels;

    /**
     * 发布状态1发布，0不发布
     */
    private Integer publishStatus;

    /**
     * 书单发布开始时间
     */
    private LocalDateTime resPublishStart;

    /**
     * 书单发布结束时间
     */
    private LocalDateTime resPublishEnd;
}
