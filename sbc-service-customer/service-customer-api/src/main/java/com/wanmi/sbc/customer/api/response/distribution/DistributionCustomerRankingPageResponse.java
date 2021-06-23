package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerRankingVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>用户分销排行榜分页结果</p>
 * @author lq
 * @date 2019-04-19 10:05:05
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerRankingPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户分销排行榜分页结果
     */
    private MicroServicePage<DistributionCustomerRankingVO> distributionCustomerRankingVOPage;
}
