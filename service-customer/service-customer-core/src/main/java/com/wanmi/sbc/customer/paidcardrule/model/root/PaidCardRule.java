package com.wanmi.sbc.customer.paidcardrule.model.root;

import com.wanmi.sbc.customer.bean.enums.PaidCardRuleTypeEnum;
import com.wanmi.sbc.customer.bean.enums.StatusEnum;
import com.wanmi.sbc.customer.bean.enums.TimeUnitEnum;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>付费会员实体类</p>
 * @author xuhai
 * @date 2021-01-29 14:04:01
 */
@Data
@Entity
@Table(name = "paid_card_rule")
public class PaidCardRule implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@Id
	@Column(name = "id")
	private String id;

	/**
	 * 配置类型 0：付费配置；1：续费配置
	 */
	@Column(name = "type")
	@Enumerated
	private PaidCardRuleTypeEnum type;

	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;

	/**
	 * 价格
	 */
	@Column(name = "price")
	private BigDecimal price;

	/**
	 * 0:禁用；1：启用
	 */
	@Column(name = "status")
	@Enumerated
	private StatusEnum status;

	/**
	 * 付费会员类型id
	 */
	@Column(name = "paid_card_id")
	private String paidCardId;

	/**
	 * 时间单位：0天，1月，2年
	 */
	@Column(name = "time_unit")
	@Enumerated
	private TimeUnitEnum timeUnit;

	/**
	 * 时间（数值）
	 */
	@Column(name = "time_val")
	private Integer timeVal;
	/**
	 * erp sku 编码
	 */
	@Column(name = "erp_sku_code")
	private String erpSkuCode;


}