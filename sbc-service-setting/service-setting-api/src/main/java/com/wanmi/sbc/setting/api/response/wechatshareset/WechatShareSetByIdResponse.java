package com.wanmi.sbc.setting.api.response.wechatshareset;

import com.wanmi.sbc.setting.bean.vo.WechatShareSetVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）微信分享配置信息response</p>
 * @author lq
 * @date 2019-11-05 16:15:54
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatShareSetByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 微信分享配置信息
     */
    @ApiModelProperty(value = "微信分享配置信息")
    private WechatShareSetVO wechatShareSetVO;
}
