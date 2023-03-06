package com.wanmi.sbc.goods.info.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.google.gson.JsonArray;
import com.soybean.common.util.WebConstantUtil;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.OsUtil;
import com.wanmi.sbc.customer.api.constant.SigningClassErrorCode;
import com.wanmi.sbc.customer.api.constant.StoreCateErrorCode;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.goods.api.constant.GoodsBrandErrorCode;
import com.wanmi.sbc.goods.api.constant.GoodsCateErrorCode;
import com.wanmi.sbc.goods.api.constant.GoodsErrorCode;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterprisePriceGetRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsDataUpdateRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsDeleteByIdsRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifyCollectNumRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifyEvaluateNumRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifySalesNumRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsQueryNeedSynRequest;
import com.wanmi.sbc.goods.api.request.goods.GoodsUpdateProviderRequest;
import com.wanmi.sbc.goods.api.request.goods.ProviderGoodsNotSellRequest;
import com.wanmi.sbc.goods.api.request.goods.ThirdGoodsVendibilityRequest;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsQueryRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponGoodsRequest;
import com.wanmi.sbc.goods.api.request.virtualcoupon.VirtualCouponQueryRequest;
import com.wanmi.sbc.goods.api.response.enterprise.EnterprisePriceResponse;
import com.wanmi.sbc.goods.ares.GoodsAresService;
import com.wanmi.sbc.goods.bean.dto.GoodsPackDetailDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.GoodsPriceType;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.enums.PriceType;
import com.wanmi.sbc.goods.bean.enums.SaleType;
import com.wanmi.sbc.goods.bean.enums.UnAddedFlagReason;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import com.wanmi.sbc.goods.brand.repository.ContractBrandRepository;
import com.wanmi.sbc.goods.brand.repository.GoodsBrandRepository;
import com.wanmi.sbc.goods.brand.request.ContractBrandQueryRequest;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.model.root.GoodsCateSync;
import com.wanmi.sbc.goods.cate.repository.ContractCateRepository;
import com.wanmi.sbc.goods.cate.repository.GoodsCateRepository;
import com.wanmi.sbc.goods.cate.repository.GoodsCateSyncRepository;
import com.wanmi.sbc.goods.cate.request.ContractCateQueryRequest;
import com.wanmi.sbc.goods.cate.service.GoodsCateService;
import com.wanmi.sbc.goods.classify.model.root.ClassifyDTO;
import com.wanmi.sbc.goods.classify.model.root.ClassifyGoodsRelDTO;
import com.wanmi.sbc.goods.classify.repository.ClassifyGoodsRelRepository;
import com.wanmi.sbc.goods.classify.repository.ClassifyRepository;
import com.wanmi.sbc.goods.common.GoodsCommonService;
import com.wanmi.sbc.goods.common.GoodsPackDetailRepository;
import com.wanmi.sbc.goods.distributor.goods.repository.DistributiorGoodsInfoRepository;
import com.wanmi.sbc.goods.fandeng.SiteSearchService;
import com.wanmi.sbc.goods.freight.model.root.FreightTemplateGoods;
import com.wanmi.sbc.goods.freight.repository.FreightTemplateGoodsRepository;
import com.wanmi.sbc.goods.images.GoodsImage;
import com.wanmi.sbc.goods.images.GoodsImageRepository;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsPropDetailRel;
import com.wanmi.sbc.goods.info.model.root.GoodsSync;
import com.wanmi.sbc.goods.info.model.root.GoodsSyncRelation;
import com.wanmi.sbc.goods.info.model.root.GoodsVote;
import com.wanmi.sbc.goods.info.reponse.GoodsDetailResponse;
import com.wanmi.sbc.goods.info.reponse.GoodsEditResponse;
import com.wanmi.sbc.goods.info.reponse.GoodsQueryResponse;
import com.wanmi.sbc.goods.info.reponse.GoodsResponse;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsPropDetailRelRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.repository.GoodsSyncRelationRepository;
import com.wanmi.sbc.goods.info.repository.GoodsSyncRepository;
import com.wanmi.sbc.goods.info.repository.GoodsVoteRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsRequest;
import com.wanmi.sbc.goods.info.request.GoodsSaveRequest;
import com.wanmi.sbc.goods.info.request.GoodsSyncQueryRequest;
import com.wanmi.sbc.goods.mini.enums.goods.WxGoodsEditStatus;
import com.wanmi.sbc.goods.mini.model.goods.WxGoodsModel;
import com.wanmi.sbc.goods.mini.repository.goods.WxGoodsRepository;
import com.wanmi.sbc.goods.pointsgoods.model.root.PointsGoods;
import com.wanmi.sbc.goods.pointsgoods.repository.PointsGoodsRepository;
import com.wanmi.sbc.goods.pointsgoods.service.PointsGoodsWhereCriteriaBuilder;
import com.wanmi.sbc.goods.price.model.root.GoodsCustomerPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsIntervalPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsLevelPrice;
import com.wanmi.sbc.goods.price.repository.GoodsCustomerPriceRepository;
import com.wanmi.sbc.goods.price.repository.GoodsIntervalPriceRepository;
import com.wanmi.sbc.goods.price.repository.GoodsLevelPriceRepository;
import com.wanmi.sbc.goods.price.service.GoodsIntervalPriceService;
import com.wanmi.sbc.goods.prop.model.root.GoodsProp;
import com.wanmi.sbc.goods.prop.repository.GoodsPropRepository;
import com.wanmi.sbc.goods.redis.RedisService;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpec;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpecDetail;
import com.wanmi.sbc.goods.spec.repository.GoodsInfoSpecDetailRelRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecDetailRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecRepository;
import com.wanmi.sbc.goods.spec.service.GoodsInfoSpecDetailRelService;
import com.wanmi.sbc.goods.standard.model.root.StandardGoodsRel;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRelRepository;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRepository;
import com.wanmi.sbc.goods.standard.repository.StandardSkuRepository;
import com.wanmi.sbc.goods.standard.service.StandardImportService;
import com.wanmi.sbc.goods.storecate.model.root.StoreCateGoodsRela;
import com.wanmi.sbc.goods.storecate.repository.StoreCateGoodsRelaRepository;
import com.wanmi.sbc.goods.storegoodstab.model.root.GoodsTabRela;
import com.wanmi.sbc.goods.storegoodstab.model.root.StoreGoodsTab;
import com.wanmi.sbc.goods.storegoodstab.repository.GoodsTabRelaRepository;
import com.wanmi.sbc.goods.storegoodstab.repository.StoreGoodsTabRepository;
import com.wanmi.sbc.goods.tag.service.TagService;
import com.wanmi.sbc.goods.virtualcoupon.model.root.VirtualCoupon;
import com.wanmi.sbc.goods.virtualcoupon.service.VirtualCouponService;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商品服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class GoodsService {

    @Value("${stock.threshold:5}")
    private Integer stockThreshold;

    @Autowired
    GoodsAresService goodsAresService;

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
    private GoodsImageRepository goodsImageRepository;

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
    private GoodsTabRelaRepository goodsTabRelaRepository;

    @Autowired
    private StoreCateGoodsRelaRepository storeCateGoodsRelaRepository;

    @Autowired
    private ClassifyRepository classifyRepository;

    @Autowired
    private StoreGoodsTabRepository storeGoodsTabRepository;

    @Autowired
    private ContractCateRepository contractCateRepository;

    @Autowired
    private ContractBrandRepository contractBrandRepository;

    @Autowired
    private GoodsPropDetailRelRepository goodsPropDetailRelRepository;

    @Autowired
    private GoodsPropRepository goodsPropRepository;

    @Autowired
    private StandardGoodsRelRepository standardGoodsRelRepository;

    @Autowired
    private StandardGoodsRepository standardGoodsRepository;

    @Autowired
    private StandardSkuRepository standardSkuRepository;

    @Autowired
    private GoodsCateService goodsCateService;

    @Autowired
    private GoodsCommonService goodsCommonService;

    @Autowired
    private FreightTemplateGoodsRepository freightTemplateGoodsRepository;

    @Autowired
    private OsUtil osUtil;

    @Autowired
    private DistributiorGoodsInfoRepository distributiorGoodsInfoRepository;

    @Autowired
    private PointsGoodsRepository pointsGoodsRepository;

    @Autowired
    private GoodsInfoStockService goodsInfoStockService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private GoodsInfoSpecDetailRelService goodsInfoSpecDetailRelService;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsIntervalPriceService goodsIntervalPriceService;

    @Autowired
    private StandardImportService standardImportService;

    @Autowired
    private VirtualCouponService virtualCouponService;
    @Autowired
    private TagService tagService;
    @Autowired
    private GoodsSyncRepository goodsSyncRepository;

//    @Autowired
//    private GoodsStockSyncRepository goodsStockSyncRepository;
//
//    @Autowired
//    private GoodsPriceSyncRepository goodsPriceSyncRepository;

    @Autowired
    private ClassifyGoodsRelRepository classifyGoodsRelRepository;

    @Autowired
    private GoodsSyncRelationRepository goodsSyncRelationRepository;

    @Value("${default.providerId}")
    private Long defaultProvider;

    @Autowired
    private GoodsCateSyncRepository goodsCateSyncRepository;

    @Autowired
    private GoodsVoteRepository goodsVoteRepository;

    @Autowired
    private WxGoodsRepository wxGoodsRepository;

    @Autowired
    private SiteSearchService siteSearchService;

    @Autowired
    private GoodsPackDetailRepository goodsPackDetailRepository;


    /**
     * 供应商商品删除
     *
     * @param request 多个商品
     * @throws SbcRuntimeException
     */
    @Transactional
    public void deleteProvider(GoodsDeleteByIdsRequest request) throws SbcRuntimeException {

        List<String> goodsIds = request.getGoodsIds();
//
        List<StandardGoodsRel> standardGoodsRels = standardGoodsRelRepository.findByGoodsIds(goodsIds);
        if (CollectionUtils.isNotEmpty(standardGoodsRels)) {
            List<String> standardIds = standardGoodsRels.stream().map(StandardGoodsRel::getStandardId).collect(Collectors.toList());
            //删除供应商商品库商品
            standardGoodsRepository.deleteByGoodsIds(standardIds);
            standardSkuRepository.deleteByGoodsIds(standardIds);

            /*同步代码 关联商家商品下架
            goodsRepository.updateAddedFlagByPrividerGoodsIds(AddedFlag.NO.toValue(),goodsIds,UnAddedFlagReason.PROVIDERDELETE.toString());*/
            //供应商商品下架
            goodsRepository.updateAddedFlagByGoodsIds(AddedFlag.NO.toValue(), goodsIds, UnAddedFlagReason.PROVIDERDELETE.toString());

        }

        //供应商商品 删除
        goodsRepository.deleteProviderByGoodsIds(goodsIds, request.getDeleteReason());
        goodsInfoRepository.deleteByGoodsIds(goodsIds);
        goodsPropDetailRelRepository.deleteByGoodsIds(goodsIds);
        goodsSpecRepository.deleteByGoodsIds(goodsIds);
        goodsSpecDetailRepository.deleteByGoodsIds(goodsIds);
        goodsInfoSpecDetailRelRepository.deleteByGoodsIds(goodsIds);
        standardGoodsRelRepository.deleteByGoodsIds(goodsIds);
        pointsGoodsRepository.deleteByGoodsIdList(goodsIds);
        goodsIds.forEach(goodsID -> {
            distributiorGoodsInfoRepository.deleteByGoodsId(goodsID);
        });

        ProviderGoodsNotSellRequest providerGoodsNotSellRequest = ProviderGoodsNotSellRequest.builder()
                .checkFlag(Boolean.FALSE).goodsIds(goodsIds).build();
//        this.dealGoodsVendibility(providerGoodsNotSellRequest);


        //ares埋点-商品-后台批量删除商品spu
        goodsAresService.dispatchFunction("delGoodsSpu", goodsIds);

        //删除主站搜素
        siteSearchService.siteSearchBookResNotify(goodsIds);
    }

    /**
     * 更新商品上下架状态(如果存在 被boss端删除了的供货商商品库商品，则失败)
     *
     * @param addedFlag 状态
     * @param goodsIds  多个商品
     * @throws SbcRuntimeException
     */
    @Transactional
    public void providerUpdateAddedStatus(Integer addedFlag, List<String> goodsIds) throws SbcRuntimeException {

        if (0 == addedFlag) {
//            下架
            // 供应商商品下架
            goodsRepository.updateAddedFlagByGoodsIds(addedFlag, goodsIds, UnAddedFlagReason.PROVIDERUNADDED.toString());
            //同步代码 供应商关联商品下架
            /*goodsRepository.updateAddedFlagByPrividerGoodsIds(addedFlag, goodsIds, UnAddedFlagReason.PROVIDERUNADDED.toString());*/
            goodsInfoRepository.updateAddedFlagByGoodsIds(addedFlag, goodsIds);
            goodsIds.forEach(goodsID -> {
                distributiorGoodsInfoRepository.deleteByGoodsId(goodsID);
            });
            ProviderGoodsNotSellRequest request = ProviderGoodsNotSellRequest.builder().checkFlag(Boolean.FALSE).goodsIds(goodsIds).build();
//            this.dealGoodsVendibility(request);
        } else {
//          上架
            List<StandardGoodsRel> goodsIdIn = standardGoodsRelRepository.findByDelFlagAndGoodsIdIn(DeleteFlag.YES, goodsIds);
            if (CollectionUtils.isNotEmpty(goodsIdIn)) {
                throw new SbcRuntimeException(GoodsErrorCode.STANDARDGOODS_DELETED, "商品关联商品库已被平台删除");
            } else {
                goodsRepository.updateAddedFlagByGoodsIds(addedFlag, goodsIds);
                goodsInfoRepository.updateAddedFlagByGoodsIds(addedFlag, goodsIds);
                ProviderGoodsNotSellRequest request = ProviderGoodsNotSellRequest.builder().checkFlag(Boolean.TRUE).goodsIds(goodsIds).build();
//                this.dealGoodsVendibility(request);
            }
        }

        //同步商品库上下架状态
        List<GoodsInfo> oldGoodsInfos = goodsInfoRepository.findByGoodsIdIn(goodsIds);

        if (CollectionUtils.isNotEmpty(oldGoodsInfos)) {
            for (GoodsInfo goodsInfo : oldGoodsInfos) {
                StandardGoodsRel standardGoodsRel = standardGoodsRelRepository.findByGoodsIdAndDelFlag(goodsInfo.getGoodsId(), DeleteFlag.NO);
                if (standardGoodsRel != null) {
                    standardGoodsRepository.updateAddedFlag(standardGoodsRel.getStandardId(), addedFlag);
                    List<StandardSku> standardSkuList = standardSkuRepository.findByGoodsId(standardGoodsRel.getStandardId());
                    if (CollectionUtils.isNotEmpty(standardSkuList)) {
                        for (StandardSku standardSku : standardSkuList) {
                            if (goodsInfo.getGoodsInfoId().equals(standardSku.getProviderGoodsInfoId())) {
                                standardSkuRepository.updateAddedFlag(standardSku.getGoodsInfoId(), addedFlag);
                            }
                        }
                    }
                }
            }
        }

        siteSearchService.siteSearchBookResNotify(goodsIds);
    }

    /**
     * 分页查询商品
     *
     * @param request 参数
     * @return list
     */
    public GoodsQueryResponse page(GoodsQueryRequest request) {
        GoodsQueryResponse response = new GoodsQueryResponse();

        List<GoodsInfo> goodsInfos = new ArrayList<>();
        List<GoodsBrand> goodsBrandList = new ArrayList<>();
        List<GoodsCate> goodsCateList = new ArrayList<>();

        //根据SKU模糊查询SKU，获取SKU编号
        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        if (StringUtils.isNotBlank(request.getLikeGoodsInfoNo())) {
            infoQueryRequest.setLikeGoodsInfoNo(request.getLikeGoodsInfoNo());
            infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            List<GoodsInfo> infos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
            if (CollectionUtils.isNotEmpty(infos)) {
                request.setGoodsIds(infos.stream().map(GoodsInfo::getGoodsId).collect(Collectors.toList()));
            } else {
                return response;
            }
        }

        //获取该分类的所有子分类
        if (request.getCateId() != null) {
            request.setCateIds(goodsCateService.getChlidCateId(request.getCateId()));
            if (CollectionUtils.isNotEmpty(request.getCateIds())) {
                request.getCateIds().add(request.getCateId());
                request.setCateId(null);
            }
        }

        Page<Goods> goodsPage = goodsRepository.findAll(request.getWhereCriteria(), request.getPageRequest());
        /*if (CollectionUtils.isNotEmpty(goodsPage.getContent())) {
            List<String> goodsIds = goodsPage.getContent().stream().map(Goods::getGoodsId).collect(Collectors.toList());
            //查询所有SKU
            infoQueryRequest.setLikeGoodsInfoNo(null);
            infoQueryRequest.setGoodsIds(goodsIds);
            infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            goodsInfos.addAll(goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria()));
            //查询所有SKU规格值关联
            Map<String, String> goodsInfoSpecDetails = goodsInfoSpecDetailRelService.textByGoodsIds(goodsIds);

            //填充每个SKU的规格关系
            goodsInfos.forEach(goodsInfo -> {
                //为空，则以商品主图
                if (StringUtils.isBlank(goodsInfo.getGoodsInfoImg())) {
                    goodsInfo.setGoodsInfoImg(goodsPage.getContent().stream().filter(goods -> goods.getGoodsId().equals(goodsInfo.getGoodsId())).findFirst().orElse(new Goods()).getGoodsImg());
                }
                goodsInfo.setSpecText(goodsInfoSpecDetails.get(goodsInfo.getGoodsInfoId()));

                //外部重复处理
                updateGoodsInfoSupplyPriceAndStock(goodsInfo);

            });

            //填充每个SKU的SKU关系
            goodsPage.getContent().forEach(goods -> {
                goods.setGoodsInfoIds(goodsInfos.stream().filter(goodsInfo -> goodsInfo.getGoodsId().equals(goods.getGoodsId())).map(GoodsInfo::getGoodsInfoId).collect(Collectors.toList()));
                //合计库存
                goods.setStock(goodsInfos.stream().filter(goodsInfo -> goodsInfo.getGoodsId().equals(goods.getGoodsId()) && Objects.nonNull(goodsInfo.getStock())).mapToLong(GoodsInfo::getStock).sum());

                GoodsInfo tempGoodsInfo = goodsInfos.stream().filter(goodsInfo -> goods.getGoodsId().equals(goodsInfo.getGoodsId())).filter(goodsInfo -> Objects.nonNull(goodsInfo.getMarketPrice())).min(Comparator.comparing(GoodsInfo::getMarketPrice)).orElse(null);
                //取SKU最小市场价
                goods.setMarketPrice(tempGoodsInfo != null ? tempGoodsInfo.getMarketPrice() : goods.getMarketPrice());
                //取最小市场价SKU的相应购买积分
                goods.setBuyPoint(0L);
                if(tempGoodsInfo!= null && Objects.nonNull(tempGoodsInfo.getBuyPoint())) {
                    goods.setBuyPoint(tempGoodsInfo.getBuyPoint());
                }
                //取SKU最小供货价I
                goods.setSupplyPrice(goodsnfos.stream().filter(goodsInfo -> goods.getGoodsId().equals(goodsInfo.getGoodsId())).filter(goodsInfo -> Objects.nonNull(goodsInfo.getSupplyPrice())).map(GoodsInfo::getSupplyPrice).min(BigDecimal::compareTo).orElse(goods.getSupplyPrice()));
            });

            //获取所有品牌
            GoodsBrandQueryRequest brandRequest = new GoodsBrandQueryRequest();
            brandRequest.setDelFlag(DeleteFlag.NO.toValue());
            brandRequest.setBrandIds(goodsPage.getContent().stream().filter
                    (goods -> Objects.nonNull(goods.getBrandId())).map(Goods::getBrandId).distinct().collect(Collectors.toList()));
            goodsBrandList.addAll(goodsBrandRepository.findAll(brandRequest.getWhereCriteria()));

            //获取所有分类
            GoodsCateQueryRequest cateRequest = new GoodsCateQueryRequest();
            cateRequest.setCateIds(goodsPage.getContent().stream().filter(goods -> Objects.nonNull(goods.getCateId())).map(Goods::getCateId).distinct().collect(Collectors.toList()));
            goodsCateList.addAll(goodsCateRepository.findAll(cateRequest.getWhereCriteria()));
        }*/
        response.setGoodsPage(goodsPage);
        response.setGoodsInfoList(goodsInfos);
        response.setGoodsBrandList(goodsBrandList);
        response.setGoodsCateList(goodsCateList);

        //如果是linkedmall商品，实时查库存
        List<String> spuIds = response.getGoodsPage().getContent().stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .map(Goods::getGoodsId)
                .distinct()
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(spuIds)) {
            return response;
        }

        infoQueryRequest.setGoodsIds(spuIds);
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        List<GoodsInfo> skuList = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());

        List<Long> itemIds = response.getGoodsPage().getContent().stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .distinct()
                .collect(Collectors.toList());


        List<QueryItemInventoryResponse.Item> stocks = null;
        if (itemIds.size() > 0) {
            stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
        }
        if (stocks != null) {
            for (GoodsInfo goodsInfo : skuList) {
                for (QueryItemInventoryResponse.Item spuStock : stocks) {
                    if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {
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
            }
            for (Goods goods : response.getGoodsPage().getContent()) {
                if (ThirdPlatformType.LINKED_MALL.equals(goods.getThirdPlatformType())) {
                    Long spuStock = skuList.stream()
                            .filter(v -> v.getGoodsId().equals(goods.getGoodsId()) && v.getStock() != null)
                            .map(v -> v.getStock()).reduce(0L, (aLong, aLong2) -> aLong + aLong2);
                    goods.setStock(spuStock);
                }
            }

        }
        return response;
    }

    public Page<Goods> pageForXsite(GoodsQueryRequest request) {
        return goodsRepository.findAll(request.getWhereCriteria(), request.getPageRequest());
    }


    /**
     * @param goods 商品
     * @return list
     */
    public GoodsEditResponse findInfoByIdCache(Goods goods, String customerId) throws SbcRuntimeException {
        GoodsEditResponse response = new GoodsEditResponse();
        if (Objects.isNull(goods)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        response.setGoods(goods);

        //查询商品图片
        response.setImages(goodsImageRepository.findByGoodsId(goods.getGoodsId()));
        //标签
        response.setTags(tagService.findTagByGoods(goods.getGoodsId()));
        //是否书籍
        GoodsCate cate = goodsCateService.findById(goods.getCateId());
        response.setBookFlag(cate.getBookFlag());

        //商品属性
        response.setGoodsPropDetailRels(goodsPropDetailRelRepository.queryByGoodsId(goods.getGoodsId()));
        List<GoodsPropDetailRel> goodsPropDetailRels = goodsPropDetailRelRepository.queryByGoodsId(goods.getGoodsId());
        Map<Long, List<GoodsPropDetailRel>> idRel = goodsPropDetailRels.stream().collect(Collectors.groupingBy(GoodsPropDetailRel::getPropId));
        List<GoodsProp> props = findPropByIds(new ArrayList<>(idRel.keySet()));
        if(CollectionUtils.isNotEmpty(props)){
            for (GoodsProp prop : props) {
                List<GoodsPropDetailRel> goodsPropDetailRelVos = idRel.get(prop.getPropId());
                for (GoodsPropDetailRel goodsPropDetailRelVo : goodsPropDetailRelVos) {
                    goodsPropDetailRelVo.setPropName(prop.getPropName());
                    goodsPropDetailRelVo.setPropType(prop.getPropType());
                }
            }
        }
        Iterator<GoodsPropDetailRel> it = goodsPropDetailRels.iterator();
        Map<String, String> extProps = new HashMap<>();
        while (it.hasNext()) {
            GoodsPropDetailRel rel = it.next();
            if("作者".equals(rel.getPropName())){
                extProps.put("author", rel.getPropValue());
                it.remove();
            }else if("出版社".equals(rel.getPropName())){
                extProps.put("publisher", rel.getPropValue());
                it.remove();
            }else if("定价".equals(rel.getPropName())){
                extProps.put("price", rel.getPropValue());
                it.remove();
            }else if("评分".equals(rel.getPropName())){
                extProps.put("score", rel.getPropValue());
                it.remove();
            }
        }
        response.setExtProps(extProps);
        response.setGoodsPropDetailRels(goodsPropDetailRels);

        List<String> enterpriseGoodsInfoIds = new ArrayList<>();
        Map<String, Long> buyCountMap = new HashMap<>();
        //查询SKU列表
        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setGoodsId(goods.getGoodsId());
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
        Long goodsStock = 0L;
        for (GoodsInfo goodsInfo : goodsInfos) {
            if (goodsInfo.getEnterPriseAuditState() == EnterpriseAuditState.CHECKED) {
                enterpriseGoodsInfoIds.add(goodsInfo.getGoodsInfoId());
                buyCountMap.put(goodsInfo.getGoodsInfoId(), 1L);
            }
            goodsInfo.setSalePrice(Objects.isNull(goodsInfo.getMarketPrice()) ? BigDecimal.ZERO : goodsInfo.getMarketPrice());
            goodsInfo.setPriceType(goods.getPriceType());
            goodsInfo.setBrandId(goods.getBrandId());
            goodsInfo.setCateId(goods.getCateId());
            Long goodsInfoStock = goodsInfoStockService.checkStockCache(goodsInfo.getGoodsInfoId());
            Long actualStock = goodsInfoStock;
            if(goodsInfoStock <= 5){
                actualStock = 0L;
            }
            goodsInfo.setStock(actualStock);
            goodsStock += actualStock;
        }
        if(goodsStock <= 5) {
            goodsStock = 0L;
        }
        goods.setStock(goodsStock);
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
            }
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

        //如果是多规格
        if (Constants.yes.equals(goods.getMoreSpecFlag())) {
            response.setGoodsSpecs(goodsSpecRepository.findByGoodsId(goods.getGoodsId()));
            response.setGoodsSpecDetails(goodsSpecDetailRepository.findByGoodsId(goods.getGoodsId()));

            //对每个规格填充规格值关系
            response.getGoodsSpecs().forEach(goodsSpec -> {
                goodsSpec.setSpecDetailIds(response.getGoodsSpecDetails().stream().filter(specDetail -> specDetail.getSpecId().equals(goodsSpec.getSpecId())).map(GoodsSpecDetail::getSpecDetailId).collect(Collectors.toList()));
            });

            //对每个SKU填充规格和规格值关系
            Map<String, List<GoodsInfoSpecDetailRel>> goodsInfoSpecDetailRels = goodsInfoSpecDetailRelRepository.findByGoodsId(goods.getGoodsId()).stream().collect(Collectors.groupingBy(GoodsInfoSpecDetailRel::getGoodsInfoId));
            goodsInfos.forEach(goodsInfo -> {
                goodsInfo.setMockSpecIds(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getSpecId).collect(Collectors.toList()));
                goodsInfo.setMockSpecDetailIds(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getSpecDetailId).collect(Collectors.toList()));
                goodsInfo.setMockSpecDetailName(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getDetailName).collect(Collectors.toList()));
            });
        }
        response.setGoodsInfos(goodsInfos);

        //商品按订货区间，查询订货区间
        if (Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(goods.getPriceType())) {
            response.setGoodsIntervalPrices(goodsIntervalPriceRepository.findByGoodsId(goods.getGoodsId()));
        } else if (Integer.valueOf(GoodsPriceType.CUSTOMER.toValue()).equals(goods.getPriceType())) {
            response.setGoodsLevelPrices(goodsLevelPriceRepository.findByGoodsId(goods.getGoodsId()));
            //如果是按单独客户定价
            if (Constants.yes.equals(goods.getCustomFlag())) {
                response.setGoodsCustomerPrices(goodsCustomerPriceRepository.findByGoodsId(goods.getGoodsId()));
            }
        }
        //按客户等级设价
        this.getGoodsIntervalPrice(response, customerId);
        //企业设价--填充
        EnterprisePriceGetRequest enterprisePriceGetRequest = new EnterprisePriceGetRequest();
        enterprisePriceGetRequest.setGoodsInfoIds(enterpriseGoodsInfoIds);
        enterprisePriceGetRequest.setCustomerId(customerId);
        enterprisePriceGetRequest.setOrderFlag(false);
        enterprisePriceGetRequest.setListFlag(false);
        enterprisePriceGetRequest.setBuyCountMap(buyCountMap);
        EnterprisePriceResponse userPrice = goodsInfoService.getUserPrice(enterprisePriceGetRequest);
        Map<String, BigDecimal> priceMap = userPrice.getPriceMap();
        Map<String, List<GoodsIntervalPriceVO>> intervalMap = userPrice.getIntervalMap();

        response.getGoodsInfos().forEach(e -> {
            if(e.getDistributionGoodsAudit()==DistributionGoodsAudit.CHECKED){
                e.setEnterPriseAuditState(EnterpriseAuditState.INIT);
                e.setEnterPriseGoodsAuditReason(null);
                e.setEnterPrisePrice(null);
                return;
            }
            String goodsInfoId = e.getGoodsInfoId();
            BigDecimal price = priceMap.get(goodsInfoId);
            if (price != null) {
                e.setEnterPrisePrice(price);
            }
            List<GoodsIntervalPriceVO> list = intervalMap.get(goodsInfoId);
            if (list != null && !list.isEmpty()) {
                response.getGoodsIntervalPrices().addAll(KsBeanUtil.convert(list, GoodsIntervalPrice.class));
            }
        });
        return response;
    }

    /**
     * @param goodsId 商品ID
     * @return list
     */
    public GoodsEditResponse findInfoByIdNew(String goodsId, String customerId) throws SbcRuntimeException {
        GoodsEditResponse response = new GoodsEditResponse();
        Goods goods = goodsRepository.findById(goodsId).orElse(null);
        if (Objects.isNull(goods)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        goods.setGoodsDetail(null);
        GoodsResponse goodsResponse = new GoodsResponse();
        goodsResponse.setGoods(goods);
        redisService.setString(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId,
                JSONObject.toJSONString(goodsResponse), 6 * 60 * 60);
        response.setGoods(goods);

        //查询商品图片
        response.setImages(goodsImageRepository.findByGoodsId(goods.getGoodsId()));
        //标签
        response.setTags(tagService.findTagByGoods(goods.getGoodsId()));
        //是否书籍
        GoodsCate cate = goodsCateService.findById(goods.getCateId());
        response.setBookFlag(cate.getBookFlag());

        //商品属性
        response.setGoodsPropDetailRels(goodsPropDetailRelRepository.queryByGoodsId(goods.getGoodsId()));
        List<GoodsPropDetailRel> goodsPropDetailRels = goodsPropDetailRelRepository.queryByGoodsId(goods.getGoodsId());
        Map<Long, List<GoodsPropDetailRel>> idRel = goodsPropDetailRels.stream().collect(Collectors.groupingBy(GoodsPropDetailRel::getPropId));
        List<GoodsProp> props = findPropByIds(new ArrayList<>(idRel.keySet()));
        if(CollectionUtils.isNotEmpty(props)){
            for (GoodsProp prop : props) {
                List<GoodsPropDetailRel> goodsPropDetailRelVos = idRel.get(prop.getPropId());
                for (GoodsPropDetailRel goodsPropDetailRelVo : goodsPropDetailRelVos) {
                    goodsPropDetailRelVo.setPropName(prop.getPropName());
                    goodsPropDetailRelVo.setPropType(prop.getPropType());
                }
            }
        }
        Iterator<GoodsPropDetailRel> it = goodsPropDetailRels.iterator();
        Map<String, String> extProps = new HashMap<>();
        while (it.hasNext()) {
            GoodsPropDetailRel rel = it.next();
            if("作者".equals(rel.getPropName())){
                extProps.put("author", rel.getPropValue());
                it.remove();
            }else if("出版社".equals(rel.getPropName())){
                extProps.put("publisher", rel.getPropValue());
                it.remove();
            }else if("定价".equals(rel.getPropName())){
                extProps.put("price", rel.getPropValue());
                it.remove();
            }else if("评分".equals(rel.getPropName())){
                extProps.put("score", rel.getPropValue());
                it.remove();
            }
        }
        response.setExtProps(extProps);
        response.setGoodsPropDetailRels(goodsPropDetailRels);

        List<String> enterpriseGoodsInfoIds = new ArrayList<>();
        Map<String, Long> buyCountMap = new HashMap<>();
        //查询SKU列表
        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setGoodsId(goods.getGoodsId());
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
        Long goodsStock = 0L;
        for (GoodsInfo goodsInfo : goodsInfos) {
            if (goodsInfo.getEnterPriseAuditState() == EnterpriseAuditState.CHECKED) {
                enterpriseGoodsInfoIds.add(goodsInfo.getGoodsInfoId());
                buyCountMap.put(goodsInfo.getGoodsInfoId(), 1L);
            }
            goodsInfo.setSalePrice(Objects.isNull(goodsInfo.getMarketPrice()) ? BigDecimal.ZERO : goodsInfo.getMarketPrice());
            goodsInfo.setPriceType(goods.getPriceType());
            goodsInfo.setBrandId(goods.getBrandId());
            goodsInfo.setCateId(goods.getCateId());
            Long goodsInfoStock = goodsInfoStockService.checkStockCache(goodsInfo.getGoodsInfoId());
            Long actualStock = goodsInfoStock;
            if(goodsInfoStock <= stockThreshold){
                actualStock = 0L;
            }
            goodsInfo.setStock(actualStock);
            goodsStock += actualStock;
        }
        if(goodsStock <= stockThreshold) {
            goodsStock = 0L;
        }
        goods.setStock(goodsStock);
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
            }
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

        //如果是多规格
        if (Constants.yes.equals(goods.getMoreSpecFlag())) {
            response.setGoodsSpecs(goodsSpecRepository.findByGoodsId(goods.getGoodsId()));
            response.setGoodsSpecDetails(goodsSpecDetailRepository.findByGoodsId(goods.getGoodsId()));

            //对每个规格填充规格值关系
            response.getGoodsSpecs().forEach(goodsSpec -> {
                goodsSpec.setSpecDetailIds(response.getGoodsSpecDetails().stream().filter(specDetail -> specDetail.getSpecId().equals(goodsSpec.getSpecId())).map(GoodsSpecDetail::getSpecDetailId).collect(Collectors.toList()));
            });

            //对每个SKU填充规格和规格值关系
            Map<String, List<GoodsInfoSpecDetailRel>> goodsInfoSpecDetailRels = goodsInfoSpecDetailRelRepository.findByGoodsId(goods.getGoodsId()).stream().collect(Collectors.groupingBy(GoodsInfoSpecDetailRel::getGoodsInfoId));
            goodsInfos.forEach(goodsInfo -> {
                goodsInfo.setMockSpecIds(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getSpecId).collect(Collectors.toList()));
                goodsInfo.setMockSpecDetailIds(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getSpecDetailId).collect(Collectors.toList()));
                goodsInfo.setMockSpecDetailName(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getDetailName).collect(Collectors.toList()));
            });
        }
        response.setGoodsInfos(goodsInfos);

        //商品按订货区间，查询订货区间
        if (Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(goods.getPriceType())) {
            response.setGoodsIntervalPrices(goodsIntervalPriceRepository.findByGoodsId(goods.getGoodsId()));
        } else if (Integer.valueOf(GoodsPriceType.CUSTOMER.toValue()).equals(goods.getPriceType())) {
            response.setGoodsLevelPrices(goodsLevelPriceRepository.findByGoodsId(goods.getGoodsId()));
            //如果是按单独客户定价
            if (Constants.yes.equals(goods.getCustomFlag())) {
                response.setGoodsCustomerPrices(goodsCustomerPriceRepository.findByGoodsId(goods.getGoodsId()));
            }
        }

        //按客户等级设价
        this.getGoodsIntervalPrice(response, customerId);
        //企业设价--填充
        EnterprisePriceGetRequest enterprisePriceGetRequest = new EnterprisePriceGetRequest();
        enterprisePriceGetRequest.setGoodsInfoIds(enterpriseGoodsInfoIds);
        enterprisePriceGetRequest.setCustomerId(customerId);
        enterprisePriceGetRequest.setOrderFlag(false);
        enterprisePriceGetRequest.setListFlag(false);
        enterprisePriceGetRequest.setBuyCountMap(buyCountMap);
        EnterprisePriceResponse userPrice = goodsInfoService.getUserPrice(enterprisePriceGetRequest);
        Map<String, BigDecimal> priceMap = userPrice.getPriceMap();
        Map<String, List<GoodsIntervalPriceVO>> intervalMap = userPrice.getIntervalMap();

        response.getGoodsInfos().forEach(e -> {
            if(e.getDistributionGoodsAudit()==DistributionGoodsAudit.CHECKED){
                e.setEnterPriseAuditState(EnterpriseAuditState.INIT);
                e.setEnterPriseGoodsAuditReason(null);
                e.setEnterPrisePrice(null);
                return;
            }
            String goodsInfoId = e.getGoodsInfoId();
            BigDecimal price = priceMap.get(goodsInfoId);
            if (price != null) {
                e.setEnterPrisePrice(price);
            }
            List<GoodsIntervalPriceVO> list = intervalMap.get(goodsInfoId);
            if (list != null && !list.isEmpty()) {
                response.getGoodsIntervalPrices().addAll(KsBeanUtil.convert(list, GoodsIntervalPrice.class));
            }
        });
        return response;
    }


    /**
     * @param
     * @return
     * @discription 按客户等级设价
     * @author yangzhen
     * @date 2020/9/11 15:32
     */
    private void getGoodsIntervalPrice(GoodsEditResponse response, String customerId) {
        Goods goods = response.getGoods();
        //提取店铺
        Map<Long, CommonLevelVO> levelMap = null;
        if (StringUtils.isNotBlank(customerId)) {
            if (Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(goods.getPriceType())) {
                levelMap = goodsIntervalPriceService.getLevelMap(customerId, Collections.singletonList(goods.getStoreId()));
            }
        }
        List<GoodsIntervalPrice> goodsIntervalPriceList = goodsIntervalPriceService.putIntervalPrice(response.getGoodsInfos(), levelMap);
        goodsIntervalPriceList.addAll(goodsIntervalPriceService.putGoodsIntervalPrice(Collections.singletonList(goods), levelMap));
        response.setGoods(goods);
        response.setGoodsIntervalPrices(goodsIntervalPriceList);

    }


    /**
     * 查询商品信息
     *
     * @param goodsId 商品ID
     * @return list
     */
    public GoodsResponse findGoodsSimple(String goodsId) throws SbcRuntimeException {
        GoodsResponse response = new GoodsResponse();
        Goods goods = goodsRepository.findById(goodsId).orElse(null);
        if (Objects.isNull(goods)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        goods.setGoodsDetail(null);
        response.setGoods(goods);
        return response;
    }


    /**
     * 根据ID查询商品
     *
     * @param goodsId 商品ID
     * @return list
     */
    public GoodsEditResponse findInfoById(String goodsId) throws SbcRuntimeException {
        GoodsEditResponse response = new GoodsEditResponse();
        Goods goods = goodsRepository.findById(goodsId).orElse(null);
        if (Objects.isNull(goods)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        if (Objects.nonNull(goods.getFreightTempId())) {
            FreightTemplateGoods goodsTemplate = freightTemplateGoodsRepository.queryById(goods.getFreightTempId());
            if (Objects.nonNull(goodsTemplate)) {
                goods.setFreightTempName(goodsTemplate.getFreightTempName());
            }
        }
        response.setGoods(goods);

        // 获取商家所绑定的模板列表
        List<StoreGoodsTab> storeGoodsTabs = storeGoodsTabRepository.queryTabByStoreId(goods.getStoreId());
        response.setStoreGoodsTabs(storeGoodsTabs);
        if (CollectionUtils.isNotEmpty(storeGoodsTabs)) {
            storeGoodsTabs.sort(Comparator.comparingInt(StoreGoodsTab::getSort));
            List<GoodsTabRela> goodsTabRelas = goodsTabRelaRepository.queryListByTabIds(goodsId, storeGoodsTabs.stream().map(StoreGoodsTab::getTabId).collect(Collectors.toList()));
            response.setGoodsTabRelas(goodsTabRelas);
        }

        //查询商品图片
        response.setImages(goodsImageRepository.findByGoodsId(goods.getGoodsId()));
        response.setTags(tagService.findTagByGoods(goodsId));

        //查询SKU列表
        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setGoodsId(goods.getGoodsId());
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());

        //查询卡券所有的名称进行拼接
        if (goods.getGoodsType() == GoodsType.VIRTUAL_COUPON.toValue()) {
            List<Long> idList = goodsInfos.stream().map(GoodsInfo::getVirtualCouponId).collect(Collectors.toList());
            Map<Long, String> couponMapName = virtualCouponService.list(VirtualCouponQueryRequest.builder().idList(idList).build())
                    .stream().collect(Collectors.toMap(VirtualCoupon::getId, VirtualCoupon::getName));
            goodsInfos.forEach(goodsInfo -> {
                goodsInfo.setVirtualCouponName(couponMapName.get(goodsInfo.getVirtualCouponId()));
            });
        }

        goodsInfos.forEach(goodsInfo -> {
            goodsInfo.setSalePrice(Objects.isNull(goodsInfo.getMarketPrice()) ? BigDecimal.ZERO : goodsInfo.getMarketPrice());
            goodsInfo.setPriceType(goods.getPriceType());
            goodsInfo.setBrandId(goods.getBrandId());
            goodsInfo.setCateId(goods.getCateId());
            goodsInfo.setStock(goodsInfoStockService.checkStockCache(goodsInfo.getGoodsInfoId()));
            updateGoodsInfoSupplyPriceAndStock(goodsInfo);
            goodsInfo.setVendibility(goodsInfoService.buildGoodsInfoVendibility(goodsInfo));
        });
        //如果是linkedmall商品，实时查库存
        if (ThirdPlatformType.LINKED_MALL.equals(goods.getThirdPlatformType()) || Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(goods.getGoodsSource())) {
            List<QueryItemInventoryResponse.Item> stockList = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(Collections.singletonList(Long.valueOf(goods.getThirdPlatformSpuId())), "0", null)).getContext();
            if (stockList.size() > 0) {
                QueryItemInventoryResponse.Item stock = stockList.get(0);
                for (GoodsInfo goodsInfo : goodsInfos) {
                    for (QueryItemInventoryResponse.Item.Sku sku : stock.getSkuList()) {
                        if (stock.getItemId().equals(Long.valueOf(goodsInfo.getThirdPlatformSpuId())) && sku.getSkuId().equals(Long.valueOf(goodsInfo.getThirdPlatformSkuId()))) {
                            Long skuStock = sku.getInventory().getQuantity();
                            goodsInfo.setStock(skuStock);
                            if (!GoodsStatus.INVALID.equals(goodsInfo.getGoodsStatus())) {
                                goodsInfo.setGoodsStatus(skuStock > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                            }
                        }
                    }
                }
                goods.setStock(stock.getSkuList().stream()
                        .map(v -> v.getInventory().getQuantity())
                        .reduce(0L, (aLong, aLong2) -> aLong + aLong2));
            }
        }
        //商品属性
        List<GoodsPropDetailRel> goodsPropDetailRels = goodsPropDetailRelRepository.queryByGoodsId(goods.getGoodsId());
        Map<Long, List<GoodsPropDetailRel>> idRel = goodsPropDetailRels.stream().collect(Collectors.groupingBy(GoodsPropDetailRel::getPropId));
        List<GoodsProp> props = findPropByIds(new ArrayList<>(idRel.keySet()));
        if(CollectionUtils.isNotEmpty(props)){
            for (GoodsProp prop : props) {
                List<GoodsPropDetailRel> goodsPropDetailRelVos = idRel.get(prop.getPropId());
                for (GoodsPropDetailRel goodsPropDetailRelVo : goodsPropDetailRelVos) {
                    goodsPropDetailRelVo.setPropName(prop.getPropName());
                    goodsPropDetailRelVo.setPropType(prop.getPropType());
                }
            }
        }
        response.setGoodsPropDetailRels(goodsPropDetailRels);

        //如果是多规格
        if (Constants.yes.equals(goods.getMoreSpecFlag())) {
            response.setGoodsSpecs(goodsSpecRepository.findByGoodsId(goods.getGoodsId()));
            response.setGoodsSpecDetails(goodsSpecDetailRepository.findByGoodsId(goods.getGoodsId()));

            //对每个规格填充规格值关系
            response.getGoodsSpecs().forEach(goodsSpec -> {
                goodsSpec.setSpecDetailIds(response.getGoodsSpecDetails().stream().filter(specDetail -> specDetail.getSpecId().equals(goodsSpec.getSpecId())).map(GoodsSpecDetail::getSpecDetailId).collect(Collectors.toList()));
            });

            //对每个SKU填充规格和规格值关系
            Map<String, List<GoodsInfoSpecDetailRel>> goodsInfoSpecDetailRels = goodsInfoSpecDetailRelRepository.findByGoodsId(goods.getGoodsId()).stream().collect(Collectors.groupingBy(GoodsInfoSpecDetailRel::getGoodsInfoId));
            goodsInfos.forEach(goodsInfo -> {
                goodsInfo.setMockSpecIds(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getSpecId).collect(Collectors.toList()));
                goodsInfo.setMockSpecDetailIds(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getSpecDetailId).collect(Collectors.toList()));
                goodsInfo.setMockSpecDetailName(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getDetailName).collect(Collectors.toList()));
            });
        }
        response.setGoodsInfos(goodsInfos);

        //商品按订货区间，查询订货区间
        if (Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(goods.getPriceType())) {
            response.setGoodsIntervalPrices(goodsIntervalPriceRepository.findByGoodsId(goods.getGoodsId()));
        } else if (Integer.valueOf(GoodsPriceType.CUSTOMER.toValue()).equals(goods.getPriceType())) {
            response.setGoodsLevelPrices(goodsLevelPriceRepository.findByGoodsId(goods.getGoodsId()));
            //如果是按单独客户定价
            if (Constants.yes.equals(goods.getCustomFlag())) {
                response.setGoodsCustomerPrices(goodsCustomerPriceRepository.findByGoodsId(goods.getGoodsId()));
            }
        }
        return response;
    }

    private void updateGoodsInfoSupplyPriceAndStock(GoodsInfo goodsInfo) {
        //供应商库存
        if (StringUtils.isNotBlank(goodsInfo.getProviderGoodsInfoId()) && goodsInfo.getThirdPlatformType() == null) {
            GoodsInfo providerGoodsInfo = goodsInfoRepository.findById(goodsInfo.getProviderGoodsInfoId()).orElse(null);
            if (providerGoodsInfo != null) {
                goodsInfo.setStock(providerGoodsInfo.getStock());
                goodsInfo.setSupplyPrice(providerGoodsInfo.getSupplyPrice());
            }
        }
    }


    /**
     * @param
     * @return
     * @discription 查询商品属性和图文信息
     * @author yangzhen
     * @date 2020/9/3 11:17
     */
    public GoodsDetailResponse findGoodsDetail(String skuId, String spuId) {
        String goodsId = spuId;
        goodsId = WebConstantUtil.filterSpecialCharacter(goodsId);
        if (StringUtils.isEmpty(goodsId)) {
            List<GoodsInfo> goodsInfo = goodsInfoRepository.findByGoodsInfoIds(Arrays.asList(skuId));
            if (CollectionUtils.isNotEmpty(goodsInfo)) {
                goodsId = goodsInfo.get(0).getGoodsId();
            }
        }
        if(StringUtils.isEmpty(goodsId)){
            return new GoodsDetailResponse();
        }
        GoodsDetailResponse response = new GoodsDetailResponse();
        String goodsDetail = goodsRepository.getGoodsDetail(goodsId);
        response.setGoodsDetail(goodsDetail);
        response.setGoodsPropDetailRels(goodsPropDetailRelRepository.queryByGoodsId(goodsId));
        return response;
    }
    /**
     * @param
     * @return
     * @discription 根据sku 查询商品详情
     * @author yangzhen
     * @date 2020/9/3 11:17
     */
    public GoodsVO findGoodsInfoDetail(String skuId) {
        List<GoodsInfo> goodsInfo = goodsInfoRepository.findByGoodsInfoIds(Arrays.asList(skuId));
        GoodsDetailResponse response = new GoodsDetailResponse();
        if (CollectionUtils.isNotEmpty(goodsInfo)) {
            Goods goods = goodsRepository.getOne(goodsInfo.get(0).getGoodsId());
            GoodsVO goodsVO = KsBeanUtil.convert(goods, GoodsVO.class);
            return  goodsVO;
        }
        return null;
    }

    /**
     * @param
     * @return
     * @discription 运费模板
     * @author yangzhen
     * @date 2020/9/3 14:17
     */
    public void getFreightTempId(Goods goods, GoodsEditResponse response) {
        if (Objects.nonNull(goods.getFreightTempId())) {
            FreightTemplateGoods goodsTemplate = freightTemplateGoodsRepository.queryById(goods.getFreightTempId());
            if (Objects.nonNull(goodsTemplate)) {
                goods.setFreightTempName(goodsTemplate.getFreightTempName());
            }
        }
        response.setGoods(goods);
    }

    /**
     * @param
     * @return
     * @discription 获取商家所绑定的模板列表
     * @author yangzhen
     * @date 2020/9/3 14:17
     */
    public void getStoreGoodsTabs(Goods goods, GoodsEditResponse response) {
        List<StoreGoodsTab> storeGoodsTabs = storeGoodsTabRepository.queryTabByStoreId(goods.getStoreId());
        response.setStoreGoodsTabs(storeGoodsTabs);
        if (CollectionUtils.isNotEmpty(storeGoodsTabs)) {
            storeGoodsTabs.sort(Comparator.comparingInt(StoreGoodsTab::getSort));
            List<GoodsTabRela> goodsTabRelas = goodsTabRelaRepository.queryListByTabIds(goods.getGoodsId(),
                    storeGoodsTabs.stream().map(StoreGoodsTab::getTabId).collect(Collectors.toList()));
            response.setGoodsTabRelas(goodsTabRelas);
        }
    }

    /**
     * @param
     * @return
     * @discription 商品图片
     * @author yangzhen
     * @date 2020/9/3 14:18
     */
    public void getImages(Goods goods, GoodsEditResponse response) {
        //查询商品图片
        response.setImages(goodsImageRepository.findByGoodsId(goods.getGoodsId()));
    }

    /**
     * @param
     * @return
     * @discription 商品属性
     * @author yangzhen
     * @date 2020/9/3 14:18
     */
    public void getGoodsPropDetailRels(Goods goods, GoodsEditResponse response) {
        //商品属性
        response.setGoodsPropDetailRels(goodsPropDetailRelRepository.queryByGoodsId(goods.getGoodsId()));
    }

    /**
     * @param
     * @return
     * @discription sku列表
     * @author yangzhen
     * @date 2020/9/3 14:18
     */
    public void getSku(Goods goods, GoodsEditResponse response) {
        //商品属性
        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setGoodsId(goods.getGoodsId());
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
        goodsInfos.forEach(goodsInfo -> {
            goodsInfo.setSalePrice(Objects.isNull(goodsInfo.getMarketPrice()) ? BigDecimal.ZERO : goodsInfo.getMarketPrice());
            goodsInfo.setPriceType(goods.getPriceType());
            goodsInfo.setBrandId(goods.getBrandId());
            goodsInfo.setCateId(goods.getCateId());
            goodsInfo.setStock(goodsInfoStockService.checkStockCache(goodsInfo.getGoodsInfoId()));
        });

        //如果是多规格
        if (Constants.yes.equals(goods.getMoreSpecFlag())) {
            response.setGoodsSpecs(goodsSpecRepository.findByGoodsId(goods.getGoodsId()));
            response.setGoodsSpecDetails(goodsSpecDetailRepository.findByGoodsId(goods.getGoodsId()));

            //对每个规格填充规格值关系
            response.getGoodsSpecs().forEach(goodsSpec -> {
                goodsSpec.setSpecDetailIds(response.getGoodsSpecDetails().stream().filter(specDetail -> specDetail.getSpecId().equals(goodsSpec.getSpecId())).map(GoodsSpecDetail::getSpecDetailId).collect(Collectors.toList()));
            });

            //对每个SKU填充规格和规格值关系
            Map<String, List<GoodsInfoSpecDetailRel>> goodsInfoSpecDetailRels = goodsInfoSpecDetailRelRepository.findByGoodsId(goods.getGoodsId()).stream().collect(Collectors.groupingBy(GoodsInfoSpecDetailRel::getGoodsInfoId));
            goodsInfos.forEach(goodsInfo -> {
                goodsInfo.setMockSpecIds(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getSpecId).collect(Collectors.toList()));
                goodsInfo.setMockSpecDetailIds(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getSpecDetailId).collect(Collectors.toList()));
            });
        }
        response.setGoodsInfos(goodsInfos);
    }

    /**
     * @param
     * @return
     * @discription 商品按订货区间，查询订货区间
     * @author yangzhen
     * @date 2020/9/3 14:19
     */
    public void getPrice(Goods goods, GoodsEditResponse response) {
        if (Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(goods.getPriceType())) {
            response.setGoodsIntervalPrices(goodsIntervalPriceRepository.findByGoodsId(goods.getGoodsId()));
        } else if (Integer.valueOf(GoodsPriceType.CUSTOMER.toValue()).equals(goods.getPriceType())) {
            response.setGoodsLevelPrices(goodsLevelPriceRepository.findByGoodsId(goods.getGoodsId()));
            //如果是按单独客户定价
            if (Constants.yes.equals(goods.getCustomFlag())) {
                response.setGoodsCustomerPrices(goodsCustomerPriceRepository.findByGoodsId(goods.getGoodsId()));
            }
        }
    }


    /**
     * 根据积分商品ID查询商品
     *
     * @param pointsGoodsId 积分商品ID
     * @return list
     */
    public GoodsEditResponse findInfoByPointsGoodsId(String pointsGoodsId) throws SbcRuntimeException {
        GoodsEditResponse response = new GoodsEditResponse();
        //查看积分商品信息
        PointsGoods pointsGoods = pointsGoodsRepository.findById(pointsGoodsId).orElse(new PointsGoods());
        Goods goods = goodsRepository.findById(pointsGoods.getGoodsId()).orElse(null);
        if (Objects.isNull(goods)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        if (Objects.nonNull(goods.getFreightTempId())) {
            FreightTemplateGoods goodsTemplate = freightTemplateGoodsRepository.queryById(goods.getFreightTempId());
            if (Objects.nonNull(goodsTemplate)) {
                goods.setFreightTempName(goodsTemplate.getFreightTempName());
            }
        }
        response.setGoods(goods);

        // 获取商家所绑定的模板列表
        List<StoreGoodsTab> storeGoodsTabs = storeGoodsTabRepository.queryTabByStoreId(goods.getStoreId());
        response.setStoreGoodsTabs(storeGoodsTabs);
        if (CollectionUtils.isNotEmpty(storeGoodsTabs)) {
            storeGoodsTabs.sort(Comparator.comparingInt(StoreGoodsTab::getSort));
            List<GoodsTabRela> goodsTabRelas = goodsTabRelaRepository.queryListByTabIds(pointsGoods.getGoodsId(), storeGoodsTabs.stream().map(StoreGoodsTab::getTabId).collect(Collectors.toList()));
            response.setGoodsTabRelas(goodsTabRelas);
        }

        //查询商品图片
        response.setImages(goodsImageRepository.findByGoodsId(goods.getGoodsId()));

        //查询SKU列表
        //验证积分商品(校验积分商品库存，删除，启用停用状态，兑换时间)
        if (Objects.isNull(pointsGoods)
                || Objects.equals(DeleteFlag.YES, pointsGoods.getDelFlag())
                || (!Objects.equals(EnableStatus.ENABLE, pointsGoods.getStatus()))) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        //去积分商品表里查 当前积分商品下其他的可用积分商品（未删除、已启用、在当前兑换时间、库存不为0）
        PointsGoodsQueryRequest queryReq = new PointsGoodsQueryRequest();
        queryReq.setGoodsId(pointsGoods.getGoodsId());
        queryReq.setDelFlag(DeleteFlag.NO);
        queryReq.setStatus(EnableStatus.ENABLE);
        queryReq.setBeginTimeEnd(LocalDateTime.now());
        queryReq.setEndTimeBegin(LocalDateTime.now());
        List<PointsGoods> pointsGoodsList = pointsGoodsRepository.findAll(PointsGoodsWhereCriteriaBuilder.build(queryReq));
        //如果是linkedmall商品，实时查库存
        List<String> skuIds = pointsGoodsList.stream().map(v -> v.getGoodsInfoId()).collect(Collectors.toList());
        List<GoodsInfo> linkedMallGoodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsInfoIds(skuIds).build())
                .stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .collect(Collectors.toList());
        if (linkedMallGoodsInfos != null && linkedMallGoodsInfos.size() > 0) {
            List<Long> itemIds = linkedMallGoodsInfos.stream().map(v -> Long.valueOf(v.getThirdPlatformSpuId())).distinct().collect(Collectors.toList());
            List<QueryItemInventoryResponse.Item> stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
            if (stocks != null) {
                for (GoodsInfo goodsInfo : linkedMallGoodsInfos) {
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
                for (PointsGoods wrapSotckgoods : pointsGoodsList) {
                    Optional<GoodsInfo> goodsInfo = linkedMallGoodsInfos.stream().filter(v -> v.getGoodsInfoId().equals(wrapSotckgoods.getGoodsInfoId())).findFirst();
                    if (goodsInfo.isPresent()) {
//                        wrapSotckgoods.getGoodsInfo().setStock(goodsInfo.get().getStock());
                        Long quantity = goodsInfo.get().getStock();
                        wrapSotckgoods.getGoodsInfo().setStock(quantity);
                        if (!GoodsStatus.INVALID.equals(wrapSotckgoods.getGoodsInfo().getGoodsStatus())) {
                            wrapSotckgoods.getGoodsInfo().setGoodsStatus(quantity > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                        }
                    }
                }
            }
        }
        pointsGoodsList = pointsGoodsList.stream().filter(pointsGoodsVO -> pointsGoodsVO.getStock() > 0).collect(Collectors.toList());

        //查询积分商品对应的商品sku信息
        List<String> skuIdList = pointsGoodsList.stream().map(PointsGoods::getGoodsInfoId).collect(Collectors.toList());
        List<GoodsInfo> goodsInfoList = goodsInfoRepository.findByGoodsInfoIds(skuIdList);
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = goodsInfoList.stream().filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType())).map(v -> Long.valueOf(v.getThirdPlatformSpuId())).distinct().collect(Collectors.toList());
        if (itemIds != null && itemIds.size() > 0) {
            List<QueryItemInventoryResponse.Item> stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
            if (stocks != null) {
                for (GoodsInfo goodsInfo : goodsInfoList) {
                    if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {
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
            }
        }
        goodsInfoService.updateGoodsInfoSupplyPriceAndStock(goodsInfoList);
        //商品属性
        response.setGoodsPropDetailRels(goodsPropDetailRelRepository.queryByGoodsId(goods.getGoodsId()));

        //如果是多规格
        if (Constants.yes.equals(goods.getMoreSpecFlag())) {
            response.setGoodsSpecs(goodsSpecRepository.findByGoodsId(goods.getGoodsId()));
            response.setGoodsSpecDetails(goodsSpecDetailRepository.findByGoodsId(goods.getGoodsId()));

            //对每个规格填充规格值关系
            response.getGoodsSpecs().forEach(goodsSpec -> {
                goodsSpec.setSpecDetailIds(response.getGoodsSpecDetails().stream().filter(specDetail -> specDetail.getSpecId().equals(goodsSpec.getSpecId())).map(GoodsSpecDetail::getSpecDetailId).collect(Collectors.toList()));
            });

            //对每个SKU填充规格和规格值关系
            Map<String, List<GoodsInfoSpecDetailRel>> goodsInfoSpecDetailRels = goodsInfoSpecDetailRelRepository.findAllByGoodsId(goods.getGoodsId()).stream().collect(Collectors.groupingBy(GoodsInfoSpecDetailRel::getGoodsInfoId));
            goodsInfoList.forEach(goodsInfo -> {
                goodsInfo.setMockSpecIds(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getSpecId).collect(Collectors.toList()));
                goodsInfo.setMockSpecDetailIds(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getSpecDetailId).collect(Collectors.toList()));
            });
        }
        pointsGoodsList.forEach(v -> {
            GoodsInfo goodsInfo = goodsInfoList.stream().filter(o -> v.getGoodsInfoId().equals(o.getGoodsInfoId()))
                    .findFirst().orElseGet(null);
            if (Objects.nonNull(goodsInfo) && v.getStock() > goodsInfo.getStock()) {
                v.setStock(goodsInfo.getStock());
            }
        });


        response.setGoodsInfos(goodsInfoList);
        response.setPointsGoodsList(pointsGoodsList);
        return response;
    }

    /**
     * 店铺精选页-进入商品详情接口
     *
     * @param goodsId
     * @param skuIds
     * @return
     * @throws SbcRuntimeException
     */
    public GoodsEditResponse findInfoByIdAndSkuIds(String goodsId, List<String> skuIds) throws SbcRuntimeException {
        GoodsEditResponse response = new GoodsEditResponse();
        Goods goods = goodsRepository.findById(goodsId).orElse(null);
        if (Objects.isNull(goods)) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        if (Objects.nonNull(goods.getFreightTempId())) {
            FreightTemplateGoods goodsTemplate = freightTemplateGoodsRepository.queryById(goods.getFreightTempId());
            if (Objects.nonNull(goodsTemplate)) {
                goods.setFreightTempName(goodsTemplate.getFreightTempName());
            }
        }
        response.setGoods(goods);

        // 获取商家所绑定的模板列表
        List<StoreGoodsTab> storeGoodsTabs = storeGoodsTabRepository.queryTabByStoreId(goods.getStoreId());
        response.setStoreGoodsTabs(storeGoodsTabs);
        if (CollectionUtils.isNotEmpty(storeGoodsTabs)) {
            storeGoodsTabs.sort(Comparator.comparingInt(StoreGoodsTab::getSort));
            List<GoodsTabRela> goodsTabRelas = goodsTabRelaRepository.queryListByTabIds(goodsId, storeGoodsTabs.stream().map(StoreGoodsTab::getTabId).collect(Collectors.toList()));
            response.setGoodsTabRelas(goodsTabRelas);
        }

        //查询商品图片
        response.setImages(goodsImageRepository.findByGoodsId(goods.getGoodsId()));

        //查询SKU列表
        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setGoodsId(goods.getGoodsId());
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
        List<GoodsInfo> goodsInfoList = new ArrayList<>(goodsInfos.size());
        for (GoodsInfo goodsInfo : goodsInfos) {
            if (skuIds.stream().anyMatch(skuId -> goodsInfo.getGoodsInfoId().equals(skuId)) && DistributionGoodsAudit.CHECKED.equals(goodsInfo.getDistributionGoodsAudit())) {
                goodsInfoList.add(goodsInfo);
            }
        }
        goodsInfoList.forEach(goodsInfo -> {
            goodsInfo.setSalePrice(Objects.isNull(goodsInfo.getMarketPrice()) ? BigDecimal.ZERO : goodsInfo.getMarketPrice());
            goodsInfo.setPriceType(goods.getPriceType());
        });

        //商品属性
        response.setGoodsPropDetailRels(goodsPropDetailRelRepository.queryByGoodsId(goods.getGoodsId()));

        //如果是多规格
        checkMoreSpecFlag(goods, response, goodsInfoList);

        response.setGoodsInfos(goodsInfoList);
        //如果是linkedmall商品，实时查库存
        if (ThirdPlatformType.LINKED_MALL.equals(goods.getThirdPlatformType())) {
            QueryItemInventoryResponse.Item stock =
                    linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(Collections.singletonList(Long.valueOf(goods.getThirdPlatformSpuId())), "0", null)).getContext().get(0);
            if (stock != null) {
                Long totalStock = stock.getSkuList().stream()
                        .map(v -> v.getInventory().getQuantity())
                        .reduce(0L, ((aLong, aLong2) -> aLong + aLong2));
                goods.setStock(totalStock);
                for (GoodsInfo goodsInfo : goodsInfoList) {
                    Optional<QueryItemInventoryResponse.Item.Sku> skuStock = stock.getSkuList().stream()
                            .filter(v -> String.valueOf(stock.getItemId()).equals(goodsInfo.getThirdPlatformSpuId()) && String.valueOf(v.getSkuId()).equals(goodsInfo.getThirdPlatformSkuId()))
                            .findFirst();
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
        return response;
    }

    private void checkMoreSpecFlag(Goods goods, GoodsEditResponse response, List<GoodsInfo> goodsInfoList) {
        if (Constants.yes.equals(goods.getMoreSpecFlag())) {
            response.setGoodsSpecs(goodsSpecRepository.findByGoodsId(goods.getGoodsId()));
            response.setGoodsSpecDetails(goodsSpecDetailRepository.findByGoodsId(goods.getGoodsId()));

            //对每个规格填充规格值关系
            response.getGoodsSpecs().forEach(goodsSpec -> {
                goodsSpec.setSpecDetailIds(response.getGoodsSpecDetails().stream().filter(specDetail -> specDetail.getSpecId().equals(goodsSpec.getSpecId())).map(GoodsSpecDetail::getSpecDetailId).collect(Collectors.toList()));
            });

            //对每个SKU填充规格和规格值关系
            Map<String, List<GoodsInfoSpecDetailRel>> goodsInfoSpecDetailRels = goodsInfoSpecDetailRelRepository.findByGoodsId(goods.getGoodsId()).stream().collect(Collectors.groupingBy(GoodsInfoSpecDetailRel::getGoodsInfoId));
            goodsInfoList.forEach(goodsInfo -> {
                goodsInfo.setMockSpecIds(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getSpecId).collect(Collectors.toList()));
                goodsInfo.setMockSpecDetailIds(goodsInfoSpecDetailRels.getOrDefault(goodsInfo.getGoodsInfoId(), new ArrayList<>()).stream().map(GoodsInfoSpecDetailRel::getSpecDetailId).collect(Collectors.toList()));
            });
        }
    }

    /**
     * 商品新增
     *
     * @param saveRequest
     * @return SPU编号
     * @throws SbcRuntimeException
     */
    @Transactional
    public String add(GoodsSaveRequest saveRequest) throws SbcRuntimeException {
        List<GoodsImage> goodsImages = saveRequest.getImages();
        List<GoodsInfo> goodsInfos = saveRequest.getGoodsInfos();
        Goods goods = saveRequest.getGoods();

        //判断sku编码是否重复，当组合商品时不sku是非必填的
        goodsInfos.forEach(goodsInfo -> {

            if (StringUtils.isBlank(goodsInfo.getErpGoodsNo())) {
                throw new SbcRuntimeException(GoodsErrorCode.SPU_REQUIRED);
            }

            if (Objects.equals(goodsInfo.getGoodsType(),GoodsType.CYCLE_BUY.toValue()) || Objects.equals(goodsInfo.getGoodsType(),GoodsType.VIRTUAL_COUPON.toValue()) || Objects.equals(goodsInfo.getGoodsType(),GoodsType.VIRTUAL_GOODS.toValue())) {
                if (StringUtils.isBlank(goodsInfo.getErpGoodsInfoNo())) {
                    throw new SbcRuntimeException(GoodsErrorCode.SKU_REQUIRED);
                }
            }else {
                if (!goodsInfo.getCombinedCommodity() && StringUtils.isBlank(goodsInfo.getErpGoodsInfoNo())) {
                    throw new SbcRuntimeException(GoodsErrorCode.SKU_REQUIRED);
                }
            }
        });

        //判断sku编码是否重复
        Map<String, List<GoodsInfo>> erpSpuMap = goodsInfos.stream().filter(goodsInfo -> StringUtils.isNotBlank(goodsInfo.getErpGoodsInfoNo())).collect(Collectors.groupingBy(GoodsInfo::getErpGoodsNo));
        erpSpuMap.forEach((key, value) -> {
            long count =value.stream().map(GoodsInfo::getErpGoodsInfoNo).distinct().count();
            boolean isRepeat = count < value.size();
            if (isRepeat){
                throw new SbcRuntimeException(GoodsErrorCode.ERP_SKU_NO_EXIST);
            }
        });

        //验证SPU编码重复
        GoodsQueryRequest queryRequest = new GoodsQueryRequest();
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setGoodsNo(goods.getGoodsNo());
        if (goodsRepository.count(queryRequest.getWhereCriteria()) > 0) {
            throw new SbcRuntimeException(GoodsErrorCode.SPU_NO_EXIST);
        }

        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        infoQueryRequest.setGoodsInfoNos(goodsInfos.stream().map(GoodsInfo::getGoodsInfoNo).distinct().collect(Collectors.toList()));

        //如果SKU数据有重复
        if (goodsInfos.size() != infoQueryRequest.getGoodsInfoNos().size()) {
            throw new SbcRuntimeException(GoodsErrorCode.SKU_NO_EXIST);
        }

          //验证SKU编码重复
        List<GoodsInfo> goodsInfosList = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
        if (CollectionUtils.isNotEmpty(goodsInfosList)) {
            throw new SbcRuntimeException(GoodsErrorCode.SKU_NO_H_EXIST, new Object[]{StringUtils.join(goodsInfosList.stream().map(GoodsInfo::getGoodsInfoNo).collect(Collectors.toList()), ",")});
        }
        //验证商品相关基础数据
        this.checkBasic(goods);

        goods.setDelFlag(DeleteFlag.NO);
        goods.setCreateTime(LocalDateTime.now());
        goods.setUpdateTime(goods.getCreateTime());
        goods.setAddedTime(goods.getCreateTime());
        goodsCommonService.setCheckState(goods);

        if (Objects.isNull(goods.getPriceType())) {
            goods.setPriceType(GoodsPriceType.MARKET.toValue());
        }
        if (goods.getAddedFlag() == null) {
            goods.setAddedFlag(AddedFlag.YES.toValue());
        }
        if (CollectionUtils.isNotEmpty(goodsImages)) {
            goods.setGoodsImg(goodsImages.get(0).getArtworkUrl());
        }
        if (goods.getMoreSpecFlag() == null) {
            goods.setMoreSpecFlag(Constants.no);
        }
        if (goods.getCustomFlag() == null) {
            goods.setCustomFlag(Constants.no);
        }
        if (goods.getLevelDiscountFlag() == null) {
            goods.setLevelDiscountFlag(Constants.no);
        }
        if (goods.getGoodsCollectNum() == null) {
            goods.setGoodsCollectNum(0L);
        }
        if (goods.getGoodsSalesNum() == null) {
            goods.setGoodsSalesNum(0L);
        }
        if (goods.getGoodsEvaluateNum() == null) {
            goods.setGoodsEvaluateNum(0L);
        }
        if (goods.getGoodsFavorableCommentNum() == null) {
            goods.setGoodsFavorableCommentNum(0L);
        }

        goods.setShamSalesNum(0L);
        goods.setSortNo(0L);
        goods.setSingleSpecFlag(true);
        goods.setStock(goodsInfos.stream().filter(s -> Objects.nonNull(s.getStock())).mapToLong(GoodsInfo::getStock).sum());
        // 兼容云掌柜, 如果没有定时上下架功能, 则默认设置为false
        if (Objects.isNull(goods.getAddedTimingFlag())) {
            goods.setAddedTimingFlag(Boolean.FALSE);
        }
        //如果勾选的定时上架时间
        if (Boolean.TRUE.equals(goods.getAddedTimingFlag()) && goods.getAddedTimingTime() != null) {
            if (goods.getAddedTimingTime().compareTo(LocalDateTime.now()) > 0) {
                goods.setAddedFlag(AddedFlag.NO.toValue());
            } else {
                goods.setAddedFlag(AddedFlag.YES.toValue());
            }
        }
        //goods.setAddedTimingFlag(false);
        //同步库存和市场价 duanlsh TODO
        final String goodsId = goodsRepository.save(goods).getGoodsId();

        //新增图片
        if (CollectionUtils.isNotEmpty(goodsImages)) {
            goodsImages.forEach(goodsImage -> {
                goodsImage.setCreateTime(goods.getCreateTime());
                goodsImage.setUpdateTime(goods.getUpdateTime());
                goodsImage.setGoodsId(goodsId);
                goodsImage.setDelFlag(DeleteFlag.NO);
                goodsImage.setImageId(goodsImageRepository.save(goodsImage).getImageId());
            });
        }

        //标签
        tagService.saveAndUpdateTagForGoods(goodsId, saveRequest.getTags());

        //店铺分类
        if (osUtil.isS2b() && CollectionUtils.isNotEmpty(goods.getStoreCateIds())) {
            List<ClassifyGoodsRelDTO> rels = new ArrayList<>();
            goods.getStoreCateIds().forEach(cateId -> {
                Date now = new Date();
                ClassifyGoodsRelDTO classifyGoodsRelDTO = new ClassifyGoodsRelDTO();
                classifyGoodsRelDTO.setClassifyId(cateId.intValue());
                classifyGoodsRelDTO.setDelFlag(0);
                classifyGoodsRelDTO.setGoodsId(goodsId);
                classifyGoodsRelDTO.setCreateTime(now);
                classifyGoodsRelDTO.setUpdateTime(now);
                rels.add(classifyGoodsRelDTO);
            });
            classifyGoodsRelRepository.saveAll(rels);
        }

        //保存商品属性
        List<GoodsPropDetailRel> goodsPropDetailRels = saveRequest.getGoodsPropDetailRels();
        if (CollectionUtils.isNotEmpty(goodsPropDetailRels)) {
            //如果是修改则设置修改时间，如果是新增则设置创建时间
            goodsPropDetailRels.forEach(goodsPropDetailRel -> {
                goodsPropDetailRel.setDelFlag(DeleteFlag.NO);
                goodsPropDetailRel.setCreateTime(LocalDateTime.now());
                goodsPropDetailRel.setGoodsId(goodsId);
            });
            goodsPropDetailRelRepository.saveAll(goodsPropDetailRels);
        }

        List<GoodsSpec> specs = saveRequest.getGoodsSpecs();
        List<GoodsSpecDetail> specDetails = saveRequest.getGoodsSpecDetails();

        List<GoodsInfoSpecDetailRel> specDetailRels = new ArrayList<>();
        List<GoodsTabRela> goodsTabRelas = saveRequest.getGoodsTabRelas();
        if (CollectionUtils.isNotEmpty(goodsTabRelas)) {
            goodsTabRelas.forEach(info -> {
                info.setGoodsId(goodsId);
                goodsTabRelaRepository.save(info);
            });
        }
        //如果是多规格
        if (Constants.yes.equals(goods.getMoreSpecFlag())) {
            /**
             * 填放可用SKU相应的规格\规格值
             * （主要解决SKU可以前端删除，但相应规格值或规格仍然出现的情况）
             */
            Map<Long, Integer> isSpecEnable = new HashMap<>();
            Map<Long, Integer> isSpecDetailEnable = new HashMap<>();
            goodsInfos.forEach(goodsInfo -> {
                goodsInfo.getMockSpecIds().forEach(specId -> {
                    isSpecEnable.put(specId, Constants.yes);
                });
                goodsInfo.getMockSpecDetailIds().forEach(detailId -> {
                    isSpecDetailEnable.put(detailId, Constants.yes);
                });
            });

            //新增规格
            specs.stream()
                    .filter(goodsSpec -> Constants.yes.equals(isSpecEnable.get(goodsSpec.getMockSpecId()))) //如果SKU有这个规格
                    .forEach(goodsSpec -> {
                        goodsSpec.setCreateTime(goods.getCreateTime());
                        goodsSpec.setUpdateTime(goods.getUpdateTime());
                        goodsSpec.setGoodsId(goodsId);
                        goodsSpec.setDelFlag(DeleteFlag.NO);
                        goodsSpec.setSpecId(goodsSpecRepository.save(goodsSpec).getSpecId());
                    });
            //新增规格值
            specDetails.stream()
                    .filter(goodsSpecDetail -> Constants.yes.equals(isSpecDetailEnable.get(goodsSpecDetail.getMockSpecDetailId()))) //如果SKU有这个规格值
                    .forEach(goodsSpecDetail -> {
                        Optional<GoodsSpec> specOpt = specs.stream().filter(goodsSpec -> goodsSpec.getMockSpecId().equals(goodsSpecDetail.getMockSpecId())).findFirst();
                        if (specOpt.isPresent()) {
                            goodsSpecDetail.setCreateTime(goods.getCreateTime());
                            goodsSpecDetail.setUpdateTime(goods.getUpdateTime());
                            goodsSpecDetail.setGoodsId(goodsId);
                            goodsSpecDetail.setDelFlag(DeleteFlag.NO);
                            goodsSpecDetail.setSpecId(specOpt.get().getSpecId());
                            goodsSpecDetail.setSpecDetailId(goodsSpecDetailRepository.save(goodsSpecDetail).getSpecDetailId());
                        }
                    });

            //判断是否单规格标识
            if (specDetails.stream().filter(s -> StringUtils.isNotBlank(s.getGoodsId())).count() > 1) {
                goods.setSingleSpecFlag(false);
            }
        }

        BigDecimal minPrice = goods.getMarketPrice();//最小sku市场价
        for (GoodsInfo sku : goodsInfos) {
            if (sku.getStock() == null) {
                sku.setStock(0L);
            }
            // 同步更新库存状态 duanlsh TODO
            sku.setCateTopId(goods.getCateTopId());
            sku.setCateId(goods.getCateId());
            sku.setBrandId(goods.getBrandId());
            sku.setGoodsId(goodsId);
            sku.setGoodsInfoName(goods.getGoodsName());
            if (Integer.valueOf(GoodsPriceType.CUSTOMER.toValue()).equals(goods.getPriceType())
                    && goods.getMarketPrice() != null) {
                // XXX 后面商家boss新增商品页优化后，可以删除这部分逻辑
                sku.setMarketPrice(goods.getMarketPrice());
            }
//            sku.setCostPrice(goods.getCostPrice());
            sku.setCreateTime(goods.getCreateTime());
            sku.setUpdateTime(goods.getUpdateTime());
            sku.setAddedTime(goods.getAddedTime());
            sku.setDelFlag(goods.getDelFlag());
            if (!goods.getAddedFlag().equals(AddedFlag.PART.toValue())) {
                sku.setAddedFlag(goods.getAddedFlag());
            }
            sku.setCompanyInfoId(goods.getCompanyInfoId());
            sku.setPriceType(goods.getPriceType());
            sku.setLevelDiscountFlag(goods.getLevelDiscountFlag());
            sku.setCustomFlag(goods.getCustomFlag());
            sku.setStoreId(goods.getStoreId());
            sku.setAuditStatus(goods.getAuditStatus());
            sku.setCompanyType(goods.getCompanyType());
            sku.setAloneFlag(Boolean.FALSE);
            sku.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
            sku.setSaleType(goods.getSaleType());
            sku.setProviderId(goods.getProviderId());
            //批发类型不支付购买积分
            if (Integer.valueOf(SaleType.WHOLESALE.toValue()).equals(goods.getSaleType())) {
                sku.setBuyPoint(0L);
            }
            sku.setGoodsSource(goods.getGoodsSource());
            sku.setAddedTimingTime(goods.getAddedTimingTime());
            sku.setAddedTimingFlag(goods.getAddedTimingFlag());
            sku.setGoodsChannelType(goods.getGoodsChannelType());
            String goodsInfoId = goodsInfoRepository.save(sku).getGoodsInfoId();
            sku.setGoodsInfoId(goodsInfoId);
            sku.setEnterprisePriceType(0);
            sku.setEnterpriseDiscountFlag(false);
            sku.setEnterpriseCustomerFlag(false);

            //覆盖redis中缓存，如果需要增量修改，则需要结合前端修改
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    goodsInfoStockService.initCacheStock(sku.getStock(), goodsInfoId);
                }
            });

            if (goods.getGoodsType() == GoodsType.VIRTUAL_COUPON.toValue()) {
                VirtualCouponGoodsRequest couponGoodsRequest = new VirtualCouponGoodsRequest();
                couponGoodsRequest.setCouponId(sku.getVirtualCouponId());
                couponGoodsRequest.setSkuId(sku.getGoodsInfoId());
                couponGoodsRequest.setStoreId(sku.getStoreId());
                couponGoodsRequest.setUpdatePerson(saveRequest.getUpdatePerson());
                virtualCouponService.linkGoods(couponGoodsRequest);
            }
            //如果是多规格,新增SKU与规格明细值的关联表
            if (Constants.yes.equals(goods.getMoreSpecFlag())) {
                if (CollectionUtils.isNotEmpty(specs)) {
                    for (GoodsSpec spec : specs) {
                        if (sku.getMockSpecIds().contains(spec.getMockSpecId())) {
                            for (GoodsSpecDetail detail : specDetails) {
                                if (spec.getMockSpecId().equals(detail.getMockSpecId()) && sku.getMockSpecDetailIds().contains(detail.getMockSpecDetailId())) {
                                    GoodsInfoSpecDetailRel detailRel = new GoodsInfoSpecDetailRel();
                                    detailRel.setGoodsId(goodsId);
                                    detailRel.setGoodsInfoId(goodsInfoId);
                                    detailRel.setSpecId(spec.getSpecId());
                                    detailRel.setSpecDetailId(detail.getSpecDetailId());
                                    detailRel.setDetailName(detail.getDetailName());
                                    detailRel.setCreateTime(detail.getCreateTime());
                                    detailRel.setUpdateTime(detail.getUpdateTime());
                                    detailRel.setDelFlag(detail.getDelFlag());
                                    detailRel.setSpecName(spec.getSpecName());
                                    specDetailRels.add(goodsInfoSpecDetailRelRepository.save(detailRel));
                                }
                            }
                        }
                    }
                }
            }

            if (minPrice == null || minPrice.compareTo(sku.getMarketPrice()) > 0) {
                minPrice = sku.getMarketPrice();
            }
        }
        goods.setSkuMinMarketPrice(minPrice);

        //供应商发布商品审核通过则直接加入到商品库中
        if (goods.getAuditStatus() == CheckStatus.CHECKED && NumberUtils.INTEGER_ZERO.equals(goods.getGoodsSource())) {
            List<String> standardIds = standardImportService.importStandard(GoodsRequest.builder().goodsIds(Arrays.asList(goodsId)).build());
        }
        //更新sync状态
        if(!Objects.equals(goods.getProviderId(),defaultProvider)) {
            goodsSyncRepository.updateStatus(goods.getErpGoodsNo(), 4);
            GoodsSyncRelation goodsSyncRelation = new GoodsSyncRelation();
            goodsSyncRelation.setGoodsNo(goods.getErpGoodsNo());
            goodsSyncRelation.setGoodsId(goodsId);
            goodsSyncRelation.setDeleted(0);
            goodsSyncRelation.setCreateTime(LocalDateTime.now());
            goodsSyncRelation.setUpdateTime(LocalDateTime.now());
            goodsSyncRelationRepository.save(goodsSyncRelation);

        }
        //商品打包处理
        handGoodsPack(saveRequest);
        //商品推送到站内搜索
        siteSearchService.siteSearchBookResNotify(Arrays.asList(goods.getGoodsId()));
        return goodsId;
    }

    private void handGoodsPack(GoodsSaveRequest saveRequest) {
        if (CollectionUtils.isEmpty(saveRequest.getGoodsPackDetails())) {
            return;
        }
        //参数验证主商品
        if (CollectionUtils.isEmpty(saveRequest.getGoodsInfos())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "打包主商品sku信息不能为空");
        }
        if (saveRequest.getGoodsInfos().size() > 1) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "打包主商品sku数量不能大于1");
        }
        //参数验证子商品
        for (GoodsPackDetailDTO item : saveRequest.getGoodsPackDetails()) {
            if (Objects.isNull(item.getGoodsId())) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "打包子商品的spuId不能为空");
            }
            if (Objects.isNull(item.getGoodsInfoId())) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "打包子商品的skuId不能为空");
            }
            if (Objects.isNull(item.getCount())) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "打包子商品的组合数量不能为空");
            }
            if (Objects.isNull(item.getShareRate())) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "打包子商品的分配比例不能为空");
            }
            if (item.getShareRate().compareTo(BigDecimal.ZERO) < 0) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "打包子商品的分配比例不能为负");
            }
        }

        String mainSpuId = saveRequest.getGoodsInfos().get(0).getGoodsId();
        String mainSkuId = saveRequest.getGoodsInfos().get(0).getGoodsInfoId();

        //验证主商品不能存在子商品身份
        GoodsPackDetailDTO detailDTO = new GoodsPackDetailDTO();
        detailDTO.setDelFlag(0);
        detailDTO.setGoodsId(mainSpuId);
        List<GoodsPackDetailDTO> packDetialsByGoods = goodsPackDetailRepository.findAll(Example.of(detailDTO));
        Optional<GoodsPackDetailDTO> childRecord = packDetialsByGoods.stream().filter(item -> item.getGoodsId().equals(mainSpuId) && !item.getPackId().equals(mainSpuId)).findFirst();
        if (childRecord.isPresent()) {
            Optional<Goods> packGoods = goodsRepository.findById(childRecord.get().getPackId());
            if (!packGoods.isPresent()) {
                log.warn("根据id查询商品不存在, goodsId = {}", childRecord.get().getPackId());
                throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
            }
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "当前主商品已存在于其他商品包中:" + packGoods.get().getGoodsName());
        }

        //验证子商品不能存在主商品身份
        List<String> packIds = saveRequest.getGoodsPackDetails().stream().map(GoodsPackDetailDTO::getGoodsId).distinct().collect(Collectors.toList());
        List<GoodsPackDetailDTO> packDetails = goodsPackDetailRepository.listByPackIds(packIds);
        packDetails = packDetails.stream().filter(item -> item.getPackId().equals(item.getGoodsId())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(packDetails)) {
            List<String> goodsIds = packDetails.stream().map(GoodsPackDetailDTO::getGoodsId).distinct().collect(Collectors.toList());
            List<Goods> goodsList = goodsRepository.findAllById(goodsIds);
            if (CollectionUtils.isEmpty(goodsList)) {
                log.warn("根据ids查询商品不存在, goodsIds = {}", JSON.toJSONString(goodsIds));
                throw new SbcRuntimeException(CommonErrorCode.DATA_NOT_EXISTS);
            }
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR, "组合中含有主商品:" + goodsList.stream().map(Goods::getGoodsName).collect(Collectors.joining("、")));
        }

        //删除关联的打包数据
        goodsPackDetailRepository.removeAllByPackId(mainSpuId);

        BigDecimal sumRate = new BigDecimal(100);
        List<GoodsPackDetailDTO> insertList = new ArrayList<>();
        for (GoodsPackDetailDTO item : saveRequest.getGoodsPackDetails()) {
            sumRate = sumRate.subtract(item.getShareRate());
            GoodsPackDetailDTO childGoods = new GoodsPackDetailDTO();
            childGoods.setPackId(mainSpuId);
            childGoods.setGoodsId(item.getGoodsId());
            childGoods.setGoodsInfoId(item.getGoodsInfoId());
            childGoods.setCount(item.getCount());
            childGoods.setShareRate(item.getShareRate());
            insertList.add(childGoods);
        }
        if (sumRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new SbcRuntimeException("组合商品拆分比例总和不能超出上限");
        }

        GoodsPackDetailDTO mainGoods = new GoodsPackDetailDTO();
        mainGoods.setGoodsId(mainSpuId);
        mainGoods.setGoodsInfoId(mainSkuId);
        mainGoods.setPackId(mainSpuId);
        mainGoods.setCount(1);
        mainGoods.setShareRate(sumRate);
        insertList.add(mainGoods);
        if (goodsPackDetailRepository.saveAll(insertList).size() != insertList.size()) {
            throw new SbcRuntimeException(CommonErrorCode.FAILED, "打包数据保存失败");
        }
    }

    /**
     * 商品更新
     *
     * @param saveRequest
     * @throws SbcRuntimeException
     */
    @Transactional
    public Map<String, Object> edit(GoodsSaveRequest saveRequest) throws SbcRuntimeException {
        Goods newGoods = saveRequest.getGoods();
        Goods oldGoods = goodsRepository.findById(newGoods.getGoodsId()).orElse(null);
        if (oldGoods == null || oldGoods.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        //微信是微信直播商品，修改后需要重新向微信提审
        WxGoodsModel wxGoodsModel = wxGoodsRepository.findByGoodsIdAndDelFlag(newGoods.getGoodsId(), DeleteFlag.NO);
        if(wxGoodsModel != null){
            log.info("编辑直播商品:" + newGoods.getGoodsId());
            if(wxGoodsModel.getAuditStatus().equals(WxGoodsEditStatus.ON_CHECK)){
                log.info("编辑直播商品，正在审核中:" + newGoods.getGoodsId());
                wxGoodsModel.setNeedToAudit(3);
            }else {
                wxGoodsModel.setNeedToAudit(1);
            }
            wxGoodsRepository.save(wxGoodsModel);
        }
        //设价方式是否变更
        boolean priceTypeUpdate = !oldGoods.getPriceType().equals(newGoods.getPriceType());

//        //如果S2B模式下，商品已审核无法编辑分类
//        if (osUtil.isS2b() && CheckStatus.CHECKED.toValue() == oldGoods.getAuditStatus().toValue() && (!oldGoods.getCateId().equals(newGoods.getCateId()))) {
//            throw new SbcRuntimeException(GoodsErrorCode.EDIT_GOODS_CATE);
//        }
        List<GoodsInfo> goodsInfoList = saveRequest.getGoodsInfos();

        //判断sku编码是否重复，当组合商品时不sku是非必填的
        goodsInfoList.forEach(goodsInfo -> {

            if (StringUtils.isBlank(goodsInfo.getErpGoodsNo())) {
                throw new SbcRuntimeException(GoodsErrorCode.SPU_REQUIRED);
            }

            if (Objects.equals(goodsInfo.getGoodsType(),GoodsType.CYCLE_BUY.toValue()) || Objects.equals(goodsInfo.getGoodsType(),GoodsType.VIRTUAL_COUPON.toValue()) || Objects.equals(goodsInfo.getGoodsType(),GoodsType.VIRTUAL_GOODS.toValue())) {
                if (StringUtils.isBlank(goodsInfo.getErpGoodsInfoNo())) {
                    throw new SbcRuntimeException(GoodsErrorCode.SKU_REQUIRED);
                }
            }else {
                if (!goodsInfo.getCombinedCommodity() && StringUtils.isBlank(goodsInfo.getErpGoodsInfoNo())) {
                    throw new SbcRuntimeException(GoodsErrorCode.SKU_REQUIRED);
                }
            }

        });

        //判断sku编码是否重复
        Map<String, List<GoodsInfo>> erpSpuMap = goodsInfoList.stream().filter(goodsInfo -> StringUtils.isNotBlank(goodsInfo.getErpGoodsInfoNo())).collect(Collectors.groupingBy(GoodsInfo::getErpGoodsNo));

        erpSpuMap.forEach((key, value) -> {
            long count =value.stream().map(GoodsInfo::getErpGoodsInfoNo).distinct().count();
            boolean isRepeat = count < value.size();
            if (isRepeat){
                throw new SbcRuntimeException(GoodsErrorCode.ERP_SKU_NO_EXIST);
            }
        });

        //验证SPU编码重复
        GoodsQueryRequest queryRequest = new GoodsQueryRequest();
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setGoodsNo(newGoods.getGoodsNo());
        queryRequest.setNotGoodsId(newGoods.getGoodsId());
        if (goodsRepository.count(queryRequest.getWhereCriteria()) > 0) {
            throw new SbcRuntimeException(GoodsErrorCode.SPU_NO_EXIST);
        }

        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        infoQueryRequest.setGoodsInfoNos(saveRequest.getGoodsInfos().stream().map(GoodsInfo::getGoodsInfoNo).distinct().collect(Collectors.toList()));
        infoQueryRequest.setNotGoodsId(newGoods.getGoodsId());
        //如果SKU数据有重复
        if (saveRequest.getGoodsInfos().size() != infoQueryRequest.getGoodsInfoNos().size()) {
            throw new SbcRuntimeException(GoodsErrorCode.SKU_NO_EXIST);
        }

        //验证SKU编码重复
        List<GoodsInfo> goodsInfosList = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
        if (CollectionUtils.isNotEmpty(goodsInfosList)) {
            throw new SbcRuntimeException(GoodsErrorCode.SKU_NO_H_EXIST, new Object[]{StringUtils.join(goodsInfosList.stream().map(GoodsInfo::getGoodsInfoNo).collect(Collectors.toList()), ",")});
        }
        if (Objects.isNull(newGoods.getPriceType())) {
            newGoods.setPriceType(GoodsPriceType.MARKET.toValue());
        }

        //验证商品相关基础数据
        newGoods.setStoreId(oldGoods.getStoreId());
        this.checkBasic(newGoods);

        List<GoodsImage> goodsImages = saveRequest.getImages();
        if (CollectionUtils.isNotEmpty(goodsImages)) {
            newGoods.setGoodsImg(goodsImages.get(0).getArtworkUrl());
        } else {
            newGoods.setGoodsImg(null);
        }
        if (newGoods.getMoreSpecFlag() == null) {
            newGoods.setMoreSpecFlag(Constants.no);
        }
        if (newGoods.getCustomFlag() == null) {
            newGoods.setCustomFlag(Constants.no);
        }
        if (newGoods.getLevelDiscountFlag() == null) {
            newGoods.setLevelDiscountFlag(Constants.no);
        }

        LocalDateTime currDate = LocalDateTime.now();
        //更新商品
        newGoods.setUpdateTime(currDate);
        //如果勾选了定时上架时间
        if (Boolean.TRUE.equals(newGoods.getAddedTimingFlag())
                && newGoods.getAddedTimingTime() != null) {
            if (newGoods.getAddedTimingTime().compareTo(LocalDateTime.now()) > 0) {
                newGoods.setAddedFlag(AddedFlag.NO.toValue());
            } else {
                newGoods.setAddedFlag(AddedFlag.YES.toValue());
            }
        }
        //上下架状态是否发生变化
        boolean isChgAddedTime = false;
        //上下架更改商家代销商品的可售性
        Boolean isDealGoodsVendibility = false;
        if (!oldGoods.getAddedFlag().equals(newGoods.getAddedFlag())) {
            isChgAddedTime = true;
            isDealGoodsVendibility = true;
        }

        //更新上下架时间
        if (isChgAddedTime) {
            newGoods.setAddedTime(newGoods.getUpdateTime());
        } else {
            newGoods.setAddedTime(oldGoods.getAddedTime());
        }

        //设价类型是否发生变化 -> 影响sku的独立设价状态为false
        boolean isChgPriceType = false;
        if (!newGoods.getPriceType().equals(oldGoods.getPriceType())) {
            isChgPriceType = true;
        }

        //如果设价方式变化为非按客户设价，则将spu市场价清空
        if (isChgPriceType
                && !Integer.valueOf(GoodsPriceType.CUSTOMER.toValue()).equals(newGoods.getPriceType())) {
            newGoods.setMarketPrice(null);
            newGoods.setCustomFlag(Constants.no);
        }
        if (SaleType.RETAIL.toValue() == newGoods.getSaleType() && GoodsPriceType.STOCK.toValue() == newGoods.getPriceType()) {
            newGoods.setPriceType(GoodsPriceType.MARKET.toValue());
            priceTypeUpdate = true;
        }

        KsBeanUtil.copyProperties(newGoods, oldGoods);
        CheckStatus oldAuditStatus = oldGoods.getAuditStatus();
        goodsCommonService.setCheckState(oldGoods);
        //如果当自动审核时，更改商家代销商品的可售性
        if (CheckStatus.CHECKED.equals(oldGoods.getAuditStatus()) && (!CheckStatus.CHECKED.equals(oldAuditStatus))) {
            isDealGoodsVendibility = true;
        }
        goodsRepository.save(oldGoods);

        //更新图片
        List<GoodsImage> oldImages = goodsImageRepository.findByGoodsId(newGoods.getGoodsId());
        if (CollectionUtils.isNotEmpty(oldImages)) {
            for (GoodsImage oldImage : oldImages) {
                if (CollectionUtils.isNotEmpty(goodsImages)) {
                    Optional<GoodsImage> imageOpt = goodsImages.stream().filter(goodsImage -> oldImage.getImageId().equals(goodsImage.getImageId())).findFirst();
                    //如果图片存在，更新
                    if (imageOpt.isPresent()) {
                        KsBeanUtil.copyProperties(imageOpt.get(), oldImage);
                    } else {
                        oldImage.setDelFlag(DeleteFlag.YES);
                    }
                } else {
                    oldImage.setDelFlag(DeleteFlag.YES);
                }
                oldImage.setUpdateTime(currDate);
                goodsImageRepository.saveAll(oldImages);
            }
        }

        //更新标签
        tagService.saveAndUpdateTagForGoods(oldGoods.getGoodsId(), saveRequest.getTags());

        //新增图片
        if (CollectionUtils.isNotEmpty(goodsImages)) {
            goodsImages.stream().filter(goodsImage -> goodsImage.getImageId() == null).forEach(goodsImage -> {
                goodsImage.setCreateTime(currDate);
                goodsImage.setUpdateTime(currDate);
                goodsImage.setGoodsId(newGoods.getGoodsId());
                goodsImage.setDelFlag(DeleteFlag.NO);
                goodsImageRepository.save(goodsImage);
            });
        }

        //保存商品属性
        goodsPropDetailRelRepository.deletePropsForGoods(newGoods.getGoodsId());
        List<GoodsPropDetailRel> goodsPropDetailRels = saveRequest.getGoodsPropDetailRels();
        if (CollectionUtils.isNotEmpty(goodsPropDetailRels)) {
            //如果是修改则设置修改时间，如果是新增则设置创建时间
            goodsPropDetailRels.forEach(goodsPropDetailRel -> {
                goodsPropDetailRel.setDelFlag(DeleteFlag.NO);
                goodsPropDetailRel.setCreateTime(LocalDateTime.now());
            });
            goodsPropDetailRelRepository.saveAll(goodsPropDetailRels);
        }

        //保存商品属性
        /*List<GoodsPropDetailRel> goodsPropDetailRels = saveRequest.getGoodsPropDetailRels();
        if (CollectionUtils.isNotEmpty(goodsPropDetailRels)) {
            //修改设置修改时间
            goodsPropDetailRels.forEach(goodsPropDetailRel -> {
                goodsPropDetailRel.setDelFlag(DeleteFlag.NO);
                if (goodsPropDetailRel.getRelId() != null) {
                    goodsPropDetailRel.setUpdateTime(LocalDateTime.now());
                }
            });
        }*/
        //店铺分类
        if (osUtil.isS2b() && CollectionUtils.isNotEmpty(newGoods.getStoreCateIds())) {
            classifyGoodsRelRepository.deleteByGoodsId(newGoods.getGoodsId());
            List<ClassifyGoodsRelDTO> rels = new ArrayList<>();
            newGoods.getStoreCateIds().forEach(cateId -> {
                Date now = new Date();
                ClassifyGoodsRelDTO classifyGoodsRelDTO = new ClassifyGoodsRelDTO();
                classifyGoodsRelDTO.setClassifyId(cateId.intValue());
                classifyGoodsRelDTO.setDelFlag(0);
                classifyGoodsRelDTO.setGoodsId(newGoods.getGoodsId());
                classifyGoodsRelDTO.setCreateTime(now);
                classifyGoodsRelDTO.setUpdateTime(now);
                rels.add(classifyGoodsRelDTO);
            });
            classifyGoodsRelRepository.saveAll(rels);
        }

        List<GoodsSpec> specs = saveRequest.getGoodsSpecs();
        List<GoodsSpecDetail> specDetails = saveRequest.getGoodsSpecDetails();
        List<GoodsTabRela> goodsTabRelas = saveRequest.getGoodsTabRelas();
        if (CollectionUtils.isNotEmpty(goodsTabRelas)) {
            goodsTabRelas.forEach(info -> {
                goodsTabRelaRepository.save(info);
            });
        }


        //如果是多规格
        if (Constants.yes.equals(newGoods.getMoreSpecFlag())) {

            /**
             * 填放可用SKU相应的规格\规格值
             * （主要解决SKU可以前端删除，但相应规格值或规格仍然出现的情况）
             */
            Map<Long, Integer> isSpecEnable = new HashMap<>();
            Map<Long, Integer> isSpecDetailEnable = new HashMap<>();
            saveRequest.getGoodsInfos().forEach(goodsInfo -> {
                goodsInfo.getMockSpecIds().forEach(specId -> {
                    isSpecEnable.put(specId, Constants.yes);
                });
                goodsInfo.getMockSpecDetailIds().forEach(detailId -> {
                    isSpecDetailEnable.put(detailId, Constants.yes);
                });
            });

            Map<Long, Boolean> specCount = new HashMap<>();

            if (Constants.yes.equals(oldGoods.getMoreSpecFlag())) {
                //更新规格
                List<GoodsSpec> goodsSpecs = goodsSpecRepository.findByGoodsId(oldGoods.getGoodsId());
                if (CollectionUtils.isNotEmpty(goodsSpecs)) {
                    for (GoodsSpec oldSpec : goodsSpecs) {
                        if (CollectionUtils.isNotEmpty(specs)) {
                            Optional<GoodsSpec> specOpt = specs.stream().filter(spec -> oldSpec.getSpecId().equals(spec.getSpecId())).findFirst();
                            //如果规格存在且SKU有这个规格，更新
                            if (specOpt.isPresent() && Constants.yes.equals(isSpecEnable.get(specOpt.get().getMockSpecId()))) {
                                KsBeanUtil.copyProperties(specOpt.get(), oldSpec);
                            } else {
                                oldSpec.setDelFlag(DeleteFlag.YES);
                            }
                        } else {
                            oldSpec.setDelFlag(DeleteFlag.YES);
                        }
                        oldSpec.setUpdateTime(currDate);
                        goodsSpecRepository.save(oldSpec);


                    }
                }

                //更新规格值
                List<GoodsSpecDetail> goodsSpecDetails = goodsSpecDetailRepository.findByGoodsId(oldGoods.getGoodsId());
                if (CollectionUtils.isNotEmpty(goodsSpecDetails)) {

                    for (GoodsSpecDetail oldSpecDetail : goodsSpecDetails) {
                        if (CollectionUtils.isNotEmpty(specDetails)) {
                            Optional<GoodsSpecDetail> specDetailOpt = specDetails.stream().filter(specDetail -> oldSpecDetail.getSpecDetailId().equals(specDetail.getSpecDetailId())).findFirst();
                            //如果规格值存在且SKU有这个规格值，更新
                            if (specDetailOpt.isPresent() && Constants.yes.equals(isSpecDetailEnable.get(specDetailOpt.get().getMockSpecDetailId()))) {
                                KsBeanUtil.copyProperties(specDetailOpt.get(), oldSpecDetail);

                                //更新SKU规格值表的名称备注
                                goodsInfoSpecDetailRelRepository.updateNameBySpecDetail(specDetailOpt.get().getDetailName(), oldSpecDetail.getSpecDetailId(), oldGoods.getGoodsId());
                            } else {
                                oldSpecDetail.setDelFlag(DeleteFlag.YES);
                            }
                        } else {
                            oldSpecDetail.setDelFlag(DeleteFlag.YES);
                        }
                        oldSpecDetail.setUpdateTime(currDate);
                        goodsSpecDetailRepository.save(oldSpecDetail);

                        if (DeleteFlag.NO.equals(oldSpecDetail.getDelFlag())) {
                            specCount.put(oldSpecDetail.getSpecDetailId(), Boolean.TRUE);
                        }
                    }
                }
            }

            //新增规格
            if (CollectionUtils.isNotEmpty(specs)) {
                specs.stream().filter(goodsSpec -> goodsSpec.getSpecId() == null && Constants.yes.equals(isSpecEnable.get(goodsSpec.getMockSpecId()))).forEach(goodsSpec -> {
                    goodsSpec.setCreateTime(currDate);
                    goodsSpec.setUpdateTime(currDate);
                    goodsSpec.setGoodsId(newGoods.getGoodsId());
                    goodsSpec.setDelFlag(DeleteFlag.NO);
                    goodsSpec.setSpecId(goodsSpecRepository.save(goodsSpec).getSpecId());


                });
            }
            //新增规格值
            if (CollectionUtils.isNotEmpty(specDetails)) {
                specDetails.stream().filter(goodsSpecDetail -> goodsSpecDetail.getSpecDetailId() == null && Constants.yes.equals(isSpecDetailEnable.get(goodsSpecDetail.getMockSpecDetailId()))).forEach(goodsSpecDetail -> {
                    Optional<GoodsSpec> specOpt = specs.stream().filter(goodsSpec -> goodsSpec.getMockSpecId().equals(goodsSpecDetail.getMockSpecId())).findFirst();
                    if (specOpt.isPresent()) {
                        goodsSpecDetail.setCreateTime(currDate);
                        goodsSpecDetail.setUpdateTime(currDate);
                        goodsSpecDetail.setGoodsId(newGoods.getGoodsId());
                        goodsSpecDetail.setDelFlag(DeleteFlag.NO);
                        goodsSpecDetail.setSpecId(specOpt.get().getSpecId());
                        goodsSpecDetail.setSpecDetailId(goodsSpecDetailRepository.save(goodsSpecDetail).getSpecDetailId());

                        specCount.put(goodsSpecDetail.getSpecDetailId(), Boolean.TRUE);
                    }
                });
            }

            //判断是否单规格标识
            if (specCount.size() <= 1) {
                oldGoods.setSingleSpecFlag(true);
                goodsRepository.save(oldGoods);
            } else {
                oldGoods.setSingleSpecFlag(false);
                goodsRepository.save(oldGoods);
            }
        } else {//修改为单规格
            //如果老数据为多规格
            if (Constants.yes.equals(oldGoods.getMoreSpecFlag())) {
                //删除规格
                goodsSpecRepository.deleteByGoodsId(newGoods.getGoodsId());

                //删除规格值
                goodsSpecDetailRepository.deleteByGoodsId(newGoods.getGoodsId());

                //删除商品规格值
                goodsInfoSpecDetailRelRepository.deleteByGoodsId(newGoods.getGoodsId());
            }
            oldGoods.setSingleSpecFlag(true);
            goodsRepository.save(oldGoods);
        }

        //只存储新增的SKU数据，用于当修改价格及订货量设置为否时，只为新SKU增加相关的价格数据
        List<GoodsInfo> newGoodsInfo = new ArrayList<>();//需要被添加的sku信息


        //更新原有的SKU列表
        List<GoodsInfo> goodsInfos = saveRequest.getGoodsInfos();
        List<GoodsInfo> oldGoodsInfos = new ArrayList<>();//需要被更新的sku信息
        List<String> delInfoIds = new ArrayList<>();//需要被删除的sku信息
        if (CollectionUtils.isNotEmpty(goodsInfos)) {
            infoQueryRequest = new GoodsInfoQueryRequest();
            infoQueryRequest.setGoodsId(newGoods.getGoodsId());
            infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
            List<GoodsInfo> oldInfos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());

            if (CollectionUtils.isNotEmpty(oldInfos)) {
                for (GoodsInfo oldInfo : oldInfos) {

                    //商品设价类型变更
                    if (priceTypeUpdate && oldInfo.getEnterPriseAuditState() == EnterpriseAuditState.CHECKED) {
                        //变更企业价
                        Integer newPriceType = newGoods.getPriceType();
                        goodsIntervalPriceRepository.deleteByGoodsInfoIdAndType(oldInfo.getGoodsInfoId(), PriceType.ENTERPRISE_SKU);
                        goodsCustomerPriceRepository.deleteByGoodsInfoIdAndType(oldInfo.getGoodsInfoId(), PriceType.ENTERPRISE_SKU);
                        goodsLevelPriceRepository.deleteByGoodsInfoIdAndType(oldInfo.getGoodsInfoId(), PriceType.ENTERPRISE_SKU);
                        if (newPriceType == 2) {
                            //按市场价
                            oldInfo.setEnterprisePriceType(0);
                        } else if (newPriceType == 0) {
                            //按客户等级
                            oldInfo.setEnterprisePriceType(1);
                        } else if (newPriceType == 1) {
                            //按订货量
                            oldInfo.setEnterprisePriceType(2);
                        }
                    }

                    if (Objects.isNull(oldInfo.getStock())) {
                        oldInfo.setStock(0L);
                    }

                    //调用卡券取消关联商品
                    if (saveRequest.getGoods().getGoodsType() == GoodsType.VIRTUAL_COUPON.toValue()) {
                        VirtualCouponGoodsRequest couponGoodsRequest = new VirtualCouponGoodsRequest();
                        couponGoodsRequest.setStoreId(oldInfo.getStoreId());
                        couponGoodsRequest.setSkuId(oldInfo.getGoodsInfoId());
                        couponGoodsRequest.setCouponId(oldInfo.getVirtualCouponId());
                        couponGoodsRequest.setUpdatePerson(saveRequest.getUpdatePerson());
                        virtualCouponService.unlinkGoods(couponGoodsRequest);
                    }
                    Optional<GoodsInfo> infoOpt = goodsInfos.stream().filter(goodsInfo -> oldInfo.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).findFirst();
                    //如果SKU存在，更新
                    if (infoOpt.isPresent()) {
                        infoOpt.get().setAddedFlag(oldInfo.getAddedFlag());
                        infoOpt.get().setAddedTimingTime(oldInfo.getAddedTimingTime());
                        infoOpt.get().setAddedTimingFlag(oldInfo.getAddedTimingFlag());
                        infoOpt.get().setVendibility(oldInfo.getVendibility());

                        //如果上下架不是部分上下架，以SPU为准
                        if (!newGoods.getAddedFlag().equals(AddedFlag.PART.toValue())) {
                            infoOpt.get().setAddedFlag(newGoods.getAddedFlag());
                            infoOpt.get().setAddedTimingFlag(newGoods.getAddedTimingFlag());
                            infoOpt.get().setAddedTimingTime(newGoods.getAddedTimingTime());
                        }

                        //更新上下架时间
                        if (isChgAddedTime) {
                            infoOpt.get().setAddedTime(newGoods.getAddedTime());
                        }

                        if (Objects.isNull(infoOpt.get().getStock())) {
                            infoOpt.get().setStock(0L);
                        }

                        //如果发生设价类型变化，原SKU的独立设价设为FALSE
                        if (isChgPriceType) {
                            oldInfo.setAloneFlag(Boolean.FALSE);
                        }

                        //非独立设价SKU的叠加等级价、自定义客户都要以SPU为准
                        if (Objects.isNull(oldInfo.getAloneFlag())
                                || Boolean.FALSE.equals(oldInfo.getAloneFlag())) {
                            infoOpt.get().setCustomFlag(newGoods.getCustomFlag());
                            infoOpt.get().setLevelDiscountFlag(newGoods.getLevelDiscountFlag());
                        }
                        //市场价刷新非独立设价的SKU
                        if (Objects.nonNull(newGoods.getMarketPrice()) && Boolean.FALSE.equals(oldInfo
                                .getAloneFlag())) {
                            infoOpt.get().setMarketPrice(newGoods.getMarketPrice());
                        }

                        //不允许独立设价 sku的叠加客户等级折扣  与spu同步
                        if (newGoods.getPriceType() == 1 && newGoods.getAllowPriceSet() == 0) {
                            infoOpt.get().setLevelDiscountFlag(newGoods.getLevelDiscountFlag());
                        }
//                        infoOpt.get().setCostPrice(newGoods.getCostPrice());
                        infoOpt.get().setAuditStatus(oldGoods.getAuditStatus());
                        infoOpt.get().setBrandId(oldGoods.getBrandId());
                        infoOpt.get().setCateId(oldGoods.getCateId());
                        infoOpt.get().setCateTopId(oldGoods.getCateTopId());
                        infoOpt.get().setSaleType(oldGoods.getSaleType());

                        //批发类型不支付购买积分
                        if (Integer.valueOf(SaleType.WHOLESALE.toValue()).equals(newGoods.getSaleType())) {
                            infoOpt.get().setBuyPoint(0L);
                        }
                        KsBeanUtil.copyProperties(infoOpt.get(), oldInfo);
                        //修改预估佣金
                        if (Objects.nonNull(oldInfo.getMarketPrice()) && Objects.nonNull(oldInfo.getCommissionRate())) {
                            oldInfo.setDistributionCommission(oldInfo.getMarketPrice().multiply(oldInfo
                                    .getCommissionRate()));
                        }
                        oldGoodsInfos.add(oldInfo);//修改前后都存在的数据--加入需要被更新的sku中
                        //调用关联商品
                        if (saveRequest.getGoods().getGoodsType() == GoodsType.VIRTUAL_COUPON.toValue()) {
                            VirtualCouponGoodsRequest couponGoodsRequest = new VirtualCouponGoodsRequest();
                            couponGoodsRequest.setStoreId(oldInfo.getStoreId());
                            couponGoodsRequest.setSkuId(oldInfo.getGoodsInfoId());
                            couponGoodsRequest.setCouponId(oldInfo.getVirtualCouponId());
                            couponGoodsRequest.setUpdatePerson(saveRequest.getUpdatePerson());
                            virtualCouponService.linkGoods(couponGoodsRequest);
                        }
                    } else {
                        oldInfo.setDelFlag(DeleteFlag.YES);
                        delInfoIds.add(oldInfo.getGoodsInfoId());//修改后不存在的数据--加入需要被删除的sku中
                    }
                    oldInfo.setGoodsInfoName(newGoods.getGoodsName());
                    oldInfo.setUpdateTime(currDate);
                    oldInfo.setGoodsChannelType(oldGoods.getGoodsChannelType());
                    goodsInfoRepository.save(oldInfo);

                    //覆盖redis中的库存，如需增量修改，则需要结合前端修改
                    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                        @Override
                        public void afterCommit() {
                            goodsInfoStockService.initCacheStock(oldInfo.getStock(), oldInfo.getGoodsInfoId());
                        }
                    });


                }

                //删除SKU相关的规格关联表
                if (!delInfoIds.isEmpty()) {
                    goodsInfoSpecDetailRelRepository.deleteByGoodsInfoIds(delInfoIds, newGoods.getGoodsId());
                    //批量删除积分商城中该sku，避免前台出现脏数据
                    pointsGoodsRepository.deleteByGoodInfoIdList(delInfoIds);

                    //被删除的sku需被更新为不可售
                    ProviderGoodsNotSellRequest providerGoodsNotSellRequest = ProviderGoodsNotSellRequest.builder()
                            .checkFlag(Boolean.FALSE).goodsInfoIds(delInfoIds).build();
//                    this.dealGoodsVendibility(providerGoodsNotSellRequest);
                }
            }

            //只保存新SKU
            for (GoodsInfo sku : goodsInfos) {
                sku.setCateTopId(newGoods.getCateTopId());
                sku.setCateId(newGoods.getCateId());
                sku.setBrandId(newGoods.getBrandId());
                sku.setGoodsId(newGoods.getGoodsId());
                sku.setGoodsInfoName(newGoods.getGoodsName());
                sku.setCreateTime(currDate);
                sku.setUpdateTime(currDate);
                sku.setDelFlag(DeleteFlag.NO);
                sku.setCompanyInfoId(oldGoods.getCompanyInfoId());
                sku.setPriceType(oldGoods.getPriceType());
                sku.setStoreId(oldGoods.getStoreId());
                sku.setAuditStatus(oldGoods.getAuditStatus());
                sku.setCompanyType(oldGoods.getCompanyType());
                //只处理新增的SKU
                if (sku.getGoodsInfoId() != null) {
                    continue;
                }
                if (sku.getStock() == null) {
                    sku.setStock(0L);
                }
                sku.setCustomFlag(oldGoods.getCustomFlag());
                sku.setLevelDiscountFlag(oldGoods.getLevelDiscountFlag());

                //新商品会采用SPU市场价
                if (newGoods.getMarketPrice() != null) {
                    sku.setMarketPrice(newGoods.getMarketPrice());
                }
//                sku.setCostPrice(oldGoods.getCostPrice());

                //如果SPU选择部分上架，新增SKU的上下架状态为上架
                sku.setAddedFlag(oldGoods.getAddedFlag().equals(AddedFlag.PART.toValue()) ? AddedFlag.YES.toValue() : newGoods.getAddedFlag());
                sku.setAddedTime(oldGoods.getAddedTime());
                sku.setAddedTimingFlag(oldGoods.getAddedTimingFlag());
                sku.setProviderId(newGoods.getProviderId());
                sku.setAddedTimingTime(oldGoods.getAddedTimingTime());
                sku.setAloneFlag(Boolean.FALSE);
                sku.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                sku.setSaleType(newGoods.getSaleType());
                sku.setGoodsChannelType(oldGoods.getGoodsChannelType());
                //批发类型不支付购买积分
                if (Integer.valueOf(SaleType.WHOLESALE.toValue()).equals(newGoods.getSaleType())) {
                    sku.setBuyPoint(0L);
                }
                sku.setGoodsSource(oldGoods.getGoodsSource());
                String goodsInfoId = goodsInfoRepository.save(sku).getGoodsInfoId();
                sku.setGoodsInfoId(goodsInfoId);
                //把新插入的sku也调用关联卡券接口
                if (saveRequest.getGoods().getGoodsType() == GoodsType.VIRTUAL_COUPON.toValue()) {
                    VirtualCouponGoodsRequest couponGoodsRequest = new VirtualCouponGoodsRequest();
                    couponGoodsRequest.setStoreId(sku.getStoreId());
                    couponGoodsRequest.setSkuId(sku.getGoodsInfoId());
                    couponGoodsRequest.setCouponId(sku.getVirtualCouponId());
                    couponGoodsRequest.setUpdatePerson(saveRequest.getUpdatePerson());
                    virtualCouponService.linkGoods(couponGoodsRequest);
                }
                //初始化redis缓存
                TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                    @Override
                    public void afterCommit() {
                        goodsInfoStockService.initCacheStock(sku.getStock(), goodsInfoId);
                    }
                });


                //如果是多规格,新增SKU与规格明细值的关联表
                if (Constants.yes.equals(newGoods.getMoreSpecFlag())) {
                    if (CollectionUtils.isNotEmpty(specs)) {
                        for (GoodsSpec spec : specs) {
                            if (sku.getMockSpecIds().contains(spec.getMockSpecId())) {
                                for (GoodsSpecDetail detail : specDetails) {
                                    if (spec.getMockSpecId().equals(detail.getMockSpecId()) && sku.getMockSpecDetailIds().contains(detail.getMockSpecDetailId())) {
                                        GoodsInfoSpecDetailRel detailRel = new GoodsInfoSpecDetailRel();
                                        detailRel.setGoodsId(newGoods.getGoodsId());
                                        detailRel.setGoodsInfoId(goodsInfoId);
                                        detailRel.setSpecId(spec.getSpecId());
                                        detailRel.setSpecDetailId(detail.getSpecDetailId());
                                        detailRel.setDetailName(detail.getDetailName());
                                        detailRel.setCreateTime(currDate);
                                        detailRel.setUpdateTime(currDate);
                                        detailRel.setDelFlag(DeleteFlag.NO);
                                        goodsInfoSpecDetailRelRepository.save(detailRel);
                                    }
                                }
                            }
                        }
                    }
                }
                newGoodsInfo.add(sku);//修改后才存在(新出现)的数据--加入需要被添加的sku中
            }
        }

        //为新增加的SKU补充设价数据
        if (CollectionUtils.isNotEmpty(newGoodsInfo)) {
            if (Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(oldGoods.getPriceType())) {
                saveRequest.setGoodsIntervalPrices(goodsIntervalPriceRepository.findByGoodsId(oldGoods.getGoodsId()));
            } else if (Integer.valueOf(GoodsPriceType.CUSTOMER.toValue()).equals(oldGoods.getPriceType())) {
                saveRequest.setGoodsLevelPrices(goodsLevelPriceRepository.findByGoodsId(oldGoods.getGoodsId()));
                //按客户单独定价
                if (Constants.yes.equals(oldGoods.getCustomFlag())) {
                    saveRequest.setGoodsCustomerPrices(goodsCustomerPriceRepository.findByGoodsId(oldGoods.getGoodsId()));
                }
            }
            this.saveGoodsPrice(newGoodsInfo, oldGoods, saveRequest);
        }

        //如果spu是部分上架,当相关Sku都是上架时，自动设为上架
        if (AddedFlag.PART.toValue() == oldGoods.getAddedFlag()) {
            long addedCount = goodsInfos.stream().filter(g -> DeleteFlag.NO.equals(g.getDelFlag()) && AddedFlag.YES.toValue() == g.getAddedFlag()).count();
            long count = goodsInfos.stream().filter(g -> DeleteFlag.NO.equals(g.getDelFlag())).count();
            if (addedCount == count) {
                oldGoods.setAddedFlag(AddedFlag.YES.toValue());
                goodsRepository.save(oldGoods);
            }
        }

        //所有可用的SKU数据
        List<GoodsInfo> allSku = new ArrayList<>();
        allSku.addAll(oldGoodsInfos);
        allSku.addAll(newGoodsInfo);
        Long newStock = allSku.stream().filter(s -> Objects.nonNull(s.getStock())).mapToLong(GoodsInfo::getStock).sum();
        //库存发生变化，更改商家代销商品的库存
        if (!newStock.equals(oldGoods.getStock())) {
            isDealGoodsVendibility = true;
        }
        oldGoods.setStock(newStock);
        oldGoods.setSkuMinMarketPrice(allSku.stream().filter(s -> Objects.nonNull(s.getMarketPrice()))
                .map(GoodsInfo::getMarketPrice)
                .reduce(BinaryOperator.minBy(BigDecimal::compareTo))
                .orElse(oldGoods.getMarketPrice()));

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("newGoodsInfo", newGoodsInfo);
        returnMap.put("delInfoIds", delInfoIds);
        returnMap.put("oldGoodsInfos", oldGoodsInfos);
        returnMap.put("oldGoods", oldGoods);
        returnMap.put("isDealGoodsVendibility", isDealGoodsVendibility);

        //商品打包处理
        handGoodsPack(saveRequest);

        siteSearchService.siteSearchBookResNotify(Arrays.asList(newGoods.getGoodsId()));
        return returnMap;
    }

    /**
     * 第二步，商品保存设价
     *
     * @param saveRequest
     */
    @Transactional
    public void savePrice(GoodsSaveRequest saveRequest) throws SbcRuntimeException {
        Goods newGoods = saveRequest.getGoods();
        Goods oldGoods = goodsRepository.findById(newGoods.getGoodsId()).orElse(null);
        if (oldGoods == null || oldGoods.getDelFlag().compareTo(DeleteFlag.YES) == 0) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }

        goodsIntervalPriceRepository.deleteByGoodsId(oldGoods.getGoodsId());
        goodsLevelPriceRepository.deleteByGoodsId(oldGoods.getGoodsId());
        goodsCustomerPriceRepository.deleteByGoodsId(oldGoods.getGoodsId());

        //按订货量设价，保存订货区间
        if (Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(newGoods.getPriceType())) {
            if (CollectionUtils.isEmpty(saveRequest.getGoodsIntervalPrices()) || saveRequest.getGoodsIntervalPrices().stream().filter(intervalPrice -> intervalPrice.getCount() == 1).count() == 0) {
                GoodsIntervalPrice intervalPrice = new GoodsIntervalPrice();
                intervalPrice.setCount(1L);
                intervalPrice.setPrice(newGoods.getMarketPrice());
                if (saveRequest.getGoodsIntervalPrices() == null) {
                    saveRequest.setGoodsLevelPrices(new ArrayList<>());
                }
                saveRequest.getGoodsIntervalPrices().add(intervalPrice);
            }

            saveRequest.getGoodsIntervalPrices().forEach(intervalPrice -> {
                intervalPrice.setGoodsId(newGoods.getGoodsId());
                intervalPrice.setType(PriceType.SPU);
            });
            goodsIntervalPriceRepository.saveAll(saveRequest.getGoodsIntervalPrices());
        } else if (Integer.valueOf(GoodsPriceType.CUSTOMER.toValue()).equals(newGoods.getPriceType())) {
            //按客户等级
            saveRequest.getGoodsLevelPrices().forEach(goodsLevelPrice -> {
                goodsLevelPrice.setGoodsId(newGoods.getGoodsId());
                goodsLevelPrice.setType(PriceType.SPU);
            });
            goodsLevelPriceRepository.saveAll(saveRequest.getGoodsLevelPrices());

            //按客户单独定价
            if (Constants.yes.equals(newGoods.getCustomFlag()) && CollectionUtils.isNotEmpty(saveRequest.getGoodsCustomerPrices())) {
                saveRequest.getGoodsCustomerPrices().forEach(price -> {
                    price.setGoodsId(newGoods.getGoodsId());
                    price.setType(PriceType.SPU);
                });
                goodsCustomerPriceRepository.saveAll(saveRequest.getGoodsCustomerPrices());
            }
        }
        if (!Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(newGoods.getPriceType()) || newGoods.getSaleType() == 1) {
            oldGoods.setAllowPriceSet(0);
        }
        oldGoods.setPriceType(newGoods.getPriceType());
        oldGoods.setCustomFlag(newGoods.getCustomFlag());
        oldGoods.setLevelDiscountFlag(newGoods.getLevelDiscountFlag());
        goodsRepository.save(oldGoods);

        //存储SKU相关的设价数据
        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
        infoQueryRequest.setGoodsId(newGoods.getGoodsId());
        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
        List<GoodsInfo> goodsInfos = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
