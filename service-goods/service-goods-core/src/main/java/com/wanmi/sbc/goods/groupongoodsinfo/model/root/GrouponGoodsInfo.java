package com.wanmi.sbc.goods.groupongoodsinfo.model.root;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>拼团活动商品信息表实体类</p>
 * @author groupon
 * @date 2019-05-15 14:49:12
 */
@Data
@Entity
@Table(name = "groupon_goods_info")
public class GrouponGoodsInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 拼团商品ID
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "groupon_goods_id")
	private String grouponGoodsId;

	/**
	 * SKU编号
	 */
	@Column(name = "goods_info_id")
	private String goodsInfoId;

	/**
	 * 拼团价格
	 */
	@Column(name = "groupon_price")
	private BigDecimal grouponPrice;

	/**
	 * 起售数量
	 */
	@Column(name = "start_selling_num")
	private Integer startSellingNum;

	/**
	 * 限购数量
	 */
	@Column(name = "limit_selling_num")
	private Integer limitSellingNum;

	/**
	 * 拼团活动ID
	 */
	@Column(name = "groupon_activity_id")
	private String grouponActivityId;

	/**
	 * 拼团分类ID
	 */
	@Column(name = "groupon_cate_id")
	private String grouponCateId;

	/**
	 * 是否精选
	 */
	@Column(name = "sticky")
	private Boolean sticky;

	/**
	 * 店铺ID
	 */
	@Column(name = "store_id")
	private String storeId;

	/**
	 * SPU编号
	 */
	@Column(name = "goods_id")
	private String goodsId;

	/**
	 * 商品销售数量
	 */
	@Column(name = "goods_sales_num")
	private Integer goodsSalesNum = NumberUtils.INTEGER_ZERO;

	/**
	 * 订单数量
	 */
	@Column(name = "order_sales_num")
	private Integer orderSalesNum = NumberUtils.INTEGER_ZERO;

	/**
	 * 交易额
	 */
	@Column(name = "trade_amount")
	private BigDecimal tradeAmount = BigDecimal.ZERO;

	/**
	 * 已成团人数
	 */
	@Column(name = "already_groupon_num")
	private Integer alreadyGrouponNum = NumberUtils.INTEGER_ZERO;

	/**
	 * 成团后退单数量
	 */
	@Column(name = "refund_num")
	private Integer refundNum = NumberUtils.INTEGER_ZERO;

	/**
	 * 成团后退单金额
	 */
	@Column(name = "refund_amount")
	private BigDecimal refundAmount = BigDecimal.ZERO;

	/**
	 * 活动开始时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "start_time")
	private LocalDateTime startTime;

	/**
	 * 活动结束时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "end_time")
	private LocalDateTime endTime;

	/**
	 * 活动审核状态，0：待审核，1：审核通过，2：审核不通过
	 */
	@Column(name = "audit_status")
	private AuditStatus auditStatus;

	/**
	 * 创建时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	@OneToOne(cascade = CascadeType.DETACH)
	@JoinColumn(name = "goods_info_id", insertable = false, updatable = false)
	@NotFound(action= NotFoundAction.IGNORE)
	@JsonBackReference
	private GoodsInfo goodsInfo;

}