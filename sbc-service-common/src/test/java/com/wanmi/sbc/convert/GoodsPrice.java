package com.wanmi.sbc.convert;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * com.wanmi.sbc.goods.api.provider.info.GoodsPrice
 *
 * @author lipeng
 * @dateTime 2018/11/8 下午5:50
 */
@Data
public class GoodsPrice implements Serializable {

    private Integer id;

    private String name;

    private BigDecimal price;
}
