package com.wanmi.sbc.goods.api.response.goodsevaluateimage;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateImageVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>商品评价图片分页结果</p>
 * @author liutao
 * @date 2019-02-26 09:56:17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluateImagePageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品评价图片分页结果
     */
    private MicroServicePage<GoodsEvaluateImageVO> goodsEvaluateImageVOPage;
}
