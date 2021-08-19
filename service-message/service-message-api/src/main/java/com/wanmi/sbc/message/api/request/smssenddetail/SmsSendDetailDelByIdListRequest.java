package com.wanmi.sbc.message.api.request.smssenddetail;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除短信发送请求参数</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDetailDelByIdListRequest extends SmsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-idList
	 */
	@ApiModelProperty(value = "批量删除-idList")
	@NotEmpty
	private List<Long> idList;
}