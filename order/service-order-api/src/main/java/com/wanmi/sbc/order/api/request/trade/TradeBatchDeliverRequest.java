package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.dto.TradeBatchDeliverDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradeBatchDeliverRequest implements Serializable {

    /**
     * 批量发货参数
     */
    @ApiModelProperty(value = "批量发货参数")
    @NotEmpty
    private List<TradeBatchDeliverDTO> batchDeliverDTOList;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    @NotNull
    private Operator operator;

}
