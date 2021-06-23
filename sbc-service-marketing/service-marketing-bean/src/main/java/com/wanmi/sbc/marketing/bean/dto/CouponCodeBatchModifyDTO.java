package com.wanmi.sbc.marketing.bean.dto;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 批量更新券码使用状态信息结构
 * @Author: gaomuwei
 * @Date: Created In 下午7:47 2018/10/9
 * @Description:
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CouponCodeBatchModifyDTO implements Serializable {

    private static final long serialVersionUID = 4820650764641567327L;

    /**
     * 优惠券码id
     */
    @ApiModelProperty(value = "优惠券码id")
    private String couponCodeId;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    /**
     * 使用状态
     */
    @ApiModelProperty(value = "优惠券是否已使用")
    private DefaultFlag useStatus;

    /**
     * 使用的订单号
     */
    @ApiModelProperty(value = "使用的订单号")
    private String orderCode;

}
