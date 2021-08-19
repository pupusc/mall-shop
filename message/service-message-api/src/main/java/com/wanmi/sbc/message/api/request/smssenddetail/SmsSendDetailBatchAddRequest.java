package com.wanmi.sbc.message.api.request.smssenddetail;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import com.wanmi.sbc.message.bean.dto.SmsSendDetailDTO;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

/**
 * <p>短信发送批量新增参数</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDetailBatchAddRequest extends SmsBaseRequest {
	private static final long serialVersionUID = 1L;

	private List<SmsSendDetailDTO> detailList;

}