package com.wanmi.sbc.goods.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: lq
 * @CreateTime:2018-09-12 09:34
 * @Description:优惠券商品作用范围
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponMarketingScopeDTO implements Serializable {

    /**
     * 优惠券id
     */
    private String couponId;

    /**
     * 营销id,可以为0(全部)，brand_id(品牌id)，cate_id(分类id), goods_info_id(货品id)
     */
    private String scopeId;


}
