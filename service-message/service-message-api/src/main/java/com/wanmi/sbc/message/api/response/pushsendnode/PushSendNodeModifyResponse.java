package com.wanmi.sbc.message.api.response.pushsendnode;

import com.wanmi.sbc.message.bean.vo.PushSendNodeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>会员推送通知节点修改结果</p>
 * @author Bob
 * @date 2020-01-13 10:47:41
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushSendNodeModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的会员推送通知节点信息
     */
    @ApiModelProperty(value = "已修改的会员推送通知节点信息")
    private PushSendNodeVO pushSendNodeVO;
}
