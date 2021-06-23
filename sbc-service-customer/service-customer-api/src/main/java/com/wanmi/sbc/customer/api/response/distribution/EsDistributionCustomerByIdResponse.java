package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.customer.bean.vo.DistributionCustomerShowPhoneVO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsDistributionCustomerByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分销员信息
     */
    private DistributionCustomerShowPhoneVO distributionCustomerVO;
}