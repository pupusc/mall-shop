package com.wanmi.sbc.customer.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>店铺退货地址表VO</p>
 * @author dyt
 * @date 2020-11-02 11:38:39
 */
@ApiModel
@Data
public class StoreReturnAddressVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 收货地址ID
	 */
	@ApiModelProperty(value = "收货地址ID")
	private String addressId;

	/**
	 * 公司信息ID
	 */
	@ApiModelProperty(value = "公司信息ID")
	private Long companyInfoId;

	/**
	 * 店铺信息ID
	 */
	@ApiModelProperty(value = "店铺信息ID")
	private Long storeId;

	/**
	 * 收货人
	 */
	@ApiModelProperty(value = "收货人")
	private String consigneeName;

	/**
	 * 收货人手机号码
	 */
	@ApiModelProperty(value = "收货人手机号码")
	private String consigneeNumber;

	/**
	 * 省份
	 */
	@ApiModelProperty(value = "省份")
	private Long provinceId;

	/**
	 * 市
	 */
	@ApiModelProperty(value = "市")
	private Long cityId;

	/**
	 * 区
	 */
	@ApiModelProperty(value = "区")
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
	private String returnAddress;

	/**
	 * 是否是默认地址
	 */
	@ApiModelProperty(value = "是否是默认地址")
	private Boolean isDefaultAddress;

	/**
	 * 删除时间
	 */
	@ApiModelProperty(value = "删除时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTime;

	/**
	 * 删除人
	 */
	@ApiModelProperty(value = "删除人")
	private String deletePerson;

	/**
	 * 省
	 */
	@ApiModelProperty(value = "省")
	private String provinceName;

	/**
	 * 市
	 */
	@ApiModelProperty(value = "市")
	private String cityName;

	/**
	 * 区
	 */
	@ApiModelProperty(value = "区")
	private String areaName;

	/**
	 * 街道
	 */
	@ApiModelProperty(value = "街道")
	private String streetName;

}