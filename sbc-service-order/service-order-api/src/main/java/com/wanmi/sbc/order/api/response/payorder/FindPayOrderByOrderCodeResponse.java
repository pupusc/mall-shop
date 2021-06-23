package com.wanmi.sbc.order.api.response.payorder;

import com.wanmi.sbc.order.bean.vo.PayOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class FindPayOrderByOrderCodeResponse  implements Serializable {

    @ApiModelProperty(value = "支付单信息")
    PayOrderVO value;
}
