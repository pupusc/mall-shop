package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.customer.bean.vo.DistributionCustomerInviteInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>分销员邀新信息新增结果（运营后台）</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerInviteInfoAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的分销员邀新信息
     */
    @ApiModelProperty(value = "分销员邀新信息")
    private DistributionCustomerInviteInfoVO distributionCustomerInviteInfoVO;
}
