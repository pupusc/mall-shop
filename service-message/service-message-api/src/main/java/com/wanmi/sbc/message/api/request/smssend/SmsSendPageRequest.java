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

/**
 * <p>短信发送分页查询请求参数</p>
 * @author zgl
 * @date 2019-12-03 15:36:05
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;


	@ApiModelProperty(value = "平台商家id")
	private Long smsSettingId;

    /**
     * 模板code
     */
    @ApiModelProperty(value = "模板code")
    private String templateCode;

	/**
	 * 接收类型（0-全部，1-会员等级，2-会员人群，3-自定义）
	 */
	@ApiModelProperty(value = "接收类型（0-全部，1-会员等级，2-会员人群，3-自定义）", hidden = true)
	private ReceiveType receiveType;


	/**
	 * 状态（0-未开始，1-进行中，2-已结束，3-任务失败）
	 */
	@ApiModelProperty(value = "状态（0-未开始，1-进行中，2-已结束，3-任务失败）")
	private SendStatus status;

	/**
	 * 发送类型（0-立即发送，1-定时发送）
	 */
	@ApiModelProperty(value = "发送类型（0-立即发送，1-定时发送）", hidden = true)
	private SendType sendType;

	/**
	 * 搜索条件:发送时间开始
	 */
	@ApiModelProperty(value = "搜索条件:发送时间开始", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime sendTimeBegin;
	/**
	 * 搜索条件:发送时间截止
	 */
	@ApiModelProperty(value = "搜索条件:发送时间截止", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime sendTimeEnd;

	@ApiModelProperty(value = "重发类型（0-不可重发，1-可重发）")
	private ResendType resendType;
}