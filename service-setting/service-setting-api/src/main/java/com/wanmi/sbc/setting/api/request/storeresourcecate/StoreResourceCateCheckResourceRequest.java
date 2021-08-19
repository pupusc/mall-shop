package com.wanmi.sbc.setting.api.request.storeresourcecate;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>平台素材资源分类列表查询请求参数</p>
 * @author lq
 * @date 2019-11-05 16:14:55
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResourceCateCheckResourceRequest extends BaseQueryRequest {
	private static final long serialVersionUID = 1L;


	/**
	 * 素材资源分类id
	 */
	@ApiModelProperty(value = "素材资源分类id")
	@NotNull
	private Long cateId;

	/**
	 * 店铺标识
	 */
	@ApiModelProperty(value = "店铺标识")
	private Long storeId;

	
}