package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.order.bean.dto.TradeSettingDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/12/3 11:12
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class TradeSettingModifyRequest implements Serializable {
    private static final long serialVersionUID = -2453210155688012532L;

    @ApiModelProperty(value = "订单设置请求信息")
    private List<TradeSettingDTO> tradeSettingRequests;
}
