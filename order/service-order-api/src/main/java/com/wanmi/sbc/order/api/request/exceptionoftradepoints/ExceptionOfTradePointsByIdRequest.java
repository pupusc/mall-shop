package com.wanmi.sbc.order.api.request.exceptionoftradepoints;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * <p>单个查询积分订单抵扣异常表请求参数</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionOfTradePointsByIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 异常标识ID
	 */
	@ApiModelProperty(value = "异常标识ID")
	@NotNull
	private String id;

}