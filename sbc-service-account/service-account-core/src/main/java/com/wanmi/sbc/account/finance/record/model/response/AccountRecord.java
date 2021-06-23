package com.wanmi.sbc.account.finance.record.model.response;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>对账单返回数据结构</p>
 * Created by of628-wenzhi on 2017-12-08-上午11:16.
 */
@Data
public class AccountRecord implements Serializable {

    private static final long serialVersionUID = 341254993882851745L;

    /**
     * 店铺id
     */
    private Long storeId;

    /**
     * 店铺名称
     */
    private String storeName;

    /**
     * 商家id
     */
    private Long supplierId;

    /**
     * 商家名称
     */
    private String supplierName;

    /**
     * 总计金额，单位元，格式："￥#,###.00"
     */
    private String totalAmount;

    /**
     * 存在各支付项金额的Map，key: 枚举PayWay的name, value: 金额，单位元，格式："￥#,###.00"
     */
    private Map<String, String> payItemAmountMap;
}
