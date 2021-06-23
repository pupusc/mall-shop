package com.wanmi.sbc.setting.api.request.qqloginset;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>qq登录信息分页查询请求参数</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QqLoginSetPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-qqSetIdList
	 */
	@ApiModelProperty(value = "批量查询-qqSetIdList")
	private List<String> qqSetIdList;

	/**
	 * qqSetId
	 */
	@ApiModelProperty(value = "qqSetId")
	private String qqSetId;

	/**
	 * mobileServerStatus
	 */
	@ApiModelProperty(value = "mobileServerStatus")
	private Integer mobileServerStatus;

	/**
	 * mobileAppId
	 */
	@ApiModelProperty(value = "mobileAppId")
	private String mobileAppId;

	/**
	 * mobileAppSecret
	 */
	@ApiModelProperty(value = "mobileAppSecret")
	private String mobileAppSecret;

	/**
	 * pcServerStatus
	 */
	@ApiModelProperty(value = "pcServerStatus")
	private Integer pcServerStatus;

	/**
	 * pcAppId
	 */
	@ApiModelProperty(value = "pcAppId")
	private String pcAppId;

	/**
	 * pcAppSecret
	 */
	@ApiModelProperty(value = "pcAppSecret")
	private String pcAppSecret;

	/**
	 * appServerStatus
	 */
	@ApiModelProperty(value = "appServerStatus")
	private Integer appServerStatus;

	/**
	 * 搜索条件:createTime开始
	 */
	@ApiModelProperty(value = "搜索条件:createTime开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:createTime截止
	 */
	@ApiModelProperty(value = "搜索条件:createTime截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 搜索条件:updateTime开始
	 */
	@ApiModelProperty(value = "搜索条件:updateTime开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:updateTime截止
	 */
	@ApiModelProperty(value = "搜索条件:updateTime截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * operatePerson
	 */
	@ApiModelProperty(value = "operatePerson")
	private String operatePerson;

}