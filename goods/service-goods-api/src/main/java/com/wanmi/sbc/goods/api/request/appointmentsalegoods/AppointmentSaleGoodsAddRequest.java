package com.wanmi.sbc.goods.api.request.appointmentsalegoods;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>预约抢购新增参数</p>
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleGoodsAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 预约id
	 */
	@ApiModelProperty(value = "预约id")
	@NotNull
	@Max(9223372036854775807L)
	private Long appointmentSaleId;

	/**
	 * 商户id
	 */
	@ApiModelProperty(value = "商户id")
	@NotNull
	@Max(9223372036854775807L)
	private Long storeId;

	/**
	 * skuID
	 */
	@ApiModelProperty(value = "skuID")
	@NotBlank
	@Length(max=32)
	private String goodsInfoId;

	/**
	 * spuID
	 */
	@ApiModelProperty(value = "spuID")
	@NotBlank
	@Length(max=32)
	private String goodsId;

	/**
	 * 预约价
	 */
	@ApiModelProperty(value = "预约价")
	private BigDecimal price;

	/**
	 * 预约数量
	 */
	@ApiModelProperty(value = "预约数量")
	@NotNull
	@Max(9999999999L)
	private Integer appointmentCount;

	/**
	 * 购买数量
	 */
	@ApiModelProperty(value = "购买数量")
	@NotNull
	@Max(9999999999L)
	private Integer buyerCount;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人", hidden = true)
	private String createPerson;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人", hidden = true)
	private String updatePerson;

}