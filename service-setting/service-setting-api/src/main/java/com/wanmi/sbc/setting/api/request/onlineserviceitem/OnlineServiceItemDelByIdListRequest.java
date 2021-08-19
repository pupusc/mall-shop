package com.wanmi.sbc.setting.api.request.onlineserviceitem;

import com.wanmi.sbc.setting.api.request.SettingBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除onlineerviceItem请求参数</p>
 * @author lq
 * @date 2019-11-05 16:10:54
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineServiceItemDelByIdListRequest extends SettingBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-在线客服座席idList
	 */
	@ApiModelProperty(value = "批量删除-在线客服座席idList")
	@NotEmpty
	private List<Integer> serviceItemIdList;
}