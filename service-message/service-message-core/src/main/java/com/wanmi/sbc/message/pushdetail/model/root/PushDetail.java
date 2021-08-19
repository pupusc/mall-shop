package com.wanmi.sbc.message.pushdetail.model.root;


import com.wanmi.sbc.common.base.BaseEntity;
import com.wanmi.sbc.message.bean.enums.PushPlatform;
import com.wanmi.sbc.message.bean.enums.PushStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * <p>推送详情实体类</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
@Data
@Entity
@Table(name = "push_detail")
public class PushDetail extends BaseEntity {
	private static final long serialVersionUID = 1L;

	/**
	 * 友盟推送任务ID
	 */
	@Id
	@Column(name = "task_id")
	private String taskId;

	@ApiModelProperty(value = "taskId对应的平台 0：iOS 1：android")
	private PushPlatform platform;

	/**
	 * 节点ID
	 */
	@Column(name = "node_id")
	private Long nodeId;

	/**
	 * 实际发送
	 */
	@Column(name = "send_sum")
	private Integer sendSum;

	/**
	 * 打开数
	 */
	@Column(name = "open_sum")
	private Integer openSum;

	@Column(name = "send_status")
	@Enumerated
	private PushStatus sendStatus;

	/**
	 * 失败信息
	 */
	@Column(name = "fail_remark")
	private String failRemark;

	/**
	 * 运营计划ID
	 */
	@Column(name = "plan_id")
	private Long planId;

}