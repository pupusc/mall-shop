package com.wanmi.sbc.setting.api.request.systemprivacypolicy;

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
 * <p>隐私政策列表查询请求参数</p>
 * @author yangzhen
 * @date 2020-09-23 14:52:35
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemPrivacyPolicyListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-隐私政策idList
	 */
	@ApiModelProperty(value = "批量查询-隐私政策idList")
	private List<String> privacyPolicyIdList;

	/**
	 * 隐私政策id
	 */
	@ApiModelProperty(value = "隐私政策id")
	private String privacyPolicyId;

	/**
	 * 隐私政策
	 */
	@ApiModelProperty(value = "隐私政策")
	private String privacyPolicy;

	/**
	 * 隐私政策弹窗
	 */
	@ApiModelProperty(value = "隐私政策弹窗")
	private String privacyPolicyPop;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createPerson;

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
	 * 修改人
	 */
	@ApiModelProperty(value = "修改人")
	private String updatePerson;

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
	 * 是否删除标志 0：否，1：是
	 */
	@ApiModelProperty(value = "是否删除标志 0：否，1：是")
	private DeleteFlag delFlag;

}