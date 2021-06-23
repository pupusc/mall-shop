package com.wanmi.sbc.message.api.request.pushsendnode;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;

/**
 * <p>会员推送通知节点修改参数</p>
 * @author Bob
 * @date 2020-01-13 10:47:41
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushSendNodeModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id", required = true)
	@Max(9223372036854775807L)
	private Long id;

	/**
	 * 节点名称
	 */
	@ApiModelProperty(value = "节点名称", required = true)
	@Length(max=32)
	private String nodeName;

	/**
	 * 节点类型
	 */
	@ApiModelProperty(value = "节点类型", hidden = true)
	@Max(127)
	private Integer nodeType;

	/**
	 * 节点code
	 */
	@ApiModelProperty(value = "节点code")
	private String nodeCode;

	/**
	 * 节点标题
	 */
	@ApiModelProperty(value = "节点标题", required = true)
	@Length(max=32)
	private String nodeTitle;

	/**
	 * 通知内容
	 */
	@ApiModelProperty(value = "通知内容", required = true)
	private String nodeContext;

	@ApiModelProperty(value = "预计发送总数")
	private Long expectedSendCount;

	/**
	 * 实际发送总数
	 */
	@ApiModelProperty(value = "实际发送总数", hidden = true)
	private Long actuallySendCount;

	/**
	 * 打开总数
	 */
	@ApiModelProperty(value = "打开总数", hidden = true)
	private Long openCount;

	/**
	 * 状态 0:未启用 1:启用
	 */
	@ApiModelProperty(value = "状态 0:未启用 1:启用", required = true)
	@Max(127)
	private Integer status;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新人ID
	 */
	@ApiModelProperty(value = "更新人ID", hidden = true)
	private String updatePerson;

}