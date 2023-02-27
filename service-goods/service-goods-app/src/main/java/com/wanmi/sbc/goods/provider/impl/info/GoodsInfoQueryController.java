package com.wanmi.sbc.goods.provider.impl.info;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterpriseGoodsInfoPageRequest;
import com.wanmi.sbc.goods.api.request.goodslabel.GoodsLabelQueryRequest;
import com.wanmi.sbc.goods.api.request.info.DistributionGoodsChangeRequest;
import com.wanmi.sbc.goods.api.request.info.DistributionGoodsPageRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoCountByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoMarketingPriceByNosRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoPageRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoPartColsByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoStoreIdBySkuIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewPageRequest;
import com.wanmi.sbc.goods.api.response.enterprise.EnterpriseGoodsInfoPageResponse;
import com.wanmi.sbc.goods.api.response.info.DistributionGoodsInfoPageResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByGoodsIdresponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoCountByConditionResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByConditionResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoMarketingPriceByNosResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoPageResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoPartColsByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoStoreIdBySkuIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewPageResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoMarketingPriceDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLabelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.common.SystemPointsConfigService;
import com.wanmi.sbc.goods.goodslabel.model.root.GoodsLabel;
import com.wanmi.sbc.goods.goodslabel.service.GoodsLabelService;
import com.wanmi.sbc.goods.info.model.entity.GoodsMarketingPrice;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.reponse.DistributionGoodsQueryResponse;
import com.wanmi.sbc.goods.info.reponse.EnterPriseGoodsQueryResponse;
import com.wanmi.sbc.goods.info.reponse.GoodsInfoEditResponse;
import com.wanmi.sbc.goods.info.reponse.GoodsInfoResponse;
import com.wanmi.sbc.goods.info.request.DistributionGoodsQueryRequest;
import com.wanmi.sbc.goods.info.request.EnterpriseGoodsQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsInfoRequest;
import com.wanmi.sbc.goods.info.service.GoodsCacheService;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.LinkedMallGoodsService;
import com.wanmi.sbc.goods.redis.RedisService;
import com.wanmi.sbc.goods.spec.service.GoodsInfoSpecDetailRelService;
import com.wanmi.sbc.goods.util.mapper.GoodsInfoMapper;
import com.wanmi.sbc.goods.util.mapper.GoodsMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>对商品sku查询接口</p>
 * Created by daiyitian on 2018-11-5-下午6:23.
 */
@RestController
@Validated
public class GoodsInfoQueryController implements GoodsInfoQueryProvider {

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;

    @Autowired
    private GoodsCacheService goodsCacheService;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsLabelService goodsLabelService;

    @Autowired
    private LinkedMallGoodsService linkedMallGoodsService;

    @Autowired
    private GoodsInfoSpecDetailRelService goodsInfoSpecDetailRelService;

    @Autowired
    private RedisService redisService;

    /**
     * 分页查询商品sku视图列表
     *
     * @param request 商品sku视图分页条件查询结构 {@link GoodsInfoViewPageRequest}
     * @return 商品sku视图分页列表 {@link GoodsInfoViewPageResponse}
     */
    @Override
    public BaseResponse<GoodsInfoViewPageResponse> pageView(@RequestBody @Valid GoodsInfoViewPageRequest request) {
        GoodsInfoQueryRequest queryRequest = new GoodsInfoQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);
        // 代客下单时，积分开关开启 并且 积分使用方式是订单抵扣，此时不需要过滤积分价商品
        if (Objects.nonNull(request.getIntegralPriceFlag()) && Objects.equals(Boolean.TRUE, request.getIntegralPriceFlag())
                && !systemPointsConfigService.isGoodsPoint()) {
            queryRequest.setIntegralPriceFlag(Boolean.FALSE);
        }
        GoodsInfoResponse pageResponse = goodsInfoService.pageView(queryRequest);
        GoodsInfoViewPageResponse response = new GoodsInfoViewPageResponse();
        response.setGoodsInfoPage(KsBeanUtil.convertPage(pageResponse.getGoodsInfoPage(), GoodsInfoVO.class));

