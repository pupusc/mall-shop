package com.wanmi.sbc.goods.api.response.bookingsalegoods;

import com.wanmi.sbc.goods.bean.vo.BookingSaleGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）预售商品信息信息response</p>
 * @author dany
 * @date 2020-06-05 10:51:35
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleGoodsByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 预售商品信息信息
     */
    @ApiModelProperty(value = "预售商品信息信息")
    private BookingSaleGoodsVO bookingSaleGoodsVO;
}
