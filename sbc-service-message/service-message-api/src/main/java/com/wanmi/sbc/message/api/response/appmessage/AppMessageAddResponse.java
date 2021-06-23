package com.wanmi.sbc.message.api.response.appmessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>App站内信消息发送表新增结果</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppMessageAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的App站内信消息发送表信息的数量
     */
    @ApiModelProperty(value = "已新增的App站内信消息发送表信息的数量")
    private Integer size;
}
