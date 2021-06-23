package com.wanmi.sbc.marketing.api.request.marketingsuitssku;

import com.wanmi.sbc.common.base.BaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除组合活动关联商品sku表请求参数</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketingSuitsSkuDelByIdListRequest extends BaseRequest {
private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-主键idList
	 */
	@ApiModelProperty(value = "批量删除-主键idList")
	@NotEmpty
	private List<Long> idList;
}
