package com.wanmi.sbc.setting.api.request.onlineservice;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import java.time.LocalDateTime;

/**
 * <p>onlineService新增参数</p>
 * @author lq
 * @date 2019-11-05 16:10:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceAddRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 店铺ID
	 */
	@ApiModelProperty(value = "店铺ID")
	@Max(9223372036854775807L)
	private Long storeId;

	/**
	 * 在线客服是否启用 0 不启用， 1 启用
	 */
	@ApiModelProperty(value = "在线客服是否启用 0 不启用， 1 启用")
	private Integer serverStatus;

	/**
	 * 客服标题
	 */
	@ApiModelProperty(value = "客服标题")
	@Length(max=10)
	private String serviceTitle;

	/**
	 * 生效终端pc 0 不生效 1 生效
	 */
	@ApiModelProperty(value = "生效终端pc 0 不生效 1 生效")
	private Integer effectivePc;

	/**
	 * 生效终端App 0 不生效 1 生效
	 */
	@ApiModelProperty(value = "生效终端App 0 不生效 1 生效")
	private Integer effectiveApp;

	/**
	 * 生效终端移动版 0 不生效 1 生效
	 */
	@ApiModelProperty(value = "生效终端移动版 0 不生效 1 生效")
	private Integer effectiveMobile;

	/**
	 * 删除标志 默认0：未删除 1：删除
	 */
	@ApiModelProperty(value = "删除标志 默认0：未删除 1：删除")
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 操作人
	 */
	@ApiModelProperty(value = "操作人")
	@Length(max=45)
	private String operatePerson;

}