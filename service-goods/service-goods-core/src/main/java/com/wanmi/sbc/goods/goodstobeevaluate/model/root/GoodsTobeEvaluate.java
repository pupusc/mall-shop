package com.wanmi.sbc.goods.goodstobeevaluate.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.Serializable;

/**
 * <p>订单商品待评价实体类</p>
 * @author lzw
 * @date 2019-03-20 14:47:38
 */
@Data
@Entity
@Table(name = "goods_tobe_evaluate")
public class GoodsTobeEvaluate implements Serializable {
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
	 * 店铺名称
	 */
	@Column(name = "store_name")
	private String storeName;

	/**
	 * 商品id(spuId)
	 */
	@Column(name = "goods_id")
	private String goodsId;

	/**
	 * 商品图片
	 */
	@Column(name = "goods_img")
	private String goodsImg;

	/**
	 * 货品id(skuId)
	 */
	@Column(name = "goods_info_id")
	private String goodsInfoId;

	/**
	 * 商品名称
	 */
	@Column(name = "goods_info_name")
	private String goodsInfoName;

	/**
	 * 规格值名称
	 */
	@Column(name = "goods_spec_detail")
	private String goodsSpecDetail;

	/**
	 * 购买时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "buy_time")
	private LocalDateTime buyTime;

	/**
	 * 订单号
	 */
	@Column(name = "order_no")
	private String orderNo;

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
	 * 是否评价 0：未评价，1：已评价
	 */
	@Column(name = "evaluate_status")
	private Integer evaluateStatus;

	/**
	 * 是否晒单 0：未晒单，1：已晒单
	 */
	@Column(name = "evaluate_img_status")
	private Integer evaluateImgStatus;

	/**
	 * 商品自动评价日期
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
	@Column(name = "auto_goods_evaluate_date")
	private LocalDate autoGoodsEvaluateDate;

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