package com.wanmi.sbc.customer.api.response.distribution.performance;

import com.wanmi.sbc.customer.bean.vo.DistributionPerformanceSummaryVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>查询指定分销员指定日期范围内汇总业绩数据response</p>
 * Created by of628-wenzhi on 2019-04-23-18:32.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionPerformanceSummaryQueryResponse implements Serializable {
    private static final long serialVersionUID = -3859475772270782561L;

    /**
     * 分销员业绩月汇总数据
     */
    @ApiModelProperty("分销员业绩月汇总数据")
    private List<DistributionPerformanceSummaryVO> dataList;
}
