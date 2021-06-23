package com.wanmi.sbc.customer.storestatistics.model.root;

import java.math.BigDecimal;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * <p>店铺客户消费统计表实体类</p>
 * @author yang
 * @date 2019-03-13 17:55:08
 */
@Data
@Entity
@Table(name = "store_consumer_statistics")
public class StoreConsumerStatistics implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	private String id;

	/**
	 * 用户id
	 */
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * 店铺id
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 会员在该店铺下单数
	 */
	@Column(name = "trade_count")
	private Integer tradeCount;

	/**
	 * 会员在该店铺消费额
	 */
	@Column(name = "trade_price_count")
	private BigDecimal tradePriceCount;

	/**
	 * 更新时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 删除标记 0:未删除 1:已删除
	 */
	@Column(name = "del_flag")
	private Integer delFlag;

}