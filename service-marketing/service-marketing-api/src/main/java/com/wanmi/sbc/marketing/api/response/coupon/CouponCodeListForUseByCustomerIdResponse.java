package com.wanmi.sbc.marketing.api.response.coupon;

import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 查询使用优惠券页需要的优惠券列表响应结构
 * @Author: daiyitian
 * @Date: Created In 下午5:58 2018/11/23
 * @Description: 使用优惠券列表请求对象
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponCodeListForUseByCustomerIdResponse implements Serializable {

    private static final long serialVersionUID = 2572855130915278064L;

    public List<CouponCodeVO> getCouponCodeList() {
        return couponCodeList;
    }

    /**
     * 优惠券券码列表
     */
    @ApiModelProperty(value = "优惠券券码列表")
    private List<CouponCodeVO> couponCodeList;


}
