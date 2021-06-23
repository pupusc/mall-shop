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
 * 分销员佣金导出结果
 * Created by of2975 on 2019/4/30.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DistributionCommissionExportResponse implements Serializable {
    /**
     * 分销佣金导出结果
     */
    @ApiModelProperty(value = "分销佣金导出结果")
    private List<DistributionCommissionForPageVO> recordList;

}
