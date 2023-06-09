package com.wanmi.sbc.bookmeta.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 书籍(MetaBook)实体类
 *
 * @author Liang Jun
 * @since 2022-05-23 23:47:42
 */
@Data
public class MetaBookEditPublishInfoReqBO implements Serializable {

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
     * 书组id
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
     * 语言
     */
    private Integer languageId;
    /**
     * 标签
     */
    private List<Integer> labelIds = new ArrayList<>();

    /**
     * 人物列表
     */
    private List<Figure> figures = new ArrayList<>();

    @Data
    public static class Figure {
        /**
         * 人物id
         */
        private Integer figureId;
        /**
         * 类型：1作者；2译者；3绘画人；
         */
        private Integer figureType;
    }
}

