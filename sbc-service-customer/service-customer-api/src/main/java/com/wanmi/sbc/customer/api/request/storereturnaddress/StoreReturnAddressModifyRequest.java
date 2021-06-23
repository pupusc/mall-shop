package com.wanmi.sbc.customer.api.request.storereturnaddress;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.ValidateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>店铺退货地址表修改参数</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreReturnAddressModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 收货地址ID
	 */
	@ApiModelProperty(value = "收货地址ID")
	@NotBlank
	@Length(max=32)
	private String addressId;

	/**
	 * 公司信息ID
	 */
	@ApiModelProperty(value = "公司信息ID", hidden = true)
	private Long companyInfoId;

	/**
	 * 店铺信息ID
	 */
	@ApiModelProperty(value = "店铺信息ID", hidden = true)
	private Long storeId;

	/**
	 * 收货人
	 */
	@ApiModelProperty(value = "收货人")
	@NotBlank
	@Length(min = 2, max=15)
	private String consigneeName;

	/**
	 * 收货人手机号码
	 */
	@ApiModelProperty(value = "收货人手机号码")
	@NotBlank
	@Length(min=11, max=11)
	private String consigneeNumber;

	/**
	 * 省份
	 */
	@ApiModelProperty(value = "省份")
	@NotNull
	private Long provinceId;

	/**
	 * 市
	 */
	@ApiModelProperty(value = "市")
	@NotNull
	private Long cityId;

	/**
	 * 区
	 */
	@ApiModelProperty(value = "区")
	@NotNull
	private Long areaId;

	/**
	 * 街道id
	 */
	@ApiModelProperty(value = "街道id")
	private Long streetId;

	/**
	 * 详细街道地址
	 */
	@ApiModelProperty(value = "详细街道地址")
	@NotBlank
	@Length(max=60)
	private String returnAddress;

	/**
	 * 是否是默认地址
	 */
	@ApiModelProperty(value = "是否是默认地址")
	private Boolean isDefaultAddress;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人", hidden = true)
	private String updatePerson;

	@Override
	public void checkParam() {
		if(StringUtils.isNotEmpty(consigneeNumber)){
			if(!ValidateUtil.isPhone(consigneeNumber)){
				throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
			}
		}
	}
}