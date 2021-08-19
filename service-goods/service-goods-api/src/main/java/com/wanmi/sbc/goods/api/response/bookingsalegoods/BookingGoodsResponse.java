package com.wanmi.sbc.goods.api.response.bookingsalegoods;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.BookingSaleGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>魔方H5预售商品response</p>
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingGoodsResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 预售商品信息
     */
    @ApiModelProperty(value = "预售商品信息")
    private MicroServicePage<BookingSaleGoodsVO> page;
}
