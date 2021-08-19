package com.wanmi.sbc.pay.api.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.pay.bean.enums.TradeStatus;
import com.wanmi.sbc.pay.bean.enums.TradeType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>渠道方交易回调请求参数</p>
 * Created by of628-wenzhi on 2017-08-07-下午2:39.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradeCallbackRequest extends PayBaseRequest {

    private static final long serialVersionUID = 5333124286528694569L;

    /**
     * 交易完成时间
     */
    @ApiModelProperty(value = "交易完成时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime finishTime;

    /**
     * 交易类型
     */
    @ApiModelProperty(value = "交易类型")
    @NotNull
    private TradeType tradeType;

    /**
     * 交易状态
     */
    @ApiModelProperty(value = "交易状态")
    @NotNull
    private TradeStatus tradeStatus;

    /**
     * 交易量（笔）
     */
    @ApiModelProperty(value = "交易量（笔）")
    @NotNull
    private Integer tradeCount = 1;

    /**
     * 实际交易金额
     */
    @ApiModelProperty(value = "实际交易金额")
    @NotNull
    private BigDecimal amount;

    /**
     * 交易对象id
     */
    @ApiModelProperty(value = "交易对象id")
    @NotNull
    private String objectId;

}
