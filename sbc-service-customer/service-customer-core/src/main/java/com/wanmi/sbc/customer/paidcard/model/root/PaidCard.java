package com.wanmi.sbc.customer.paidcard.model.root;

import java.math.BigDecimal;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.enums.AccessType;
import com.wanmi.sbc.customer.bean.enums.BgTypeEnum;
import com.wanmi.sbc.customer.bean.enums.EnableEnum;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;
import java.util.List;

/**
 * <p>付费会员实体类</p>
 * @author xuhai
 * @date 2021-01-29 14:03:56
 */
@Data
@Entity
@Table(name = "paid_card")
public class PaidCard implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 背景信息。背景颜色传十六进制类似 #ccc；背景图片传图片地址
	 */
	@Column(name = "background")
	private String background;

	/**
	 * 付费会员图标
	 */
	@Column(name = "icon")
	private String icon;

	/**
	 * 折扣率
	 */
	@Column(name = "discount_rate")
	private BigDecimal discountRate;

	/**
	 * 规则说明
	 */
	@Column(name = "rule")
	private String rule;

	/**
	 * 付费会员用户协议
	 */
	@Column(name = "agreement")
	private String agreement;

	/**
	 * 删除标识 1：删除；0：未删除
	 */
	@Column(name = "del_flag")
	@Enumerated
	private DeleteFlag delFlag;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 启动禁用标识 1：启用；2：禁用
	 */
	@Column(name = "enable")
	@Enumerated
	private EnableEnum enable;

	/**
	 * 创建人ID
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 修改人ID
	 */
	@Column(name = "update_person")
	private String updatePerson;

	/**
	 * 背景类型0背景色；1背景图片
	 */
	@Column(name = "bg_type")
	private BgTypeEnum bgType;

	/**
	 * 前景色
	 */
	@Column(name = "text_color")
	private String textColor;

	/**
	 * erp spu 编码
	 */
	@Column(name = "erp_spu_code")
	private String erpSpuCode;

	/**
	 * 付费会员数量
	 */
	@Transient
	private Long customerNum=0L;

	@Transient
	private List<String> rightsNameList;

	/**
	 * 购买类型 0 用户购买 1 有赞同步
	 */
	@Enumerated
	@Column(name = "access_type")
	private AccessType accessType;

}