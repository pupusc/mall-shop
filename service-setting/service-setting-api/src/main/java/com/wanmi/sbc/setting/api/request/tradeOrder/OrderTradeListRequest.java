package com.wanmi.sbc.setting.api.request.tradeOrder;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class OrderTradeListRequest implements Serializable {
    private static final long serialVersionUID = -3354087848195632606L;

    private List<GoodsMonthRequest> list;
}
