package com.wanmi.sbc.customer.api.response.company;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Author: songhanlin
 * @Date: Created In 上午11:38 2017/11/14
 * @Description: 工商信息Response
 */
@ApiModel
@Data
public class CompanyInfoResponse {

    /**
     * 公司信息Id
     */
    @ApiModelProperty(value = "公司信息Id")
    private Long companyInfoId;

    /**
     * 社会信用代码
     */
    @ApiModelProperty(value = "社会信用代码")
    private String socialCreditCode;

    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
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
     * 法人身份证正面
     */
    @ApiModelProperty(value = "法人身份证正面")
    private String frontIDCard;

    /**
     * 法人身份证反面
     */
    @ApiModelProperty(value = "法人身份证反面")
    private String backIDCard;

}
