package com.wanmi.sbc.marketing.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>拼团活动信息表VO</p>
 * @author groupon
 * @date 2019-05-15 14:02:38
 */
@ApiModel
@Data
public class GrouponSettingGoodsVO implements Serializable{

	private static final long serialVersionUID = -7216585267445597002L;
	/**
	 * spu编号
	 */
	@ApiModelProperty(value = "spu编号")
	private String goodsId;

	/**
	 * spu编码
	 */
	@ApiModelProperty(value = "spu编号")
	private String goodsNo;

	/**
	 * sku编号(默认取第一个)
	 */
	@ApiModelProperty(value = "spu编号")
	private String goodsInfoId;

	/**
	 * spu商品名称
	 */
	@ApiModelProperty(value = "spu商品名称")
	private String goodsName;

	/**
	 * 商品主图
	 */
	@ApiModelProperty(value = "商品主图")
	private String goodsImg;

	/**
	 * 拼团价格--最低拼团价格
	 */
	@ApiModelProperty(value = "拼团价格")
	private BigDecimal grouponPrice;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime endTime;
}