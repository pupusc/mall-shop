package com.wanmi.sbc.erp.api.req.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class SaleAfterItemBO implements Serializable {

	private static final long serialVersionUID = -276467556970597388L;

	/**
	 * 售后主单ID
	 */
	private Long saOrderId;
	
	/**
	 * 售后类型：1退货退款 2仅退款 3换货 4补偿 5赠品
	 */
	private Integer refundType;
	
	/**
	 * 退款费用
	 */
	private Integer refundFee;
	
	/**
	 * 退款运费
	 */
	private Integer refundPostFee;
	
	/**
	 * 快递编码
	 */
	private String expressCode;
	
	/**
	 * 快递号
	 */
	private String expressNo;
	
	/**
	 * 退款数量
	 */
	private Integer refundNum;
	
	/**
	 * 补偿ID，补偿数据会创建补偿订单
	 */
	private Long compensateId;
	
	/**
	 * 补偿类型
	 */
	private String compensateType;
	
	/**
	 * 子订单ID/主订单ID
	 */
	private Long objectId;
	
	/**
	 * 对象类型
	 */
	private String objectType;
	
	/**
	 * 物流状态
	 */
	private Integer deliveryStatus;
	
	/**
	 * 签收时间
	 */
	private Date signatureTime;
	
	/**
	 * 是否为商品包
	 */
	private Boolean isPack;
	
	// 下面字段为退款子表用，需要跟售后子表建立关联关系
	/**
	 * 退款明细
	 */
	private List<SaleAfterRefundDetailBO> saleAfterRefundDetailBOList;
	
	// 下面字段为商品包用，需要跟售后子表建立关联关系
	/**
	 * 主订单ID
	 */
	private Long orderId;
	
	/**
	 * 商品包ID
	 */
	private String goodsId;
	
	/**
	 * 商品包名称
	 */
	private String goodsName;
	
	/**
	 * 商品包价格
	 */
	private Integer price;
	
	/**
	 * 商品包数量
	 */
	private Integer num;
	
	/**
	 * 销售码
	 */
	private String saleCode;

	// 换货、补偿、赠品时使用
	/**
	 * 订单子单
	 */
	private List<SaleAfterOrdItemBO> saleAfterOrdItemBOList;

}
