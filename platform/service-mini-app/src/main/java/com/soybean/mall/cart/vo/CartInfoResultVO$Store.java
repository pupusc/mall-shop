package com.soybean.mall.cart.vo;

import com.wanmi.sbc.common.enums.BoolFlag;
import lombok.Data;

import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-16 11:15:00
 */
@Data
public class CartInfoResultVO$Store {
    /**
     * 店铺主键
     */
    private Long storeId;
    /**
     * 店铺名称
     */
    private String storeName;
    /**
     * 是否可用优惠券
     */
    private boolean ableCoupon;
}
