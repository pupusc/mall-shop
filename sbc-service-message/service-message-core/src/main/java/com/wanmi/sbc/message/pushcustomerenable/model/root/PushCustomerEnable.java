package com.wanmi.sbc.message.pushcustomerenable.model.root;


import com.wanmi.sbc.common.base.BaseEntity;
import com.wanmi.sbc.common.enums.DeleteFlag;
import lombok.Data;

import javax.persistence.*;

/**
 * <p>会员推送通知开关实体类</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@Data
@Entity
@Table(name = "push_customer_enable")
public class PushCustomerEnable extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 会员ID
	 */
	@Id
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * 开启状态 0:未开启 1:启用
	 */
	@Column(name = "enable_status")
	private Integer enableStatus;

	/**
	 * 账号安全通知 0:未启用 1:启用
	 */
	@Column(name = "account_security")
	private Integer accountSecurity;

	/**
	 * 账户资产通知 0:未启用 1:启用
	 */
	@Column(name = "account_assets")
	private Integer accountAssets;

	/**
	 * 订单进度通知 0:未启用 1:启用
	 */
	@Column(name = "order_progress_rate")
	private Integer orderProgressRate;

	/**
	 * 退单进度通知 0:未启用 1:启用
	 */
	@Column(name = "return_order_progress_rate")
	private Integer returnOrderProgressRate;

	/**
	 * 分销业务通知 0:未启用 1:启用
	 */
	@Column(name = "distribution")
	private Integer distribution;

	/**
	 * 删除标志 0:未删除 1:删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

}