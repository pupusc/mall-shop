package com.wanmi.sbc.customer.api.request.customer;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class CustomerCheckStateModifyRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = -8427145177039519006L;

    /**
     * 审核状态 0：待审核 1：已审核 2：审核未通过
     */
    @ApiModelProperty(value = "审核状态(0:待审核,1:已审核,2:审核未通过)",dataType = "com.wanmi.sbc.customer.bean.enums.CheckState")
    @NotNull
    private Integer checkState;

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
    private String rejectReason;
}
