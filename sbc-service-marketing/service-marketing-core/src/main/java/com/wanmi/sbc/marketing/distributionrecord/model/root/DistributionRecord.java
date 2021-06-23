package com.wanmi.sbc.marketing.distributionrecord.model.root;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.marketing.bean.enums.CommissionReceived;
import com.wanmi.sbc.marketing.bean.vo.GoodsInfoForDistribution;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>DistributionRecord实体类</p>
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@Data
@Entity
@Table(name = "distribution_record")
public class DistributionRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分销记录表主键
	 */
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@Column(name = "record_id")
	private String recordId;

	/**
	 * 货品Id
	 */
	@ApiModelProperty(value = "货品Id")
	@Column(name = "goods_info_id")
	private String goodsInfoId;

	/**
	 * 订单交易号
	 */
	@ApiModelProperty(value = "订单交易号")
	@Column(name = "trade_id")
	private String tradeId;

	/**
	 * 店铺Id
	 */
	@ApiModelProperty(value = "店铺Id")
	@Column(name = "store_id")
	private Long storeId;

	/**
	 * 商家Id
	 */
	@ApiModelProperty(value = "商家Id")
	@Column(name = "company_id")
	private Long companyId;

	/**
	 * 会员Id
	 */
	@ApiModelProperty(value = "会员Id")
	@Column(name = "customer_id")
	private String customerId;

	/**
	 * 分销员Id
	 */
	@ApiModelProperty(value = "分销员标识UUID")
	@Column(name = "distributor_id")
	private String distributorId;

	/**
	 * 付款时间
	 */
	@ApiModelProperty(value = "付款时间")
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "pay_time")
	private LocalDateTime payTime;

	/**
	 * 订单完成时间
	 */
	@ApiModelProperty(value = "订单完成时间")
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "finish_time")
	private LocalDateTime finishTime;

	/**
	 * 佣金入账时间
	 */
	@ApiModelProperty(value = "佣金入账时间")
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "mission_received_time")
	private LocalDateTime missionReceivedTime;

	/**
	 * 订单商品总金额
	 */
	@ApiModelProperty(value = "订单商品总金额")
	@Column(name = "order_goods_price")
	private BigDecimal orderGoodsPrice;

	/**
	 * 商品的数量
	 */
	@ApiModelProperty(value = "商品的数量")
	@Column(name = "order_goods_count")
	private Long orderGoodsCount;

	/**
	 * 佣金比例
	 */
	@ApiModelProperty(value = "佣金比例")
	@Column(name = "commission_rate")
	private BigDecimal commissionRate;

	/**
	 * 货品的总佣金
	 */
	@ApiModelProperty(value = "货品的总佣金")
	@Column(name = "commission_goods")
	private BigDecimal commissionGoods;

	/**
	 * 是否已入账 0：否  1：是
	 */
	@Column(name = "commission_state")
	@ApiModelProperty(value = "是否已入账")
	private CommissionReceived commissionState;

	/**
	 * 是否删除 0：未删除  1：已删除
	 */
	@Column(name = "del_flag")
	@ApiModelProperty(value = "是否已被删除")
	private DeleteFlag deleteFlag;

	@Column(name = "distributor_customer_id")
	@ApiModelProperty(value = "分销员的客户id")
	private String distributorCustomerId;

	/**
	 * 规格值信息
	 */
	@Transient
	private List<GoodsInfoSpecDetailRelVO> goodsInfoSpecDetailRelVOS;

	/**
	 * 分销记录使用的货品信息
	 */
	@Transient
	private GoodsInfoForDistribution goodsInfo;

	/**
	 * 会员信息
	 */
	@Transient
	private CustomerDetailVO customerDetailVO;

	/**
	 * 分销员信息
	 */
	@Transient
	private DistributionCustomerVO distributionCustomerVO;

	/**
	 * 店铺信息
	 */
	@Transient
	private StoreVO storeVO;

	/**
	 * 商家信息
	 */
	@Transient
	private CompanyInfoVO companyInfoVO;

}