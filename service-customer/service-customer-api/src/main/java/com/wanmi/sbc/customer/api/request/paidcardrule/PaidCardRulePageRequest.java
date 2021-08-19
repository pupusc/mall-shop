package com.wanmi.sbc.customer.api.request.paidcardrule;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>付费会员分页查询请求参数</p>
 * @author xuhai
 * @date 2021-01-29 14:04:01
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaidCardRulePageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键List
	 */
	@ApiModelProperty(value = "批量查询-主键List")
	private List<String> idList;

	/**
	 * 主键
	 */
	@ApiModelProperty(value = "主键")
	private String id;

	/**
	 * 配置类型 0：付费配置；1：续费配置
	 */
	@ApiModelProperty(value = "配置类型 0：付费配置；1：续费配置")
	private Integer type;

	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;

	/**
	 * 价格
	 */
	@ApiModelProperty(value = "价格")
	private BigDecimal price;

	/**
	 * 0:禁用；1：启用
	 */
	@ApiModelProperty(value = "0:禁用；1：启用")
	private Integer status;

	/**
	 * 付费会员类型id
	 */
	@ApiModelProperty(value = "付费会员类型id")
	private String paidCardId;

	/**
	 * 时间单位：0天，1月，2年
	 */
	@ApiModelProperty(value = "时间单位：0天，1月，2年")
	private Integer timeUnit;

	/**
	 * 时间（数值）
	 */
	@ApiModelProperty(value = "时间（数值）")
	private Integer timeVal;

	/**
	 * erp sku 编码
	 */
	@ApiModelProperty(value = "erp_sku_code")
	private String erpSkuCode;

}