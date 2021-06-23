package com.wanmi.sbc.message.api.response.pushsendnode;

import com.wanmi.sbc.message.bean.vo.PushSendNodeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>会员推送通知节点列表结果</p>
 * @author Bob
 * @date 2020-01-13 10:47:41
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushSendNodeListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员推送通知节点列表结果
     */
    @ApiModelProperty(value = "会员推送通知节点列表结果")
    private List<PushSendNodeVO> pushSendNodeVOList;
}
