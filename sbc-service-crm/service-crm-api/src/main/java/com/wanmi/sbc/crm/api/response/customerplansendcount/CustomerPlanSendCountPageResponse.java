package com.wanmi.sbc.crm.api.response.customerplansendcount;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanSendCountVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>权益礼包优惠券发放统计表分页结果</p>
 * @author zhanghao
 * @date 2020-02-04 13:29:18
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanSendCountPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 权益礼包优惠券发放统计表分页结果
     */
    @ApiModelProperty(value = "权益礼包优惠券发放统计表分页结果")
    private MicroServicePage<CustomerPlanSendCountVO> customerPlanSendCountVOPage;
}
