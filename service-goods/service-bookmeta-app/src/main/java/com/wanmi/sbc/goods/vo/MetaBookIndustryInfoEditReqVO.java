package com.wanmi.sbc.goods.vo;

import java.util.Date;
import java.io.Serializable;

/**
 * 行业数据(MetaBookIndustryInfo)实体类
 *
 * @author Liang Jun
 * @since 2022-05-13 22:20:02
 */
public class MetaBookIndustryInfoEditReqVO implements Serializable {
    private static final long serialVersionUID = -53991329007322673L;
    
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
    private String language;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Double getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Double originPrice) {
        this.originPrice = originPrice;
    }

    public String getEditorRcmmd() {
        return editorRcmmd;
    }

    public void setEditorRcmmd(String editorRcmmd) {
        this.editorRcmmd = editorRcmmd;
    }

    public String getContent_Rcmmd() {
        return content_Rcmmd;
    }

    public void setContent_Rcmmd(String content_Rcmmd) {
        this.content_Rcmmd = content_Rcmmd;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorAbout() {
        return authorAbout;
    }

    public void setAuthorAbout(String authorAbout) {
        this.authorAbout = authorAbout;
    }

    public String getForeword() {
        return foreword;
    }

    public void setForeword(String foreword) {
        this.foreword = foreword;
    }

    public String getMediaComment() {
        return mediaComment;
    }

    public void setMediaComment(String mediaComment) {
        this.mediaComment = mediaComment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishBatch() {
        return publishBatch;
    }

    public void setPublishBatch(String publishBatch) {
        this.publishBatch = publishBatch;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getSuitFlag() {
        return suitFlag;
    }

    public void setSuitFlag(Integer suitFlag) {
        this.suitFlag = suitFlag;
    }

    public String getPaper() {
        return paper;
    }

    public void setPaper(String paper) {
        this.paper = paper;
    }

    public String getBookBind() {
        return bookBind;
    }

    public void setBookBind(String bookBind) {
        this.bookBind = bookBind;
    }

    public String getPrintTime() {
        return printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }

    public Integer getToneFlag() {
        return toneFlag;
    }

    public void setToneFlag(Integer toneFlag) {
        this.toneFlag = toneFlag;
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

    public Integer getPrintSheet() {
        return printSheet;
    }

    public void setPrintSheet(Integer printSheet) {
        this.printSheet = printSheet;
    }

    public Integer getBookletCount() {
        return bookletCount;
    }

    public void setBookletCount(Integer bookletCount) {
        this.bookletCount = bookletCount;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public String getTranslatorName() {
        return translatorName;
    }

    public void setTranslatorName(String translatorName) {
        this.translatorName = translatorName;
    }

    public String getPainterName() {
        return painterName;
    }

    public void setPainterName(String painterName) {
        this.painterName = painterName;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public Integer getRankPlace() {
        return rankPlace;
    }

    public void setRankPlace(Integer rankPlace) {
        this.rankPlace = rankPlace;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRelationProduct() {
        return relationProduct;
    }

    public void setRelationProduct(String relationProduct) {
        this.relationProduct = relationProduct;
    }

    public String getRcmmdAlsoBought() {
        return rcmmdAlsoBought;
    }

    public void setRcmmdAlsoBought(String rcmmdAlsoBought) {
        this.rcmmdAlsoBought = rcmmdAlsoBought;
    }

    public String getRcmmdAlsoBrowse() {
        return rcmmdAlsoBrowse;
    }

    public void setRcmmdAlsoBrowse(String rcmmdAlsoBrowse) {
        this.rcmmdAlsoBrowse = rcmmdAlsoBrowse;
    }

    public String getRcmmdSameTime() {
        return rcmmdSameTime;
    }

    public void setRcmmdSameTime(String rcmmdSameTime) {
        this.rcmmdSameTime = rcmmdSameTime;
    }

    public String getRcmmdPerhapsLike() {
        return rcmmdPerhapsLike;
    }

    public void setRcmmdPerhapsLike(String rcmmdPerhapsLike) {
        this.rcmmdPerhapsLike = rcmmdPerhapsLike;
    }

    public String getRcmmdBookList() {
        return rcmmdBookList;
    }

    public void setRcmmdBookList(String rcmmdBookList) {
        this.rcmmdBookList = rcmmdBookList;
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

