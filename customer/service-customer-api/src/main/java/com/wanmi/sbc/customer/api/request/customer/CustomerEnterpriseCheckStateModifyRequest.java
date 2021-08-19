package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class CustomerEnterpriseCheckStateModifyRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -8427145177039519006L;

    /**
     * 审核状态 1：待审核 2：已审核 3：审核未通过
     */
    @ApiModelProperty(value = "审核状态 1：待审核 2：已审核 3：审核未通过")
    @NotNull
    private EnterpriseCheckState enterpriseCheckState;

    /**
     * 客户ID
     */
    @ApiModelProperty(value = "客户ID")
    @NotNull
    private String customerId;

    /**
     * 审核驳回原因
     */
    @ApiModelProperty(value = "审核驳回原因")
    private String enterpriseCheckReason;
}
