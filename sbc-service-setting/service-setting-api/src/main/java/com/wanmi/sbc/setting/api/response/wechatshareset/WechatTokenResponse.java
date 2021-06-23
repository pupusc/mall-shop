package com.wanmi.sbc.setting.api.response.wechatshareset;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>微信token</p>
 * @author lq
 * @date 2019-11-05 16:15:54
 */
@Data
public class WechatTokenResponse {


    private Integer errcode;

    private String errmsg;

    private String access_token;

    private Integer expires_in;

}