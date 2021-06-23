package com.wanmi.sbc.message.api.request.smssend;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import com.wanmi.sbc.message.bean.dto.SmsSendDTO;
import io.swagger.annotations.ApiModel;
import lombok.*;

/**
 * <p>短信发送参数</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDetailSendRequest extends SmsBaseRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * 发送任务id
	 */
	private SmsSendDTO sendDTO;
}