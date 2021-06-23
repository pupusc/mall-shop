package com.wanmi.sbc.message.api.response.smssend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>短信发送新增结果</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDetailSendResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 发送标记
     */
    @ApiModelProperty(value = "发送标记")
    private Boolean sendFlag;
}
