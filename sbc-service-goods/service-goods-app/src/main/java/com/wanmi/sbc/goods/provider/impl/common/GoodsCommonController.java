package com.wanmi.sbc.goods.provider.impl.common;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.common.GoodsCommonProvider;
import com.wanmi.sbc.goods.api.request.common.GoodsCommonBatchAddRequest;
import com.wanmi.sbc.goods.api.request.common.GoodsCommonModifyRequest;
import com.wanmi.sbc.goods.api.response.common.GoodsCommonBatchAddResponse;
import com.wanmi.sbc.goods.common.GoodsCommonService;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.mq.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/11/2 9:52
 * @version: 1.0
 */
@RestController
@Validated
public class GoodsCommonController implements GoodsCommonProvider {

    @Autowired
    private GoodsCommonService goodsCommonService;

    @Autowired
    private ProducerService producerService;

    /**
     * 新增/编辑操作中，商品审核状态
     * @param goodsCommonModifyRequest {@link GoodsCommonModifyRequest }
     * @return
     */
    @Override
    public BaseResponse modifyCheckState(@RequestBody @Valid GoodsCommonModifyRequest goodsCommonModifyRequest){
        Goods goods = new Goods();
        KsBeanUtil.copyPropertiesThird(goodsCommonModifyRequest,goods);
        goodsCommonService.setCheckState(goods);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 商品批量新增
     * @param request 商品以及相关的批量信息 {@link GoodsCommonBatchAddRequest }
     * @return 批量新增的商品skuId {@link GoodsCommonBatchAddResponse }
     */
    @Override
    public BaseResponse<GoodsCommonBatchAddResponse> batchAdd(@RequestBody @Valid GoodsCommonBatchAddRequest
                                                                       request) {
        List<String> skuIds = goodsCommonService.batchAdd(request);
        producerService.initGoodsBySkuIds(skuIds);
        return BaseResponse.success(GoodsCommonBatchAddResponse.builder()
                .skuNoList(skuIds).build());
    }

}
