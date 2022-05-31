package com.wanmi.sbc.goods.info.service;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.google.common.collect.Lists;
import com.sbc.wanmi.erp.bean.vo.ERPGoodsInfoVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoQueryByIdsRequest;
import com.wanmi.sbc.customer.api.request.store.ListStoreByIdsRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.request.store.StorePartColsListByIdsRequest;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoQueryByIdsResponse;
import com.wanmi.sbc.customer.api.response.store.StoreByIdResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.request.enterprise.goods.*;
import com.wanmi.sbc.goods.api.request.goods.ProviderGoodsNotSellRequest;
import com.wanmi.sbc.goods.api.request.info.DistributionGoodsBatchCheckRequest;
import com.wanmi.sbc.goods.api.request.info.DistributionGoodsCheckRequest;
import com.wanmi.sbc.goods.api.request.info.DistributionGoodsDeleteRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoSmallProgramCodeRequest;
import com.wanmi.sbc.goods.api.response.enterprise.EnterpriseByIdResponse;
import com.wanmi.sbc.goods.api.response.enterprise.EnterprisePriceResponse;
import com.wanmi.sbc.goods.bean.dto.BatchEnterPrisePriceDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoMinusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoPlusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoPriceChangeDTO;
import com.wanmi.sbc.goods.bean.enums.*;
import com.wanmi.sbc.goods.bean.vo.GoodsCustomerPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import com.wanmi.sbc.goods.bookuu.BooKuuSupplierClient;
import com.wanmi.sbc.goods.bookuu.request.GoodsCostPriceRequest;
import com.wanmi.sbc.goods.bookuu.response.GoodsCostPriceResponse;
import com.wanmi.sbc.goods.bookuu.response.GoodsStockResponse;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import com.wanmi.sbc.goods.brand.repository.GoodsBrandRepository;
import com.wanmi.sbc.goods.brand.request.GoodsBrandQueryRequest;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
import com.wanmi.sbc.goods.cate.request.GoodsCateQueryRequest;
import com.wanmi.sbc.goods.cate.service.GoodsCateService;
import com.wanmi.sbc.goods.distributor.goods.service.DistributorGoodsInfoService;
import com.wanmi.sbc.goods.images.GoodsImageRepository;
import com.wanmi.sbc.goods.info.model.entity.GoodsInfoLiveGoods;
import com.wanmi.sbc.goods.info.model.entity.GoodsInfoParams;
import com.wanmi.sbc.goods.info.model.entity.GoodsMarketingPrice;
import com.wanmi.sbc.goods.info.model.entity.GoodsStockInfo;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsPriceSync;
import com.wanmi.sbc.goods.info.model.root.GoodsSpecialPriceSync;
import com.wanmi.sbc.goods.info.reponse.DistributionGoodsQueryResponse;
import com.wanmi.sbc.goods.info.reponse.EnterPriseGoodsQueryResponse;
import com.wanmi.sbc.goods.info.reponse.GoodsInfoEditResponse;
import com.wanmi.sbc.goods.info.reponse.GoodsInfoResponse;
import com.wanmi.sbc.goods.info.repository.*;
import com.wanmi.sbc.goods.info.request.*;
import com.wanmi.sbc.goods.marketing.service.GoodsMarketingService;
import com.wanmi.sbc.goods.price.model.root.GoodsCustomerPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsIntervalPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsLevelPrice;
import com.wanmi.sbc.goods.price.repository.GoodsCustomerPriceRepository;
import com.wanmi.sbc.goods.price.repository.GoodsIntervalPriceRepository;
import com.wanmi.sbc.goods.price.repository.GoodsLevelPriceRepository;
import com.wanmi.sbc.goods.price.service.GoodsIntervalPriceService;
import com.wanmi.sbc.goods.redis.RedisService;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpecDetail;
import com.wanmi.sbc.goods.spec.repository.GoodsInfoSpecDetailRelRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecDetailRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecRepository;
import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.standard.model.root.StandardGoodsRel;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRelRepository;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRepository;
import com.wanmi.sbc.goods.storecate.model.root.StoreCateGoodsRela;
import com.wanmi.sbc.goods.storecate.service.StoreCateService;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商品服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
@Slf4j
public class GoodsInfoService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private GoodsSpecRepository goodsSpecRepository;

    @Autowired
    private GoodsSpecDetailRepository goodsSpecDetailRepository;

    @Autowired
    private GoodsInfoSpecDetailRelRepository goodsInfoSpecDetailRelRepository;

    @Autowired
    private GoodsIntervalPriceRepository goodsIntervalPriceRepository;

    @Autowired
    private GoodsLevelPriceRepository goodsLevelPriceRepository;

    @Autowired
    private GoodsCustomerPriceRepository goodsCustomerPriceRepository;

    @Autowired
    private GoodsBrandRepository goodsBrandRepository;

    @Autowired
    private GoodsCateRepository goodsCateRepository;

    @Autowired
    private GoodsPropDetailRelRepository goodsPropDetailRelRepository;

    @Autowired
    private StandardGoodsRelRepository standardGoodsRelRepository;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private StoreCateService storeCateService;

    @Autowired
    private GoodsImageRepository goodsImageRepository;

    @Autowired
    private GoodsCateService goodsCateService;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private DistributorGoodsInfoService distributorGoodsInfoService;

    @Autowired
    private StandardGoodsRepository standardGoodsRepository;

    @Autowired
    private GoodsInfoStockService goodsInfoStockService;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsCacheService goodsCacheService;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private GoodsMarketingService goodsMarketingService;


    @Autowired
    private ThirdPlatformConfigQueryProvider thirdPlatformConfigQueryProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsIntervalPriceService goodsIntervalPriceService;

    @Autowired
    private GoodsPriceSyncRepository goodsPriceSyncRepository;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private GoodsSpecialPriceSyncRepository goodsSpecialPriceSyncRepository;

    @Autowired
    private BooKuuSupplierClient booKuuSupplierClient;
    /**
     * SKU分页
     *
     * @param queryRequest 查询请求
     * @return 商品分页响应
     */
    @Transactional(readOnly = true, timeout = 10, propagation = Propagation.REQUIRES_NEW)
    public GoodsInfoResponse pageView(GoodsInfoQueryRequest queryRequest) {
        if (StringUtils.isNotBlank(queryRequest.getLikeGoodsNo())
                || queryRequest.getStoreCateId() != null
                || CollectionUtils.isNotEmpty(queryRequest.getBrandIds())
                || (queryRequest.getCateId() != null && queryRequest.getCateId() > 0)
                || queryRequest.getBrandId() != null && queryRequest.getBrandId() > 0
                || (queryRequest.getNotThirdPlatformType() != null && queryRequest.getNotThirdPlatformType().size() > 0)
                || (queryRequest.getLabelId() != null && queryRequest.getLabelId() > 0)) {
            GoodsQueryRequest goodsQueryRequest = GoodsQueryRequest.builder()
                    .likeGoodsNo(queryRequest.getLikeGoodsNo())
                    .brandId(queryRequest.getBrandId())
                    .notThirdPlatformType(queryRequest.getNotThirdPlatformType())
                    .brandIds(queryRequest.getBrandIds())
                    .labelId(queryRequest.getLabelId()).build();

            //获取该分类的所有子分类
            if (queryRequest.getCateId() != null && queryRequest.getCateId() > 0) {
                goodsQueryRequest.setCateIds(goodsCateService.getChlidCateId(queryRequest.getCateId()));
                goodsQueryRequest.getCateIds().add(queryRequest.getCateId());
                queryRequest.setCateId(null);
            }

            //获取该店铺分类下的所有spuIds
            if (queryRequest.getStoreCateId() != null && queryRequest.getStoreCateId() > 0) {
                List<StoreCateGoodsRela> relas = storeCateService.findAllChildRela(queryRequest.getStoreCateId(), true);
                if (CollectionUtils.isNotEmpty(relas)) {
                    goodsQueryRequest.setStoreCateGoodsIds(relas.stream().map(StoreCateGoodsRela::getGoodsId).collect(Collectors.toList()));
                } else {
                    return GoodsInfoResponse.builder().goodsInfoPage(new MicroServicePage<>(Collections.emptyList(), queryRequest.getPageRequest(), 0)).build();
                }
            }

            List<Goods> goods = goodsRepository.findAll(goodsQueryRequest.getWhereCriteria());
            if (CollectionUtils.isEmpty(goods)) {
                return GoodsInfoResponse.builder().goodsInfoPage(new MicroServicePage<>(Collections.emptyList(), queryRequest.getPageRequest(), 0)).build();
            }
            queryRequest.setGoodsIds(goods.stream().map(Goods::getGoodsId).collect(Collectors.toList()));
            queryRequest.setCateId(null);
        }

        //分页查询SKU信息列表
        Page<GoodsInfo> goodsInfoPage = goodsInfoRepository.findAll(queryRequest.getWhereCriteria(), queryRequest.getPageRequest());

        MicroServicePage<GoodsInfo> microPage = KsBeanUtil.convertPage(goodsInfoPage, GoodsInfo.class);
        if (Objects.isNull(microPage) || microPage.getTotalElements() < 1 || CollectionUtils.isEmpty(microPage.getContent())) {
            return GoodsInfoResponse.builder().goodsInfoPage(microPage).build();
        }


        //查询SPU
        List<String> goodsIds = microPage.getContent().stream().map(GoodsInfo::getGoodsId).distinct().collect(Collectors.toList());
        List<Goods> goodses = goodsRepository.findAll(GoodsQueryRequest.builder().goodsIds(goodsIds).build().getWhereCriteria());
        Map<String, Goods> goodsMap = goodses.stream().collect(Collectors.toMap(Goods::getGoodsId, goods -> goods));
        //查询规格明细关联表
        List<String> skuIds = microPage.getContent().stream().map(GoodsInfo::getGoodsInfoId).collect(Collectors.toList());
        Map<String, List<GoodsInfoSpecDetailRel>> specDetailMap = goodsInfoSpecDetailRelRepository.findByAllGoodsInfoIds(skuIds).stream().collect(Collectors.groupingBy(GoodsInfoSpecDetailRel::getGoodsInfoId));

        List<GoodsBrand> brands = goodsBrandRepository.findAll(GoodsBrandQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).brandIds(goodses.stream().map(Goods::getBrandId).collect(Collectors.toList())).build().getWhereCriteria());

        List<GoodsCate> cates = goodsCateRepository.findAll(GoodsCateQueryRequest.builder().cateIds(goodses.stream().map(Goods::getCateId).collect(Collectors.toList())).build().getWhereCriteria());

        microPage.getContent().forEach(goodsInfo -> {
            //明细组合->，规格值1 规格值2
            if (MapUtils.isNotEmpty(specDetailMap)) {
                goodsInfo.setSpecText(StringUtils.join(specDetailMap.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream()
                        .map(GoodsInfoSpecDetailRel::getDetailName)
                        .collect(Collectors.toList()), " "));
            }

            //原价
            goodsInfo.setSalePrice(goodsInfo.getMarketPrice() == null ? BigDecimal.ZERO : goodsInfo.getMarketPrice());

            Goods goods = goodsMap.get(goodsInfo.getGoodsId());
            if (goods != null) {
                goodsInfo.setSaleType(goods.getSaleType());
                goodsInfo.setAllowPriceSet(goods.getAllowPriceSet());
                goodsInfo.setPriceType(goods.getPriceType());
                goodsInfo.setGoodsUnit(goods.getGoodsUnit());
                goodsInfo.setBrandId(goods.getBrandId());
                goodsInfo.setCateId(goods.getCateId());
                goodsInfo.setGoodsCubage(goods.getGoodsCubage());
                goodsInfo.setGoodsWeight(goods.getGoodsWeight());
                goodsInfo.setFreightTempId(goods.getFreightTempId());

                //为空，则以商品主图
                if (StringUtils.isEmpty(goodsInfo.getGoodsInfoImg())) {
                    goodsInfo.setGoodsInfoImg(goods.getGoodsImg());
                }

                if (Objects.equals(DeleteFlag.NO, goodsInfo.getDelFlag())
                        && Objects.equals(CheckStatus.CHECKED, goodsInfo.getAuditStatus())
                        && Objects.equals(DefaultFlag.YES.toValue(), buildGoodsInfoVendibility(goodsInfo))) {
                    goodsInfo.setGoodsStatus(GoodsStatus.OK);
                    if (Objects.isNull(goodsInfo.getStock()) || goodsInfo.getStock() < 1) {
                        goodsInfo.setGoodsStatus(GoodsStatus.OUT_STOCK);
                    }
                } else {
                    goodsInfo.setGoodsStatus(GoodsStatus.INVALID);
                }
            } else {
                goodsInfo.setDelFlag(DeleteFlag.YES);
                goodsInfo.setGoodsStatus(GoodsStatus.INVALID);
            }
        });
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = goodses.stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .collect(Collectors.toList());
        List<QueryItemInventoryResponse.Item> stocks = null;
        if (itemIds.size() > 0) {
            stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
        }
        if (stocks != null) {
            for (Goods goods : goodses) {
                if (ThirdPlatformType.LINKED_MALL.equals(goods.getThirdPlatformType())) {
                    Optional<QueryItemInventoryResponse.Item> optional = stocks.stream()
                            .filter(v -> v.getItemId().equals(Long.valueOf(goods.getThirdPlatformSpuId())))
                            .findFirst();
                    if (optional.isPresent()) {
                        Long totalStock = optional.get().getSkuList().stream()
                                .map(v -> v.getInventory().getQuantity())
                                .reduce(0L, ((aLong, aLong2) -> aLong + aLong2));
                        goods.setStock(totalStock);
                    }
                }
            }
            for (GoodsInfo goodsInfo : microPage.getContent()) {
                for (QueryItemInventoryResponse.Item spuStock : stocks) {
                    Optional<QueryItemInventoryResponse.Item.Sku> stock = spuStock.getSkuList().stream().filter(v -> String.valueOf(spuStock.getItemId()).equals(goodsInfo.getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(goodsInfo.getThirdPlatformSkuId())).findFirst();
                    if (stock.isPresent()) {
//                        goodsInfo.setStock(stock.get().getInventory().getQuantity());
                        Long quantity = stock.get().getInventory().getQuantity();
                        goodsInfo.setStock(quantity);
                        if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                            goodsInfo.setGoodsStatus(quantity > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                        }
                    }
                }
            }
        }
        updateGoodsInfoSupplyPriceAndStock(microPage.getContent());
        return GoodsInfoResponse.builder().goodsInfoPage(microPage)
                .goodses(goodses)
                .cates(cates)
                .brands(brands).build();
    }

    /**
     * 根据ID批量查询商品SKU
     *
     * @param infoRequest 查询参数
     * @return 商品列表响应
     */
    @Transactional(readOnly = true, timeout = 10, propagation = Propagation.REQUIRES_NEW)
    public GoodsInfoResponse findSkuByIds(GoodsInfoRequest infoRequest) {
        if (CollectionUtils.isEmpty(infoRequest.getGoodsInfoIds())) {
            return GoodsInfoResponse.builder().goodsInfos(new ArrayList<>()).goodses(new ArrayList<>()).build();
        }
        //批量查询SKU信息列表
        GoodsInfoQueryRequest queryRequest = new GoodsInfoQueryRequest();
        queryRequest.setGoodsInfoIds(infoRequest.getGoodsInfoIds());
        queryRequest.setStoreId(infoRequest.getStoreId());
        if (infoRequest.getDeleteFlag() != null) {
            queryRequest.setDelFlag(infoRequest.getDeleteFlag().toValue());
        }

        List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(queryRequest.getWhereCriteria());


        if (CollectionUtils.isEmpty(goodsInfos)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST, new Object[]{goodsInfos});
        }

        List<String> goodsInfoIds = new ArrayList<>();
        List<String> goodsIds = new ArrayList<>();
        //取redis库存。
        Map<String, Long> stockMap = goodsInfos.stream().peek(goodsInfo -> {
            goodsIds.add(goodsInfo.getGoodsId());
            goodsInfoIds.add(goodsInfo.getGoodsInfoId());
            String stock = redisService.getString(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfo.getGoodsInfoId());
            if (StringUtils.isNotBlank(stock)) {
                goodsInfo.setStock(NumberUtils.toLong(stock));
            }
        }).collect(Collectors.toMap(GoodsInfo::getGoodsInfoId, GoodsInfo::getStock));

        EnterprisePriceGetRequest request = new EnterprisePriceGetRequest();
        request.setCustomerId(infoRequest.getCustomerId());
        request.setGoodsInfoIds(goodsInfoIds);
        request.setListFlag(true);

        //批量查询SPU信息列表
        GoodsQueryRequest goodsQueryRequest = new GoodsQueryRequest();
        goodsQueryRequest.setGoodsIds(goodsIds);
        List<Goods> goodses = goodsRepository.findAll(goodsQueryRequest.getWhereCriteria());
        if (CollectionUtils.isEmpty(goodses)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        Map<String, Goods> goodsMap = goodses.stream().collect(Collectors.toMap(Goods::getGoodsId, g -> g));


        List<String> skuIds = goodsInfos.stream().map(GoodsInfo::getGoodsInfoId).collect(Collectors.toList());
        //对每个SKU填充规格和规格值关系
        Map<String, List<GoodsInfoSpecDetailRel>> specDetailMap = new HashMap<>();
        //如果需要规格值，则查询
        if (Constants.yes.equals(infoRequest.getIsHavSpecText())) {
            specDetailMap.putAll(goodsInfoSpecDetailRelRepository.findByAllGoodsInfoIds(skuIds).stream().collect(Collectors.groupingBy(GoodsInfoSpecDetailRel::getGoodsInfoId)));
        }

        //如果是供应商商品则实时查
        this.updateGoodsInfoSupplyPriceAndStock(goodsInfos);
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = goodsInfos.stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .distinct()
                .collect(Collectors.toList());
        List<QueryItemInventoryResponse.Item> stocks = null;
        if (itemIds.size() > 0) {
            stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
        }
        if (stocks != null) {
            for (GoodsInfo goodsInfo : goodsInfos) {
                if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {
                    Optional<QueryItemInventoryResponse.Item> spuStock = stocks.stream().filter(v -> String.valueOf(v.getItemId()).equals(goodsInfo.getThirdPlatformSpuId())).findFirst();
                    if (spuStock.isPresent()) {
                        Optional<QueryItemInventoryResponse.Item.Sku> skuStock = spuStock.get().getSkuList().stream().filter(v -> String.valueOf(v.getSkuId()).equals(goodsInfo.getThirdPlatformSkuId())).findFirst();
                        if (skuStock.isPresent()) {
                            Long quantity = skuStock.get().getInventory().getQuantity();
                            goodsInfo.setStock(quantity);
                            if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                                goodsInfo.setGoodsStatus(quantity > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                            }
                        }
                    }
                }
            }
            for (Goods goods : goodses) {
                if (ThirdPlatformType.LINKED_MALL.equals(goods.getThirdPlatformType())) {
                    Optional<QueryItemInventoryResponse.Item> optional = stocks.stream().filter(v -> String.valueOf(v.getItemId()).equals(goods.getThirdPlatformSpuId())).findFirst();
                    if (optional.isPresent()) {
                        Long spuStock = optional.get().getSkuList().stream()
                                .map(v -> v.getInventory().getQuantity())
                                .reduce(0L, (aLong, aLong2) -> aLong + aLong2);
                        goods.setStock(spuStock);
                    }
                }
            }
        }
        //遍历SKU，填充销量价、商品状态
        goodsInfos.forEach(goodsInfo -> {
            goodsInfo.setSalePrice(goodsInfo.getMarketPrice() == null ? BigDecimal.ZERO : goodsInfo.getMarketPrice());
            Goods goods = goodsMap.get(goodsInfo.getGoodsId());
            if (goods != null) {
                //建立扁平化数据
                if (goods.getGoodsInfoIds() == null) {
                    goods.setGoodsInfoIds(new ArrayList<>());
                }
                goods.getGoodsInfoIds().add(goodsInfo.getGoodsInfoId());
                goodsInfo.setPriceType(goods.getPriceType());
                goodsInfo.setGoodsUnit(goods.getGoodsUnit());
                goodsInfo.setCateId(goods.getCateId());
                goodsInfo.setBrandId(goods.getBrandId());
                goodsInfo.setGoodsCubage(goods.getGoodsCubage());
                goodsInfo.setGoodsWeight(goods.getGoodsWeight());
                goodsInfo.setFreightTempId(goods.getFreightTempId());
                goodsInfo.setGoodsEvaluateNum(goods.getGoodsEvaluateNum());
                goodsInfo.setGoodsSalesNum(goods.getGoodsSalesNum());
                goodsInfo.setGoodsCollectNum(goods.getGoodsCollectNum());
                goodsInfo.setGoodsFavorableCommentNum(goods.getGoodsFavorableCommentNum());
                goodsInfo.setThirdPlatformSpuId(goods.getThirdPlatformSpuId());

                Long stock = stockMap.get(goodsInfo.getGoodsInfoId());
                goodsInfo.setStock(stock);


                //redis库存
                //为空，则以商品主图
                if (StringUtils.isEmpty(goodsInfo.getGoodsInfoImg())) {
                    goodsInfo.setGoodsInfoImg(goods.getGoodsImg());
                }

                //填充规格值
                if (MapUtils.isNotEmpty(specDetailMap) && Constants.yes.equals(goods.getMoreSpecFlag())) {
                    goodsInfo.setSpecText(StringUtils.join(specDetailMap.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream()
                            .map(GoodsInfoSpecDetailRel::getDetailName)
                            .collect(Collectors.toList()), " "));
                }

                if (Objects.equals(DeleteFlag.NO, goodsInfo.getDelFlag())
                        && Objects.equals(CheckStatus.CHECKED, goodsInfo.getAuditStatus()) && Objects.equals(AddedFlag.YES.toValue(), goodsInfo.getAddedFlag())
                        && Objects.equals(DefaultFlag.YES.toValue(), buildGoodsInfoVendibility(goodsInfo))) {
                    goodsInfo.setGoodsStatus(GoodsStatus.OK);
                    if (Objects.isNull(goodsInfo.getStock()) || goodsInfo.getStock() < 1) {
                        goodsInfo.setGoodsStatus(GoodsStatus.OUT_STOCK);
                    }
                } else {
                    goodsInfo.setGoodsStatus(GoodsStatus.INVALID);
                }
            } else {//不存在，则做为删除标记
                goodsInfo.setDelFlag(DeleteFlag.YES);
                goodsInfo.setGoodsStatus(GoodsStatus.INVALID);
            }
        });

        //定义响应结果
        GoodsInfoResponse responses = new GoodsInfoResponse();
        responses.setGoodsInfos(goodsInfos);
        responses.setGoodses(goodses);
        return responses;
    }

    /**
     * 供应商商品库导入的商品是否可售
     * <p>
     * 供应商供货商品SKU上增加展示商品可售状态
     * 供应商关店/过期、供应商下架/删除商品、平台禁售供应商商品，商品均是不可售状态
     * 前端商品列表展示、加购、下单时需判断商品可售状态
     *
     * @param goodsInfo
     * @return
     */
    public Integer buildGoodsInfoVendibility(GoodsInfo goodsInfo) {
//        如果是linkedmall商品，判断渠道启用开关
        if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {
            ThirdPlatformConfigResponse response = thirdPlatformConfigQueryProvider.get(
                    ThirdPlatformConfigByTypeRequest.builder().configType(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue()).build())
                    .getContext();
            if (response == null || Integer.valueOf(0).equals(response.getStatus())) {
                return Constants.no;
            }
        }
        Integer vendibility = Constants.yes;

        LocalDateTime now = LocalDateTime.now();

        String providerGoodsInfoId = goodsInfo.getProviderGoodsInfoId();

        if (StringUtils.isNotBlank(providerGoodsInfoId)) {

            GoodsInfo providerGoodsInfo = goodsInfoRepository.findById(providerGoodsInfoId).orElse(null);

            if (Objects.nonNull(providerGoodsInfo)) {
                if (!(Objects.equals(DeleteFlag.NO, providerGoodsInfo.getDelFlag())
                        && Objects.equals(CheckStatus.CHECKED, providerGoodsInfo.getAuditStatus())
                        && Objects.equals(AddedFlag.YES.toValue(), providerGoodsInfo.getAddedFlag()))) {
                    vendibility = Constants.no;
                }

                Long storeId = goodsInfo.getProviderId();

                if (Objects.nonNull(storeId)) {
                    StoreByIdResponse storeByIdResponse = storeQueryProvider.getById(StoreByIdRequest.builder().storeId(storeId).build()).getContext();
                    if (Objects.nonNull(storeByIdResponse)) {
                        StoreVO store = storeByIdResponse.getStoreVO();
                        if (!(
                                Objects.equals(DeleteFlag.NO, store.getDelFlag())
                                        && Objects.equals(StoreState.OPENING, store.getStoreState())
                                        && (now.isBefore(store.getContractEndDate()) || now.isEqual(store.getContractEndDate()))
                                        && (now.isAfter(store.getContractStartDate()) || now.isEqual(store.getContractStartDate()))
                        )) {
                            vendibility = Constants.no;
                        }
                    }
                }
            }
        }
        return vendibility;
    }

    /**
     * 根据ID查询商品SKU
     *
     * @param goodsInfoId 商品SKU编号
     * @return 商品SKU详情
     */
    @Transactional(readOnly = true, timeout = 10, propagation = Propagation.REQUIRES_NEW)
    public GoodsInfoEditResponse findById(String goodsInfoId) {
        GoodsInfoEditResponse response = new GoodsInfoEditResponse();
        GoodsInfo goodsInfo = goodsInfoRepository.findById(goodsInfoId).orElse(null);
        if (goodsInfo == null || DeleteFlag.YES.toValue() == goodsInfo.getDelFlag().toValue()) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }


        Goods goods = goodsRepository.findById(goodsInfo.getGoodsId()).orElseThrow(() -> new SbcRuntimeException(GoodsErrorCode.NOT_EXIST));

        //如果是多规格
        if (Constants.yes.equals(goods.getMoreSpecFlag())) {
            response.setGoodsSpecs(goodsSpecRepository.findByGoodsId(goods.getGoodsId()));
            response.setGoodsSpecDetails(goodsSpecDetailRepository.findByGoodsId(goods.getGoodsId()));

            //对每个规格填充规格值关系
            response.getGoodsSpecs().forEach(goodsSpec -> {
                goodsSpec.setSpecDetailIds(response.getGoodsSpecDetails().stream().filter(specDetail -> specDetail.getSpecId().equals(goodsSpec.getSpecId())).map(GoodsSpecDetail::getSpecDetailId).collect(Collectors.toList()));
            });

            //对每个SKU填充规格和规格值关系
            List<GoodsInfoSpecDetailRel> goodsInfoSpecDetailRels = goodsInfoSpecDetailRelRepository.findByGoodsId(goods.getGoodsId());
            goodsInfo.setMockSpecIds(goodsInfoSpecDetailRels.stream().filter(detailRel -> detailRel.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).map(GoodsInfoSpecDetailRel::getSpecId).collect(Collectors.toList()));
            goodsInfo.setMockSpecDetailIds(goodsInfoSpecDetailRels.stream().filter(detailRel -> detailRel.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).map(GoodsInfoSpecDetailRel::getSpecDetailId).collect(Collectors.toList()));
            goodsInfo.setSpecText(StringUtils.join(goodsInfoSpecDetailRels.stream().filter(specDetailRel -> goodsInfo.getGoodsInfoId().equals(specDetailRel.getGoodsInfoId())).map(GoodsInfoSpecDetailRel::getDetailName).collect(Collectors.toList()), " "));
        }
        updateGoodsInfoSupplyPriceAndStock(goodsInfo);
        //如果是linkedmall商品，实时查库存
        if (ThirdPlatformType.LINKED_MALL.equals(goods.getThirdPlatformType())) {
            List<QueryItemInventoryResponse.Item> stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(Collections.singletonList(Long.valueOf(goods.getThirdPlatformSpuId())), "0", null)).getContext();
            if (stocks != null) {
                if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {
                    for (QueryItemInventoryResponse.Item spuStock : stocks) {
                        Optional<QueryItemInventoryResponse.Item.Sku> stock = spuStock.getSkuList().stream()
                                .filter(v -> String.valueOf(spuStock.getItemId()).equals(goodsInfo.getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(goodsInfo.getThirdPlatformSkuId()))
                                .findFirst();
                        if (stock.isPresent()) {
                            Long skuStock = stock.get().getInventory().getQuantity();
                            goodsInfo.setStock(skuStock);
                            if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                                goodsInfo.setGoodsStatus(skuStock > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                            }
                        }
                    }
                }
                Optional<QueryItemInventoryResponse.Item> optional = stocks.stream().filter(v -> String.valueOf(v.getItemId()).equals(goods.getThirdPlatformSpuId())).findFirst();
                if (optional.isPresent()) {
                    Long spuStock = optional.get().getSkuList().stream()
                            .map(v -> v.getInventory().getQuantity())
                            .reduce(0L, (aLong, aLong2) -> aLong + aLong2);
                    goods.setStock(spuStock);
                }
            }
        }
        response.setGoodsInfo(goodsInfo);
        response.setGoods(goods);

        //商品按订货区间，查询订货区间
        if (Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(goods.getPriceType())) {
            response.setGoodsIntervalPrices(goodsIntervalPriceRepository.findSkuByGoodsInfoId(goodsInfo.getGoodsInfoId()));
        } else if (Integer.valueOf(GoodsPriceType.CUSTOMER.toValue()).equals(goods.getPriceType())) {
            response.setGoodsLevelPrices(goodsLevelPriceRepository.findSkuByGoodsInfoId(goodsInfo.getGoodsInfoId()));
            //如果是按单独客户定价
            if (Constants.yes.equals(goodsInfo.getCustomFlag())) {
                response.setGoodsCustomerPrices(goodsCustomerPriceRepository.findSkuByGoodsInfoId(goodsInfo.getGoodsInfoId()));
            }
        }
        response.setImages(goodsImageRepository.findByGoodsId(goods.getGoodsId()));
        return response;
    }


    public void updateGoodsInfoSupplyPriceAndStock(GoodsInfo goodsInfo) {
        //供应商库存
        if (StringUtils.isNotBlank(goodsInfo.getProviderGoodsInfoId()) && goodsInfo.getThirdPlatformType() == null) {
            GoodsInfo providerGoodsInfo = goodsInfoRepository.findById(goodsInfo.getProviderGoodsInfoId()).orElse(null);
            if (providerGoodsInfo != null) {
                goodsInfo.setStock(providerGoodsInfo.getStock());
                goodsInfo.setSupplyPrice(providerGoodsInfo.getSupplyPrice());
                goodsInfo.setVendibility(getstatus(providerGoodsInfo));
            }
        }
    }

    /**
     * 实时查询供应商商品List库存
     *
     * @param goodsInfoList
     */
    public void updateGoodsInfoSupplyPriceAndStock(List<GoodsInfo> goodsInfoList) {
        List<String> providerGoodsInfoIds = goodsInfoList.stream()
                .filter(goodsInfo -> Objects.nonNull(goodsInfo) && StringUtils.isNotBlank(goodsInfo.getProviderGoodsInfoId()) && (!Constants.no.equals(goodsInfo.getVendibility())))
                .map(GoodsInfo::getProviderGoodsInfoId).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(providerGoodsInfoIds)) {
            List<GoodsInfo> providerGoodsInfoList = goodsInfoRepository.findByGoodsInfoIds(providerGoodsInfoIds);

            List<Long> storeIds = providerGoodsInfoList.stream().map(GoodsInfo::getStoreId).collect(Collectors.toList());
            Map<Long, StoreVO> storeMap = storeQueryProvider.listStorePartColsByIds(StorePartColsListByIdsRequest.builder()
                    .storeIds(storeIds).cols(Arrays.asList("storeId", "delFlag", "storeState", "contractStartDate", "contractEndDate")).build())
                    .getContext().getStoreVOList().stream().collect(Collectors.toMap(StoreVO::getStoreId, s -> s));
            Map<String, GoodsInfo> skuMap = providerGoodsInfoList.stream().collect(Collectors.toMap(GoodsInfo::getGoodsInfoId, g -> g));

            goodsInfoList.forEach(goodsInfo -> {
                if (StringUtils.isNotBlank(goodsInfo.getProviderGoodsInfoId())) {
                    GoodsInfo providerGoodsInfo = skuMap.get(goodsInfo.getProviderGoodsInfoId());
                    if (Objects.nonNull(providerGoodsInfo)) {
                        goodsInfo.setStock(providerGoodsInfo.getStock());
                        goodsInfo.setSupplyPrice(providerGoodsInfo.getSupplyPrice());
                        goodsInfo.setVendibility(this.getStatus(providerGoodsInfo, storeMap.get(providerGoodsInfo.getStoreId())));
                    }
                }
            });
        }
    }


    /**
     * 批量查询营销价
     *
     * @param goodsInfoNos
     * @return
     */
    public List<GoodsMarketingPrice> findMarketingPriceByNos(List<String> goodsInfoNos, Long storeId) {
        List<GoodsMarketingPrice> list = goodsInfoRepository.marketingPriceByNos(goodsInfoNos, storeId);
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<String> skuIds = list.stream().map(GoodsMarketingPrice::getGoodsInfoId).collect(Collectors.toList());

        //设置规格值
        Map<String, List<GoodsInfoSpecDetailRel>> specDetailMap = new HashMap<>();
        specDetailMap.putAll(goodsInfoSpecDetailRelRepository.findByAllGoodsInfoIds(skuIds).stream().collect(
                Collectors.groupingBy(GoodsInfoSpecDetailRel::getGoodsInfoId)));
        list.forEach(i -> {
            i.setSpecText(StringUtils.join(specDetailMap.getOrDefault(i.getGoodsInfoId(), new ArrayList<>()).stream()
                    .map(GoodsInfoSpecDetailRel::getDetailName)
                    .collect(Collectors.toList()), " "));
        });
        return list;
    }

    /**
     * 根据供应商商品 赋值 可售状态
     *
     * @param providerGoodsInfo
     * @return
     */
    private Integer getstatus(GoodsInfo providerGoodsInfo) {
        LocalDateTime now = LocalDateTime.now();
        if (!(Objects.equals(DeleteFlag.NO, providerGoodsInfo.getDelFlag())
                && Objects.equals(CheckStatus.CHECKED, providerGoodsInfo.getAuditStatus()) && Objects.equals(AddedFlag.YES.toValue(), providerGoodsInfo.getAddedFlag()))) {
            return Constants.no;
        }

        StoreVO store = storeQueryProvider.getById(StoreByIdRequest.builder().storeId(providerGoodsInfo.getStoreId()).build()).getContext().getStoreVO();
        if (!(store != null
                && Objects.equals(DeleteFlag.NO, store.getDelFlag())
                && Objects.equals(StoreState.OPENING, store.getStoreState())
                && (now.isBefore(store.getContractEndDate()) || now.isEqual(store.getContractEndDate()))
                && (now.isAfter(store.getContractStartDate()) || now.isEqual(store.getContractStartDate()))
        )) {
            return Constants.no;
        }
        return Constants.yes;
    }


    /**
     * 根据供应商商品 赋值 可售状态
     *
     * @param providerGoodsInfo
     * @return
     */
    private Integer getStatus(GoodsInfo providerGoodsInfo, StoreVO store) {
        LocalDateTime now = LocalDateTime.now();
        if (!(Objects.equals(DeleteFlag.NO, providerGoodsInfo.getDelFlag())
                && Objects.equals(CheckStatus.CHECKED, providerGoodsInfo.getAuditStatus()) && Objects.equals(AddedFlag.YES.toValue(), providerGoodsInfo.getAddedFlag()))) {
            return Constants.no;
        }

        if (!(store != null
                && Objects.equals(DeleteFlag.NO, store.getDelFlag())
                && Objects.equals(StoreState.OPENING, store.getStoreState())
                && (now.isBefore(store.getContractEndDate()) || now.isEqual(store.getContractEndDate()))
                && (now.isAfter(store.getContractStartDate()) || now.isEqual(store.getContractStartDate()))
        )) {
            return Constants.no;
        }
        return Constants.yes;
    }

    /**
     * 商品删除
     *
     * @param goodsInfoIds 商品skuId列表
     */
    @Transactional
    public void delete(List<String> goodsInfoIds) {

        // 1.删除sku相关信息
        goodsInfoRepository.deleteByGoodsInfoIds(goodsInfoIds);

        // 2.查询仅包含当前skus的规格值，并删除
        // 2.1.按规格值分组删除的商品总数
        List<GoodsInfoSpecDetailRel> specDetailRels = goodsInfoSpecDetailRelRepository.findByGoodsInfoIds(goodsInfoIds);
        if (CollectionUtils.isNotEmpty(specDetailRels)) {
            Map<Long, Long> specDetailMap = specDetailRels.stream()
                    .collect(Collectors.groupingBy(GoodsInfoSpecDetailRel::getSpecDetailId, Collectors.counting()));
            // 2.2.查询各规格值下的商品总数
            List<GoodsInfoSpecDetailRel> specDetailAllRels = goodsInfoSpecDetailRelRepository.findBySpecDetailIds(specDetailMap.keySet());
            Map<Long, Long> specDetailAllMap = specDetailAllRels.stream()
                    .collect(Collectors.groupingBy(GoodsInfoSpecDetailRel::getSpecDetailId, Collectors.counting()));
            // 2.3.判断规格值商品数是否相等，相等则删除规格值
            List<Long> specDetailIds = specDetailMap.keySet().stream().filter(specDetailId ->
                    specDetailMap.get(specDetailId).equals(specDetailAllMap.get(specDetailId))
            ).collect(Collectors.toList());
            if (specDetailIds.size() != 0) {
                goodsSpecDetailRepository.deleteBySpecDetailIds(specDetailIds);
            }
        }


        // 3.删除sku和规格值的关联关系
        goodsInfoSpecDetailRelRepository.deleteByGoodsInfoIds(goodsInfoIds);

        // 4.查找不完整包含的spu
        List<String> goodsIds = this.findByIds(goodsInfoIds).stream()
                .map(GoodsInfo::getGoodsId)
                .distinct().collect(Collectors.toList());
        List<String> partContainGoodsIds = goodsInfoRepository.findByGoodsIds(goodsIds).stream()
                .filter(goodsInfo -> !goodsInfoIds.contains(goodsInfo.getGoodsInfoId()))
                .map(GoodsInfo::getGoodsId)
                .distinct().collect(Collectors.toList());
        // 5.反找出完整包含的spu
        goodsIds.removeAll(partContainGoodsIds);


        // 6.删除完整包含的spu
        if (goodsIds.size() != 0) {
            goodsRepository.deleteByGoodsIds(goodsIds);
            goodsPropDetailRelRepository.deleteByGoodsIds(goodsIds);
            goodsSpecRepository.deleteByGoodsIds(goodsIds);
            goodsSpecDetailRepository.deleteByGoodsIds(goodsIds);
            standardGoodsRelRepository.deleteByGoodsIds(goodsIds);
        }

        // 6.更新不完整包含的spu的上下架状态
        this.updateGoodsAddedFlag(partContainGoodsIds);

    }


    /**
     * 商品SKU更新
     *
     * @param saveRequest sku信息
     * @return 商品信息
     */
    @Transactional
    public GoodsInfo edit(GoodsInfoSaveRequest saveRequest) {
        GoodsInfo newGoodsInfo = saveRequest.getGoodsInfo();
        GoodsInfo oldGoodsInfo = goodsInfoRepository.findById(newGoodsInfo.getGoodsInfoId()).orElse(null);
        if (oldGoodsInfo == null || oldGoodsInfo.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        Goods goods = goodsRepository.findById(oldGoodsInfo.getGoodsId()).orElse(null);
        if (goods == null) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        infoQueryRequest.setGoodsInfoNos(Collections.singletonList(newGoodsInfo.getGoodsInfoNo()));
        infoQueryRequest.setNotGoodsInfoId(oldGoodsInfo.getGoodsInfoId());
        //验证SKU编码重复
        if (goodsInfoRepository.count(infoQueryRequest.getWhereCriteria()) > 0) {
            throw new SbcRuntimeException(GoodsErrorCode.SKU_NO_EXIST);
        }

        //如果勾选了定时上架时间
        if (Boolean.TRUE.equals(newGoodsInfo.getAddedTimingFlag())) {
            if (newGoodsInfo.getAddedTimingTime() != null) {
                if (newGoodsInfo.getAddedTimingTime().compareTo(LocalDateTime.now()) > 0) {
                    newGoodsInfo.setAddedFlag(AddedFlag.NO.toValue());
                } else {
                    newGoodsInfo.setAddedFlag(AddedFlag.YES.toValue());
                }
            }
        }

        //分析同一SPU的SKU上下架状态，去更新SPU上下架状态
        GoodsInfoQueryRequest queryRequest = new GoodsInfoQueryRequest();
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setGoodsId(oldGoodsInfo.getGoodsId());
        queryRequest.setNotGoodsInfoId(newGoodsInfo.getGoodsInfoId());
        List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(queryRequest.getWhereCriteria());
        goodsInfos.add(newGoodsInfo);
        Map<Integer, List<GoodsInfo>> skuMapGroupAddedFlag = goodsInfos.stream()
                .peek(g -> {
                    if (Objects.isNull(g.getAddedFlag())) {
                        g.setAddedFlag(AddedFlag.NO.toValue());
                    }
                })
                .collect(Collectors.groupingBy(GoodsInfo::getAddedFlag));

        Integer oldGoodsAddedFalg = goods.getAddedFlag();
        long len = goodsInfos.size();
        List<GoodsInfo> empty = new ArrayList<>();
        if (skuMapGroupAddedFlag.getOrDefault(AddedFlag.NO.toValue(), empty).size() == len) {
            goods.setAddedFlag(AddedFlag.NO.toValue());
        } else if (skuMapGroupAddedFlag.getOrDefault(AddedFlag.YES.toValue(), empty).size() == len) {
            goods.setAddedFlag(AddedFlag.YES.toValue());
        } else {
            goods.setAddedFlag(AddedFlag.PART.toValue());
        }
        goods.setSkuMinMarketPrice(goodsInfos.stream().filter(s -> Objects.nonNull(s.getMarketPrice()))
                .map(GoodsInfo::getMarketPrice)
                .reduce(BinaryOperator.minBy(BigDecimal::compareTo))
                .orElse(goods.getMarketPrice()));
        //如果只有一个sku
        if (len == 1) {
            goods.setAddedTimingFlag(newGoodsInfo.getAddedTimingFlag());
            goods.setAddedTimingTime(newGoodsInfo.getAddedTimingTime());
        }

        LocalDateTime currDate = LocalDateTime.now();
        //更新商品
        goods.setUpdateTime(currDate);
        //当上下架状态不一致，更新上下架时间
        if (!oldGoodsAddedFalg.equals(goods.getAddedFlag())) {
            goods.setAddedTime(currDate);
        }

        //更新商品SKU
        if (newGoodsInfo.getStock() == null) {
            newGoodsInfo.setStock(0L);
        }
        //当上下架状态不一致，更新上下架时间
        if (!oldGoodsInfo.getAddedFlag().equals(newGoodsInfo.getAddedFlag())) {
            newGoodsInfo.setAddedTime(currDate);
        }
        newGoodsInfo.setUpdateTime(currDate);
        if (Objects.nonNull(oldGoodsInfo.getCommissionRate())) {
            newGoodsInfo.setDistributionCommission(newGoodsInfo.getMarketPrice().multiply(oldGoodsInfo.getCommissionRate()));
        }
        KsBeanUtil.copyProperties(newGoodsInfo, oldGoodsInfo);
        goodsInfoRepository.save(oldGoodsInfo);
        goodsInfoStockService.initCacheStock(oldGoodsInfo.getStock(), oldGoodsInfo.getGoodsInfoId());

        //重新计算库存
        Long newStock = goodsInfos.stream().filter(s -> Objects.nonNull(s.getStock()) && (!s.getGoodsInfoId().equals(newGoodsInfo.getGoodsInfoId()))).mapToLong(GoodsInfo::getStock).sum();
        newStock += newGoodsInfo.getStock();
        goods.setStock(newStock);
        goodsRepository.save(goods);

        //更新标准库商品库供货价
        String goodsId = oldGoodsInfo.getGoodsId();
        StandardGoodsRel standardGoodsRel = standardGoodsRelRepository.findByGoodsId(goodsId);
        if (standardGoodsRel != null) {
            String standardId = standardGoodsRel.getStandardId();
            List<StandardGoods> standardGoodsList = standardGoodsRepository.findByGoodsIdIn(Arrays.asList(standardId));

            if (CollectionUtils.isNotEmpty(standardGoodsList)) {
                for (StandardGoods standardGoods : standardGoodsList) {
                    standardGoods.setSupplyPrice(oldGoodsInfo.getSupplyPrice());
                    standardGoodsRepository.save(standardGoods);
                }
            }
        }
        //更新商家商品库供货价等
        List<GoodsInfo> goodsInfoList = goodsInfoRepository.findByProviderGoodsInfoId(oldGoodsInfo.getGoodsInfoId());
        if (CollectionUtils.isNotEmpty(goodsInfoList)) {
            goodsInfoList.stream().forEach(supplierGoodsInfo -> {
                supplierGoodsInfo.setSupplyPrice(oldGoodsInfo.getSupplyPrice());
                supplierGoodsInfo.setStock(oldGoodsInfo.getStock());
//            supplierGoodsInfo.setAddedFlag(oldGoodsInfo.getAddedFlag());
//            supplierGoodsInfo.setGoodsInfoBarcode(oldGoodsInfo.getGoodsInfoBarcode());
//            supplierGoodsInfo.setGoodsInfoNo(oldGoodsInfo.getGoodsInfoNo());
                goodsInfoRepository.save(supplierGoodsInfo);
            });
        }

        //更新代销商品可售状态
        ProviderGoodsNotSellRequest request = new ProviderGoodsNotSellRequest();
        request.setStockFlag(Boolean.TRUE);
        request.setGoodsIds(Lists.newArrayList(goods.getGoodsId()));
        if (!oldGoodsAddedFalg.equals(goods.getAddedFlag())) {
            //上下架更改商家代销商品的可售性
            Boolean checkFlag = AddedFlag.NO.toValue() == newGoodsInfo.getAddedFlag() ? Boolean.FALSE : Boolean.TRUE;
            request.setCheckFlag(checkFlag);
            if (AddedFlag.PART.toValue() == goods.getAddedFlag()) {
                request.setGoodsInfoIds(Lists.newArrayList(newGoodsInfo.getGoodsInfoId()));
            }
        } else {
            Boolean checkFlag = AddedFlag.NO.toValue() == newGoodsInfo.getAddedFlag() ? Boolean.FALSE : Boolean.TRUE;
            request.setCheckFlag(checkFlag);
            request.setGoodsInfoIds(Lists.newArrayList(newGoodsInfo.getGoodsInfoId()));
        }
//        goodsService.dealGoodsVendibility(request);
        return oldGoodsInfo;
    }

    /**
     * 更新SKU设价
     *
     * @param saveRequest sku设价信息
     */
    @Transactional
    public GoodsInfo editPrice(GoodsInfoSaveRequest saveRequest) {
        GoodsInfo newGoodsInfo = saveRequest.getGoodsInfo();
        GoodsInfo oldGoodsInfo = goodsInfoRepository.findById(newGoodsInfo.getGoodsInfoId()).orElse(null);
        if (oldGoodsInfo == null || oldGoodsInfo.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        Goods goods = goodsRepository.findById(oldGoodsInfo.getGoodsId()).orElseThrow(() -> new SbcRuntimeException(GoodsErrorCode.NOT_EXIST));

        //传递设价内部值
        oldGoodsInfo.setCustomFlag(newGoodsInfo.getCustomFlag());
        oldGoodsInfo.setLevelDiscountFlag(newGoodsInfo.getLevelDiscountFlag());
        goodsInfoRepository.save(oldGoodsInfo);

        //先删除设价数据
        goodsIntervalPriceRepository.deleteByGoodsInfoId(newGoodsInfo.getGoodsInfoId());
        goodsLevelPriceRepository.deleteByGoodsInfoId(newGoodsInfo.getGoodsInfoId());
        goodsCustomerPriceRepository.deleteByGoodsInfoId(newGoodsInfo.getGoodsInfoId());

        //按订货量设价，保存订货区间
        List<GoodsIntervalPrice> goodsIntervalPrices = null;
        if (Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(goods.getPriceType())) {
            goodsIntervalPrices = saveRequest.getGoodsIntervalPrices();
            if (CollectionUtils.isEmpty(saveRequest.getGoodsIntervalPrices()) || saveRequest.getGoodsIntervalPrices().stream().filter(intervalPrice -> intervalPrice.getCount() == 1).count() == 0) {
                GoodsIntervalPrice intervalPrice = new GoodsIntervalPrice();
                intervalPrice.setCount(1L);
                intervalPrice.setPrice(newGoodsInfo.getMarketPrice());
                if (saveRequest.getGoodsIntervalPrices() == null) {
                    saveRequest.setGoodsLevelPrices(new ArrayList<>());
                }
                saveRequest.getGoodsIntervalPrices().add(intervalPrice);
            }

            if (CollectionUtils.isNotEmpty(goodsIntervalPrices)) {
                goodsIntervalPrices.forEach(intervalPrice -> {
                    intervalPrice.setGoodsId(goods.getGoodsId());
                    intervalPrice.setGoodsInfoId(newGoodsInfo.getGoodsInfoId());
                    intervalPrice.setType(PriceType.SKU);
                });
                goodsIntervalPriceRepository.saveAll(goodsIntervalPrices);
            }
        } else {
            //否则按客户
            if (CollectionUtils.isNotEmpty(saveRequest.getGoodsLevelPrices())) {
                saveRequest.getGoodsLevelPrices().forEach(goodsLevelPrice -> {
                    goodsLevelPrice.setGoodsId(goods.getGoodsId());
                    goodsLevelPrice.setGoodsInfoId(newGoodsInfo.getGoodsInfoId());
                    goodsLevelPrice.setType(PriceType.SKU);
                });
                goodsLevelPriceRepository.saveAll(saveRequest.getGoodsLevelPrices());
            }

            //按客户单独定价
            if (Constants.yes.equals(newGoodsInfo.getCustomFlag()) && CollectionUtils.isNotEmpty(saveRequest.getGoodsCustomerPrices())) {
                saveRequest.getGoodsCustomerPrices().forEach(price -> {
                    price.setGoodsId(goods.getGoodsId());
                    price.setGoodsInfoId(newGoodsInfo.getGoodsInfoId());
                    price.setType(PriceType.SKU);
                });
                goodsCustomerPriceRepository.saveAll(saveRequest.getGoodsCustomerPrices());
            }
        }

        return this.edit(saveRequest);
    }

    /**
     * 更新商品上下架状态
     *
     * @param addedFlag    上下架状态
     * @param goodsInfoIds 商品skuId列表
     */
    @Transactional
    public void updateAddedStatus(Integer addedFlag, List<String> goodsInfoIds) {
        // 1.修改sku上下架状态
        goodsInfoRepository.updateAddedFlagByGoodsInfoIds(addedFlag, goodsInfoIds);

        // 2.修改spu上下架状态
        List<String> goodsIds = this.findByIds(goodsInfoIds).stream()
                .map(GoodsInfo::getGoodsId)
                .distinct().collect(Collectors.toList());
        this.updateGoodsAddedFlag(goodsIds);

    }

    /**
     * 根据SKU编号加库存
     *
     * @param stock       库存数
     * @param goodsInfoId SKU编号
     */
    @Transactional
    @GlobalTransactional
    public void addStockById(Long stock, String goodsInfoId) {

        // 更新商品库存时，判断其中商品是否来自供应商，来自供应商的商品，要改为更新供应商商品库存
        GoodsInfo goodsInfo = goodsInfoRepository.getOne(goodsInfoId);

        //关联供应商商品同时减库存，暂不维护商品库库存，导入商品时，与供应商商品一致，否则维护库存的地方太多。从系统设计到性能都是有影响的。
        if (StringUtils.isNotBlank(goodsInfo.getProviderGoodsInfoId())) {
            goodsInfoId = goodsInfo.getProviderGoodsInfoId();
        }

        goodsInfoStockService.addStockById(stock, goodsInfoId);

    }

    /**
     * 批量加库存
     *
     * @param dtoList 增量库存参数
     */
    @Transactional
    @GlobalTransactional
    public void batchAddStock(List<GoodsInfoPlusStockDTO> dtoList) {
        dtoList.forEach(dto -> addStockById(dto.getStock(), dto.getGoodsInfoId()));
        //sku定时es库存
        goodsInfoStockService.batchAddStock(dtoList);
    }

    /**
     * 根据SKU编号减库存
     *
     * @param stock       库存数
     * @param goodsInfoId SKU编号
     */
    @Transactional
    public void subStockById(Long stock, String goodsInfoId) {

        //更新商品库存时，判断其中商品是否来自供应商，来自供应商的商品，要改为更新供应商商品库存
        GoodsInfo goodsInfo = goodsInfoRepository.getOne(goodsInfoId);

        if (StringUtils.isNotBlank(goodsInfo.getProviderGoodsInfoId())) {
            goodsInfoId = goodsInfo.getProviderGoodsInfoId();
        }

        goodsInfoStockService.subStockById(stock, goodsInfoId);
    }

    /**
     * 批量减库存
     *
     * @param dtoList 减量库存参数
     */
    @Transactional
    @GlobalTransactional
    public void batchSubStock(List<GoodsInfoMinusStockDTO> dtoList) {
        dtoList.forEach(dto -> subStockById(dto.getStock(), dto.getGoodsInfoId()));
        //sku定时es库存
        goodsInfoStockService.batchSubStock(dtoList);
    }

    /**
     * 获取SKU详情
     *
     * @param skuId 商品skuId
     * @return 商品sku详情
     */
    public GoodsInfo findOne(String skuId) {
        GoodsInfo goodsInfo = this.goodsInfoRepository.findById(skuId).orElse(null);
        //如果是linkedmall商品，实时查库存
        if (Objects.nonNull(goodsInfo) && (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType()) || Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(goodsInfo.getGoodsSource()))) {
            List<QueryItemInventoryResponse.Item> stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(Collections.singletonList(Long.valueOf(goodsInfo.getThirdPlatformSpuId())), "0", null)).getContext();
            if (stocks != null) {
                String thirdPlatformSpuId = goodsInfo.getThirdPlatformSpuId();
                Optional<QueryItemInventoryResponse.Item> optional = stocks.stream().filter(v -> String.valueOf(v.getItemId()).equals(thirdPlatformSpuId)).findFirst();
                if (optional.isPresent()) {
                    String thirdPlatformSkuId = goodsInfo.getThirdPlatformSkuId();
                    Optional<QueryItemInventoryResponse.Item.Sku> skuStock = optional.get().getSkuList().stream().filter(v -> String.valueOf(v.getSkuId()).equals(thirdPlatformSkuId)).findFirst();
                    if (skuStock.isPresent()) {
                        Long quantity = skuStock.get().getInventory().getQuantity();
                        goodsInfo.setStock(quantity);
                        if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                            goodsInfo.setGoodsStatus(quantity > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                        }
                    }
                }
            }
        }
        return goodsInfo;
    }

    public Map<String, Object> findSimpleGoods(List<String> goodsInfoIds){
        List<GoodsInfo> goodsInfos;
        if(CollectionUtils.isEmpty(goodsInfoIds)) {
            goodsInfos = goodsInfoRepository.findSpuId();
        }else {
            goodsInfos = goodsInfoRepository.findSpuId(goodsInfoIds);
        }
        if(CollectionUtils.isEmpty(goodsInfos)){
            return new HashMap<>();
        }
        Map<String, Object> result = new HashMap<>();
        List<String> goodsInfoIds2 = new ArrayList<>();
        for (GoodsInfo goodsInfo : goodsInfos) {
            goodsInfoIds2.add(goodsInfo.getGoodsInfoId());
        }
        result.put("goodsId", goodsInfos.get(0).getGoodsId());
        result.put("goodsInfoIds", goodsInfoIds2);
        return result;
    }

    /**
     * 根据sku编号查询未删除的商品信息
     *
     * @param skuId
     * @return
     */
    public GoodsInfo findByGoodsInfoIdAndDelFlag(String skuId) {
        GoodsInfo goodsInfo = this.goodsInfoRepository.findByGoodsInfoIdAndDelFlag(skuId, DeleteFlag.NO);
        //如果是linkedmall商品，实时查库存
        if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType()) || Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(goodsInfo.getGoodsSource())) {
            List<QueryItemInventoryResponse.Item> stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(Collections.singletonList(Long.valueOf(goodsInfo.getThirdPlatformSpuId())), "0", null)).getContext();
            if (stocks != null) {
                String thirdPlatformSpuId = goodsInfo.getThirdPlatformSpuId();
                Optional<QueryItemInventoryResponse.Item> optional = stocks.stream().filter(v -> String.valueOf(v.getItemId()).equals(thirdPlatformSpuId)).findFirst();
                if (optional.isPresent()) {
                    String thirdPlatformSkuId = goodsInfo.getThirdPlatformSkuId();
                    Optional<QueryItemInventoryResponse.Item.Sku> skuStock = optional.get().getSkuList().stream().filter(v -> String.valueOf(v.getSkuId()).equals(thirdPlatformSkuId)).findFirst();
                    if (skuStock.isPresent()) {
                        Long quantity = skuStock.get().getInventory().getQuantity();
                        goodsInfo.setStock(quantity);
                        if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                            goodsInfo.setGoodsStatus(quantity > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                        }
                    }
                }

            }
        }
        return goodsInfo;
    }

    public GoodsInfo findByGoodsInfoIdAndStoreIdAndDelFlag(String skuId, Long storeId) {
        GoodsInfo goodsInfo = this.goodsInfoRepository.findByGoodsInfoIdAndStoreIdAndDelFlag(skuId, storeId, DeleteFlag.NO).orElse(null);
        //如果是linkedmall商品，实时查库存
        if (Objects.nonNull(goodsInfo) && (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType()) || Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(goodsInfo.getGoodsSource()))) {
            List<QueryItemInventoryResponse.Item> stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(Collections.singletonList(Long.valueOf(goodsInfo.getThirdPlatformSpuId())), "0", null)).getContext();
            if (stocks != null) {
                String thirdPlatformSpuId = goodsInfo.getThirdPlatformSpuId();
                Optional<QueryItemInventoryResponse.Item> optional = stocks.stream().filter(v -> String.valueOf(v.getItemId()).equals(thirdPlatformSpuId)).findFirst();
                if (optional.isPresent()) {
                    String thirdPlatformSkuId = goodsInfo.getThirdPlatformSkuId();
                    Optional<QueryItemInventoryResponse.Item.Sku> skuStock = optional.get().getSkuList().stream().filter(v -> String.valueOf(v.getSkuId()).equals(thirdPlatformSkuId)).findFirst();
                    if (skuStock.isPresent()) {
                        Long quantity = skuStock.get().getInventory().getQuantity();
                        goodsInfo.setStock(quantity);
                        if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                            goodsInfo.setGoodsStatus(quantity > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                        }
                    }
                }

            }
        }
        return goodsInfo;
    }

    /**
     * 根据id批量查询SKU数据
     *
     * @param skuIds 商品skuId
     * @return 商品sku列表
     */
    public List<GoodsInfo> findByIds(List<String> skuIds) {
        return this.goodsInfoRepository.findAllById(skuIds);
    }

    /**
     * 条件查询SKU数据
     *
     * @param request 查询条件
     * @return 商品sku列表
     */
    public List<GoodsInfo> findByParams(GoodsInfoQueryRequest request) {
        return this.goodsInfoRepository.findAll(request.getWhereCriteria());
    }

    /**
     * 填充商品的有效性
     *
     * @param goodsInfoList 填充后有效状态的商品列表数据
     */
    public List<GoodsInfo> fillGoodsStatus(List<GoodsInfo> goodsInfoList) {
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = goodsInfoList.stream().filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType())).map(v -> Long.valueOf(v.getThirdPlatformSpuId())).distinct().collect(Collectors.toList());
        if (itemIds.size() > 0) {
            List<QueryItemInventoryResponse.Item> stock = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
            if (stock != null) {
                for (GoodsInfo goodsInfo : goodsInfoList) {
                    Optional<QueryItemInventoryResponse.Item> spuStock = stock.stream().filter(v -> String.valueOf(v.getItemId()).equals(goodsInfo.getThirdPlatformSpuId())).findFirst();
                    if (spuStock.isPresent()) {
                        Optional<QueryItemInventoryResponse.Item.Sku> skuStock = spuStock.get().getSkuList().stream().filter(v -> String.valueOf(v.getSkuId()).equals(goodsInfo.getThirdPlatformSkuId())).findFirst();
                        if (skuStock.isPresent()) {
                            Long quantity = skuStock.get().getInventory().getQuantity();
                            goodsInfo.setStock(quantity);
                            if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                                goodsInfo.setGoodsStatus(quantity > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                            }
                        }
                    }
                }
            }
        }
        updateGoodsInfoSupplyPriceAndStock(goodsInfoList);
        Map<Long, StoreVO> storeVOMap = new HashMap<>();
        List<Long> storeIds = goodsInfoList.stream().map(GoodsInfo::getStoreId).collect(Collectors.toList());
        List<StoreVO> storeVOList = storeQueryProvider.listByIds(ListStoreByIdsRequest.builder().storeIds(storeIds).build()).getContext().getStoreVOList();
        if (CollectionUtils.isNotEmpty(storeVOList)) {
            storeVOMap.putAll(storeVOList.stream().collect(Collectors.toMap(StoreVO::getStoreId, s -> s, (k1, k2) -> k1)));
        }
        //验证商品，并设为无效
        goodsInfoList.stream().filter(goodsInfo -> !Objects.equals(GoodsStatus.INVALID, goodsInfo.getGoodsStatus())).forEach(goodsInfo -> {
            LocalDateTime now = LocalDateTime.now();
            StoreVO storeVO = storeVOMap.get(goodsInfo.getStoreId());
            //店铺是否失效 true失效，false未失效
            Boolean storeExpired = Boolean.FALSE;
            if (storeVO == null || DeleteFlag.YES.equals(storeVO.getDelFlag())
                    || !CheckState.CHECKED.equals(storeVO.getAuditState()) || StoreState.CLOSED.equals(storeVO.getStoreState()) ||
                    !((now.isBefore(storeVO.getContractEndDate()) || now.isEqual(storeVO.getContractEndDate()))
                            && (now.isAfter(storeVO.getContractStartDate()) || now.isEqual(storeVO.getContractStartDate())))) {
                storeExpired = Boolean.TRUE;
            }
            if (Objects.equals(DeleteFlag.NO, goodsInfo.getDelFlag())
                    && Objects.equals(CheckStatus.CHECKED, goodsInfo.getAuditStatus()) && Objects.equals(AddedFlag.YES.toValue(), goodsInfo.getAddedFlag())
                    && Objects.equals(DefaultFlag.YES.toValue(), buildGoodsInfoVendibility(goodsInfo)) && storeExpired.equals(Boolean.FALSE)) {
                goodsInfo.setGoodsStatus(GoodsStatus.OK);
                if (Objects.isNull(goodsInfo.getStock()) || goodsInfo.getStock() < 1) {
                    goodsInfo.setGoodsStatus(GoodsStatus.OUT_STOCK);
                }
            } else {
                goodsInfo.setGoodsStatus(GoodsStatus.INVALID);
            }
        });

        if (goodsInfoList.stream().filter(goodsInfo -> !Objects.equals(GoodsStatus.INVALID, goodsInfo.getGoodsStatus())).count() < 1) {
            return goodsInfoList;
        }

