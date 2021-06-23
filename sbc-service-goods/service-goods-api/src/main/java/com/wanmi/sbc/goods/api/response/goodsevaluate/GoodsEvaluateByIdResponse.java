package com.wanmi.sbc.goods.api.response.goodsevaluate;

import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）商品评价信息response</p>
 * @author liutao
 * @date 2019-02-25 15:17:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluateByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品评价信息
     */
    private GoodsEvaluateVO goodsEvaluateVO;
}
