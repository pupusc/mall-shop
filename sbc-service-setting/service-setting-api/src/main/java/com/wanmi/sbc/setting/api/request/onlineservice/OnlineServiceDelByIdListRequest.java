package com.wanmi.sbc.setting.api.request.onlineservice;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除onlineService请求参数</p>
 * @author lq
 * @date 2019-11-05 16:10:28
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceDelByIdListRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-在线客服主键List
	 */
	@ApiModelProperty(value = "批量删除-在线客服主键List")
	@NotEmpty
	private List<Integer> onlineServiceIdList;
}