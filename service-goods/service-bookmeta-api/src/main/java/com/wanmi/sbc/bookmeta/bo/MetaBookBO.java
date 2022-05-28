package com.wanmi.sbc.bookmeta.bo;

import java.util.Date;
import java.io.Serializable;
import lombok.Data;

/**
 * 书籍(MetaBook)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 13:54:06
 */
@Data
public class MetaBookBO implements Serializable {
    private static final long serialVersionUID = 793734853132313686L;
         
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
}

