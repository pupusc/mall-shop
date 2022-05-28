package com.wanmi.sbc.bookmeta.vo;

import java.util.Date;
import java.io.Serializable;
import com.wanmi.sbc.common.base.Page;
import lombok.Data;

/**
 * 行业数据(MetaBookIndustryInfo)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookIndustryInfoQueryByPageReqVO implements Serializable {
    private static final long serialVersionUID = 228055829671904571L;
    
    private Integer id;
    /**
     * isbn编号
     */
    private String isbn;
    /**
     * 产品id
     */
    private String productId;
    /**
     * 来源：1豆瓣；2当当；3博库；4商城；
     */
    private Integer sourceType;
    /**
     * 标题
     */
    private String title;
    /**
     * 副标题
     */
    private String subTitle;
    /**
     * 售价
     */
    private Double sellPrice;
    /**
     * 原价
     */
    private Double originPrice;
    /**
     * 编辑推荐
     */
    private String editorRcmmd;
    /**
     * 内容推荐
     */
    private String content_Rcmmd;
    /**
     * 目录
     */
    private String catalog;
    /**
     * 精彩书摘
     */
    private String digest;
    /**
     * 作者
     */
    private String author;
    /**
     * 作者简介
     */
    private String authorAbout;
    /**
     * 前言
     */
    private String foreword;
    /**
     * 媒体好评
     */
    private String mediaComment;
    /**
     * 出版社
     */
    private String publisher;
    /**
     * 出版日期
     */
    private String publishTime;
    /**
     * 出版批次
     */
    private String publishBatch;
    /**
     * 尺寸
     */
    private String size;
    /**
     * 是否套装
     */
    private Integer suitFlag;
    /**
     * 用纸
     */
    private String paper;
    /**
     * 包装装订
     */
    private String bookBind;
    /**
     * 打印日期
     */
    private String printTime;
    /**
     * 是否注音
     */
    private Integer toneFlag;
    /**
     * 页数
     */
    private Integer pageCount;
    /**
     * 字数
     */
    private Integer wordCount;
    /**
     * 印张
     */
    private Integer printSheet;
    /**
     * 册数
     */
    private Integer bookletCount;
    /**
     * 原作品名
     */
    private String originName;
    /**
     * 语言
     */
    private Integer languageId;
    /**
     * 编者
     */
    private String editorName;
    /**
     * 译者
     */
    private String translatorName;
    /**
     * 绘者
     */
    private String painterName;
    /**
     * 评论数
     */
    private Integer commentCount;
    /**
     * 排行榜名
     */
    private String rankName;
    /**
     * 排行名次
     */
    private Integer rankPlace;
    /**
     * 标签
     */
    private String tags;
    /**
     * 分类
     */
    private String category;
    /**
     * 关联产品
     */
    private String relationProduct;
    /**
     * 还购买过
     */
    private String rcmmdAlsoBought;
    /**
     * 还浏览过
     */
    private String rcmmdAlsoBrowse;
    /**
     * 一起购买的
     */
    private String rcmmdSameTime;
    /**
     * 可能感兴趣
     */
    private String rcmmdPerhapsLike;
    /**
     * 书单推荐
     */
    private String rcmmdBookList;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 删除标识
     */
    private Integer delFlag;
    /**
     * 分页参数
     */
    private Page page = new Page(1, 10);
}

