package com.wanmi.sbc.message.api.request.messagesend;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>单个删除站内信任务表请求参数</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendDelByIdRequest extends SmsBaseRequest {
private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(value = "主键id")
    private Long messageId;

    @ApiModelProperty(value = "推送标识id")
    private String pushId;
}
