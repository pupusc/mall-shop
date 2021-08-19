package com.wanmi.sbc.setting.api.request.flashsalesetting;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>秒杀设置列表查询请求参数</p>
 * @author yxz
 * @date 2019-06-11 13:48:53
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlashSaleSettingListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-秒杀设置主键List
	 */
	@ApiModelProperty(value = "批量查询-秒杀设置主键List")
	private List<Long> idList;

	/**
	 * 秒杀设置主键
	 */
	@ApiModelProperty(value = "秒杀设置主键")
	private Long id;

	/**
	 * 每日场次整点时间
	 */
	@ApiModelProperty(value = "每日场次整点时间")
	private String time;

	/**
	 * 是否启用 0：停用，1：启用
	 */
	@ApiModelProperty(value = "是否启用 0：停用，1：启用")
	private EnableStatus status;

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
	 * 搜索条件:修改时间开始
	 */
	@ApiModelProperty(value = "搜索条件:修改时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:修改时间截止
	 */
	@ApiModelProperty(value = "搜索条件:修改时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updatePerson;

	/**
	 * 删除标识,0: 未删除 1: 已删除
	 */
	@ApiModelProperty(value = "删除标识,0: 未删除 1: 已删除")
	private DeleteFlag delFlag;

}