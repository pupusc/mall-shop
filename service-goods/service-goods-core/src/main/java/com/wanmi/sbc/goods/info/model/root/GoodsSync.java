package com.wanmi.sbc.goods.info.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import org.checkerframework.checker.units.qual.C;
import org.hibernate.annotations.Proxy;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Proxy(lazy = false)
@Data
@Entity
@Table(name = "goods_sync")
public class GoodsSync {
    /**
     * 主键
     */
    @Id
    @Column(name = "id")
    private Long id;

    /**
     * 商品编码
     */
    @Column(name = "goods_no")
    private String goodsNo;

    /**
     * 供应商类型1博库
     */
    @Column(name = "goods_supplier_type")
    private Integer goodsSupplierType;

    /**
     * isbn
     */
    @Column(name = "isbn")
    private String isbn;

    /**
     * 商品数量
     */
    @Column(name = "qty")
    private Integer qty;

    /**
     * 版次
     */
    @Column(name = "edition_number")
    private String editionNumber;

    /**
     * 印次
     */
    @Column(name = "print_number")
    private String printNumber;

    /**
     * 出版日期
     */
    @Column(name = "pulicate_date")
    private String pulicateDate;

    /**
     * 印刷日期
     */
    @Column(name = "print_date")
    private String printDate;

    /**
     * 出版社
     */
    @Column(name = "publish_name")
    private String publishName;

    /**
     * 成本价
     */
    @Column(name = "base_price")
    private BigDecimal basePrice;

    /**
     * 建议销售价
     */
    @Column(name = "sale_price")
    private BigDecimal salePrice;

    /**
     * 作者
     */
    @Column(name = "author")
    private String author;

    /**
     * 开本
     */
    @Column(name = "format")
    private String format;

    /**
     * 页数
     */
    @Column(name = "page_number")
    private Integer pageNumber;

    /**
     * 导语
     */
    @Column(name = "guide")
    private String guide;

    /**
     * 内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 作者简介
     */
    @Column(name = "author_desc")
    private String authorDesc;

    /**
     * 推荐语
     */
    @Column(name = "recommend")
    private String recommend;

    /**
     * 目录
     */
    @Column(name = "diretory")
    private String diretory;

    /**
     * 精彩内容
     */
    @Column(name = "wonderful_content")
    private String wonderfulContent;

    /**
     * 版权页图片
     */
    @Column(name = "copyright_url")
    private String copyrightUrl;

    /**
     * 封底夜
     */
    @Column(name = "bottom_url")
    private String bottomUrl;

    /**
     * 类目
     */
    @Column(name = "category")
    private Long category;

    /**
     * 标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 定价
     */
    @Column(name = "price")
    private BigDecimal price;

    /**
     * 图片
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * 大图
     */
    @Column(name = "large_image_url")
    private String largeImageUrl;

    /**
     * 状态0未审核1已审核2已同步
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 审核失败原因
     */
    @Column(name = "aduit_error")
    private String aduitError;

    @Column(name = "create_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    @Column(name = "update_time")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    @Column(name = "provider_id")
    private Long providerId;

    @Column(name = "detail_image_url")
    private String detailImageUrl;

    @Column(name = "ad_audit_status")
    private Integer adAuditStatus;

    @Column(name = "ad_manual_audit_status")
    private Integer adManualAuditStatus;

    @Column(name="ad_manual_reject_reason")
    private String adManualRejectReason;

    @Column(name = "launch_status")
    private Integer launchStatus;

    @Column(name="launch_reject_reason")
    private String launchRejectReason;
}
