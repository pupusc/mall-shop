package com.wanmi.sbc.customer.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.GenderType;
import com.wanmi.sbc.common.enums.TerminalType;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.customer.validGroups.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * Created by dyt on 2017/7/11.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    /**
     * 用户编号
     */
    @ApiModelProperty(value = "用户编号")
    @NotBlank(groups = {NotCustomerId.class})
    private String customerId;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号")
    @NotBlank(groups = {NotCustomerAccount.class})
    private String customerAccount;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    @NotBlank(groups = {NotPassword.class})
    private String customerPassword;

    /**
     * 验证码
     */
    @ApiModelProperty(value = "验证码")
    @NotBlank(groups = {NotVerify.class})
    private String verifyCode;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

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
    @ApiModelProperty(value = "区")
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
     * 是否是忘记密码 true：忘记密码 | false：
     */
    @ApiModelProperty(value = "是否是忘记密码")
    private Boolean isForgetPassword;

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

    /**
     * 企业会员是否第一次点击注册
     */
    @ApiModelProperty(value = "企业会员是否第一次点击注册")
    private Boolean firstRegisterFlag;

    /**
     * 终端类型，用于区分三端不同逻辑处理  PC:0 H5:1 APP:2
     */
    @ApiModelProperty(value = "渠道终端")
    private TerminalType terminalType;


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
}
