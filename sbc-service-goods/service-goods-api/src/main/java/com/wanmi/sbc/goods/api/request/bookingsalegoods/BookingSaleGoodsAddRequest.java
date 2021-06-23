package com.wanmi.sbc.goods.api.request.bookingsalegoods;

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
 * <p>预售商品信息新增参数</p>
 * @author dany
 * @date 2020-06-05 10:51:35
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleGoodsAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 预售id
	 */
	@ApiModelProperty(value = "预售id")
	@NotNull
	@Max(9223372036854775807L)
	private Long bookingSaleId;

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
	 * 定金
	 */
	@ApiModelProperty(value = "定金")
	private BigDecimal handSelPrice;

	/**
	 * 膨胀价格
	 */
	@ApiModelProperty(value = "膨胀价格")
	private BigDecimal inflationPrice;

	/**
	 * 预售价
	 */
	@ApiModelProperty(value = "预售价")
	private BigDecimal bookingPrice;

	/**
	 * 预售数量
	 */
	@ApiModelProperty(value = "预售数量")
	@Max(9999999999L)
	private Integer bookingCount;

	/**
	 * 定金支付数量
	 */
	@ApiModelProperty(value = "定金支付数量")
	@NotNull
	@Max(9999999999L)
	private Integer handSelCount;

	/**
	 * 尾款支付数量
	 */
	@ApiModelProperty(value = "尾款支付数量")
	@NotNull
	@Max(9999999999L)
	private Integer tailCount;

	/**
	 * 全款支付数量
	 */
	@ApiModelProperty(value = "全款支付数量")
	@NotNull
	@Max(9999999999L)
	private Integer payCount;

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