        //当不需示强制显示积分时，在设置未开启商品抵扣下清零buyPoint
        if (CollectionUtils.isNotEmpty(response.getGoodsInfoPage().getContent())) {
            systemPointsConfigService.clearBuyPoinsForSkus(response.getGoodsInfoPage().getContent());
        }
        if (CollectionUtils.isNotEmpty(pageResponse.getGoodses())) {
            response.setGoodses(KsBeanUtil.convertList(pageResponse.getGoodses(), GoodsVO.class));
        }
        if (CollectionUtils.isNotEmpty(pageResponse.getBrands())) {
            response.setBrands(KsBeanUtil.convertList(pageResponse.getBrands(), GoodsBrandVO.class));
        }
        if (CollectionUtils.isNotEmpty(pageResponse.getCates())) {
            response.setCates(KsBeanUtil.convertList(pageResponse.getCates(), GoodsCateVO.class));
        }
        return BaseResponse.success(response);
    }

    /**
     * 分页查询商品sku列表
     *
     * @param request 商品sku分页条件查询结构 {@link GoodsInfoPageRequest}
     * @return 商品sku分页列表 {@link GoodsInfoPageResponse}
     */
    @Override
    public BaseResponse<GoodsInfoPageResponse> page(@RequestBody @Valid GoodsInfoPageRequest request) {
        GoodsInfoQueryRequest queryRequest = new GoodsInfoQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);
        GoodsInfoPageResponse response = new GoodsInfoPageResponse();
        response.setGoodsInfoPage(KsBeanUtil.convertPage(goodsInfoService.page(queryRequest), GoodsInfoVO.class));
        //当不需示强制显示积分时，在设置未开启商品抵扣下清零buyPoint
        if ((!Boolean.TRUE.equals(request.getShowPointFlag())) && CollectionUtils.isNotEmpty(response.getGoodsInfoPage().getContent())) {
            systemPointsConfigService.clearBuyPoinsForSkus(response.getGoodsInfoPage().getContent());
        }
        return BaseResponse.success(response);
    }

    /**
     * 根据商品skuId批量查询商品sku视图列表
     *
     * @param request 根据批量商品skuId查询结构 {@link GoodsInfoViewByIdsRequest}
     * @return 商品sku视图列表 {@link GoodsInfoViewByIdsResponse}
     */
    @Override
    public BaseResponse<GoodsInfoViewByIdsResponse> listViewByIds(@RequestBody @Valid GoodsInfoViewByIdsRequest
                                                                          request) {
        GoodsInfoRequest infoRequest = goodsInfoMapper.goodsInfoViewByIdsRequestToGoodsInfoRequest(request);
        GoodsInfoResponse infoResponse = goodsInfoService.findSkuByIds(infoRequest);
        GoodsInfoViewByIdsResponse response = GoodsInfoViewByIdsResponse.builder()
                .goodsInfos(goodsInfoMapper.goodsInfosToGoodsInfoVOs(infoResponse.getGoodsInfos()))
                .goodses(goodsMapper.goodsListToGoodsVOList(infoResponse.getGoodses())).build();
        //在设置未开启商品抵扣下清零buyPoint
        systemPointsConfigService.clearBuyPoinsForSkus(response.getGoodsInfos());

        //控制是否显示商品标签
        if (Boolean.TRUE.equals(request.getShowLabelFlag())) {
            goodsLabelService.fillGoodsLabel(response.getGoodses(), request.getShowSiteLabelFlag());
        }

        if (CollectionUtils.isNotEmpty(response.getGoodses())) {
            for (GoodsVO item : response.getGoodses()) {
                if (StringUtils.isNotBlank(item.getGoodsChannelType())) {
                    item.setGoodsChannelTypeSet(Arrays.asList(item.getGoodsChannelType().split(",")));
                }
            }
        }

        if (CollectionUtils.isNotEmpty(response.getGoodsInfos())) {
            for (GoodsInfoVO item : response.getGoodsInfos()) {
                if (StringUtils.isNotBlank(item.getGoodsChannelType())) {
                    item.setGoodsChannelTypeSet(Arrays.asList(item.getGoodsChannelType().split(",")));
                }
            }
        }

        return BaseResponse.success(response);
    }

    /**
     * 根据商品skuId查询商品sku视图
     *
     * @param request 根据商品skuId查询结构 {@link GoodsInfoViewByIdRequest}
     * @return 商品sku视图 {@link GoodsInfoViewByIdResponse}
     */
    @Override
    public BaseResponse<GoodsInfoViewByIdResponse> getViewById(@RequestBody @Valid GoodsInfoViewByIdRequest request) {
        GoodsInfoEditResponse editResponse = goodsInfoService.findById(request.getGoodsInfoId());
        GoodsInfoViewByIdResponse response = GoodsInfoConvert.toGoodsInfoViewByGoodsInfoEditResponse(editResponse);
        //在设置未开启商品抵扣下清零buyPoint
        systemPointsConfigService.clearBuyPoinsForSku(response.getGoodsInfo());


        if (StringUtils.isNotBlank(response.getGoods().getLabelIdStr())) {
            String labelId=response.getGoods().getLabelIdStr().replace(",","");
            List<String> labelIds = Arrays.asList(labelId.split(""));
            if (CollectionUtils.isNotEmpty(labelIds)) {
                List<Long> result = labelIds.stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                List<GoodsLabel>  goodsLabels =  goodsLabelService.list(GoodsLabelQueryRequest.builder().goodsLabelIdList(result).build());
                if (CollectionUtils.isNotEmpty(goodsLabels)) {
                    response.getGoods().setGoodsLabelList(KsBeanUtil.convertList(goodsLabels,GoodsLabelVO.class));
                }
            }
        }


        return BaseResponse.success(response);
    }

    /**
     * 根据商品skuId批量查询商品sku列表
     *
     * @param request 根据批量商品skuId查询结构 {@link GoodsInfoListByIdsRequest}
     * @return 商品sku列表 {@link GoodsInfoListByIdsResponse}
     */
    @Override
    public BaseResponse<GoodsInfoListByIdsResponse> listByIds(@RequestBody @Valid GoodsInfoListByIdsRequest request) {
        List<GoodsInfo> goodsInfoList = goodsInfoService.findByIds(request.getGoodsInfoIds());
        if (CollectionUtils.isNotEmpty(goodsInfoList)) {
            goodsInfoService.fillGoodsStatus(goodsInfoList);
        }
        GoodsInfoListByIdsResponse response = GoodsInfoListByIdsResponse.builder()
                .goodsInfos(KsBeanUtil.convertList(goodsInfoList, GoodsInfoVO.class))
                .build();
        //填充供应商商品编码
        List<String> providerGoodsInfoIds = response.getGoodsInfos().stream()
                .map(GoodsInfoVO::getProviderGoodsInfoId)
                .filter(id -> Objects.nonNull(id)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(providerGoodsInfoIds)) {
            //供应商商品
            List<GoodsInfo> goodsInfos = goodsInfoService.findByIds(providerGoodsInfoIds);

            if (CollectionUtils.isNotEmpty(goodsInfos) && CollectionUtils.isNotEmpty(response.getGoodsInfos())) {
                response.getGoodsInfos().stream().forEach(goodsInfoVO -> {
                    goodsInfos.stream().filter(goodsInfo ->
                            goodsInfo.getGoodsInfoId().equals(goodsInfoVO.getProviderGoodsInfoId()))
                            .findFirst().ifPresent(g -> goodsInfoVO.setProviderGoodsInfoNo(g.getGoodsInfoNo()));
                });
            }
        }
        //在设置未开启商品抵扣下清零buyPoint
        systemPointsConfigService.clearBuyPoinsForSkus(response.getGoodsInfos());
        return BaseResponse.success(response);
    }

    /**
     * 根据商品skuId查询商品sku
     *
     * @param request 根据商品skuId查询结构 {@link GoodsInfoByIdRequest}
     * @return 商品sku {@link GoodsInfoByIdResponse}
     */
    @Override
    public BaseResponse<GoodsInfoByIdResponse> getById(@RequestBody @Valid GoodsInfoByIdRequest request) {
        GoodsInfoByIdResponse response = null;
        GoodsInfo goodsInfo;
        if (Objects.nonNull(request.getStoreId())) {
            goodsInfo = goodsInfoService.findByGoodsInfoIdAndStoreIdAndDelFlag(request.getGoodsInfoId(), request.getStoreId());
        } else {
            goodsInfo = goodsInfoService.findOne(request.getGoodsInfoId());
        }
        if (Objects.nonNull(goodsInfo)) {
            response = new GoodsInfoByIdResponse();
            KsBeanUtil.copyPropertiesThird(goodsInfo, response);
            goodsInfoService.updateGoodsInfoSupplyPriceAndStock(goodsInfo);
            response.setStock(goodsInfo.getStock());
        }
        //在设置未开启商品抵扣下清零buyPoint
        systemPointsConfigService.clearBuyPoinsForSku(response);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<Map<String, Object>> getSimpleById(List<String> goodsInfoIds) {
        return BaseResponse.success(goodsInfoService.findSimpleGoods(goodsInfoIds));
    }

    /**
     * 根据动态条件查询商品sku列表
     *
     * @param request 根据动态条件查询结构 {@link GoodsInfoListByConditionRequest}
     * @return 商品sku列表 {@link GoodsInfoListByConditionResponse}
     */
    @Override
    public BaseResponse<GoodsInfoListByConditionResponse> listByCondition(@RequestBody @Valid GoodsInfoListByConditionRequest
                                                                                  request) {
        GoodsInfoQueryRequest queryRequest = new GoodsInfoQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);
        GoodsInfoListByConditionResponse response = GoodsInfoListByConditionResponse.builder()
                .goodsInfos(KsBeanUtil.convertList(goodsInfoService.findByParams(queryRequest), GoodsInfoVO.class))
                .build();
        //当不需示强制显示积分时，在设置未开启商品抵扣下清零buyPoint
        if ((!Boolean.TRUE.equals(request.getShowPointFlag())) && CollectionUtils.isNotEmpty(response.getGoodsInfos())) {
            systemPointsConfigService.clearBuyPoinsForSkus(response.getGoodsInfos());
        }
        //填充规格明细
        if (Boolean.TRUE.equals(request.getShowSpecFlag())) {
            goodsInfoSpecDetailRelService.fillSpecDetail(response.getGoodsInfos());
        }
        //是否填充可售性
        if (Boolean.TRUE.equals(request.getShowVendibilityFlag())) {
            linkedMallGoodsService.fillGoodsVendibility(response.getGoodsInfos());
        }
        //供应商商品相关信息
        if (Boolean.TRUE.equals(request.getShowProviderInfoFlag())) {
            List<GoodsInfo> list = KsBeanUtil.convert(response.getGoodsInfos(), GoodsInfo.class);
            goodsInfoService.updateGoodsInfoSupplyPriceAndStock(list);
            response.setGoodsInfos(KsBeanUtil.convert(list, GoodsInfoVO.class));
        }

        //是否填充LM库存
        if(Boolean.TRUE.equals(request.getFillLmInfoFlag())){
            linkedMallGoodsService.fillLmStock(response.getGoodsInfos());
        }
        //填充库存
        if (CollectionUtils.isNotEmpty(response.getGoodsInfos())) {
            for (GoodsInfoVO goodsParam : response.getGoodsInfos()) {
                goodsParam.setMarketPrice(goodsParam.getMarketPrice() == null ? new BigDecimal("9999") : goodsParam.getMarketPrice());
                goodsParam.setSalePrice(goodsParam.getSalePrice() == null ? goodsParam.getMarketPrice() : goodsParam.getSalePrice());
                //获取冻结
                String freezeStockStr = redisService.getString(RedisKeyConstant.GOODS_INFO_STOCK_FREEZE_PREFIX + goodsParam.getGoodsInfoId());
                long freezeStock = StringUtils.isBlank(freezeStockStr) ? 0L : Long.parseLong(freezeStockStr);
                goodsParam.setTotalStock(freezeStock + goodsParam.getStock());
                goodsParam.setFreezeStock(freezeStock);
            }
        }
        return BaseResponse.success(response);
    }

    /**
     * 根据动态条件统计商品sku个数
     *
     * @param request 根据动态条件统计结构 {@link GoodsInfoCountByConditionRequest}
     * @return 商品sku个数 {@link GoodsInfoCountByConditionResponse}
     */
    @Override
    public BaseResponse<GoodsInfoCountByConditionResponse> countByCondition(@RequestBody @Valid
                                                                                    GoodsInfoCountByConditionRequest
                                                                                    request) {
        GoodsInfoQueryRequest queryRequest = new GoodsInfoQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);
        GoodsInfoCountByConditionResponse response = GoodsInfoCountByConditionResponse.builder()
                .count(goodsInfoService.count(queryRequest))
                .build();
        return BaseResponse.success(response);
    }

    /**
     * 分页查询分销商品sku视图列表
     *
     * @param request 分销商品sku视图分页条件查询结构 {@link DistributionGoodsPageRequest}
     * @return DistributionGoodsInfoPageResponse
     */
    @Override
    public BaseResponse<DistributionGoodsInfoPageResponse> distributionGoodsInfoPage(@RequestBody @Valid DistributionGoodsPageRequest request) {
        DistributionGoodsQueryRequest queryRequest = new DistributionGoodsQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);
        DistributionGoodsQueryResponse queryResponse = goodsInfoService.distributionGoodsPage(queryRequest);

        DistributionGoodsInfoPageResponse response = new DistributionGoodsInfoPageResponse();

        response.setGoodsInfoPage(KsBeanUtil.convertPage(queryResponse.getGoodsInfoPage(), GoodsInfoVO.class));
        if (CollectionUtils.isNotEmpty(queryResponse.getGoodsBrandList())) {
            response.setBrands(KsBeanUtil.convertList(queryResponse.getGoodsBrandList(), GoodsBrandVO.class));
        }
        if (CollectionUtils.isNotEmpty(queryResponse.getGoodsCateList())) {
            response.setCates(KsBeanUtil.convertList(queryResponse.getGoodsCateList(), GoodsCateVO.class));
        }
        if (CollectionUtils.isNotEmpty(queryResponse.getGoodsInfoSpecDetails())) {
            response.setGoodsInfoSpecDetails(KsBeanUtil.convertList(queryResponse.getGoodsInfoSpecDetails(),
                    GoodsInfoSpecDetailRelVO.class));
        }
        if (CollectionUtils.isNotEmpty(queryResponse.getCompanyInfoList())) {
            response.setCompanyInfoList(queryResponse.getCompanyInfoList());
        }

        //在设置未开启商品抵扣下清零buyPoint
        if (CollectionUtils.isNotEmpty(response.getGoodsInfoPage().getContent())) {
            systemPointsConfigService.clearBuyPoinsForSkus(response.getGoodsInfoPage().getContent());
        }

        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<GoodsInfoByGoodsIdresponse> getByGoodsId(@RequestBody @Valid DistributionGoodsChangeRequest request) {
        List<GoodsInfo> goodsInfos = goodsInfoService.queryBygoodsId(request.getGoodsId());
        List<GoodsInfoVO> goodsInfoVOS = KsBeanUtil.convertList(goodsInfos, GoodsInfoVO.class);

        if (CollectionUtils.isNotEmpty(goodsInfoVOS)) {
            //在设置未开启商品抵扣下清零buyPoint
            systemPointsConfigService.clearBuyPoinsForSkus(goodsInfoVOS);
            //填充规格明细
            if (Boolean.TRUE.equals(request.getShowSpecFlag())) {
                goodsInfoSpecDetailRelService.fillSpecDetail(goodsInfoVOS);
            }
            //是否填充可售性
            if (Boolean.TRUE.equals(request.getShowVendibilityFlag())) {
                linkedMallGoodsService.fillGoodsVendibility(goodsInfoVOS);
            }
            //供应商商品相关信息
            if (Boolean.TRUE.equals(request.getShowProviderInfoFlag())) {
                List<GoodsInfo> list = KsBeanUtil.convert(goodsInfoVOS, GoodsInfo.class);
                goodsInfoService.updateGoodsInfoSupplyPriceAndStock(list);
                goodsInfoVOS = KsBeanUtil.convert(list, GoodsInfoVO.class);
            }

            //是否填充LM库存
            if(Boolean.TRUE.equals(request.getFillLmInfoFlag())){
                linkedMallGoodsService.fillLmStock(goodsInfoVOS);
            }

            //填充库存
            for (GoodsInfoVO goodsParam : goodsInfoVOS) {
                //获取冻结
                String freezeStockStr = redisService.getString(RedisKeyConstant.GOODS_INFO_STOCK_FREEZE_PREFIX + goodsParam.getGoodsInfoId());
                long freezeStock = StringUtils.isBlank(freezeStockStr) ? 0L : Long.parseLong(freezeStockStr);
                goodsParam.setTotalStock(freezeStock + goodsParam.getStock());
                goodsParam.setFreezeStock(freezeStock);
            }
        }
        return BaseResponse.success(GoodsInfoByGoodsIdresponse.builder().goodsInfoVOList(goodsInfoVOS).build());
    }

    @Override
    public BaseResponse<EnterpriseGoodsInfoPageResponse> enterpriseGoodsInfoPage(@Valid EnterpriseGoodsInfoPageRequest request) {
        EnterpriseGoodsQueryRequest queryRequest = new EnterpriseGoodsQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);
        EnterPriseGoodsQueryResponse queryResponse = goodsInfoService.enterpriseGoodsPage(queryRequest);
        EnterpriseGoodsInfoPageResponse response = new EnterpriseGoodsInfoPageResponse();
        response.setGoodsInfoPage(KsBeanUtil.convertPage(queryResponse.getGoodsInfoPage(), GoodsInfoVO.class));
        if (CollectionUtils.isNotEmpty(queryResponse.getGoodsInfoSpecDetails())) {
            response.setGoodsInfoSpecDetails(KsBeanUtil.convertList(queryResponse.getGoodsInfoSpecDetails(),
                    GoodsInfoSpecDetailRelVO.class));
        }
        if (CollectionUtils.isNotEmpty(queryResponse.getGoodsCateList())) {
            response.setCates(KsBeanUtil.convertList(queryResponse.getGoodsCateList(), GoodsCateVO.class));
        }
        if (CollectionUtils.isNotEmpty(queryResponse.getGoodsBrandList())) {
            response.setBrands(KsBeanUtil.convertList(queryResponse.getGoodsBrandList(), GoodsBrandVO.class));
        }
        if (CollectionUtils.isNotEmpty(queryResponse.getCompanyInfoList())) {
            response.setCompanyInfoList(queryResponse.getCompanyInfoList());
        }
        //在设置未开启商品抵扣下清零buyPoint
        if (CollectionUtils.isNotEmpty(response.getGoodsInfoPage().getContent())) {
            systemPointsConfigService.clearBuyPoinsForSkus(response.getGoodsInfoPage().getContent());

            //供应商商品同步库存
            List<GoodsInfo> list = KsBeanUtil.convert(response.getGoodsInfoPage().getContent(), GoodsInfo.class);
            goodsInfoService.updateGoodsInfoSupplyPriceAndStock(list);
            MicroServicePage<GoodsInfoVO> goodsInfoVOS = new MicroServicePage<>(
                    KsBeanUtil.convert(list, GoodsInfoVO.class),
                    response.getGoodsInfoPage().getPageable(),
                    response.getGoodsInfoPage().getTotal());
            response.setGoodsInfoPage(goodsInfoVOS);
        }
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<GoodsInfoStoreIdBySkuIdResponse> getStoreIdByGoodsId(@Valid GoodsInfoStoreIdBySkuIdRequest request) {
        Long storeId = goodsInfoService.queryStoreId(request.getSkuId());
        return BaseResponse.success(GoodsInfoStoreIdBySkuIdResponse.builder().StoreId(storeId).build());
    }

    /**
     * 根据商品id查询商品的积分价
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<GoodsInfoListByIdsResponse> listIntegralPriceGoodsByIds(@Valid GoodsInfoListByIdsRequest request) {
        // 积分开关开启 并且 积分使用方式是商品抵扣 需要过滤积分价商品
        if (systemPointsConfigService.isGoodsPoint()) {
            List<GoodsInfo> goodsList = goodsInfoService.findByIds(request.getGoodsInfoIds());
            goodsList =
                    goodsList.stream().filter(item -> item.getBuyPoint() != null && item.getBuyPoint() > 0).collect(Collectors.toList());
            List<GoodsInfoVO> voList = KsBeanUtil.convert(goodsList, GoodsInfoVO.class);
            return BaseResponse.success(GoodsInfoListByIdsResponse.builder().goodsInfos(voList).build());
        }
        return BaseResponse.success(GoodsInfoListByIdsResponse.builder().goodsInfos(Collections.emptyList()).build());
    }

    @Override
    public BaseResponse<GoodsInfoPartColsByIdsResponse> listPartColsByIds(@RequestBody @Valid GoodsInfoPartColsByIdsRequest
                                                                                  request) {
        if (CollectionUtils.isEmpty(request.getGoodsInfoIds())) {
            return BaseResponse.success(new GoodsInfoPartColsByIdsResponse(Collections.emptyList()));
        }
        return BaseResponse.success(new GoodsInfoPartColsByIdsResponse(goodsInfoService.findGoodsInfoPartColsByIds(request.getGoodsInfoIds())));
    }

    @Override
    public BaseResponse<GoodsInfoMarketingPriceByNosResponse> listMarketingPriceByNos(@RequestBody @Valid GoodsInfoMarketingPriceByNosRequest request) {
        List<GoodsMarketingPrice> marketingPriceByNos = goodsInfoService.findMarketingPriceByNos(request.getGoodsInfoNos(), request.getStoreId());
        return BaseResponse.success(new GoodsInfoMarketingPriceByNosResponse(KsBeanUtil.convertList(marketingPriceByNos,
                GoodsInfoMarketingPriceDTO.class)));
    }


    @Override
    public BaseResponse<GoodsInfoViewByIdsResponse> listSimpleView(@RequestBody GoodsInfoViewByIdsRequest request) {
        GoodsInfoRequest infoRequest = goodsInfoMapper.goodsInfoViewByIdsRequestToGoodsInfoRequest(request);
        GoodsInfoResponse goodsInfoResponse = goodsInfoService.listSimpleGoodsInfo(infoRequest);
        GoodsInfoViewByIdsResponse response = GoodsInfoViewByIdsResponse.builder()
                .goodsInfos(goodsInfoMapper.goodsInfosToGoodsInfoVOs(goodsInfoResponse.getGoodsInfos())).build();

        //填充规格明细
        if (Objects.equals(request.getIsHavSpecText(), DeleteFlag.YES.toValue())) {
            goodsInfoSpecDetailRelService.fillSpecDetail(response.getGoodsInfos());
        }

        if (CollectionUtils.isNotEmpty(response.getGoodsInfos())) {
            for (GoodsInfoVO item : response.getGoodsInfos()) {
                if (StringUtils.isNotBlank(item.getGoodsChannelType())) {
                    item.setGoodsChannelTypeSet(Arrays.asList(item.getGoodsChannelType().split(",")));
                }
            }
        }
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<GoodsInfoViewByIdsResponse> goodsInfoByIsbns(GoodsInfoViewByIdsRequest isbnList) {
        GoodsInfoViewByIdsResponse goodsInfoViewByIdsResponse=new GoodsInfoViewByIdsResponse();
        List<GoodsInfo> goodsInfos = goodsInfoService.listSimpleGoodsInfo(isbnList.getIsbnList()).getGoodsInfos();
        if(null!=goodsInfos&&goodsInfos.size()!=0){
            goodsInfoViewByIdsResponse.setGoodsInfos( KsBeanUtil.convertList(goodsInfos, GoodsInfoVO.class) );
            return BaseResponse.success(goodsInfoViewByIdsResponse);
        }
        return BaseResponse.FAILED();
    }


}
