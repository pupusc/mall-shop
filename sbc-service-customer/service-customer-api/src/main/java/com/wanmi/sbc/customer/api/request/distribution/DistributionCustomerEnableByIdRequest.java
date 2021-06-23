package com.wanmi.sbc.customer.api.request.distribution;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>依据分销员id验证分销员状态</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerEnableByIdRequest implements Serializable {


    private static final long serialVersionUID = -3017325452893786016L;
    /**
     * 分销员标识UUID
     */
    @NotNull
    private String distributionId;
}