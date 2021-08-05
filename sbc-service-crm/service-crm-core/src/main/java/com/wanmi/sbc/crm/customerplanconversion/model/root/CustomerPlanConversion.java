package com.wanmi.sbc.crm.customerplanconversion.model.root;

import java.math.BigDecimal;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>运营计划转化效果实体类</p>
 * @author zhangwenchang
 * @date 2020-02-12 00:16:50
 */
@Data
@Entity
@Table(name = "customer_plan_conversion")
public class CustomerPlanConversion implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	/**
	 * 运营计划id
	 */
	@Column(name = "plan_id")
	private Long planId;

	/**
	 * 访客数UV
	 */
	@Column(name = "visitors_uv_count")
	private Long visitorsUvCount;

	/**
	 * 下单人数
	 */
	@Column(name = "order_person_count")
	private Long orderPersonCount;

	/**
	 * 下单笔数
	 */
	@Column(name = "order_count")
	private Long orderCount;

	/**
	 * 付款人数
	 */
	@Column(name = "pay_person_count")
	private Long payPersonCount;

	/**
	 * 付款笔数
	 */
	@Column(name = "pay_count")
	private Long payCount;

	/**
	 * 付款金额
	 */
	@Column(name = "total_price")
	private BigDecimal totalPrice;

	/**
	 * 客单价
	 */
	@Column(name = "unit_price")
	private BigDecimal unitPrice;

	/**
	 * 覆盖人数
	 */
	@Column(name = "covers_count")
	private Long coversCount;

	/**
	 * 访客人数
	 */
	@Column(name = "visitors_count")
	private Long visitorsCount;

	/**
	 * 访客人数/覆盖人数转换率
	 */
	@Column(name = "covers_visitors_rate")
	private Double coversVisitorsRate;

	/**
	 * 付款人数/访客人数转换率
	 */
	@Column(name = "pay_visitors_rate")
	private Double payVisitorsRate;

	/**
	 * 付款人数/覆盖人数转换率
	 */
	@Column(name = "pay_covers_rate")
	private Double payCoversRate;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

}