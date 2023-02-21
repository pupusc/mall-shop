package com.wanmi.sbc.setting.api.request.tradeOrder;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class GoodsMonthRequest implements Serializable {
    private static final long serialVersionUID = -3354087848195632606L;

    private String id;

    private String goodsInfoId;

    private Integer statMonth;

    private BigDecimal payCount;

    private BigDecimal payNum;

    private BigDecimal payMoney;

    private LocalDateTime creatTM;
}
