package com.wanmi.sbc.customer.api.response.distribution.performance;

import com.wanmi.sbc.customer.bean.vo.DistributionPerformanceByMonthVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>分销业绩查询近6个月数据response</p>
 * Created by of628-wenzhi on 2019-04-18-18:41.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionPerformanceByLast6MonthsQueryResponse implements Serializable {
    private static final long serialVersionUID = -8439546365220497940L;

    /**
     * 分销业绩集合
     */
    @ApiModelProperty("分销业绩集合")
    private List<DistributionPerformanceByMonthVO> dataList;

    /**
     * 销售额合计
     */
    @ApiModelProperty(value = "销售额合计")
    private BigDecimal totalSaleAmount = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN);

    /**
     * 预估收益合计
     */
    @ApiModelProperty(value = "预估收益合计")
    private BigDecimal totalCommission = BigDecimal.ZERO.setScale(2, BigDecimal.ROUND_DOWN);

}
