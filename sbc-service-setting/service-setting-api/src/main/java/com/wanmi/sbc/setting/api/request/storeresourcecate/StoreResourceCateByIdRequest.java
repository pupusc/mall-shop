package com.wanmi.sbc.setting.api.request.storeresourcecate;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个查询店铺资源资源分类表请求参数</p>
 * @author lq
 * @date 2019-11-05 16:13:19
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResourceCateByIdRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 素材分类id
	 */
	@ApiModelProperty(value = "素材分类id")
	@NotNull
	private Long cateId;
}