//        List<Long> storeIds = goodsInfoList.stream().map(GoodsInfo::getStoreId).distinct().collect(Collectors.toList());
//        ListNoDeleteStoreByIdsRequest listNoDeleteStoreByIdsRequest = new ListNoDeleteStoreByIdsRequest();
//        listNoDeleteStoreByIdsRequest.setStoreIds(storeIds);
//        BaseResponse<ListNoDeleteStoreByIdsResponse> listNoDeleteStoreByIdsResponseBaseResponse = storeQueryProvider.listNoDeleteStoreByIds(listNoDeleteStoreByIdsRequest);
//        Map<Long, StoreVO> storeMap = listNoDeleteStoreByIdsResponseBaseResponse.getContext().getStoreVOList().stream().collect(Collectors.toMap(StoreVO::getStoreId, s -> s));
//        LocalDateTime now = LocalDateTime.now();
//        goodsInfoList.stream().filter(goodsInfo -> !Objects.equals(GoodsStatus.INVALID, goodsInfo.getGoodsStatus())).forEach(goodsInfo -> {
//            StoreVO store = storeMap.get(goodsInfo.getStoreId());
//            if (!(store != null
//                    && Objects.equals(DeleteFlag.NO, store.getDelFlag())
//                    && Objects.equals(StoreState.OPENING, store.getStoreState())
//                    && (now.isBefore(store.getContractEndDate()) || now.isEqual(store.getContractEndDate()))
//                    && (now.isAfter(store.getContractStartDate()) || now.isEqual(store.getContractStartDate()))
//            )) {
//                goodsInfo.setGoodsStatus(GoodsStatus.INVALID);
//            }
//        });
        return goodsInfoList;
    }

    /**
     * 填充商品的是否可售
     *
     * @param skuIds 填充后有效状态的商品列表数据
     */
    public List<GoodsInfo> fillGoodsVendibility(List<String> skuIds) {

        if (CollectionUtils.isEmpty(skuIds)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        LocalDateTime now = LocalDateTime.now();

        List<GoodsInfo> goodsInfoList = findByIds(skuIds);

        //验证商品,设置是否可售 0 不可售 1 可售
        goodsInfoList.stream().forEach(goodsInfo -> {


            goodsInfo.setVendibility(Constants.yes);
            String providerGoodsInfoId = goodsInfo.getProviderGoodsInfoId();

            if (StringUtils.isNotBlank(providerGoodsInfoId)) {
                GoodsInfo providerGoodsInfo = findOne(providerGoodsInfoId);
                if (!(Objects.equals(DeleteFlag.NO, providerGoodsInfo.getDelFlag())
                        && Objects.equals(CheckStatus.CHECKED, providerGoodsInfo.getAuditStatus()) && Objects.equals(AddedFlag.YES.toValue(), providerGoodsInfo.getAddedFlag()))) {
                    goodsInfo.setVendibility(Constants.no);
                }

                StoreVO store = storeQueryProvider.getById(StoreByIdRequest.builder().storeId(providerGoodsInfo.getStoreId()).build()).getContext().getStoreVO();

                if (!(store != null
                        && Objects.equals(DeleteFlag.NO, store.getDelFlag())
                        && Objects.equals(StoreState.OPENING, store.getStoreState())
                        && (now.isBefore(store.getContractEndDate()) || now.isEqual(store.getContractEndDate()))
                        && (now.isAfter(store.getContractStartDate()) || now.isEqual(store.getContractStartDate()))
                )) {
                    goodsInfo.setVendibility(Constants.no);
                }

            }
        });

        return goodsInfoList;
    }

    /**
     * 刷新spu的上下架状态
     *
     * @param goodsIds 发生变化的spuId列表
     */
    private void updateGoodsAddedFlag(List<String> goodsIds) {
        // 1.查询所有的sku
        GoodsInfoQueryRequest queryRequest = new GoodsInfoQueryRequest();
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setGoodsIds(goodsIds);
        List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(queryRequest.getWhereCriteria());

        // 2.按spu分组
        Map<String, List<GoodsInfo>> goodsMap = goodsInfos.stream().collect(Collectors.groupingBy(GoodsInfo::getGoodsId));

        // 3.判断每个spu的上下架状态
        List<String> yesGoodsIds = new ArrayList<>(); // 上架的spu
        List<String> noGoodsIds = new ArrayList<>(); //  下架的spu
        List<String> partGoodsIds = new ArrayList<>(); // 部分上架的spu
        goodsMap.keySet().forEach(goodsId -> {
            List<GoodsInfo> skus = goodsMap.get(goodsId);
            Long skuCount = (long) (skus.size());
            Long yesCount = skus.stream().filter(sku -> sku.getAddedFlag() == AddedFlag.YES.toValue()).count();

            if (yesCount.equals(0L)) {
                // 下架
                noGoodsIds.add(goodsId);
            } else if (yesCount.equals(skuCount)) {
                // 上架
                yesGoodsIds.add(goodsId);
            } else {
                // 部分上架
                partGoodsIds.add(goodsId);
            }
        });

        // 4.修改spu上下架状态
        if (noGoodsIds.size() != 0) {
            goodsRepository.updateAddedFlagByGoodsIds(AddedFlag.NO.toValue(), noGoodsIds);
        }
        if (yesGoodsIds.size() != 0) {
            goodsRepository.updateAddedFlagByGoodsIds(AddedFlag.YES.toValue(), yesGoodsIds);
        }
        if (partGoodsIds.size() != 0) {
            goodsRepository.updateAddedFlagByGoodsIds(AddedFlag.PART.toValue(), partGoodsIds);
        }
    }

    /**
     * SKU分页
     *
     * @param queryRequest 查询请求
     * @return 商品分页列表
     */
    @Transactional(readOnly = true, timeout = 10)
    public Page<GoodsInfo> page(GoodsInfoQueryRequest queryRequest) {
        return goodsInfoRepository.findAll(queryRequest.getWhereCriteria(), queryRequest.getPageRequest());
    }

    /**
     * SKU统计
     *
     * @param queryRequest 查询请求
     * @return 统计个数
     */
    @Transactional(readOnly = true, timeout = 10)
    public long count(GoodsInfoQueryRequest queryRequest) {
        return goodsInfoRepository.count(queryRequest.getWhereCriteria());
    }

    /**
     * 更新小程序码
     *
     * @param request
     * @return
     */
    @Transactional
    public void updateSkuSmallProgram(GoodsInfoSmallProgramCodeRequest request) {
        goodsInfoRepository.updateSkuSmallProgram(request.getGoodsInfoId(), request.getCodeUrl());
    }

    @Transactional
    public void clearSkuSmallProgramCode() {
        goodsInfoRepository.clearSkuSmallProgramCode();
    }


    /**
     * 分页查询分销商品
     *
     * @param request 参数
     * @return DistributionGoodsQueryResponse
     */
    public DistributionGoodsQueryResponse distributionGoodsPage(DistributionGoodsQueryRequest request) {
        DistributionGoodsQueryResponse response = new DistributionGoodsQueryResponse();
        List<GoodsInfoSpecDetailRel> goodsInfoSpecDetails = new ArrayList<>();
        List<GoodsBrand> goodsBrandList = new ArrayList<>();
        List<GoodsCate> goodsCateList = new ArrayList<>();

        //获取该分类的所有子分类
        if (Objects.nonNull(request.getCateId()) && request.getCateId() > 0) {
            request.setCateIds(goodsCateService.getChlidCateId(request.getCateId()));
            if (CollectionUtils.isNotEmpty(request.getCateIds())) {
                request.getCateIds().add(request.getCateId());
                request.setCateId(null);
            }
        }

        //获取该店铺分类下的所有spuIds
//        List<String> goodsIdList = new ArrayList<>();
        if (Objects.nonNull(request.getStoreCateId()) && request.getStoreCateId() > 0) {
            List<StoreCateGoodsRela> relas = storeCateService.findAllChildRela(request.getStoreCateId(), true);
            if (CollectionUtils.isNotEmpty(relas)) {
                List<String> goodsIdList = relas.stream().map(StoreCateGoodsRela::getGoodsId).collect(Collectors.toList());
                request.setGoodsIds(goodsIdList);
            } else {
                return DistributionGoodsQueryResponse.builder().goodsInfoPage(new MicroServicePage<>(Collections.emptyList(), request.getPageRequest(), 0)).build();
            }
        }

        //标签搜索
        if (request.getLabelId() != null && request.getLabelId() > 0) {
            GoodsQueryRequest goodsQueryRequest = GoodsQueryRequest.builder().labelId(request.getLabelId()).build();
            List<Goods> goods = goodsRepository.findAll(goodsQueryRequest.getWhereCriteria());
            if (CollectionUtils.isEmpty(goods)) {
                return DistributionGoodsQueryResponse.builder().goodsInfoPage(new MicroServicePage<>(Collections.emptyList(), request.getPageRequest(), 0)).build();
            }
            List<String> spuId = goods.stream().map(Goods::getGoodsId).collect(Collectors.toList());
            //如果前面已经有条件了，取交集中的spuId  大条件上是and关系
            if (CollectionUtils.isNotEmpty(request.getGoodsIds())) {
                spuId = spuId.stream().filter(request.getGoodsIds()::contains).collect(Collectors.toList());
            }
            request.setGoodsIds(spuId);
        }

//        // 查询零售模式的spu，再过滤spuId
//        GoodsQueryRequest queryRequest = GoodsQueryRequest.builder().goodsIds(goodsIdList).storeId(request.getStoreId())
//                .delFlag(DeleteFlag.NO.toValue()).saleType(request.getSaleType()).build();
//        List<Goods> goodsRepositoryAll = goodsRepository.findAll(queryRequest.getWhereCriteria());
//        if (CollectionUtils.isNotEmpty(goodsRepositoryAll)) {
//            request.setGoodsIds(goodsRepositoryAll.stream().map(Goods::getGoodsId).collect(Collectors.toList()));
//        } else {
//            return DistributionGoodsQueryResponse.builder().goodsInfoPage(new MicroServicePage<>(Collections.emptyList(), request.getPageRequest(), 0)).build();
//        }

        //分页查询分销商品sku
        request.setDelFlag(DeleteFlag.NO.toValue());
        //按创建时间倒序、ID升序
        request.putSort("createTime", SortType.DESC.toValue());
        Page<GoodsInfo> goodsInfoPage = goodsInfoRepository.findAll(request.getWhereCriteria(), request.getPageRequest());
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = goodsInfoPage.getContent().stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .distinct()
                .collect(Collectors.toList());
        List<QueryItemInventoryResponse.Item> stocks = null;
        if (itemIds.size() > 0) {
            stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
        }
        if (stocks != null) {
            for (GoodsInfo goodsInfo : goodsInfoPage.getContent()) {
                for (QueryItemInventoryResponse.Item spuStock : stocks) {
                    Optional<QueryItemInventoryResponse.Item.Sku> stock = spuStock.getSkuList().stream().filter(v -> String.valueOf(spuStock.getItemId()).equals(goodsInfo.getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(goodsInfo.getThirdPlatformSkuId())).findFirst();
                    if (stock.isPresent()) {
                        Long quantity = stock.get().getInventory().getQuantity();
                        goodsInfo.setStock(quantity);
                        if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                            goodsInfo.setGoodsStatus(quantity > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                        }
                    }
                }
            }
        }
        updateGoodsInfoSupplyPriceAndStock(goodsInfoPage.getContent());
        MicroServicePage<GoodsInfo> microPage = KsBeanUtil.convertPage(goodsInfoPage, GoodsInfo.class);
        if (Objects.isNull(microPage) || microPage.getTotalElements() < 1 || CollectionUtils.isEmpty(microPage.getContent())) {
            return DistributionGoodsQueryResponse.builder().goodsInfoPage(microPage).build();
        }

        //拿到商家相关消息
        List<Long> companyInfoIds =
                goodsInfoPage.getContent().stream().map(GoodsInfo::getCompanyInfoId).distinct().collect(Collectors.toList());
        CompanyInfoQueryByIdsRequest companyInfoQueryByIdsRequest = new CompanyInfoQueryByIdsRequest();
        companyInfoQueryByIdsRequest.setCompanyInfoIds(companyInfoIds);
        companyInfoQueryByIdsRequest.setDeleteFlag(DeleteFlag.NO);
        BaseResponse<CompanyInfoQueryByIdsResponse> companyInfoQueryByIdsResponseBaseResponse =
                companyInfoQueryProvider.queryByCompanyInfoIds(companyInfoQueryByIdsRequest);

        //查询所有SKU规格值关联
        List<String> goodsInfoIds =
                goodsInfoPage.getContent().stream().map(GoodsInfo::getGoodsInfoId).collect(Collectors.toList());
        goodsInfoSpecDetails.addAll(goodsInfoSpecDetailRelRepository.findByGoodsInfoIds(goodsInfoIds));

        GoodsQueryRequest goodsQueryRequest = new GoodsQueryRequest();
        List<String> goodsIds =
                goodsInfoPage.getContent().stream().map(GoodsInfo::getGoodsId).collect(Collectors.toList());
        goodsQueryRequest.setGoodsIds(goodsIds);
        List<Goods> goodsList = goodsRepository.findAll(goodsQueryRequest.getWhereCriteria());

        // 填充店铺分类
        List<StoreCateGoodsRela> storeCateGoodsRelas = storeCateService.getStoreCateByGoods(goodsIds);
        if (CollectionUtils.isNotEmpty(storeCateGoodsRelas)) {
            Map<String, List<StoreCateGoodsRela>> storeCateMap = storeCateGoodsRelas.stream().collect(Collectors.groupingBy(StoreCateGoodsRela::getGoodsId));
            //为每个spu填充店铺分类编号
            if (MapUtils.isNotEmpty(storeCateMap)) {
                microPage.getContent().stream()
                        .filter(goods -> storeCateMap.containsKey(goods.getGoodsId()))
                        .forEach(goods -> {
                            goods.setStoreCateIds(storeCateMap.get(goods.getGoodsId()).stream().map(StoreCateGoodsRela::getStoreCateId).filter(id -> id != null).collect(Collectors.toList()));
                        });
            }
        }

        //填充每个SKU
        microPage.getContent().forEach(goodsInfo -> {
            //sku商品图片为空，则以spu商品主图
            if (StringUtils.isBlank(goodsInfo.getGoodsInfoImg())) {
                goodsInfo.setGoodsInfoImg(goodsList.stream().filter(goods -> goods.getGoodsId().equals(goodsInfo.getGoodsId())).findFirst().orElse(new Goods()).getGoodsImg());
            }
            goodsInfo.setSpecText(goodsInfoSpecDetails.stream().filter(specDetailRel -> specDetailRel.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).map(GoodsInfoSpecDetailRel::getDetailName).collect(Collectors.joining(" ")));
            //填充每个SKU的规格关系
            goodsInfo.setSpecDetailRelIds(goodsInfoSpecDetails.stream().filter(specDetailRel -> specDetailRel.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).map(GoodsInfoSpecDetailRel::getSpecDetailRelId).collect(Collectors.toList()));
        });

        //获取所有品牌
        GoodsBrandQueryRequest brandRequest = new GoodsBrandQueryRequest();
        brandRequest.setDelFlag(DeleteFlag.NO.toValue());
        brandRequest.setBrandIds(microPage.getContent().stream().filter(goodsInfo -> Objects.nonNull(goodsInfo.getBrandId())).map(GoodsInfo::getBrandId).distinct().collect(Collectors.toList()));
        goodsBrandList.addAll(goodsBrandRepository.findAll(brandRequest.getWhereCriteria()));

        //获取所有分类
        GoodsCateQueryRequest cateRequest = new GoodsCateQueryRequest();
        cateRequest.setCateIds(microPage.getContent().stream().filter(goodsInfo -> Objects.nonNull(goodsInfo.getCateId())).map(GoodsInfo::getCateId).distinct().collect(Collectors.toList()));
        goodsCateList.addAll(goodsCateRepository.findAll(cateRequest.getWhereCriteria()));

        response.setGoodsInfoPage(microPage);
        response.setGoodsInfoSpecDetails(goodsInfoSpecDetails);
        response.setGoodsBrandList(goodsBrandList);
        response.setGoodsCateList(goodsCateList);
        response.setCompanyInfoList(companyInfoQueryByIdsResponseBaseResponse.getContext().getCompanyInfoList());
        return response;
    }

    /**
     * 分销商品审核通过(单个)
     *
     * @param request
     */
    @Transactional
    public void checkDistributionGoods(DistributionGoodsCheckRequest request) {
        int checkResult = goodsInfoRepository.checkDistributionGoods(request.getGoodsInfoId());
        if (0 >= checkResult) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /**
     * 批量审核分销商品
     *
     * @param request
     */
    @Transactional
    public void batchCheckDistributionGoods(DistributionGoodsBatchCheckRequest request) {
        int checkResult = goodsInfoRepository.batchCheckDistributionGoods(request.getGoodsInfoIds());
        if (0 >= checkResult) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /**
     * 驳回或禁止分销商品
     *
     * @param goodsInfoId
     * @param distributionGoodsAudit
     * @param distributionGoodsAuditReason
     */
    @Transactional
    public void refuseCheckDistributionGoods(String goodsInfoId, DistributionGoodsAudit distributionGoodsAudit,
                                             String distributionGoodsAuditReason) {
        int checkResult = goodsInfoRepository.refuseCheckDistributionGoods(goodsInfoId, distributionGoodsAudit, distributionGoodsAuditReason);
        if (0 >= checkResult) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
        if (distributionGoodsAudit.equals(DistributionGoodsAudit.FORBID)) {
            // 同步删除分销员与商品关联表
            distributorGoodsInfoService.deleteByGoodsInfoId(goodsInfoId);
        }
    }

    /**
     * 编辑分销商品，修改佣金和状态
     *
     * @param goodsInfoId
     * @param distributionGoodsAudit
     * @param distributionCommission
     */
    @Transactional
    public void modifyDistributionGoods(String goodsInfoId, BigDecimal distributionCommission, DistributionGoodsAudit distributionGoodsAudit) {
        int checkResult = goodsInfoRepository.modifyDistributionGoods(goodsInfoId, distributionCommission, distributionGoodsAudit);
        if (0 >= checkResult) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /**
     * 编辑分销商品，修改佣金比例和状态
     *
     * @param goodsInfoId
     * @param
     */
    @Transactional
    public void modifyCommissionDistributionGoods(String goodsInfoId, BigDecimal commissionRate,
                                                  BigDecimal distributionCommission,
                                                  DistributionGoodsAudit distributionGoodsAudit) {
        int checkResult = goodsInfoRepository.modifyCommissionDistributionGoods(goodsInfoId, commissionRate,
                distributionCommission, distributionGoodsAudit);
        if (0 >= checkResult) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /**
     * 删除分销商品
     *
     * @param request
     */
    @Transactional
    public void delDistributionGoods(DistributionGoodsDeleteRequest request) {
        int checkResult = goodsInfoRepository.delDistributionGoods(request.getGoodsInfoId());
        if (0 >= checkResult) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /*
     * @Description: 商品ID<spu> 修改商品审核状态
     * @Param:  goodsId 商品ID
     * @Param:  审核状态
     * @Author: Bob
     * @Date: 2019-03-11 16:33
     */
    @Transactional
    public void modifyDistributeState(String goodsId, DistributionGoodsAudit state) {
        int i = goodsInfoRepository.modifyDistributeState(goodsId, state);
        if (0 >= i) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /*
     * @Description:  商品ID<spu> 查询sku信息
     * @Param:  goodsId 商品ID
     * @Author: Bob
     * @Date: 2019-03-11 17:14
     */
    public List<GoodsInfo> queryBygoodsId(String goodsId) {
        List<String> goodsIds = new ArrayList<>();
        goodsIds.add(goodsId);
        return goodsInfoRepository.findByGoodsIds(goodsIds);
    }

    /**
     * 添加分销商品前，验证所添加的sku是否符合条件
     * 条件：商品是否有效状态（商品已审核通过且未删除和上架状态）以及是否是零售商品
     *
     * @param goodsInfoIds
     * @return
     */
    public List<String> getInvalidGoodsInfoByGoodsInfoIds(List<String> goodsInfoIds) {
        List<Object> goodsInfoIdObj = goodsInfoRepository.getInvalidGoodsInfoByGoodsInfoIds(goodsInfoIds);
        if (CollectionUtils.isNotEmpty(goodsInfoIdObj)) {
            return goodsInfoIdObj.stream().map(obj -> (String) obj).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * 添加企业购商品前，验证所添加的sku是否符合条件
     * 条件：商品是否有效状态（商品已审核通过且未删除和上架状态）以及是否是零售商品
     *
     * @param goodsInfoIds
     * @return
     */
    public List<String> getInvalidEnterpriseByGoodsInfoIds(List<String> goodsInfoIds) {
        List<Object> goodsInfoIdObj = goodsInfoRepository.getInvalidEnterpriseByGoodsInfoIds(goodsInfoIds);
        if (CollectionUtils.isNotEmpty(goodsInfoIdObj)) {
            return goodsInfoIdObj.stream().map(obj -> (String) obj).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }


    /**
     * 根据单品ids，查询商品名称、市场价、规格值
     *
     * @param goodsInfoIds 单品ids
     * @return
     */
    public List<GoodsInfoParams> findGoodsInfoParamsByIds(List<String> goodsInfoIds) {
        // 1.查询商品名称、市场价
        List<GoodsInfoParams> infos = goodsInfoRepository.findGoodsInfoParamsByIds(goodsInfoIds);

        // 2.查询规格值
        Map<String, List<GoodsInfoSpecDetailRel>> specDetailMap = goodsInfoSpecDetailRelRepository.findByGoodsInfoIds(goodsInfoIds).stream().collect(Collectors.groupingBy(GoodsInfoSpecDetailRel::getGoodsInfoId));
        infos.forEach(goodsInfo -> {
            if (MapUtils.isNotEmpty(specDetailMap)) {
                goodsInfo.setSpecText(StringUtils.join(specDetailMap.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream()
                        .map(GoodsInfoSpecDetailRel::getDetailName)
                        .collect(Collectors.toList()), " "));
            }
        });
        return infos;
    }


    /**
     * 批量修改企业价格接口
     *
     * @param batchEnterPrisePriceDTOS
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateEnterPrisePrice(List<BatchEnterPrisePriceDTO> batchEnterPrisePriceDTOS, DefaultFlag defaultFlag) {
        //商品审核开关已打开--更新状态为待审核
        if (DefaultFlag.NO.equals(defaultFlag)) {
            batchEnterPrisePriceDTOS.forEach(entity -> {
                if (entity.getEnterprisePriceType() == 2) {
                    GoodsIntervalPrice price = new GoodsIntervalPrice();
                    price.setPrice(entity.getEnterPrisePrice());
                    price.setDiscount(100);
                    price.setCount(1L);
                    price.setGoodsId(entity.getGoodsId());
                    price.setType(PriceType.ENTERPRISE_SKU);
                    price.setGoodsInfoId(entity.getGoodsInfoId());
                    goodsIntervalPriceRepository.save(price);
                }
                goodsInfoRepository.addGoodsInfoEnterPrisePrice(entity.getGoodsInfoId(), entity.getEnterPrisePrice(), EnterpriseAuditState.CHECKED, entity.getEnterprisePriceType());
            });
        } else {
            //商品审核开关关闭直接更新为已审核
            batchEnterPrisePriceDTOS.forEach(entity -> {
                if (entity.getEnterprisePriceType() == 2) {
                    GoodsIntervalPrice price = new GoodsIntervalPrice();
                    price.setPrice(entity.getEnterPrisePrice());
                    price.setDiscount(100);
                    price.setCount(1L);
                    price.setGoodsId(entity.getGoodsId());
                    price.setType(PriceType.ENTERPRISE_SKU);
                    price.setGoodsInfoId(entity.getGoodsInfoId());
                    goodsIntervalPriceRepository.save(price);
                }
                goodsInfoRepository.addGoodsInfoEnterPrisePrice(entity.getGoodsInfoId(), entity.getEnterPrisePrice(), EnterpriseAuditState.WAIT_CHECK, entity.getEnterprisePriceType());
            });
        }

    }

    /**
     * 修改企业价格接口
     *
     * @param request
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateEnterPrisePrice(EnterprisePriceUpdateRequest request) {
        GoodsInfo goodsInfo = goodsInfoRepository.findByGoodsInfoIdAndDelFlag(request.getGoodsInfoId(), DeleteFlag.NO);
        if (goodsInfo.getEnterpriseDiscountFlag()) {
            if (goodsInfo.getEnterprisePriceType() == 1) {
                List<GoodsLevelPrice> list = goodsLevelPriceRepository.findByGoodsInfoIdAndType(request.getGoodsInfoId(), PriceType.ENTERPRISE_SKU);
                if (list != null && !list.isEmpty()) {
                    list.forEach(e -> e.setPrice(request.getEnterPrisePrice().multiply(BigDecimal.valueOf(e.getDiscount() * 0.01)).setScale(2, RoundingMode.HALF_UP)));
                    goodsLevelPriceRepository.saveAll(list);
                }
                if (goodsInfo.getEnterpriseCustomerFlag()) {
                    List<GoodsCustomerPrice> list1 = goodsCustomerPriceRepository.findByGoodsInfoIdAndType(request.getGoodsInfoId(), PriceType.ENTERPRISE_SKU);
                    list1.forEach(e -> e.setPrice(request.getEnterPrisePrice().multiply(BigDecimal.valueOf(e.getDiscount() * 0.01)).setScale(2, RoundingMode.HALF_UP)));
                    goodsCustomerPriceRepository.saveAll(list1);
                }
            } else if (goodsInfo.getEnterprisePriceType() == 2) {
                List<GoodsIntervalPrice> list = goodsIntervalPriceRepository.findByGoodsInfoIdAndType(request.getGoodsInfoId(), PriceType.ENTERPRISE_SKU);
                list.forEach(e -> e.setPrice(request.getEnterPrisePrice().multiply(BigDecimal.valueOf(e.getDiscount() * 0.01)).setScale(2, RoundingMode.HALF_UP)));
                goodsIntervalPriceRepository.saveAll(list);
            }
        }
        //商品已审核--更新状态为待审核
        if (DefaultFlag.YES.equals(request.getEnterpriseGoodsAuditFlag())) {
            goodsInfoRepository.updateGoodsInfoEnterPrisePrice(request.getGoodsInfoId(), request.getEnterPrisePrice(), EnterpriseAuditState.CHECKED);
        } else {
            goodsInfoRepository.updateGoodsInfoEnterPrisePrice(request.getGoodsInfoId(), request.getEnterPrisePrice(), EnterpriseAuditState.WAIT_CHECK);
        }
    }

    /**
     * 删除企业购商品
     *
     * @param goodsInfoId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteEnterpriseGoods(String goodsInfoId) {
        Optional<GoodsInfo> goodsInfo = goodsInfoRepository.findById(goodsInfoId);
        if (goodsInfo.isPresent()) {
            GoodsInfo sku = goodsInfo.get();
            sku.setEnterPriseAuditState(EnterpriseAuditState.INIT);
            sku.setEnterPrisePrice(BigDecimal.ZERO);
            sku.setEnterpriseDiscountFlag(false);
            sku.setEnterpriseCustomerFlag(false);
            goodsInfoRepository.saveAndFlush(sku);
            goodsIntervalPriceRepository.deleteByGoodsInfoIdsAndType(Collections.singletonList(sku.getGoodsInfoId()), PriceType.ENTERPRISE_SKU);
            goodsLevelPriceRepository.deleteByGoodsInfoIdsAndType(Collections.singletonList(sku.getGoodsInfoId()), PriceType.ENTERPRISE_SKU);
            goodsCustomerPriceRepository.deleteByGoodsInfoIdsAndType(Collections.singletonList(sku.getGoodsInfoId()), PriceType.ENTERPRISE_SKU);
        }
    }

    /**
     * 删除企业购商品
     *
     * @param goodsId
     */
    @Transactional(rollbackFor = Exception.class)
    public List<String> batchDeleteEnterpriseGoods(String goodsId) {
        List<GoodsInfo> goodsInfos = goodsInfoRepository.findByGoodsIds(Arrays.asList(goodsId));
        List<String> goodsInfoIds = new ArrayList<>();
        goodsInfos.forEach(goodsInfo -> {
            goodsInfo.setEnterPriseAuditState(EnterpriseAuditState.INIT);
            goodsInfo.setEnterPrisePrice(BigDecimal.ZERO);
            goodsInfo.setEnterpriseDiscountFlag(false);
            goodsInfo.setEnterpriseCustomerFlag(false);
            goodsInfoIds.add(goodsInfo.getGoodsInfoId());
        });
        goodsIntervalPriceRepository.deleteByGoodsInfoIdsAndType(goodsInfoIds, PriceType.ENTERPRISE_SKU);
        goodsLevelPriceRepository.deleteByGoodsInfoIdsAndType(goodsInfoIds, PriceType.ENTERPRISE_SKU);
        goodsCustomerPriceRepository.deleteByGoodsInfoIdsAndType(goodsInfoIds, PriceType.ENTERPRISE_SKU);
        goodsInfoRepository.saveAll(goodsInfos);
        return goodsInfos.stream().map(GoodsInfo::getGoodsInfoId).collect(Collectors.toList());
    }

    /**
     * 审核企业购商品
     *
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public String auditEnterpriseGoodsInfo(EnterpriseAuditCheckRequest request) {
        Optional<GoodsInfo> goodsInfoOptional = goodsInfoRepository.findById(request.getGoodsInfoId());
        if (goodsInfoOptional.isPresent()) {
            if (EnterpriseAuditState.WAIT_CHECK.equals(goodsInfoOptional.get().getEnterPriseAuditState())) {
                goodsInfoOptional.get().setEnterPriseAuditState(request.getEnterpriseAuditState());
                if (EnterpriseAuditState.NOT_PASS.equals(request.getEnterpriseAuditState())) {
                    goodsInfoOptional.get().setEnterPriseGoodsAuditReason(request.getEnterPriseGoodsAuditReason());
                }
                goodsInfoRepository.saveAndFlush(goodsInfoOptional.get());
            } else {
                return CommonErrorCode.STATUS_HAS_BEEN_CHANGED_ERROR;
            }
        }
        return CommonErrorCode.SUCCESSFUL;
    }

    /**
     * 批量审核企业购商品的价格
     *
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchAuditEnterpriseGoods(EnterpriseAuditStatusBatchRequest request) {
        //审核通过
        if (EnterpriseAuditState.CHECKED.equals(request.getEnterpriseGoodsAuditFlag())) {
            goodsInfoRepository.batchAuditEnterprise(request.getGoodsInfoIds(), request.getEnterpriseGoodsAuditFlag());
        }
        //审核被驳回
        if (EnterpriseAuditState.NOT_PASS.equals(request.getEnterpriseGoodsAuditFlag())) {
            goodsInfoRepository.batchRejectAuditEnterprise(request.getGoodsInfoIds(),
                    request.getEnterpriseGoodsAuditFlag(),
                    request.getEnterPriseGoodsAuditReason());
        }
    }

    /**
     * 分页查询企业购商品
     *
     * @param request 参数
     * @return EnterPriseGoodsQueryResponse
     */
    public EnterPriseGoodsQueryResponse enterpriseGoodsPage(EnterpriseGoodsQueryRequest request) {
        EnterPriseGoodsQueryResponse response = new EnterPriseGoodsQueryResponse();
        List<GoodsCate> goodsCateList = new ArrayList<>();
        List<GoodsBrand> goodsBrandList = new ArrayList<>();
        List<GoodsInfoSpecDetailRel> goodsInfoSpecDetails = new ArrayList<>();

        //获取该分类的所有子分类
        if (Objects.nonNull(request.getCateId()) && request.getCateId() > 0) {
            request.setCateIds(goodsCateService.getChlidCateId(request.getCateId()));
            if (CollectionUtils.isNotEmpty(request.getCateIds())) {
                request.setCateId(null);
                request.getCateIds().add(request.getCateId());
            }
        }

        //获取该店铺分类下的所有spuIds
        if (Objects.nonNull(request.getStoreCateId()) && request.getStoreCateId() > 0) {
            List<StoreCateGoodsRela> relas = storeCateService.findAllChildRela(request.getStoreCateId(), true);
            if (CollectionUtils.isNotEmpty(relas)) {
                List<String> goodsIdList = relas.stream().map(StoreCateGoodsRela::getGoodsId).collect(Collectors.toList());
                request.setGoodsIds(goodsIdList);
            } else {
                return EnterPriseGoodsQueryResponse.builder().goodsInfoPage(new MicroServicePage<>(Collections.emptyList(), request.getPageRequest(), 0)).build();
            }
        }

        //标签搜索
        if (request.getLabelId() != null && request.getLabelId() > 0) {
            GoodsQueryRequest goodsQueryRequest = GoodsQueryRequest.builder().labelId(request.getLabelId()).build();
            List<Goods> goods = goodsRepository.findAll(goodsQueryRequest.getWhereCriteria());
            if (CollectionUtils.isEmpty(goods)) {
                return EnterPriseGoodsQueryResponse.builder().goodsInfoPage(new MicroServicePage<>(Collections.emptyList(), request.getPageRequest(), 0)).build();
            }
            List<String> spuId = goods.stream().map(Goods::getGoodsId).collect(Collectors.toList());
            //如果前面已经有条件了，取交集中的spuId  大条件上是and关系
            if (CollectionUtils.isNotEmpty(request.getGoodsIds())) {
                spuId = spuId.stream().filter(request.getGoodsIds()::contains).collect(Collectors.toList());
            }
            request.setGoodsIds(spuId);
        }


        //分页查询企业购商品sku
        request.setDelFlag(DeleteFlag.NO.toValue());
        request.setEnterpriseFlag(true);
        Page<GoodsInfo> goodsInfoPage = goodsInfoRepository.findAll(KsBeanUtil.copyPropertiesThird(request, CommonGoodsInfoQueryRequest.class).getWhereCriteria(), request.getPageRequest());
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = goodsInfoPage.getContent().stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .distinct()
                .collect(Collectors.toList());
        if (itemIds.size() > 0) {
            List<QueryItemInventoryResponse.Item> stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
            if (stocks != null) {
                for (GoodsInfo goodsInfo : goodsInfoPage.getContent()) {
                    for (QueryItemInventoryResponse.Item spuStock : stocks) {
                        if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {
                            Optional<QueryItemInventoryResponse.Item.Sku> stock = spuStock.getSkuList().stream()
                                    .filter(v -> String.valueOf(spuStock.getItemId()).equals(goodsInfo.getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(goodsInfo.getThirdPlatformSkuId()))
                                    .findFirst();
                            if (stock.isPresent()) {
                                Long quantity = stock.get().getInventory().getQuantity();
                                goodsInfo.setStock(quantity);
                                if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                                    goodsInfo.setGoodsStatus(quantity > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                                }
                            }
                        }
                    }
                }
            }
        }
        MicroServicePage<GoodsInfo> microPage = KsBeanUtil.convertPage(goodsInfoPage, GoodsInfo.class);
        if (Objects.isNull(microPage) || microPage.getTotalElements() < 1 || CollectionUtils.isEmpty(microPage.getContent())) {
            return EnterPriseGoodsQueryResponse.builder().goodsInfoPage(microPage).build();
        }
        //拿到商家相关消息
        List<Long> companyInfoIds =
                goodsInfoPage.getContent().stream().map(GoodsInfo::getCompanyInfoId).distinct().collect(Collectors.toList());
        CompanyInfoQueryByIdsRequest companyInfoQueryByIdsRequest = new CompanyInfoQueryByIdsRequest();
        companyInfoQueryByIdsRequest.setDeleteFlag(DeleteFlag.NO);
        companyInfoQueryByIdsRequest.setCompanyInfoIds(companyInfoIds);
        BaseResponse<CompanyInfoQueryByIdsResponse> companyInfoQueryByIdsResponseBaseResponse =
                companyInfoQueryProvider.queryByCompanyInfoIds(companyInfoQueryByIdsRequest);
        GoodsQueryRequest goodsQueryRequest = new GoodsQueryRequest();
        //查询所有SKU规格值关联
        List<String> goodsInfoIds =
                goodsInfoPage.getContent().stream().map(GoodsInfo::getGoodsInfoId).collect(Collectors.toList());
        goodsInfoSpecDetails.addAll(goodsInfoSpecDetailRelRepository.findByGoodsInfoIds(goodsInfoIds));

        List<String> goodsIds =
                goodsInfoPage.getContent().stream().map(GoodsInfo::getGoodsId).collect(Collectors.toList());
        goodsQueryRequest.setGoodsIds(goodsIds);
        List<Goods> goodsList = goodsRepository.findAll(goodsQueryRequest.getWhereCriteria());
        // 填充店铺分类
        List<StoreCateGoodsRela> storeCateGoodsRelas = storeCateService.getStoreCateByGoods(goodsIds);
        if (CollectionUtils.isNotEmpty(storeCateGoodsRelas)) {
            Map<String, List<StoreCateGoodsRela>> storeCateMap = storeCateGoodsRelas.stream().collect(Collectors.groupingBy(StoreCateGoodsRela::getGoodsId));
            //为每个spu填充店铺分类编号
            if (MapUtils.isNotEmpty(storeCateMap)) {
                microPage.getContent().stream()
                        .filter(goods -> storeCateMap.containsKey(goods.getGoodsId()))
                        .forEach(goods -> {
                            goods.setStoreCateIds(storeCateMap.get(goods.getGoodsId()).stream().map(StoreCateGoodsRela::getStoreCateId).filter(id -> id != null).collect(Collectors.toList()));
                        });
            }
        }
        Map<String, Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getGoodsId, v1 -> v1));
        //填充每个SKU
        microPage.getContent().forEach(goodsInfo -> {
            //sku商品图片为空，则以spu商品主图
            if (StringUtils.isBlank(goodsInfo.getGoodsInfoImg())) {
                goodsInfo.setGoodsInfoImg(goodsList.stream().filter(goods -> goods.getGoodsId().equals(goodsInfo.getGoodsId())).findFirst().orElse(new Goods()).getGoodsImg());
            }
            //填充每个SKU的规格关系
            goodsInfo.setSpecText(goodsInfoSpecDetails.stream().filter(specDetailRel -> specDetailRel.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).map(GoodsInfoSpecDetailRel::getDetailName).collect(Collectors.joining(" ")));
            goodsInfo.setSpecDetailRelIds(goodsInfoSpecDetails.stream().filter(specDetailRel -> specDetailRel.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).map(GoodsInfoSpecDetailRel::getSpecDetailRelId).collect(Collectors.toList()));

            Goods goods = goodsMap.get(goodsInfo.getGoodsId());
            //如果商品按订货量设价+不允许独立设价  不允许选择企业商品
            goodsInfo.setAllowPriceSet(goods.getAllowPriceSet());
            goodsInfo.setPriceType(goods.getPriceType());
        });

        //获取所有品牌
        GoodsBrandQueryRequest brandRequest = new GoodsBrandQueryRequest();
        brandRequest.setDelFlag(DeleteFlag.NO.toValue());
        brandRequest.setBrandIds(microPage.getContent().stream().filter(goodsInfo -> Objects.nonNull(goodsInfo.getBrandId())).map(GoodsInfo::getBrandId).distinct().collect(Collectors.toList()));
        goodsBrandList.addAll(goodsBrandRepository.findAll(brandRequest.getWhereCriteria()));

        //获取所有分类
        GoodsCateQueryRequest cateRequest = new GoodsCateQueryRequest();
        cateRequest.setCateIds(microPage.getContent().stream().filter(goodsInfo -> Objects.nonNull(goodsInfo.getCateId())).map(GoodsInfo::getCateId).distinct().collect(Collectors.toList()));
        goodsCateList.addAll(goodsCateRepository.findAll(cateRequest.getWhereCriteria()));
        response.setGoodsInfoSpecDetails(goodsInfoSpecDetails);
        response.setGoodsInfoPage(microPage);
        response.setGoodsCateList(goodsCateList);
        response.setGoodsBrandList(goodsBrandList);
        response.setCompanyInfoList(companyInfoQueryByIdsResponseBaseResponse.getContext().getCompanyInfoList());
        return response;
    }


    /**
     * 商品如果来自供应商，将商户库存批量转供应商库存
     *
     * @param goodsInfoList
     * @return
     */
    public List<GoodsInfo> turnProviderStock(List<GoodsInfo> goodsInfoList) {
        if (CollectionUtils.isNotEmpty(goodsInfoList)) {
            // 过滤出所有来源于供应商的商品sku集合
            List<GoodsInfo> goodsInfos = goodsInfoList.stream().filter(
                    goodsInfo -> StringUtils.isNotEmpty(goodsInfo.getProviderGoodsInfoId())).collect(Collectors.toList());

            // 存在来自于供应商的商品信息，要取供应商商品的库存信息
            if (CollectionUtils.isNotEmpty(goodsInfos)) {
                // sku商品所属供应商sku商品id集合
                List<String> providerGoodsInfoIds = goodsInfos.stream().filter(goodsInfoVO ->
                        StringUtils.isNotEmpty(goodsInfoVO.getProviderGoodsInfoId())).map(GoodsInfo::getProviderGoodsInfoId).collect(Collectors.toList());
                List<GoodsInfo> providerGoodsInfos = goodsInfoRepository.findAllById(providerGoodsInfoIds);

                goodsInfoList.forEach(vo -> providerGoodsInfos.forEach(goodsInfo -> {
                    // 用供应商库存替换经营商户的库存
                    if (goodsInfo.getGoodsInfoId().equals(vo.getProviderGoodsInfoId())) {
                        vo.setStock(goodsInfo.getStock());
                    }
                }));
            }
        }

        return goodsInfoList;
    }

    /**
     * 商品如果来自供应商，将商户库存转供应商库存
     *
     * @param goodsInfo
     * @return
     */
    public GoodsInfo turnSingleProviderStock(GoodsInfo goodsInfo) {
        if (Objects.nonNull(goodsInfo)) {
            // 商户商品skuid对应的供应商商品skuid
            String providerGoodsInfoId = goodsInfo.getProviderGoodsInfoId();
            if (StringUtils.isNotEmpty(providerGoodsInfoId)) {
                GoodsInfo providerGoodsInfo = goodsInfoRepository.findById(providerGoodsInfoId).orElse(new GoodsInfo());
                goodsInfo.setStock(providerGoodsInfo.getStock());
            }
        }

        return goodsInfo;
    }

    /**
     * 商品如果来自供应商，将商户skuid转供应商skuid
     *
     * @param goodsInfoId
     */
    public String turnProviderGoodsInfoId(String goodsInfoId) {
        GoodsInfo goodsInfo = goodsInfoRepository.findById(goodsInfoId).orElse(new GoodsInfo());
        if (StringUtils.isNotEmpty(goodsInfo.getProviderGoodsInfoId())) {
            goodsInfoId = goodsInfo.getProviderGoodsInfoId();
        }

        return goodsInfoId;
    }


    /**
     * @param
     * @return
     * @discription 获取storeid
     * @author yangzhen
     * @date 2020/9/2 20:37
     */
    public Long queryStoreId(String SkuId) {
        Long storeId = goodsInfoRepository.queryStoreId(SkuId);
        return storeId;
    }

    /**
     * 根据skuId查询需要实时字段
     */
    public List<GoodsInfoVO> findGoodsInfoPartColsByIds(List<String> ids) {
        List<Object> resultList = goodsInfoRepository.findGoodsInfoPartColsByIds(ids);
        return convertFromNativeSQLResult(resultList);
    }

    /**
     * 根据单品ids，SPU、库存、规格值
     *
     * @param goodsInfoIds 单品ids
     * @return
     */
    public List<GoodsInfoLiveGoods> findGoodsInfoLiveGoodsByIds(List<String> goodsInfoIds) {
        Map<String, List<GoodsInfoSpecDetailRel>> specDetailMap = goodsInfoSpecDetailRelRepository.findByAllGoodsInfoIds(goodsInfoIds).stream().collect(Collectors.groupingBy(GoodsInfoSpecDetailRel::getGoodsInfoId));
        List<GoodsInfoLiveGoods> liveGoodsList = goodsInfoRepository.findGoodsInfoLiveGoodsByIds(goodsInfoIds);
        liveGoodsList.stream().forEach(goodsInfoLiveGoods -> {
            if (MapUtils.isNotEmpty(specDetailMap)) {
                goodsInfoLiveGoods.setSpecText(StringUtils.join(specDetailMap.getOrDefault(goodsInfoLiveGoods.getGoodsInfoId(), new ArrayList<>()).stream()
                        .map(GoodsInfoSpecDetailRel::getDetailName)
                        .collect(Collectors.toList()), " "));
            }
        });
        return liveGoodsList;
    }

    private List<GoodsInfoVO> convertFromNativeSQLResult(List<Object> resultList) {
        List<GoodsInfoVO> voList = new ArrayList<>();
        if (CollectionUtils.isEmpty(resultList)) {
            return voList;
        }
        for (Object obj : resultList) {
            Object[] result = (Object[]) obj;
            GoodsInfoVO vo = new GoodsInfoVO();
            vo.setGoodsInfoId(StringUtil.cast(result, 0, String.class));
            Byte delFlagByte = toByte(result, 1);
            vo.setDelFlag(delFlagByte != null ? DeleteFlag.fromValue(delFlagByte.intValue()) : null);
            Byte addedFlagByte = toByte(result, 2);
            vo.setAddedFlag(addedFlagByte != null ? addedFlagByte.intValue() : AddedFlag.NO.toValue());
            Byte vendibilityByte = toByte(result, 3);
            vo.setVendibility(vendibilityByte != null ? vendibilityByte.intValue() : null);
            Byte auditStatusByte = toByte(result, 4);
            vo.setAuditStatus(auditStatusByte != null ? CheckStatus.values()[auditStatusByte.intValue()] : null);
            vo.setMarketPrice(StringUtil.cast(result, 5, BigDecimal.class));
            vo.setSupplyPrice(StringUtil.cast(result, 6, BigDecimal.class));
            BigInteger buyPoint = StringUtil.cast(result, 7, BigInteger.class);
            vo.setBuyPoint(buyPoint != null ? buyPoint.longValue() : null);
            String channelTypeIdStr = StringUtil.cast(result, 8, String.class);
            if (StringUtils.isNotBlank(channelTypeIdStr)) {
                vo.setGoodsChannelTypeSet(Arrays.asList(channelTypeIdStr.split(",")));
            }
            voList.add(vo);
        }
        return voList;
    }

    private Byte toByte(Object[] result, int index) {
        return StringUtil.cast(result, index, Byte.class);
    }

    /***
     * 根据ID查询企业购商品信息
     * @param goodsInfoId
     * @return
     */
    public EnterpriseByIdResponse findEnterpriseGoodsById(String goodsInfoId, Long storeId) {
        EnterpriseByIdResponse response = new EnterpriseByIdResponse();
        GoodsInfo goodsInfo = goodsInfoRepository.findByGoodsInfoIdAndDelFlag(goodsInfoId, DeleteFlag.NO);
        if (goodsInfo == null || !storeId.equals(goodsInfo.getStoreId())) {
            throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
        }
        if (goodsInfo.getEnterPriseAuditState() != EnterpriseAuditState.CHECKED) {
            throw new SbcRuntimeException(CommonErrorCode.STATUS_HAS_BEEN_CHANGED_ERROR);
        }
        Goods goods = goodsService.getGoodsById(goodsInfo.getGoodsId());
        //商品设价类型和企业设价类型不一致
        Integer priceType = goods.getPriceType();
        if (priceType == null || priceType == 2) {
            //市场价
            goodsInfo.setEnterprisePriceType(0);
        } else if (priceType == 1) {
            //订货量设价
            goodsInfo.setEnterprisePriceType(2);
        } else {
            //客户等级设价
            goodsInfo.setEnterprisePriceType(1);
        }
        if (goodsInfo.getEnterprisePriceType() != null && goodsInfo.getEnterprisePriceType() == 1) {
            //会员等级设价
            List<GoodsLevelPrice> list = goodsLevelPriceRepository.findByGoodsInfoIdAndType(goodsInfoId, PriceType.ENTERPRISE_SKU);
            response.setGoodsLevelPrices(KsBeanUtil.convertList(list, GoodsLevelPriceVO.class));
        } else if (goodsInfo.getEnterprisePriceType() != null && goodsInfo.getEnterprisePriceType() == 2) {
            //购买数量设价
            List<GoodsIntervalPrice> list = goodsIntervalPriceRepository.findByGoodsInfoIdAndType(goodsInfoId, PriceType.ENTERPRISE_SKU);
            if (list == null || list.isEmpty()) {
                list = new ArrayList<>();
                GoodsIntervalPrice intervalPrice = new GoodsIntervalPrice();
                intervalPrice.setPrice(goodsInfo.getEnterPrisePrice());
                intervalPrice.setGoodsId(goods.getGoodsId());
                intervalPrice.setGoodsInfoId(goodsInfoId);
                intervalPrice.setType(PriceType.ENTERPRISE_SKU);
                intervalPrice.setCount(1L);
                intervalPrice.setDiscount(100);
                list.add(intervalPrice);
            }
            if (list.size() == 1) {
                list.get(0).setCanDel(false);
            }
            response.setGoodsIntervalPrices(KsBeanUtil.convert(list, GoodsIntervalPriceVO.class));
        }

        List<GoodsCustomerPrice> goodsCustomerPrices = goodsCustomerPriceRepository.findByGoodsInfoIdAndType(goodsInfoId, PriceType.ENTERPRISE_SKU);
        response.setGoodsCustomerPrices(KsBeanUtil.convertList(goodsCustomerPrices, GoodsCustomerPriceVO.class));

        GoodsInfoVO goodsInfoVO = KsBeanUtil.convert(goodsInfo, GoodsInfoVO.class);
        response.setGoodsInfo(goodsInfoVO);

        return response;
    }

    /**
     * 设价保存
     *
     * @param request
     * @return
     */
    @Transactional
    public void enterpriseSavePrice(EnterprisePriceSaveRequest request) {
        //校验商品
        GoodsInfo goodsInfo = goodsInfoRepository.getOne(request.getGoodsInfoId());
        if (!goodsInfo.getStoreId().equals(request.getStoreId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        List<GoodsLevelPrice> goodsLevelPrices = null;
        List<GoodsCustomerPrice> goodsCustomerPrices = null;
        List<GoodsIntervalPrice> goodsIntervalPrices = null;

        //判断类型
        if (request.getEnterprisePriceType().equals(0)) {
            goodsInfo.setEnterprisePriceType(0);
            goodsInfo.setEnterpriseCustomerFlag(false);
            goodsInfo.setEnterpriseDiscountFlag(false);
        } else if (request.getEnterprisePriceType().equals(1)) {
            //客户等级设价
            goodsInfo.setEnterprisePriceType(1);
            goodsInfo.setEnterpriseCustomerFlag(request.getEnterpriseCustomerFlag());
            goodsInfo.setEnterpriseDiscountFlag(request.getEnterpriseDiscountFlag());
            if (request.getStoreLevels() == null || request.getStoreLevels().isEmpty()) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            goodsLevelPrices = new ArrayList<>();
            List<GoodsLevelPrice> finalGoodsLevelPrices = goodsLevelPrices;

            request.getStoreLevels().forEach(e -> {
                GoodsLevelPrice price = new GoodsLevelPrice();
                price.setGoodsId(goodsInfo.getGoodsId());
                price.setGoodsInfoId(goodsInfo.getGoodsInfoId());
                price.setLevelId(e.getStoreLevelId());
                price.setDiscount(e.getDiscount());
                price.setPrice(e.getPrice());
                price.setType(PriceType.ENTERPRISE_SKU);
                finalGoodsLevelPrices.add(price);

            });

            if (request.getEnterpriseCustomerFlag()) {
                //特定客户设价
                if (CollectionUtils.isNotEmpty(request.getGoodsCustomerPrices())) {
                    goodsCustomerPrices = new ArrayList<>();
                    List<GoodsCustomerPrice> finalGoodsCustomerPrices = goodsCustomerPrices;
                    request.getGoodsCustomerPrices().forEach(e -> {
                        GoodsCustomerPrice price = new GoodsCustomerPrice();
                        price.setGoodsId(goodsInfo.getGoodsId());
                        price.setGoodsInfoId(goodsInfo.getGoodsInfoId());
                        price.setCustomerId(e.getCustomerId());
                        price.setDiscount(e.getDiscount());
                        price.setPrice(e.getPrice());
                        price.setType(PriceType.ENTERPRISE_SKU);
                        finalGoodsCustomerPrices.add(price);
                    });
                }
            }
        } else if (request.getEnterprisePriceType().equals(2)) {
            //区间设价
            goodsInfo.setEnterprisePriceType(2);
            goodsInfo.setEnterpriseCustomerFlag(false);
            goodsInfo.setEnterpriseDiscountFlag(request.getEnterpriseDiscountFlag());
            goodsIntervalPrices = new ArrayList<>();
            if (CollectionUtils.isEmpty(request.getGoodsIntervalPrices())) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            List<GoodsIntervalPrice> finalGoodsIntervalPrices = goodsIntervalPrices;
            request.getGoodsIntervalPrices().forEach(e -> {
                GoodsIntervalPrice price = new GoodsIntervalPrice();
                price.setGoodsId(goodsInfo.getGoodsId());
                price.setGoodsInfoId(goodsInfo.getGoodsInfoId());
                price.setCount(e.getCount());
                price.setDiscount(e.getDiscount());
                price.setPrice(e.getPrice());
                price.setType(PriceType.ENTERPRISE_SKU);
                finalGoodsIntervalPrices.add(price);
            });
        }

        //删除--等级设价
        goodsLevelPriceRepository.deleteByGoodsInfoIdAndType(request.getGoodsInfoId(), PriceType.ENTERPRISE_SKU);
        //保存--等级设价
        if (goodsLevelPrices != null && !goodsLevelPrices.isEmpty()) {
            goodsLevelPriceRepository.saveAll(goodsLevelPrices);
        }
        //删除--单独客户设价
        goodsCustomerPriceRepository.deleteByGoodsInfoIdAndType(request.getGoodsInfoId(), PriceType.ENTERPRISE_SKU);
        if (goodsCustomerPrices != null && !goodsCustomerPrices.isEmpty()) {
            goodsCustomerPriceRepository.saveAll(goodsCustomerPrices);
        }
        //删除--区间价设价
        goodsIntervalPriceRepository.deleteByGoodsInfoIdAndType(request.getGoodsInfoId(), PriceType.ENTERPRISE_SKU);
        if (goodsIntervalPrices != null && !goodsIntervalPrices.isEmpty()) {
            goodsIntervalPriceRepository.saveAll(goodsIntervalPrices);
        }
        goodsInfoRepository.save(goodsInfo);
    }

    /**
     * 处理企业商品的最低价和最高价
     */
    public List<GoodsInfoVO> fillEnterpriseMinMaxPrice(List<GoodsInfoVO> goodsInfos) {
        if (goodsInfos == null || goodsInfos.isEmpty()) {
            return new ArrayList<>();
        }
        goodsInfos.forEach(goodsInfoVO -> {
            AtomicReference<BigDecimal> min = new AtomicReference<>(goodsInfoVO.getEnterPrisePrice());
            AtomicReference<BigDecimal> max = new AtomicReference<>(goodsInfoVO.getEnterPrisePrice());
            if (goodsInfoVO.getEnterPriseAuditState() == EnterpriseAuditState.CHECKED && goodsInfoVO.getEnterprisePriceType() != 0) {
                if (goodsInfoVO.getEnterprisePriceType() == 1) {
                    //按客户设价
                    List<GoodsLevelPrice> list = goodsLevelPriceRepository.findByGoodsInfoIdAndType(goodsInfoVO.getGoodsInfoId(), PriceType.ENTERPRISE_SKU);
                    if (list != null && !list.isEmpty()) {
                        list.forEach(e -> {
                            BigDecimal price = e.getPrice();
                            if (goodsInfoVO.getEnterpriseDiscountFlag()) {
                                price = goodsInfoVO.getEnterPrisePrice().multiply(BigDecimal.valueOf(e.getDiscount())).multiply(BigDecimal.valueOf(0.01)).setScale(2, RoundingMode.HALF_UP);
                            }
                            if (price.compareTo(min.get()) < 0) {
                                min.set(price);
                            }
                            if (price.compareTo(max.get()) > 0) {
                                max.set(price);
                            }
                        });
                    }
                } else {
                    //按购买数量设价
                    List<GoodsIntervalPrice> list = goodsIntervalPriceRepository.findByGoodsInfoIdAndType(goodsInfoVO.getGoodsInfoId(), PriceType.ENTERPRISE_SKU);
                    if (list != null && !list.isEmpty()) {
                        list.forEach(e -> {
                            BigDecimal price = e.getPrice();
                            if (goodsInfoVO.getEnterpriseDiscountFlag()) {
                                price = goodsInfoVO.getEnterPrisePrice().multiply(BigDecimal.valueOf(e.getDiscount())).multiply(BigDecimal.valueOf(0.01)).setScale(2, RoundingMode.HALF_UP);
                            }
                            if (price.compareTo(min.get()) < 0) {
                                min.set(price);
                            }
                            if (price.compareTo(max.get()) > 0) {
                                max.set(price);
                            }
                        });
                    }
                }
            }
            goodsInfoVO.setEnterpriseMinPrice(min.get());
            goodsInfoVO.setEnterpriseMaxPrice(max.get());
        });
        return goodsInfos;
    }

    /**
     * 根据商品 用户 购买数量 获取商品价格
     *
     * @param request
     */
    public EnterprisePriceResponse getUserPrice(EnterprisePriceGetRequest request) {
        EnterprisePriceResponse response = new EnterprisePriceResponse();
        List<GoodsInfo> goodsInfos = goodsInfoRepository.findByGoodsInfoIds(request.getGoodsInfoIds());

        List<Long> storeIds = new ArrayList<>();
        List<String> goodsLevelIds = new ArrayList<>();
        List<String> goodsCustomerIds = new ArrayList<>();
        List<String> goodsCountIds = new ArrayList<>();

        goodsInfos.forEach(e -> {
            if (e.getEnterPriseAuditState() != EnterpriseAuditState.CHECKED) {
                return;
            }
            if (e.getEnterprisePriceType() == 1) {
                if (e.getEnterpriseCustomerFlag()) {
                    goodsCustomerIds.add(e.getGoodsInfoId());
                }
                goodsLevelIds.add(e.getGoodsInfoId());
            } else if (e.getEnterprisePriceType() == 2) {
                goodsCountIds.add(e.getGoodsInfoId());
            }
            storeIds.add(e.getStoreId());
        });


        //查询商品-等级设价
        Map<String, List<GoodsLevelPrice>> levelPriceMap = new HashMap<>();
        if (!goodsLevelIds.isEmpty()) {
            List<GoodsLevelPrice> goodsLevelPriceList = goodsLevelPriceRepository.findEnterpriseByGoodsInfoIds(goodsLevelIds);
            if (CollectionUtils.isEmpty(goodsLevelPriceList)) {
                goodsLevelPriceList.forEach(e -> {
                    List<GoodsLevelPrice> list = levelPriceMap.get(e.getGoodsInfoId());
                    if (list == null) {
                        list = new ArrayList<>();
                        list.add(e);
                        levelPriceMap.put(e.getGoodsInfoId(), list);
                    } else {
                        list.add(e);
                    }
                });
            }
        }

        //查询商品-客户设价
        Map<String, GoodsCustomerPrice> customerPriceMap = new HashMap<>();
        if (StringUtils.isNotBlank(request.getCustomerId()) && CollectionUtils.isNotEmpty(goodsCustomerIds)) {
            List<GoodsCustomerPrice> goodsCustomerPricesList = goodsCustomerPriceRepository.findEnterpriseByGoodsInfoIdAndCustomerId(goodsCustomerIds, request.getCustomerId());
            if (CollectionUtils.isNotEmpty(goodsCustomerPricesList)) {
                goodsCustomerPricesList.forEach(e -> customerPriceMap.put(e.getGoodsInfoId(), e));
            }
        }
        //按商品-购买数量设价
        Map<String, List<GoodsIntervalPriceVO>> intervalPriceMap = new HashMap<>();
        if (!goodsCountIds.isEmpty()) {
            List<GoodsIntervalPrice> skuByGoodsInfoIds = goodsIntervalPriceRepository.findEnterpriseByGoodsInfoIds(goodsCountIds);
            skuByGoodsInfoIds.forEach(e -> {
                List<GoodsIntervalPriceVO> goodsIntervalPrices = intervalPriceMap.get(e.getGoodsInfoId());
                if (goodsIntervalPrices == null) {
                    goodsIntervalPrices = new ArrayList<>();
                    goodsIntervalPrices.add(KsBeanUtil.convert(e, GoodsIntervalPriceVO.class));
                    intervalPriceMap.put(e.getGoodsInfoId(), goodsIntervalPrices);
                } else {
                    goodsIntervalPrices.add(KsBeanUtil.convert(e, GoodsIntervalPriceVO.class));
                }
            });
        }

        //获取店铺等级ID列表
        Map<Long, CommonLevelVO> levelMap = new HashMap<>();
        if (request.getCustomerId() != null) {
            Map<Long, CommonLevelVO> levelMap1 = goodsIntervalPriceService.getLevelMap(request.getCustomerId(), storeIds);
            levelMap.putAll(levelMap1);
        }

        //取商品的价格
        Map<String, BigDecimal> enterpriseMap = new HashMap<>();
        Map<String, BigDecimal> minMap = new HashMap<>();
        Map<String, BigDecimal> maxMap = new HashMap<>();

        goodsInfos.forEach(goodsInfo -> {
            if (goodsInfo.getEnterPriseAuditState() != EnterpriseAuditState.CHECKED) {
                return;
            }
            if (goodsInfo.getEnterprisePriceType() == 0) {
                enterpriseMap.put(goodsInfo.getGoodsInfoId(), goodsInfo.getEnterPrisePrice());
                minMap.put(goodsInfo.getGoodsInfoId(), goodsInfo.getEnterPrisePrice());
                maxMap.put(goodsInfo.getGoodsInfoId(), goodsInfo.getEnterPrisePrice());
            } else if (goodsInfo.getEnterprisePriceType() == 1) {
                BigDecimal price = goodsInfo.getEnterPrisePrice();
                //如果商品开启按等级设价--优先
                if (request.getCustomerId() != null && goodsInfo.getEnterpriseCustomerFlag()) {
                    GoodsCustomerPrice goodsCustomerPrice = customerPriceMap.get(goodsInfo.getGoodsInfoId());
                    if (goodsCustomerPrice != null) {
                        //按照客户设价
                        price = goodsCustomerPrice.getPrice();
                        if (goodsInfo.getEnterpriseDiscountFlag()) {
                            price = goodsInfo.getEnterPrisePrice().multiply(BigDecimal.valueOf(goodsCustomerPrice.getDiscount())).multiply(BigDecimal.valueOf(0.01));
                        }
                        enterpriseMap.put(goodsInfo.getGoodsInfoId(), price);
                        return;
                    }
                }
                //按客户等级设价
                List<GoodsLevelPrice> levelPrices = levelPriceMap.get(goodsInfo.getGoodsInfoId());
                if (request.getCustomerId() != null) {
                    CommonLevelVO commonLevelVO = levelMap.get(goodsInfo.getStoreId());
                    if (commonLevelVO != null && levelPrices != null && !levelPrices.isEmpty()) {
                        Optional<GoodsLevelPrice> first = levelPrices.stream().filter(e -> e.getLevelId().equals(commonLevelVO.getLevelId())).findFirst();
                        if (first.isPresent()) {
                            GoodsLevelPrice levelPrice = first.get();
                            price = levelPrice.getPrice();
                            if (goodsInfo.getEnterpriseDiscountFlag()) {
                                price = goodsInfo.getEnterPrisePrice().multiply(BigDecimal.valueOf(levelPrice.getDiscount())).multiply(BigDecimal.valueOf(0.01));
                            }
                        }
                    }
                } else {
                    if (CollectionUtils.isNotEmpty(levelPrices)) {
                        Optional<GoodsLevelPrice> first = levelPrices.stream().min(Comparator.comparing(GoodsLevelPrice::getPrice));
                        GoodsLevelPrice levelPrice = first.get();
                        price = levelPrice.getPrice();
                        if (goodsInfo.getEnterpriseDiscountFlag()) {
                            price = goodsInfo.getEnterPrisePrice().multiply(BigDecimal.valueOf(levelPrice.getDiscount())).multiply(BigDecimal.valueOf(0.01));
                        }
                    }
                }
                //取最小值
                enterpriseMap.put(goodsInfo.getGoodsInfoId(), price);
                minMap.put(goodsInfo.getGoodsInfoId(), price);
                maxMap.put(goodsInfo.getGoodsInfoId(), price);
            } else {
                //按购买数量设价
                long count = request.getBuyCountMap().get(goodsInfo.getGoodsInfoId());
                List<GoodsIntervalPriceVO> goodsIntervalPrices = intervalPriceMap.get(goodsInfo.getGoodsInfoId());
                Optional<GoodsIntervalPriceVO> intervalPrice;
                BigDecimal price = goodsInfo.getEnterPrisePrice();

                if (goodsIntervalPrices != null && !goodsIntervalPrices.isEmpty()) {
                    Optional<GoodsIntervalPriceVO> min = goodsIntervalPrices.stream().min(Comparator.comparing(GoodsIntervalPriceVO::getPrice));
                    Optional<GoodsIntervalPriceVO> max = goodsIntervalPrices.stream().max(Comparator.comparing(GoodsIntervalPriceVO::getPrice));

                    if (request.getListFlag()) {
                        intervalPrice = min;
                    } else {
                        //购物车--价格会重新计算
                        intervalPrice = Optional.empty();
                    }
                    //如果是订单
                    if (request.getOrderFlag()) {
                        //取区间价
                        intervalPrice = goodsIntervalPrices.stream().sorted(Comparator.comparingLong(GoodsIntervalPriceVO::getCount).reversed())
                                .filter(goodsIntervalPrice -> count >= goodsIntervalPrice.getCount()).findFirst();
                    }

                    if (intervalPrice.isPresent()) {
                        price = intervalPrice.get().getPrice();
                        if (goodsInfo.getEnterpriseDiscountFlag()) {
                            price = goodsInfo.getEnterPrisePrice().multiply(BigDecimal.valueOf(intervalPrice.get().getDiscount())).multiply(BigDecimal.valueOf(0.01));
                        }
                    }
                    min.ifPresent(goodsIntervalPriceVO -> {
                        BigDecimal minPrice = goodsInfo.getEnterPrisePrice();
                        if (goodsInfo.getEnterpriseDiscountFlag()) {
                            minPrice = goodsInfo.getEnterPrisePrice().multiply(BigDecimal.valueOf(goodsIntervalPriceVO.getDiscount())).multiply(BigDecimal.valueOf(0.01));
                        }
                        minMap.put(goodsInfo.getGoodsInfoId(), minPrice);
                    });
                    max.ifPresent(goodsIntervalPriceVO -> {
                        BigDecimal maxPrice = goodsInfo.getEnterPrisePrice();
                        if (goodsInfo.getEnterpriseDiscountFlag()) {
                            maxPrice = goodsInfo.getEnterPrisePrice().multiply(BigDecimal.valueOf(goodsIntervalPriceVO.getDiscount())).multiply(BigDecimal.valueOf(0.01));
                        }
                        maxMap.put(goodsInfo.getGoodsInfoId(), maxPrice);
                    });
                }
                enterpriseMap.put(goodsInfo.getGoodsInfoId(), price);
            }
        });
        response.setPriceMap(enterpriseMap);
        response.setIntervalMap(intervalPriceMap);
        response.setMinMap(minMap);
        response.setMaxMap(maxMap);
        return response;
    }

    public long countGoodPriceSync() {
        Specification<GoodsPriceSync> request = (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cbuild.equal(root.get("status"), 0));
            predicates.add(cbuild.equal(root.get("deleted"), 0));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
        return goodsPriceSyncRepository.count(request);
    }

    /**
     * 更新商品价格
     * @param priceSyncQueryRequest
     * @return
     */
    public MicroServicePage<GoodsInfoPriceChangeDTO> syncGoodsPrice(GoodsPriceSyncQueryRequest priceSyncQueryRequest) {
        Page<GoodsPriceSync> pricePage = goodsPriceSyncRepository.findAll(priceSyncQueryRequest.getWhereCriteria(),
                priceSyncQueryRequest.getPageRequest());
        MicroServicePage<GoodsInfoPriceChangeDTO> page = new MicroServicePage<>(
                new ArrayList<>(), PageRequest.of(priceSyncQueryRequest.getPageNum(), priceSyncQueryRequest.getPageSize(), priceSyncQueryRequest.getSort()),0);

        if(CollectionUtils.isEmpty(pricePage.getContent())){
             return page;
        }
        page.setTotal(pricePage.getTotalElements());
        List<GoodsPriceSync> goodsPriceList = pricePage.getContent();
        // 3 遍历商品列表并更新价格
        List<GoodsInfoPriceChangeDTO> result = new ArrayList<>(priceSyncQueryRequest.getPageSize());
        //根据goodsno查询sku列表
        List<String> goodsNos = goodsPriceList.stream().map(GoodsPriceSync::getGoodsNo).distinct().collect(Collectors.toList());
        List<GoodsStockInfo> goodsInfoList = goodsInfoRepository.findGoodsInfoByGoodsNos(goodsNos);
        if(CollectionUtils.isEmpty(goodsInfoList)){
            log.info("there is no goods info list,price:{}",goodsPriceList);
            goodsPriceSyncRepository.updateStatusByIds(goodsPriceList.stream().map(GoodsPriceSync::getId).collect(Collectors.toList()));
            return page;
        }
        //查询促销价
        List<GoodsSpecialPriceSync> specialPrice = goodsSpecialPriceSyncRepository.findByGoodsNo(goodsNos);
        goodsPriceList.forEach(price -> {
            goodsInfoService.updateGoodsPriceSingle(price,result,goodsInfoList,specialPrice);
        });
        page.setContent(result);
        return page;
    }

    @Transactional
    public void updateGoodsPriceSingle(GoodsPriceSync price,List<GoodsInfoPriceChangeDTO> result,List<GoodsStockInfo> goodsInfoList,List<GoodsSpecialPriceSync> specialPrice){
        //查询sku信息
        Optional<GoodsStockInfo> goodsInfoOptional = goodsInfoList.stream().filter(p->p.getGoodsNo().equals(price.getGoodsNo())).findFirst();
        if (!goodsInfoOptional.isPresent() || goodsInfoOptional.get().getCostPriceSyncFlag().equals(0)) {
            goodsPriceSyncRepository.updateStatus(price.getId());
            log.info("updateGoodsPriceSingle there is no sku or costPriceSyncFlag is 0,goods:{},goodsInfo flag:{}", price,goodsInfoOptional.isPresent());
            return;
        }
        GoodsStockInfo goodsInfo = goodsInfoOptional.get();
        BigDecimal costPrice = price.getPrice();
        LocalDateTime startTime = null;
        LocalDateTime endTime =null;
        BigDecimal originalPrice = goodsInfo.getCostPrice();
        Optional<GoodsSpecialPriceSync> priceOptional = specialPrice.stream().filter(p->p.getGoodsNo().equals(goodsInfo.getGoodsNo()) && p.getStartTime().compareTo(LocalDateTime.now()) <=0 && p.getEndTime().compareTo(LocalDateTime.now()) >=0).findFirst();
        if(priceOptional.isPresent()){
            //有促销价，则成本价=促销价
            costPrice = priceOptional.get().getSpecialprice();
            startTime = priceOptional.get().getStartTime();
            endTime = priceOptional.get().getEndTime();
        }
        //现有成本价与数据库成本价比较，不一致则更新
        if(costPrice.compareTo(goodsInfo.getCostPrice()) == 0){
            goodsPriceSyncRepository.updateStatus(price.getId());
            log.info("there is no cost price change,goods:{}",price);
            return;
        }
        goodsInfoRepository.updateGoodsPriceById(goodsInfo.getGoodsInfoId(),costPrice,startTime,endTime);
        //更新spu价格
        goodsRepository.resetGoodsPriceById(goodsInfo.getGoodsId(),costPrice);
        result.add(GoodsInfoPriceChangeDTO.builder().goodsInfoId(goodsInfo.getGoodsInfoId())
                .goodsId(goodsInfo.getGoodsId())
                .time(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now()))
                .oldPrice(originalPrice)
                .newPrice(costPrice)
                .name(goodsInfo.getGoodsInfoName())
                .skuNo(goodsInfo.getGoodsInfoNo())
                .marketPrice(goodsInfo.getMarketPrice()).build());
        //更新状态
        goodsPriceSyncRepository.updateStatus(price.getId());
    }


    /**
     * 更新商品价格
     * @return
     */
    @Transactional
    public void bookuuSyncGoodsPrice(List<String> goodsIdList) {
        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        infoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
        infoQueryRequest.setGoodsIds(goodsIdList);
        List<GoodsInfo> goodsInfoList = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
        if (CollectionUtils.isEmpty(goodsInfoList)) {
            return;
        }

        GoodsCostPriceRequest request = new GoodsCostPriceRequest();
        request.setGoodsIdList(goodsIdList);
        BaseResponse<List<GoodsCostPriceResponse>> goodsCostPriceResponse = booKuuSupplierClient.getGoodsCostPrice(request);
        List<GoodsCostPriceResponse> context = goodsCostPriceResponse.getContext();
        if (CollectionUtils.isEmpty(context)) {
            return;
        }

        Map<String, GoodsCostPriceResponse> erpGoodsNo2GoodsCostPriceMap =
                context.stream().collect(Collectors.toMap(GoodsCostPriceResponse::getErpGoodsNo, Function.identity(), (k1, k2) -> k1));

        //促销价
        List<GoodsSpecialPriceSync> specialPrice = goodsSpecialPriceSyncRepository.findByGoodsNo(new ArrayList<>(erpGoodsNo2GoodsCostPriceMap.keySet()));
        Map<String, GoodsSpecialPriceSync> erpGoodsNo2GoodsSpecailPriceMap
                = specialPrice.stream().collect(Collectors.toMap(GoodsSpecialPriceSync::getGoodsNo, Function.identity(), (k1, k2) -> k1));


        for (GoodsInfo goodsInfoParam : goodsInfoList) {
            if (Objects.equals(goodsInfoParam.getCostPriceSyncFlag(), 0)) {
                continue;
            }
            BigDecimal costPrice = null;
            LocalDateTime startTime = null;
            LocalDateTime endTime =null;

            GoodsSpecialPriceSync goodsSpecialPriceSync = erpGoodsNo2GoodsSpecailPriceMap.get(goodsInfoParam.getErpGoodsNo());
            if (goodsSpecialPriceSync != null) {
                costPrice = goodsSpecialPriceSync.getSpecialprice();
                startTime = goodsSpecialPriceSync.getStartTime();
                endTime = goodsSpecialPriceSync.getEndTime();
            }

            if(costPrice == null) {
                GoodsCostPriceResponse goodsCostPrice = erpGoodsNo2GoodsCostPriceMap.get(goodsInfoParam.getErpGoodsNo());
                if (goodsCostPrice == null) {
                    continue;
                }
                costPrice = goodsCostPrice.getCostPrice();
            }
            log.info("GoodsInfoService bookuuSyncGoodsPrice goodsInfoId:{} erpGoodsNo:{} costPrice:{} goodsInfoParam.costPrice:{}",
                    goodsInfoParam.getGoodsInfoId(), goodsInfoParam.getErpGoodsNo(), costPrice, goodsInfoParam.getCostPrice());
            if (costPrice == null) {
                continue;
            }

            if (goodsInfoParam.getCostPrice().compareTo(costPrice) == 0) {
                continue;
            }

            goodsInfoRepository.updateGoodsPriceById(goodsInfoParam.getGoodsInfoId(),costPrice,startTime,endTime);
            //更新spu价格
            goodsRepository.resetGoodsPriceById(goodsInfoParam.getGoodsId(),costPrice);


        }
    }

    /**
     * 同步管易成本价
     */
//    public MicroServicePage<GoodsInfoPriceChangeDTO> syncGoodsCostPrice(GoodsCostPriceChangeQueryRequest request){
//        request.setSortColumn("erpGoodsNo");
//        request.setSortRole("asc");
//        Page<GoodsInfo> goodsInfoPage = goodsInfoRepository.findAll(request.getWhereCriteria(),request.getPageRequest());
//        MicroServicePage page = new MicroServicePage<>(
//                Collections.emptyList(),
//                PageRequest.of(request.getPageNum(), request.getPageSize(), request.getSort()),
//                0
//        );
//        if(goodsInfoPage == null || CollectionUtils.isEmpty(goodsInfoPage.getContent())){
//            return page;
//        }
//        page.setTotal(goodsInfoPage.getTotalElements());
//        List<GoodsInfoPriceChangeDTO> list = new ArrayList<>(request.getPageSize());
//        List<String> erpGoodsNo = goodsInfoPage.getContent().stream().map(GoodsInfo::getErpGoodsNo).distinct().collect(Collectors.toList());
//        erpGoodsNo.forEach(g->{
//            try {
//                BaseResponse<List<ERPGoodsInfoVO>> erpGoodsInfoWithoutStock = guanyierpProvider.getErpGoodsInfoWithoutStock(g);
//                if (erpGoodsInfoWithoutStock == null || CollectionUtils.isEmpty(erpGoodsInfoWithoutStock.getContext())) {
//                    return;
//                }
//                List<GoodsInfo> items = goodsInfoPage.getContent().stream().filter(p -> p.getErpGoodsNo().equals(g)).collect(Collectors.toList());
//                items.forEach(goodsInfo -> {
//                    Optional<ERPGoodsInfoVO> erpGoodsInfoVO = erpGoodsInfoWithoutStock.getContext().stream().filter(p -> p.getSkuCode().equals(goodsInfo.getErpGoodsInfoNo())).findFirst();
//                    if (!erpGoodsInfoVO.isPresent()) {
//                        return;
//                    }
//                    if (erpGoodsInfoVO.get().getCostPrice().compareTo(goodsInfo.getCostPrice()) == 0) {
//                        return;
//                    }
//                    //成本不一致
//                    goodsInfoRepository.updateCostPriceById(goodsInfo.getGoodsInfoId(), erpGoodsInfoVO.get().getCostPrice());
//                    list.add(GoodsInfoPriceChangeDTO.builder().goodsInfoId(goodsInfo.getGoodsInfoId())
//                            .goodsId(goodsInfo.getGoodsId())
//                            .time(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now()))
//                            .oldPrice(goodsInfo.getCostPrice())
//                            .newPrice(erpGoodsInfoVO.get().getCostPrice())
//                            .name(goodsInfo.getGoodsInfoName())
//                            .skuNo(goodsInfo.getGoodsInfoNo())
//                            .marketPrice(goodsInfo.getMarketPrice()).build());
//                });
//            }catch (Exception e){
//                log.warn("syncGoodsCostPrice error,goodsId:{}",g,e);
//            }
//        });
//        page.setContent(list);
//        return page;
//    }
}
