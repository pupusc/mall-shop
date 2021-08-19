package com.wanmi.sbc.elastic.api.response.customer;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.elastic.bean.vo.customer.EsDistributionCustomerVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: HouShuai
 * @date: 2020/12/7 11:23
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsDistributionCustomerPageResponse {

    private MicroServicePage<EsDistributionCustomerVO> distributionCustomerVOPage;
}
