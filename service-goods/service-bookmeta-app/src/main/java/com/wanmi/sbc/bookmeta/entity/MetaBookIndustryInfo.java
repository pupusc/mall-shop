package com.wanmi.sbc.bookmeta.entity;

import java.util.Date;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 行业数据(MetaBookIndustryInfo)实体类
 *
 * @author Liang Jun
 * @since 2022-05-26 01:28:20
 */
@Table(name = "meta_book_industry_info")
public class MetaBookIndustryInfo implements Serializable {
    private static final long serialVersionUID = 965073114568630411L;
         
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
    /**
     * isbn编号
     */     
    @Column(name = "isbn")
    private String isbn;
    /**
     * 产品id
     */     
    @Column(name = "product_id")
    private String productId;
    /**
     * 来源：1豆瓣；2当当；3博库；4商城；
     */     
    @Column(name = "source_type")
    private Integer sourceType;
    /**
     * 标题
     */     
    @Column(name = "title")
    private String title;
    /**
     * 副标题
     */     
    @Column(name = "sub_title")
    private String subTitle;
    /**
     * 售价
     */     
    @Column(name = "sell_price")
    private Double sellPrice;
    /**
     * 原价
     */     
    @Column(name = "origin_price")
    private Double originPrice;
    /**
     * 编辑推荐
     */     
    @Column(name = "editor_rcmmd")
    private String editorRcmmd;
    /**
     * 内容推荐
     */     
    @Column(name = "content__rcmmd")
    private String content_Rcmmd;
    /**
     * 目录
     */     
    @Column(name = "catalog")
    private String catalog;
    /**
     * 精彩书摘
     */     
    @Column(name = "digest")
    private String digest;
    /**
     * 作者
     */     
    @Column(name = "author")
    private String author;
    /**
     * 作者简介
     */     
    @Column(name = "author_about")
    private String authorAbout;
    /**
     * 前言
     */     
    @Column(name = "foreword")
    private String foreword;
    /**
     * 媒体好评
     */     
    @Column(name = "media_comment")
    private String mediaComment;
    /**
     * 出版社
     */     
    @Column(name = "publisher")
    private String publisher;
    /**
     * 出版日期
     */     
    @Column(name = "publish_time")
    private String publishTime;
    /**
     * 出版批次
     */     
    @Column(name = "publish_batch")
    private String publishBatch;
    /**
     * 尺寸
     */     
    @Column(name = "size")
    private String size;
    /**
     * 是否套装
     */     
    @Column(name = "suit_flag")
    private Integer suitFlag;
    /**
     * 用纸
     */     
    @Column(name = "paper")
    private String paper;
    /**
     * 包装装订
     */     
    @Column(name = "book_bind")
    private String bookBind;
    /**
     * 打印日期
     */     
    @Column(name = "print_time")
    private String printTime;
    /**
     * 是否注音
     */     
    @Column(name = "tone_flag")
    private Integer toneFlag;
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
     * 印张
     */     
    @Column(name = "print_sheet")
    private Integer printSheet;
    /**
     * 册数
     */     
    @Column(name = "booklet_count")
    private Integer bookletCount;
    /**
     * 原作品名
     */     
    @Column(name = "origin_name")
    private String originName;
    /**
     * 语言
     */     
    @Column(name = "language")
    private String language;
    /**
     * 编者
     */     
    @Column(name = "editor_name")
    private String editorName;
    /**
     * 译者
     */     
    @Column(name = "translator_name")
    private String translatorName;
    /**
     * 绘者
     */     
    @Column(name = "painter_name")
    private String painterName;
    /**
     * 评论数
     */     
    @Column(name = "comment_count")
    private Integer commentCount;
    /**
     * 排行榜名
     */     
    @Column(name = "rank_name")
    private String rankName;
    /**
     * 排行名次
     */     
    @Column(name = "rank_place")
    private Integer rankPlace;
    /**
     * 标签
     */     
    @Column(name = "tags")
    private String tags;
    /**
     * 分类
     */     
    @Column(name = "category")
    private String category;
    /**
     * 关联产品
     */     
    @Column(name = "relation_product")
    private String relationProduct;
    /**
     * 还购买过
     */     
    @Column(name = "rcmmd_also_bought")
    private String rcmmdAlsoBought;
    /**
     * 还浏览过
     */     
    @Column(name = "rcmmd_also_browse")
    private String rcmmdAlsoBrowse;
    /**
     * 一起购买的
     */     
    @Column(name = "rcmmd_same_time")
    private String rcmmdSameTime;
    /**
     * 可能感兴趣
     */     
    @Column(name = "rcmmd_perhaps_like")
    private String rcmmdPerhapsLike;
    /**
     * 书单推荐
     */     
    @Column(name = "rcmmd_book_list")
    private String rcmmdBookList;
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

