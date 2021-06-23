package com.wanmi.sbc.order.api.request.areas;

import com.wanmi.sbc.order.bean.dto.PayOrderDTO;
import com.wanmi.sbc.order.bean.vo.PayOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class PayOrderRequest implements Serializable {

    @ApiModelProperty(value = "付款单信息")
    PayOrderDTO payOrderVO;
}
