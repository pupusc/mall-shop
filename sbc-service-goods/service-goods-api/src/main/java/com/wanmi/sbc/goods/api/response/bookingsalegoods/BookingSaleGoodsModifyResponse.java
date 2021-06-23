package com.wanmi.sbc.goods.api.response.bookingsalegoods;

import com.wanmi.sbc.goods.bean.vo.BookingSaleGoodsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>预售商品信息修改结果</p>
 * @author dany
 * @date 2020-06-05 10:51:35
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingSaleGoodsModifyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的预售商品信息信息
     */
    @ApiModelProperty(value = "已修改的预售商品信息信息")
    private BookingSaleGoodsVO bookingSaleGoodsVO;
}
