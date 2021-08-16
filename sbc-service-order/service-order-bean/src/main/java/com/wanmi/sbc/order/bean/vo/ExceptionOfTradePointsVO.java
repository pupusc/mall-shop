package com.wanmi.sbc.order.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.order.bean.enums.HandleStatus;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>积分订单抵扣异常表VO</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@ApiModel
@Data
public class ExceptionOfTradePointsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 异常标识ID
	 */
	@ApiModelProperty(value = "异常标识ID")
	private String id;

	/**
	 * 订单id
	 */
	@ApiModelProperty(value = "订单id")
	private String tradeId;

	/**
	 * 使用积分
	 */
	@ApiModelProperty(value = "使用积分")
	private Long points;
	/**
	 * 类型
	 */
	private Integer type;

	/**
	 * 异常码
	 */
	@ApiModelProperty(value = "异常码")
	private String errorCode;

	/**
	 * 异常描述
	 */
	@ApiModelProperty(value = "异常描述")
	private String errorDesc;

	/**
	 * 樊登积分抵扣码
	 */
	@ApiModelProperty(value = "樊登积分抵扣码")
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
	 * 删除标记  0：正常，1：删除
	 */
	@ApiModelProperty(value = "删除标记")
	public DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;
}