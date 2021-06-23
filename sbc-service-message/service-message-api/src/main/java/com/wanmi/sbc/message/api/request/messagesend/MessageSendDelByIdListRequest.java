package com.wanmi.sbc.message.api.request.messagesend;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;
import java.util.List;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>批量删除站内信任务表请求参数</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendDelByIdListRequest extends SmsBaseRequest {
private static final long serialVersionUID = 1L;

	/**
	 * 批量删除-主键idList
	 */
	@ApiModelProperty(value = "批量删除-主键idList")
	@NotEmpty
	private List<Long> messageIdList;
}
