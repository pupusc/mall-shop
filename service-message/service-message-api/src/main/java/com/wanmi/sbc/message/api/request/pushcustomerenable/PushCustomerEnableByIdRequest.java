package com.wanmi.sbc.message.api.request.pushcustomerenable;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;


/**
 * <p>单个查询会员推送通知开关请求参数</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushCustomerEnableByIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 会员ID
	 */
	@ApiModelProperty(value = "会员ID")
	@NotNull
	private String customerId;

}