package com.wanmi.sbc.message.api.request.pushdetail;

import com.wanmi.sbc.message.api.request.SmsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>单个删除推送详情请求参数</p>
 * @author Bob
 * @date 2020-01-08 17:16:17
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushDetailDelByIdRequest extends SmsBaseRequest {
private static final long serialVersionUID = 1L;

    /**
     * 友盟推送任务ID
     */
    @ApiModelProperty(value = "友盟推送任务ID")
    @NotNull
    private String taskId;
}
