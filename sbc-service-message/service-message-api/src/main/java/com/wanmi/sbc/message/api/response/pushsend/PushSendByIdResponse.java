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
 * <p>根据id查询任意（包含已删除）会员推送信息信息response</p>
 * @author Bob
 * @date 2020-01-08 17:15:32
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushSendByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 会员推送信息信息
     */
    @ApiModelProperty(value = "会员推送信息信息")
    private PushSendVO pushSendVO;
}
