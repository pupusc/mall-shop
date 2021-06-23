package com.wanmi.sbc.message.api.response.messagesendnode;

import com.wanmi.sbc.message.bean.vo.MessageSendNodeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>站内信通知节点表列表结果</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendNodeListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 站内信通知节点表列表结果
     */
    @ApiModelProperty(value = "站内信通知节点表列表结果")
    private List<MessageSendNodeVO> messageSendNodeVOList;
}
