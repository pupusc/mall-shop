package com.wanmi.sbc.setting.api.request;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import java.time.LocalDateTime;
import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import org.hibernate.validator.constraints.*;

/**
 * <p>修改参数</p>
 * @author wjj
 * @date 2019-02-22 16:09:48
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SensitiveWordsModifyRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 敏感词id 主键
	 */
	@Max(9223372036854775807L)
	private Long sensitiveId;

	/**
	 * 敏感词内容
	 */
	@Length(max=255)
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