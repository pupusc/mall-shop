package com.wanmi.sbc.message.api.request.appmessage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppMessageSetReadRequest implements Serializable {
    private static final long serialVersionUID = -4475929582134165622L;

    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String customerId;

    /**
     * app消息id
     */
    @ApiModelProperty(value = "app消息id")
    private String messageId;
}
