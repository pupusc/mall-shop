package com.wanmi.sbc.crm.preferencetagdetail.model.root;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.Data;
import javax.persistence.*;
import com.wanmi.sbc.common.base.BaseEntity;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>偏好标签明细实体类</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
@Data
@Entity
@Table(name = "preference_tag_detail")
public class PreferenceTagDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 标签id
	 */
	@Column(name = "tag_id")
	private Long tagId;

	/**
	 * 偏好类标签名称
	 */
	@Column(name = "detail_name")
	private String detailName;

	/**
	 * 会员人数
	 */
	@Column(name = "customer_count")
	private Long customerCount;

    /**
     * 创建时间
     */
    @CreatedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    @Column(name = "update_time")
    private LocalDateTime updateTime;

}