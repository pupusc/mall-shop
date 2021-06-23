package com.wanmi.sbc.goods.api.request.appointmentsalegoods;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

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
public class AppointmentSaleGoodsModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@Max(9223372036854775807L)
	private Long id;

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
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人", hidden = true)
	private String updatePerson;

}