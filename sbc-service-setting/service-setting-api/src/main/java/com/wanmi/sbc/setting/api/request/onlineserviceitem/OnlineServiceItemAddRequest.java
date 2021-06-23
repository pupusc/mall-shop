package com.wanmi.sbc.setting.api.request.onlineserviceitem;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.*;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>onlineerviceItem新增参数</p>
 * @author lq
 * @date 2019-11-05 16:10:54
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceItemAddRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 店铺ID
	 */
	@ApiModelProperty(value = "店铺ID")
	@Max(9223372036854775807L)
	private Long storeId;

	/**
	 * 在线客服主键
	 */
	@ApiModelProperty(value = "在线客服主键")
	@NotNull
	@Max(9999999999L)
	private Integer onlineServiceId;

	/**
	 * 客服昵称
	 */
	@ApiModelProperty(value = "客服昵称")
	@NotBlank
	@Length(max=10)
	private String customerServiceName;

	/**
	 * 客服账号
	 */
	@ApiModelProperty(value = "客服账号")
	@NotBlank
	@Length(max=20)
	private String customerServiceAccount;

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