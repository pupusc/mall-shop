package com.wanmi.sbc.login;

import com.wanmi.sbc.common.enums.BoolFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录返回
 * Created by aqlu on 15/11/28.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse implements Serializable{

    /**
     * jwt验证token
     */
    @ApiModelProperty(value = "jwt验证token")
    private String token;

    /**
     * 账号名称
     */
    @ApiModelProperty(value = "账号名称")
    private String accountName;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String mobile;

    /**
     * 公司信息ID
     */
    @ApiModelProperty(value = "公司信息ID")
    private Long companyInfoId;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    /**
     * 审核状态 0、待审核 1、已审核 2、审核未通过
     */
    @ApiModelProperty(value = "审核状态", dataType = "com.wanmi.sbc.order.bean.enums.AuditState")
    private Integer auditState;

    /**
     * 是否主账号
     */
    @ApiModelProperty(value = "是否主账号", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer isMasterAccount;

    /**
     * 是否业务员
     */
    @ApiModelProperty(value = "是否业务员", dataType = "com.wanmi.sbc.common.enums.DefaultFlag")
    private Integer isEmployee;

    /**
     * 业务员id
     */
    @ApiModelProperty(value = "业务员id")
    private String employeeId;

    /**
     * 业务员名称
     */
    @ApiModelProperty(value = "业务员名称")
    private String employeeName;

    /**
     * 商家类型 0、平台自营 1、第三方商家
     */
    @ApiModelProperty(value = "商家类型(0、平台自营 1、第三方商家)")
    private BoolFlag companyType;
    
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String supplierName;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    /**
     * 店铺logo
     */
    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;

}
