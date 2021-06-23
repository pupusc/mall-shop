package com.wanmi.sbc.setting.api.response.weibologinset;

import com.wanmi.sbc.setting.bean.vo.WeiboLoginSetVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>微信登录配置新增结果</p>
 * @author lq
 * @date 2019-11-05 16:17:06
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeiboLoginSetAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的微信登录配置信息
     */
    @ApiModelProperty(value = "已新增的微信登录配置信息")
    private WeiboLoginSetVO weiboLoginSetVO;
}
