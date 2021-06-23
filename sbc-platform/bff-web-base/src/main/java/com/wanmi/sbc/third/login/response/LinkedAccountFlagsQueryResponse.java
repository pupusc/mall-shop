package com.wanmi.sbc.third.login.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 关联账号状态（目前只有微信，后期可以扩展qq、微博）
 * Created by gaomuwei on 2018/8/15.
 */
@ApiModel
@Data
@Builder
public class LinkedAccountFlagsQueryResponse {

    /**
     * 微信绑定状态
     */
    @ApiModelProperty(value = "微信绑定状态")
    private Boolean wxFlag;

    @ApiModelProperty(value = "头像路径")
    private String headimgurl;

    @ApiModelProperty(value = "昵称")
    private String nickname;

}


