package com.wanmi.sbc.order.api.request.exceptionoftradepoints;

import com.wanmi.sbc.order.api.request.OrderBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除积分订单抵扣异常表请求参数</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionOfTradePointsDelByIdListRequest extends OrderBaseRequest {
private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-异常标识IDList
	 */
	@ApiModelProperty(value = "批量删除-异常标识IDList")
	@NotEmpty
	private List<String> idList;
}
