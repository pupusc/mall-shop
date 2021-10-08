package com.wanmi.sbc.goods.provider.impl.goods;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.goods.*;
import com.wanmi.sbc.goods.api.request.goodsevaluate.GoodsEvaluateQueryRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsCacheInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsCountByConditionRequest;
import com.wanmi.sbc.goods.api.response.goods.*;
import com.wanmi.sbc.goods.api.response.info.GoodsCountByConditionResponse;
import com.wanmi.sbc.goods.appointmentsale.model.root.AppointmentSaleDO;
import com.wanmi.sbc.goods.appointmentsale.service.AppointmentSaleService;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.goods.bookingsale.model.root.BookingSale;
import com.wanmi.sbc.goods.bookingsale.service.BookingSaleService;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import com.wanmi.sbc.goods.brand.request.GoodsBrandQueryRequest;
import com.wanmi.sbc.goods.brand.service.GoodsBrandService;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.request.GoodsCateQueryRequest;
import com.wanmi.sbc.goods.cate.service.GoodsCateService;
import com.wanmi.sbc.goods.common.SystemPointsConfigService;
import com.wanmi.sbc.goods.goodsevaluate.service.GoodsEvaluateService;
import com.wanmi.sbc.goods.goodslabel.service.GoodsLabelService;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsPropDetailRel;
import com.wanmi.sbc.goods.info.model.root.GoodsSync;
import com.wanmi.sbc.goods.info.reponse.GoodsEditResponse;
import com.wanmi.sbc.goods.info.reponse.GoodsQueryResponse;
import com.wanmi.sbc.goods.info.reponse.GoodsResponse;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.info.service.*;
import com.wanmi.sbc.goods.info.service.GoodsInfoService;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.info.service.LinkedMallGoodsService;
import com.wanmi.sbc.goods.info.service.S2bGoodsService;
import com.wanmi.sbc.goods.prop.model.root.GoodsProp;
import com.wanmi.sbc.goods.redis.RedisService;
import com.wanmi.sbc.goods.util.mapper.GoodsBrandMapper;
import com.wanmi.sbc.goods.util.mapper.GoodsCateMapper;
import com.wanmi.sbc.goods.util.mapper.GoodsMapper;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * com.wanmi.sbc.goods.provider.impl.goods.GoodsQueryController
 *
 * @author lipeng
 * @dateTime 2018/11/7 下午3:20
 */
@RestController
@Validated
public class GoodsQueryController implements GoodsQueryProvider {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private S2bGoodsService s2bGoodsService;

    @Autowired
    private GoodsCateService goodsCateService;

    @Autowired
    private GoodsBrandService goodsBrandService;

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;

    @Autowired
    private LinkedMallGoodsService linkedMallGoodsService;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private AppointmentSaleService appointmentSaleService;

    @Autowired
    private BookingSaleService bookingSaleService;

    @Autowired
    private GoodsLabelService goodsLabelService;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private GoodsBrandMapper goodsBrandMapper;

    @Autowired
    private GoodsCateMapper goodsCateMapper;

    @Autowired
    private GoodsStockService goodsStockService;

    /**
     * 分页查询商品信息
     *
     * @param request {@link GoodsPageRequest}
     * @return 分页商品信息 {@link GoodsPageResponse}
     */
    @Override

