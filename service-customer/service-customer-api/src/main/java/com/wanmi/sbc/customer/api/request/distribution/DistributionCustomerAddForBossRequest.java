package com.wanmi.sbc.customer.api.request.distribution;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

/**
 * <p>分销员新增参数（运营端）</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class DistributionCustomerAddForBossRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 会员登录账号|手机号
     */
    @ApiModelProperty(value = "会员登录账号|手机号")
    @NotBlank
    @Length(max = 20)
    private String customerAccount;

    /**
     * 负责业务员
     */
    @ApiModelProperty(value = "负责业务员")
    private String employeeId;


    /**
     * 客户类型 0:平台客户,1:商家客户
     */
    @ApiModelProperty(value = "客户类型")
    private CustomerType customerType;

    /**
     * 分销员等级ID
     */
    @ApiModelProperty(value = "分销员等级ID")
    private String distributorLevelId;

}