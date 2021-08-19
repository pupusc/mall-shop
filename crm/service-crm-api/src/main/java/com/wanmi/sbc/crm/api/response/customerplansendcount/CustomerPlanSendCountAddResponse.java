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
 * <p>权益礼包优惠券发放统计表新增结果</p>
 * @author zhanghao
 * @date 2020-02-04 13:29:18
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanSendCountAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的权益礼包优惠券发放统计表信息
     */
    @ApiModelProperty(value = "已新增的权益礼包优惠券发放统计表信息")
    private CustomerPlanSendCountVO customerPlanSendCountVO;
}
