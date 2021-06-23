package com.wanmi.sbc.elastic.bean.vo.sensitivewords;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>VO</p>
 * @author wjj
 * @date 2019-02-22 16:09:48
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EsSensitiveWordsVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 敏感词id 主键
	 */
	private Long sensitiveId;

	/**
	 * 敏感词内容
	 */
	private String sensitiveWords;

	/**
	 * 是否删除
	 */
	private DeleteFlag delFlag;

	/**
	 * 创建人
	 */
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
	private String deleteUser;

	/**
	 * 删除时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime deleteTime;

}