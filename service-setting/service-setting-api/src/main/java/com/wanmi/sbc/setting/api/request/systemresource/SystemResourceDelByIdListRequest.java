package com.wanmi.sbc.setting.api.request.systemresource;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * <p>批量删除平台素材资源请求参数</p>
 * @author lq
 * @date 2019-11-05 16:14:27
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemResourceDelByIdListRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-素材资源IDList
	 */
	@ApiModelProperty(value = "批量删除-素材资源IDList")
	@NotEmpty
	private List<Long> resourceIds;
}