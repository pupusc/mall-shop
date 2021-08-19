package com.wanmi.sbc.setting.api.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * <p>新增参数</p>
 * @author wjj
 * @date 2019-02-22 16:09:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SensitiveWordsAddRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 敏感词内容
	 */
	@NotBlank
	private String sensitiveWords;

	/**
	 * 是否删除
	 */
	private DeleteFlag delFlag;

	/**
	 * 创建人
	 */
	@Length(max=255)
	private String createUser;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTime;

	/**
	 * 修改人
	 */
	@Length(max=255)
	private String updateUser;

	/**
	 * 修改时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTime;

	/**
	 * 删除人
	 */
	@Length(max=255)
	private String deleteUser;

	/**
	 * 删除时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTime;

}