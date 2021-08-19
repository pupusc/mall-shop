package com.wanmi.sbc.goods.api.request.virtualcoupon;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>券码分页查询请求参数</p>
 * @author 梁善
 * @date 2021-01-25 16:14:42
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VirtualCouponCodePageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-券码IDList
	 */
	@ApiModelProperty(value = "批量查询-券码IDList")
	private List<Long> idList;

	/**
	 * 券码ID
	 */
	@ApiModelProperty(value = "券码ID")
	private Long id;

	/**
	 * 电子卡券ID
	 */
	@ApiModelProperty(value = "电子卡券ID")
	private Long couponId;

	/**
	 * 批次号
	 */
	@ApiModelProperty(value = "批次号")
	private String batchNo;

	/**
	 * 有效期
	 */
	@ApiModelProperty(value = "有效期")
	private Integer validDays;

	/**
	 * 0:兑换码 1:券码+密钥 2:链接
	 */
	@ApiModelProperty(value = "0:兑换码 1:券码+密钥 2:链接")
	private Integer provideType;

	/**
	 * 兑换码/券码/链接
	 */
	@ApiModelProperty(value = "兑换码/券码/链接")
	private String couponNo;

	/**
	 * 密钥
	 */
	@ApiModelProperty(value = "密钥")
	private String couponSecret;

	/**
	 * 搜索条件:领取结束时间开始
	 */
	@ApiModelProperty(value = "搜索条件:领取结束时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime receiveEndTimeBegin;
	/**
	 * 搜索条件:领取结束时间截止
	 */
	@ApiModelProperty(value = "搜索条件:领取结束时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime receiveEndTimeEnd;

	/**
	 * 搜索条件:兑换开始时间开始
	 */
	@ApiModelProperty(value = "搜索条件:兑换开始时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime exchangeStartTimeBegin;
	/**
	 * 搜索条件:兑换开始时间截止
	 */
	@ApiModelProperty(value = "搜索条件:兑换开始时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime exchangeStartTimeEnd;

	/**
	 * 搜索条件:兑换结束时间开始
	 */
	@ApiModelProperty(value = "搜索条件:兑换结束时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime exchangeEndTimeBegin;
	/**
	 * 搜索条件:兑换结束时间截止
	 */
	@ApiModelProperty(value = "搜索条件:兑换结束时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime exchangeEndTimeEnd;

	/**
	 * 0:未发放 1:已发放 2:已过期
	 */
	@ApiModelProperty(value = "0:未发放 1:已发放 2:已过期")
	private Integer status;

	/**
	 * 订单号
	 */
	@ApiModelProperty(value = "订单号")
	private String tid;

	/**
	 * 删除标识;0:未删除1:已删除
	 */
	@ApiModelProperty(value = "删除标识;0:未删除1:已删除")
	private DeleteFlag delFlag;

	/**
	 * 搜索条件:创建时间开始
	 */
	@ApiModelProperty(value = "搜索条件:创建时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:创建时间截止
	 */
	@ApiModelProperty(value = "搜索条件:创建时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createPerson;

	/**
	 * 搜索条件:更新时间开始
	 */
	@ApiModelProperty(value = "搜索条件:更新时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:更新时间截止
	 */
	@ApiModelProperty(value = "搜索条件:更新时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private String updatePerson;

	/**
	 * 店铺标识
	 */
	@ApiModelProperty(value = "店铺标识")
	private Long storeId;

}