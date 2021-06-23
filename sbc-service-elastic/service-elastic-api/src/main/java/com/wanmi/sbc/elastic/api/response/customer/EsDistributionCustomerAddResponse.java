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

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsDistributionCustomerAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的分销员信息
     */
    @ApiModelProperty(value = "分销员信息")
    private List<DistributionCustomerVO> distributionCustomerVO;
}