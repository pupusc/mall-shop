package com.wanmi.sbc.bookmeta.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 书籍(MetaBook)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 01:25:34
 */
@Table(name = "meta_book")
public class MetaBook implements Serializable {
    private static final long serialVersionUID = -89045210411200591L;
         
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
         
    @Column(name = "isbn")
    private String isbn;
    /**
     * 名称
     */     
    @Column(name = "name")
    private String name;
    /**
     * 副名称
     */     
    @Column(name = "sub_name")
    private String subName;
    /**
     * 原作名称
     */     
    @Column(name = "origin_name")
    private String originName;
    /**
     * 出版社
     */     
    @Column(name = "publisher_id")
    private Integer publisherId;
    /**
     * 出品方
     */     
    @Column(name = "producer_id")
    private Integer producerId;
    /**
     * 书组id
     */     
    @Column(name = "book_group_id")
    private Integer bookGroupId;
    /**
     * 装帧
     */     
    @Column(name = "bind_id")
    private Integer bindId;
    /**
     * 用纸
     */     
    @Column(name = "paper_id")
    private Integer paperId;
    /**
     * 出版日期
     */     
    @Column(name = "publish_time")
    private Date publishTime;
    /**
     * 出版批次
     */     
    @Column(name = "publish_batch")
    private Integer publishBatch;
    /**
     * 印刷日期
     */     
    @Column(name = "print_time")
    private Date printTime;
    /**
     * 印刷批次
     */     
    @Column(name = "print_batch")
    private Integer printBatch;
    /**
     * 页数
     */     
    @Column(name = "page_count")
    private Integer pageCount;
    /**
     * 字数
     */     
    @Column(name = "word_count")
    private Integer wordCount;
    /**
     * 定价
     */     
    @Column(name = "price")
    private Double price;
    /**
     * 尺寸：长
     */     
    @Column(name = "size_length")
    private Integer sizeLength;
    /**
     * 尺寸：宽
     */     
    @Column(name = "size_width")
    private Integer sizeWidth;
    /**
     * 开本
     */
    @Column(name = "size_folio")
    private String sizeFolio;
    /**
     * 印张
     */     
    @Column(name = "print_sheet")
    private Integer printSheet;
    /**
     * 最小阅读年龄
     */     
    @Column(name = "fit_age_min")
    private Integer fitAgeMin;
    /**
     * 最大阅读年龄
     */     
    @Column(name = "fit_age_max")
    private Integer fitAgeMax;
    /**
     * 创建时间
     */     
    @Column(name = "create_time")
    private Date createTime;
    /**
     * 更新时间
     */     
    @Column(name = "update_time")
    private Date updateTime;
    /**
     * 删除标识
     */     
    @Column(name = "del_flag")
    private Integer delFlag;
    /**
     * 丛书id
     */     
    @Column(name = "book_clump_id")
    private Integer bookClumpId;
    /**
     * 书组说明
     */     
    @Column(name = "book_group_descr")
    private String bookGroupDescr;
    /**
     * 语言
     */     
    @Column(name = "language_id")
    private Integer languageId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public Integer getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(Integer publisherId) {
        this.publisherId = publisherId;
    }

    public Integer getProducerId() {
        return producerId;
    }

    public void setProducerId(Integer producerId) {
        this.producerId = producerId;
    }

    public Integer getBookGroupId() {
        return bookGroupId;
    }

    public void setBookGroupId(Integer bookGroupId) {
        this.bookGroupId = bookGroupId;
    }

    public Integer getBindId() {
        return bindId;
    }

    public void setBindId(Integer bindId) {
        this.bindId = bindId;
    }

    public Integer getPaperId() {
        return paperId;
    }

    public void setPaperId(Integer paperId) {
        this.paperId = paperId;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public Integer getPublishBatch() {
        return publishBatch;
    }

    public void setPublishBatch(Integer publishBatch) {
        this.publishBatch = publishBatch;
    }

    public Date getPrintTime() {
        return printTime;
    }

    public void setPrintTime(Date printTime) {
        this.printTime = printTime;
    }

    public Integer getPrintBatch() {
        return printBatch;
    }

    public void setPrintBatch(Integer printBatch) {
        this.printBatch = printBatch;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getSizeLength() {
        return sizeLength;
    }

    public void setSizeLength(Integer sizeLength) {
        this.sizeLength = sizeLength;
    }

    public Integer getSizeWidth() {
        return sizeWidth;
    }

    public void setSizeWidth(Integer sizeWidth) {
        this.sizeWidth = sizeWidth;
    }

    public Integer getPrintSheet() {
        return printSheet;
    }

    public void setPrintSheet(Integer printSheet) {
        this.printSheet = printSheet;
    }

    public Integer getFitAgeMin() {
        return fitAgeMin;
    }

    public void setFitAgeMin(Integer fitAgeMin) {
        this.fitAgeMin = fitAgeMin;
    }

    public Integer getFitAgeMax() {
        return fitAgeMax;
    }

    public void setFitAgeMax(Integer fitAgeMax) {
        this.fitAgeMax = fitAgeMax;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getBookClumpId() {
        return bookClumpId;
    }

    public void setBookClumpId(Integer bookClumpId) {
        this.bookClumpId = bookClumpId;
    }

    public String getBookGroupDescr() {
        return bookGroupDescr;
    }

    public void setBookGroupDescr(String bookGroupDescr) {
        this.bookGroupDescr = bookGroupDescr;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public String getSizeFolio() {
        return sizeFolio;
    }

    public void setSizeFolio(String sizeFolio) {
        this.sizeFolio = sizeFolio;
    }
}

