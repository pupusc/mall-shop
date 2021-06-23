package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>分销员导出结果</p>
 *
 * @author of2975
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerExportResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分销员导出结果
     */
    @ApiModelProperty(value = "分销员导出结果")
    private List<DistributionCustomerVO> distributionCustomerVOList;
}
