package com.wanmi.sbc.elastic.api.response.customer;

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
 * @author houshuai
 * @date 2020/12/9 21:09
 * @description <p> </p>
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
