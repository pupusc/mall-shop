package com.wanmi.sbc.message.api.response.appmessage;

import com.wanmi.sbc.message.bean.vo.AppMessageVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）App站内信消息发送表信息response</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppMessageByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * App站内信消息发送表信息
     */
    @ApiModelProperty(value = "App站内信消息发送表信息")
    private AppMessageVO appMessageVO;
}
