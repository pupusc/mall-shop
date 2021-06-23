package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p>根据授权码获取微信授权用户openId请求参数结构</p>
 * Created by of628-wenzhi on 2018-08-29-下午4:13.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WxCodeRequest extends PayBaseRequest {
    private static final long serialVersionUID = 6066988904907869630L;

    /**
     * 授权码
     */
    @ApiModelProperty(value = "授权码")
    private String code;


    /**
     * 商户id-boss端取默认值
     */
    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long storeId;
}
