package com.wanmi.sbc.order.api.response.payorder;

import com.wanmi.sbc.order.bean.vo.PayOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class FindPayOrderByPayOrderIdsResponse  implements Serializable {

    @ApiModelProperty(value = "支付单列表")
    List<PayOrderVO> orders;
}
