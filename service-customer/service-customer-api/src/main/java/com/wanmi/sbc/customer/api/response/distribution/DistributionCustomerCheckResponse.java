package com.wanmi.sbc.customer.api.response.distribution;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 是否分销员
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerCheckResponse implements Serializable {


    private static final long serialVersionUID = -3017325452893786016L;
    /**
     * 是否分销员
     */
    private Boolean enable;
}