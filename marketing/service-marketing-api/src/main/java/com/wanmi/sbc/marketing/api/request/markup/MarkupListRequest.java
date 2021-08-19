package com.wanmi.sbc.marketing.api.request.markup;

import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.marketing.api.request.market.MarketingIdRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>单个查询加价购活动请求参数</p>
 * @author he
 * @date 2021-02-04 16:09:09
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarkupListRequest  {
	private static final long serialVersionUID = 1L;

	private List<Long> marketingId;
}