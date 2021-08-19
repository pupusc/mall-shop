package com.wanmi.sbc.customer.api.response.distribution.performance;

import com.wanmi.sbc.customer.bean.vo.DistributionPerformanceByDayVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>查询分销员昨日业务数据返回结构</p>
 * Created by of628-wenzhi on 2019-06-18-16:44.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class DistributionPerformanceYesterdayQueryResponse implements Serializable {

    private static final long serialVersionUID = -2933921103804445650L;

    @ApiModelProperty("分销员昨日业绩数据")
    private DistributionPerformanceByDayVO performanceByDayVO;
}
