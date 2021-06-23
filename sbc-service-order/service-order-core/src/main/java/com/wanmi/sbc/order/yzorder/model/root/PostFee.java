package com.wanmi.sbc.order.yzorder.model.root;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 邮费优惠信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostFee {

    /**
     * 优惠活动id
     */
    private Long promotion_id;

    /**
     * 优惠活动名称
     */
    private String promotion_title;

    /**
     * 优惠类型名称
     */
    private String promotion_type_name;

    /**
     * 优惠金额,单位：分
     */
    private Long decrease;
}
