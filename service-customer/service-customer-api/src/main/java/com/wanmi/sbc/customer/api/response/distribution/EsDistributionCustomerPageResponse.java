package com.wanmi.sbc.customer.api.response.distribution;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerShowPhoneVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>分销员分页结果</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsDistributionCustomerPageResponse implements Serializable {


    private static final long serialVersionUID = 2143453846639078233L;
    /**
     * 分销员分页结果
     */
    @ApiModelProperty(value = "分销员分页结果")
    private MicroServicePage<DistributionCustomerShowPhoneVO> distributionCustomerVOPage;
}