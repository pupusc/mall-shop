package com.wanmi.sbc.goods.api.response.goodsevaluateimage;

import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateImageVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）商品评价图片信息response</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluateImageByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品评价图片信息
     */
    private GoodsEvaluateImageVO goodsEvaluateImageVO;
}
