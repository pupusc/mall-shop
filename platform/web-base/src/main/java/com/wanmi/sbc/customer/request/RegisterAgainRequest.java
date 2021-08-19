package com.wanmi.sbc.customer.request;

import com.wanmi.sbc.customer.validGroups.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * 企业会员再次申请审核请求类
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterAgainRequest {

    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    @NotBlank(groups = {NotCustomerId.class})
    private String customerId;

    /**
     * 图片验证码
     */
    @ApiModelProperty(value = "图片验证码")
    @NotBlank(groups = NotPatchca.class)
    private String patchca;

    /**
     * 图片验证码的key
     */
    @ApiModelProperty(value = "图片验证码的key")
    @NotBlank(groups = NotPatchca.class)
    private String uuid;

    /**
     * 业务员id
     */
    @ApiModelProperty(value = "业务员id")
    private String employeeId;

    /**
     * 邀请人id
     */
    @ApiModelProperty(value = "邀请人id")
    private String inviteeId;

    /**
     * 分享人id
     */
    @ApiModelProperty(value = "分享人id")
    private String shareUserId;

    /**
     * 邀请码
     */
    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    /**
     * 公司性质
     */
    @ApiModelProperty(value = "公司性质")
    private Integer businessNatureType;

    /**
     * 公司名称
     */
    @ApiModelProperty(value = "企业名称")
    private String enterpriseName;

    /**
     * 统一社会信用代码
     */
    @ApiModelProperty(value = "统一社会信用代码")
    private String socialCreditCode;

    /**
     * 营业执照地址
     */
    @ApiModelProperty(value = "营业执照地址")
    private String businessLicenseUrl;
}
