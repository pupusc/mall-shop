package com.wanmi.sbc.setting.platformaddress.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.setting.bean.enums.AddrLevel;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>平台地址信息实体类</p>
 * @author dyt
 * @date 2020-03-30 14:39:57
 */
@Data
@Entity
@Table(name = "platform_address")
@EntityListeners(AuditingEntityListener.class)
public class PlatformAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 地址id
	 */
	@Column(name = "addr_id")
	private String addrId;

	/**
	 * 地址名称
	 */
	@Column(name = "addr_name")
	private String addrName;

	/**
	 * 父地址ID
	 */
	@Column(name = "addr_parent_id")
	private String addrParentId;

	/**
	 * 地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)
	 */
	@Column(name = "addr_level")
    @Enumerated
	private AddrLevel addrLevel;

    /**
     * 排序号
     */
    @Column(name = "sort_no")
	private Integer sortNo;

	/**
	 * 是否删除标志 0：否，1：是；默认0
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 数据类型 0:初始化 1:人工
	 */
	@Column(name = "data_type")
	private Integer dataType;

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

	/**
	 * 删除时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "delete_time")
	private LocalDateTime deleteTime;

}