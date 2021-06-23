package com.wanmi.sbc.customer.api.request.customersignrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>用户签到记录列表查询请求参数</p>
 * @author wangtao
 * @date 2019-10-05 16:13:04
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignRecordListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-用户签到记录表idList
	 */
	@ApiModelProperty(value = "批量查询-用户签到记录表idList")
	private List<String> signRecordIdList;

	/**
	 * 用户签到记录表id
	 */
	@ApiModelProperty(value = "用户签到记录表id")
	private String signRecordId;

	/**
	 * 用户id
	 */
	@ApiModelProperty(value = "用户id")
	private String customerId;

	/**
	 * 签到日期记录
	 */
	@ApiModelProperty(value = "签到日期记录")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime signRecord;

	/**
	 * 搜索条件:签到日期记录开始
	 */
	@ApiModelProperty(value = "搜索条件:签到日期记录开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime signRecordBegin;

	/**
	 * 搜索条件:签到日期记录截止
	 */
	@ApiModelProperty(value = "搜索条件:签到日期记录截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime signRecordEnd;

	/**
	 * 搜索条件：当月签到记录日期
	 */
	@ApiModelProperty(value = "搜索条件:当月签到记录日期")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime signRecordSameMonth;

	/**
	 * 删除区分：0 未删除，1 已删除
	 */
	@ApiModelProperty(value = "删除区分：0 未删除，1 已删除")
	private DeleteFlag delFlag;

}