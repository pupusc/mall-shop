package com.wanmi.sbc.coupon.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 根据客户和券码id查询不可用的平台券以及优惠券实际优惠总额的请求结构
 * @Author: gaomuwei
 * @Date: Created In 上午9:27 2018/9/29
 */
@ApiModel
@Data
public class CouponCheckoutBaseRequest implements Serializable {

    private static final long serialVersionUID = -4150614606625262723L;

    /**
     * 已勾选的优惠券码id
     */
    @ApiModelProperty(value = "已勾选的优惠券码id集合")
    @NotNull
    private List<String> couponCodeIds;

}
