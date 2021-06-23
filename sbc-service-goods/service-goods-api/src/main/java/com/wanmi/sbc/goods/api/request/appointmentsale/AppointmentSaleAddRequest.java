package com.wanmi.sbc.goods.api.request.appointmentsale;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.dto.AppointmentSaleGoodsDTO;
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
 * <p>预约抢购新增参数</p>
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 活动名称
	 */
	@ApiModelProperty(value = "活动名称")
	@NotBlank
	@Length(max=128)
	private String activityName;

	/**
	 * 商户id
	 */
	@ApiModelProperty(value = "商户id")
	@NotNull
	@Max(9223372036854775807L)
	private Long storeId;

	/**
	 * 预约类型 0：不预约不可购买  1：不预约可购买
	 */
	@ApiModelProperty(value = "预约类型 0：不预约不可购买  1：不预约可购买")
	@NotNull
	@Max(127)
	private Integer appointmentType;

	/**
	 * 预约开始时间
	 */
	@ApiModelProperty(value = "预约开始时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime appointmentStartTime;

	/**
	 * 预约结束时间
	 */
	@ApiModelProperty(value = "预约结束时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime appointmentEndTime;

	/**
	 * 抢购开始时间
	 */
	@ApiModelProperty(value = "抢购开始时间")
	@NotNull
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime snapUpStartTime;

	/**
	 * 抢购结束时间
	 */
	@ApiModelProperty(value = "抢购结束时间")
	@NotNull
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime snapUpEndTime;

	/**
	 * 发货日期 2020-01-10
	 */
	@ApiModelProperty(value = "发货日期 2020-01-10")
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
	private DefaultFlag joinLevelType;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@ApiModelProperty(value = "是否删除标志 0：否，1：是", hidden = true)
	private DeleteFlag delFlag;

	/**
	 * 是否暂停 0:否 1:是
	 */
	@ApiModelProperty(value = "是否暂停 0:否 1:是")
	@Max(127)
	private Integer pauseFlag;

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


	@ApiModelProperty(value = "预约商品列表")
	private List<AppointmentSaleGoodsDTO> appointmentSaleGoods;

}