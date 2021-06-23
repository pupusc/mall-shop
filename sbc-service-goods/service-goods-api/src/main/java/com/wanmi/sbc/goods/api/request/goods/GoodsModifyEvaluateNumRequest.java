package com.wanmi.sbc.goods.api.request.goods;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName GoodsModifyPositiveFeedbackRequest
 * @Description 更新商品好评率Request
 * @Author lvzhenwei
 * @Date 2019/4/11 15:50
 **/
@Data
public class GoodsModifyEvaluateNumRequest implements Serializable {

    private static final long serialVersionUID = 6326580073387240604L;

    /**
     * 商品编号
     */
    @NotNull
    private String goodsId;

    /**
     * 商品评分
     */
    @NotNull
    private Integer evaluateScore;
}
