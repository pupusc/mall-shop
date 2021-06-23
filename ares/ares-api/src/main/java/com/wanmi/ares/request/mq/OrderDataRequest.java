package com.wanmi.ares.request.mq;

import com.wanmi.ares.base.BaseMqRequest;
import com.wanmi.ares.enums.DataSourceType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单数据池信息
 * Created by bail on 2017/10/18.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class OrderDataRequest extends BaseMqRequest {

    private static final long serialVersionUID = -2232209324098443248L;

    /**
     * 订单类别
     */
    private DataSourceType type;

    /**
     * 订单金额
     */
    private BigDecimal orderAmt;

    /**
     * 实际金额
     */
    private BigDecimal realAmt;

    /**
     * 用户id
     */
    private String customerId;

    /**
     * 订单的商品信息
     */
    private List<TradeItemRequest> itemRequestList;

    /**
     * 商家id
     */
    private String companyId;

}
