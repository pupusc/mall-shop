package com.wanmi.sbc.customer.api.request.loginregister;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 会员登录注册-注册Request
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRegisterRequest extends CustomerBaseRequest implements Serializable {


    private static final long serialVersionUID = 1969208490000731631L;

    @ApiModelProperty(value = "负责业务员")
    private String employeeId;

    @ApiModelProperty(value = "会员信息-共用DTO")
    @NotNull
    private CustomerDTO customerDTO;

    @ApiModelProperty(value = "会员id")
    private String customerId;

    @ApiModelProperty(value = "企业名称")
    private String enterpriseName;

    @ApiModelProperty(value = "统一社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty(value = "公司性质")
    private Integer businessNatureType;

    @ApiModelProperty(value = "公司行业")
    private Integer businessIndustryType;

    @ApiModelProperty(value = "营业执照地址")
    private String businessLicenseUrl;

    @ApiModelProperty(value = "企业会员审核状态")
    private DefaultFlag enterpriseCustomerAuditFlag;
}
