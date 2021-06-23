package com.wanmi.sbc.goods.provider.impl.info;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoDetailByGoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdResponse;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.reponse.GoodsInfoDetailResponse;
import com.wanmi.sbc.goods.info.reponse.GoodsInfoEditResponse;
import org.apache.commons.collections4.CollectionUtils;

import java.util.stream.Collectors;

/**
 * @author: wanggang
 * @createDate: 2018/11/8 15:15
 * @version: 1.0
 */
public class GoodsInfoConvert {

    private GoodsInfoConvert(){

    }

    /**
     * GoodsInfoEditResponse 对象 转换成 GoodsInfoViewByIdResponse 对象
     * @param editResponse
     * @return GoodsInfoViewByIdResponse 对象
     */
    public static GoodsInfoViewByIdResponse toGoodsInfoViewByGoodsInfoEditResponse(GoodsInfoEditResponse editResponse){
        GoodsInfoViewByIdResponse response = new GoodsInfoViewByIdResponse();
        response.setGoodsInfo(new GoodsInfoVO());
        KsBeanUtil.copyPropertiesThird(editResponse.getGoodsInfo(), response.getGoodsInfo());
        response.setGoods(new GoodsVO());
        KsBeanUtil.copyPropertiesThird(editResponse.getGoods(), response.getGoods());
        response.setGoodsSpecs(KsBeanUtil.convertList(editResponse.getGoodsSpecs(), GoodsSpecVO.class));
        response.setGoodsSpecDetails(KsBeanUtil.convertList(editResponse.getGoodsSpecDetails(), GoodsSpecDetailVO.class));
        response.setImages(KsBeanUtil.convertList(editResponse.getImages(), GoodsImageVO.class));

        if (CollectionUtils.isNotEmpty(editResponse.getGoodsLevelPrices())) {
            response.setGoodsLevelPrices(KsBeanUtil.convertList(editResponse.getGoodsLevelPrices(),
                    GoodsLevelPriceVO.class));
        }
        if (CollectionUtils.isNotEmpty(editResponse.getGoodsCustomerPrices())) {
            response.setGoodsCustomerPrices(KsBeanUtil.convertList(editResponse.getGoodsCustomerPrices(),
                    GoodsCustomerPriceVO.class));
        }
        if (CollectionUtils.isNotEmpty(editResponse.getGoodsIntervalPrices())) {
            response.setGoodsIntervalPrices(KsBeanUtil.convertList(editResponse.getGoodsIntervalPrices(),
                    GoodsIntervalPriceVO.class));
        }
        return response;
    }


    /**
     * GoodsInfoDetailResponse 对象 转换成 GoodsInfoDetailByGoodsInfoResponse 对象
     * @param goodsInfoDetailResponse
     * @return
     */
    public static GoodsInfoDetailByGoodsInfoResponse toGoodsInfoDetailByGoodsInfoResponse(GoodsInfoDetailResponse goodsInfoDetailResponse){
        GoodsInfoDetailByGoodsInfoResponse goodsInfoDetailByGoodsInfoResponse = new GoodsInfoDetailByGoodsInfoResponse();
        KsBeanUtil.copyPropertiesThird(goodsInfoDetailResponse, goodsInfoDetailByGoodsInfoResponse);
        GoodsInfoVO goodsInfoVO = new GoodsInfoVO();
        GoodsInfo goodsInfo = goodsInfoDetailResponse.getGoodsInfo();
        KsBeanUtil.copyPropertiesThird(goodsInfo, goodsInfoVO);
        goodsInfoVO.setMarketingLabels(goodsInfo.getMarketingLabels().stream().map(marketingLabel -> {
            MarketingLabelVO marketingLabelVO = new MarketingLabelVO();
            KsBeanUtil.copyPropertiesThird(marketingLabel, marketingLabelVO);
            return marketingLabelVO;
        }).collect(Collectors.toList()));
        goodsInfoDetailByGoodsInfoResponse.setGoodsInfo(goodsInfoVO);
        GoodsVO goodsVO = new GoodsVO();
        KsBeanUtil.copyPropertiesThird(goodsInfoDetailResponse.getGoods(), goodsVO);
        goodsInfoDetailByGoodsInfoResponse.setGoods(goodsVO);
        goodsInfoDetailByGoodsInfoResponse.setGoodsPropDetailRels(KsBeanUtil.convertList(goodsInfoDetailResponse.getGoodsPropDetailRels(), GoodsPropDetailRelVO.class));
        goodsInfoDetailByGoodsInfoResponse.setGoodsSpecs(KsBeanUtil.convertList(goodsInfoDetailResponse.getGoodsSpecs(), GoodsSpecVO.class));
        goodsInfoDetailByGoodsInfoResponse.setGoodsSpecDetails(KsBeanUtil.convertList(goodsInfoDetailResponse.getGoodsSpecDetails(), GoodsSpecDetailVO.class));
        goodsInfoDetailByGoodsInfoResponse.setGoodsLevelPrices(KsBeanUtil.convertList(goodsInfoDetailResponse.getGoodsLevelPrices(), GoodsLevelPriceVO.class));
        goodsInfoDetailByGoodsInfoResponse.setGoodsCustomerPrices(KsBeanUtil.convertList(goodsInfoDetailResponse.getGoodsCustomerPrices(), GoodsCustomerPriceVO.class));
        goodsInfoDetailByGoodsInfoResponse.setGoodsIntervalPrices(KsBeanUtil.convertList(goodsInfoDetailResponse.getGoodsIntervalPrices(), GoodsIntervalPriceVO.class));
        goodsInfoDetailByGoodsInfoResponse.setImages(KsBeanUtil.convertList(goodsInfoDetailResponse.getImages(), GoodsImageVO.class));
        return goodsInfoDetailByGoodsInfoResponse;
    }
}
