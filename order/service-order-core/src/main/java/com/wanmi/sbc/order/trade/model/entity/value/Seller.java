package com.wanmi.sbc.order.trade.model.entity.value;

import com.wanmi.sbc.common.base.Operator;
import lombok.Data;

import java.io.Serializable;

/**
 * 商家
 * Created by Administrator on 2017/5/1.
 */
@Data
public class Seller implements Serializable{

    private static final long serialVersionUID = 1045093112069149841L;
    /**
     * 卖家ID
     */
    private String adminId;

    /**
     * 代理人Id，用于代客下单
     */
    private String proxyId;

    /**
     * 代理人名称，用于代客下单，相当于OptUserName
     */
    private String proxyName;



    public static Seller fromOperator(Operator operator) {
        Seller seller = new Seller();
        seller.setAdminId(operator.getAdminId());
        seller.setProxyId(operator.getUserId());
        seller.setProxyName(operator.getName());
        return seller;
    }
}
