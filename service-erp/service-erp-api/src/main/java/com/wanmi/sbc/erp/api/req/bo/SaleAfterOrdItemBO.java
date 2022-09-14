package com.wanmi.sbc.erp.api.req.bo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SaleAfterOrdItemBO implements Serializable {

	private static final long serialVersionUID = -5624069689491397440L;
	
	/**
	 * 业务类型 1:实物商品 2：虚拟商品-樊登，3：图书订阅商品 4-历史虚拟
	 */
	private Integer metaGoodsType;
	
	/**
	 * 
	 */
	private Long metaGoodsId;
	
	/**
	 * 
	 */
	private String metaGoodsName;
	
	/**
	 * 
	 */
	private Long metaSkuId;
	
	/**
	 * 
	 */
	private String metaSkuName;
	
	/**
	 * 
	 */
	private String platformItemId;
	
	/**
	 * 寻仓后价格
	 */
	private Integer costPrice;
	
	/**
	 * 
	 */
	private Integer goodsId;
	
	/**
	 * 
	 */
	private String platformGoodsId;
	
	/**
	 * 
	 */
	private String platformGoodsName;
	
	/**
	 * 
	 */
	private String platformSkuId;
	
	/**
	 * 
	 */
	private String platformSkuName;
	
	/**
	 * 销售编码
	 */
	private String saleCode;
	
	/**
	 * 商品价格
	 */
	private Integer price;
	
	/**
	 * 数量
	 */
	private Integer num;
	
	/**
	 * 订单状态
	 */
	private Integer status;
	
	/**
	 * 
	 */
	private Integer stockType;
	
	/**
	 * 快递编码
	 */
	private String expressCode;
	
	/**
	 * 快递号
	 */
	private String expressNo;
	
	/**
	 * 仓库ID
	 */
	private Long whId;
	
	/**
	 * 仓库编码
	 */
	private String whCode;
	
	/**
	 * 
	 */
	private Integer costPostPrice;
	
	/**
	 * 优惠总金额
	 */
	private Integer discountFee;
	
	/**
	 * 实付金额
	 */
	private Integer oughtFee;
	
	/**
	 * 
	 */
	private Integer refundStatus;
	
	/**
	 * 
	 */
	private Integer refundFee;
	
	/**
	 * 发货状态
	 */
	private Integer deliveryStatus;
	
	/**
	 * 发货时间
	 */
	private Date deliveryTime;
	
	/**
	 * 签收时间
	 */
	private Date signatureTime;
	
	/**
	 * 计划发货时间
	 */
	private Date planDeliveryTime;
	
	/**
	 * 是否为赠送
	 */
	private Integer giftFlag;
	
	/**
	 * 赠送类型
	 */
	private String giftObjectType;
	
	/**
	 * 赠送商品ID
	 */
	private Long giftObjectId;
	
	/**
	 * 订单ID
	 */
	private Long orderId;
	
	/**
	 * 订单包ID
	 */
	private Long orderPackId;
	
	/**
	 * 子订单分类
	 */
	private Integer orderItemType;
	
	/**
	 * 商品子类型
	 */
	private Integer metaGoodsSubType;
	
	/**
	 * 市场价
	 */
	private Integer marketPrice;

}
