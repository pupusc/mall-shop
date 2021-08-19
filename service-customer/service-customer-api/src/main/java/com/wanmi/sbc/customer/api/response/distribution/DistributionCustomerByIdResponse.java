package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据会员编号查询（未删除）分销员信息response</p>
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionCustomerByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分销员信息
     */
    private DistributionCustomerVO distributionCustomerVO;
}
