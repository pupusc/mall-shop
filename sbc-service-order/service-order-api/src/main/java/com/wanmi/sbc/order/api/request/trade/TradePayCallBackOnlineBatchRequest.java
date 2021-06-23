package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.order.bean.dto.TradePayCallBackOnlineDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * <p>订单线上支付批量回调请求参数</p>
 * Created by of628-wenzhi on 2019-07-25-16:06.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class TradePayCallBackOnlineBatchRequest extends BaseRequest {
    private static final long serialVersionUID = 6606463403594041365L;

    private List<TradePayCallBackOnlineDTO> requestList;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;
}
