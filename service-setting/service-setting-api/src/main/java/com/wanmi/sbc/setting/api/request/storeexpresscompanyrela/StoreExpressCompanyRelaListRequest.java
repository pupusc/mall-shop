package com.wanmi.sbc.setting.api.request.storeexpresscompanyrela;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.*;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>店铺快递公司关联表列表查询请求参数</p>
 * @author lq
 * @date 2019-11-05 16:12:13
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreExpressCompanyRelaListRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;
	/**
	 * 店铺标识
	 */
	@ApiModelProperty(value = "店铺标识")
	private Long storeId;
}