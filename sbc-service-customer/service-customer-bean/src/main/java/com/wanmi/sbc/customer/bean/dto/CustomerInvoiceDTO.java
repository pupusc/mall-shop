package com.wanmi.sbc.customer.bean.dto;


import com.wanmi.sbc.customer.bean.enums.CheckState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * 会员增票资质-共用DTO
 */
@ApiModel
@Data
public class CustomerInvoiceDTO implements Serializable {

    private static final long serialVersionUID = -5166953281820654274L;

    /**
     * 增专资质ID
     */
    @ApiModelProperty(value = "增专资质ID")
    private Long customerInvoiceId;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * 单位全称
     */
    @ApiModelProperty(value = "单位全称")
    @NotBlank
    private String companyName;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    @NotBlank
    private String taxpayerNumber;

    /**
     * 单位电话
     */
    @ApiModelProperty(value = "单位电话")
    @NotBlank
    private String companyPhone;

    /**
     * 单位地址
     */
    @ApiModelProperty(value = "单位地址")
    @NotBlank
    private String companyAddress;

    /**
     * 银行基本户号
     */
    @ApiModelProperty(value = "银行基本户号")
    @NotBlank
    private String bankNo;

    /**
     * 开户行
     */
    @ApiModelProperty(value = "开户行")
    @NotBlank
    private String bankName;

    /**
     * 营业执照复印件
     */
    @ApiModelProperty(value = "营业执照复印件")
    @NotBlank
    private String businessLicenseImg;

    /**
     * 一般纳税人认证资格复印件
     */
    @ApiModelProperty(value = "一般纳税人认证资格复印件")
    @NotBlank
    private String taxpayerIdentificationImg;

    /**
     * 增票资质审核状态
     */
    @ApiModelProperty(value = "增票资质审核状态")
    private CheckState checkState;

}
