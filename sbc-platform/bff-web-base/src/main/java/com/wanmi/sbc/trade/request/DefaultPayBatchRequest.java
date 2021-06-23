package com.wanmi.sbc.trade.request;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * <p>0元订单批量支付请求参数</p>
 * Created by of628-wenzhi on 2019-07-24-17:03.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class DefaultPayBatchRequest extends BaseRequest {

    private static final long serialVersionUID = 8495958692184027573L;

    /**
     * 0元订单单号集合
     */
    @NotEmpty
    @ApiModelProperty("0元订单单号集合")
    private List<String> tradeIds;

}
