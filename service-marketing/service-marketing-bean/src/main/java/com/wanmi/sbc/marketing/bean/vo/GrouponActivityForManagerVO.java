package com.wanmi.sbc.marketing.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>拼团活动信息表VO</p>
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@ApiModel
@Data
public class GrouponActivityForManagerVO extends GrouponActivityVO implements Serializable{


	private static final long serialVersionUID = -7216585267445597002L;
	/**
	 * 拼团价格--最低拼团价格
	 */
	@ApiModelProperty(value = "拼团价格")
	private BigDecimal grouponPrice;


	/**
	 * 商家名称
	 */
	@ApiModelProperty(value = "商家名称")
	private String supplierName;


	/**
	 * skuId
	 */
	@ApiModelProperty(value = "skuId")
	private String goodsInfoId;

	/**
	 * spu编码
	 */
	@ApiModelProperty(value = "spu编码")
	private String goodsNo;

	/**
	 * 拼团分类名称
	 */
	@ApiModelProperty(value = "拼团分类名称")
	private String grouponCateName;


}