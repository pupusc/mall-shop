package com.wanmi.sbc.goods.provider.impl.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.standard.StandardGoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.standard.*;
import com.wanmi.sbc.goods.api.response.standard.*;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.service.GoodsCateService;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.standard.model.root.StandardGoodsRel;
import com.wanmi.sbc.goods.standard.request.StandardQueryRequest;
import com.wanmi.sbc.goods.standard.response.StandardEditResponse;
import com.wanmi.sbc.goods.standard.response.StandardQueryResponse;
import com.wanmi.sbc.goods.standard.service.StandardGoodsService;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-08 15:27
 */
@Validated
@RestController
public class StandardGoodsQueryController implements StandardGoodsQueryProvider {

    @Autowired
    private StandardGoodsService standardGoodsService;

    @Autowired
    private GoodsCateService goodsCateService;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    /**
     * @param request 分页查询商品库 {@link StandardGoodsPageRequest}
     * @return
     */
    @Override
    public BaseResponse<StandardGoodsPageResponse> page(@RequestBody @Valid StandardGoodsPageRequest request) {
        StandardQueryRequest standardQueryRequest = KsBeanUtil.convert(request, StandardQueryRequest.class);
        StandardQueryResponse queryResponse = standardGoodsService.page(standardQueryRequest);
        StandardGoodsPageResponse pageResponse = new StandardGoodsPageResponse();
        if (CollectionUtils.isNotEmpty(queryResponse.getGoodsCateList())) {
            pageResponse.setGoodsCateList(KsBeanUtil.convert(queryResponse.getGoodsCateList(), GoodsCateVO.class));
        }
        if (CollectionUtils.isNotEmpty(queryResponse.getGoodsBrandList())) {
            pageResponse.setGoodsBrandList(KsBeanUtil.convert(queryResponse.getGoodsBrandList(), GoodsBrandVO.class));
        }
        if (CollectionUtils.isNotEmpty(queryResponse.getStandardSkuList())) {
            pageResponse.setStandardSkuList(KsBeanUtil.convert(queryResponse.getStandardSkuList(), StandardSkuVO
                    .class));
        }
        if (CollectionUtils.isNotEmpty(queryResponse.getStandardSkuSpecDetails())) {
            pageResponse.setStandardSkuSpecDetails(KsBeanUtil.convert(queryResponse.getStandardSkuSpecDetails(), StandardSkuSpecDetailRelVO
                    .class));
        }
        pageResponse.setUsedStandard(queryResponse.getUsedStandard());
        MicroServicePage<StandardGoodsVO> standardGoodsVOS = KsBeanUtil.convertPage(queryResponse.getStandardGoodsPage(), StandardGoodsVO.class);


        pageResponse.setStandardGoodsPage(standardGoodsVOS);
        return BaseResponse.success(pageResponse);
    }

    /**
     * @param request 分页查询商品库 {@link StandardGoodsPageRequest}
     * @return
     */
    @Override
    public BaseResponse<StandardGoodsPageResponse> simplePage(@RequestBody @Valid StandardGoodsPageRequest request) {
        StandardQueryRequest standardQueryRequest = KsBeanUtil.convert(request, StandardQueryRequest.class);
        Page<StandardGoods> page = standardGoodsService.simplePage(standardQueryRequest);
        StandardGoodsPageResponse pageResponse = new StandardGoodsPageResponse();
        pageResponse.setStandardGoodsPage(KsBeanUtil.convertPage(page, StandardGoodsVO.class));
        return BaseResponse.success(pageResponse);
    }

