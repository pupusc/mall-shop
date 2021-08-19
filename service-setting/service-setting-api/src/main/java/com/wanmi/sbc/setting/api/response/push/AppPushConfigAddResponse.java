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
 * <p>消息推送新增结果</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppPushConfigAddResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 已新增的消息推送信息
     */
    @ApiModelProperty(value = "已新增的消息推送信息")
    private AppPushConfigVO appPushConfigVO;
}
