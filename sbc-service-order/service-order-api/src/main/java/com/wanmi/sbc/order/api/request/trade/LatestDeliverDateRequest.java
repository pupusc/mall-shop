package com.wanmi.sbc.order.api.request.trade;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateSerializer;
import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class LatestDeliverDateRequest implements Serializable {

    private static final long serialVersionUID = 3976362856981544921L;

    /**
     * 推算的时间基准
     */
    @ApiModelProperty(value = "推算的时间基准")
    @JsonSerialize(using = CustomLocalDateSerializer.class)
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    @NonNull
    private LocalDate date;

    /**
     * 配送周期 0:每日一期 1:每周一期 2:每月一期
     */
    @ApiModelProperty(value = "配送周期 0:每日一期 1:每周一期 2:每月一期")
    @NonNull
    private DeliveryCycle deliveryCycle;

    /**
     * 发货时间
     */
    @ApiModelProperty(value = "发货时间")
    @NotBlank
    private String rule;
}
