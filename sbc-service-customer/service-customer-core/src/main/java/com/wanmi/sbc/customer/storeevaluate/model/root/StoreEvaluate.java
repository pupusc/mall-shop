package com.wanmi.sbc.customer.storeevaluate.model.root;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.enums.TerminalSource;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * <p>店铺评价实体类</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@Data
@Entity
@Table(name = "store_evaluate")
public class StoreEvaluate implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 评价id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "evaluate_id")
	private String evaluateId;

	/**
	 * 店铺Id
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 店铺名称
	 */
	@Column(name = "store_name")
	private String storeName;

	/**
	 * 订单号
	 */
	@Column(name = "order_no")
	private String orderNo;

	/**
	 * 购买时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "buy_time")
	private LocalDateTime buyTime;

	/**
	 * 会员Id
	 */
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * 会员名称
	 */
	@Column(name = "customer_name")
	private String customerName;

	/**
	 * 会员登录账号|手机号
	 */
	@Column(name = "customer_account")
	private String customerAccount;

	/**
	 * 商品评分
	 */
	@Column(name = "goods_score")
	private Integer goodsScore;

	/**
	 * 服务评分
	 */
	@Column(name = "server_score")
	private Integer serverScore;

	/**
	 * 物流评分
	 */
	@Column(name = "logistics_score")
	private Integer logisticsScore;

	/**
	 * 综合评分（冗余字段看后面怎么做）
	 */
	@Column(name = "composite_score")
	private BigDecimal compositeScore;

	/**
	 * 是否删除标志 0：否，1：是
	 */
	@Column(name = "del_flag")
	private Integer delFlag;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 创建人
	 */
	@Column(name = "create_person")
	private String createPerson;

	/**
	 * 修改时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 修改人
	 */
	@Column(name = "update_person")
	private String updatePerson;

	/**
	 * 删除时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "del_time")
	private LocalDateTime delTime;

	/**
	 * 删除人
	 */
	@Column(name = "del_person")
	private String delPerson;

    /**
     * 终端来源
     */
    @Column(name = "terminal_source")
    private TerminalSource terminalSource;

}