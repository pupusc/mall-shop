package com.wanmi.sbc.elastic.api.request.customer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsCustomerCheckStateModifyRequest implements Serializable {
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
