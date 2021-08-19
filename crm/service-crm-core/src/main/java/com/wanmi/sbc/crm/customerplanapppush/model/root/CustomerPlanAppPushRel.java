package com.wanmi.sbc.crm.customerplanapppush.model.root;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>运营计划App通知实体类</p>
 * @author dyt
 * @date 2020-01-10 11:14:29
 */
@Data
@Entity
@Table(name = "customer_plan_app_push_rel")
public class CustomerPlanAppPushRel implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 标识
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 任务名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 消息标题
	 */
	@Column(name = "notice_title")
	private String noticeTitle;

	/**
	 * 消息内容
	 */
	@Column(name = "notice_context")
	private String noticeContext;

	/**
	 * 封面地址
	 */
	@Column(name = "cover_url")
	private String coverUrl;

	/**
	 * 落页地地址
	 */
	@Column(name = "page_url")
	private String pageUrl;

	/**
	 * 计划id
	 */
	@Column(name = "plan_id")
	private Long planId;

}