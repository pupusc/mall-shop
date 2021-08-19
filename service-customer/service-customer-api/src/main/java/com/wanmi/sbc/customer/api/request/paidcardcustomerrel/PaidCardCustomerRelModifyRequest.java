package com.wanmi.sbc.customer.api.request.paidcardcustomerrel;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;

/**
 * <p>付费会员修改参数</p>
 * @author xuhai
 * @date 2021-01-29 14:03:59
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardCustomerRelModifyRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	@Length(max=32)
	private String id;

	/**
	 * 会员id
	 */
	@ApiModelProperty(value = "会员id")
	@NotBlank
	@Length(max=32)
	private String customerId;

	/**
	 * 付费会员类型ID
	 */
	@ApiModelProperty(value = "付费会员类型ID")
	@NotBlank
	@Length(max=32)
	private String paidCardId;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	@NotNull
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime beginTime;

	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	@NotNull
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTime;

	/**
	 * 状态： 0：未删除 1：删除
	 */
	@ApiModelProperty(value = "状态： 0：未删除 1：删除")
	private DeleteFlag delFlag;

	/**
	 * 卡号
	 */
	@ApiModelProperty(value = "卡号")
	@NotBlank
	@Length(max=25)
	private String cardNo;

	/**
	 * 是否发送了过期提醒短信
	 */
	@ApiModelProperty(value = "是否发送了过期提醒短信")
	private Boolean sendMsgFlag;

}