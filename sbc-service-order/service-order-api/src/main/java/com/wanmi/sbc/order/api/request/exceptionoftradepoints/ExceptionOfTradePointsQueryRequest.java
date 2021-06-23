package com.wanmi.sbc.order.api.request.exceptionoftradepoints;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.order.bean.enums.HandleStatus;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>积分订单抵扣异常表通用查询请求参数</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionOfTradePointsQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-异常标识IDList
	 */
	@ApiModelProperty(value = "批量查询-异常标识IDList")
	private List<String> idList;

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
	 * 处理状态,0：待处理，1：处理失败，2：处理成功
	 */
	@ApiModelProperty(value = "处理状态,0：待处理，1：处理失败，2：处理成功")
	private List<HandleStatus> handleStatuses;

	/**
	 * 错误次数
	 */
	@ApiModelProperty(value = "错误次数")
	private Integer errorTime;

	/**
	 * 错误次数集合
	 */
	@ApiModelProperty(value = "错误次数集合")
	private List<Integer> errorTimes;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@ApiModelProperty(value = "是否删除标志 0：否，1：是")
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

}