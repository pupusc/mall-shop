package com.wanmi.sbc.crm.api.request.customerplanconversion;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.crm.api.request.CrmBaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>运营计划转化效果修改参数</p>
 * @author zhangwenchang
 * @date 2020-02-12 00:16:50
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPlanConversionModifyRequest extends CrmBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "主键ID")
	@Max(9223372036854775807L)
	private Long id;

	/**
	 * 运营计划id
	 */
	@ApiModelProperty(value = "运营计划id")
	@Max(9223372036854775807L)
	private Long planId;

	/**
	 * 访客数UV
	 */
	@ApiModelProperty(value = "访客数UV")
	@Max(9223372036854775807L)
	private Long visitorsUvCount;

	/**
	 * 下单人数
	 */
	@ApiModelProperty(value = "下单人数")
	@Max(9223372036854775807L)
	private Long orderPersonCount;

	/**
	 * 下单笔数
	 */
	@ApiModelProperty(value = "下单笔数")
	@Max(9223372036854775807L)
	private Long orderCount;

	/**
	 * 付款人数
	 */
	@ApiModelProperty(value = "付款人数")
	@Max(9223372036854775807L)
	private Long payPersonCount;

	/**
	 * 付款笔数
	 */
	@ApiModelProperty(value = "付款笔数")
	@Max(9223372036854775807L)
	private Long payCount;

	/**
	 * 付款金额
	 */
	@ApiModelProperty(value = "付款金额")
	private BigDecimal totalPrice;

	/**
	 * 客单价
	 */
	@ApiModelProperty(value = "客单价")
	private BigDecimal unitPrice;

	/**
	 * 覆盖人数
	 */
	@ApiModelProperty(value = "覆盖人数")
	@Max(9223372036854775807L)
	private Long coversCount;

	/**
	 * 访客人数
	 */
	@ApiModelProperty(value = "访客人数")
	@Max(9223372036854775807L)
	private Long visitorsCount;

	/**
	 * 访客人数/覆盖人数转换率
	 */
	@ApiModelProperty(value = "访客人数/覆盖人数转换率")
	private BigDecimal coversVisitorsRate;

	/**
	 * 付款人数/访客人数转换率
	 */
	@ApiModelProperty(value = "付款人数/访客人数转换率")
	private BigDecimal payVisitorsRate;

	/**
	 * 付款人数/覆盖人数转换率
	 */
	@ApiModelProperty(value = "付款人数/覆盖人数转换率")
	private BigDecimal payCoversRate;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

}