    public BaseResponse<GoodsPageResponse> page(@RequestBody @Valid GoodsPageRequest request) {
        GoodsQueryRequest goodsQueryRequest = KsBeanUtil.convert(request, GoodsQueryRequest.class);
        GoodsQueryResponse goodsQueryResponse = goodsService.page(goodsQueryRequest);
        Page<Goods> goodsPage = goodsQueryResponse.getGoodsPage();
        GoodsPageResponse response = new GoodsPageResponse();
        MicroServicePage<GoodsVO> microServicePage = new MicroServicePage<>();
        if (Objects.nonNull(goodsPage) && CollectionUtils.isNotEmpty(goodsPage.getContent())) {
            response.setGoodsBrandList(goodsBrandMapper.brandToVoList(goodsQueryResponse.getGoodsBrandList()));
            response.setGoodsCateList(goodsCateMapper.cateToGoodsVOList(goodsQueryResponse.getGoodsCateList()));
//            response.setGoodsInfoList(KsBeanUtil.convert(goodsQueryResponse.getGoodsInfoList(), GoodsInfoVO.class));
            response.setImportStandard(goodsQueryResponse.getImportStandard());
            microServicePage.setPageable(goodsPage.getPageable());
            microServicePage.setTotal(goodsPage.getTotalElements());
            microServicePage.setContent(goodsMapper.goodsListToGoodsVOList(goodsPage.getContent()));
            //当不需示强制显示积分时，在设置未开启商品抵扣下清零buyPoint
            if (!Boolean.TRUE.equals(request.getShowPointFlag())) {
                systemPointsConfigService.clearBuyPoinsForSpus(microServicePage.getContent());
//                systemPointsConfigService.clearBuyPoinsForSkus(response.getGoodsInfoList());
            }
        }
        response.setGoodsPage(microServicePage);

        //是否填充可售性
        /*if(Boolean.TRUE.equals(request.getShowVendibilityFlag())){
            linkedMallGoodsService.fillGoodsVendibility(response.getGoodsInfoList());
        }*/
        //供应商商品库存同步
        /*if(CollectionUtils.isNotEmpty(response.getGoodsInfoList())) {
            response.setGoodsInfoList(providerStockSync(response.getGoodsInfoList()));
        }*/
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<GoodsPageByConditionResponse> pageByCondition(@RequestBody @Valid GoodsPageByConditionRequest request){
        GoodsQueryRequest goodsQueryRequest = KsBeanUtil.convert(request, GoodsQueryRequest.class);
        GoodsPageByConditionResponse response = new GoodsPageByConditionResponse();
        Page<Goods> goodsPage = goodsService.pageByCondition(goodsQueryRequest);
        MicroServicePage<GoodsVO> microServicePage = new MicroServicePage<>();
        if (Objects.nonNull(goodsPage) && CollectionUtils.isNotEmpty(goodsPage.getContent())) {
            //es评价数实时查询评价表
            List<Object> counts = goodsEvaluateService.countByGoodsIdsGroupByAndGoodsId(goodsPage.getContent().stream().map(Goods::getGoodsId).collect(Collectors.toList()));
            IteratorUtils.zip(goodsPage.getContent(), counts,
                    (goods, count) -> goods.getGoodsId().equals(((Object[]) count)[0]),
                    (goods, count) -> {
                        goods.setGoodsEvaluateNum((Long) ((Object[]) count)[1]);
                    }
            );
            //es好评数实时查询评价表
            List<Object> favoriteCounts = goodsEvaluateService.countFavorteByGoodsIdsGroupByAndGoodsId(goodsPage.getContent().stream().map(Goods::getGoodsId).collect(Collectors.toList()));
            IteratorUtils.zip(goodsPage.getContent(), favoriteCounts,
                    (goods, count) -> goods.getGoodsId().equals(((Object[]) count)[0]),
                    (goods, count) -> {
                        goods.setGoodsFavorableCommentNum((Long) ((Object[]) count)[1]);
                    }
            );
            microServicePage = KsBeanUtil.convertPage(goodsPage, GoodsVO.class);
        }
        response.setGoodsPage(microServicePage);
        return BaseResponse.success(response);
    }

    /**
     * 分页查询商品信息 (魔方)
     *
     * @param request {@link GoodsPageRequest}
     * @return 分页商品信息 {@link GoodsPageForXsiteResponse}
     */
    @Override
    public BaseResponse<GoodsPageForXsiteResponse> pageForXsite(@Valid GoodsPageRequest request) {
        GoodsQueryRequest goodsQueryRequest = KsBeanUtil.convert(request, GoodsQueryRequest.class);
        Page<Goods> goodsPage = goodsService.pageForXsite(goodsQueryRequest);
        List<Goods> goodsList = goodsPage.getContent();
        GoodsPageForXsiteResponse response = new GoodsPageForXsiteResponse(new MicroServicePage<>(Collections.emptyList(),
                request.getPageable(), goodsPage.getTotalElements()));
        if (CollectionUtils.isNotEmpty(goodsList)) {
            List<GoodsForXsiteVO> xsiteVOS = goodsList.stream().map(goods -> {
                GoodsForXsiteVO xsiteVO = new GoodsForXsiteVO();
                KsBeanUtil.copyPropertiesThird(goods, xsiteVO);
                return xsiteVO;
            }).collect(Collectors.toList());

            Map<Long, GoodsCate> goodsCateMap = new HashMap<>();
            List<Long> cateIds = goodsPage.get().map(Goods::getCateId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(cateIds)) {
                List<GoodsCate> goodsCates = goodsCateService.query(GoodsCateQueryRequest.builder().cateIds(cateIds)
                        .delFlag(DeleteFlag.NO.toValue()).build());
                goodsCateMap = goodsCates.stream().collect(Collectors.toMap(GoodsCate::getCateId,
                        Function.identity()));
            }

            Map<Long, GoodsBrand> goodsBrandMap = new HashMap<>();
            List<Long> brandIds = goodsPage.get().map(Goods::getBrandId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(brandIds)) {
                List<GoodsBrand> goodsBrands =
                        goodsBrandService.query(GoodsBrandQueryRequest.builder().brandIds(brandIds).delFlag(DeleteFlag.NO.toValue()).build());
                goodsBrandMap = goodsBrands.stream().collect(Collectors.toMap(GoodsBrand::getBrandId,
                        Function.identity()));
            }

            for (GoodsForXsiteVO xsiteVO : xsiteVOS) {
                GoodsCate goodsCate = goodsCateMap.get(xsiteVO.getCateId());
                if (Objects.nonNull(goodsCate)) {
                    xsiteVO.setCateName(goodsCate.getCateName());
                }
                GoodsBrand goodsBrand = goodsBrandMap.get(xsiteVO.getBrandId());
                if (Objects.nonNull(goodsBrand)) {
                    xsiteVO.setBrandName(goodsBrand.getBrandName());
                }
            }
            response = new GoodsPageForXsiteResponse(new MicroServicePage<>(xsiteVOS,
                    request.getPageable(), goodsPage.getTotalElements()));
        }
        return BaseResponse.success(response);
    }

    /**
     * 优化，缓存商品信息，规格，属性，商品图文信息不缓存
     * 预约信息在这里填充，减少重复请求goods
     *
     * @param request {@link GoodsViewByIdRequest}
     * @return 商品视图信息 {@link GoodsViewByIdResponse}
     */
    @Override
    public BaseResponse<GoodsViewByIdResponse> getCacheViewById(@RequestBody @Valid GoodsCacheInfoByIdRequest request) {

        GoodsInfo goodsInfo = goodsInfoService.findOne(request.getGoodsInfoId());
        if (Objects.isNull(goodsInfo) || (!Objects.equals(CheckStatus.CHECKED, goodsInfo.getAuditStatus()))) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        String goodsId = goodsInfo.getGoodsId();
        String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId);
        GoodsViewByIdResponse goodsByIdResponse = null;
        if (StringUtils.isBlank(goodsDetailInfo)) {
            GoodsEditResponse goodsEditResponse = goodsService.findInfoByIdNew(goodsId,request.getCustomerId());
            goodsByIdResponse = KsBeanUtil.convert(goodsEditResponse, GoodsViewByIdResponse.class);
        } else {
            GoodsResponse response = JSONObject.parseObject(goodsDetailInfo, GoodsResponse.class);
            GoodsEditResponse goodsEditResponse = goodsService.findInfoByIdCache(response.getGoods(),request.getCustomerId());
            goodsByIdResponse = KsBeanUtil.convert(goodsEditResponse, GoodsViewByIdResponse.class);
        }

        //供应商商品同步库存
        goodsByIdResponse.setGoodsInfos(providerStockSync(goodsByIdResponse.getGoodsInfos()));

        //预约，预售商品与其他营销活动互斥
        List<String> goodInfoIdList = goodsByIdResponse.getGoodsInfos().stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(goodInfoIdList)) {

            List<AppointmentSaleDO> appointmentSaleDOS = appointmentSaleService.inProgressAppointmentSaleInfoByGoodsInfoIdList(goodInfoIdList);
            List<BookingSale> bookingSaleList = bookingSaleService.inProgressBookingSaleInfoByGoodsInfoIdList(goodInfoIdList);

            if ((CollectionUtils.isNotEmpty(appointmentSaleDOS) || CollectionUtils.isNotEmpty(bookingSaleList))) {
                Map<String, List<AppointmentSaleDO>> appointmentMap = appointmentSaleDOS.stream().collect(Collectors.groupingBy(a -> a.getAppointmentSaleGood().getGoodsInfoId()));

                Map<String, List<BookingSale>> bookingMap = bookingSaleList.stream().collect(Collectors.groupingBy(a -> a.getBookingSaleGoods().getGoodsInfoId()));

                goodsByIdResponse.getGoodsInfos().forEach(goodsInfoVO -> {
                    if (appointmentMap.containsKey(goodsInfoVO.getGoodsInfoId())) {
                        BigDecimal appointmentPrice = appointmentMap.get(goodsInfoVO.getGoodsInfoId()).get(0).getAppointmentSaleGood().getPrice();
                        goodsInfoVO.setAppointmentPrice(appointmentPrice);
                        goodsInfoVO.setAppointmentSaleVO(KsBeanUtil.convert(appointmentMap.get(goodsInfoVO.getGoodsInfoId()).get(0), AppointmentSaleVO.class));
                    }
                    if (bookingMap.containsKey(goodsInfoVO.getGoodsInfoId())) {
                        BigDecimal bookingPrice = bookingMap.get(goodsInfoVO.getGoodsInfoId()).get(0).getBookingSaleGoods().getBookingPrice();
                        goodsInfoVO.setBookingPrice(bookingPrice);
                        goodsInfoVO.setBookingSaleVO(KsBeanUtil.convert(bookingMap.get(goodsInfoVO.getGoodsInfoId()).get(0), BookingSaleVO.class));
                    }
                });
            }
        }

