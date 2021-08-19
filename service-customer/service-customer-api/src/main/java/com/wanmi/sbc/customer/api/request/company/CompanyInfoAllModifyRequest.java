package com.wanmi.sbc.customer.api.request.company;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 公司信息参数
 * Created by daiyitian on 2017/5/12.
 */
@ApiModel
@Data
public class CompanyInfoAllModifyRequest implements Serializable {

    private static final long serialVersionUID = 2786039114279422918L;
    /**
     * 编号
     */
    @ApiModelProperty(value = "编号")
    private Long companyInfoId;

    /**
     * 商家编号
     */
    @ApiModelProperty(value = "商家编号")
    private String companyCode;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    private String supplierName;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "公司名称")
    private String companyName;

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
    private String detailAddress;

    /**
     * 联系人名字
     */
    @ApiModelProperty(value = "联系人名字")
    private String contactName;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    private String contactPhone;

    /**
     * 版权信息
     */
    @ApiModelProperty(value = "版权信息")
    private String copyright;

    /**
     * 公司简介
     */
    @ApiModelProperty(value = "公司简介")
    private String companyDescript;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private String operator;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime updateTime;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型-0：平台自营 1：第三方商家")
    private BoolFlag companyType;

    /**
     * 一对多关系，多个SPU编号
     */
    @ApiModelProperty(value = "一对多关系，多个SPU编号")
    private List<String> goodsIds = new ArrayList<>();

    /**
     * 社会信用代码
     */
    @ApiModelProperty(value = "社会信用代码")
    private String socialCreditCode;

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
     * 法人身份证正面
     */
    @ApiModelProperty(value = "法人身份证正面")
    private String frontIDCard;

    /**
     * 法人身份证反面
     */
    @ApiModelProperty(value = "法人身份证反面")
    private String backIDCard;


    /**
     * 删除标志 0未删除 1已删除
     */
    @ApiModelProperty(value = "删除标志")
    private DeleteFlag delFlag;

    /**
     * 是否确认打款
     */
    @ApiModelProperty(value = "是否确认打款")
    private BoolFlag remitAffirm;

    /**
     * 入驻时间(第一次审核通过时间)
     */
    @ApiModelProperty(value = "入驻时间(第一次审核通过时间)")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime applyEnterTime;
}
