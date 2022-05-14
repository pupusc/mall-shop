package com.wanmi.sbc.goods.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * 书籍(MetaBook)实体类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:01
 */
public class MetaBook implements Serializable {
    private static final long serialVersionUID = 286567604570536190L;
    
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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

}

