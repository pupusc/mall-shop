package com.wanmi.sbc.goods.api.request.bookingsale;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;


/**
 * <p>单个查询预售信息请求参数</p>
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleByIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@NotNull
	private Long id;

	/**
	 * 商户id
	 */
	@ApiModelProperty(value = "商户id", hidden = true)
	private Long storeId;

}