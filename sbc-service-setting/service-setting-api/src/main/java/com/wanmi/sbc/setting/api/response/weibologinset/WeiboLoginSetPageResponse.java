package com.wanmi.sbc.setting.api.response.weibologinset;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.setting.bean.vo.WeiboLoginSetVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>微信登录配置分页结果</p>
 * @author lq
 * @date 2019-11-05 16:17:06
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeiboLoginSetPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 微信登录配置分页结果
     */
    @ApiModelProperty(value = "微信登录配置分页结果")
    private MicroServicePage<WeiboLoginSetVO> weiboLoginSetVOPage;
}
