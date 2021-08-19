package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.customer.bean.vo.DistributorLevelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributorLevelByCustomerIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分销员等级信息
     */
    @ApiModelProperty(value = "分销员等级信息")
    private DistributorLevelVO distributorLevelVO;
}
