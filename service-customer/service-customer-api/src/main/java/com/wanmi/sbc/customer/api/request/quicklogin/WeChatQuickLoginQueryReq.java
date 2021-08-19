package com.wanmi.sbc.customer.api.request.quicklogin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2020-05-22 16:20
 **/
@Data
@ApiModel
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeChatQuickLoginQueryReq implements Serializable {

    private static final long serialVersionUID = 2564066426394799737L;

    @ApiModelProperty(value = "应用内用户唯一标示")
    private String openId;

}