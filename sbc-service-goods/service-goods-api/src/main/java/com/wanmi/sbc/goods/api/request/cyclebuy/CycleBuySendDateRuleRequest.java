package com.wanmi.sbc.goods.api.request.cyclebuy;

import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CycleBuySendDateRuleRequest implements Serializable {

    private static final long serialVersionUID = 2666489175165559351L;

    /**
     * 配送周期
     */
    @ApiModelProperty(value = "配送周期")
    @NonNull
    private DeliveryCycle deliveryCycle;

    /**
     * 发货日期规则
     */
    @ApiModelProperty(value = "发货日期规则")
    @NotEmpty
    private List<String> rules;
}
