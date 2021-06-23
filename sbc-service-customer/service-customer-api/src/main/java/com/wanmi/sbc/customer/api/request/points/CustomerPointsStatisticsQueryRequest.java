package com.wanmi.sbc.customer.api.request.points;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;

import java.util.List;

/**
 * <p>会员积分明细通用查询请求参数</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPointsStatisticsQueryRequest extends BaseQueryRequest {
    private static final long serialVersionUID = 1L;

    /**
     * 用户id列表
     */
    private List<String> customerIdList;


    /**
     * 负责业务员
     */
    private String employeeId;

    /**
     * 负责业务员ID集合
     */
    private List<String> employeeIds;
}