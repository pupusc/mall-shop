package com.wanmi.sbc.customer.api.request.fdpaidcast;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>樊登付费类型 映射商城付费类型新增参数</p>
 * @author tzx
 * @date 2021-01-29 14:13:37
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FdPaidCastAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

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
	 * 删除时间
	 */
	@ApiModelProperty(value = "删除时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTime;

	/**
	 * 删除标记  0：正常，1：删除
	 */
	@ApiModelProperty(value = "删除标记  0：正常，1：删除", hidden = true)
	private DeleteFlag delFlag;

}