        //控制是否显示商品标签
        if(Boolean.TRUE.equals(request.getShowLabelFlag())){
            goodsLabelService.fillGoodsLabel(Collections.singletonList(goodsByIdResponse.getGoods()), request.getShowSiteLabelFlag());
        }

        //未开启商品抵扣时，清零buyPoint
        systemPointsConfigService.clearBuyPoinsForSkus(goodsByIdResponse.getGoodsInfos());
        return BaseResponse.success(goodsByIdResponse);
    }

    @Autowired
    GoodsEvaluateService goodsEvaluateService;

    @Override
    public BaseResponse<GoodsDetailProperResponse> getGoodsDetail(@RequestBody @Valid GoodsDetailProperBySkuIdRequest request) {
        return BaseResponse.success(KsBeanUtil.convert(goodsService.findGoodsDetail(request.getSkuId()), GoodsDetailProperResponse.class));
    }

    @Override
    public BaseResponse<GoodsDetailSimpleResponse> getGoodsDetailSimple(@Valid GoodsDetailSimpleRequest goodsByIdRequest) {
        String goodsId = goodsByIdRequest.getGoodsId();

        String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId);
        GoodsDetailSimpleResponse goodsDetailSimpleResponse = null;
        if (StringUtils.isNotBlank(goodsDetailInfo)) {
            goodsDetailSimpleResponse = JSONObject.parseObject(goodsDetailInfo, GoodsDetailSimpleResponse.class);
        } else {
            GoodsResponse response = goodsService.findGoodsSimple(goodsId);
            if (Objects.nonNull(response)) {
                redisService.setString(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId,
                        JSONObject.toJSONString(response), 6 * 60 * 60);
            }
            goodsDetailSimpleResponse = KsBeanUtil.convert(response, GoodsDetailSimpleResponse.class);
        }
        return BaseResponse.success(goodsDetailSimpleResponse);
    }


    /**
     * 根据编号查询商品视图信息
     *
     * @param request {@link GoodsViewByIdRequest}
     * @return 商品视图信息 {@link GoodsViewByIdResponse}
     */
    @Override
    public BaseResponse<GoodsViewByIdResponse> getViewById(@RequestBody @Valid GoodsViewByIdRequest request) {
        String goodsId = request.getGoodsId();
        GoodsEditResponse goodsEditResponse = goodsService.findInfoById(goodsId);
        GoodsViewByIdResponse goodsByIdResponse = KsBeanUtil.convert(goodsEditResponse, GoodsViewByIdResponse.class);
        GoodsVO goods = goodsByIdResponse.getGoods();
        if (StringUtils.isNotBlank(goods.getProviderGoodsId()) && Objects.nonNull(goods.getProviderId())) {
            Goods providerGoods = goodsService.getGoodsById(goods.getProviderGoodsId());
            if (Objects.nonNull(providerGoods)) {
                goods.setProviderGoodsNo(providerGoods.getGoodsNo());
            }
            List<String> providerGoodsInfoIdList
                    = goodsByIdResponse.getGoodsInfos().stream().filter(v -> !ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                    .map(GoodsInfoVO::getProviderGoodsInfoId).collect(Collectors.toList());
            if (providerGoodsInfoIdList.size()>0) {
                List<GoodsInfo> providerGoodsInfoList = goodsInfoService.findByIds(providerGoodsInfoIdList);
                if (CollectionUtils.isNotEmpty(providerGoodsInfoList)) {
                    goodsByIdResponse.getGoodsInfos().forEach(goodsInfoVO -> {
                        GoodsInfo providerGoodsInfo = providerGoodsInfoList.stream()
                                .filter(v -> v.getGoodsInfoId().equals(goodsInfoVO.getProviderGoodsInfoId())).findFirst().orElseGet(null);
                        if (Objects.nonNull(providerGoodsInfo)) {
                            goodsInfoVO.setProviderGoodsInfoNo(providerGoodsInfo.getGoodsInfoNo());
                            goodsInfoVO.setStock(providerGoodsInfo.getStock());
                        }
                    });
                }
            }
        }
        //控制是否显示商品标签
        if (Boolean.TRUE.equals(request.getShowLabelFlag())) {
            goodsLabelService.fillGoodsLabel(Collections.singletonList(goods), request.getShowSiteLabelFlag());
        }

        //未开启商品抵扣时，清零buyPoint
        systemPointsConfigService.clearBuyPoinsForSkus(goodsByIdResponse.getGoodsInfos());
        return BaseResponse.success(goodsByIdResponse);
    }
    /**
     * 根据SPU编号和SkuId集合查询商品视图信息
     *
     * @param request {@link GoodsViewByIdAndSkuIdsRequest}
     * @return 商品视图信息 {@link GoodsViewByIdAndSkuIdsResponse}
     */
    @Override
    public BaseResponse<GoodsViewByIdAndSkuIdsResponse> getViewByIdAndSkuIds(@RequestBody @Valid GoodsViewByIdAndSkuIdsRequest request) {
        GoodsEditResponse goodsEditResponse = goodsService.findInfoByIdAndSkuIds(request.getGoodsId(), request.getSkuIds());
        GoodsViewByIdAndSkuIdsResponse goodsByIdResponse = KsBeanUtil.convert(goodsEditResponse, GoodsViewByIdAndSkuIdsResponse.class);
        //未开启商品抵扣时，清零buyPoint
        systemPointsConfigService.clearBuyPoinsForSkus(goodsByIdResponse.getGoodsInfos());
        //供应商商品库存同步
        goodsByIdResponse.setGoodsInfos(providerStockSync(goodsByIdResponse.getGoodsInfos()));
        return BaseResponse.success(goodsByIdResponse);
    }

    /**
     * 根据积分商品Id查询商品视图信息
     *
     * @param request {@link GoodsViewByPointsGoodsIdRequest}
     * @return 商品视图信息 {@link GoodsViewByPointsGoodsIdResponse}
     */
    @Override
    public BaseResponse<GoodsViewByPointsGoodsIdResponse> getViewByPointsGoodsId(@RequestBody @Valid GoodsViewByPointsGoodsIdRequest request) {
        GoodsEditResponse goodsEditResponse = goodsService.findInfoByPointsGoodsId(request.getPointsGoodsId());
        GoodsViewByPointsGoodsIdResponse goodsByIdResponse = KsBeanUtil.convert(goodsEditResponse, GoodsViewByPointsGoodsIdResponse.class);
        //控制是否显示商品标签
        if(Boolean.TRUE.equals(request.getShowLabelFlag())){
            goodsLabelService.fillGoodsLabel(Collections.singletonList(goodsByIdResponse.getGoods()), request.getShowSiteLabelFlag());
        }
        return BaseResponse.success(goodsByIdResponse);
    }

    /**
     * 根据编号查询商品信息
     *
     * @param request {@link GoodsByIdRequest}
     * @return 商品信息 {@link GoodsByIdResponse}
     */
    @Override
    public BaseResponse<GoodsByIdResponse> getById(@RequestBody @Valid GoodsByIdRequest request) {
        return BaseResponse.success(KsBeanUtil.convert(goodsService.getGoodsById(request.getGoodsId()), GoodsByIdResponse.class));
    }

    /**
     * 根据多个SpuID查询属性关联
     *
     * @param request {@link GoodsPropDetailRelByIdsRequest}
     * @return 属性关联信息 {@link GoodsPropDetailRelByIdsResponse}
     */
    @Override
    public BaseResponse<GoodsPropDetailRelByIdsResponse> getRefByGoodIds(
            @RequestBody @Valid GoodsPropDetailRelByIdsRequest request) {
        List<String> goodsIds = request.getGoodsIds();
        List<GoodsPropDetailRel> goodsPropDetailRelList = goodsService.findRefByGoodIds(goodsIds);

        GoodsPropDetailRelByIdsResponse response = new GoodsPropDetailRelByIdsResponse();
        List<GoodsPropDetailRelVO> goodsPropDetailRelVOList =
                KsBeanUtil.convertList(goodsPropDetailRelList, GoodsPropDetailRelVO.class);
        response.setGoodsPropDetailRelVOList(goodsPropDetailRelVOList);
        return BaseResponse.success(response);
    }

    /**
     * 根据属性id查询
     */
    @Override
    public BaseResponse<List<GoodsPropVO>> getPropByIds(@RequestBody List<Long> ids) {
        List<GoodsProp> props = goodsService.findPropByIds(ids);
        List<GoodsPropVO> propVos = new ArrayList<>();
        for (GoodsProp prop : props) {
            GoodsPropVO goodsPropVO = new GoodsPropVO();
            BeanUtils.copyProperties(prop, goodsPropVO);
            propVos.add(goodsPropVO);
        }
        return BaseResponse.success(propVos);
    }

    /**
     * 待审核商品统计
     *
     * @param request {@link GoodsUnAuditCountRequest}
     * @return 待审核商品统计数量 {@link GoodsUnAuditCountResponse}
     */
    @Override

    public BaseResponse<GoodsUnAuditCountResponse> countUnAudit(
            @RequestBody @Valid GoodsUnAuditCountRequest request) {
        GoodsQueryRequest goodsQueryRequest = KsBeanUtil.convert(request, GoodsQueryRequest.class);
        Long unAuditCount = s2bGoodsService.countByTodo(goodsQueryRequest);

        GoodsUnAuditCountResponse response = new GoodsUnAuditCountResponse();
        response.setUnAuditCount(unAuditCount);
        return BaseResponse.success(response);
    }

    /**
     * 根据不同条件查询商品信息
     *
     * @param goodsByConditionRequest {@link GoodsByConditionRequest}
     * @return {@link GoodsByConditionResponse}
     */
    @Override
    public BaseResponse<GoodsByConditionResponse> listByCondition(@RequestBody @Valid GoodsByConditionRequest goodsByConditionRequest) {
        GoodsQueryRequest goodsQueryRequest = KsBeanUtil.convert(goodsByConditionRequest, GoodsQueryRequest.class);
        List<Goods> goodsList = goodsService.findAll(goodsQueryRequest);
        if (CollectionUtils.isEmpty(goodsList)) {
            return BaseResponse.success(new GoodsByConditionResponse(Collections.EMPTY_LIST));
        }
        List<GoodsVO> goodsVOList = KsBeanUtil.convert(goodsList, GoodsVO.class);
        return BaseResponse.success(new GoodsByConditionResponse(goodsVOList));
    }

    /**
     * 根据不同条件查询商品信息
     *
     * @param goodsByConditionRequest
     * @return
     */
    @Override
    public BaseResponse<GoodsCountByConditionResponse> countByCondition(@RequestBody @Valid GoodsCountByConditionRequest goodsByConditionRequest) {
        long count = goodsService.countByCondition(KsBeanUtil.convert(goodsByConditionRequest, GoodsQueryRequest.class));
        return BaseResponse.success(GoodsCountByConditionResponse.builder().count(count).build());
    }

    /**
     * 根据goodsId批量查询商品信息列表
     *
     * @param request 包含goodsIds的批量查询请求结构 {@link GoodsListByIdsRequest}
     * @return 商品信息列表 {@link GoodsListByIdsResponse}
     */
    @Override
    public BaseResponse<GoodsListByIdsResponse> listByIds(@RequestBody @Valid GoodsListByIdsRequest request) {
        List<Goods> goodsList = goodsService.listByGoodsIds(request.getGoodsIds());
        if (CollectionUtils.isEmpty(goodsList)) {
            return BaseResponse.success(GoodsListByIdsResponse.builder().goodsVOList(Collections.emptyList()).build());
        }
        List<GoodsVO> voList = KsBeanUtil.convert(goodsList, GoodsVO.class);
        return BaseResponse.success(GoodsListByIdsResponse.builder().goodsVOList(voList).build());
    }

    /**
     * @param request {@link GoodsCountByStoreIdRequest}
     * @Description: 店铺ID统计店铺商品总数
     * @Author: Bob
     * @Date: 2019-04-03 10:47
     */
    @Override
    public BaseResponse<GoodsCountByStoreIdResponse> countByStoreId(GoodsCountByStoreIdRequest request) {
        GoodsQueryRequest goodsQueryRequest = KsBeanUtil.convert(request, GoodsQueryRequest.class);
        goodsQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        long count = goodsService.countByCondition(goodsQueryRequest);
        return BaseResponse.success(GoodsCountByStoreIdResponse.builder().goodsCount(count).build());
    }

    @Override
    public BaseResponse<GoodsListByIdsResponse> listByProviderGoodsId(@Valid GoodsListByIdsRequest request) {
        List<Goods> goodsList = goodsService.listByProviderGoodsIds(request.getGoodsIds());
        if (CollectionUtils.isEmpty(goodsList)) {
            return BaseResponse.success(GoodsListByIdsResponse.builder().goodsVOList(Collections.emptyList()).build());
        }
        List<GoodsVO> voList = KsBeanUtil.convert(goodsList, GoodsVO.class);
        return BaseResponse.success(GoodsListByIdsResponse.builder().goodsVOList(voList).build());
    }

    @Override
    public BaseResponse<GoodsListNeedSynResponse> listNeedSyn(@Valid GoodsQueryNeedSynRequest request) {
        List<Goods> goodsList = goodsService.listNeedSyn(request);
        if (CollectionUtils.isEmpty(goodsList)) {
            return BaseResponse.success(GoodsListNeedSynResponse.builder().goodsVOList(Collections.emptyList()).build());
        }
        List<GoodsVO> voList = KsBeanUtil.convert(goodsList, GoodsVO.class);
        return BaseResponse.success(GoodsListNeedSynResponse.builder().goodsVOList(voList).build());
    }

    @Override
    public BaseResponse vaildErpNo(@RequestBody @Valid GoodsErpNoRequest request) {
        goodsService.findExistsErpGoodsInfoNo(request.getErpGoodsNo(), request.getErpGoodsInfoNos());
        return BaseResponse.SUCCESSFUL();
    }

    private List<GoodsInfoVO> providerStockSync(List<GoodsInfoVO> goodsInfoVOList){
        List<GoodsInfo> list = KsBeanUtil.convert(goodsInfoVOList, GoodsInfo.class);
        goodsInfoService.updateGoodsInfoSupplyPriceAndStock(list);
        return KsBeanUtil.convert(list, GoodsInfoVO.class);
    }

    @Override
    public BaseResponse<List<GoodsSyncVO>> listGoodsSync() {
        List<GoodsSync> list= goodsService.listGoodsSync();
        if(CollectionUtils.isEmpty(list)){
            return BaseResponse.success(Collections.emptyList());
        }
        return BaseResponse.success(KsBeanUtil.convert(list, GoodsSyncVO.class));
    }

    @Override
    public BaseResponse<Integer> countGoodsStockSync() {
        return BaseResponse.success((int)goodsStockService.countGoodsStockSync());
    }

    @Override
    public BaseResponse<Integer> countGoodsPriceSync() {
        return BaseResponse.success((int)goodsInfoService.countGoodPriceSync());
    }
}
