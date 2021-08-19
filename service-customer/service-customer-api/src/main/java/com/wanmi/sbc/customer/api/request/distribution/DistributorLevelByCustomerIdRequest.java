package com.wanmi.sbc.customer.api.request.distribution;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotNull;


@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributorLevelByCustomerIdRequest extends CustomerBaseRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 会员编号UUID
     */
    @NotNull
    private String customerId;
}