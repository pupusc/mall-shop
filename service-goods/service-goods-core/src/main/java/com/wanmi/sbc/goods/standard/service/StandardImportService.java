package com.wanmi.sbc.goods.standard.service;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.constant.GoodsCateErrorCode;
import com.wanmi.sbc.goods.api.constant.GoodsImportErrorCode;
import com.wanmi.sbc.goods.api.constant.StandardGoodsErrorCode;
import com.wanmi.sbc.goods.bean.enums.*;
import com.wanmi.sbc.goods.brand.model.root.GoodsBrand;
import com.wanmi.sbc.goods.cate.model.root.GoodsCate;
import com.wanmi.sbc.goods.cate.service.GoodsCateService;
import com.wanmi.sbc.goods.common.GoodsCommonService;
import com.wanmi.sbc.goods.freight.model.root.FreightTemplateGoods;
import com.wanmi.sbc.goods.freight.repository.FreightTemplateGoodsRepository;
import com.wanmi.sbc.goods.images.GoodsImage;
import com.wanmi.sbc.goods.images.GoodsImageRepository;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsPropDetailRel;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsPropDetailRelRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsRequest;
import com.wanmi.sbc.goods.info.service.GoodsService;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpec;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpecDetail;
import com.wanmi.sbc.goods.spec.repository.GoodsInfoSpecDetailRelRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecDetailRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecRepository;
import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.standard.model.root.StandardGoodsRel;
import com.wanmi.sbc.goods.standard.model.root.StandardPropDetailRel;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRelRepository;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRepository;
import com.wanmi.sbc.goods.standard.repository.StandardPropDetailRelRepository;
import com.wanmi.sbc.goods.standard.repository.StandardSkuRepository;
import com.wanmi.sbc.goods.standard.request.StandardImportRequest;
import com.wanmi.sbc.goods.standard.request.StandardQueryRequest;
import com.wanmi.sbc.goods.standard.request.StandardSkuQueryRequest;
import com.wanmi.sbc.goods.standardimages.model.root.StandardImage;
import com.wanmi.sbc.goods.standardimages.repository.StandardImageRepository;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSkuSpecDetailRel;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpec;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpecDetail;
import com.wanmi.sbc.goods.standardspec.repository.StandardSkuSpecDetailRelRepository;
import com.wanmi.sbc.goods.standardspec.repository.StandardSpecDetailRepository;
import com.wanmi.sbc.goods.standardspec.repository.StandardSpecRepository;
import com.wanmi.sbc.goods.storecate.model.root.StoreCate;
import com.wanmi.sbc.goods.storecate.model.root.StoreCateGoodsRela;
import com.wanmi.sbc.goods.storecate.repository.StoreCateGoodsRelaRepository;
import com.wanmi.sbc.goods.storecate.repository.StoreCateRepository;
import com.wanmi.sbc.goods.storecate.request.StoreCateQueryRequest;
import io.seata.common.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品库服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
@Transactional(readOnly = false)
public class StandardImportService {

    @Autowired
    private StandardGoodsRepository standardGoodsRepository;

    @Autowired
    private StandardSkuRepository standardSkuRepository;

    @Autowired
    private StandardSpecRepository standardSpecRepository;

    @Autowired
    private StandardSpecDetailRepository standardSpecDetailRepository;

    @Autowired
    private StandardSkuSpecDetailRelRepository standardSkuSpecDetailRelRepository;

    @Autowired
    private StandardImageRepository standardImageRepository;

    @Autowired
    private StandardPropDetailRelRepository standardPropDetailRelRepository;

    @Autowired
    private StandardGoodsRelRepository standardGoodsRelRepository;

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
    private GoodsPropDetailRelRepository goodsPropDetailRelRepository;

    @Autowired
    private StoreCateRepository storeCateRepository;

    @Autowired
    private StoreCateGoodsRelaRepository storeCateGoodsRelaRepository;

    @Autowired
    private FreightTemplateGoodsRepository freightTemplateGoodsRepository;

    @Autowired
    private GoodsCateService goodsCateService;

