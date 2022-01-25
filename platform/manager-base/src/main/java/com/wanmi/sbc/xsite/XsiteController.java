package com.wanmi.sbc.xsite;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.ImageUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.ListStoreByIdsRequest;
import com.wanmi.sbc.customer.api.request.store.StoreHomeInfoRequest;
import com.wanmi.sbc.customer.api.request.store.StorePageRequest;
import com.wanmi.sbc.customer.api.response.store.ListStoreByIdsResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticQueryProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoQueryRequest;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsInfoResponse;
import com.wanmi.sbc.elastic.api.response.goods.EsGoodsResponse;
import com.wanmi.sbc.elastic.bean.vo.goods.EsGoodsInfoVO;
import com.wanmi.sbc.elastic.bean.vo.goods.GoodsInfoNestVO;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.storecate.StoreCateQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdsRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdRequest;
import com.wanmi.sbc.goods.api.request.storecate.StoreCateListByStoreIdRequest;
import com.wanmi.sbc.goods.api.response.brand.GoodsBrandByIdsResponse;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoPageResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.StoreCateResponseVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.info.GoodsInfoListByGoodsInfoResponse;
import com.wanmi.sbc.marketing.bean.enums.CouponSceneType;
import com.wanmi.sbc.setting.api.provider.storeresource.StoreResourceQueryProvider;
import com.wanmi.sbc.setting.api.provider.storeresourcecate.StoreResourceCateQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemresource.SystemResourceQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemresourcecate.SystemResourceCateQueryProvider;
import com.wanmi.sbc.setting.api.provider.yunservice.YunServiceProvider;
import com.wanmi.sbc.setting.api.request.storeresource.StoreResourcePageRequest;
import com.wanmi.sbc.setting.api.request.storeresourcecate.StoreResourceCateListRequest;
import com.wanmi.sbc.setting.api.request.systemresource.SystemResourcePageRequest;
import com.wanmi.sbc.setting.api.request.systemresourcecate.SystemResourceCateListRequest;
import com.wanmi.sbc.setting.api.request.yunservice.YunUploadResourceRequest;
import com.wanmi.sbc.setting.api.response.storeresource.StoreResourcePageResponse;
import com.wanmi.sbc.setting.api.response.storeresourcecate.StoreResourceCateListResponse;
import com.wanmi.sbc.setting.api.response.systemresource.SystemResourcePageResponse;
import com.wanmi.sbc.setting.api.response.systemresourcecate.SystemResourceCateListResponse;
import com.wanmi.sbc.common.enums.ResourceType;
import com.wanmi.sbc.setting.bean.enums.AuditStatus;
import com.wanmi.sbc.setting.bean.vo.StoreResourceVO;
import com.wanmi.sbc.setting.bean.vo.SystemResourceVO;
import com.wanmi.sbc.system.service.SystemPointsConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 魔方建站接口
 */
@Api(tags = "XsiteController", description = "魔方建站接口")
@RestController
@RequestMapping("")
public class XsiteController {

    private static final Logger logger = LoggerFactory.getLogger(XsiteController.class);
    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;
    @Autowired
    private GoodsCateQueryProvider goodsCateQueryProvider;
    @Autowired
    private StoreCateQueryProvider storeCateQueryProvider;
    @Autowired
    private EsGoodsInfoElasticQueryProvider esGoodsInfoElasticQueryProvider;
    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private SystemResourceCateQueryProvider systemResourceCateQueryProvider;

    @Autowired
    private YunServiceProvider yunServiceProvider;

    @Autowired
    private StoreResourceCateQueryProvider storeResourceCateQueryProvider;

    @Autowired
    private SystemResourceQueryProvider systemResourceQueryProvider;

    @Autowired
    private StoreResourceQueryProvider storeResourceQueryProvider;

    @Autowired
    private GoodsBrandQueryProvider goodsBrandQueryProvider;

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    /**
     * 魔方分页显示商品
     *
     * @param
     * @return 商品详情
     */
    @ApiOperation(value = "魔方分页显示商品")
    @RequestMapping(value = "/xsite/skusForXsite", method = RequestMethod.POST)
    public Map<String, Object> skuList(@RequestBody GoodsInfoRequest request) {
        EsGoodsInfoQueryRequest esGoodsInfoQueryRequest = new EsGoodsInfoQueryRequest();
        esGoodsInfoQueryRequest.setPageNum(request.getPageNum());
        esGoodsInfoQueryRequest.setPageSize(request.getPageSize());
        esGoodsInfoQueryRequest.setLikeGoodsName(request.getQ());
        esGoodsInfoQueryRequest.setCateId(request.getCatName());
        esGoodsInfoQueryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        esGoodsInfoQueryRequest.setStoreState(StoreState.OPENING.toValue());
        esGoodsInfoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
        esGoodsInfoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        String now = DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4);
        esGoodsInfoQueryRequest.setContractStartDate(now);
        esGoodsInfoQueryRequest.setContractEndDate(now);
        esGoodsInfoQueryRequest.setCustomerLevelId(0L);
        esGoodsInfoQueryRequest.setCustomerLevelDiscount(BigDecimal.ONE);
        esGoodsInfoQueryRequest.setSortFlag(request.getSortFlag());
        if (Objects.nonNull(request.getLabelId())) {
            esGoodsInfoQueryRequest.setLabelIds(Collections.singletonList(request.getLabelId()));
        }
        Long storeId = request.getStoreId();
        //如果店铺id存在，带入查询
        if (!ObjectUtils.isEmpty(storeId)) {
            esGoodsInfoQueryRequest.setStoreId(storeId);
            if (!ObjectUtils.isEmpty(request.getCatName())) {
                esGoodsInfoQueryRequest.setCateId(null);
                List<Long> storeCateIds = new ArrayList<>();
                storeCateIds.add(request.getCatName());
                esGoodsInfoQueryRequest.setStoreCateIds(storeCateIds);
            }
        }
        Long searchByStore = request.getSearchByStore();
        if (!ObjectUtils.isEmpty(searchByStore)) {
            esGoodsInfoQueryRequest.setStoreId(searchByStore);
        }
        EsGoodsInfoResponse response = esGoodsInfoElasticQueryProvider.page(esGoodsInfoQueryRequest).getContext();

