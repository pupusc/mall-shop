package com.wanmi.sbc.setting.thirdaddress.model.root;

import com.wanmi.sbc.common.base.BaseEntity;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.setting.bean.enums.AddrLevel;
import lombok.Data;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>第三方地址映射表实体类</p>
 * @author dyt
 * @date 2020-08-14 13:41:44
 */
@Data
@Entity
@Table(name = "third_address")
public class ThirdAddress implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 第三方地址主键
	 */
	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 第三方地址编码id
	 */
	@Column(name = "third_addr_id")
	private String thirdAddrId;

	/**
	 * 第三方父级地址编码id
	 */
	@Column(name = "third_parent_id")
	private String thirdParentId;

	/**
	 * 地址名称
	 */
	@Column(name = "addr_name")
	private String addrName;

	/**
	 * 地址层级(0-省级;1-市级;2-区县级;3-乡镇或街道级)
	 */
	@Column(name = "level")
	@Enumerated
	private AddrLevel level;

	/**
	 * 第三方标志 0:likedMall 1:京东
	 */
	@Column(name = "third_flag")
	@Enumerated
	private ThirdPlatformType thirdFlag;

	/**
	 * 平台地址id
	 */
	@Column(name = "platform_addr_id")
	private String platformAddrId;

	/**
	 * 删除标志
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 删除时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "delete_time")
	private LocalDateTime deleteTime;

}