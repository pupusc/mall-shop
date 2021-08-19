package com.wanmi.sbc.setting.api.request.systemresourcecate;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除平台素材资源分类请求参数</p>
 * @author lq
 * @date 2019-11-05 16:14:55
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemResourceCateDelByIdListRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-素材资源分类idList
	 */
	@ApiModelProperty(value = "批量删除-素材资源分类idList")
	@NotEmpty
	private List<Long> cateIdList;
}