package com.wanmi.sbc.message.api.request.messagesendnode;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>站内信通知节点表列表查询请求参数</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendNodeListRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键idList
	 */
	@ApiModelProperty(value = "批量查询-主键idList")
	private List<Long> idList;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 节点名称
	 */
	@ApiModelProperty(value = "节点名称")
	private String nodeName;

	/**
	 * 节点标题
	 */
	@ApiModelProperty(value = "节点标题")
	private String nodeTitle;

	/**
	 * 内容
	 */
	@ApiModelProperty(value = "内容")
	private String nodeContent;

	/**
	 * 启用状态：0未启用 1启用
	 */
	@ApiModelProperty(value = "启用状态：0未启用 1启用")
	private Integer status;

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
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createPerson;

	/**
	 * 搜索条件:修改时间开始
	 */
	@ApiModelProperty(value = "搜索条件:修改时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:修改时间截止
	 */
	@ApiModelProperty(value = "搜索条件:修改时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updatePerson;

	/**
	 * 删除标识 0未删除 1删除
	 */
	@ApiModelProperty(value = "删除标识 0未删除 1删除")
	private DeleteFlag delFlag;

	/**
	 * 发送数
	 */
	@ApiModelProperty(value = "发送数")
	private Integer sendSum;

	/**
	 * 打开数
	 */
	@ApiModelProperty(value = "打开数")
	private Integer openSum;

}