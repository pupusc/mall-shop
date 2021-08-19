package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.marketing.bean.vo.CheckGoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 根据客户和券码id查询不可用的平台券以及优惠券实际优惠总额的响应结构
 * @Author: gaomuwei
 * @Date: Created In 上午10:49 2018/9/29
 * @Description:
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponCheckoutResponse implements Serializable {

    private static final long serialVersionUID = 8924280663833144720L;

    /**
     * 没有达到门槛的平台优惠券码id
     */
    @ApiModelProperty(value = "没有达到门槛的平台优惠券码id")
    private List<String> unreachedIds;

    /**
     * 计算完优惠券均摊价的商品总价
     */
    @ApiModelProperty(value = "计算完优惠券均摊价的商品总价")
    private BigDecimal totalPrice;

    /**
     * 优惠券优惠总价
     */
    @ApiModelProperty(value = "优惠券优惠总价")
    private BigDecimal couponTotalPrice;

    /**
     * 均摊完优惠券后的商品价格
     */
    @ApiModelProperty(value = "均摊完优惠券后的商品价格")
    List<CheckGoodsInfoVO> checkGoodsInfos;

}
