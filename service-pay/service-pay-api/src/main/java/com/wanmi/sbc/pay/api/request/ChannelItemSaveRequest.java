package com.wanmi.sbc.pay.api.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.pay.bean.enums.TerminalType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>新增支付渠道request</p>
 * Created by of628-wenzhi on 2018-08-13-下午4:23.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChannelItemSaveRequest extends PayBaseRequest {
    private static final long serialVersionUID = 6211617366001265668L;

    @ApiModelProperty(value = "支付渠道id")
    private Long id;

    /**
     * 支付项名称
     */
    @ApiModelProperty(value = "支付项名称")
    private String name;

    /**
     * 支付渠道别称
     */
    @ApiModelProperty(value = "支付渠道别称")
    private String channel;

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
     * 支付网关id
     */
    @ApiModelProperty(value = "支付网关id")
    @NotNull
    private Long gatewayId;

    /**
     * 是否开启:0关闭 1开启
     */
    @ApiModelProperty(value = "是否开启")
    private IsOpen isOpen;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;
}
