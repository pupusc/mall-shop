package com.wanmi.sbc.customer.api.request.storereturnaddress;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>店铺退货地址表设为默认参数</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreReturnAddressDefaultRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 收货地址ID
	 */
	@ApiModelProperty(value = "收货地址ID")
	@NotBlank
	@Length(max=32)
	private String addressId;

	/**
	 * 店铺信息ID
	 */
	@ApiModelProperty(value = "店铺信息ID", hidden = true)
	private Long storeId;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人", hidden = true)
	private String updatePerson;
}