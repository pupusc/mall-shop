package com.wanmi.sbc.goods.api.request.bookingsalegoods;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>预售商品信息通用查询请求参数</p>
 * @author dany
 * @date 2020-06-05 10:51:35
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleGoodsQueryRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-idList
	 */
	@ApiModelProperty(value = "批量查询-idList")
	private List<Long> idList;

	/**
	 * id
	 */
	@ApiModelProperty(value = "id")
	private Long id;

	/**
	 * 预售id
	 */
	@ApiModelProperty(value = "预售id")
	private Long bookingSaleId;

	/**
	 * 商户id
	 */
	@ApiModelProperty(value = "商户id")
	private Long storeId;

	/**
	 * skuID
	 */
	@ApiModelProperty(value = "skuID")
	private String goodsInfoId;

	/**
	 * skuID List
	 */
	@ApiModelProperty(value = "skuID-List")
	private List<String> goodsInfoIdList;

	/**
	 * 预售id List
	 */
	@ApiModelProperty(value = "预售id List")
	private List<Long> bookingSaleIdList;

	/**
	 * spuID
	 */
	@ApiModelProperty(value = "spuID")
	private String goodsId;

	/**
	 * 定金
	 */
	@ApiModelProperty(value = "定金")
	private BigDecimal handSelPrice;

	/**
	 * 膨胀价格
	 */
	@ApiModelProperty(value = "膨胀价格")
	private BigDecimal inflationPrice;

	/**
	 * 预售价
	 */
	@ApiModelProperty(value = "预售价")
	private BigDecimal bookingPrice;

	/**
	 * 预售数量
	 */
	@ApiModelProperty(value = "预售数量")
	private Integer bookingCount;

	/**
	 * 定金支付数量
	 */
	@ApiModelProperty(value = "定金支付数量")
	private Integer handSelCount;

	/**
	 * 尾款支付数量
	 */
	@ApiModelProperty(value = "尾款支付数量")
	private Integer tailCount;

	/**
	 * 全款支付数量
	 */
	@ApiModelProperty(value = "全款支付数量")
	private Integer payCount;

	/**
	 * 搜索条件:创建时间开始
	 */
	@ApiModelProperty(value = "搜索条件:创建时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeBegin;
	/**
	 * 搜索条件:创建时间截止
	 */
	@ApiModelProperty(value = "搜索条件:创建时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime createTimeEnd;

	/**
	 * 搜索条件:更新时间开始
	 */
	@ApiModelProperty(value = "搜索条件:更新时间开始")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeBegin;
	/**
	 * 搜索条件:更新时间截止
	 */
	@ApiModelProperty(value = "搜索条件:更新时间截止")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	private LocalDateTime updateTimeEnd;

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private String createPerson;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private String updatePerson;

}