package com.wanmi.sbc.pay.api.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>微信openId返回结果</p>
 * Created by of628-wenzhi on 2018-08-10-下午3:57.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WxOpenIdResponse implements Serializable {
    private static final long serialVersionUID = -1341532400056310570L;

    /**
     * 授权用户微信openId
     */
    @ApiModelProperty(value = "授权用户微信openId")
    private String openId;
}
