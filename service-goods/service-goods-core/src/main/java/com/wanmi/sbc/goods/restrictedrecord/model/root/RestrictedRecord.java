package com.wanmi.sbc.goods.restrictedrecord.model.root;

import com.wanmi.sbc.goods.bean.enums.RestrictedCycleType;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDate;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

/**
 * <p>限售实体类</p>
 * @author 限售记录
 * @date 2020-04-11 15:59:01
 */
@Data
@Entity
@Table(name = "restricted_record")
public class RestrictedRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 记录主键
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "record_id")
	private Long recordId;

	/**
	 * 会员的主键
	 */
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * 货品主键
	 */
	@Column(name = "goods_info_id")
	private String goodsInfoId;

	/**
	 * 购买的数量
	 */
	@Column(name = "purchase_num")
	private Long purchaseNum;

	/**
	 * 周期类型（0:天  1:周  2:月  3:年  4:终生  5:订单）
	 */
	@Column(name = "restricted_cycle_type")
	private RestrictedCycleType restrictedCycleType;

	/**
	 * 开始时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	@Column(name = "start_date")
	private LocalDate startDate;

	/**
	 * 结束时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	@Column(name = "end_date")
	private LocalDate endDate;

}