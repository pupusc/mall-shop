package com.wanmi.sbc.message.api.request.pushdetail;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.message.bean.enums.PushPlatform;
import com.wanmi.sbc.message.bean.enums.PushStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;

/**
 * <p>推送详情新增参数</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushDetailAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 节点ID
	 */
	@ApiModelProperty(value = "节点ID")
	private Long nodeId;

	@ApiModelProperty(value = "taskId对应的平台 0：iOS 1：android")
	private PushPlatform platform;

	/**
	 * 实际发送
	 */
	@ApiModelProperty(value = "实际发送")
	@Max(9999999999L)
	private Integer sendSum;

	/**
	 * 打开数
	 */
	@ApiModelProperty(value = "打开数")
	@Max(9999999999L)
	private Integer openSum;

	/**
	 * 发送状态 0-排队中, 1-发送中，2-发送完成，3-发送失败，4-消息被撤销
	 */
	@ApiModelProperty(value = "发送状态 0-排队中, 1-发送中，2-发送完成，3-发送失败，4-消息被撤销")
	private PushStatus sendStatus;

	/**
	 * 失败信息
	 */
	@ApiModelProperty(value = "失败信息")
	@Length(max=128)
	private String failRemark;

	/**
	 * 创建人ID
	 */
	@ApiModelProperty(value = "创建人ID", hidden = true)
	private String createPerson;

	/**
	 * 更新人ID
	 */
	@ApiModelProperty(value = "更新人ID", hidden = true)
	private String updatePerson;

}