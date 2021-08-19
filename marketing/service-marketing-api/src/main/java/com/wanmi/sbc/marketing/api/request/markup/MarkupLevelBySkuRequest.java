package com.wanmi.sbc.marketing.api.request.markup;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.marketing.api.request.market.MarketingIdRequest;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 通过sku查询,换购商品
 * @author he
 * @date 2021-02-04 16:09:09
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkupLevelBySkuRequest  {
	private static final long serialVersionUID = 1L;

	/**
	 * 商品信息
	 */
	@ApiModelProperty(value = "商品信息")
	@NotNull
	private List<String> skuIds;

	@ApiModelProperty(value = "订单价格")
	@NotNull
	private BigDecimal levelAmount;

	/**
	 * 客户等级ID
	 */
	@ApiModelProperty(value = "客户等级ID")
	private List<MarketingJoinLevel> marketingJoinLevelList= new ArrayList<>();

	/**
	 * 客户等级ID
	 */
	@ApiModelProperty(value = "客户等级ID")
	Map<Long, CommonLevelVO> levelMap=new HashMap<>();
}