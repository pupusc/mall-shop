package com.wanmi.sbc.goods.provider.impl.ares;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.ares.GoodsAresQueryProvider;
import com.wanmi.sbc.goods.api.request.ares.GoodsAresListByGoodsIdRequest;
import com.wanmi.sbc.goods.api.request.ares.GoodsAresListByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.request.ares.GoodsAresListByGoodsInfoIdsRequest;
import com.wanmi.sbc.goods.api.response.ares.GoodsAresListByGoodsIdResponse;
import com.wanmi.sbc.goods.api.response.ares.GoodsAresListByGoodsIdsResponse;
import com.wanmi.sbc.goods.api.response.ares.GoodsAresListByGoodsInfoIdsResponse;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.ares.GoodsAresService;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
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
 * @createDate: 2018/11/7 13:53
 * @version: 1.0
 */
@RestController
@Validated
public class GoodsAresQueryController implements GoodsAresQueryProvider {

    @Autowired
    private GoodsAresService goodsAresService;

    /**
     * 根据spuId获取Sku
     * @param goodsAresListByGoodsIdRequest {@link GoodsAresListByGoodsIdRequest }
     * @return {@link GoodsAresListByGoodsIdResponse }
     */

    @Override
    public BaseResponse<GoodsAresListByGoodsIdResponse> listByGoodsId(@RequestBody @Valid GoodsAresListByGoodsIdRequest goodsAresListByGoodsIdRequest) {
        List<GoodsInfo> goodsInfoList = goodsAresService.findSkuByGoodsId(goodsAresListByGoodsIdRequest.getGoodsId());
        if (CollectionUtils.isEmpty(goodsInfoList)){
            return BaseResponse.success(new GoodsAresListByGoodsIdResponse(Collections.EMPTY_LIST));
        }
        List<GoodsInfoVO> goodsInfoVOList = GoodsAresConvert.toVO(goodsInfoList);
        return BaseResponse.success(new GoodsAresListByGoodsIdResponse(goodsInfoVOList));
    }

    /**
     * 根据spuIdList获取Sku
     * @param goodsAresListByGoodsIdsRequest {@link GoodsAresListByGoodsIdsRequest }
     * @return {@link GoodsAresListByGoodsIdsResponse }
     */

    @Override
    public BaseResponse<GoodsAresListByGoodsIdsResponse> listByGoodsIds(@RequestBody @Valid GoodsAresListByGoodsIdsRequest goodsAresListByGoodsIdsRequest) {
        List<GoodsInfo> goodsInfoList = goodsAresService.findSkuByGoodsIds(goodsAresListByGoodsIdsRequest.getGoodsIds());
        if (CollectionUtils.isEmpty(goodsInfoList)){
            return BaseResponse.success(new GoodsAresListByGoodsIdsResponse(Collections.EMPTY_LIST));
        }
        List<GoodsInfoVO> goodsInfoVOList = GoodsAresConvert.toVO(goodsInfoList);
        return BaseResponse.success(new GoodsAresListByGoodsIdsResponse(goodsInfoVOList));
    }


    /**
     * 根据skuIdList获取Sku
     * @param goodsAresListByGoodsInfoIdsRequest {@link GoodsAresListByGoodsInfoIdsRequest }
     * @return {@link GoodsAresListByGoodsInfoIdsResponse }
     */

    @Override
    public BaseResponse<GoodsAresListByGoodsInfoIdsResponse> listByGoodsInfoIds(@RequestBody @Valid GoodsAresListByGoodsInfoIdsRequest goodsAresListByGoodsInfoIdsRequest) {
        List<GoodsInfo> goodsInfoList = goodsAresService.findSkuByGoodsInfoIds(goodsAresListByGoodsInfoIdsRequest.getGoodsInfoIds());
        if (CollectionUtils.isEmpty(goodsInfoList)){
            return BaseResponse.success(new GoodsAresListByGoodsInfoIdsResponse(Collections.EMPTY_LIST));
        }
        List<GoodsInfoVO> goodsInfoVOList = GoodsAresConvert.toVO(goodsInfoList);
        return BaseResponse.success(new GoodsAresListByGoodsInfoIdsResponse(goodsInfoVOList));
    }
}
