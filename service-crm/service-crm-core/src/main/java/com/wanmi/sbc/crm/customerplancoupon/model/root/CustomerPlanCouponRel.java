package com.wanmi.sbc.crm.customerplancoupon.model.root;


import lombok.Data;
import javax.persistence.*;

import java.io.Serializable;

/**
 * <p>运营计划与优惠券关联实体类</p>
 * @author dyt
 * @date 2020-01-08 14:11:18
 */
@Data
@Entity
@Table(name = "customer_plan_coupon_rel")
public class CustomerPlanCouponRel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 计划id
	 */
	@Column(name = "plan_id")
	private Long planId;

	/**
	 * 优惠券Id
	 */
	@Column(name = "coupon_id")
	private String couponId;

	/**
	 * 赠送数量
	 */
	@Column(name = "gift_count")
	private Integer giftCount;

}