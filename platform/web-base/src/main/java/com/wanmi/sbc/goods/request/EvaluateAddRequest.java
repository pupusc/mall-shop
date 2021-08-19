package com.wanmi.sbc.goods.request;

import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluateAddRequest;
import com.wanmi.sbc.goods.api.request.goodsevaluate.GoodsEvaluateAddRequest;
import com.wanmi.sbc.goods.api.request.goodsevaluateimage.GoodsEvaluateImageAddRequest;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

/**
 * @Auther: jiaojiao
 * @Date: 2019/3/7 09:56
 * @Description:
 */

@ApiModel
@Data
public class EvaluateAddRequest {

    private GoodsEvaluateAddRequest goodsEvaluateAddRequest;

    private List<GoodsEvaluateImageAddRequest> goodsEvaluateImageAddRequest;

    private StoreEvaluateAddRequest storeEvaluateAddRequestList;
}
