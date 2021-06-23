package com.wanmi.sbc.setting.api.response.push;

import com.wanmi.sbc.setting.bean.vo.AppPushConfigVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）消息推送信息response</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppPushConfigByIdResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息推送信息
     */
    @ApiModelProperty(value = "消息推送信息")
    private AppPushConfigVO appPushConfigVO;
}
