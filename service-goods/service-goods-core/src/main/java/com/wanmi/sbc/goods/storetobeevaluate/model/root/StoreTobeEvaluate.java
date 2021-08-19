package com.wanmi.sbc.goods.storetobeevaluate.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * <p>店铺服务待评价实体类</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@Data
@Entity
@Table(name = "store_tobe_evaluate")
public class StoreTobeEvaluate implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "id")
	private String id;

	/**
	 * 店铺Id
	 */
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 店铺logo
	 */
	@Column(name = "store_logo")
	private String storeLogo;

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
	 * 购买商品数量
	 */
	@Column(name = "goods_num")
	private Integer goodsNum;

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
	 * 店铺自动评价日期
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	@Column(name = "auto_store_evaluate_date")
	private LocalDate autoStoreEvaluateDate;

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

}