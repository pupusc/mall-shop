package com.wanmi.sbc.marketing.api.request.distributionrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>DistributionRecord修改参数</p>
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@Data
@ApiModel
public class DistributionRecordUpdateInfo {
	private static final long serialVersionUID = 1L;

	/**
	 * 货品Id(sku)
	 */
	@ApiModelProperty(value = "货品Id")
	@Length(max=32)
	@NotBlank
	private String goodsInfoId;

	/**
	 * 订单交易号
	 */
	@ApiModelProperty(value = "订单交易号")
	@Length(max=32)
	@NotBlank
	private String tradeId;

	/**
	 * 订单完成时间
	 */
	@ApiModelProperty(value = "订单完成时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime finishTime;

	/**
	 * 佣金入账时间
	 */
	@ApiModelProperty(value = "佣金入账时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime missionReceivedTime;

	/**
	 * 商品数量
	 */
	@Max(9999999999L)
	@NotNull
	@ApiModelProperty(value = "商品数量")
	private Long goodsCount;

}