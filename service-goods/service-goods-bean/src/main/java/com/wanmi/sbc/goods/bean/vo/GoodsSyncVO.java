package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ApiModel
@Data
public class GoodsSyncVO implements Serializable {

    private static final long serialVersionUID = -914598443177926235L;
    /**
     * 主键
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 商品编码
     */
    @ApiModelProperty("商品编码")
    private String goodsNo;

    /**
     * 供应商类型1博库
     */
    @ApiModelProperty("供应商类型1博库")
    private Byte goodsSupplierType;

    /**
     * isbn
     */
    @ApiModelProperty("isbn")
    private String isbn;

    /**
     * 商品数量
     */
    @ApiModelProperty("商品数量")
    private Integer qty;

    /**
     * 版次
     */
    @ApiModelProperty("版次")
    private String editionNumber;

    /**
     * 印次
     */
    @ApiModelProperty("印次")
    private String printNumber;

    /**
     * 出版日期
     */
    @ApiModelProperty("出版日期")
    private String pulicateDate;

    /**
     * 印刷日期
     */
    @ApiModelProperty("印刷日期")
    private String printDate;

    /**
     * 出版社
     */
    @ApiModelProperty("出版社")
    private String publishName;

    /**
     * 成本价
     */
    @ApiModelProperty("成本价")
    private BigDecimal basePrice;

    /**
     * 建议销售价
     */
    @ApiModelProperty("建议销售价")
    private BigDecimal salePrice;

    /**
     * 作者
     */
    @ApiModelProperty("作者")
    private String author;

    /**
     * 开本
     */
    @ApiModelProperty("开本")
    private String format;

    /**
     * 页数
     */
    @ApiModelProperty("页数")
    private Integer pageNumber;

    /**
     * 导语
     */
    @ApiModelProperty("导语")
    private String guide;

    /**
     * 内容
     */
    @ApiModelProperty("内容")
    private String content;

    /**
     * 作者简介
     */
    @ApiModelProperty("作者简介")
    private String authorDesc;

    /**
     * 推荐语
     */
    @ApiModelProperty("推荐语")
    private String recommend;

    /**
     * 目录
     */
    @ApiModelProperty("目录")
    private String diretory;

    /**
     * 精彩内容
     */
    @ApiModelProperty("精彩内容")
    private String wonderfulContent;

    /**
     * 版权页图片
     */
    @ApiModelProperty("版权页图片")
    private String copyrightUrl;

    /**
     * 封底夜
     */
    @ApiModelProperty("封底夜")
    private String bottomUrl;

    /**
     * 类目
     */
    @ApiModelProperty("类目")
    private Integer category;

    /**
     * 标题
     */
    @ApiModelProperty("商品名称")
    private String title;

    /**
     * 定价
     */
    @ApiModelProperty("定价")
    private BigDecimal price;

    /**
     * 图片
     */
    @ApiModelProperty("图片")
    private String imageUrl;

    /**
     * 大图
     */
    @ApiModelProperty("大图")
    private String largeImageUrl;

    /**
     * 状态0未审核1已审核2已同步
     */
    @ApiModelProperty("状态0未审核1已审核2已同步3审核失败")
    private Byte status;

    /**
     * 审核失败原因
     */
    @ApiModelProperty("审核失败原因")
    private String aduitError;

    @ApiModelProperty("供应商id")
    private Long providerId;

    @ApiModelProperty("供应商名称")
    private String providerName;

    @ApiModelProperty("详情图")
    private String detailImageUrl;

    @ApiModelProperty("毛利率")
    private BigDecimal rate;

    @ApiModelProperty("父类目")
    private List<String> parentCategory=new ArrayList<>(5);

    @ApiModelProperty("广告法审核状态0：待审核，1和2：审核中， 3：同盾审核成功，4：同盾审核失败")
    private Integer adAuditStatus;

    @ApiModelProperty("广告法人工审核状态0待审核 1 审核通过2审核拒绝")
    private Integer adManualAuditStatus;

    @ApiModelProperty("广告法人工审核驳回原因")
    private String adManualRejectReason;

    @ApiModelProperty("上架状态0待审核1审核通过2审核拒绝")
    private Integer launchStatus;

    @ApiModelProperty("上架驳回原因")
    private String launchRejectReason;

    @ApiModelProperty("同盾审核拒绝列表")
    private List<GoodsAdAuditVO> rejectList;
}
