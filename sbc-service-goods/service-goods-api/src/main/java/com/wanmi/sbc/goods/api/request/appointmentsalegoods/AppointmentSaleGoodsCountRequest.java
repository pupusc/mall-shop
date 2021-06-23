package com.wanmi.sbc.goods.api.request.appointmentsalegoods;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>预约抢购修改参数</p>
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleGoodsCountRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;


	/**
	 * 预约id
	 */
	@ApiModelProperty(value = "预约id")
	@NotNull
	@Max(9223372036854775807L)
	private Long appointmentSaleId;


	/**
	 * skuID
	 */
	@ApiModelProperty(value = "skuID")
	@NotBlank
	@Length(max=32)
	private String goodsInfoId;

	/**
	 * stock
	 */
	@ApiModelProperty(value = "stock")
	private Long stock;


}