package com.wanmi.sbc.customer.api.request.mq;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.customer.bean.enums.FundsType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@ApiModel
@Data
public class CustomerFundsGrantAmountRequest {

    @NotBlank
    @ApiModelProperty(value = "会员ID")
    private String customerId;

    @NotNull
    @ApiModelProperty(value = "奖励金额")
    private BigDecimal amount;

    @NotNull
    @ApiModelProperty(value = "奖励发放时间")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime dateTime;

    @NotNull
    @ApiModelProperty(value = "账务类型")
    private FundsType type;

    @ApiModelProperty(value = "业务编号")
    private String businessId;

    /**
     * 会员账号
     */
    @ApiModelProperty(value = "会员账号")
    private String customerAccount;

    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
    private String customerName;

    /**
     * 是否是分销员
     */
    @ApiModelProperty(value = "是否是分销员，0：否 1：是")
    private Integer distributor = NumberUtils.INTEGER_ZERO;
}
