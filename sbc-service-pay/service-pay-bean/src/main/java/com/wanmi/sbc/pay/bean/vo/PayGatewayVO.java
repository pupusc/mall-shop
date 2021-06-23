package com.wanmi.sbc.pay.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.pay.bean.enums.IsOpen;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>支付网关VO</p>
 * Created by of628-wenzhi on 2018-08-10-下午3:42.
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayGatewayVO implements Serializable{
    private static final long serialVersionUID = -6618059067856724471L;

    @ApiModelProperty(value = "支付网关id")
    private Long id;

    /**
     * 网关名称
     */
    @ApiModelProperty(value = "网关名称")
    private PayGatewayEnum name;

    /**
     * 是否开启: 0关闭 1开启
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

    /**
     * 是否聚合支付
     */
    @ApiModelProperty(value = "是否聚合支付")
    private Boolean type;

    /**
     * 支付网关配置
     */
    @ApiModelProperty(value = "支付网关配置")
    private PayGatewayConfigVO config;

    /**
     * 支付项
     */
    @ApiModelProperty(value = "支付项")
    private List<PayChannelItemVO> payChannelItemList;
}
