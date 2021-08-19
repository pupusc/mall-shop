package com.wanmi.sbc.customer.api.request.paidcard;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import com.wanmi.sbc.customer.bean.enums.EnableEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>批量删除付费会员请求参数</p>
 * @author xuhai
 * @date 2021-01-29 14:03:56
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardEnableRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 付费会员卡id
	 */
	@ApiModelProperty(value = "付费会员卡id")
	@NotBlank
	private String id;

	/**
	 * 付费会员卡启用状态
	 */
	@ApiModelProperty(value = "付费会员卡启用状态 1:启用 0：禁用")
	@NotNull
	private EnableEnum enable;
}