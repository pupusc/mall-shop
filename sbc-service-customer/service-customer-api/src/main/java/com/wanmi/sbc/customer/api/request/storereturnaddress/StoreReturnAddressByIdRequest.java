package com.wanmi.sbc.customer.api.request.storereturnaddress;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * <p>单个查询店铺退货地址表请求参数</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreReturnAddressByIdRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 收货地址ID
	 */
	@ApiModelProperty(value = "收货地址ID")
	@NotNull
	private String addressId;

	/**
	 * 店铺信息ID
	 */
	@ApiModelProperty(value = "店铺信息ID", hidden = true)
	private Long storeId;

	/**
	 * 是否返回省市区名称
	 */
	@ApiModelProperty(value = "是否返回省市区名称")
	private Boolean showAreaName;

}