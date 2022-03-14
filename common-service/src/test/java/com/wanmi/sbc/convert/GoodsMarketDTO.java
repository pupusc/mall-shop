package com.wanmi.sbc.convert;

import lombok.Data;

import java.io.Serializable;

/**
 * com.wanmi.sbc.goods.api.provider.info.GoodsMarket
 *
 * @author lipeng
 * @dateTime 2018/11/8 下午5:46
 */
@Data
public class GoodsMarketDTO implements Serializable {

    private Integer id;

    private String name;

    private GoodsPriceDTO goodsPriceDTO;
}
