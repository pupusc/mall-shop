package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 会员增票信息
 */
@ApiModel
@Data
public class CustomerInvoiceVO implements Serializable{

    private static final long serialVersionUID = -5225543641610244083L;
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
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

    /**
     * 单位全称
     */
    @ApiModelProperty(value = "单位全称")
    private String companyName;

    /**
     * 纳税人识别号
     */
    @ApiModelProperty(value = "纳税人识别号")
    private String taxpayerNumber;

    /**
     * 单位电话
     */
    @ApiModelProperty(value = "单位电话")
    private String companyPhone;

    /**
     * 单位地址
     */
    @ApiModelProperty(value = "单位地址")
    private String companyAddress;

    /**
     * 银行基本户号
     */
    @ApiModelProperty(value = "银行基本户号")
    private String bankNo;

    /**
     * 开户行
     */
    @ApiModelProperty(value = "开户行")
    private String bankName;

    /**
     * 营业执照复印件
     */
    @ApiModelProperty(value = "营业执照复印件")
    private String businessLicenseImg;

    /**
     * 一般纳税人认证资格复印件
     */
    @ApiModelProperty(value = "一般纳税人认证资格复印件")
    private String taxpayerIdentificationImg;

    /**
     * 增票资质审核状态  0:待审核 1:已审核通过 2:审核未通过
     */

    @ApiModelProperty(value = "增票资质审核状态")
    private CheckState checkState;

    /**
     * 审核未通过原因
     */
    @ApiModelProperty(value = "审核未通过原因")
    private String rejectReason;

    /**
     * 增专资质是否作废 0：否 1：是
     */
    @ApiModelProperty(value = "增专资质是否作废")
    private InvalidFlag invalidFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
}
