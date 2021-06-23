package com.wanmi.sbc.customer.api.request.store;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.AccountType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.api.request.store.validGroups.StoreUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 店铺基本信息参数
 * Created by CHENLI on 2017/11/2.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreSaveRequest extends BaseRequest {
    private static final long serialVersionUID = -4687907203853776352L;
    /**
     * 店铺主键
     */
    @ApiModelProperty(value = "店铺主键")
    @NotNull(groups = {StoreUpdate.class})
    private Long storeId;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;

    /**
     * 店铺店招
     */
    @ApiModelProperty(value = "店铺店招")
    private String storeSign;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    @NotNull(groups = {StoreUpdate.class})
    private Long companyInfoId;

    /**
     * 商家名称
     */
    @ApiModelProperty(value = "商家名称")
    @NotBlank
    @Length(max = 20)
    private String supplierName;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    @NotBlank
    @Length(max = 20)
    private String storeName;

    /**
     * 联系人名字
     */
    @ApiModelProperty(value = "联系人名字")
    @NotBlank
    @Length(min = 2, max = 15)
    private String contactPerson;

    /**
     * 联系方式
     */
    @ApiModelProperty(value = "联系方式")
    @NotBlank
    @Length(max = 11)
    private String contactMobile;

    /**
     * 联系邮箱
     */
    @ApiModelProperty(value = "联系邮箱")
    @NotBlank
    @Length(min = 1, max = 100)
    @Email
    private String contactEmail;

    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    @NotNull
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
    @NotBlank
    @Length(max = 60)
    private String addressDetail;

    /**
     * 是否重置密码 0 否 1 是
     */
    @ApiModelProperty(value = "是否重置密码")
    private Boolean isResetPwd = Boolean.FALSE;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String accountPassword;

    /**
     * 商家账号
     */
    @ApiModelProperty(value = "商家账号")
    private String accountName;

    /**
     * 账号类型 0 b2b账号 1 s2b平台端账号 2 s2b商家端账号
     */
    @ApiModelProperty(value = "账号类型")
    private AccountType accountType;

    /**
     * 使用的运费模板类别(0:店铺运费,1:单品运费)
     */
    @ApiModelProperty(value = "使用的运费模板类别(0:店铺运费,1:单品运费)")
    private DefaultFlag freightTemplateType = DefaultFlag.NO;
}
