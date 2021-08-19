package com.wanmi.sbc.message.api.request.smssend;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import com.wanmi.sbc.message.bean.enums.ReceiveType;
import com.wanmi.sbc.message.bean.enums.ResendType;
import com.wanmi.sbc.message.bean.enums.SendStatus;
import com.wanmi.sbc.message.bean.enums.SendType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;

/**
 * <p>短信发送修改参数</p>
 * @author zgl
 * @date 2019-12-03 15:36:05
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendModifyRequest extends SmsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@Max(9223372036854775807L)
	private Long id;

	@ApiModelProperty(value = "平台商家id")
	private Long smsSettingId;

	/**
	 * 短信内容
	 */
	@ApiModelProperty(value = "短信内容")
	@Length(max=1024)
	private String context;

	/**
	 * 模板code
	 */
	@ApiModelProperty(value = "模板Code")
	private String templateCode;

	/**
	 * 签名id
	 */
	@ApiModelProperty(value = "签名id")
	@Max(9223372036854775807L)
	private Long signId;

	/**
	 * 接收人描述
	 */
	@ApiModelProperty(value = "接收人描述")
	@Length(max=2000)
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
	 * 任务执行信息
	 */
	@ApiModelProperty(value = "任务执行信息")
	@Length(max=2000)
	private String message;

	/**
	 * 发送类型（0-立即发送，1-定时发送）
	 */
	@ApiModelProperty(value = "发送类型（0-立即发送，1-定时发送）")
	private SendType sendType;

	/**
	 * 发送时间
	 */
	@ApiModelProperty(value = "发送时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime sendTime;

	/**
	 * 预计发送条数
	 */
	@ApiModelProperty(value = "预计发送条数")
	@Max(9999999999L)
	private Integer rowCount;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	@Length(max=32)
	private String createPerson;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	@Length(max=32)
	private String updatePerson;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	@ApiModelProperty(value = "重发类型（0-不可重发，1-可重发）")
	private ResendType resendType;

	/**
	 * 发送明细条数
	 */
	@ApiModelProperty(value = "发送明细条数")
	private Integer sendDetailCount;


}