package com.wanmi.sbc.goods.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 书籍(MetaBook)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookEditReqVO implements Serializable {
    private static final long serialVersionUID = 852029942663037808L;
    
    private Integer id;
    
    private String isbn;
    /**
     * 名称
     */
    private String name;
    /**
     * 副名称
     */
    private String subName;
    /**
     * 原作名称
     */
    private String originName;
    /**
     * 出版社
     */
    private Integer publisherId;
    /**
     * 出品方
     */
    private Integer producerId;
    /**
     * 数组
     */
    private Integer bookGroupId;
    /**
     * 装帧
     */
    private Integer bindId;
    /**
     * 用纸
     */
    private Integer paperId;
    /**
     * 出版日期
     */
    private Date publishTime;
    /**
     * 出版批次
     */
    private Integer publishBatch;
    /**
     * 印刷日期
     */
    private Date printTime;
    /**
     * 印刷批次
     */
    private Integer printBatch;
    /**
     * 页数
     */
    private Integer pageCount;
    /**
     * 字数
     */
    private Integer wordCount;
    /**
     * 评分
     */
    private Double score;
    /**
     * 定价
     */
    private Double price;
    /**
     * 尺寸：长
     */
    private Integer sizeLength;
    /**
     * 尺寸：宽
     */
    private Integer sizeWidth;
    /**
     * 印张
     */
    private Integer printSheet;
    /**
     * 语言
     */
    private String language;
    /**
     * 最小阅读年龄
     */
    private Integer fitAgeMin;
    /**
     * 最大阅读年龄
     */
    private Integer fitAgeMax;
    /**
     * 丛书id
     */
    private Integer bookClumpId;
    /**
     * 书组说明
     */
    private String bookGroupDescr;
    /**
     * 标签列表
     */
    private List<MetaBookLabelVO> labelList;
    /**
     * 作者id列表
     */
    private List<Integer> authorIdList;
    /**
     * 译者id列表
     */
    private List<Integer> translatorIdList;
    /**
     * 绘者id列表
     */
    private List<Integer> painterIdList;

    public static class MetaBookLabelVO {
        private Integer id;
        /**
         * 名称
         */
        private String name;
        /**
         * 说明
         */
        private String descr;
        /**
         * 顺序
         */
        private Integer seq;
        /**
         * 路径，下划线分隔
         */
        private String path;
    }
}

