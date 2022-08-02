package com.wanmi.sbc.bookmeta.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 书籍(MetaBook)实体类
 *
 * @author Liang Jun
 * @since 2022-05-16 11:48:33
 */
@Data
public class MetaBookEditPublishInfoVO implements Serializable {
    @NotNull
    private Integer id;
    /**
     * 出版社
     */
    private Integer publisherId;
    /**
     * 出版社名称
     */
    private String publisherName;
    /**
     * 出品方
     */
    private Integer producerId;
    /**
     * 出品方名称
     */
    private String producerName;
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
    private Integer languageId;
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
     * 丛书名称
     */
    private String bookClumpName;
    /**
     * 书组说明
     */
    private String bookGroupDescr;
}

