package com.wanmi.sbc.elastic.api.request.customer;

import com.wanmi.sbc.customer.bean.vo.DistributionCustomerShowPhoneVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: HouShuai
 * @date: 2020/12/8 16:55
 * @description:
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EsDistributionCustomerAddRequest {

    /**
     * 已新增的分销员信息
     */
    @ApiModelProperty(value = "分销员信息")
    private List<DistributionCustomerShowPhoneVO> list;
}
