package com.wanmi.sbc.goods.price.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品区间价
 * Created by zhangjin on 2017/5/17.
 */
@Data
@Builder
public class GoodsIntervalResponse implements Serializable{

    //订货区间
    private Long count;

    //区间价格
    private BigDecimal intervalPrice;
}
