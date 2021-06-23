package com.wanmi.sbc.customer.api.request.distribution;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
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
public class DistributionCustomerCheckRequest implements Serializable {


    private static final long serialVersionUID = -3017325452893786016L;
    /**
     * 会员标识UUID
     */
    @NotBlank
    private String customerId ;

    /**
     * 是否开启社交分销
     */
    private DefaultFlag openFlag;
}