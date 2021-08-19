package com.wanmi.sbc.message.api.response.messagesendnode;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.message.bean.vo.MessageSendNodeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>站内信通知节点表分页结果</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageSendNodePageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 站内信通知节点表分页结果
     */
    @ApiModelProperty(value = "站内信通知节点表分页结果")
    private MicroServicePage<MessageSendNodeVO> messageSendNodeVOPage;
}
