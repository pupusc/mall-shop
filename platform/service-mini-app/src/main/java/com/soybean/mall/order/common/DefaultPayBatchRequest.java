package com.soybean.mall.order.common;

import com.wanmi.sbc.common.base.BaseRequest;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

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
    private List<String> tradeIds;

}
