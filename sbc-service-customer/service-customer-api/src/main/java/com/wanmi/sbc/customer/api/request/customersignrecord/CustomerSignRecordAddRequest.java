package com.wanmi.sbc.customer.api.request.customersignrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>用户签到记录新增参数</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignRecordAddRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	@NotBlank
	@Length(max=32)
	private String customerId;

	/**
	 * 签到日期记录
	 */
	@ApiModelProperty(value = "签到日期记录")
	@NotNull
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime signRecord;

	/**
	 * 删除区分：0 未删除，1 已删除
	 */
	@ApiModelProperty(value = "删除区分：0 未删除，1 已删除")
	private DeleteFlag delFlag;

	/**
	 * 签到ip
	 */
	@ApiModelProperty(value = "签到ip")
	private String signIp;

	/**
	 * 签到终端：pc,wechat,app,minipro
	 */
	@ApiModelProperty(value = "签到终端：pc,wechat,app,minipro")
	private String signTerminal;
}