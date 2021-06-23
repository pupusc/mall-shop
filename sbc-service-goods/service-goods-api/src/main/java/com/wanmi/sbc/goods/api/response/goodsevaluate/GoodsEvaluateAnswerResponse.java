package com.wanmi.sbc.goods.api.response.goodsevaluate;

import com.wanmi.sbc.goods.bean.vo.GoodsEvaluateVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>商品评价修改结果</p>
 * @author liutao
 * @date 2019-02-25 15:17:42
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsEvaluateAnswerResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已修改的商品评价信息
     */
    private GoodsEvaluateVO goodsEvaluateVO;
}
