package com.wanmi.sbc.customer.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.GenderType;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.customer.bean.vo.EnterpriseInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 会员基本信息返回数据
 * Created by CHENLI on 2017/7/17.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerBaseInfoResponse {

    @ApiModelProperty(value = "账号")
    private String customerDetailId;

    /**
     * 客户编号
     */
    @ApiModelProperty(value = "客户编号")
    private String customerId;

    /**
     * 客户账号
     */
    @ApiModelProperty(value = "客户账号")
    private String customerAccount;

    /**
     * 客户名称
     */
    @ApiModelProperty(value = "客户名称")
    private String customerName;

    /**
     * 客户等级名称
     */
    @ApiModelProperty(value = "客户等级名称")
    private String customerLevelName;

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
    private String customerAddress;

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
     * 业务员名称
     */
    @ApiModelProperty(value = "业务员名称")
    private String employeeName;

    @ApiModelProperty(value = "头像")
    private String headImg;

    /**
     * 生日
     */
    @ApiModelProperty(value = "生日")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate birthDay;

    /**
     * 性别，0女，1男
     */
    @ApiModelProperty(value = "性别，0女，1男")
    private GenderType gender;

    /**
     * 是否是企业会员
     */
    @ApiModelProperty(value = "是否是企业会员")
    private Boolean isEnterpriseCustomer;

    /**
     * 企业信息
     */
    @ApiModelProperty(value = "企业信息")
    private EnterpriseInfoVO enterpriseInfo;
}
