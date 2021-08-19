package com.wanmi.sbc.order.request;

import com.wanmi.sbc.common.enums.DefaultFlag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午3:28 2019/3/6
 * @Description:
 */
@ApiModel
@Data
public class ImmediateBuyRequest {

    /**
     * 批发商品
     */
    @Valid
    @NotEmpty
    private List<TradeItemRequest> tradeItemRequests;

    /**
     * 是否开店礼包
     */
    @ApiModelProperty("是否开店礼包")
    private DefaultFlag storeBagsFlag;

}
