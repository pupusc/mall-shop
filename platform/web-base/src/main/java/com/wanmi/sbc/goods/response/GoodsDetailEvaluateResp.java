package com.wanmi.sbc.goods.response;

import com.wanmi.sbc.goods.api.response.goodsevaluate.GoodsEvaluateCountResponse;
import com.wanmi.sbc.goods.api.response.goodsevaluateimage.GoodsEvaluateImagePageResponse;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: sbc-micro-service
 * @description: 商品详情评价
 * @create: 2019-04-22 22:39
 **/
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDetailEvaluateResp {

    private GoodsEvaluateCountResponse countResponse;

    private GoodsEvaluateImagePageResponse imagePageResponse;
}