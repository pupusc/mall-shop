package com.wanmi.sbc.goods.api.request.priceadjustmentrecorddetail;

import java.math.BigDecimal;
import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>调价单详情表列表查询请求参数</p>
 * @author chenli
 * @date 2020-12-09 19:55:41
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustmentRecordDetailListRequest extends BaseRequest {
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
	 * 调价单号
	 */
	@ApiModelProperty(value = "调价单号")
	private String priceAdjustmentNo;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String goodsInfoName;

	/**
	 * SKU编码
	 */
	@ApiModelProperty(value = "SKU编码")
	private String goodsInfoNo;

	/**
	 * 商品规格
	 */
	@ApiModelProperty(value = "商品规格")
	private String goodsSpecText;

	/**
	 * 是否独立设价：0 否、1 是
	 */
	@ApiModelProperty(value = "是否独立设价：0 否、1 是")
	private Integer aloneFlag;

	/**
	 * 销售类别(0:批发,1:零售)
	 */
	@ApiModelProperty(value = "销售类别(0:批发,1:零售)")
	private Integer saleType;

	/**
	 * 设价类型,0:按客户(等级)1:按订货量(阶梯价)2:按市场价
	 */
	@ApiModelProperty(value = "设价类型,0:按客户(等级)1:按订货量(阶梯价)2:按市场价")
	private Integer priceType;

	/**
	 * 原市场价
	 */
	@ApiModelProperty(value = "原市场价")
	private BigDecimal originalMarketPrice;

	/**
	 * 调整后市场价
	 */
	@ApiModelProperty(value = "调整后市场价")
	private BigDecimal adjustedMarketPrice;

	/**
	 * 差异
	 */
	@ApiModelProperty(value = "差异")
	private BigDecimal priceDifference;

	/**
	 * 等级价 eg:[{},{}...]
	 */
	@ApiModelProperty(value = "等级价 eg:[{},{}...]")
	private String leverPrice;

	/**
	 * 阶梯价 eg:[{},{}...]
	 */
	@ApiModelProperty(value = "阶梯价 eg:[{},{}...]")
	private String intervalPrice;

	/**
	 * 执行结果：0 未执行、1 执行成功、2 执行失败
	 */
	@ApiModelProperty(value = "执行结果：0 未执行、1 执行成功、2 执行失败")
	private Integer adjustResult;

	/**
	 * 失败原因
	 */
	@ApiModelProperty(value = "失败原因")
	private String failReason;

	/**
	 * 是否确认：0 未确认、1 已确认
	 */
	@ApiModelProperty(value = "是否确认：0 未确认、1 已确认")
	private Integer confirmFlag;

}