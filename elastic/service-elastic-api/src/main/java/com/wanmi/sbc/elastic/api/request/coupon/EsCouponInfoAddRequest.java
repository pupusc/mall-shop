package com.wanmi.sbc.elastic.api.request.coupon;


import com.wanmi.sbc.elastic.bean.dto.coupon.EsCouponInfoDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 新增优惠券
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EsCouponInfoAddRequest implements Serializable {

    private static final long serialVersionUID = -9162622109556746710L;

    private EsCouponInfoDTO couponInfoDTO;
}