        Map<String, Object> resMap = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        List<EsGoodsInfoVO> goodsInfos = response.getEsGoodsInfoPage().getContent();
        systemPointsConfigService.clearBuyPoinsForEsSku(goodsInfos);
        //计算营销价格
        List<GoodsInfoDTO> goodsInfoList = goodsInfos.stream().filter(e -> Objects.nonNull(e.getGoodsInfo()))
                .map(e -> KsBeanUtil.convert(e.getGoodsInfo(), GoodsInfoDTO.class))
                .collect(Collectors.toList());
        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfoList, GoodsInfoDTO.class));
        GoodsInfoListByGoodsInfoResponse filterResponse =
                marketingPluginProvider.goodsListFilter(filterRequest).getContext();
        Map<String, GoodsInfoVO> goodsInfoVOMap = new HashMap<>();
        if (Objects.nonNull(filterResponse.getGoodsInfoVOList())) {
            goodsInfoVOMap.putAll(filterResponse.getGoodsInfoVOList().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, c -> c)));
        }
        List<Map<String, Object>> skus = new ArrayList<>();
        goodsInfos.forEach(esGoodsInfo -> {
            GoodsInfoNestVO goodsInfo = esGoodsInfo.getGoodsInfo();
            Map<String, Object> sku = new HashMap<>();
            GoodsInfoViewByIdResponse goodsInfoEditResponse =
                    goodsInfoQueryProvider.getViewById(GoodsInfoViewByIdRequest.builder().goodsInfoId(goodsInfo
                    .getGoodsInfoId()).build()).getContext();
            sku.put("skuId", goodsInfo.getGoodsInfoId());
            sku.put("spuId", goodsInfo.getGoodsId());
            sku.put("skuName", goodsInfo.getGoodsInfoName());
            sku.put("spuName", goodsInfoEditResponse.getGoods().getGoodsName());
            if (Objects.nonNull(goodsInfoEditResponse.getGoods()) && Objects.nonNull(goodsInfoEditResponse.getGoods().getGoodsSubtitle())){
                sku.put("goodsSubtitle",goodsInfoEditResponse.getGoods().getGoodsSubtitle());
            } else {
                sku.put("goodsSubtitle",null);
            }

            if (Objects.nonNull(goodsInfoEditResponse.getGoods()) && CollectionUtils.isNotEmpty(goodsInfoEditResponse.getGoods().getGoodsLabelList())){
                sku.put("goodsLabel",goodsInfoEditResponse.getGoods().getGoodsLabelList());
            } else {
                sku.put("labelName",null);
            }

            List<String> imgs = new ArrayList<>();
            String img = goodsInfoEditResponse.getGoodsInfo().getGoodsInfoImg();
            if (StringUtils.isEmpty(img)) {
                img = goodsInfoEditResponse.getGoods().getGoodsImg();
            }
            imgs.add(img);
            sku.put("images", imgs);
            List<Map<String, Object>> specs = new ArrayList<>();
            Map<String, Object> spec = new HashMap<>();
            spec.put("goodsId", goodsInfo.getGoodsId());
            spec.put("valKey", goodsInfo.getSpecText());
            specs.add(spec);
            sku.put("specs", specs);
            sku.put("sellPoint", "");
            sku.put("salePrice", goodsInfo.getSalePrice());
            Map<String, Object> stock = new HashMap<>();
            stock.put("stock", goodsInfo.getStock());
            sku.put("stock", stock);
            sku.put("buyPoint", goodsInfo.getBuyPoint());

            GoodsInfoVO vo = goodsInfoVOMap.get(goodsInfo.getGoodsInfoId());
            if (Objects.nonNull(vo)) {
                sku.put("grouponLabel", vo.getGrouponLabel());
                sku.put("grouponPrice", vo.getGrouponPrice());
                sku.put("marketingLabels", vo.getMarketingLabels());
                sku.put("couponLabels", vo.getCouponLabels());
            }
            skus.add(sku);
        });
        data.put("dataList", skus);
        data.put("pageNum", request.getPageNum());
        data.put("pageSize", request.getPageSize());
        data.put("totalCount", response.getEsGoodsInfoPage().getTotalElements());
        resMap.put("data", data);
        resMap.put("status", 1);
        resMap.put("message", "操作成功");
        resMap.put("code", null);
        return resMap;
    }

    @ApiOperation(value = "魔方分页显示商品")
    @RequestMapping(value = "/xsite/skuPageForXsite", method = RequestMethod.POST)
    public BaseResponse<GoodsInfoPageResponse> skusPage(@RequestBody GoodsInfoRequest request) {
        EsGoodsInfoQueryRequest esGoodsInfoQueryRequest = new EsGoodsInfoQueryRequest();
        esGoodsInfoQueryRequest.setPageNum(request.getPageNum());
        esGoodsInfoQueryRequest.setPageSize(request.getPageSize());
        esGoodsInfoQueryRequest.setLikeGoodsName(request.getQ());
        esGoodsInfoQueryRequest.setCateId(request.getCatName());
        esGoodsInfoQueryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        esGoodsInfoQueryRequest.setStoreState(StoreState.OPENING.toValue());
        esGoodsInfoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
        esGoodsInfoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        esGoodsInfoQueryRequest.setSortFlag(0);
        String now = DateUtil.format(LocalDateTime.now(), DateUtil.FMT_TIME_4);
        esGoodsInfoQueryRequest.setContractStartDate(now);
        esGoodsInfoQueryRequest.setContractEndDate(now);
        esGoodsInfoQueryRequest.setCustomerLevelId(0L);
        esGoodsInfoQueryRequest.setCustomerLevelDiscount(BigDecimal.ONE);
        if (Objects.nonNull(request.getLabelId())) {
            esGoodsInfoQueryRequest.setLabelIds(Collections.singletonList(request.getLabelId()));
        }

        Long storeId = request.getStoreId();
        //如果店铺id存在，带入查询
        if (!ObjectUtils.isEmpty(storeId)) {
            esGoodsInfoQueryRequest.setStoreId(storeId);
            if (!ObjectUtils.isEmpty(request.getCatName())) {
                esGoodsInfoQueryRequest.setCateId(null);
                List<Long> storeCateIds = new ArrayList<>();
                storeCateIds.add(request.getCatName());
                esGoodsInfoQueryRequest.setStoreCateIds(storeCateIds);
            }
        }
        Long searchByStore = request.getSearchByStore();
        if (!ObjectUtils.isEmpty(searchByStore)) {
            esGoodsInfoQueryRequest.setStoreId(searchByStore);
        }
        EsGoodsInfoResponse response = esGoodsInfoElasticQueryProvider.page(esGoodsInfoQueryRequest).getContext();

        List<EsGoodsInfoVO> goodsInfos = response.getEsGoodsInfoPage().getContent();
        systemPointsConfigService.clearBuyPoinsForEsSku(goodsInfos);
        // 店铺信息
        List<Long> storeIds =
                goodsInfos.stream().map(EsGoodsInfoVO::getGoodsInfo).map(GoodsInfoNestVO::getStoreId).collect(Collectors.toList());
        Map<Long, StoreVO> storeVOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(storeIds)) {
            ListStoreByIdsResponse storeByIdsResponse =
                    storeQueryProvider.listByIds(ListStoreByIdsRequest.builder().storeIds(storeIds).build()).getContext();
            storeVOMap = storeByIdsResponse.getStoreVOList().stream().collect(Collectors.toMap(StoreVO::getStoreId,
                    Function.identity()));
        }

        // 分类信息
        Map<Long, GoodsCateVO> goodsCateVOMap = new HashMap<>();
        List<Long> cateIds =
                goodsInfos.stream().map(EsGoodsInfoVO::getGoodsInfo).map(GoodsInfoNestVO::getCateId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(cateIds)) {
            GoodsCateByIdsResponse cateByIdsResponse =
                    goodsCateQueryProvider.getByIds(new GoodsCateByIdsRequest(cateIds)).getContext();
            goodsCateVOMap =
                    cateByIdsResponse.getGoodsCateVOList().stream().collect(Collectors.toMap(GoodsCateVO::getCateId,
                            Function.identity()));
        }

        // 品牌信息
        Map<Long, GoodsBrandVO> goodsBrandVOMap = new HashMap<>();
        List<Long> branIds =
                goodsInfos.stream().map(EsGoodsInfoVO::getGoodsInfo).map(GoodsInfoNestVO::getBrandId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(branIds)) {
            GoodsBrandByIdsResponse brandByIdsResponse =
                    goodsBrandQueryProvider.listByIds(new GoodsBrandByIdsRequest(branIds)).getContext();
            goodsBrandVOMap =
                    brandByIdsResponse.getGoodsBrandVOList().stream().collect(Collectors.toMap(GoodsBrandVO::getBrandId,
                            Function.identity()));
        }

        List<GoodsInfoVO> goodsInfoVOS = new ArrayList<>();
        final Map<Long, GoodsCateVO> resultGoodsCateVOMap = goodsCateVOMap;
        final Map<Long, GoodsBrandVO> resultGoodsBrandVOMap = goodsBrandVOMap;
        final Map<Long, StoreVO> resultstoreVOMap = storeVOMap;
        goodsInfos.forEach(esGoodsInfo -> {
            GoodsInfoNestVO goodsInfo = esGoodsInfo.getGoodsInfo();
            GoodsInfoVO vo = new GoodsInfoVO();
            vo.setGoodsInfoId(goodsInfo.getGoodsInfoId());
            vo.setGoodsInfoName(goodsInfo.getGoodsInfoName());
            vo.setGoodsInfoImg(goodsInfo.getGoodsInfoImg());
            vo.setStock(goodsInfo.getStock());
            vo.setMarketPrice(goodsInfo.getSalePrice());
            vo.setStoreId(goodsInfo.getStoreId());
            vo.setSpecText(goodsInfo.getSpecText());
            vo.setBrandId(goodsInfo.getBrandId());
            vo.setCateId(goodsInfo.getCateId());
            vo.setBuyPoint(goodsInfo.getBuyPoint());

            StoreVO storeVO = resultstoreVOMap.get(goodsInfo.getStoreId());
            if (Objects.nonNull(storeVO)) {
                vo.setStoreName(storeVO.getStoreName());
            }

            GoodsCateVO goodsCateVO = resultGoodsCateVOMap.get(goodsInfo.getCateId());
            if (Objects.nonNull(goodsCateVO)) {
                vo.setCateName(goodsCateVO.getCateName());
            }

            GoodsBrandVO goodsBrandVO = resultGoodsBrandVOMap.get(goodsInfo.getBrandId());
            if (Objects.nonNull(goodsBrandVO)) {
                vo.setBrandName(goodsBrandVO.getBrandName());
            }

            goodsInfoVOS.add(vo);
        });

        GoodsInfoPageResponse pageResponse = new GoodsInfoPageResponse();
        pageResponse.setGoodsInfoPage(new MicroServicePage<GoodsInfoVO>(goodsInfoVOS,
                response.getEsGoodsInfoPage().getPageable(),
                response.getEsGoodsInfoPage().getTotalElements()));

        return BaseResponse.success(pageResponse);
    }

    @ApiOperation(value = "魔方分页显示商品组")
    @RequestMapping(value = "/xsite/spusForXsite", method = RequestMethod.POST)
    public BaseResponse spuList(@RequestBody GoodsInfoRequest request) {
        EsGoodsInfoQueryRequest esGoodsInfoQueryRequest = new EsGoodsInfoQueryRequest();
        esGoodsInfoQueryRequest.setPageNum(request.getPageNum());
        esGoodsInfoQueryRequest.setPageSize(request.getPageSize());
        esGoodsInfoQueryRequest.setLikeGoodsName(request.getQ());
        esGoodsInfoQueryRequest.setCateId(request.getCatName());
        esGoodsInfoQueryRequest.setAuditStatus(CheckStatus.CHECKED.toValue());
        esGoodsInfoQueryRequest.setStoreState(StoreState.OPENING.toValue());
        esGoodsInfoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
        esGoodsInfoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        esGoodsInfoQueryRequest.setSortFlag(0);
        esGoodsInfoQueryRequest.setQueryGoods(true);
        if (Objects.nonNull(request.getBrandId())) {
            esGoodsInfoQueryRequest.setBrandIds(Collections.singletonList(request.getBrandId()));
        }

        if (Objects.nonNull(request.getLabelId())) {
            esGoodsInfoQueryRequest.setLabelIds(Collections.singletonList(request.getLabelId()));
        }

        Long storeId = request.getStoreId();
        //如果店铺id存在，带入查询
        if (!ObjectUtils.isEmpty(storeId)) {
            esGoodsInfoQueryRequest.setStoreId(storeId);
            if (!ObjectUtils.isEmpty(request.getCatName())) {
                esGoodsInfoQueryRequest.setCateId(null);
                List<Long> storeCateIds = new ArrayList<>();
                storeCateIds.add(request.getCatName());
                esGoodsInfoQueryRequest.setStoreCateIds(storeCateIds);
            }
        }
        Long searchByStore = request.getSearchByStore();
        if (!ObjectUtils.isEmpty(searchByStore)) {
            esGoodsInfoQueryRequest.setStoreId(searchByStore);
        }
        EsGoodsResponse esGoodsResponse =
                esGoodsInfoElasticQueryProvider.pageByGoods(esGoodsInfoQueryRequest).getContext();

        List<Long> storeIds =
                esGoodsResponse.getEsGoods().get().flatMap(x -> x.getGoodsInfos().stream())
                        .map(GoodsInfoNestVO::getStoreId).distinct().collect(Collectors.toList());

        ListStoreByIdsResponse storeByIdsResponse =
                storeQueryProvider.listByIds(ListStoreByIdsRequest.builder().storeIds(storeIds).build()).getContext();
        Map<Long, StoreVO> storeVOMap =
                storeByIdsResponse.getStoreVOList().stream().collect(Collectors.toMap(StoreVO::getStoreId,
                        Function.identity()));

        systemPointsConfigService.clearBuyPointsForEsSpu(esGoodsResponse.getEsGoods().getContent());
        esGoodsResponse.getEsGoods().get().flatMap(x -> x.getGoodsInfos().stream()).forEach((goodsInfoNest) -> {
            StoreVO storeVO = storeVOMap.get(goodsInfoNest.getStoreId());
            if (Objects.nonNull(storeVO)) {
                goodsInfoNest.setStoreName(storeVO.getStoreName());
            }
        });
        return BaseResponse.success(esGoodsResponse.getEsGoods());
    }

    /**
     * PC端魔方显示商品类别
     *
     * @param
     * @return
     */
    @ApiOperation(value = "PC端魔方显示商品类别")
    @RequestMapping(value = "/xsite/goodsCatesForXsite", method = RequestMethod.GET)
    public BaseResponse<List<GoodsCateForXsite>> goodsCatesForXsite(@RequestParam(required = false) Long storeId) {
        List<GoodsCateForXsite> goodsCateForXsites = getCateList(storeId);
        return BaseResponse.success(goodsCateForXsites);
    }

    /**
     * 微信端魔方显示商品类别
     *
     * @param
     * @return
     */
    @ApiOperation(value = "微信端魔方显示商品类别")
    @RequestMapping(value = "/xsite/goodsCatesForXsite", method = RequestMethod.POST)
    public Map<String, Object> xSiteCatesForWx(@RequestParam(required = false) Long storeId) {
        List<GoodsCateForXsite> goodsCateForXsites = getCateList(storeId);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("data", goodsCateForXsites);
        resMap.put("msg", "操作成功");
        resMap.put("rescode", 200);
        resMap.put("result", "ok");
        return resMap;
    }


    private List<GoodsCateForXsite> getCateList(Long storeId) {

        if (ObjectUtils.isEmpty(storeId) || storeId == -1) {
            List<GoodsCateVO> goodsCates = goodsCateQueryProvider.list().getContext().getGoodsCateVOList();
            List<GoodsCateForXsite> goodsCateForXsites = new ArrayList<>();
            goodsCates.stream().forEach(goodsCate -> {
                List<GoodsCateVO> cates = goodsCate.getGoodsCateList();
                cates.forEach(cate -> {
                    GoodsCateForXsite gcfx = getGcfx(cate);
                    goodsCateForXsites.add(gcfx);
                    List<GoodsCateVO> cateList = cate.getGoodsCateList();
                    cateList.forEach(gCate -> {
                        GoodsCateForXsite xsiteCate = getGcfx(gCate);
                        goodsCateForXsites.add(xsiteCate);
                    });
                });

                GoodsCateForXsite goodsCateForXsite = getGcfx(goodsCate);
                goodsCateForXsites.add(goodsCateForXsite);
            });
            return goodsCateForXsites;
        } else {
            List<StoreCateResponseVO> goodsCates =
                    storeCateQueryProvider.listByStoreId(new StoreCateListByStoreIdRequest(storeId)).getContext().getStoreCateResponseVOList();
            List<GoodsCateForXsite> goodsCateForXsites = new ArrayList<>();
            goodsCates.stream().forEach(goodsCate -> {
                GoodsCateForXsite goodsCateForXsite = getGcfx(goodsCate);
                goodsCateForXsites.add(goodsCateForXsite);
            });
            return goodsCateForXsites;
        }

    }

    private GoodsCateForXsite getGcfx(Object goodsCate) {
        GoodsCateVO goodsCateVO = null;
        StoreCateResponseVO storeCateResponseVO = null;
        if (goodsCate instanceof GoodsCateVO) {
            goodsCateVO = (GoodsCateVO) goodsCate;
        } else if (goodsCate instanceof StoreCateResponseVO) {
            storeCateResponseVO = (StoreCateResponseVO) goodsCate;
        }
        GoodsCateForXsite goodsCateForXsite = new GoodsCateForXsite();
        goodsCateForXsite.setId(ObjectUtils.isEmpty(storeCateResponseVO) ? goodsCateVO.getCateId() + "" :
                storeCateResponseVO.getStoreCateId() + "");
        goodsCateForXsite.setName(ObjectUtils.isEmpty(storeCateResponseVO) ? goodsCateVO.getCateName() :
                storeCateResponseVO.getCateName());
        goodsCateForXsite.setParentId(ObjectUtils.isEmpty(storeCateResponseVO) ? goodsCateVO.getCateParentId() + "" :
                storeCateResponseVO.getCateParentId() + "");
        Integer rate = ObjectUtils.isEmpty(storeCateResponseVO) ? goodsCateVO.getCateGrade() :
                storeCateResponseVO.getCateGrade();
        goodsCateForXsite.setDepth(rate);
        String path = ObjectUtils.isEmpty(storeCateResponseVO) ? goodsCateVO.getCatePath() :
                storeCateResponseVO.getCatePath();
        path = path.substring(0, path.length() - 1);
        path = path.replace("|", ",");
        goodsCateForXsite.setPath(path);
        goodsCateForXsite.setPinYin(ObjectUtils.isEmpty(storeCateResponseVO) ? goodsCateVO.getPinYin() :
                "");
        goodsCateForXsite.setSimplePinYin(ObjectUtils.isEmpty(storeCateResponseVO) ? goodsCateVO.getSPinYin() : "");
        return goodsCateForXsite;
    }

    /**
     * 魔方查询图片分类
     *
     * @return
     */
    @ApiOperation(value = "魔方查询图片分类")
    @RequestMapping(value = {"/api/gallery/cate/list"}, method = RequestMethod.GET)
    public Map<String, Object> list(@RequestParam(required = false) Long storeId) {
        List<Map<String, Object>> maps;
        if (ObjectUtils.isEmpty(storeId)) {

            BaseResponse<SystemResourceCateListResponse> response =
                    systemResourceCateQueryProvider.list(SystemResourceCateListRequest.builder().delFlag(DeleteFlag.NO).build());
            maps = KsBeanUtil.objectsToMaps(response.getContext().getSystemResourceCateVOList());

        } else {
            StoreResourceCateListRequest queryRequest = StoreResourceCateListRequest.builder()
                    .storeId(storeId).build();
            BaseResponse<StoreResourceCateListResponse> response = storeResourceCateQueryProvider.list(queryRequest);
            maps = KsBeanUtil.objectsToMaps(response.getContext().getStoreResourceCateVOList());
        }

        List<Map<String, Object>> resMaps = new ArrayList<>();
        maps.stream().forEach(stringObjectMap -> {
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("id", stringObjectMap.get("cateId"));
            resMap.put("name", stringObjectMap.get("cateName"));
            resMap.put("pid", stringObjectMap.get("cateParentId"));
            resMap.put("isDefault", stringObjectMap.get("isDefault"));
            String path = String.valueOf(stringObjectMap.get("catePath"));
            path = path.substring(0, path.length() - 1);
            path = path.replace("|", ",");
            resMap.put("path", path);
            resMaps.add(resMap);
        });
        Map<String, Object> result = new HashMap<>();
        result.put("status", 1);
        result.put("data", resMaps);
        result.put("success", true);
        return result;
    }


    /**
     * 魔方分页图片
     *
     * @param imgPageReq 图片参数
     * @return
     */
    @ApiOperation(value = "魔方分页图片")
    @RequestMapping(value = "/api/gallery/item/list", method = RequestMethod.POST)
    public Map<String, Object> page(@RequestBody ImgPageReq imgPageReq, @RequestParam(required = false) Long storeId) {
        List<Map<String, Object>> maps;
        List<Map<String, Object>> pageList;
        long totalElements = 0;
        if (ObjectUtils.isEmpty(storeId)) {
            Long cateId = imgPageReq.getCateId();
            if (cateId == null) {
                cateId = getDefaultCateId(storeId);
            }
            List<Long> cateIds = getCateIds(cateId, storeId);
            SystemResourcePageRequest pageReq = new SystemResourcePageRequest();
            pageReq.setCateIds(cateIds);
            pageReq.setPageNum(imgPageReq.getPageNo());
            pageReq.setPageSize(imgPageReq.getPageSize());
            pageReq.setResourceName(imgPageReq.getName());
            pageReq.setResourceType(ResourceType.IMAGE);
            pageReq.setAuditStatus(AuditStatus.CHECKED);
            BaseResponse<SystemResourcePageResponse> response = systemResourceQueryProvider.page(pageReq);
            MicroServicePage<SystemResourceVO> systemResourceVOPage = response.getContext().getSystemResourceVOPage();
            totalElements = systemResourceVOPage.getTotalElements();
            pageList = KsBeanUtil.objectsToMaps(systemResourceVOPage.getContent());
        } else {
            Long cateId = imgPageReq.getCateId();
            if (cateId == null) {
                cateId = getDefaultCateId(storeId);
            }
            List<Long> cateIds = getCateIds(cateId, storeId);
            StoreResourcePageRequest pageReq = new StoreResourcePageRequest();
            pageReq.setCateIds(cateIds);
            pageReq.setPageNum(imgPageReq.getPageNo());
            pageReq.setPageSize(imgPageReq.getPageSize());
            pageReq.setStoreId(storeId);
            pageReq.setResourceName(imgPageReq.getName());
            pageReq.setResourceType(ResourceType.IMAGE);
            pageReq.setAuditStatus(AuditStatus.CHECKED);
            BaseResponse<StoreResourcePageResponse> response = storeResourceQueryProvider.page(pageReq);
            MicroServicePage<StoreResourceVO> systemResourceVOPage = response.getContext().getStoreResourceVOPage();
            totalElements = systemResourceVOPage.getTotalElements();
            pageList = KsBeanUtil.objectsToMaps(systemResourceVOPage.getContent());
        }

        if (totalElements == 0) {
            Map<String, Object> res = new HashMap<>();
            res.put("status", 1);
            res.put("success", true);
            Map<String, Object> content = new HashMap<>();
            content.put("data", Collections.EMPTY_LIST);
            content.put("content", Collections.EMPTY_LIST);
            content.put("totalElements", totalElements);
            res.put("data", content);
            return res;
        }
        List<Map<String, Object>> data = new ArrayList<>();
        pageList.stream().forEach(stringObjectMap -> {
            Map<String, Object> item = new HashMap<>();
            item.put("id", stringObjectMap.get("resourceId"));
            item.put("pid", stringObjectMap.get("cateId"));
            item.put("name", stringObjectMap.get("resourceName"));
            item.put("isCate", "0");
            item.put("path", stringObjectMap.get("artworkUrl"));
            data.add(item);
        });
        Map<String, Object> res = new HashMap<>();
        res.put("status", 1);
        res.put("success", true);
        Map<String, Object> content = new HashMap<>();
        content.put("data", data);
        content.put("content", data);
        content.put("totalElements", totalElements);
        content.put("pageIndex", imgPageReq.getPageNo());
        content.put("pageSize", imgPageReq.getPageSize());
        content.put("total", totalElements);
        content.put("size", 10);
        content.put("number", imgPageReq.getPageNo());
        content.put("totalPages", (totalElements % 10) + 1);
        res.put("data", content);
        return res;
    }

    private List<Long> getCateIds(Long cateId, Long storeId) {
        List<Map<String, Object>> maps;
        if (ObjectUtils.isEmpty(storeId)) {
            BaseResponse<SystemResourceCateListResponse> response =
                    systemResourceCateQueryProvider.list(SystemResourceCateListRequest
                    .builder()
                    .delFlag(DeleteFlag.NO)
                    .build());
            maps = KsBeanUtil.objectsToMaps(response.getContext().getSystemResourceCateVOList());
        } else {
            StoreResourceCateListRequest queryRequest = StoreResourceCateListRequest.builder()
                    .storeId(storeId).delFlag(DeleteFlag.NO).build();
            BaseResponse<StoreResourceCateListResponse> response = storeResourceCateQueryProvider.list(queryRequest);
            maps = KsBeanUtil.objectsToMaps(response.getContext().getStoreResourceCateVOList());

        }
        List<Long> cateIds = new ArrayList<>();
        cateIds.add(cateId);
        List<Long> second = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            Long pid = (Long) map.get("cateParentId");
            if (pid == cateId.intValue()) {
                Long fid = (Long) (map.get("cateId"));
                second.add(fid);
            }
        }
        cateIds.addAll(second);
        if (second.size() > 0) {
            for (Map<String, Object> map : maps) {
                Long pid = (Long) (map.get("cateParentId"));
                for (Long cId : second) {
                    if (pid == cId.intValue()) {
                        Long fid = (Long) (map.get("cateId"));
                        cateIds.add(fid);
                    }
                }
            }
        }
        return cateIds;
    }

    /**
     * 返回状态
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/open_site_api/refresh", method = RequestMethod.POST)
    public Map<String, Object> refresh(RefreshRequest request) {
        String msg = "操作成功！";
        Integer rescode = 200;
        String result = "ok";
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> res = new HashMap<>();
        res.put("msg", msg);
        res.put("data", data);
        res.put("rescode", rescode);
        res.put("result", result);
        return res;
    }

    /**
     * 上传图片
     *
     * @param multipartFile
     * @return
     */
    @ApiOperation(value = "上传图片")
    @RequestMapping(value = "/api/upload/image", method = RequestMethod.POST)
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile multipartFile, @RequestParam(required =
            false) Long storeId) {
        //验证上传参数
        if (null == multipartFile || multipartFile.getSize() == 0 || multipartFile.getOriginalFilename() == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        if (ImageUtils.checkImageSuffix(multipartFile.getOriginalFilename())) {
            String resourceUrl;
            try {
                if (ObjectUtils.isEmpty(storeId)) {
                    // 上传
                    resourceUrl = yunServiceProvider.uploadFile(YunUploadResourceRequest.builder()
                            .cateId(null)
                            .resourceType(ResourceType.IMAGE)
                            .content(multipartFile.getBytes())
                            .resourceName(multipartFile.getOriginalFilename())
                            .build()).getContext().getResourceUrl();
                } else {
                    Long companyInfoId =
                            storeQueryProvider.getStoreHomeInfo(StoreHomeInfoRequest.builder().storeId(storeId).build()).getContext().getCompanyInfoId();
                    // 上传
                    resourceUrl = yunServiceProvider.uploadFile(YunUploadResourceRequest.builder()
                            .cateId(null)
                            .storeId(storeId)
                            .companyInfoId(companyInfoId)
                            .resourceType(ResourceType.IMAGE)
                            .content(multipartFile.getBytes())
                            .resourceName(multipartFile.getOriginalFilename())
                            .build()).getContext().getResourceUrl();
                }
            } catch (Exception e) {
                Map<String, Object> resMap = getErroeMap();
                Map<String, String> err = new HashMap<>();
                err.put("msg", e.getMessage());
                resMap.put("err", err);
                return resMap;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("url", resourceUrl);
            data.put("name", multipartFile.getOriginalFilename());
            data.put("fileType",
                    multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf(".") + 1));
            try {
                data.put("duration", multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            List<Object> datas = new ArrayList<>();
            datas.add(data);
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("status", 1);
            resMap.put("message", null);
            resMap.put("code", null);
            resMap.put("detail", null);
            resMap.put("errorCodes", null);
            resMap.put("success", true);
            resMap.put("data", datas);
            return resMap;
        } else {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }

    /**
     * 保存图片信息
     *
     * @param imgRequest
     * @return
     */
    @ApiOperation(value = "保存图片信息")
    @RequestMapping(value = "/api/gallery/image/save", method = RequestMethod.POST)
    public Map<String, Object> saveImg(@RequestBody ImgRequest imgRequest) {
        List<YunUploadResourceRequest> yunUploadResourceRequestList = new ArrayList<>();
        boolean isFileNameTooLong = false;
        for (ImageForms imageForms : imgRequest.getImageForms()) {
            String fileName = imageForms.getName();
            int length = fileName.length();
            if (length > 50) {
                isFileNameTooLong = true;
                break;
            }
            YunUploadResourceRequest resourceRequest = YunUploadResourceRequest.builder()
                    .resourceType(ResourceType.IMAGE)
                    .resourceName(imageForms.getName())
                    .content(imageForms.getDuration())
                    .build();
            yunUploadResourceRequestList.add(resourceRequest);
        }
        if (isFileNameTooLong) {
            Map<String, Object> resMap = getErroeMap();
            Map<String, String> err = new HashMap<>();
            err.put("msg", "文件名过长，请检查后重试！");
            resMap.put("err", err);
            return resMap;
        }
        boolean isPic = true;
        for (ImageForms imageForms : imgRequest.getImageForms()) {
            String fileName = imageForms.getName();
            if (!ImageUtils.checkImageSuffix(fileName)) {
                isPic = false;
                break;
            }
        }
        if (!isPic) {
            Map<String, Object> resMap = getErroeMap();
            Map<String, String> err = new HashMap<>();
            err.put("msg", "文件格式错误！");
            resMap.put("err", err);
            return resMap;
        }
        Long storeId = imgRequest.getStoreId();
        List<String> resourceUrls = new ArrayList<>();
        try {
            if (ObjectUtils.isEmpty(storeId)) {
                Long cateId = Long.valueOf(imgRequest.getCateId());
                if (cateId == 0) {
                    cateId = getDefaultCateId(storeId);
                }
                for (YunUploadResourceRequest uploadResourceRequest : yunUploadResourceRequestList) {
                    uploadResourceRequest.setCateId(cateId);
                    // 上传
                    String resourceUrl = yunServiceProvider.uploadFile(uploadResourceRequest).getContext().getResourceUrl();
                    resourceUrls.add(resourceUrl);
                }
            } else {
                Long cateId = Long.valueOf(imgRequest.getCateId());
                if (cateId == 0) {
                    cateId = getDefaultCateId(storeId);
                }
                Long companyInfoId =
                        storeQueryProvider.getStoreHomeInfo(StoreHomeInfoRequest.builder().storeId(storeId).build()).getContext().getCompanyInfoId();
                for (YunUploadResourceRequest uploadResourceRequest : yunUploadResourceRequestList) {
                    uploadResourceRequest.setCateId(cateId);
                    uploadResourceRequest.setStoreId(storeId);
                    uploadResourceRequest.setCompanyInfoId(companyInfoId);
                    // 上传
                    String resourceUrl = yunServiceProvider.uploadFile(uploadResourceRequest).getContext().getResourceUrl();
                    resourceUrls.add(resourceUrl);
                }

            }
        } catch (Exception e) {
            logger.error("uploadStoreImage rop error: {}", e.getMessage());
            Map<String, Object> resMap = getErroeMap();
            Map<String, String> err = new HashMap<>();
            err.put("msg", e.getMessage());
            resMap.put("err", err);
            return resMap;
        }
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("status", 1);
        resMap.put("message", null);
        resMap.put("code", null);
        resMap.put("detail", null);
        resMap.put("errorCodes", null);
        resMap.put("success", true);
        resMap.put("data", resourceUrls);
        return resMap;
    }

    /**
     * 根据网络地址拉网络图片
     *
     * @param url
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据网络地址拉网络图片")
    @RequestMapping(value = "/api/upload/image/net", method = RequestMethod.GET)
    public Map<String, Object> netImg(@RequestParam String url, @RequestParam(required = false) Long storeId) {
        boolean fileTypeSupport = false;
        String[] fileUrl = url.split("\\.");
        int index = fileUrl.length - 1;
        String fileType = "";
        if (index < 0) {
            fileTypeSupport = true;
        } else {
            fileType = fileUrl[fileUrl.length - 1].toLowerCase();
        }
        switch (fileType) {
            case "jpeg":
                break;
            case "jpg":
                break;
            case "gif":
                break;
            case "png":
                break;
            default:
                fileTypeSupport = true;
                break;

        }
        if (fileTypeSupport) {
            Map<String, Object> resMap = getErroeMap();
            Map<String, String> err = new HashMap<>();
            err.put("message", "这不是图片的网络地址，请检查后重试");
            resMap.put("err", err);
            return resMap;
        }
        URL urls = null;
        try {
            urls = new URL(url);
        } catch (MalformedURLException e) {
            Map<String, Object> resMap = getErroeMap();
            Map<String, String> err = new HashMap<>();
            err.put("message", "请输入正确的网址！");
            resMap.put("err", err);
            return resMap;
        }
        //打开链接
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) urls.openConnection();
        } catch (IOException e) {
            Map<String, Object> resMap = getErroeMap();
            Map<String, String> err = new HashMap<>();
            err.put("message", "请输入正确的网址");
            resMap.put("err", err);
            return resMap;
        }
        //设置请求方式为"GET"
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream inStream = null;
        try {
            inStream = conn.getInputStream();
        } catch (IOException e) {
            Map<String, Object> resMap = getErroeMap();
            Map<String, String> err = new HashMap<>();
            err.put("message", "这不是图片的网络地址，请检查后重试！");
            resMap.put("err", err);
            return resMap;
        }
        String name = UUID.randomUUID().toString() + "\\." + fileType;
        MultipartFile multipartFile = null;
        try {
            multipartFile = new MockMultipartFile(name, name, null,
                    inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, Object> res = uploadFile(multipartFile, storeId);
        List<Object> datas = (List<Object>) res.get("data");
        res.put("data", datas.get(0));
        return res;
    }

    /**
     * 返回访问错误的数据格式
     *
     * @return
     */
    private Map<String, Object> getErroeMap() {
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("status", 1);
        resMap.put("message", null);
        resMap.put("code", null);
        resMap.put("detail", null);
        resMap.put("errorCodes", null);
        resMap.put("success", false);
        resMap.put("data", null);
        return resMap;
    }

    /**
     * 向开放平台查询默认分类ID
     *
     * @return
     */
    private Long getDefaultCateId(Long storeId) {
        List<Map<String, Object>> maps;
        if (ObjectUtils.isEmpty(storeId)) {

            BaseResponse<SystemResourceCateListResponse> response =
                    systemResourceCateQueryProvider.list(SystemResourceCateListRequest
                    .builder()
                    .delFlag(DeleteFlag.NO)
                    .isDefault(DefaultFlag.YES)
                    .build());
            maps = KsBeanUtil.objectsToMaps(response.getContext().getSystemResourceCateVOList());
        } else {
            StoreResourceCateListRequest queryRequest = StoreResourceCateListRequest.builder()
                    .storeId(storeId).isDefault(DefaultFlag.YES).build();
            BaseResponse<StoreResourceCateListResponse> response = storeResourceCateQueryProvider.list(queryRequest);
            maps = KsBeanUtil.objectsToMaps(response.getContext().getStoreResourceCateVOList());

        }
        Long defaultCateId = null;
        if (maps != null && maps.size() > 0) {
            Optional<Map<String, Object>> optionalMap = maps.stream().filter(stringObjectMap -> {
                DefaultFlag isDefault = (DefaultFlag) stringObjectMap.get("isDefault");
                return Objects.equals(DefaultFlag.YES, isDefault);
            }).findFirst();
            Map<String, Object> map = optionalMap.get();
            defaultCateId = Long.parseLong(String.valueOf(map.get("cateId")));
        }
        return defaultCateId;
    }


    /**
     * 店铺列表(未登录)
     *
     * @param queryRequest 搜索条件
     * @return 返回分页结果
     */
    @ApiOperation(value = "店铺列表(未登录)")
    @RequestMapping(value = "/xsite/storeList", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<StoreVO>> nonLoginList(@RequestBody StorePageRequest queryRequest) {
        queryRequest.setAuditState(CheckState.CHECKED);
        queryRequest.setStoreState(StoreState.OPENING);
        queryRequest.setGteContractStartDate(LocalDateTime.now());
        queryRequest.setLteContractEndDate(LocalDateTime.now());
        MicroServicePage<StoreVO> page = storeQueryProvider.page(queryRequest).getContext().getStoreVOPage();
        return BaseResponse.success(page);
    }
}
