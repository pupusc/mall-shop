package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerInviteInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>分销员邀请信息分页结果</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerInviteInfoPageResponse implements Serializable {


    private static final long serialVersionUID = 6753794928472664673L;
    /**
     * 分销员邀请信息分页结果
     */
    @ApiModelProperty(value = "分销员邀请信息分页结果")
    private MicroServicePage<DistributionCustomerInviteInfoVO> distributionCustomerInviteInfoVOPage;
}
