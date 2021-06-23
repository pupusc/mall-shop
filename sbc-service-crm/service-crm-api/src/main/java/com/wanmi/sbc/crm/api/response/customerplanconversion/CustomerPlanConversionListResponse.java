package com.wanmi.sbc.crm.api.response.customerplanconversion;

import com.wanmi.sbc.crm.bean.vo.CustomerPlanConversionVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>运营计划转化效果列表结果</p>
 * @author zhangwenchang
 * @date 2020-02-12 00:16:50
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanConversionListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 运营计划转化效果列表结果
     */
    @ApiModelProperty(value = "运营计划转化效果列表结果")
    private List<CustomerPlanConversionVO> customerPlanConversionVOList;
}
