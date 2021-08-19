package com.wanmi.sbc.customer.api.request.distribution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: HouShuai
 * @date: 2020/12/7 15:45
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DistributorLevelByIdsRequest {

    /**
     * 分销员等级id
     */
    List<String> idList;
}