    @Autowired
    private GoodsCommonService goodsCommonService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    /**
     * 商品库批量导入商品
     *
     * @param request 参数
     * @return
     */
    @Transactional
    public List<String> importGoods(StandardImportRequest request) {
        Boolean delFlag = standardGoodsRepository.findAllById(request.getGoodsIds()).stream().anyMatch(s -> DeleteFlag.YES == s.getDelFlag());
        if (delFlag) {
            throw new SbcRuntimeException(StandardGoodsErrorCode.NOT_EXIST);
        }
        if (standardGoodsRelRepository.countByStandardAndStoreIds(request.getGoodsIds(), Collections.singletonList(request.getStoreId())) > 0) {
            throw new SbcRuntimeException(StandardGoodsErrorCode.COMPANY_IMPORT);
        }

        //店铺分类未配置
        List<StoreCate> storeCateList = storeCateRepository.findAll(
                StoreCateQueryRequest.builder()
                        .storeId(request.getStoreId())
                        .delFlag(DeleteFlag.NO)
                        .isDefault(DefaultFlag.YES).build().getWhereCriteria());
        if (CollectionUtils.isEmpty(storeCateList)) {
            throw new SbcRuntimeException(GoodsCateErrorCode.DEFAULT_CATE_NOT_EXIST);
        }

        Long defaultStoreCateId = storeCateList.get(0).getStoreCateId();

        //运费模板未配置
        FreightTemplateGoods freightTemp = freightTemplateGoodsRepository.queryByDefault(request.getStoreId());
        if (freightTemp == null) {
            throw new SbcRuntimeException(GoodsImportErrorCode.NOT_SETTING);
        }

        List<StandardGoods> standardGoodses = standardGoodsRepository.findAll(StandardQueryRequest.builder().goodsIds(request.getGoodsIds()).build().getWhereCriteria());

        List<String> newSkuIds = new ArrayList<>();

        List<StandardGoodsRel> goodsRels = standardGoodsRelRepository.findByStandardIds(request.getGoodsIds());
        Map<String, String> mappingOldSpu = new HashMap<>();
        Map<String, Long> mappingOldStoreId = new HashMap<>();
        if (CollectionUtils.isNotEmpty(standardGoodses)) {
            //规格映射map,商品库Id -> 老商品Id
            goodsRels.forEach(goodsRel -> {
                mappingOldSpu.put(goodsRel.getStandardId() + ":" + goodsRel.getStoreId(), goodsRel.getGoodsId());
                mappingOldStoreId.put(goodsRel.getStandardId() + ":" + goodsRel.getStoreId(), goodsRel.getStoreId());
            });
        }

        Map<String, Long> cateTopMap = new HashMap<>();//spuId对应商品一级类目
        if(CollectionUtils.isNotEmpty(standardGoodses)){
            //规格映射Map,商品库Id -> 新商品Id
            Map<String, String> mappingSpu = new HashMap<>();
            //规格映射Map,商品库规格Id -> 新商品规格Id
            Map<Long, Long> mappingSpec = new HashMap<>();
            //规格映射Map,商品库规格值Id -> 新商品规格值Id
            Map<Long, Long> mappingDetail = new HashMap<>();
            //规格映射Map,商品库SkuId -> 新商品SkuId
            Map<String, String> mappingSku = new HashMap<>();
            //规格映射Map,商品库Id -> 商品来源
            Map<String, Integer> mappingGoodsSourse = new HashMap<>();

            LocalDateTime now = LocalDateTime.now();
            List<Goods> goodsList = new ArrayList<>();

            //导入Spu
            standardGoodses.forEach(standardGoods -> {
                Goods goods = new Goods();
                //PS:这里有个属性拷贝
                BeanUtils.copyProperties(standardGoods, goods);
                if (GoodsSource.LINKED_MALL.toValue() == standardGoods.getGoodsSource()) {
                    goods.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
                }
                goods.setGoodsId(null);
                goods.setAddedFlag(AddedFlag.NO.toValue());
                goods.setDelFlag(DeleteFlag.NO);
                goods.setCreateTime(now);
                goods.setUpdateTime(now);
                goods.setAddedTime(now);
                goods.setSubmitTime(now);
                goods.setAuditStatus(CheckStatus.CHECKED);
                goods.setCustomFlag(Constants.no);
                goods.setLevelDiscountFlag(Constants.no);
                goods.setCompanyInfoId(request.getCompanyInfoId());
                goods.setCompanyType(request.getCompanyType());
                goods.setStoreId(request.getStoreId());
                goods.setSupplierName(request.getSupplierName());
                goods.setGoodsNo(goodsCommonService.getSpuNoByUnique());
                goods.setStoreCateIds(Collections.singletonList(defaultStoreCateId));
                goods.setFreightTempId(freightTemp.getFreightTempId());
                goods.setPriceType(GoodsPriceType.MARKET.toValue());
                //默认销售类型 零售
                goods.setSaleType(SaleType.RETAIL.toValue());
                goods.setMarketPrice(standardGoods.getMarketPrice());

                goods.setGoodsVideo(standardGoods.getGoodsVideo());
                //商品来源，0供应商，1商家 导入后就是商家商品 以此来区分列表展示
                goods.setGoodsSource(1);
                if (standardGoods.getGoodsSource().equals(NumberUtils.INTEGER_ZERO) || Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(standardGoods.getGoodsSource())) {
                    goods.setSupplyPrice(standardGoods.getSupplyPrice());
                    goods.setProviderGoodsId(mappingOldSpu.get(standardGoods.getGoodsId() + ":" + standardGoods.getStoreId()));
                    goods.setProviderId(mappingOldStoreId.get(standardGoods.getGoodsId() + ":" + standardGoods.getStoreId()));
                    goods.setProviderName(standardGoods.getProviderName());
                    //商品可售性
                    if (standardGoods.getAddedFlag() != AddedFlag.NO.toValue()) {
                        goods.setVendibility(Constants.yes);
                    } else {
                        goods.setVendibility(Constants.no);
                    }
                    //店铺状态
                    goods.setProviderStatus(this.getProviderStatus(standardGoods.getStoreId()));
                    //供应商导入商品市场价默认 供应商供货价
                    goods.setMarketPrice(standardGoods.getSupplyPrice());
                }
                //允许独立设价
                goods.setAllowPriceSet(1);
                goods.setAddedTimingFlag(Boolean.FALSE);

                //初始化商品对应的数量（收藏、销量、评论数、好评数）
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
                goods.setGoodsBuyTypes("0,1");
                goods.setStock(0L);
                Goods newGoods = goodsRepository.save(goods);

                // 检查商品基本信息
                goodsService.checkBasic(goods);

                cateTopMap.put(newGoods.getGoodsId(), goods.getCateTopId());
                mappingSpu.put(standardGoods.getGoodsId(), newGoods.getGoodsId());
                mappingGoodsSourse.put(standardGoods.getGoodsId(), standardGoods.getGoodsSource());
                goodsList.add(newGoods);

                //关联商品与商品库
                StandardGoodsRel rel = new StandardGoodsRel();
                rel.setGoodsId(newGoods.getGoodsId());
                rel.setStandardId(standardGoods.getGoodsId());
                rel.setStoreId(request.getStoreId());
                rel.setDelFlag(DeleteFlag.NO);
                standardGoodsRelRepository.save(rel);
            });

            List<StandardGoods> spuList = standardGoodses;
            //导入Sku
            List<StandardSku> skus = standardSkuRepository.findAll(StandardSkuQueryRequest.builder().goodsIds(request.getGoodsIds()).delFlag(DeleteFlag.NO.toValue()).build().getWhereCriteria());
            List<GoodsInfo> goodsInfos = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(skus)) {
                skus.forEach(standardSku -> {
                    GoodsInfo sku = new GoodsInfo();
                    //PS:这里有个属性拷贝
                    BeanUtils.copyProperties(standardSku, sku);
                    if (Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(standardSku.getGoodsSource())) {
                        sku.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
                    }
                    sku.setGoodsInfoId(null);
                    sku.setGoodsId(mappingSpu.get(standardSku.getGoodsId()));
                    sku.setGoodsSource(1);
                    sku.setCateTopId(cateTopMap.get(sku.getGoodsId()));
                    sku.setCreateTime(now);
                    sku.setUpdateTime(now);
                    sku.setAddedTime(now);
                    sku.setAddedFlag(AddedFlag.NO.toValue());
                    sku.setCompanyInfoId(request.getCompanyInfoId());
                    sku.setLevelDiscountFlag(Constants.no);
                    sku.setCustomFlag(Constants.no);
                    sku.setStoreId(request.getStoreId());
                    sku.setAuditStatus(CheckStatus.CHECKED);
                    sku.setCompanyType(request.getCompanyType());
                    sku.setGoodsInfoNo(goodsCommonService.getSkuNoByUnique());
                    sku.setStock(standardSku.getStock() == null ? 0 : standardSku.getStock());
                    sku.setAloneFlag(Boolean.FALSE);
                    sku.setSupplyPrice(standardSku.getSupplyPrice());
                    sku.setMarketPrice(standardSku.getMarketPrice());
                    if (NumberUtils.INTEGER_ZERO.equals(standardSku.getGoodsSource()) || Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(standardSku.getGoodsSource())) {
                        //供应商导入商品市场价默认 供应商供货价
                        sku.setMarketPrice(standardSku.getSupplyPrice());
                    }
                    sku.setAddedTimingFlag(Boolean.FALSE);
                    String barCode = StringUtils.EMPTY;
                    if (StringUtils.isNotBlank(standardSku.getProviderGoodsInfoId())) {
                        //供应商商品允许批量设价
                        sku.setAloneFlag(Boolean.TRUE);
                        GoodsInfo providerGoodsInfo = goodsInfoRepository.findById(standardSku.getProviderGoodsInfoId()).orElse(null);
                        if (providerGoodsInfo != null) {
                            barCode = providerGoodsInfo.getGoodsInfoBarcode();
                            sku.setGoodsInfoBarcode(barCode);
                        }
                    }


                    Optional<StandardGoods> standardGoods = spuList.stream().filter(goodses -> goodses.getGoodsId().equals(standardSku.getGoodsId())).findFirst();
                    sku.setCateId(standardGoods.get().getCateId());
                    sku.setBrandId(standardGoods.get().getBrandId());
                    sku.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                    //默认销售类型 零售
                    sku.setSaleType(SaleType.RETAIL.toValue());

                    if (NumberUtils.INTEGER_ZERO.equals(standardSku.getGoodsSource()) || Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(standardSku.getGoodsSource())) {
                        sku.setProviderId(mappingOldStoreId.get(standardSku.getGoodsId() + ":" + standardGoods.get().getStoreId()));
                    }


                    if (NumberUtils.INTEGER_ZERO.equals(standardSku.getGoodsSource()) || Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(standardSku.getGoodsSource())) {
                        //商品可售性
                        if (!Integer.valueOf(AddedFlag.NO.toValue()).equals(standardSku.getAddedFlag())) {
                            sku.setVendibility(Constants.yes);
                        } else {
                            sku.setVendibility(Constants.no);
                        }
                        //店铺状态
                        if(standardGoods.get().getStoreId() != null) {
                            sku.setProviderStatus(this.getProviderStatus(standardGoods.get().getStoreId()));
                        }
                    }
                    GoodsInfo newSku = goodsInfoRepository.save(sku);
                    goodsInfos.add(newSku);
                    newSkuIds.add(newSku.getGoodsInfoId());
                    mappingSku.put(standardSku.getGoodsInfoId(), newSku.getGoodsInfoId());
                });

                //更新SPU库存
                goodsList.forEach(spu -> {
                    spu.setStock(goodsInfos.stream().filter(s -> Objects.nonNull(s.getStock())).mapToLong(GoodsInfo::getStock).sum());
                });
            }



            //导入规格
            List<StandardSpec> specs = standardSpecRepository.findByGoodsIds(request.getGoodsIds());
            if (CollectionUtils.isNotEmpty(specs)) {
                specs.forEach(standardSpec -> {
                    GoodsSpec spec = new GoodsSpec();
                    BeanUtils.copyProperties(standardSpec, spec);
                    spec.setSpecId(null);
                    spec.setGoodsId(mappingSpu.get(standardSpec.getGoodsId()));
                    mappingSpec.put(standardSpec.getSpecId(), goodsSpecRepository.save(spec).getSpecId());
                });
            }

            //导入规格值
            List<StandardSpecDetail> details = standardSpecDetailRepository.findByGoodsIds(request.getGoodsIds());
            if (CollectionUtils.isNotEmpty(details)) {
                details.forEach(specDetail -> {
                    GoodsSpecDetail detail = new GoodsSpecDetail();
                    BeanUtils.copyProperties(specDetail, detail);
                    detail.setSpecDetailId(null);
                    detail.setSpecId(mappingSpec.get(specDetail.getSpecId()));
                    detail.setGoodsId(mappingSpu.get(specDetail.getGoodsId()));
                    mappingDetail.put(specDetail.getSpecDetailId(), goodsSpecDetailRepository.save(detail).getSpecDetailId());
                });
            }

            //导入规格值与Sku的关系
            List<StandardSkuSpecDetailRel> rels = standardSkuSpecDetailRelRepository.findByGoodsIds(request.getGoodsIds());
            if (CollectionUtils.isNotEmpty(rels)) {
                rels.forEach(standardRel -> {
                    GoodsInfoSpecDetailRel rel = new GoodsInfoSpecDetailRel();
                    BeanUtils.copyProperties(standardRel, rel);
                    rel.setSpecDetailRelId(null);
                    rel.setSpecId(mappingSpec.get(standardRel.getSpecId()));
                    rel.setSpecDetailId(mappingDetail.get(standardRel.getSpecDetailId()));
                    rel.setGoodsInfoId(mappingSku.get(standardRel.getGoodsInfoId()));
                    rel.setGoodsId(mappingSpu.get(standardRel.getGoodsId()));
                    goodsInfoSpecDetailRelRepository.save(rel);
                });
            }

            //导入图片
            List<StandardImage> imageList = standardImageRepository.findByGoodsIds(request.getGoodsIds());
            if (CollectionUtils.isNotEmpty(imageList)) {
                imageList.forEach(standardImage -> {
                    GoodsImage image = new GoodsImage();
                    BeanUtils.copyProperties(standardImage, image);
                    image.setImageId(null);
                    image.setGoodsId(mappingSpu.get(standardImage.getGoodsId()));
                    goodsImageRepository.save(image);
                });
            }

            //导入商品属性
            List<StandardPropDetailRel> propDetailRelList = standardPropDetailRelRepository.findByGoodsIds(request.getGoodsIds());
            if (CollectionUtils.isNotEmpty(propDetailRelList)) {
                propDetailRelList.forEach(goodsPropDetailRel -> {
                    GoodsPropDetailRel rel = new GoodsPropDetailRel();
                    BeanUtils.copyProperties(goodsPropDetailRel, rel);
                    rel.setRelId(null);
                    rel.setGoodsId(mappingSpu.get(goodsPropDetailRel.getGoodsId()));
                    goodsPropDetailRelRepository.save(rel);
                });
            }

            mappingSpu.values().forEach(spuId -> {
                StoreCateGoodsRela rela = new StoreCateGoodsRela();
                rela.setGoodsId(spuId);
                rela.setStoreCateId(defaultStoreCateId);
                storeCateGoodsRelaRepository.save(rela);
            });
        }
        return newSkuIds;
    }

    /**
     * 商品批量导入商品库
     *
     * @param request 参数
     * @return
     */
    @Transactional
    public List<String> importStandard(GoodsRequest request) {
        if (standardGoodsRelRepository.countByGoodsIds(request.getGoodsIds()) > 0) {
            throw new SbcRuntimeException(StandardGoodsErrorCode.COMPANY_IMPORT);
        }

        List<Goods> goodsesAll = goodsRepository.findAll(GoodsQueryRequest.builder().goodsIds(request.getGoodsIds())
                .build().getWhereCriteria());
        List<StandardGoodsRel> reImportGoodses = standardGoodsRelRepository.findByDelFlagAndGoodsIdIn(DeleteFlag.YES, request.getGoodsIds());
        List<String> importedGoodsId = reImportGoodses.stream().map(StandardGoodsRel::getGoodsId).collect(Collectors.toList());
        List<Goods> importGoodses = new ArrayList<>();
        for (Goods goods : goodsesAll) {
            if (!importedGoodsId.contains(goods.getGoodsId())) {
                importGoodses.add(goods);
            }
        }

        reImportStandardGoods(reImportGoodses);
        List<String> standardIds = reImportGoodses.stream().map(StandardGoodsRel::getStandardId).distinct().collect(Collectors.toList());
        standardIds.addAll(importStandardGoods(request, importGoodses));
        return standardIds;
    }

    @Transactional
    public void reImportStandardGoods(List<StandardGoodsRel> reImportGoodses) {
        for (StandardGoodsRel reImportGoods : reImportGoodses) {
            synProviderGoods(reImportGoods.getGoodsId(), reImportGoods.getStandardId());
        }
    }


    @Transactional
    public List<String> importStandardGoods(GoodsRequest request, List<Goods> importGoodses) {
        List<String> ids = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(importGoodses)) {
            //规格映射Map,商品Id -> 新商品库Id
            Map<String, String> mappingSpu = new HashMap<>();
            //规格映射Map,商品规格Id -> 新商品库规格Id
            Map<Long, Long> mappingSpec = new HashMap<>();
            //规格映射Map,商品规格值Id -> 新商品库规格值Id
            Map<Long, Long> mappingDetail = new HashMap<>();
            //规格映射Map,商品SkuId -> 新商品库SkuId
            Map<String, String> mappingSku = new HashMap<>();
            LocalDateTime now = LocalDateTime.now();

            //导入Spu
            importGoodses.forEach(goods -> {
                StandardGoods standardGoods = new StandardGoods();
                BeanUtils.copyProperties(goods, standardGoods);
                //商家用供应商
                if (Integer.valueOf(GoodsSource.SELLER.toValue()).equals(goods.getGoodsSource())) {
                    standardGoods.setProviderName(goods.getProviderName());
                }else{
                    standardGoods.setProviderName(goods.getSupplierName());
                }
                standardGoods.setGoodsId(null);
                standardGoods.setDelFlag(DeleteFlag.NO);
                standardGoods.setCreateTime(now);
                standardGoods.setUpdateTime(now);
                standardGoods.setStoreId(goods.getStoreId());
                standardGoods.setGoodsNo(goods.getGoodsNo());
                String newGoodsId = standardGoodsRepository.save(standardGoods).getGoodsId();
                mappingSpu.put(goods.getGoodsId(), newGoodsId);
                ids.add(newGoodsId);

                //关联商品与商品库
                StandardGoodsRel rel = new StandardGoodsRel();
                rel.setGoodsId(goods.getGoodsId());
                rel.setStandardId(newGoodsId);
                rel.setStoreId(goods.getStoreId());
                rel.setDelFlag(DeleteFlag.NO);
                standardGoodsRelRepository.save(rel);
            });

            //导入Sku
            List<GoodsInfo> skus = goodsInfoRepository.findAll(GoodsInfoQueryRequest.builder().goodsIds(request.getGoodsIds()).delFlag(DeleteFlag.NO.toValue()).build().getWhereCriteria());
            if (CollectionUtils.isNotEmpty(skus)) {
                skus.forEach(sku -> {
                    StandardSku standardSku = new StandardSku();
                    BeanUtils.copyProperties(sku, standardSku);
                    //商家用供应商
                    if (Integer.valueOf(GoodsSource.SELLER.toValue()).equals(sku.getGoodsSource())) {
                        standardSku.setProviderGoodsInfoId(sku.getProviderGoodsInfoId());
                    }else{
                        standardSku.setProviderGoodsInfoId(sku.getGoodsInfoId());
                    }
                    standardSku.setSupplyPrice(sku.getSupplyPrice());
                    standardSku.setStock(sku.getStock());
                    standardSku.setGoodsInfoId(null);
                    standardSku.setGoodsId(mappingSpu.get(sku.getGoodsId()));
                    standardSku.setCreateTime(now);
                    standardSku.setUpdateTime(now);
                    standardSku.setGoodsInfoNo(sku.getGoodsInfoNo());
                    standardSku.setGoodsSource(sku.getGoodsSource());
                    mappingSku.put(sku.getGoodsInfoId(), standardSkuRepository.save(standardSku).getGoodsInfoId());
                });
            }

            //导入规格
            List<GoodsSpec> specs = goodsSpecRepository.findByGoodsIds(request.getGoodsIds());
            if (CollectionUtils.isNotEmpty(specs)) {
                specs.forEach(spec -> {
                    StandardSpec standardSpec = new StandardSpec();
                    BeanUtils.copyProperties(spec, standardSpec);
                    standardSpec.setSpecId(null);
                    standardSpec.setGoodsId(mappingSpu.get(spec.getGoodsId()));
                    mappingSpec.put(spec.getSpecId(), standardSpecRepository.save(standardSpec).getSpecId());
                });
            }

            //导入规格值
            List<GoodsSpecDetail> details = goodsSpecDetailRepository.findByGoodsIds(request.getGoodsIds());
            if (CollectionUtils.isNotEmpty(details)) {
                details.forEach(specDetail -> {
                    StandardSpecDetail detail = new StandardSpecDetail();
                    BeanUtils.copyProperties(specDetail, detail);
                    detail.setSpecDetailId(null);
                    detail.setSpecId(mappingSpec.get(specDetail.getSpecId()));
                    detail.setGoodsId(mappingSpu.get(specDetail.getGoodsId()));
                    mappingDetail.put(specDetail.getSpecDetailId(), standardSpecDetailRepository.save(detail).getSpecDetailId());
                });
            }

            //导入规格值与Sku的关系
            List<GoodsInfoSpecDetailRel> rels = goodsInfoSpecDetailRelRepository.findByGoodsIds(request.getGoodsIds());
            if (CollectionUtils.isNotEmpty(rels)) {
                rels.forEach(rel -> {
                    StandardSkuSpecDetailRel standardRel = new StandardSkuSpecDetailRel();
                    BeanUtils.copyProperties(rel, standardRel);
                    standardRel.setSpecDetailRelId(null);
                    standardRel.setSpecId(mappingSpec.get(rel.getSpecId()));
                    standardRel.setSpecDetailId(mappingDetail.get(rel.getSpecDetailId()));
                    standardRel.setGoodsInfoId(mappingSku.get(rel.getGoodsInfoId()));
                    standardRel.setGoodsId(mappingSpu.get(rel.getGoodsId()));
                    standardSkuSpecDetailRelRepository.save(standardRel);
                });
            }

            //导入图片
            List<GoodsImage> imageList = goodsImageRepository.findByGoodsIds(request.getGoodsIds());
            if (CollectionUtils.isNotEmpty(imageList)) {
                imageList.forEach(image -> {
                    StandardImage standardImage = new StandardImage();
                    BeanUtils.copyProperties(image, standardImage);
                    standardImage.setImageId(null);
                    standardImage.setGoodsId(mappingSpu.get(image.getGoodsId()));
                    standardImageRepository.save(standardImage);
                });
            }

            //导入商品属性
            List<GoodsPropDetailRel> propDetailRelList = goodsPropDetailRelRepository.findByGoodsIds(request.getGoodsIds());
            if (CollectionUtils.isNotEmpty(propDetailRelList)) {
                propDetailRelList.forEach(goodsPropDetailRel -> {
                    StandardPropDetailRel rel = new StandardPropDetailRel();
                    BeanUtils.copyProperties(goodsPropDetailRel, rel);
                    rel.setRelId(null);
                    rel.setGoodsId(mappingSpu.get(goodsPropDetailRel.getGoodsId()));
                    standardPropDetailRelRepository.save(rel);
                });
            }
        }
        return ids;
    }


    /**
     * 从 供应商商品 同步 到 供应商商品库
     * <p>
     * 供应商商品库同步最新商品信息  最好是把关联表全部设置删除， 同步的时候再重新同步，同步的时候商品库goodsId不能变
     *
     * @param goodsId         供应商商品id
     * @param standardGoodsId 商品库商品id
     */
    @Transactional(rollbackFor = Exception.class)
    public void synProviderGoods(String goodsId, String standardGoodsId) {

        //先将所有的商品库关联设置为待同步
        List<StandardGoodsRel> standardGoodsRels = standardGoodsRelRepository.findByStandardId(standardGoodsId);
        for (StandardGoodsRel goodsRel : standardGoodsRels) {
            goodsRel.setNeedSynchronize(BoolFlag.YES);
        }
        standardGoodsRelRepository.saveAll(standardGoodsRels);

        //同步前先删除所有以前的
        standardGoodsRepository.deleteByGoodsId(goodsId);
        standardSkuRepository.deleteByGoodsId(standardGoodsId);
        standardSpecRepository.deleteByGoodsId(standardGoodsId);
        standardSpecDetailRepository.deleteByGoodsId(standardGoodsId);
        standardSkuSpecDetailRelRepository.deleteByGoodsId(standardGoodsId);
        standardImageRepository.deleteByGoodsId(standardGoodsId);
        standardPropDetailRelRepository.deleteByGoodsId(standardGoodsId);

        //重新导入------------------------------------------------------
//        //规格映射Map,商品Id -> 新商品库Id
//        Map<String, String> mappingSpu = new HashMap<>();
        //规格映射Map,商品规格Id -> 新商品库规格Id
        Map<Long, Long> mappingSpec = new HashMap<>();
        //规格映射Map,商品规格值Id -> 新商品库规格值Id
        Map<Long, Long> mappingDetail = new HashMap<>();
        //规格映射Map,商品SkuId -> 新商品库SkuId
        Map<String, String> mappingSku = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();

        Goods goods = goodsRepository.findById(goodsId).orElse(null);
        StandardGoods standardGoods = standardGoodsRepository.getOne(standardGoodsId);
        KsBeanUtil.copyProperties(goods, standardGoods);
        //商家用供应商
        if (Integer.valueOf(GoodsSource.SELLER.toValue()).equals(goods.getGoodsSource())) {
            standardGoods.setProviderName(goods.getProviderName());
        }else{
            standardGoods.setProviderName(goods.getSupplierName());
        }
        standardGoods.setDelFlag(DeleteFlag.NO);
        standardGoods.setUpdateTime(now);
        standardGoods.setGoodsId(standardGoodsId);
        standardGoods.setGoodsNo(goods.getGoodsNo());
        standardGoods.setStoreId(goods.getStoreId());
        standardGoodsRepository.save(standardGoods);

//        //关联商品与商品库
        StandardGoodsRel standardGoodsRel = standardGoodsRelRepository.findALLByGoodsId(goodsId);
        standardGoodsRel.setGoodsId(goods.getGoodsId());
        standardGoodsRel.setStoreId(goods.getStoreId());
        standardGoodsRel.setDelFlag(DeleteFlag.NO);
        standardGoodsRel.setNeedSynchronize(BoolFlag.NO);
        standardGoodsRelRepository.save(standardGoodsRel);

        //导入Sku
        List<GoodsInfo> skus = goodsInfoRepository.findAll(GoodsInfoQueryRequest.builder().goodsId(goodsId).delFlag(DeleteFlag.NO.toValue()).build().getWhereCriteria());
        if (CollectionUtils.isNotEmpty(skus)) {
            skus.forEach(sku -> {
                StandardSku standardSku = new StandardSku();
                BeanUtils.copyProperties(sku, standardSku);
                //商家用供应商
                if (Integer.valueOf(GoodsSource.SELLER.toValue()).equals(sku.getGoodsSource())) {
                    standardSku.setProviderGoodsInfoId(sku.getProviderGoodsInfoId());
                } else {
                    standardSku.setProviderGoodsInfoId(sku.getGoodsInfoId());
                }
                standardSku.setSupplyPrice(sku.getSupplyPrice());
                standardSku.setStock(sku.getStock());
                standardSku.setGoodsInfoId(null);
                standardSku.setGoodsId(standardGoodsId);
                standardSku.setCreateTime(now);
                standardSku.setUpdateTime(now);
                standardSku.setGoodsInfoNo(sku.getGoodsInfoNo());
                mappingSku.put(sku.getGoodsInfoId(), standardSkuRepository.save(standardSku).getGoodsInfoId());
            });
        }

        //导入规格
        List<GoodsSpec> specs = goodsSpecRepository.findByGoodsId(goodsId);
        if (CollectionUtils.isNotEmpty(specs)) {
            specs.forEach(spec -> {
                StandardSpec standardSpec = new StandardSpec();
                BeanUtils.copyProperties(spec, standardSpec);
                standardSpec.setSpecId(null);
                standardSpec.setGoodsId(standardGoodsId);
                standardSpec.setDelFlag(DeleteFlag.NO);
                mappingSpec.put(spec.getSpecId(), standardSpecRepository.save(standardSpec).getSpecId());
            });
        }

        //导入规格值
        List<GoodsSpecDetail> details = goodsSpecDetailRepository.findByGoodsId(goodsId);
        if (CollectionUtils.isNotEmpty(details)) {
            details.forEach(specDetail -> {
                StandardSpecDetail detail = new StandardSpecDetail();
                BeanUtils.copyProperties(specDetail, detail);
                detail.setSpecDetailId(null);
                detail.setSpecId(mappingSpec.get(specDetail.getSpecId()));
                detail.setGoodsId(standardGoodsId);
                mappingDetail.put(specDetail.getSpecDetailId(), standardSpecDetailRepository.save(detail).getSpecDetailId());
            });
        }

        //导入规格值与Sku的关系
        List<GoodsInfoSpecDetailRel> rels = goodsInfoSpecDetailRelRepository.findByGoodsId(goodsId);
        if (CollectionUtils.isNotEmpty(rels)) {
            rels.forEach(rel -> {
                StandardSkuSpecDetailRel standardRel = new StandardSkuSpecDetailRel();
                BeanUtils.copyProperties(rel, standardRel);
                standardRel.setSpecDetailRelId(null);
                standardRel.setSpecId(mappingSpec.get(rel.getSpecId()));
                standardRel.setSpecDetailId(mappingDetail.get(rel.getSpecDetailId()));
                standardRel.setGoodsInfoId(mappingSku.get(rel.getGoodsInfoId()));
                standardRel.setGoodsId(standardGoodsId);
                standardSkuSpecDetailRelRepository.save(standardRel);
            });
        }


        //导入图片
        List<GoodsImage> imageList = goodsImageRepository.findByGoodsId(goodsId);
        if (CollectionUtils.isNotEmpty(imageList)) {
            imageList.forEach(image -> {
                StandardImage standardImage = new StandardImage();
                BeanUtils.copyProperties(image, standardImage);
                standardImage.setImageId(null);
                standardImage.setGoodsId(standardGoodsId);
                standardImageRepository.save(standardImage);
            });
        }

        //导入商品属性
        List<GoodsPropDetailRel> propDetailRelList = goodsPropDetailRelRepository.queryByGoodsId(goodsId);
        if (CollectionUtils.isNotEmpty(propDetailRelList)) {
            propDetailRelList.forEach(goodsPropDetailRel -> {
                StandardPropDetailRel rel = new StandardPropDetailRel();
                BeanUtils.copyProperties(goodsPropDetailRel, rel);
                rel.setRelId(null);
                rel.setGoodsId(standardGoodsId);
                standardPropDetailRelRepository.save(rel);
            });
        }

    }

    /**
     * 从供应商商品库同步到 商家商品
     *
     * @param goodsId 商家商品id
     */
    @Transactional(rollbackFor = Exception.class)
    public void synStandardGoods(String goodsId) {
        goodsInfoRepository.updateDeleteFlagByGoodsId(goodsId);
        goodsSpecRepository.deleteByGoodsId(goodsId);
        goodsSpecDetailRepository.deleteByGoodsId(goodsId);
        goodsSpecDetailRepository.deleteByGoodsId(goodsId);
        goodsInfoSpecDetailRelRepository.deleteByGoodsId(goodsId);
        goodsImageRepository.deleteByGoodsId(goodsId);
        goodsPropDetailRelRepository.deleteByGoodsId(goodsId);

        StandardGoodsRel goodsRel = standardGoodsRelRepository.findByGoodsId(goodsId);
        //同步后后就不需要再同步了
        goodsRel.setNeedSynchronize(BoolFlag.NO);
        standardGoodsRelRepository.save(goodsRel);
        String standardGoodsId = goodsRel.getStandardId();
        StandardGoods standardGoods = standardGoodsRepository.getOne(standardGoodsId);

        //规格映射Map,商品库Id -> 新商品Id
        Map<String, String> mappingSpu = new HashMap<>();
        //规格映射Map,商品库规格Id -> 新商品规格Id
        Map<Long, Long> mappingSpec = new HashMap<>();
        //规格映射Map,商品库规格值Id -> 新商品规格值Id
        Map<Long, Long> mappingDetail = new HashMap<>();
        //规格映射Map,商品库SkuId -> 新商品SkuId
        Map<String, String> mappingSku = new HashMap<>();
        //规格映射Map,商品库Id -> 商品来源
        Map<String, Integer> mappingGoodsSourse = new HashMap<>();

        LocalDateTime now = LocalDateTime.now();

        //导入Spu
        Goods goods = goodsRepository.findById(goodsId).orElse(null);
        BeanUtils.copyProperties(standardGoods, goods);
        goods.setGoodsId(goodsId);
        goods.setAddedFlag(AddedFlag.NO.toValue());
        goods.setDelFlag(DeleteFlag.NO);
        goods.setAddFalseReason(UnAddedFlagReason.PROVIDERUNADDED.name());
        goods.setCreateTime(now);
        goods.setUpdateTime(now);
        goods.setAddedTime(now);
        goods.setSubmitTime(now);
        goods.setAuditStatus(CheckStatus.CHECKED);
        goods.setCustomFlag(Constants.no);
        goods.setLevelDiscountFlag(Constants.no);
        goods.setGoodsNo(goodsCommonService.getSpuNoByUnique());
        goods.setPriceType(GoodsPriceType.MARKET.toValue());
        //默认销售类型 批发
        goods.setSaleType(SaleType.WHOLESALE.toValue());
        goods.setMarketPrice(null);

        goods.setGoodsVideo(standardGoods.getGoodsVideo());
        //商品来源，0供应商，1商家 导入后就是商家商品 以此来区分列表展示
        goods.setGoodsSource(1);
        goods.setSupplyPrice(standardGoods.getSupplyPrice());
        goods.setRecommendedRetailPrice(standardGoods.getRecommendedRetailPrice());
        goods.setProviderName(standardGoods.getProviderName());
        //允许独立设价
        goods.setAllowPriceSet(1);
        //设置定时上架价flag
        goods.setAddedTimingFlag(Boolean.FALSE);

        //初始化商品对应的数量（收藏、销量、评论数、好评数）
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
        String newGoodsId = goodsRepository.save(goods).getGoodsId();
        // 检查商品基本信息
//        goodsService.checkBasic(goods);

        mappingSpu.put(standardGoods.getGoodsId(), newGoodsId);
        mappingGoodsSourse.put(standardGoods.getGoodsId(), standardGoods.getGoodsSource());

        //关联商品与商品库
//        StandardGoodsRel rel = new StandardGoodsRel();
//        rel.setGoodsId(newGoodsId);
//        rel.setStandardId(standardGoods.getGoodsId());
//        rel.setStoreId(request.getStoreId());
//        standardGoodsRelRepository.save(rel);

//        List<StandardGoods> spuList = standardGoodses;
        //导入Sku
        List<StandardSku> skus = standardSkuRepository.findAll(StandardSkuQueryRequest.builder().goodsId(standardGoodsId).delFlag(DeleteFlag.NO.toValue()).build().getWhereCriteria());
        if (CollectionUtils.isNotEmpty(skus)) {
            skus.forEach(standardSku -> {
                GoodsInfo sku = new GoodsInfo();
                BeanUtils.copyProperties(standardSku, sku);
                sku.setGoodsInfoId(null);
                sku.setGoodsId(mappingSpu.get(standardSku.getGoodsId()));
//                sku.setProviderId(mappingOldStoreId.get(standardSku.getGoodsId()));
                sku.setGoodsSource(1);
                sku.setCreateTime(now);
                sku.setUpdateTime(now);
                sku.setAddedTime(now);
                sku.setAddedFlag(AddedFlag.NO.toValue());
//                sku.setCompanyInfoId(request.getCompanyInfoId());
                sku.setCompanyInfoId(goods.getCompanyInfoId());
                sku.setLevelDiscountFlag(Constants.no);
                sku.setCustomFlag(Constants.no);
//                sku.setStoreId(request.getStoreId());
                sku.setStoreId(goods.getStoreId());
                sku.setAuditStatus(CheckStatus.CHECKED);
//                sku.setCompanyType(request.getCompanyType());
                sku.setCompanyType(goods.getCompanyType());
                sku.setGoodsInfoNo(goodsCommonService.getSkuNoByUnique());
                sku.setStock(standardSku.getStock());
                sku.setAloneFlag(Boolean.FALSE);
                sku.setSupplyPrice(standardSku.getSupplyPrice());

//                Optional<StandardGoods> standardGoods = spuList.stream().filter(goodses -> goodses.getGoodsId().equals(standardSku.getGoodsId())).findFirst();
                sku.setCateId(standardGoods.getCateId());
                sku.setBrandId(standardGoods.getBrandId());
                sku.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                //默认销售类型 批发
                sku.setSaleType(SaleType.WHOLESALE.toValue());
                sku.setAddedTimingFlag(Boolean.FALSE);
                String skuId = goodsInfoRepository.save(sku).getGoodsInfoId();
//                newSkuIds.add(skuId);
                mappingSku.put(standardSku.getGoodsInfoId(), skuId);
            });
        }

        //导入规格
        List<StandardSpec> specs = standardSpecRepository.findByGoodsId(standardGoodsId);
        if (CollectionUtils.isNotEmpty(specs)) {
            specs.forEach(standardSpec -> {
                GoodsSpec spec = new GoodsSpec();
                BeanUtils.copyProperties(standardSpec, spec);
                spec.setSpecId(null);
                spec.setGoodsId(mappingSpu.get(standardSpec.getGoodsId()));
                mappingSpec.put(standardSpec.getSpecId(), goodsSpecRepository.save(spec).getSpecId());
            });
        }

        //导入规格值
        List<StandardSpecDetail> details = standardSpecDetailRepository.findByGoodsId(standardGoodsId);
        if (CollectionUtils.isNotEmpty(details)) {
            details.forEach(specDetail -> {
                GoodsSpecDetail detail = new GoodsSpecDetail();
                BeanUtils.copyProperties(specDetail, detail);
                detail.setSpecDetailId(null);
                detail.setSpecId(mappingSpec.get(specDetail.getSpecId()));
                detail.setGoodsId(mappingSpu.get(specDetail.getGoodsId()));
                mappingDetail.put(specDetail.getSpecDetailId(), goodsSpecDetailRepository.save(detail).getSpecDetailId());
            });
        }

        //导入规格值与Sku的关系
        List<StandardSkuSpecDetailRel> rels = standardSkuSpecDetailRelRepository.findByGoodsId(standardGoodsId);
        if (CollectionUtils.isNotEmpty(rels)) {
            rels.forEach(standardRel -> {
                GoodsInfoSpecDetailRel rel = new GoodsInfoSpecDetailRel();
                BeanUtils.copyProperties(standardRel, rel);
                rel.setSpecDetailRelId(null);
                rel.setSpecId(mappingSpec.get(standardRel.getSpecId()));
                rel.setSpecDetailId(mappingDetail.get(standardRel.getSpecDetailId()));
                rel.setGoodsInfoId(mappingSku.get(standardRel.getGoodsInfoId()));
                rel.setGoodsId(mappingSpu.get(standardRel.getGoodsId()));
                goodsInfoSpecDetailRelRepository.save(rel);
            });
        }

        //导入图片
        List<StandardImage> imageList = standardImageRepository.findByGoodsId(standardGoodsId);
        if (CollectionUtils.isNotEmpty(imageList)) {
            imageList.forEach(standardImage -> {
                GoodsImage image = new GoodsImage();
                BeanUtils.copyProperties(standardImage, image);
                image.setImageId(null);
                image.setGoodsId(mappingSpu.get(standardImage.getGoodsId()));
                goodsImageRepository.save(image);
            });
        }

        //导入商品属性
        List<StandardPropDetailRel> propDetailRelList = standardPropDetailRelRepository.queryByGoodsId(standardGoodsId);
        if (CollectionUtils.isNotEmpty(propDetailRelList)) {
            propDetailRelList.forEach(goodsPropDetailRel -> {
                GoodsPropDetailRel rel = new GoodsPropDetailRel();
                BeanUtils.copyProperties(goodsPropDetailRel, rel);
                rel.setRelId(null);
                rel.setGoodsId(mappingSpu.get(goodsPropDetailRel.getGoodsId()));
                goodsPropDetailRelRepository.save(rel);
            });
        }

    }

    /**
     * @param standardgoodsId 商品库id
     * @param storeId         商家id
     * @return 商家商品id
     */
    public String syn(String standardgoodsId, Long storeId) {
        StandardGoodsRel standardGoodsRel = standardGoodsRelRepository.findByStandardIdAndStoreId(standardgoodsId, storeId);
        synStandardGoods(standardGoodsRel.getGoodsId());
        return standardGoodsRel.getGoodsId();
    }

    /**
     * 查询店铺开店状态
     *
     * @param storeId
     * @return
     */
    public Integer getProviderStatus(Long storeId) {
        if(Objects.isNull(storeId)){
            return Constants.yes;
        }
        StoreVO storeVO = storeQueryProvider.getById(StoreByIdRequest.builder().storeId(storeId).build()).getContext().getStoreVO();
        LocalDateTime now = LocalDateTime.now();
        if (Objects.nonNull(storeVO)) {
            //判断当前店铺是否过期 true：过期 false：没过期
            Boolean isExpired = !((now.isBefore(storeVO.getContractEndDate()) || now.isEqual(storeVO.getContractEndDate()))
                    && (now.isAfter(storeVO.getContractStartDate()) || now.isEqual(storeVO.getContractStartDate())));
            if (StoreState.OPENING.equals(storeVO.getStoreState()) && !isExpired) {
                return Constants.yes;
            }
        }
        return Constants.no;

    }
}
