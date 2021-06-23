package com.wanmi.sbc.goods.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <p>预约抢购实体</p>
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleGoodsInfoDTO{
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;

	/**
	 * 预约id
	 */
	private Long appointmentSaleId;

	/**
	 * 商户id
	 */
	private Long storeId;

	/**
	 * skuID
	 */
	private String goodsInfoId;

	/**
	 * spuID
	 */
	private String goodsId;

	/**
	 * 预约价
	 */
	private BigDecimal price;

	private String skuName;

	private String skuPic;


}