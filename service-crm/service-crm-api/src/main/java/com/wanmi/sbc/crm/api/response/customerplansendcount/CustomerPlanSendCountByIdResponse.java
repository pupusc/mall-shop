package com.wanmi.sbc.crm.api.response.customerplansendcount;

import com.wanmi.sbc.crm.bean.vo.CustomerPlanSendCountVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）权益礼包优惠券发放统计表信息response</p>
 * @author zhanghao
 * @date 2020-02-04 13:29:18
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanSendCountByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 权益礼包优惠券发放统计表信息
     */
    @ApiModelProperty(value = "权益礼包优惠券发放统计表信息")
    private CustomerPlanSendCountVO customerPlanSendCountVO;
}
