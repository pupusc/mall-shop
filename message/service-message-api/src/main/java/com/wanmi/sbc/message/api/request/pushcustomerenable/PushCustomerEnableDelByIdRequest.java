package com.wanmi.sbc.message.api.request.pushcustomerenable;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>单个删除会员推送通知开关请求参数</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushCustomerEnableDelByIdRequest extends SmsBaseRequest {
private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
    @ApiModelProperty(value = "会员ID")
    @NotNull
    private String customerId;
}
