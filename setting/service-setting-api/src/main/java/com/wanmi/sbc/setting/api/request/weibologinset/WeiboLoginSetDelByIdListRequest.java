package com.wanmi.sbc.setting.api.request.weibologinset;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除微信登录配置请求参数</p>
 * @author lq
 * @date 2019-11-05 16:17:06
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeiboLoginSetDelByIdListRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-weiboSetIdList
	 */
	@ApiModelProperty(value = "批量删除-weiboSetIdList")
	@NotEmpty
	private List<String> weiboSetIdList;
}