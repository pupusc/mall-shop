package com.wanmi.sbc.order.api.response.payorder;

import com.wanmi.sbc.order.bean.vo.PayOrderDetailVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FindPayOrderListResponse implements Serializable {


    private static final long serialVersionUID = 8125196652267065157L;

    @ApiModelProperty("支付单集合")
    private List<PayOrderDetailVO> payOrders;
}
