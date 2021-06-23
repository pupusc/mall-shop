package com.wanmi.sbc.message.api.response.smssenddetail;

import com.wanmi.sbc.message.bean.vo.SmsSendDetailVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）短信发送信息response</p>
 * @author zgl
 * @date 2019-12-03 15:43:37
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsSendDetailByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 短信发送信息
     */
    @ApiModelProperty(value = "短信发送信息")
    private SmsSendDetailVO smsSendDetailVO;
}
