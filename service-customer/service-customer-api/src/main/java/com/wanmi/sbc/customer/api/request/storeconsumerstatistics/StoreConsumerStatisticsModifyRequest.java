package com.wanmi.sbc.customer.api.request.storeconsumerstatistics;

import java.math.BigDecimal;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.customer.api.request.CustomerBaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.*;

/**
 * <p>店铺客户消费统计表修改参数</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreConsumerStatisticsModifyRequest extends CustomerBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Length(max=32)
	@ApiModelProperty(value = "主键id")
	private String id;

	/**
	 * 用户id
	 */
	@NotBlank
	@Length(max=32)
	@ApiModelProperty(value = "用户id")
	private String customerId;

	/**
	 * 店铺id
	 */
	@NotNull
	@ApiModelProperty(value = "店铺id")
	private Long storeId;

	/**
	 * 会员在该店铺下单数
	 */
	@ApiModelProperty(value = "会员在该店铺下单数")
	private Integer tradeCount;

	/**
	 * 会员在该店铺消费额
	 */
	@ApiModelProperty(value = "会员在该店铺消费额")
	private BigDecimal tradePriceCount;

	/**
	 * 更新时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

}