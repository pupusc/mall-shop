package com.wanmi.sbc.setting.api.response.wechatloginset;

import com.wanmi.sbc.setting.bean.vo.WechatLoginSetVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）微信授权登录配置信息response</p>
 * @author lq
 * @date 2019-11-05 16:15:25
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WechatLoginSetByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 微信授权登录配置信息
     */
    @ApiModelProperty(value = "微信授权登录配置信息")
    private WechatLoginSetVO wechatLoginSetVO;
}
