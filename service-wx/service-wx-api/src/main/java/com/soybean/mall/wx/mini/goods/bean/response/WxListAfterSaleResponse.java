package com.soybean.mall.wx.mini.goods.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class WxListAfterSaleResponse extends WxResponseBase {
    @JSONField(name="after_sales_orders")
    private List<String> afterSalesOrders;

    @JSONField(name="has_more")
    private Boolean hasMore;
}
