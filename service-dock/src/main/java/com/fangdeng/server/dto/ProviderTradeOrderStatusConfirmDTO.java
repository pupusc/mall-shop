package com.fangdeng.server.dto;


import com.fangdeng.server.enums.DeliveryStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProviderTradeOrderStatusConfirmDTO implements Serializable {
    private static final long serialVersionUID = 7658912644424704644L;

    @ApiModelProperty("平台单号")
    private String platformCode;

    @ApiModelProperty("发货状态")
    private DeliveryStatus deliveryStatus;

    @ApiModelProperty("已发货数据")
    private List<DeliveryInfoDTO> deliveryInfoList;

    @ApiModelProperty("取消商品信息")
    private List<GoodsItemDTO> cancelGoods;
}
