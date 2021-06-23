package com.wanmi.sbc.customer.api.request.paidcardrightsrel;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotBlank;

/**
 * <p>付费会员新增参数</p>
 * @author xuhai
 * @date 2021-01-29 14:04:00
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardRightsRelAddRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 所属会员权益id
	 */
	@ApiModelProperty(value = "所属会员权益id")
	@NotBlank
	@Length(max=32)
	private String paidCardId;

	/**
	 * 权益id
	 */
	@ApiModelProperty(value = "权益id")
	@NotNull
	@Max(9999999999L)
	private Integer rightsId;

}