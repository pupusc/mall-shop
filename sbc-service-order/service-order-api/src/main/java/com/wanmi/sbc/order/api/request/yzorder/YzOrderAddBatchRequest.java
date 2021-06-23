package com.wanmi.sbc.order.api.request.yzorder;

import com.wanmi.sbc.order.bean.dto.yzorder.YzOrderDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class YzOrderAddBatchRequest implements Serializable {

    private static final long serialVersionUID = -6006404105093513452L;

    @ApiModelProperty("有赞订单")
    private List<YzOrderDTO> yzOrders;

    @ApiModelProperty(value = "是否更新订单")
    private Boolean updateFlag;
}