//        for (GoodsInfo sku : goodsInfos) {
//            sku.setPriceType(newGoods.getPriceType());
//            sku.setCustomFlag(newGoods.getCustomFlag());
//            sku.setLevelDiscountFlag(newGoods.getLevelDiscountFlag());
//        }
//        goodsInfoRepository.save(goodsInfos);

        this.saveGoodsPrice(goodsInfos, newGoods, saveRequest);

        siteSearchService.siteSearchBookResNotify(Arrays.asList(newGoods.getGoodsId()));
    }

    /**
     * 同时保存商品基本信息/设价信息
     *
     * @param saveRequest 参数
     */
    @Transactional
    public String addAll(GoodsSaveRequest saveRequest) {
        saveRequest.getGoods().setGoodsId(this.add(saveRequest));
        this.savePrice(saveRequest);
        return saveRequest.getGoods().getGoodsId();
    }

    /**
     * 同时更新商品基本信息/设价信息
     */
    @Transactional
    public Map<String, Object> editAll(GoodsSaveRequest saveRequest) {
        Map<String, Object> res = this.edit(saveRequest);
        this.savePrice(saveRequest);
        return res;
    }

    /**
     * 储存商品相关设价信息
     *
     * @param goodsInfos  sku集
     * @param goods       同一个spu信息
     * @param saveRequest 请求封装参数
     */
    private void saveGoodsPrice(List<GoodsInfo> goodsInfos, Goods goods, GoodsSaveRequest saveRequest) {
        List<String> skuIds = new ArrayList<>();
        //提取非独立设价的Sku编号,进行清理设价数据
        if (goods.getPriceType() == 1 && goods.getAllowPriceSet() == 0) {
            skuIds = goodsInfos.stream()
                    .map(GoodsInfo::getGoodsInfoId)
                    .collect(Collectors.toList());
        } else {
            skuIds = goodsInfos.stream()
                    .filter(sku -> Objects.isNull(sku.getAloneFlag()) || !sku.getAloneFlag())
                    .map(GoodsInfo::getGoodsInfoId)
                    .collect(Collectors.toList());
        }

        if (skuIds.size() > 0) {
            goodsIntervalPriceRepository.deleteByGoodsInfoIds(skuIds);
            goodsLevelPriceRepository.deleteByGoodsInfoIds(skuIds);
            goodsCustomerPriceRepository.deleteByGoodsInfoIds(skuIds);
        }

        for (GoodsInfo sku : goodsInfos) {
            //如果SKU是保持独立，则不更新
            if (!(goods.getPriceType() == 1 && goods.getAllowPriceSet() == 0) && Objects.nonNull(sku.getAloneFlag())
                    && sku.getAloneFlag()) {
                continue;
            }

            //按订货量设价，保存订货区间
            if (Integer.valueOf(GoodsPriceType.STOCK.toValue()).equals(goods.getPriceType())) {
                if (CollectionUtils.isNotEmpty(saveRequest.getGoodsIntervalPrices())) {
                    List<GoodsIntervalPrice> newGoodsInterValPrice = new ArrayList<>();
                    saveRequest.getGoodsIntervalPrices().forEach(intervalPrice -> {
                        GoodsIntervalPrice newIntervalPrice = new GoodsIntervalPrice();
                        newIntervalPrice.setGoodsId(sku.getGoodsId());
                        newIntervalPrice.setGoodsInfoId(sku.getGoodsInfoId());
                        newIntervalPrice.setType(PriceType.SKU);
                        newIntervalPrice.setCount(intervalPrice.getCount());
                        newIntervalPrice.setPrice(intervalPrice.getPrice());
                        newGoodsInterValPrice.add(newIntervalPrice);
                    });
                    goodsIntervalPriceRepository.saveAll(newGoodsInterValPrice);
                }
            } else if (Integer.valueOf(GoodsPriceType.CUSTOMER.toValue()).equals(goods.getPriceType())) {
                //按客户等级
                if (CollectionUtils.isNotEmpty(saveRequest.getGoodsLevelPrices())) {
                    List<GoodsLevelPrice> newLevelPrices = new ArrayList<>();
                    saveRequest.getGoodsLevelPrices().forEach(goodsLevelPrice -> {
                        GoodsLevelPrice newLevelPrice = new GoodsLevelPrice();
                        newLevelPrice.setLevelId(goodsLevelPrice.getLevelId());
                        newLevelPrice.setGoodsId(sku.getGoodsId());
                        newLevelPrice.setGoodsInfoId(sku.getGoodsInfoId());
                        newLevelPrice.setPrice(goodsLevelPrice.getPrice());
                        newLevelPrice.setCount(goodsLevelPrice.getCount());
                        newLevelPrice.setMaxCount(goodsLevelPrice.getMaxCount());
                        newLevelPrice.setType(PriceType.SKU);
                        newLevelPrices.add(newLevelPrice);
                    });
                    goodsLevelPriceRepository.saveAll(newLevelPrices);
                }

                //按客户单独定价
                if (Constants.yes.equals(goods.getCustomFlag()) && CollectionUtils.isNotEmpty(saveRequest.getGoodsCustomerPrices())) {
                    List<GoodsCustomerPrice> newCustomerPrices = new ArrayList<>();
                    saveRequest.getGoodsCustomerPrices().forEach(price -> {
                        GoodsCustomerPrice newCustomerPrice = new GoodsCustomerPrice();
                        newCustomerPrice.setGoodsInfoId(sku.getGoodsInfoId());
                        newCustomerPrice.setGoodsId(sku.getGoodsId());
                        newCustomerPrice.setCustomerId(price.getCustomerId());
                        newCustomerPrice.setMaxCount(price.getMaxCount());
                        newCustomerPrice.setCount(price.getCount());
                        newCustomerPrice.setType(PriceType.SKU);
                        newCustomerPrice.setPrice(price.getPrice());
                        newCustomerPrices.add(newCustomerPrice);
                    });
                    goodsCustomerPriceRepository.saveAll(newCustomerPrices);
                }
            }
        }
        siteSearchService.siteSearchBookResNotify(Arrays.asList(goods.getGoodsId()));
    }

    /**
     * 商品删除
     *
     * @param goodsIds 多个商品
     * @throws SbcRuntimeException
     */
    @Transactional
    public void delete(List<String> goodsIds, Long storeId,String updatePerson) throws SbcRuntimeException {
        List<Goods> goodsList = goodsRepository.findByGoodsIdInAndStoreIdAndDelFlag(goodsIds, storeId, DeleteFlag.NO);
        if (CollectionUtils.isEmpty(goodsList) || goodsList.size() != goodsIds.size()) {
            throw new SbcRuntimeException(GoodsErrorCode.NOT_EXIST);
        }
        List<String> goodsIdList = goodsList.stream()
                .filter(goods -> goods.getGoodsType() == GoodsType.VIRTUAL_COUPON.toValue())
                .map(goods -> goods.getGoodsId())
                .collect(Collectors.toList());
        List<GoodsInfo> goodsInfos =
                goodsInfoRepository.findByGoodsIds(goodsIdList);
        for (GoodsInfo goodsInfo : goodsInfos) {
            if ( goodsInfo.getVirtualCouponId() !=null ){
                VirtualCouponGoodsRequest request = new VirtualCouponGoodsRequest();
                request.setCouponId(goodsInfo.getVirtualCouponId());
                request.setStoreId(storeId);
                request.setSkuId(goodsInfo.getGoodsInfoId());
                request.setUpdatePerson(updatePerson);
                virtualCouponService.unlinkGoods(request);
            }
        }
        goodsRepository.deleteByGoodsIds(goodsIds);
        goodsInfoRepository.deleteByGoodsIds(goodsIds);
        goodsPropDetailRelRepository.deleteByGoodsIds(goodsIds);
        goodsSpecRepository.deleteByGoodsIds(goodsIds);
        goodsSpecDetailRepository.deleteByGoodsIds(goodsIds);
        goodsInfoSpecDetailRelRepository.deleteByGoodsIds(goodsIds);
        standardGoodsRelRepository.deleteByGoodsIds(goodsIds);
        pointsGoodsRepository.deleteByGoodsIdList(goodsIds);
        goodsIds.forEach(goodsID -> {
            distributiorGoodsInfoRepository.deleteByGoodsId(goodsID);
        });

        //ares埋点-商品-后台批量删除商品spu
        goodsAresService.dispatchFunction("delGoodsSpu", goodsIds);

        siteSearchService.siteSearchBookResNotify(goodsIds);
    }

    /**
     * 更新商品上下架状态
     *
     * @param addedFlag 状态
     * @param goodsIds  多个商品
     * @throws SbcRuntimeException
     */
    @Transactional
    public void updateAddedStatus(Integer addedFlag, List<String> goodsIds) throws SbcRuntimeException {
        goodsRepository.updateAddedFlagByGoodsIds(addedFlag, goodsIds);
        goodsInfoRepository.updateAddedFlagByGoodsIds(addedFlag, goodsIds);
        if (Constants.no.intValue() == addedFlag.intValue()) {
            //取消定时上架
            goodsRepository.updateAddedTimingFlagByGoodsIds(Boolean.FALSE, goodsIds);
            goodsInfoRepository.updateAddedTimingFlagByGoodsIds(Boolean.FALSE, goodsIds);
            goodsIds.forEach(goodsID -> {
                distributiorGoodsInfoRepository.deleteByGoodsId(goodsID);
            });
        }

        siteSearchService.siteSearchBookResNotify(goodsIds);
    }

    /**
     * 批量更新商品分类
     *
     * @param goodsIds
     * @param storeCateIds
     */
    @Transactional
    public void updateCate(List<String> goodsIds, List<Long> storeCateIds) {

        // 删除商品分类
        storeCateGoodsRelaRepository.deleteByGoodsIds(goodsIds);

        // 添加商品分类
        List<StoreCateGoodsRela> relas = storeCateIds.stream()
                .flatMap(storeCateId -> // 遍历分类
                        goodsIds.stream().map(goodsId -> // 遍历每个分类下的商品
                                StoreCateGoodsRela.builder().storeCateId(storeCateId).goodsId(goodsId).build()))
                .collect(Collectors.toList());

        storeCateGoodsRelaRepository.saveAll(relas);

        siteSearchService.siteSearchBookResNotify(goodsIds);
    }

    /**
     * 检测商品公共基础类
     * 如分类、品牌、店铺分类
     *
     * @param goods 商品信息
     */
    public void checkBasic(Goods goods) {
        GoodsCate cate = this.goodsCateRepository.findById(goods.getCateId()).orElse(null);
        if (Objects.isNull(cate) || Objects.equals(DeleteFlag.YES, cate.getDelFlag())) {
            throw new SbcRuntimeException(GoodsCateErrorCode.NOT_EXIST);
        }
        goods.setCateTopId(Long.parseLong(cate.getCatePath().split("\\|")[1]));

        if (goods.getBrandId() != null) {
            GoodsBrand brand = this.goodsBrandRepository.findById(goods.getBrandId()).orElse(null);
            if (Objects.isNull(brand) || Objects.equals(DeleteFlag.YES, brand.getDelFlag())) {
                throw new SbcRuntimeException(GoodsBrandErrorCode.NOT_EXIST);
            }
        }

        if (osUtil.isS2b()) {
            //验证是否签约分类
            ContractCateQueryRequest cateQueryRequest = new ContractCateQueryRequest();
            cateQueryRequest.setStoreId(goods.getStoreId());
            cateQueryRequest.setCateId(goods.getCateId());
            if (contractCateRepository.count(cateQueryRequest.getWhereCriteria()) < 1) {
                throw new SbcRuntimeException(SigningClassErrorCode.CONTRACT_CATE_NOT_EXIST);
            }

            //验证是否签约品牌
            if (goods.getBrandId() != null) {
                ContractBrandQueryRequest brandQueryRequest = new ContractBrandQueryRequest();
                brandQueryRequest.setStoreId(goods.getStoreId());
                brandQueryRequest.setGoodsBrandIds(Collections.singletonList(goods.getBrandId()));
                if (contractBrandRepository.count(brandQueryRequest.getWhereCriteria()) < 1) {
                    throw new SbcRuntimeException(GoodsBrandErrorCode.NOT_EXIST);
                }
            }

            //验证店铺分类存在性
            if(CollectionUtils.isNotEmpty(goods.getStoreCateIds())){
                List<Integer> ids = goods.getStoreCateIds().stream().map(Long::intValue).collect(Collectors.toList());
                List<ClassifyDTO> classifies = classifyRepository.findAllById(ids);
                if(classifies.size() != goods.getStoreCateIds().size()){
                    throw new SbcRuntimeException(StoreCateErrorCode.NOT_EXIST);
                }
            }
        }
    }

    /**
     * 根据商家编号批量更新spu商家名称
     *
     * @param supplierName
     * @param companyInfoId
     */
    @GlobalTransactional
    @Transactional
    public void updateSupplierName(String supplierName, Long companyInfoId) {
        goodsRepository.updateSupplierName(supplierName, companyInfoId);
    }

    /**
     * 根据多个SpuID查询属性关联
     *
     * @param goodsIds
     * @return
     */
    public List<GoodsPropDetailRel> findRefByGoodIds(List<String> goodsIds) {
        List<Object> objectList = goodsPropDetailRelRepository.findRefByGoodIds(goodsIds);
        if (objectList != null && objectList.size() > 0) {
            List<GoodsPropDetailRel> rels = new ArrayList<>();
            objectList.stream().forEach(obj -> {
                GoodsPropDetailRel rel = new GoodsPropDetailRel();
                Object[] object = (Object[]) obj;
                Long propId = ((BigInteger) object[0]).longValue();
                Long detailId = ((BigInteger) object[1]).longValue();
                String goodsId = String.valueOf(object[2]);
                String propValue = String.valueOf(object[3]);
                rel.setPropId(propId);
                rel.setDetailId(detailId);
                rel.setGoodsId(goodsId);
                rel.setPropValue(propValue);
                rels.add(rel);
            });
            return rels;
        }
        return Collections.emptyList();
    }

    /**
     * 根据属性id查询
     */
    public List<GoodsProp> findPropByIds(List<Long> ids) {
        return goodsPropRepository.findAllByPropIdIn(ids);
    }

    @Transactional
    public void updateFreight(Long freightTempId, List<String> goodsIds) throws SbcRuntimeException {
        goodsRepository.updateFreightTempIdByGoodsIds(freightTempId, goodsIds);
    }


    public List<Goods> findAll(GoodsQueryRequest goodsQueryRequest) {
        return goodsRepository.findAll(goodsQueryRequest.getWhereCriteria());
    }

    public Page<Goods> pageByCondition(GoodsQueryRequest request) {
        if (CollectionUtils.isEmpty(request.getGoodsIds())) {
            //分页优化，当百万数据时，先分页提取goodsId
            Page<String> ids = this.findIdsByCondition(request);
            if (CollectionUtils.isEmpty(ids.getContent())) {
                return new PageImpl<>(Collections.emptyList(), request.getPageable(), ids.getTotalElements());
            }
            request.setGoodsIds(ids.getContent());
            List<Goods> goods = goodsRepository.findAll(request.getWhereCriteria(), request.getSort());
            return new PageImpl<>(goods, request.getPageable(), ids.getTotalElements());
        }
        return goodsRepository.findAll(request.getWhereCriteria(), request.getPageRequest());
    }

    /**
     * 分页提取goodsId
     *
     * @param request
     * @return
     */
    private Page<String> findIdsByCondition(GoodsQueryRequest request) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<Goods> rt = cq.from(Goods.class);
        cq.select(rt.get("goodsId"));
        Specification<Goods> spec = request.getWhereCriteria();
        Predicate predicate = spec.toPredicate(rt, cq, cb);
        if (predicate != null) {
            cq.where(predicate);
        }
        Sort sort = request.getSort();
        if (sort.isSorted()) {
            cq.orderBy(QueryUtils.toOrders(sort, rt, cb));
        }
        cq.orderBy(QueryUtils.toOrders(request.getSort(), rt, cb));
        TypedQuery<String> query = entityManager.createQuery(cq);
        query.setFirstResult((int) request.getPageRequest().getOffset());
        query.setMaxResults(request.getPageRequest().getPageSize());

        return PageableExecutionUtils.getPage(query.getResultList(), request.getPageable(), () -> {
            CriteriaBuilder countCb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Long> countCq = countCb.createQuery(Long.class);
            Root<Goods> crt = countCq.from(Goods.class);
            countCq.select(countCb.count(crt));
            if (predicate != null) {
                countCq.where(predicate);
            }
            return entityManager.createQuery(countCq).getResultList().stream().filter(Objects::nonNull).mapToLong(s -> s).sum();
        });
    }


    /**
     * 根据商品id批量查询Goods
     *
     * @param goodsIds
     * @return
     */
    public List<Goods> listByGoodsIds(List<String> goodsIds) {
        GoodsQueryRequest goodsQueryRequest = new GoodsQueryRequest();
        goodsQueryRequest.setGoodsIds(goodsIds);
        return goodsRepository.findAll(goodsQueryRequest.getWhereCriteria());
    }

    /**
     * @param goodsIds
     * @return
     */
    public List<Goods> listByProviderGoodsIds(List<String> goodsIds) {
        GoodsQueryRequest goodsQueryRequest = new GoodsQueryRequest();
        goodsQueryRequest.setProviderGoodsIds(goodsIds);
        return goodsRepository.findAll(goodsQueryRequest.getWhereCriteria());
    }

    /**
     * 查询需要同步的商品
     *
     * @param request
     * @return
     */
    public List<Goods> listNeedSyn(GoodsQueryNeedSynRequest request) {

        //  改造标识到 standar_goods_rel  先查询出关系记录，再根据关系记录查出商品信息
        List<StandardGoodsRel> needSynRelList = standardGoodsRelRepository.findByNeedSynByStoreId(request.getStoreId());

        return goodsRepository.findAllByGoodsIdIn(needSynRelList.stream().map(StandardGoodsRel::getGoodsId).collect(Collectors.toList()));

    }

    /**
     * 根据商品id查询Goods
     *
     * @param goodsId
     * @return
     */
    public Goods getGoodsById(String goodsId) {
        return goodsRepository.findById(goodsId).orElse(null);
    }



    /**
     * 按条件查询数量
     *
     * @return
     */
    public long countByCondition(GoodsQueryRequest request) {
        return goodsRepository.count(request.getWhereCriteria());
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 更新商品收藏量
     * @Date 15:54 2019/4/11
     * @Param [goodsModifyCollectNumRequest]
     **/
    @Transactional
    public void updateGoodsCollectNum(GoodsModifyCollectNumRequest goodsModifyCollectNumRequest) {
        goodsRepository.updateGoodsCollectNum(goodsModifyCollectNumRequest.getGoodsCollectNum(), goodsModifyCollectNumRequest.getGoodsId());
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 更新商品销量
     * @Date 16:06 2019/4/11
     * @Param [goodsModifySalesNumRequest]
     **/
    @Transactional
    public void updateGoodsSalesNum(GoodsModifySalesNumRequest goodsModifySalesNumRequest) {
        goodsRepository.updateGoodsSalesNum(goodsModifySalesNumRequest.getGoodsSalesNum(), goodsModifySalesNumRequest.getGoodsId());
    }

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 更新商品评论数据
     * @Date 16:09 2019/4/11
     * @Param [goodsModifyPositiveFeedbackRequest]
     **/
    @Transactional
    public void updateGoodsFavorableCommentNum(GoodsModifyEvaluateNumRequest goodsModifyPositiveFeedbackRequest) {
        if (goodsModifyPositiveFeedbackRequest.getEvaluateScore() == 5 ||
                goodsModifyPositiveFeedbackRequest.getEvaluateScore() == 4) {
            //如果评论为五星好评，则好评数量加1
            goodsRepository.updateGoodsFavorableCommentNum(1L, goodsModifyPositiveFeedbackRequest.getGoodsId());
        }
        goodsRepository.updateGoodsEvaluateNum(goodsModifyPositiveFeedbackRequest.getGoodsId());
    }

    /**
     * @return void
     * @Description 更新商品注水销量
     * @Date 16:06 2019/4/11
     **/
    @Transactional
    public void updateShamSalesNum(String goodsId, Long shamSalesNum) {
        goodsRepository.updateShamGoodsSalesNum(shamSalesNum, goodsId);
    }

    /**
     * @return void
     * @Description 更新商品排序号
     * @Date 16:06 2019/4/11
     **/
    @Transactional
    public void updateSortNo(String goodsId, Long sortNo) {
        goodsRepository.updateSortNo(sortNo, goodsId);
    }

    public List<Goods> findByProviderGoodsId(String providerGoodsId) {
        return goodsRepository.findAllByProviderGoodsId(providerGoodsId);
    }


    /**
     * 同步商家商品和商品库sku 里的supplyPrice
     *
     * @param goodsSaveRequest
     * @param providerGoods
     */
    @Transactional
    public void synStoreGoodsInfoAndStandardSkuForSupplyPrice(GoodsSaveRequest goodsSaveRequest, List<Goods> providerGoods) {
        //同步商家商品的供货价
        providerGoods.forEach(s -> {
            s.setSupplyPrice(goodsSaveRequest.getGoods().getSupplyPrice());
        });
        goodsRepository.saveAll(providerGoods);

        List<String> goodIds = providerGoods.stream().map(Goods::getGoodsId).collect(Collectors.toList());

        //供应商商品goodsInfoId->supplyPrice
        HashMap<String, BigDecimal> providerMapSupplyPrice = new HashMap<>();
        //供应商商品goodsInfoId->stock
        HashMap<String, Long> providerMapStock = new HashMap<>();
        List<GoodsInfo> providerGoodsInfos = goodsSaveRequest.getGoodsInfos();
        providerGoodsInfos.forEach(goodsInfo -> {
            providerMapSupplyPrice.put(goodsInfo.getGoodsInfoId(), goodsInfo.getSupplyPrice());
            providerMapStock.put(goodsInfo.getGoodsInfoId(), goodsInfo.getStock());
        });

        //商家商品goodsInfoId->供应商商品goodsInfoId
        HashMap<String, String> storeMapSupplyPrice = new HashMap<>();
        List<GoodsInfo> storeGoodsInfos = goodsInfoRepository.findByGoodsIdIn(goodIds);
        storeGoodsInfos.forEach(goodsInfo -> {
            storeMapSupplyPrice.put(goodsInfo.getGoodsInfoId(), goodsInfo.getProviderGoodsInfoId());
        });

        //商品库skuId->供应商goodsInfoId
        HashMap<String, String> standardMapSupplyPrice = new HashMap<>();
        List<StandardGoodsRel> standardGoodsRels = standardGoodsRelRepository.findByGoodsIds(goodIds);
        List<String> standardGoodsIds = standardGoodsRels.stream().map(StandardGoodsRel::getStandardId).collect(Collectors.toList());
        List<StandardSku> standardGoodsInfos = standardSkuRepository.findByGoodsIdIn(standardGoodsIds);
        standardGoodsInfos.forEach(standardSku -> {
            standardMapSupplyPrice.put(standardSku.getGoodsId(), standardSku.getProviderGoodsInfoId());
        });

        //更新商家商品supplyPrice 和stock
        storeGoodsInfos.forEach(goodsInfo -> {
            goodsInfo.setSupplyPrice(providerMapSupplyPrice.get(storeMapSupplyPrice.get(goodsInfo.getGoodsInfoId())));
            goodsInfo.setStock(providerMapStock.get(storeMapSupplyPrice.get(goodsInfo.getGoodsInfoId())));
        });
        goodsInfoRepository.saveAll(storeGoodsInfos);

        //更新商品库suppliPrice 和sotck
        standardGoodsInfos.forEach(standardSku -> {
            standardSku.setSupplyPrice(providerMapSupplyPrice.get(standardMapSupplyPrice.get(standardSku.getGoodsInfoId())));
            standardSku.setStock(providerMapStock.get(standardMapSupplyPrice.get(standardSku.getGoodsInfoId())));
        });
        standardSkuRepository.saveAll(standardGoodsInfos);
    }

    @Transactional
    public List<String> synDeleteStoreGoodsInfoAndStandardSku(List<String> delInfoIds) {
        if (delInfoIds != null) {
            List<GoodsInfo> byProviderGoodsInfoIdIn = goodsInfoRepository.findByProviderGoodsInfoIdIn(delInfoIds);
            goodsInfoRepository.deleteByProviderGoodsInfoId(delInfoIds);
            standardSkuRepository.deleteByGoodsIds(delInfoIds);
            return byProviderGoodsInfoIdIn.stream().map(GoodsInfo::getGoodsInfoId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 修改所有商家三方渠道商品可售状态
     *
     * @param request
     */
    @Transactional
    public void vendibilityLinkedmallGoods(ThirdGoodsVendibilityRequest request) {
        goodsRepository.vendibilityLinkedmallGoods(request.getVendibility(), request.getThirdPlatformType());
        goodsInfoRepository.vendibilityLinkedmallGoodsInfos(request.getVendibility(), request.getThirdPlatformType());
    }

    /**
     * 供应商关联商品是否可售
     * 1、 禁售、删除、spu的上下架都需要同时更改spu和sku
     * 2、编辑sku时，spu的上下架场景：
     * 下架 -> 上架    修改spu和所有sku
     * 下架 -> 部分上架（上架部分商品）  修改当前sku和spu
     * 上架 -> 下架    修改所有sku和spu
     * 上架 -> 部分上架（下架部分商品）  只修改sku
     * 部分上架 -> 上架  修改当前sku
     * 部分上架 -> 下架  修改当前sku和spu
     * 部分上架 -> 部分上架 修改当前sku
     */
//    @Transactional
//    public void dealGoodsVendibility(ProviderGoodsNotSellRequest request) {
//
//        List<String> goodsInfoIds = new ArrayList<>();
//        //处理spu
//        if (CollectionUtils.isNotEmpty(request.getGoodsIds())) {
//            List<Goods> goods = goodsRepository.findAllByGoodsIdIn(request.getGoodsIds());
//            if (CollectionUtils.isNotEmpty(goods)) {
//                //上架、禁售后重新编辑的时候要综合情况看
//                if (Boolean.TRUE.equals(request.getCheckFlag())) {
//                    goods.stream().forEach(g -> {
//                        Integer goodsVendibility = Constants.no;
//                        //未下架、未删除、已审核视为商品可售（商品维度）
//                        if (AddedFlag.NO.toValue() != g.getAddedFlag()
//                                && DeleteFlag.NO.equals(g.getDelFlag())
//                                && CheckStatus.CHECKED.equals(g.getAuditStatus()))
//                            goodsVendibility = Constants.yes;
//                        goodsRepository.updateGoodsVendibility(goodsVendibility, Lists.newArrayList(g.getGoodsId()));
//                    });
//                } else {
//                    goodsRepository.updateGoodsVendibility(Constants.no, request.getGoodsIds());
//                }
//
//                //同步库存
//                if (Boolean.TRUE.equals(request.getStockFlag())) {
//                    goods.forEach(g -> goodsRepository.updateStockByProviderGoodsIds(g.getStock(), Lists.newArrayList(g.getGoodsId())));
//                }
//            }
//
//            List<GoodsInfo> goodsInfos = goodsInfoRepository.findByGoodsIdIn(request.getGoodsIds());
//            if (CollectionUtils.isNotEmpty(goodsInfos) && CollectionUtils.isEmpty(request.getGoodsInfoIds())) {
//                goodsInfoIds = goodsInfos.stream().map(GoodsInfo::getGoodsInfoId).collect(Collectors.toList());
//            }
//        }
//
//        //传入goodsInfoIds时，说明指定修改sku
//        if (CollectionUtils.isNotEmpty(request.getGoodsInfoIds())) {
//            goodsInfoIds = request.getGoodsInfoIds();
//        }
//
//        //处理sku
//        List<GoodsInfo> goodsInfos = goodsInfoRepository.findByGoodsInfoIds(goodsInfoIds);
//        if (CollectionUtils.isNotEmpty(goodsInfos)) {
//            if (Boolean.TRUE.equals(request.getCheckFlag())) {
//                goodsInfos.stream().forEach(g -> {
//                    Integer goodsVendibility = Constants.no;
//                    //上架、未删除、已审核视为商品可售（商品维度）
//                    if (AddedFlag.YES.toValue() == g.getAddedFlag()
//                            && DeleteFlag.NO.equals(g.getDelFlag())
//                            && CheckStatus.CHECKED.equals(g.getAuditStatus()))
//                        goodsVendibility = Constants.yes;
//                    goodsInfoRepository.updateGoodsInfoVendibility(goodsVendibility, Lists.newArrayList(g.getGoodsInfoId()));
//                });
//            } else {
//                goodsInfoRepository.updateGoodsInfoVendibility(Constants.no, goodsInfoIds);
//            }
//        }
//    }

    /**
     * 更改商家代销商品的供应商店铺状态
     *
     * @param storeIds
     */
    @Transactional
    public void updateProviderStatus(Integer providerStatus, List<Long> storeIds) {
        goodsRepository.updateProviderStatus(providerStatus, storeIds);
        goodsInfoRepository.updateProviderStatus(providerStatus, storeIds);
    }

    /**
     * 增加商品评论数
     *
     * @param goodsId
     */
    @Transactional
    public void increaseGoodsEvaluateNum(String goodsId) {
        goodsRepository.increaseGoodsEvaluateNum(goodsId);
    }

    /**
     * 减少商品评论数
     *
     * @param goodsId
     */
    @Transactional
    public void decreaseGoodsEvaluateNum(String goodsId) {
        goodsRepository.decreaseGoodsEvaluateNum(goodsId);
    }


    /**
     * 根据goodsId查询评论总数
     *
     * @param goodsId
     * @return
     */
    public Long getGoodsEvaluateNumByGoodsId(String goodsId) {
        return goodsRepository.getGoodsEvaluateNumByGoodsId(goodsId);
    }


    /**
     * @param
     * @return
     * @discription 查询商品sku信息
     * @author weiwenhao
     * @date 2021/3/20 11:17
     */
    public List<GoodsInfo> findGoodsInfo(List<String> skuIds) {
        List<GoodsInfo> goodsInfo = goodsInfoRepository.findByGoodsInfoIds(skuIds);
        return goodsInfo;
    }

    /**
     * 查询erp编码的数量
     * @return
     */
    public void findExistsErpGoodsInfoNo(String erpGoodsNo, List<String> erpGoodsInfoNos){
        List<String> erpSkuNos = goodsInfoRepository.findExistsErpGoodsInfoNo(erpGoodsInfoNos);
        List<String> erpSpuNos = goodsRepository.findExistsErpGoodsNo(erpGoodsNo);
        if(erpSpuNos.size() > 0) {
            throw new SbcRuntimeException(GoodsErrorCode.ERP_SPU_NO_H_EXIST, new Object[]{StringUtils.join(erpSpuNos, ",")});
        }
        if(erpSkuNos.size() > 0) {
            throw new SbcRuntimeException(GoodsErrorCode.ERP_SKU_NO_H_EXIST, new Object[]{StringUtils.join(erpSkuNos, ",")});
        }
    }


    /**
     * 根据spu编号查询
     */
    public Goods findByGoodsId(String goodsId){
        return goodsRepository.findByGoodsIdAndDelFlag(goodsId, DeleteFlag.NO);
    }

    public void save(Goods goods){
        goodsRepository.save(goods);
    }

    /**
     * 为书籍设置历史属性数据
     */
    @Transactional
    public List<Object[]> setExtPropForGoods(List<Object[]> props) {
        List<String> goodsNumbers = new ArrayList<>();
        for (Object[] prop : props) {
            goodsNumbers.add((String) prop[0]);
        }
        List<Object[]> goodsParam = goodsRepository.findIdByNumber(goodsNumbers);
        List<Object[]> propList = new ArrayList<>();
        List<String> goodsIds = new ArrayList();
        for (Object[] prop : props) {
            for (Object[] param : goodsParam) {
                if(prop[0].equals(param[1])){
                    propList.add(new Object[]{param[0], prop[1], prop[2], prop[3], prop[4], prop[5]});
                    goodsIds.add((String) param[0]);
                    break;
                }
            }
        }
        List<GoodsPropDetailRel> propRels = goodsPropDetailRelRepository.findByGoodsIds(goodsIds);
        Map<String, List<GoodsPropDetailRel>> goodsProp = propRels.stream().collect(Collectors.groupingBy(GoodsPropDetailRel::getGoodsId));

        GoodsProp authorProp = goodsPropRepository.findByPropName("作者");
        GoodsProp publisherProp = goodsPropRepository.findByPropName("出版社");
        GoodsProp scoreProp = goodsPropRepository.findByPropName("评分");
        GoodsProp priceProp = goodsPropRepository.findByPropName("定价");
        GoodsProp isbnProp = goodsPropRepository.findByPropName("ISBN");

        List<GoodsPropDetailRel> createList = new ArrayList<>();
        for (Object[] prop : propList) {
            String goodsId = (String) prop[0];
            String author = (String) prop[1];
            String publisher = (String) prop[2];
            double price = (double) prop[3];
            double score = (double) prop[4];
            String isbn = (String) prop[5];
            LocalDateTime now = LocalDateTime.now();
            List<GoodsPropDetailRel> goodsPropDetailRels = goodsProp.get(goodsId);
            boolean authorFlag = false;
            boolean publisherFlag = false;
            boolean scoreFlag = false;
            boolean priceFlag = false;
            boolean isbnFlag = false;
            //已有的更新
            if(CollectionUtils.isNotEmpty(goodsPropDetailRels)){
                for (GoodsPropDetailRel goodsPropDetailRel : goodsPropDetailRels) {
                    if(authorProp != null && authorProp.getPropId().equals(goodsPropDetailRel.getPropId())){
                        goodsPropDetailRel.setPropValue(author);
                        authorFlag = true;
                    }else if(publisherProp != null && publisherProp.getPropId().equals(goodsPropDetailRel.getPropId())){
                        goodsPropDetailRel.setPropValue(publisher);
                        publisherFlag = true;
                    }else if(scoreProp != null && scoreProp.getPropId().equals(goodsPropDetailRel.getPropId())){
                        goodsPropDetailRel.setPropValue(score + "");
                        scoreFlag = true;
                    }else if(priceProp != null && priceProp.getPropId().equals(goodsPropDetailRel.getPropId())){
                        goodsPropDetailRel.setPropValue(price + "");
                        priceFlag = true;
                    }else if(isbnProp != null && isbnProp.getPropId().equals(goodsPropDetailRel.getPropId())){
                        goodsPropDetailRel.setPropValue(isbn);
                        isbnFlag = true;
                    }
                }
            }
            //没有的新建
            if(authorProp != null && !authorFlag){
                GoodsPropDetailRel rel = createRel(createList, goodsId, now);
                rel.setPropId(authorProp.getPropId());
                rel.setPropValue(author);
            }
            if(publisherProp != null && !publisherFlag){
                GoodsPropDetailRel rel = createRel(createList, goodsId, now);
                rel.setPropId(publisherProp.getPropId());
                rel.setPropValue(publisher);
            }
            if(scoreProp != null && !scoreFlag){
                GoodsPropDetailRel rel = createRel(createList, goodsId, now);
                rel.setPropId(scoreProp.getPropId());
                rel.setPropValue(score + "");
            }
            if(priceProp != null && !priceFlag){
                GoodsPropDetailRel rel = createRel(createList, goodsId, now);
                rel.setPropId(priceProp.getPropId());
                rel.setPropValue(price + "");
            }
            if(isbnProp != null && !isbnFlag){
                GoodsPropDetailRel rel = createRel(createList, goodsId, now);
                rel.setPropId(isbnProp.getPropId());
                rel.setPropValue(isbn);
            }
        }
        goodsPropDetailRelRepository.saveAll(propRels);
        goodsPropDetailRelRepository.saveAll(createList);
        return propList;
    }

    private GoodsPropDetailRel createRel(List<GoodsPropDetailRel> rels, String goodsId, LocalDateTime now){
        GoodsPropDetailRel rel = new GoodsPropDetailRel();
        rels.add(rel);
        rel.setUpdateTime(now);
        rel.setCreateTime(now);
        rel.setDelFlag(DeleteFlag.NO);
        rel.setGoodsId(goodsId);
        rel.setDetailId(0L);
        return rel;
    }

    public List<GoodsSync> listGoodsSync(){
        GoodsSyncQueryRequest request = new GoodsSyncQueryRequest();
        request.setStatus(2);
        request.setPageSize(100);
        Page<GoodsSync> list = goodsSyncRepository.findAll(request.getWhereCriteria(),request.getPageRequest());
        if(list!=null && CollectionUtils.isNotEmpty(list.getContent())){
            return list.getContent();
        }
        return new ArrayList<>();
    }

    public List<GoodsCateSync> listGoodsCateSync(){
        return goodsCateSyncRepository.query();
    }

    public String getGoodsId(List<String> goodsInfoIds){
        if(CollectionUtils.isEmpty(goodsInfoIds)) {
            Map goods = goodsRepository.findSpuId();
            if(goods != null){
                log.info("map content : \n");
                goods.forEach((k, v) -> {
                    log.info("key: " + k + ", value: " + v);
                });
                return goods.get("goods_id").toString();
            }
            return "";
        }else {
            Map goods = goodsRepository.findSpuId2(goodsInfoIds);
            if(goods != null){
                log.info("map content : \n");
                goods.forEach((k, v) -> {
                    log.info("key: " + k + ", value: " + v);
                });
                return goods.get("goods_id").toString();
            }
            return "";
        }
    }

    public String getByClassidyId(Integer classifyId){
        Map goods = goodsRepository.findFirstByClassify(classifyId);
        if(goods != null){
            log.info("map content : \n");
            goods.forEach((k, v) -> {
                log.info("key: " + k + ", value: " + v);
            });
            return goods.get("goods_id").toString();
        }
        return "";
    }

    @Transactional
    public void syncGoodsVoteNumber(){
        Map<String, String> voteCacheMap = redisService.hgetAll(RedisKeyConstant.KEY_GOODS_VOTE_NUMBER);
        if(voteCacheMap != null) {
            List<GoodsVote> voteAll = goodsVoteRepository.findAll();
            Map<String, List<GoodsVote>> voteMap = voteAll.stream().collect(Collectors.groupingBy(GoodsVote::getGoodsId));
            List<GoodsVote> goodsVotes = new ArrayList<>();
            voteCacheMap.forEach((k, v) -> {
                LocalDateTime now = LocalDateTime.now();
                GoodsVote goodsVote;
                if(voteMap != null && !voteMap.isEmpty() && voteMap.containsKey(k)){
                    goodsVote = voteMap.get(k).get(0);
                } else {
                    goodsVote = new GoodsVote();
                    goodsVote.setCreateTime(now);
                    goodsVote.setGoodsId(k);
                    goodsVote.setDelFlag(DeleteFlag.NO);
                }
                goodsVote.setVoteNumber(Long.parseLong(v));
                goodsVote.setUpdateTime(now);
                goodsVotes.add(goodsVote);
            });
            goodsVoteRepository.saveAll(goodsVotes);
        }
    }


    /**
     * 更新erpGoodsNo
     * @param erpGoodsColl
     * @return
     */
    @Transactional
    public List<String> updateGoodsErpGoodsNo(Collection<GoodsUpdateProviderRequest> erpGoodsColl) {
        List<String> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(erpGoodsColl)) {
            log.info("update goods erpGoodsNoColl is empty");
            return result;
        }

        long beginTime = System.currentTimeMillis();
        log.info("update goods beginTime: {}", beginTime);
        List<String> goodsIdList = erpGoodsColl.stream().map(GoodsUpdateProviderRequest::getGoodsId).collect(Collectors.toList());


        if (!CollectionUtils.isEmpty(goodsIdList)) {
            List<Goods> goodsList = goodsRepository.findAll(this.packageWhere(goodsIdList, null));
            if (CollectionUtils.isEmpty(goodsList)) {
                log.info("update goods goodsList is empty");
                return goodsIdList;
            }


            Map<String, Goods> goodsId2Map = goodsList.stream().collect(Collectors.toMap(Goods::getGoodsId, Function.identity(), (k1, k2) -> k1));
            int goodsCount = 0;
            for (GoodsUpdateProviderRequest goodsUpdateProviderRequest : erpGoodsColl) {
                Goods goodsParam = goodsId2Map.get(goodsUpdateProviderRequest.getGoodsId());
                if (goodsParam == null) {
                    log.info("update goods request body: {} 没有查询到商品信息", JSON.toJSONString(goodsUpdateProviderRequest));
                    continue;
                }
                goodsParam.setErpGoodsNo(goodsUpdateProviderRequest.getErpGoodsNoNew());
                goodsRepository.save(goodsParam);
                goodsCount++;
                goodsIdList.add(goodsParam.getGoodsId());
                log.info("update goods goodsId:{} requestBody: {} complete {} times", goodsParam.getGoodsId(), JSON.toJSONString(goodsUpdateProviderRequest), goodsCount);
            }
        }

        //goodsInfo
        List<String> goodsInfoIdList = erpGoodsColl.stream().map(GoodsUpdateProviderRequest::getGoodsInfoId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(goodsInfoIdList)) {
            List<GoodsInfo> goodsInfoList = goodsInfoRepository.findByGoodsInfoIds(goodsInfoIdList);
            Map<String, GoodsInfo> goodsInfoId2Map = goodsInfoList.stream().collect(Collectors.toMap(GoodsInfo::getGoodsInfoId, Function.identity(), (k1, k2) -> k1));
            int goodsInfoCount = 0;
            for (GoodsUpdateProviderRequest goodsUpdateProviderRequest : erpGoodsColl) {
                GoodsInfo goodsInfoParam = goodsInfoId2Map.get(goodsUpdateProviderRequest.getGoodsInfoId());
                if (goodsInfoParam == null) {
                    log.info("update goodsInfo request body: {} 没有查询到商品信息", JSON.toJSONString(goodsUpdateProviderRequest));
                    continue;
                }
                goodsInfoParam.setErpGoodsNo(goodsUpdateProviderRequest.getErpGoodsNoNew());
                goodsInfoParam.setErpGoodsInfoNo(goodsUpdateProviderRequest.getErpGoodsInfoNoNew());
                goodsInfoRepository.save(goodsInfoParam);
                goodsInfoCount++;
                log.info("update goodsInfo goodsId:{} requestBody: {} complete {} times", goodsInfoParam.getGoodsInfoId(), JSON.toJSONString(goodsUpdateProviderRequest), goodsInfoCount);
            }
        }

        log.info("update goods end total : {} ms", System.currentTimeMillis() - beginTime);
        return goodsIdList;
    }


    private Specification<Goods> packageWhere(Collection<String> goodsIdColl, Collection<String> goodsNoColl) {
        return new Specification<Goods>() {
            @Override
            public Predicate toPredicate(Root<Goods> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                final List<Predicate> conditionList = new ArrayList<>();

                //只是获取有效的
                if (CollectionUtils.isNotEmpty(goodsIdColl)) {
                    conditionList.add(root.get("goodsId").in(goodsIdColl));
                }

                //只是获取有效的
                if (CollectionUtils.isNotEmpty(goodsNoColl)) {
                    conditionList.add(root.get("goodsNo").in(goodsNoColl));
                }
                return criteriaBuilder.and(conditionList.toArray(new Predicate[conditionList.size()]));
            }
        };
    }


    /**
     * 更新goods表信息
     * @param goodsDetas
     */
    @Transactional
    public void updateGoodsByCondition(List<GoodsDataUpdateRequest> goodsDetas) {
        if (CollectionUtils.isEmpty(goodsDetas)) {
            return;
        }
        List<String> goodsNos = goodsDetas.stream().map(GoodsDataUpdateRequest::getGoodsNo).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(goodsNos)) {
            return;
        }
        List<Goods> goodsList = goodsRepository.findAll(this.packageWhere(null, goodsNos));
        if (CollectionUtils.isEmpty(goodsList)) {
            return;
        }
        Map<String, GoodsDataUpdateRequest> goodsNo2GoodsParamMap =
                goodsDetas.stream().collect(Collectors.toMap(GoodsDataUpdateRequest::getGoodsNo, Function.identity(), (k1,k2) -> k1));

        List<Goods> saveGoods = new ArrayList<>();
        for (Goods goods : goodsList) {
            GoodsDataUpdateRequest goodsDataUpdateRequestTmp = goodsNo2GoodsParamMap.get(goods.getGoodsNo());
            if (goodsDataUpdateRequestTmp == null) {
                continue;
            }
            if (!StringUtils.isBlank(goodsDataUpdateRequestTmp.getUnbackgroundImg())) {
                goods.setUpdateTime(LocalDateTime.now());
                goods.setGoodsUnBackImg(goodsDataUpdateRequestTmp.getUnbackgroundImg());
            }
            saveGoods.add(goods);
        }

        goodsRepository.saveAll(saveGoods);
    }

    /**
     * 通过spu或者sku取redis获取商详信息
     * @param spuId、skuId
     * @return
     */
    public Map getGoodsDetialById(String spuId, String skuId,String redisTagsConstant) {

        String old_json=null;
        //优先用spuId去取
        if(null!=spuId && spuId.isEmpty()==false){
            old_json = redisService.getString(redisTagsConstant + ":" + spuId);
        } else{
            //spuId为空则通过skuId获取spuId
            if(null == skuId || skuId.isEmpty()){
                return null;
            }else {
                Map<String, String> goodsInfoMap = goodsInfoService.goodsInfoBySkuId(skuId);
                spuId=goodsInfoMap.get("spuId");
                old_json = redisService.getString(redisTagsConstant + ":" + spuId);
            }
        }

        if(null==old_json || old_json.isEmpty()){
            return null;//不去数据库再找了
        }else {

            Map map=JSONObject.parseObject(old_json,Map.class);

            return map;
        }
    }
}
