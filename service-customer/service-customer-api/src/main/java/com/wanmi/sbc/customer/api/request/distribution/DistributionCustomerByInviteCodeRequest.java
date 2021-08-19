package com.wanmi.sbc.customer.api.request.distribution;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerByInviteCodeRequest implements Serializable {


    private static final long serialVersionUID = -3017325452893786016L;
    /**
     * 邀请码
     */
    @NotNull
    private String inviteCode ;
}