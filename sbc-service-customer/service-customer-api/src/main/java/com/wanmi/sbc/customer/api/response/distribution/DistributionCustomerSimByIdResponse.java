package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.customer.bean.vo.DistributionCustomerSimVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据会员编号查询（未删除）分销员信息response</p>
 * @author baijz
 * @date 2019-03-04 10:13:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerSimByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分销员信息
     */
    private DistributionCustomerSimVO distributionCustomerSimVO;

}
