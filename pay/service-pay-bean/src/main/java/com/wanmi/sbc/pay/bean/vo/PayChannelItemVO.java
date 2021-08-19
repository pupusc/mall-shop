package com.wanmi.sbc.pay.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.pay.bean.enums.TerminalType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>支付渠道项VO</p>
 * Created by of628-wenzhi on 2018-08-10-下午3:45.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayChannelItemVO implements Serializable{
    private static final long serialVersionUID = -4474625038321861738L;

    @ApiModelProperty(value = "支付渠道项id")
    private Long id;

    /**
     * 支付项名称
     */
    @ApiModelProperty(value = "支付项名称")
    private String name;

    /**
     * 支付渠道
     */
    @ApiModelProperty(value = "支付渠道")
    private String channel;

    /**
     * 是否开启:0关闭 1开启
     */
    @ApiModelProperty(value = "是否开启")
    private IsOpen isOpen;

    /**
     * 终端: 0 pc 1 h5  2 app
     */
    @ApiModelProperty(value = "终端")
    private TerminalType terminal;

    /**
     * 支付项代码，同一支付网关唯一
     */
    @ApiModelProperty(value = "支付项代码，同一支付网关唯一")
    private String code;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    /**
     * 支付网关
     */
    @ApiModelProperty(value = "支付网关")
    private PayGatewayVO gateway;
}
