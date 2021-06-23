package com.wanmi.sbc.message.api.response.messagesend;

import com.wanmi.sbc.message.bean.vo.MessageSendVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>站内信任务表新增结果</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的站内信任务表信息
     */
    @ApiModelProperty(value = "已新增的站内信任务表信息")
    private MessageSendVO messageSendVO;
}
