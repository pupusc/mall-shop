package com.wanmi.sbc.message.api.request.pushsendnode;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>会员推送通知节点分页查询请求参数</p>
 * @author Bob
 * @date 2020-01-13 10:47:41
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushSendNodePageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-idList
	 */
	@ApiModelProperty(value = "批量查询-idList")
	private List<Long> idList;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 节点名称
	 */
	@ApiModelProperty(value = "节点名称")
	private String nodeName;

	/**
	 * 节点类型
	 */
	@ApiModelProperty(value = "节点类型")
	private Integer nodeType;

	/**
	 * 节点code
	 */
	@ApiModelProperty(value = "节点code")
	private String nodeCode;

	/**
	 * 节点标题
	 */
	@ApiModelProperty(value = "节点标题")
	private String nodeTitle;

	/**
	 * 通知内容
	 */
	@ApiModelProperty(value = "通知内容")
	private String nodeContext;

	@ApiModelProperty(value = "预计发送总数")
	private Long expectedSendCount;

	/**
	 * 实际发送总数
	 */
	@ApiModelProperty(value = "实际发送总数")
	private Long actuallySendCount;

	/**
	 * 打开总数
	 */
	@ApiModelProperty(value = "打开总数")
	private Long openCount;

	/**
	 * 状态 0:未启用 1:启用
	 */
	@ApiModelProperty(value = "状态 0:未启用 1:启用")
	private Integer status;

	/**
	 * 删除标志 0:未删除 1:删除
	 */
	@ApiModelProperty(value = "删除标志 0:未删除 1:删除")
	private DeleteFlag delFlag;

	/**
	 * 创建人ID
	 */
	@ApiModelProperty(value = "创建人ID")
	private String createPerson;

	/**
	 * 搜索条件:创建时间开始
	 */
	@ApiModelProperty(value = "搜索条件:创建时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:创建时间截止
	 */
	@ApiModelProperty(value = "搜索条件:创建时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 更新人ID
	 */
	@ApiModelProperty(value = "更新人ID")
	private String updatePerson;

	/**
	 * 搜索条件:更新时间开始
	 */
	@ApiModelProperty(value = "搜索条件:更新时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:更新时间截止
	 */
	@ApiModelProperty(value = "搜索条件:更新时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

}