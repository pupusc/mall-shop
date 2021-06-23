package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>根据id获取单个支付渠道request</p>
 * Created by of628-wenzhi on 2018-08-13-下午4:18.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelItemByIdRequest extends PayBaseRequest{

    private static final long serialVersionUID = -1033923130530299101L;

    /**
     * 支付渠道id
     */
    @ApiModelProperty(value = "支付渠道id")
    @NotNull
    private Long channelItemId;
}
