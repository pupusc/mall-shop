package com.wanmi.sbc.marketing.coupon.model.root;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>优惠券活动目标客户作用范围实体类</p>
 * @author lq
 * @date 2019-08-02 14:50:57
 */
@Data
@Entity
@Table(name = "coupon_marketing_customer_scope")
public class CouponMarketingCustomerScope implements Serializable {
	private static final long serialVersionUID = -923494963386744651L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "marketing_customer_scope_id")
	private String marketingCustomerScopeId;

	/**
	 * 优惠券活动id
	 */
	@Column(name = "activity_id")
	private String activityId;

	/**
	 * 会员id
	 */
	@Column(name = "customer_id")
	private String customerId;

}