package com.wanmi.sbc.message.api.request.messagesendnode;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendNodeUpdateStatusRequest implements Serializable {
    private static final long serialVersionUID = 6614161958488173054L;

    /**
     * 节点id
     */
    @ApiModelProperty(value = "节点id")
    private Long id;
}
