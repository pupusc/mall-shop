package com.wanmi.sbc.setting.api.request.push;

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
 * <p>消息推送通用查询请求参数</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppPushConfigQueryRequest extends BaseQueryRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-消息推送配置编号List
	 */
	@ApiModelProperty(value = "批量查询-消息推送配置编号List")
	private List<Long> appPushIdList;

	/**
	 * 消息推送配置编号
	 */
	@ApiModelProperty(value = "消息推送配置编号")
	private Long appPushId;

	/**
	 * 消息推送配置名称
	 */
	@ApiModelProperty(value = "消息推送配置名称")
	private String appPushName;

	/**
	 * 消息推送提供商  0:友盟
	 */
	@ApiModelProperty(value = "消息推送提供商  0:友盟")
	private String appPushManufacturer;

	/**
	 * Android App Key
	 */
	@ApiModelProperty(value = "Android App Key")
	private String androidAppKey;

	/**
	 * Android Umeng Message Secret
	 */
	@ApiModelProperty(value = "Android Umeng Message Secret")
	private String androidUmengMessageSecret;

	/**
	 * Android App Master Secret
	 */
	@ApiModelProperty(value = "Android App Master Secret")
	private String androidAppMasterSecret;

	/**
	 * IOS App Key
	 */
	@ApiModelProperty(value = "IOS App Key")
	private String iosAppKey;

	/**
	 * IOS App Master Secret
	 */
	@ApiModelProperty(value = "IOS App Master Secret")
	private String iosAppMasterSecret;

	/**
	 * 状态,0:未启用1:已启用
	 */
	@ApiModelProperty(value = "状态,0:未启用1:已启用")
	private Integer status;

	/**
	 * 搜索条件:创建日期开始
	 */
	@ApiModelProperty(value = "搜索条件:创建日期开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:创建日期截止
	 */
	@ApiModelProperty(value = "搜索条件:创建日期截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 搜索条件:更新日期开始
	 */
	@ApiModelProperty(value = "搜索条件:更新日期开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:更新日期截止
	 */
	@ApiModelProperty(value = "搜索条件:更新日期截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 搜索条件:删除日期开始
	 */
	@ApiModelProperty(value = "搜索条件:删除日期开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime delTimeBegin;
	/**
	 * 搜索条件:删除日期截止
	 */
	@ApiModelProperty(value = "搜索条件:删除日期截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime delTimeEnd;

	/**
	 * 删除标志
	 */
	@ApiModelProperty(value = "删除标志")
	private DeleteFlag delFlag;

}