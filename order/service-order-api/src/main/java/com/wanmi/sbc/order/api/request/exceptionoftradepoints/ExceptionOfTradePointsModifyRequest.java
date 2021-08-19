package com.wanmi.sbc.order.api.request.exceptionoftradepoints;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.order.bean.enums.HandleStatus;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>积分订单抵扣异常表修改参数</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionOfTradePointsModifyRequest extends BaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 异常标识ID
	 */
	@ApiModelProperty(value = "异常标识ID")
	@Length(max=32)
	private String id;

	/**
	 * 订单id
	 */
	@ApiModelProperty(value = "订单id")
	@NotBlank
	@Length(max=32)
	private String tradeId;
	/**
	 * 类型
	 */
	private Integer type = 1;


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
	@Max(127)
	private HandleStatus handleStatus;

	/**
	 * 错误次数
	 */
	@ApiModelProperty(value = "错误次数")
	private Integer errorTime;

	/**
	 * 删除标记  0：正常，1：删除
	 */
	@ApiModelProperty(value = "删除标记")
	public DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间", hidden = true)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

}