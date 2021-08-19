package com.wanmi.sbc.message.api.request.smssend;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.message.bean.enums.ReceiveType;
import com.wanmi.sbc.message.bean.enums.ResendType;
import com.wanmi.sbc.message.bean.enums.SendStatus;
import com.wanmi.sbc.message.bean.enums.SendType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>短信发送通用查询请求参数</p>
 * @author zgl
 * @date 2019-12-03 15:36:05
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendQueryRequest extends BaseQueryRequest {
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
	 * 短信内容
	 */
	@ApiModelProperty(value = "短信内容")
	private String context;

	@ApiModelProperty(value = "平台商家id")
	private Long smsSettingId;
	/**
	 * 模板code
	 */
	@ApiModelProperty(value = "模板Code")
	private String templateCode;

	/**
	 * 签名id
	 */
	@ApiModelProperty(value = "签名id")
	private Long signId;

	/**
	 * 接收人描述
	 */
	@ApiModelProperty(value = "接收人描述")
	private String receiveContext;

	/**
	 * 接收类型（0-全部，1-会员等级，2-会员人群，3-自定义）
	 */
	@ApiModelProperty(value = "接收类型（0-全部，1-会员等级，2-会员人群，3-自定义）")
	private ReceiveType receiveType;

	/**
	 * 接收人明细
	 */
	@ApiModelProperty(value = "接收人明细")
	private String receiveValue;

	/**
	 * 手工添加的号码
	 */
	@ApiModelProperty(value = "手工添加的号码")
	private String manualAdd;

	/**
	 * 状态（0-未开始，1-进行中，2-已结束，3-任务失败）
	 */
	@ApiModelProperty(value = "状态（0-未开始，1-进行中，2-已结束，3-任务失败）")
	private SendStatus status;

    /**
     * 非当前状态（0-未开始，1-进行中，2-已结束，3-任务失败）
     */
    @ApiModelProperty(value = "非当前状态（0-未开始，1-进行中，2-已结束，3-任务失败）")
    private SendStatus noStatus;

	/**
	 * 任务执行信息
	 */
	@ApiModelProperty(value = "任务执行信息")
	private String message;

	/**
	 * 发送类型（0-立即发送，1-定时发送）
	 */
	@ApiModelProperty(value = "发送类型（0-立即发送，1-定时发送）")
	private SendType sendType;

	/**
	 * 搜索条件:发送时间开始
	 */
	@ApiModelProperty(value = "搜索条件:发送时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime sendTimeBegin;
	/**
	 * 搜索条件:发送时间截止
	 */
	@ApiModelProperty(value = "搜索条件:发送时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime sendTimeEnd;

	/**
	 * 预计发送条数
	 */
	@ApiModelProperty(value = "预计发送条数")
	private Integer rowCount;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
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
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
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

	@ApiModelProperty(value = "重发类型（0-不可重发，1-可重发）")
	private ResendType resendType;

}