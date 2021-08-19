package com.wanmi.sbc.customer.api.request.fdpaidcast;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>樊登付费类型 映射商城付费类型修改参数</p>
 * @author tzx
 * @date 2021-01-29 14:13:37
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FdPaidCastModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 樊登付费类型 映射商城付费类型主键
	 */
	@ApiModelProperty(value = "樊登付费类型 映射商城付费类型主键")
	@Max(9223372036854775807L)
	private Long id;

	/**
	 * 樊登付费会员类型
	 */
	@ApiModelProperty(value = "樊登付费会员类型")
	@NotNull
	private Integer fdPayType;

	/**
	 * 商城付费会员类型id
	 */
	@ApiModelProperty(value = "商城付费会员类型id")
	@NotBlank
	@Length(max=32)
	private String paidCardId;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 删除时间
	 */
	@ApiModelProperty(value = "删除时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTime;

}