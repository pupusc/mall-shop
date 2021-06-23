package com.wanmi.sbc.goodsEvaluate.response;

import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateCountResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateListResponse;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: sbc-micro-service
 * @description:
 * @create: 2019-05-20 16:16
 **/
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsDetailEvaluateTop3Resp {

    private GoodsEvaluateCountResponse goodsEvaluateCountResponse;

    private GoodsEvaluateListResponse listResponse;
}