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
 * <p>消息推送修改结果</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppPushConfigModifyResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 已修改的消息推送信息
     */
    @ApiModelProperty(value = "已修改的消息推送信息")
    private AppPushConfigVO appPushConfigVO;
}
