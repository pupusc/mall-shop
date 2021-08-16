package com.wanmi.sbc.order.api.request.exceptionoftradepoints;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.enums.HandleStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>积分订单抵扣异常表新增参数</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionOfTradePointsAddRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String id;
	/**
	 * 类型
	 */
	private Integer type = 1;
	/**
	 * 订单id
	 */
	@ApiModelProperty(value = "订单id")
	@NotBlank
	@Length(max=32)
	private String tradeId;

	/**
	 * 使用积分
	 */
	@ApiModelProperty(value = "使用积分")
	@NotNull
	@Max(9223372036854775807L)
	private Long points;

	/**
	 * 异常码
	 */
	@ApiModelProperty(value = "异常码")
	@NotBlank
	@Length(max=32)
	private String errorCode;

	/**
	 * 异常描述
	 */
	@ApiModelProperty(value = "异常描述")
	@Length(max=225)
	private String errorDesc;

	/**
	 * 樊登积分抵扣码
	 */
	@ApiModelProperty(value = "樊登积分抵扣码")
	@Length(max=32)
	private String deductCode;

	/**
	 * 处理状态,0：待处理，1：处理失败，2：处理成功
	 */
	@ApiModelProperty(value = "处理状态,0：待处理，1：处理失败，2：处理成功")
	private HandleStatus handleStatus;

	/**
	 * 错误次数
	 */
	@ApiModelProperty(value = "错误次数")
	private Integer errorTime;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@ApiModelProperty(value = "是否删除标志 0：否，1：是", hidden = true)
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;
}