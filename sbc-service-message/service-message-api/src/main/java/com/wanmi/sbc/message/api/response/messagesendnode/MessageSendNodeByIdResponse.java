package com.wanmi.sbc.message.api.response.messagesendnode;

import com.wanmi.sbc.message.bean.vo.MessageSendNodeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）站内信通知节点表信息response</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendNodeByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 站内信通知节点表信息
     */
    @ApiModelProperty(value = "站内信通知节点表信息")
    private MessageSendNodeVO messageSendNodeVO;
}
