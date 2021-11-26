package com.wanmi.sbc.crm.api.response.customerplan;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p> 人群运营计划分页结果</p>
 * @author dyt
 * @date 2020-01-07 17:07:02
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  人群运营计划分页结果
     */
    @ApiModelProperty(value = " 人群运营计划分页结果")
    private MicroServicePage<CustomerPlanVO> customerPlanVOPage;
}
