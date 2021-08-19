package com.wanmi.sbc.elastic.sensitivewords.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.common.util.EsConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Enumerated;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author houshuai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = EsConstants.SENSITIVE_WORDS, type = EsConstants.SENSITIVE_WORDS)
public class EsSensitiveWords implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 敏感词id 主键
	 */
	@Id
	private Long sensitiveId;

	/**
	 * 敏感词内容
	 */
	@Field(type = FieldType.Keyword)
	private String sensitiveWords;

	/**
	 * 是否删除
	 */
	@Field(type = FieldType.Keyword)
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 创建人
	 */
	@Field(type = FieldType.Keyword)
	private String createUser;

	/**
	 * 创建时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Keyword)
	private LocalDateTime createTime;

	/**
	 * 修改人
	 */
	@Field(type = FieldType.Keyword)
	private String updateUser;

	/**
	 * 修改时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Keyword)
	private LocalDateTime updateTime;

	/**
	 * 删除人
	 */
	@Field(type = FieldType.Keyword)
	private String deleteUser;

	/**
	 * 删除时间
	 */
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Field(format = DateFormat.custom, pattern = "yyyy-MM-dd HH:mm:ss.SSS", type = FieldType.Keyword)
	private LocalDateTime deleteTime;

}