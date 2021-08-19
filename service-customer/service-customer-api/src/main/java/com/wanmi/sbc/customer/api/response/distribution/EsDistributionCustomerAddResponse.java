package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.customer.bean.vo.DistributionCustomerShowPhoneVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author houshuai
 * @date 2020/12/9 16:29
 * @description  <p> </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class EsDistributionCustomerAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的分销员信息
     */
    @ApiModelProperty(value = "分销员信息")
    private DistributionCustomerShowPhoneVO esDistributionCustomerVO;
}
