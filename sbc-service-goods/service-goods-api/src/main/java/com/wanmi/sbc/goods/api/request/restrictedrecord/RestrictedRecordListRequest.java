package com.wanmi.sbc.goods.api.request.restrictedrecord;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import java.time.LocalDate;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>限售列表查询请求参数</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestrictedRecordListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-记录主键List
	 */
	@ApiModelProperty(value = "批量查询-记录主键List")
	private List<Long> recordIdList;

	/**
	 * 记录主键
	 */
	@ApiModelProperty(value = "记录主键")
	private Long recordId;

	/**
	 * 会员的主键
	 */
	@ApiModelProperty(value = "会员的主键")
	private String customerId;

	/**
	 * 货品主键
	 */
	@ApiModelProperty(value = "货品主键")
	private String goodsInfoId;

	/**
	 * 购买的数量
	 */
	@ApiModelProperty(value = "购买的数量")
	private Long purchaseNum;

	/**
	 * 周期类型（0: 终生，1:周  2:月  3:年）
	 */
	@ApiModelProperty(value = "周期类型（0: 终生，1:周  2:月  3:年）")
	private Integer restrictedCycleType;

	/**
	 * 搜索条件:开始时间开始
	 */
	@ApiModelProperty(value = "搜索条件:开始时间开始")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate startDateBegin;
	/**
	 * 搜索条件:开始时间截止
	 */
	@ApiModelProperty(value = "搜索条件:开始时间截止")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate startDateEnd;

	/**
	 * 搜索条件:结束时间开始
	 */
	@ApiModelProperty(value = "搜索条件:结束时间开始")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate endDateBegin;
	/**
	 * 搜索条件:结束时间截止
	 */
	@ApiModelProperty(value = "搜索条件:结束时间截止")
	@JsonSerialize(using = CustomLocalDateSerializer.class)
	@JsonDeserialize(using = CustomLocalDateDeserializer.class)
	private LocalDate endDateEnd;

}