    /**
     * @param request 根据ID查询商品库 {@link StandardGoodsByIdRequest}
     * @return
     */
    @Override
    public BaseResponse<StandardGoodsByIdResponse> getById(@RequestBody @Valid StandardGoodsByIdRequest request) {
        StandardEditResponse standardEditResponse = standardGoodsService.findInfoById(request.getGoodsId());
        StandardGoodsByIdResponse response = KsBeanUtil.convert(standardEditResponse, StandardGoodsByIdResponse.class);
        StandardGoodsVO goods = response.getGoods();
        StandardGoods standardGoods = standardEditResponse.getGoods();
        if (Objects.nonNull(standardGoods) && Objects.nonNull(standardGoods.getCateId())){
            GoodsCate goodsCate = goodsCateService.findById(standardGoods.getCateId());
            response.getGoods().setCateName(Objects.nonNull(goodsCate) ? goodsCate.getCateName() : "");
        }
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<List<String>> getGoodsIdByStandardId(@RequestBody @Valid StandardGoodsByIdRequest request) {
        List<StandardGoodsRel> standardGoodsRelList = standardGoodsService.getGoodsIdByStandardId(request.getGoodsId());
        List<String> goodsIdList = standardGoodsRelList.stream().map(StandardGoodsRel::getGoodsId).collect(Collectors.toList());
        return BaseResponse.success(goodsIdList);
    }

    /**
     * @param request 列出已被导入的商品库ID {@link StandardGoodsGetUsedStandardRequest}
     * @return
     */
    @Override
    public BaseResponse<StandardGoodsGetUsedStandardResponse> getUsedStandard(@RequestBody @Valid StandardGoodsGetUsedStandardRequest request) {
        return BaseResponse.success(StandardGoodsGetUsedStandardResponse.builder()
                .standardIds(standardGoodsService.getUsedStandard(request.getStandardIds(), request.getStoreIds()))
                .build());
    }

    /**
     * @param request 列出已被导入的商品ID {@link StandardGoodsGetUsedGoodsRequest}
     * @return {@link StandardGoodsGetUsedGoodsResponse}
     */
    @Override
    public BaseResponse<StandardGoodsGetUsedGoodsResponse> getUsedGoods(@RequestBody @Valid StandardGoodsGetUsedGoodsRequest request) {
        return BaseResponse.success(StandardGoodsGetUsedGoodsResponse.builder()
                .goodsIds(standardGoodsService.getUsedGoods(request.getGoodsIds())).build());
    }

    /**
     * 列出已被导入的SKUID
     * @param request
     * @return
     */
    @Override
    public BaseResponse<StandardGoodsListUsedGoodsIdResponse> listUsedGoodsId(@RequestBody @Valid StandardGoodsListUsedGoodsIdRequest request) {
        List<String> goodsIds = standardGoodsService.getUsedGoodsId(request.getStandardIds(), request.getStoreIds());
        return BaseResponse.success(new StandardGoodsListUsedGoodsIdResponse(goodsIds));
    }


    /**
     * @param request 列出已被导入的商品库ID {@link StandardGoodsGetUsedStandardRequest}
     * @return
     */
    @Override
    public BaseResponse<StandardGoodsGetUsedStandardResponse> getNeedSynStandard(@RequestBody @Valid StandardGoodsGetUsedStandardRequest request) {
        return BaseResponse.success(StandardGoodsGetUsedStandardResponse.builder()
                .standardIds(standardGoodsService.getNeedSynStandard(request.getStandardIds(), request.getStoreIds(), request.getNeedSynchronize()))
                .build());
    }

    /**
     * @param request 列出已被导入的商品库ID {@link StandardGoodsGetUsedStandardRequest}
     * @return
     */
    @Override
    public BaseResponse<StandardGoodsRelByGoodsIdsResponse> listRelByGoodsIds(@RequestBody @Valid StandardGoodsRelByGoodsIdsRequest request) {
        return BaseResponse.success(StandardGoodsRelByGoodsIdsResponse.builder()
                .standardGoodsRelList(KsBeanUtil.convert(standardGoodsService.listRelByStandardIds(request.getStandardIds()), StandardGoodsRelVO.class))
                .build());
    }

    @Override
    public BaseResponse<StandardIdsByGoodsIdsResponse> listStandardIdsByGoodsIds(@RequestBody @Valid StandardIdsByGoodsIdsRequest request) {
        return BaseResponse.success(StandardIdsByGoodsIdsResponse.builder()
                .standardIds(standardGoodsService.getUsedStandardByGoodsIds(request.getGoodsIds())).build());
    }
}
