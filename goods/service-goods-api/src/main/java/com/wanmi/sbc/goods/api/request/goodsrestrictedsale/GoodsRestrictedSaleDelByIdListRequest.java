package com.wanmi.sbc.goods.api.request.goodsrestrictedsale;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除限售配置请求参数</p>
 * @author baijz
 * @date 2020-04-08 11:20:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsRestrictedSaleDelByIdListRequest extends GoodsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-限售主键List
	 */
	@ApiModelProperty(value = "批量删除-限售主键List")
	@NotEmpty
	private List<Long> restrictedIdList;
}