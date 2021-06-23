package com.wanmi.sbc.customer.api.request.customersignrecord;

import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>单个删除用户签到记录请求参数</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignRecordDelByIdRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户签到记录表id
	 */
	@ApiModelProperty(value = "用户签到记录表id")
	@NotNull
	private String signRecordId;
}