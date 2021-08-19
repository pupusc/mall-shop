package com.wanmi.sbc.setting.api.request.storeresourcecate;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除店铺资源资源分类表请求参数</p>
 * @author lq
 * @date 2019-11-05 16:13:19
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreResourceCateDelByIdListRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-素材分类idList
	 */
	@ApiModelProperty(value = "批量删除-素材分类idList")
	@NotEmpty
	private List<Long> cateIdList;
}