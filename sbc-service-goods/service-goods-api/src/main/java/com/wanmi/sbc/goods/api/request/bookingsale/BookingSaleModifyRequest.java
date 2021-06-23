package com.wanmi.sbc.goods.api.request.bookingsale;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.dto.BookingSaleGoodsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>预售信息修改参数</p>
 * @author dany
 * @date 2020-06-05 10:47:21
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	@Max(9223372036854775807L)
	private Long id;

	/**
	 * 活动名称
	 */
	@ApiModelProperty(value = "活动名称")
	@NotBlank
	@Length(max=100)
	private String activityName;

	/**
	 * 商户id
	 */
	@ApiModelProperty(value = "商户id")
	@NotNull
	@Max(9223372036854775807L)
	private Long storeId;

	/**
	 * 预售类型 0：全款预售  1：定金预售
	 */
	@ApiModelProperty(value = "预售类型 0：全款预售  1：定金预售")
	@NotNull
	@Max(127)
	private Integer bookingType;

	/**
	 * 定金支付开始时间
	 */
	@ApiModelProperty(value = "定金支付开始时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime handSelStartTime;

	/**
	 * 定金支付结束时间
	 */
	@ApiModelProperty(value = "定金支付结束时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime handSelEndTime;

	/**
	 * 尾款支付开始时间
	 */
	@ApiModelProperty(value = "尾款支付开始时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime tailStartTime;

	/**
	 * 尾款支付结束时间
	 */
	@ApiModelProperty(value = "尾款支付结束时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime tailEndTime;

	/**
	 * 预售开始时间
	 */
	@ApiModelProperty(value = "预售开始时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime bookingStartTime;

	/**
	 * 预售结束时间
	 */
	@ApiModelProperty(value = "预售结束时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime bookingEndTime;

	/**
	 * 发货日期 2020-01-10
	 */
	@ApiModelProperty(value = "发货日期 2020-01-10")
	@NotBlank
	@Length(max=10)
	private String deliverTime;

	/**
	 * 参加会员  -1:全部客户 0:全部等级 other:其他等级
	 */
	@ApiModelProperty(value = "参加会员  -1:全部客户 0:全部等级 other:其他等级")
	@NotBlank
	@Length(max=500)
	private String joinLevel;

	/**
	 * 是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）
	 */
	@ApiModelProperty(value = "是否平台等级 （1平台（自营店铺属于平台等级） 0店铺）")
	@NotNull
	private DefaultFlag joinLevelType;

	/**
	 * 是否暂停 0:否 1:是
	 */
	@ApiModelProperty(value = "是否暂停 0:否 1:是")
	@Max(127)
	private Integer pauseFlag;

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
	 * 预售活动商品信息
	 */
	@ApiModelProperty(value = "预售活动商品信息")
	private List<BookingSaleGoodsDTO> bookingSaleGoodsList;

}