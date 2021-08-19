package com.wanmi.sbc.message.api.request.pushcustomerenable;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * <p>批量删除会员推送通知开关请求参数</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushCustomerEnableDelByIdListRequest extends SmsBaseRequest {
private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-会员IDList
	 */
	@ApiModelProperty(value = "批量删除-会员IDList")
	@NotEmpty
	private List<String> customerIdList;
}
