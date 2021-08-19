package com.wanmi.sbc.marketing.api.response.distribution;

import com.wanmi.sbc.customer.bean.vo.DistributionCustomerSimVO;
import com.wanmi.sbc.marketing.bean.vo.DistributionSettingSimVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>查询分销设置simple响应</p>
 *
 * @author baijz
 * @date 2019-02-19 10:08:02
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionSimSettingResponse {

    /**
     * 分销设置信息
     */
    DistributionSettingSimVO distributionSettingSimVO;

    /**
     * 分销员信息
     */
    DistributionCustomerSimVO distributionCustomerSimVO;
}
