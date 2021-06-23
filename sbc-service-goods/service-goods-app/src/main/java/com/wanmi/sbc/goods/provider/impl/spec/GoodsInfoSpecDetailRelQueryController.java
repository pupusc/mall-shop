package com.wanmi.sbc.goods.provider.impl.spec;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelBySkuIdsRequest;
import com.wanmi.sbc.goods.api.response.spec.GoodsInfoSpecDetailRelByGoodsIdAndSkuIdResponse;
import com.wanmi.sbc.goods.api.response.spec.GoodsInfoSpecDetailRelBySkuIdsResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.service.GoodsInfoSpecDetailRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/11/13 15:09
 * @version: 1.0
 */
@RestController
@Validated
public class GoodsInfoSpecDetailRelQueryController implements GoodsInfoSpecDetailRelQueryProvider {

    @Autowired
    private GoodsInfoSpecDetailRelService goodsInfoSpecDetailRelService;

    /**
     * 根据spuid 和skuid查询
     * @param goodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest 包含：spu ID和sku ID {@link GoodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest }
     * @return {@link GoodsInfoSpecDetailRelByGoodsIdAndSkuIdResponse }
     */
    
    @Override
    public BaseResponse<GoodsInfoSpecDetailRelByGoodsIdAndSkuIdResponse> listByGoodsIdAndSkuId(@RequestBody @Valid GoodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest goodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest) {
        List<GoodsInfoSpecDetailRel>  goodsInfoSpecDetailRelList = goodsInfoSpecDetailRelService.findByGoodsIdAndGoodsInfoId(goodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest.getGoodsId(),goodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest.getGoodsInfoId());
        if (CollectionUtils.isEmpty(goodsInfoSpecDetailRelList)){
            return BaseResponse.success(new GoodsInfoSpecDetailRelByGoodsIdAndSkuIdResponse(Collections.EMPTY_LIST));
        }
        List<GoodsInfoSpecDetailRelVO> goodsInfoSpecDetailRelVOList = KsBeanUtil.convert(goodsInfoSpecDetailRelList,GoodsInfoSpecDetailRelVO.class);
        return BaseResponse.success(new GoodsInfoSpecDetailRelByGoodsIdAndSkuIdResponse(goodsInfoSpecDetailRelVOList));
    }

    /**
     * 根据多个SkuID查询
     * @param goodsInfoSpecDetailRelBySkuIdsRequest 包含：多个sku ID {@link GoodsInfoSpecDetailRelBySkuIdsRequest }
     * @return {@link GoodsInfoSpecDetailRelBySkuIdsResponse }
     */
    
    @Override
    public BaseResponse<GoodsInfoSpecDetailRelBySkuIdsResponse> listBySkuIds(@RequestBody @Valid GoodsInfoSpecDetailRelBySkuIdsRequest goodsInfoSpecDetailRelBySkuIdsRequest) {
        List<GoodsInfoSpecDetailRel>  goodsInfoSpecDetailRelList = goodsInfoSpecDetailRelService.findByGoodsInfoIds(goodsInfoSpecDetailRelBySkuIdsRequest.getGoodsInfoIds());
        if (CollectionUtils.isEmpty(goodsInfoSpecDetailRelList)){
            return BaseResponse.success(new GoodsInfoSpecDetailRelBySkuIdsResponse(Collections.EMPTY_LIST));
        }
        List<GoodsInfoSpecDetailRelVO> goodsInfoSpecDetailRelVOList = KsBeanUtil.convert(goodsInfoSpecDetailRelList,GoodsInfoSpecDetailRelVO.class);
        return BaseResponse.success(new GoodsInfoSpecDetailRelBySkuIdsResponse(goodsInfoSpecDetailRelVOList));
    }
}
