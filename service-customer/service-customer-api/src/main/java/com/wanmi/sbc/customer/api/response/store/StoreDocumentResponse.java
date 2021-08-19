package com.wanmi.sbc.customer.api.response.store;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateSumVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 店铺基本信息
 * (安全考虑只保留必要信息,隐藏前端会员无需知道的信息)
 * Created by bail on 2017/11/29.
 */
@ApiModel
@Data
public class StoreDocumentResponse {
    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    private Long storeId;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long companyInfoId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 店铺小程序码
     */
    @ApiModelProperty(value = "店铺小程序码")
    private String smallProgramCode;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型",dataType = "com.wanmi.sbc.customer.bean.enums.CompanyType")
    private Integer companyType;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 联系人名字
     */
    @ApiModelProperty(value = "联系人名字")
    private String contactPerson;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contactMobile;

    /**
     * 联系邮箱
     */
    @ApiModelProperty(value = "联系邮箱")
    private String contactEmail;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private Long provinceId;

    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private Long cityId;

    /**
     * 区
     */
    @ApiModelProperty(value = "区")
    private Long areaId;

    /**
     * 街道
     */
    @ApiModelProperty(value = "街道")
    private Long streetId;

    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String addressDetail;

    /**
     * 社会信用代码
     */
    @ApiModelProperty(value = "社会信用代码")
    private String socialCreditCode;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String companyName;

    /**
     * 住所
     */
    @ApiModelProperty(value = "住所")
    private String address;

    /**
     * 法定代表人
     */
    @ApiModelProperty(value = "法定代表人")
    private String legalRepresentative;

    /**
     * 注册资本
     */
    @ApiModelProperty(value = "注册资本")
    private BigDecimal registeredCapital;

    /**
     * 成立日期
     */
    @ApiModelProperty(value = "成立日期")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime foundDate;

    /**
     * 营业期限自
     */
    @ApiModelProperty(value = "营业期限自")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime businessTermStart;

    /**
     * 营业期限至
     */
    @ApiModelProperty(value = "营业期限至")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime businessTermEnd;

    /**
     * 经营范围
     */
    @ApiModelProperty(value = "经营范围")
    private String businessScope;

    /**
     * 营业执照副本电子版
     */
    @ApiModelProperty(value = "营业执照副本电子版")
    private String businessLicence;

    /**
     * 类目经营资质图片
     */
    @ApiModelProperty(value = "类目经营资质图片")
    private List<String> catePicList;

    /**
     * 品牌经营资质图片
     */
    @ApiModelProperty(value = "品牌经营资质图片")
    private List<String> brandPicList;

    /**
     * 店铺服务
     */
    @ApiModelProperty(value = "店铺服务")
    private StoreEvaluateSumVO storeEvaluateSumVO;

    /**
     * 店铺spu总数
     */
    @ApiModelProperty(value = "spu总数")
    private long goodsCount;

    /**
     * 店铺sku总数
     */
    @ApiModelProperty(value = "sku总数")
    private long goodsInfoCount;

    /**
     * 店铺关注总数
     */
    @ApiModelProperty(value = "收藏总数")
    private long followCount;

}
