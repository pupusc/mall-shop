package com.wanmi.sbc.order.api.request.yzsalesmancustomer;

import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除有赞销售员客户关系请求参数</p>
 * @author he
 * @date 2021-03-02 10:10:55
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YzSalesmanCustomerDelByIdRequest  {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@NotNull
	private Long id;
}