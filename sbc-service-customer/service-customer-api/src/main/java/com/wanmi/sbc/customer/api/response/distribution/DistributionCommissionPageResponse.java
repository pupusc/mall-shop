package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.customer.bean.vo.DistributionCommissionForPageVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by feitingting on 2019/2/26.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistributionCommissionPageResponse implements Serializable {
    /**
     * 分销佣金分页
     */
    @ApiModelProperty(value = "分销佣金分页")
    private List<DistributionCommissionForPageVO> recordList;

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private Long total;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Integer currentPage;
}
