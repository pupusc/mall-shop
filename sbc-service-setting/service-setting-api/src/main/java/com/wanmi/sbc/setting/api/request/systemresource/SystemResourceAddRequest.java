package com.wanmi.sbc.setting.api.request.systemresource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import com.wanmi.sbc.common.enums.ResourceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>平台素材资源新增参数</p>
 * @author lq
 * @date 2019-11-05 16:14:27
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemResourceAddRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 资源类型(0:图片,1:视频)
	 */
	@ApiModelProperty(value = "资源类型(0:图片,1:视频)")
	@NotNull
	private ResourceType resourceType;

	/**
	 * 素材分类ID
	 */
	@ApiModelProperty(value = "素材分类ID")
	@NotNull
	@Max(9223372036854775807L)
	private Long cateId;

	/**
	 * 素材KEY
	 */
	@ApiModelProperty(value = "素材KEY")
	@Length(max=255)
	private String resourceKey;

	/**
	 * 素材名称
	 */
	@ApiModelProperty(value = "素材名称")
	@Length(max=45)
	private String resourceName;

	/**
	 * 素材地址
	 */
	@ApiModelProperty(value = "素材地址")
	@Length(max=255)
	private String artworkUrl;

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
	 * 删除标识,0:未删除1:已删除
	 */
	@ApiModelProperty(value = "删除标识,0:未删除1:已删除")
	private DeleteFlag delFlag;

	/**
	 * oss服务器类型，对应system_config的config_type
	 */
	@ApiModelProperty(value = "oss服务器类型，对应system_config的config_type")
	@Length(max=255)
	private String serverType;

}