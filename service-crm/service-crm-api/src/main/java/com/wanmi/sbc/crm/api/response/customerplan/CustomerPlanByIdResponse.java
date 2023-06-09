package com.wanmi.sbc.crm.api.response.customerplan;

import com.wanmi.sbc.crm.bean.vo.CustomerPlanAppPushVO;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanCouponVO;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanSmsVO;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除） 人群运营计划信息response</p>
 * @author dyt
 * @date 2020-01-07 17:07:02
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  人群运营计划信息
     */
    @ApiModelProperty(value = "人群运营计划信息")
    private CustomerPlanVO customerPlanVO;

    /**
     *  人群运营计划优惠券列表
     */
    @ApiModelProperty(value = "人群运营计划优惠券列表")
    private List<CustomerPlanCouponVO> customerPlanCouponList;

    /**
     *  人群运营计划SMS信息
     */
    @ApiModelProperty(value = "人群运营计划SMS信息")
    private CustomerPlanSmsVO planSms;

    /**
     *  人群运营计划APP通知信息
     */
    @ApiModelProperty(value = "人群运营计划APP通知信息")
    private CustomerPlanAppPushVO planAppPush;
}
