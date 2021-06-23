package com.wanmi.sbc.customer.api.response.distribution.performance;

import com.wanmi.sbc.customer.bean.vo.DistributionPerformanceByDayVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>按天查询分销业绩response</p>
 * Created by of628-wenzhi on 2019-04-17-18:56.
 */
@Data
@ApiModel
@AllArgsConstructor
@NoArgsConstructor
public class DistributionPerformanceByDayQueryResponse implements Serializable {
    private static final long serialVersionUID = -5662508505553713045L;

    /**
     * 分销业绩集合
     */
    @ApiModelProperty(value = "分销业绩集合")
    private List<DistributionPerformanceByDayVO> dataList;

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
