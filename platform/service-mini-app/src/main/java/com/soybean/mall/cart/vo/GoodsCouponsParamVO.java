package com.soybean.mall.cart.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-20 10:43:00
 */
@Data
public class GoodsCouponsParamVO {
    /**
     * 商品sku
     */
    @NotEmpty
    List<String> goodsInfoIds;
}
