package com.wanmi.sbc.goods.api.request.virtualcoupon;

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
 * <p>券码修改参数</p>
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponCodeModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 券码ID
	 */
	@ApiModelProperty(value = "券码ID")
	@Max(9223372036854775807L)
	private Long id;

	/**
	 * 电子卡券ID
	 */
	@ApiModelProperty(value = "电子卡券ID")
	@NotNull
	@Max(9223372036854775807L)
	private Long couponId;

	/**
	 * 批次号
	 */
	@ApiModelProperty(value = "批次号")
	@NotBlank
	@Length(max=32)
	private String batchNo;

	/**
	 * 有效期
	 */
	@ApiModelProperty(value = "有效期")
	@NotNull
	@Max(9999999999L)
	private Integer validDays;

	/**
	 * 0:兑换码 1:券码+密钥 2:链接
	 */
	@ApiModelProperty(value = "0:兑换码 1:券码+密钥 2:链接")
	@NotNull
	@Max(127)
	private Integer provideType;

	/**
	 * 兑换码/券码/链接
	 */
	@ApiModelProperty(value = "兑换码/券码/链接")
	@NotBlank
	@Length(max=255)
	private String couponNo;

	/**
	 * 密钥
	 */
	@ApiModelProperty(value = "密钥")
	@Length(max=50)
	private String couponSecret;

	/**
	 * 领取结束时间
	 */
	@ApiModelProperty(value = "领取结束时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime receiveEndTime;

	/**
	 * 兑换开始时间
	 */
	@ApiModelProperty(value = "兑换开始时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime exchangeStartTime;

	/**
	 * 兑换结束时间
	 */
	@ApiModelProperty(value = "兑换结束时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime exchangeEndTime;

	/**
	 * 0:未发放 1:已发放 2:已过期
	 */
	@ApiModelProperty(value = "0:未发放 1:已发放 2:已过期")
	@NotNull
	@Max(127)
	private Integer status;

	/**
	 * 订单号
	 */
	@ApiModelProperty(value = "订单号")
	@Length(max=32)
	private String tid;

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

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

}