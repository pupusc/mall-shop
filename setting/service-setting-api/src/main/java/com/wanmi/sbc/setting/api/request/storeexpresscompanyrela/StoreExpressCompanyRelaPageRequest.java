package com.wanmi.sbc.setting.api.request.storeexpresscompanyrela;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>店铺快递公司关联表分页查询请求参数</p>
 * @author lq
 * @date 2019-11-05 16:12:13
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreExpressCompanyRelaPageRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量查询-主键UUIDList
	 */
	@ApiModelProperty(value = "批量查询-主键UUIDList")
	private List<Long> idList;

	/**
	 * 主键UUID
	 */
	@ApiModelProperty(value = "主键UUID")
	private Long id;

	/**
	 * 主键ID,自增
	 */
	@ApiModelProperty(value = "主键ID,自增")
	private Long expressCompanyId;

	/**
	 * 店铺标识
	 */
	@ApiModelProperty(value = "店铺标识")
	private Long storeId;

	/**
	 * 商家标识
	 */
	@ApiModelProperty(value = "商家标识")
	private Integer companyInfoId;
}