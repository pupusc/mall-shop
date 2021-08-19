package com.wanmi.sbc.message.api.response.pushsend;

import com.wanmi.sbc.message.bean.vo.PushSendVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>会员推送信息新增结果</p>
 * @author Bob
 * @date 2020-01-08 17:15:32
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushSendAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的会员推送信息信息
     */
    @ApiModelProperty(value = "已新增的会员推送信息信息")
    private PushSendVO pushSendVO;
}
