package com.wanmi.sbc.message.api.response.smssend;

import com.wanmi.sbc.message.bean.vo.SmsSendVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>短信发送修改结果</p>
 * @author zgl
 * @date 2019-12-03 15:36:05
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的短信发送信息
     */
    @ApiModelProperty(value = "已修改的短信发送信息")
    private SmsSendVO smsSendVO;
}
