package com.wanmi.sbc.goods.bean.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>预售VO</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingVO implements Serializable {
    private static final long serialVersionUID = 1L;

   private BookingSaleVO bookingSale;

   private BookingSaleGoodsVO bookingSaleGoods;

}