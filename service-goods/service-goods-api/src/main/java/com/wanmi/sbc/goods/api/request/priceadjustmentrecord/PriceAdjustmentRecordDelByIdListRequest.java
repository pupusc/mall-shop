package com.wanmi.sbc.goods.api.request.priceadjustmentrecord;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除调价记录表请求参数</p>
 * @author chenli
 * @date 2020-12-09 19:57:21
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceAdjustmentRecordDelByIdListRequest extends GoodsBaseRequest {
private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-调价单号List
	 */
	@ApiModelProperty(value = "批量删除-调价单号List")
	@NotEmpty
	private List<String> idList;
}
