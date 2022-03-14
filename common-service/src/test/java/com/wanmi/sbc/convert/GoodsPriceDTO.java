package com.wanmi.sbc.convert;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * com.wanmi.sbc.goods.api.provider.info.GoodsPriceDTO
 *
 * @author lipeng
 * @dateTime 2018/11/8 下午5:47
 */
@Data
public class GoodsPriceDTO implements Serializable {

    private Integer id;

    private String name;

    private BigDecimal price;

}
