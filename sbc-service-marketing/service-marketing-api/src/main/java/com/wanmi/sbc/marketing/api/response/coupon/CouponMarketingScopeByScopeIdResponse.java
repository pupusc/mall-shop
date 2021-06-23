package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.marketing.bean.vo.CouponMarketingScopeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据优惠券范围id查询优惠券商品作用范列表响应结构
 * @Author: daiyitian
 * @Date: Created In 下午5:58 2018/11/23
 * @Description: 使用优惠券列表请求对象
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponMarketingScopeByScopeIdResponse implements Serializable {

    private static final long serialVersionUID = 2572855130915278064L;

    /**
     * 优惠券商品作用范围列表
     */
    @ApiModelProperty(value = "优惠券商品作用范围列表")
    private List<CouponMarketingScopeVO> scopeVOList;
}
