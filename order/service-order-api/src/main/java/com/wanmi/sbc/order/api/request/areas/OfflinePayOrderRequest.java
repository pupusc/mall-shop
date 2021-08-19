package com.wanmi.sbc.order.api.request.areas;

import com.wanmi.sbc.order.bean.dto.PayOrderDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class OfflinePayOrderRequest implements Serializable {

    @ApiModelProperty(value = "线下付款信息")
    List<PayOrderDTO> payOrderVOS;
}

