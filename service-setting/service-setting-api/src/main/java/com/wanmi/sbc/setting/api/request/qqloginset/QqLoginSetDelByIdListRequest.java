package com.wanmi.sbc.setting.api.request.qqloginset;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除qq登录信息请求参数</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QqLoginSetDelByIdListRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-qqSetIdList
	 */
	@ApiModelProperty(value = "批量删除-qqSetIdList")
	@NotEmpty
	private List<String> qqSetIdList;
}