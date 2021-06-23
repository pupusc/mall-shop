package com.wanmi.sbc.goods.info.service;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemDetailResponse;
import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.OsUtil;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.request.goodscatethirdcaterel.GoodsCateThirdCateRelQueryRequest;
import com.wanmi.sbc.goods.api.response.linkedmall.LinkedMallGoodsDelResponse;
import com.wanmi.sbc.goods.api.response.linkedmall.LinkedMallGoodsModifyResponse;
import com.wanmi.sbc.goods.api.response.linkedmall.LinkedMallInitResponse;
import com.wanmi.sbc.goods.bean.dto.LinkedMallItemDelDTO;
import com.wanmi.sbc.goods.bean.dto.LinkedMallItemModificationDTO;
import com.wanmi.sbc.goods.bean.enums.*;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.brand.repository.ContractBrandRepository;
import com.wanmi.sbc.goods.brand.repository.GoodsBrandRepository;
import com.wanmi.sbc.goods.cate.service.GoodsCateService;
import com.wanmi.sbc.goods.common.GoodsCommonService;
import com.wanmi.sbc.goods.goodscatethirdcaterel.model.root.GoodsCateThirdCateRel;
import com.wanmi.sbc.goods.goodscatethirdcaterel.service.GoodsCateThirdCateRelService;
import com.wanmi.sbc.goods.images.GoodsImage;
import com.wanmi.sbc.goods.images.GoodsImageRepository;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsPropDetailRelRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsRequest;
import com.wanmi.sbc.goods.info.request.GoodsSaveRequest;
import com.wanmi.sbc.goods.pointsgoods.repository.PointsGoodsRepository;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpec;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpecDetail;
import com.wanmi.sbc.goods.spec.repository.GoodsInfoSpecDetailRelRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecDetailRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecRepository;
import com.wanmi.sbc.goods.spec.service.GoodsSpecService;
import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.standard.model.root.StandardGoodsRel;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRelRepository;
import com.wanmi.sbc.goods.standard.repository.StandardGoodsRepository;
import com.wanmi.sbc.goods.standard.repository.StandardSkuRepository;
import com.wanmi.sbc.goods.standard.service.StandardGoodsService;
import com.wanmi.sbc.goods.standard.service.StandardImportService;
import com.wanmi.sbc.goods.standard.service.StandardSkuService;
import com.wanmi.sbc.goods.standardimages.model.root.StandardImage;
import com.wanmi.sbc.goods.standardimages.repository.StandardImageRepository;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSkuSpecDetailRel;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpec;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSpecDetail;
import com.wanmi.sbc.goods.standardspec.repository.StandardSkuSpecDetailRelRepository;
import com.wanmi.sbc.goods.standardspec.repository.StandardSpecDetailRepository;
import com.wanmi.sbc.goods.standardspec.repository.StandardSpecRepository;
import com.wanmi.sbc.goods.storecate.model.root.StoreCateGoodsRela;
import com.wanmi.sbc.goods.storecate.repository.StoreCateGoodsRelaRepository;
import com.wanmi.sbc.goods.storecate.repository.StoreCateRepository;
import com.wanmi.sbc.goods.storegoodstab.repository.GoodsTabRelaRepository;
import com.wanmi.sbc.goods.storegoodstab.repository.StoreGoodsTabRepository;
import com.wanmi.sbc.goods.thirdgoodscate.service.ThirdGoodsCateService;
import com.wanmi.sbc.linkedmall.api.provider.goods.LinkedMallGoodsQueryProvider;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.goods.GoodsDetailByIdRequest;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import com.wanmi.sbc.setting.api.provider.thirdplatformconfig.ThirdPlatformConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdplatformconfig.ThirdPlatformConfigByTypeRequest;
import com.wanmi.sbc.setting.api.response.thirdplatformconfig.ThirdPlatformConfigResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * linkedmall商品服务
 */
@Service
@Slf4j
public class LinkedMallGoodsService {

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
    private GoodsBrandRepository goodsBrandRepository;

    @Autowired
    private GoodsTabRelaRepository goodsTabRelaRepository;

    @Autowired
    private StoreCateGoodsRelaRepository storeCateGoodsRelaRepository;

    @Autowired
    private StoreCateRepository storeCateRepository;

    @Autowired
    private StoreGoodsTabRepository storeGoodsTabRepository;

    @Autowired
    private ContractBrandRepository contractBrandRepository;

    @Autowired
    private GoodsPropDetailRelRepository goodsPropDetailRelRepository;

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
    private OsUtil osUtil;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private GoodsCateThirdCateRelService goodsCateThirdCateRelService;

    @Autowired
    private LinkedMallGoodsQueryProvider linkedMallGoodsQueryProvider;

    @Autowired
    private StandardImportService standardImportService;

    @Autowired
    private PointsGoodsRepository pointsGoodsRepository;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StandardSkuService standardSkuService;
    @Autowired
    private StandardGoodsService standardGoodsService;

    @Autowired
    private ThirdGoodsCateService thirdGoodsCateService;

    @Autowired
    private GoodsSpecService goodsSpecService;

    @Autowired
    private GoodsInfoSpecDetailRelRepository getGoodsInfoSpecDetailRelRepository;

    @Autowired
    private StandardSpecRepository standardSpecRepository;

    @Autowired
    private StandardSpecDetailRepository standardSpecDetailRepository;

    @Autowired
    private StandardSkuSpecDetailRelRepository standardSkuSpecDetailRelRepository;

    @Autowired
    private ThirdPlatformConfigQueryProvider thirdPlatformConfigQueryProvider;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    @Autowired
    private StandardImageRepository standardImageRepository;

    /**
     * 新增linkedmall商品
     *
     * @param store
     * @param storeDefaultCates
     */
    @Transactional
    public List<String> addLinkedMallGoods(Long thirdcategoryId, Long itemId, StoreVO store, List<Long> storeDefaultCates) {
        if (storeDefaultCates == null || storeDefaultCates.size() < 1) {
            throw new SbcRuntimeException("k-210001");
        }
        //查询此linkedmall商品类目关联平台的类目
        GoodsCateThirdCateRelQueryRequest goodsCateThirdCateRelQueryRequest = new GoodsCateThirdCateRelQueryRequest();
        goodsCateThirdCateRelQueryRequest.setDelFlag(DeleteFlag.NO);
        goodsCateThirdCateRelQueryRequest.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
        goodsCateThirdCateRelQueryRequest.setThirdCateId(thirdcategoryId);
        List<GoodsCateThirdCateRel> goodsCateThirdCateRels = goodsCateThirdCateRelService.list(goodsCateThirdCateRelQueryRequest);
        Long cateId = null;
        if (goodsCateThirdCateRels != null && goodsCateThirdCateRels.size() > 0) {
            cateId = goodsCateThirdCateRels.get(0).getCateId();
        }
        //linkedmall商品详情
        QueryItemDetailResponse.Item addGoodsDetail =
                linkedMallGoodsQueryProvider.getGoodsDetailById(new GoodsDetailByIdRequest(itemId)).getContext();
        List<Goods> oldLinkedMallGoods =
                goodsService.findAll(GoodsQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.LINKED_MALL.toValue()).thirdPlatformSpuId(itemId.toString()).build());
        StandardGoods oldStandardGoods =
                standardGoodsRepository.findByDelFlagAndGoodsSourceAndThirdPlatformSpuId(DeleteFlag.NO, GoodsSource.LINKED_MALL.toValue(), itemId.toString());
        //如果spu存在，则只添加sku
        if (oldLinkedMallGoods != null && oldLinkedMallGoods.size() > 0) {
            //旧的sku
            List<String> oldGoodsInfoIds =
                    goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.LINKED_MALL.toValue()).thirdPlatformSpuId(itemId.toString()).build())
                            .stream().map(v -> v.getThirdPlatformSkuId()).collect(Collectors.toList());
            //只添加数据库中没有的sku
            List<QueryItemDetailResponse.Item.Sku> addSkus = addGoodsDetail.getSkus()
                    .stream().filter(v -> !oldGoodsInfoIds.contains(v.getSkuId().toString())).collect(Collectors.toList());
            if (addSkus != null && addSkus.size() > 0) {
                //生成skuNo
                List<String> skuNos = skuNOsNotRepeatDateBase(skuNOsNotRepeat(addSkus.size()), addSkus.size());
                for (int i = 0; i < addSkus.size(); i++) {
                    QueryItemDetailResponse.Item.Sku sku = addSkus.get(i);
                    //规格和规格值字符串
                    String skuPropertiesJson = sku.getSkuPropertiesJson();
                    //添加规格
                    if (StringUtils.isNotBlank(skuPropertiesJson)) {
                        List<GoodsSpec> oldGoodsSpecs = goodsSpecService.findByGoodsIds(Collections.singletonList(oldLinkedMallGoods.get(0).getGoodsId()));
                        Matcher specAndDetail = Pattern.compile("\"[\\s\\S]+?\"").matcher(skuPropertiesJson);
                        while (specAndDetail.find()) {
                            String linkedMallSpecName = specAndDetail.group();
                            String finalLinkedMallSpecName = linkedMallSpecName.substring(1, linkedMallSpecName.length() - 1);
                            if (!oldGoodsSpecs.stream().anyMatch(v -> v.getSpecName().equals(finalLinkedMallSpecName))) {
                                GoodsSpec spec = new GoodsSpec();
                                spec.setSpecName(finalLinkedMallSpecName);
                                spec.setCreateTime(LocalDateTime.now());
                                spec.setUpdateTime(LocalDateTime.now());
                                spec.setDelFlag(DeleteFlag.NO);
                                spec.setGoodsId(oldLinkedMallGoods.get(0).getGoodsId());
                                goodsSpecRepository.save(spec);
                                StandardSpec standardSpec = new StandardSpec();
                                standardSpec.setGoodsId(oldStandardGoods.getGoodsId());
                                standardSpec.setDelFlag(DeleteFlag.NO);
                                standardSpec.setCreateTime(LocalDateTime.now());
                                standardSpec.setUpdateTime(LocalDateTime.now());
                                standardSpec.setSpecName(finalLinkedMallSpecName);
                                standardSpec.setGoodsId(oldStandardGoods.getGoodsId());
                                standardSpecRepository.save(standardSpec);
                            }
                            specAndDetail.find();
                        }
                    }
                    GoodsInfo goodsInfo = new GoodsInfo();
                    goodsInfo.setCreateTime(LocalDateTime.now());
                    goodsInfo.setGoodsInfoNo(skuNos.get(i));
                    goodsInfo.setAuditStatus(CheckStatus.CHECKED);
                    goodsInfo.setCateId(cateId == null ? -1 : cateId);
                    goodsInfo.setCompanyInfoId(store.getCompanyInfo().getCompanyInfoId());
                    goodsInfo.setCompanyType(store.getCompanyInfo().getCompanyType());
                    goodsInfo.setDelFlag(DeleteFlag.NO);
                    goodsInfo.setGoodsInfoImg("https:" + sku.getSkuPicUrl());
                    goodsInfo.setGoodsInfoName(addGoodsDetail.getItemTitle());
                    goodsInfo.setGoodsSource(GoodsSource.LINKED_MALL.toValue());
                    goodsInfo.setGoodsStatus(sku.getQuantity() > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                    goodsInfo.setGoodsUnit("件");
                    goodsInfo.setGoodsWeight(new BigDecimal("0.001"));
                    goodsInfo.setGoodsCubage(new BigDecimal("0.000001"));
                    goodsInfo.setSaleType(SaleType.RETAIL.toValue());
                    goodsInfo.setStoreId(store.getStoreId());
                    BigDecimal supplyPrice = new BigDecimal(sku.getPriceCent()).divide(new BigDecimal("100"));
                    BigDecimal marketPrice = sku.getReservePrice() != null && sku.getReservePrice() > 0 ? new BigDecimal(sku.getReservePrice()).divide(new BigDecimal("100")) : supplyPrice;
                    goodsInfo.setSupplyPrice(supplyPrice);
                    goodsInfo.setMarketPrice(marketPrice);
                    goodsInfo.setProviderId(store.getStoreId());
                    goodsInfo.setUpdateTime(LocalDateTime.now());
                    goodsInfo.setAddedFlag(sku.getCanSell() ? 1 : 0);
                    goodsInfo.setSellerId(addGoodsDetail.getSellerId());
                    goodsInfo.setThirdPlatformSkuId(String.valueOf(sku.getSkuId()));
                    goodsInfo.setThirdCateId(addGoodsDetail.getCategoryId());
                    goodsInfo.setStoreCateIds(storeDefaultCates);
                    goodsInfo.setThirdPlatformSpuId(String.valueOf(addGoodsDetail.getItemId()));
                    goodsInfo.setGoodsId(oldLinkedMallGoods.get(0).getGoodsId());
                    goodsInfo.setAloneFlag(false);
                    goodsInfo.setStock(0L);
                    GoodsInfo savedGoodsInfo = goodsInfoRepository.save(goodsInfo);
                    log.info("供应商新增linkedmall-sku，所属linkedmall商品spuId:" + addGoodsDetail.getItemId() + ",linkedmall-skuId:" + sku.getSkuId());
                    StandardSku savedStandardSku = new StandardSku();
                    BeanUtils.copyProperties(savedGoodsInfo, savedStandardSku);
                    savedStandardSku.setGoodsInfoId(null);
                    savedStandardSku.setGoodsId(oldStandardGoods.getGoodsId());
                    savedStandardSku.setProviderGoodsInfoId(savedGoodsInfo.getGoodsInfoId());
                    standardSkuRepository.save(savedStandardSku);
                    log.info("商品库新增linkedmall-sku，所属linkedmall商品spuId:" + addGoodsDetail.getItemId() + ",linkedmall-skuId:" + sku.getSkuId());
                    if (StringUtils.isNotBlank(skuPropertiesJson)) {
                        List<GoodsSpec> newGoodsSpecs = goodsSpecRepository.findByGoodsId(oldLinkedMallGoods.get(0).getGoodsId());
                        List<StandardSpec> newStandardSpec = standardSpecRepository.findByGoodsId(oldStandardGoods.getGoodsId());
                        List<GoodsSpecDetail> oldSpecDetails = goodsSpecDetailRepository.findByGoodsId(oldLinkedMallGoods.get(0).getGoodsId());
                        List<StandardSpecDetail> oldStandardSpecDetails = standardSpecDetailRepository.findByGoodsId(oldStandardGoods.getGoodsId());
                        Matcher specAndDetail = Pattern.compile("\"[\\s\\S]+?\"").matcher(skuPropertiesJson);
                        specAndDetail.find();
                        String spec = specAndDetail.group();
                        String specName = spec.substring(1, spec.length() - 1);
                        while (specAndDetail.find()) {
                            String detailName = specAndDetail.group();
                            String finalDetailName = detailName.substring(1, detailName.length() - 1);
                            String finalSpecName = specName;
                            GoodsSpec goodsSpec = newGoodsSpecs.stream().filter(v -> v.getSpecName().equals(finalSpecName)).findFirst().get();
                            Optional<GoodsSpecDetail> oldSpecDetail = oldSpecDetails.stream().filter(v -> v.getSpecId().equals(goodsSpec.getSpecId()) && v.getDetailName().equals(finalSpecName)).findFirst();
                            Optional<StandardSpecDetail> oldStandardSpecDetail = oldStandardSpecDetails.stream().filter(v -> v.getDetailName().equals(finalDetailName)).findFirst();
                            if (oldSpecDetail.isPresent()) {
                                GoodsInfoSpecDetailRel savedGoodsInfoSpecDetailRel = new GoodsInfoSpecDetailRel();
                                savedGoodsInfoSpecDetailRel.setGoodsId(oldLinkedMallGoods.get(0).getGoodsId());
                                savedGoodsInfoSpecDetailRel.setGoodsInfoId(savedGoodsInfo.getGoodsInfoId());
                                savedGoodsInfoSpecDetailRel.setSpecDetailId(oldSpecDetail.get().getSpecDetailId());
                                savedGoodsInfoSpecDetailRel.setSpecId(oldSpecDetail.get().getSpecId());
                                savedGoodsInfoSpecDetailRel.setCreateTime(LocalDateTime.now());
                                savedGoodsInfoSpecDetailRel.setDelFlag(DeleteFlag.NO);
                                savedGoodsInfoSpecDetailRel.setDetailName(oldSpecDetail.get().getDetailName());
                                savedGoodsInfoSpecDetailRel.setUpdateTime(LocalDateTime.now());
                                goodsInfoSpecDetailRelRepository.save(savedGoodsInfoSpecDetailRel);
                                StandardSkuSpecDetailRel savedStandardSkuSpecDetailRel = new StandardSkuSpecDetailRel();
                                savedStandardSkuSpecDetailRel.setGoodsId(oldStandardGoods.getGoodsId());
                                savedStandardSkuSpecDetailRel.setGoodsInfoId(savedStandardSku.getGoodsInfoId());
                                savedStandardSkuSpecDetailRel.setDetailName(oldStandardSpecDetail.get().getDetailName());
                                savedStandardSkuSpecDetailRel.setCreateTime(LocalDateTime.now());
                                savedStandardSkuSpecDetailRel.setUpdateTime(LocalDateTime.now());
                                savedStandardSkuSpecDetailRel.setDelFlag(DeleteFlag.NO);
                                savedStandardSkuSpecDetailRel.setSpecId(oldStandardSpecDetail.get().getSpecId());
                                savedStandardSkuSpecDetailRel.setSpecDetailId(oldStandardSpecDetail.get().getSpecDetailId());
                                standardSkuSpecDetailRelRepository.save(savedStandardSkuSpecDetailRel);
                            } else {
                                //规格值不存在
                                StandardSpec standardSpec = newStandardSpec.stream().filter(v -> v.getSpecName().equals(finalSpecName)).findFirst().get();
                                GoodsSpecDetail goodsSpecDetail = new GoodsSpecDetail();
                                goodsSpecDetail.setDelFlag(DeleteFlag.NO);
                                goodsSpecDetail.setGoodsId(oldLinkedMallGoods.get(0).getGoodsId());
                                goodsSpecDetail.setSpecId(goodsSpec.getSpecId());
                                goodsSpecDetail.setDetailName(finalDetailName);
                                goodsSpecDetail.setCreateTime(LocalDateTime.now());
                                goodsSpecDetail.setUpdateTime(LocalDateTime.now());
                                GoodsSpecDetail savedSpecDetail = goodsSpecDetailRepository.save(goodsSpecDetail);
                                StandardSpecDetail standardSpecDetail = new StandardSpecDetail();
                                standardSpecDetail.setGoodsId(oldStandardGoods.getGoodsId());
                                standardSpecDetail.setSpecId(standardSpec.getSpecId());
                                standardSpecDetail.setDelFlag(DeleteFlag.NO);
                                standardSpecDetail.setCreateTime(LocalDateTime.now());
                                standardSpecDetail.setUpdateTime(LocalDateTime.now());
                                standardSpecDetail.setDetailName(finalDetailName);
                                StandardSpecDetail savedStandardSpecDetail = standardSpecDetailRepository.save(standardSpecDetail);
                                GoodsInfoSpecDetailRel goodsInfoSpecDetailRel = new GoodsInfoSpecDetailRel();
                                goodsInfoSpecDetailRel.setGoodsId(oldLinkedMallGoods.get(0).getGoodsId());
                                goodsInfoSpecDetailRel.setGoodsInfoId(savedGoodsInfo.getGoodsInfoId());
                                goodsInfoSpecDetailRel.setSpecDetailId(savedSpecDetail.getSpecDetailId());
                                goodsInfoSpecDetailRel.setSpecId(goodsSpec.getSpecId());
                                goodsInfoSpecDetailRel.setCreateTime(LocalDateTime.now());
                                goodsInfoSpecDetailRel.setDelFlag(DeleteFlag.NO);
                                goodsInfoSpecDetailRel.setDetailName(savedSpecDetail.getDetailName());
                                goodsInfoSpecDetailRel.setUpdateTime(LocalDateTime.now());
                                goodsInfoSpecDetailRelRepository.save(goodsInfoSpecDetailRel);
                                StandardSkuSpecDetailRel savedStandardSkuSpecDetailRel = new StandardSkuSpecDetailRel();
                                savedStandardSkuSpecDetailRel.setGoodsId(oldStandardGoods.getGoodsId());
                                savedStandardSkuSpecDetailRel.setGoodsInfoId(savedStandardSku.getGoodsInfoId());
                                savedStandardSkuSpecDetailRel.setDetailName(savedStandardSpecDetail.getDetailName());
                                savedStandardSkuSpecDetailRel.setCreateTime(LocalDateTime.now());
                                savedStandardSkuSpecDetailRel.setUpdateTime(LocalDateTime.now());
                                savedStandardSkuSpecDetailRel.setDelFlag(DeleteFlag.NO);
                                savedStandardSkuSpecDetailRel.setSpecId(standardSpec.getSpecId());
                                savedStandardSkuSpecDetailRel.setSpecDetailId(savedStandardSpecDetail.getSpecDetailId());
                                standardSkuSpecDetailRelRepository.save(savedStandardSkuSpecDetailRel);

                            }
                            if (specAndDetail.find()) {
                                spec = specAndDetail.group();
                                specName = spec.substring(1, spec.length() - 1);
                            }
                        }
                    }

                }
            }
        } else {
            //同步spu及所属sku
            GoodsSaveRequest goodsSaveRequest = wrapSaveRequest(addGoodsDetail, store, storeDefaultCates);
            List<GoodsImage> goodsImages = goodsSaveRequest.getImages();
            List<GoodsInfo> goodsInfos = goodsSaveRequest.getGoodsInfos();
            Goods goods = goodsSaveRequest.getGoods();


            final String savedGoodsId = goodsRepository.save(goods).getGoodsId();

            //新增图片
            if (CollectionUtils.isNotEmpty(goodsImages)) {
                goodsImages.forEach(goodsImage -> {
                    goodsImage.setCreateTime(goods.getCreateTime());
                    goodsImage.setUpdateTime(goods.getUpdateTime());
                    goodsImage.setGoodsId(savedGoodsId);
                    goodsImage.setDelFlag(DeleteFlag.NO);
                    goodsImage.setImageId(goodsImageRepository.save(goodsImage).getImageId());
                });
            }

            if (osUtil.isS2b() && CollectionUtils.isNotEmpty(goods.getStoreCateIds())) {
                goods.getStoreCateIds().forEach(storeCateId -> {
                    StoreCateGoodsRela rela = new StoreCateGoodsRela();
                    rela.setGoodsId(savedGoodsId);
                    rela.setStoreCateId(storeCateId);
                    storeCateGoodsRelaRepository.save(rela);
                });
            }

            List<GoodsSpec> specs = goodsSaveRequest.getGoodsSpecs();
            List<GoodsSpecDetail> specDetails = goodsSaveRequest.getGoodsSpecDetails();

            List<GoodsInfoSpecDetailRel> specDetailRels = new ArrayList<>();
            //如果是多规格
            if (Constants.yes.equals(goods.getMoreSpecFlag())) {
                //填放可用SKU相应的规格\规格值
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
                            goodsSpec.setGoodsId(savedGoodsId);
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
                                goodsSpecDetail.setGoodsId(savedGoodsId);
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
                sku.setGoodsId(savedGoodsId);

                String goodsInfoId = goodsInfoRepository.save(sku).getGoodsInfoId();
                sku.setGoodsInfoId(goodsInfoId);

                //如果是多规格,新增SKU与规格明细值的关联表
                if (Constants.yes.equals(goods.getMoreSpecFlag())) {
                    if (CollectionUtils.isNotEmpty(specs)) {
                        for (GoodsSpec spec : specs) {
                            if (sku.getMockSpecIds().contains(spec.getMockSpecId())) {
                                for (GoodsSpecDetail detail : specDetails) {
                                    if (spec.getMockSpecId().equals(detail.getMockSpecId()) && sku.getMockSpecDetailIds().contains(detail.getMockSpecDetailId())) {
                                        GoodsInfoSpecDetailRel detailRel = new GoodsInfoSpecDetailRel();
                                        detailRel.setGoodsId(savedGoodsId);
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
            log.info("供应商新增linkedmall-spu，linkedmall商品spuId:" + addGoodsDetail.getItemId());
            GoodsRequest goodsRequest = new GoodsRequest();
            goodsRequest.setGoodsIds(Collections.singletonList(savedGoodsId));
            List<Goods> goodsesAll = goodsRepository.findAll(GoodsQueryRequest.builder().goodsIds(Collections.singletonList(savedGoodsId))
                    .build().getWhereCriteria());

            log.info("商品库新增linkedmall-spu，linkedmall商品spuId:" + addGoodsDetail.getItemId());
            //导入商品库
            return standardImportService.importStandardGoods(goodsRequest, goodsesAll);
        }
        return Collections.emptyList();
    }

    /**
     * 初始化linkedmall商品
     * @param linkedMallGoodsDetail
     * @param store linkedmall供应商店铺信息
     * @param storeDefaultCates
     */
    @Transactional
    public LinkedMallInitResponse initLinkedMallGoods(QueryItemDetailResponse.Item linkedMallGoodsDetail, StoreVO store, List<Long> storeDefaultCates) {
        Long itemId = linkedMallGoodsDetail.getItemId();
        if (storeDefaultCates == null || storeDefaultCates.size() < 1) {
            throw new SbcRuntimeException("k-210001");
        }
        List<String> standardIds = new ArrayList<>();

        //1, 查询此linkedmall商品类目关联平台的类目
        GoodsCateThirdCateRelQueryRequest goodsCateThirdCateRelQueryRequest = new GoodsCateThirdCateRelQueryRequest();
        goodsCateThirdCateRelQueryRequest.setDelFlag(DeleteFlag.NO);
        goodsCateThirdCateRelQueryRequest.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
        goodsCateThirdCateRelQueryRequest.setThirdCateId(linkedMallGoodsDetail.getCategoryId());
        List<GoodsCateThirdCateRel> goodsCateThirdCateRels = goodsCateThirdCateRelService.list(goodsCateThirdCateRelQueryRequest);
        Long cateId = null;
        if (goodsCateThirdCateRels != null && goodsCateThirdCateRels.size() > 0) {
            cateId = goodsCateThirdCateRels.get(0).getCateId();
        }
        //已经存在的linkedmall商品（以供应商角色）
        List<Goods> oldLinkedMallGoodsList =
                goodsService.findAll(
                        GoodsQueryRequest.builder().delFlag(DeleteFlag.NO.toValue())
                                .goodsSource(GoodsSource.LINKED_MALL.toValue()).thirdPlatformSpuId(itemId.toString())
                                .build());
        //商品库中已经存在的linkedmall商品
        StandardGoods oldStandardGoods =
                standardGoodsRepository.findByDelFlagAndGoodsSourceAndThirdPlatformSpuId(DeleteFlag.NO, GoodsSource.LINKED_MALL.toValue(), itemId.toString());
        //2-1, 如果spu存在，更新已有的spu
        if (oldLinkedMallGoodsList != null && oldLinkedMallGoodsList.size() > 0) {
            //spu上下架状态
            Integer spuAddedFlag = 0;
            if (linkedMallGoodsDetail.getCanSell()) {
                long count = linkedMallGoodsDetail.getSkus().stream().filter(v -> v.getCanSell() == false).count();
                if (count > 0) {
                    spuAddedFlag = 2;
                } else {
                    spuAddedFlag = 1;
                }
            }
            //是否单规格
            boolean SingleSpecFlag = StringUtils.isBlank(linkedMallGoodsDetail.getSkus().get(0).getSkuPropertiesJson());
            //2-1-1， 更新已有的linkedmall-spu
            Goods oldLinkedMallGoods = oldLinkedMallGoodsList.get(0);
            oldLinkedMallGoods.setCateId(cateId == null ? -1 : cateId);
            oldLinkedMallGoods.setGoodsDetail(linkedMallGoodsDetail.getDescOption());
            oldLinkedMallGoods.setGoodsImg("https:" + linkedMallGoodsDetail.getMainPicUrl());
            oldLinkedMallGoods.setGoodsName(linkedMallGoodsDetail.getItemTitle());
            oldLinkedMallGoods.setLinePrice(new BigDecimal(linkedMallGoodsDetail.getReservePrice()).divide(new BigDecimal("100")));
            oldLinkedMallGoods.setMarketPrice(new BigDecimal(linkedMallGoodsDetail.getReservePrice()).divide(new BigDecimal("100")));
            oldLinkedMallGoods.setMoreSpecFlag(SingleSpecFlag ? 0 : 1);
            oldLinkedMallGoods.setSingleSpecFlag(SingleSpecFlag);
            oldLinkedMallGoods.setSellerId(linkedMallGoodsDetail.getSellerId());
            oldLinkedMallGoods.setUpdateTime(LocalDateTime.now());
            oldLinkedMallGoods.setThirdCateId(linkedMallGoodsDetail.getCategoryId());
            if (!spuAddedFlag.equals(oldLinkedMallGoods.getAddedFlag())) {
                oldLinkedMallGoods.setAddedFlag(spuAddedFlag);
                oldLinkedMallGoods.setAddedTime(oldLinkedMallGoods.getCreateTime());
            }

            goodsRepository.save(oldLinkedMallGoods);
            //2-1-2, 更新商品库中已有的linkedmall-spu
            oldStandardGoods.setGoodsImg("https:" + linkedMallGoodsDetail.getMainPicUrl());
            oldStandardGoods.setUpdateTime(LocalDateTime.now());
            oldStandardGoods.setAddedFlag(spuAddedFlag);
            oldStandardGoods.setMoreSpecFlag(SingleSpecFlag ? 0 : 1);
            oldStandardGoods.setCateId(cateId == null ? -1 : cateId);
            oldStandardGoods.setGoodsDetail(linkedMallGoodsDetail.getDescOption());
            oldStandardGoods.setGoodsName(linkedMallGoodsDetail.getItemTitle());
            oldStandardGoods.setSellerId(linkedMallGoodsDetail.getSellerId());
            oldStandardGoods.setThirdCateId(linkedMallGoodsDetail.getCategoryId());
            standardGoodsRepository.save(oldStandardGoods);
            //已经存在的linkedmall-sku（以供应商角色）
            List<GoodsInfo> oldGoodsInfos =
                    goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.LINKED_MALL.toValue()).thirdPlatformSpuId(itemId.toString()).build());
            List<String> oldGoodsInfoIds = oldGoodsInfos.stream().map(v -> v.getThirdPlatformSkuId()).collect(Collectors.toList());
            //2-1-3, 更新已经存在的linkedmall-sku
            ArrayList<GoodsInfo> updateGoodsInfos = new ArrayList<>();
            for (GoodsInfo oldGoodsInfo : oldGoodsInfos) {
                List<QueryItemDetailResponse.Item.Sku> linkedMallSkus =
                        linkedMallGoodsDetail.getSkus().stream()
                                .filter(v -> v.getSkuId().toString().equals(oldGoodsInfo.getThirdPlatformSkuId()))
                                .collect(Collectors.toList());
                if (linkedMallSkus.size() > 0) {
                    QueryItemDetailResponse.Item.Sku linkedMallSku = linkedMallSkus.get(0);
                    oldGoodsInfo.setCateId(cateId == null ? -1 : cateId);
                    oldGoodsInfo.setGoodsInfoImg("https:" + linkedMallSku.getSkuPicUrl());
                    oldGoodsInfo.setGoodsInfoName(linkedMallGoodsDetail.getItemTitle());
                    BigDecimal supplyPrice = new BigDecimal(linkedMallSku.getPriceCent()).divide(new BigDecimal("100"));
                    BigDecimal marketPrice = linkedMallSku.getReservePrice() != null && linkedMallSku.getReservePrice() > 0 ?
                            new BigDecimal(linkedMallSku.getReservePrice()).divide(new BigDecimal("100")) : supplyPrice;
                    oldGoodsInfo.setSupplyPrice(supplyPrice);
                    oldGoodsInfo.setMarketPrice(marketPrice);
                    oldGoodsInfo.setUpdateTime(LocalDateTime.now());
                    Integer addFlag = linkedMallSku.getCanSell() ? 1 : 0;
                    if (!addFlag.equals(oldGoodsInfo.getAddedFlag())) {
                        oldGoodsInfo.setAddedFlag(addFlag);
                        oldGoodsInfo.setAddedTime(LocalDateTime.now());
                    }
                    oldGoodsInfo.setSellerId(linkedMallGoodsDetail.getSellerId());
                    oldGoodsInfo.setThirdCateId(linkedMallGoodsDetail.getCategoryId());
                    updateGoodsInfos.add(oldGoodsInfo);
                }
            }
            if (updateGoodsInfos.size() > 0) {
                goodsInfoRepository.saveAll(updateGoodsInfos);
            }
            //2-1-4, 更新商品库已有的linkedmall-sku
            ArrayList<StandardSku> updateStandarSkus = new ArrayList<>();
            List<StandardSku> oldStandarSkus =
                    standardSkuRepository.findByDelFlagAndGoodsSourceAndThirdPlatformSpuId(DeleteFlag.NO, GoodsSource.LINKED_MALL.toValue(), itemId.toString());
            for (StandardSku oldStandarSku : oldStandarSkus) {
                List<QueryItemDetailResponse.Item.Sku> linkedMallSkus =
                        linkedMallGoodsDetail.getSkus().stream()
                                .filter(v -> v.getSkuId().toString().equals(oldStandarSku.getThirdPlatformSkuId()))
                                .collect(Collectors.toList());
                if (linkedMallSkus.size() > 0) {
                    QueryItemDetailResponse.Item.Sku linkedMallSku = linkedMallSkus.get(0);
                    oldStandarSku.setUpdateTime(LocalDateTime.now());
                    BigDecimal supplyPrice = new BigDecimal(linkedMallSku.getPriceCent()).divide(new BigDecimal("100"));
                    BigDecimal marketPrice = linkedMallSku.getReservePrice() != null && linkedMallSku.getReservePrice() > 0 ?
                            new BigDecimal(linkedMallSku.getReservePrice()).divide(new BigDecimal("100")) : supplyPrice;
                    oldStandarSku.setSupplyPrice(supplyPrice);
                    oldStandarSku.setMarketPrice(marketPrice);
                    oldStandarSku.setGoodsInfoName(linkedMallGoodsDetail.getItemTitle());
                    oldStandarSku.setGoodsInfoImg("https:" + linkedMallSku.getSkuPicUrl());
                    oldStandarSku.setSellerId(linkedMallGoodsDetail.getSellerId());
                    oldStandarSku.setThirdCateId(linkedMallGoodsDetail.getCategoryId());
                    oldStandarSku.setAddedFlag(linkedMallSku.getCanSell() ? 1 : 0);
                    updateStandarSkus.add(oldStandarSku);
                }
            }
            if (updateStandarSkus.size() > 0) {
                standardSkuRepository.saveAll(updateStandarSkus);
            }
            //2-1-5， 添加数据库中没有的linkedmall-sku
            List<QueryItemDetailResponse.Item.Sku> addSkus = linkedMallGoodsDetail.getSkus()
                    .stream().filter(v -> !oldGoodsInfoIds.contains(v.getSkuId().toString())).collect(Collectors.toList());
            if (addSkus != null && addSkus.size() > 0) {
                //生成skuNo
                List<String> skuNos = skuNOsNotRepeatDateBase(skuNOsNotRepeat(addSkus.size()), addSkus.size());
                for (int i = 0; i < addSkus.size(); i++) {
                    QueryItemDetailResponse.Item.Sku sku = addSkus.get(i);
                    //规格和规格值字符串
                    String skuPropertiesJson = sku.getSkuPropertiesJson();
                    //添加规格
                    if (StringUtils.isNotBlank(skuPropertiesJson)) {
                        List<GoodsSpec> oldGoodsSpecs = goodsSpecService.findByGoodsIds(Collections.singletonList(oldLinkedMallGoodsList.get(0).getGoodsId()));
                        Matcher specAndDetail = Pattern.compile("\"[\\s\\S]+?\"").matcher(skuPropertiesJson);
                        while (specAndDetail.find()) {
                            String linkedMallSpecName = specAndDetail.group();
                            String finalLinkedMallSpecName = linkedMallSpecName.substring(1, linkedMallSpecName.length() - 1);
                            if (!oldGoodsSpecs.stream().anyMatch(v -> v.getSpecName().equals(finalLinkedMallSpecName))) {
                                GoodsSpec spec = new GoodsSpec();
                                spec.setSpecName(finalLinkedMallSpecName);
                                spec.setCreateTime(LocalDateTime.now());
                                spec.setUpdateTime(LocalDateTime.now());
                                spec.setDelFlag(DeleteFlag.NO);
                                spec.setGoodsId(oldLinkedMallGoodsList.get(0).getGoodsId());
                                goodsSpecRepository.save(spec);
                                StandardSpec standardSpec = new StandardSpec();
                                standardSpec.setGoodsId(oldStandardGoods.getGoodsId());
                                standardSpec.setDelFlag(DeleteFlag.NO);
                                standardSpec.setCreateTime(LocalDateTime.now());
                                standardSpec.setUpdateTime(LocalDateTime.now());
                                standardSpec.setSpecName(finalLinkedMallSpecName);
                                standardSpec.setGoodsId(oldStandardGoods.getGoodsId());
                                standardSpecRepository.save(standardSpec);
                            }
                            specAndDetail.find();
                        }
                    }
                    GoodsInfo goodsInfo = new GoodsInfo();
                    goodsInfo.setCreateTime(LocalDateTime.now());
                    goodsInfo.setGoodsInfoNo(skuNos.get(i));
                    goodsInfo.setAuditStatus(CheckStatus.CHECKED);
                    goodsInfo.setCateId(cateId == null ? -1 : cateId);
                    goodsInfo.setCompanyInfoId(store.getCompanyInfo().getCompanyInfoId());
                    goodsInfo.setCompanyType(store.getCompanyInfo().getCompanyType());
                    goodsInfo.setDelFlag(DeleteFlag.NO);
                    goodsInfo.setGoodsInfoImg("https:" + sku.getSkuPicUrl());
                    goodsInfo.setGoodsInfoName(linkedMallGoodsDetail.getItemTitle());
                    goodsInfo.setGoodsSource(GoodsSource.LINKED_MALL.toValue());
                    goodsInfo.setGoodsStatus(sku.getQuantity() > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
                    goodsInfo.setGoodsUnit("件");
                    goodsInfo.setGoodsWeight(new BigDecimal("0.001"));
                    goodsInfo.setGoodsCubage(new BigDecimal("0.000001"));
                    goodsInfo.setSaleType(SaleType.RETAIL.toValue());
                    goodsInfo.setStoreId(store.getStoreId());
                    BigDecimal supplyPrice = new BigDecimal(sku.getPriceCent()).divide(new BigDecimal("100"));
                    BigDecimal marketPrice = sku.getReservePrice() != null && sku.getReservePrice() > 0 ?
                            new BigDecimal(sku.getReservePrice()).divide(new BigDecimal("100")) : supplyPrice;
                    goodsInfo.setSupplyPrice(supplyPrice);
                    goodsInfo.setMarketPrice(marketPrice);
                    goodsInfo.setProviderId(store.getStoreId());
                    goodsInfo.setUpdateTime(LocalDateTime.now());
                    goodsInfo.setAddedFlag(sku.getCanSell() ? 1 : 0);
                    goodsInfo.setSellerId(linkedMallGoodsDetail.getSellerId());
                    goodsInfo.setThirdPlatformSkuId(String.valueOf(sku.getSkuId()));
                    goodsInfo.setThirdCateId(linkedMallGoodsDetail.getCategoryId());
                    goodsInfo.setStoreCateIds(storeDefaultCates);
                    goodsInfo.setThirdPlatformSpuId(String.valueOf(linkedMallGoodsDetail.getItemId()));
                    goodsInfo.setGoodsId(oldLinkedMallGoodsList.get(0).getGoodsId());
                    goodsInfo.setAloneFlag(false);
                    goodsInfo.setAddedTimingFlag(false);
                    goodsInfo.setStock(0L);
                    GoodsInfo savedGoodsInfo = goodsInfoRepository.save(goodsInfo);
                    log.info("供应商新增linkedmall-sku，所属linkedmall商品spuId:" + linkedMallGoodsDetail.getItemId() + ",linkedmall-skuId:" + sku.getSkuId());
                    StandardSku savedStandardSku = new StandardSku();
                    BeanUtils.copyProperties(savedGoodsInfo, savedStandardSku);
                    savedStandardSku.setGoodsInfoId(null);
                    savedStandardSku.setGoodsId(oldStandardGoods.getGoodsId());
                    savedStandardSku.setProviderGoodsInfoId(savedGoodsInfo.getGoodsInfoId());
                    standardSkuRepository.save(savedStandardSku);
                    log.info("商品库新增linkedmall-sku，所属linkedmall商品spuId:" + linkedMallGoodsDetail.getItemId() + ",linkedmall-skuId:" + sku.getSkuId());
                    if (StringUtils.isNotBlank(skuPropertiesJson)) {
                        List<GoodsSpec> newGoodsSpecs = goodsSpecRepository.findByGoodsId(oldLinkedMallGoodsList.get(0).getGoodsId());
                        List<StandardSpec> newStandardSpec = standardSpecRepository.findByGoodsId(oldStandardGoods.getGoodsId());
                        List<GoodsSpecDetail> oldSpecDetails = goodsSpecDetailRepository.findByGoodsId(oldLinkedMallGoodsList.get(0).getGoodsId());
                        List<StandardSpecDetail> oldStandardSpecDetails = standardSpecDetailRepository.findByGoodsId(oldStandardGoods.getGoodsId());
                        Matcher specAndDetail = Pattern.compile("\"[\\s\\S]+?\"").matcher(skuPropertiesJson);
                        specAndDetail.find();
                        String spec = specAndDetail.group();
                        String specName = spec.substring(1, spec.length() - 1);
                        while (specAndDetail.find()) {
                            String detailName = specAndDetail.group();
                            String finalDetailName = detailName.substring(1, detailName.length() - 1);
                            String finalSpecName = specName;
                            GoodsSpec goodsSpec = newGoodsSpecs.stream().filter(v -> v.getSpecName().equals(finalSpecName)).findFirst().get();
                            Optional<GoodsSpecDetail> oldSpecDetail = oldSpecDetails.stream().filter(v -> v.getSpecId().equals(goodsSpec.getSpecId()) && v.getDetailName().equals(finalSpecName)).findFirst();
                            Optional<StandardSpecDetail> oldStandardSpecDetail = oldStandardSpecDetails.stream().filter(v -> v.getDetailName().equals(finalDetailName)).findFirst();
                            if (oldSpecDetail.isPresent()) {
                                //规格值已经存在，添加sku和规格值的映射关系
                                GoodsInfoSpecDetailRel savedGoodsInfoSpecDetailRel = new GoodsInfoSpecDetailRel();
                                savedGoodsInfoSpecDetailRel.setGoodsId(oldLinkedMallGoodsList.get(0).getGoodsId());
                                savedGoodsInfoSpecDetailRel.setGoodsInfoId(savedGoodsInfo.getGoodsInfoId());
                                savedGoodsInfoSpecDetailRel.setSpecDetailId(oldSpecDetail.get().getSpecDetailId());
                                savedGoodsInfoSpecDetailRel.setSpecId(oldSpecDetail.get().getSpecId());
                                savedGoodsInfoSpecDetailRel.setCreateTime(LocalDateTime.now());
                                savedGoodsInfoSpecDetailRel.setDelFlag(DeleteFlag.NO);
                                savedGoodsInfoSpecDetailRel.setDetailName(oldSpecDetail.get().getDetailName());
                                savedGoodsInfoSpecDetailRel.setUpdateTime(LocalDateTime.now());
                                goodsInfoSpecDetailRelRepository.save(savedGoodsInfoSpecDetailRel);
                                StandardSkuSpecDetailRel savedStandardSkuSpecDetailRel = new StandardSkuSpecDetailRel();
                                savedStandardSkuSpecDetailRel.setGoodsId(oldStandardGoods.getGoodsId());
                                savedStandardSkuSpecDetailRel.setGoodsInfoId(savedStandardSku.getGoodsInfoId());
                                savedStandardSkuSpecDetailRel.setDetailName(oldStandardSpecDetail.get().getDetailName());
                                savedStandardSkuSpecDetailRel.setCreateTime(LocalDateTime.now());
                                savedStandardSkuSpecDetailRel.setUpdateTime(LocalDateTime.now());
                                savedStandardSkuSpecDetailRel.setDelFlag(DeleteFlag.NO);
                                savedStandardSkuSpecDetailRel.setSpecId(oldStandardSpecDetail.get().getSpecId());
                                savedStandardSkuSpecDetailRel.setSpecDetailId(oldStandardSpecDetail.get().getSpecDetailId());
                                standardSkuSpecDetailRelRepository.save(savedStandardSkuSpecDetailRel);
                            } else {
                                //规格值不存在，添加规格，规格和sku的映射关系
                                StandardSpec standardSpec = newStandardSpec.stream().filter(v -> v.getSpecName().equals(finalSpecName)).findFirst().get();
                                GoodsSpecDetail goodsSpecDetail = new GoodsSpecDetail();
                                goodsSpecDetail.setDelFlag(DeleteFlag.NO);
                                goodsSpecDetail.setGoodsId(oldLinkedMallGoodsList.get(0).getGoodsId());
                                goodsSpecDetail.setSpecId(goodsSpec.getSpecId());
                                goodsSpecDetail.setDetailName(finalDetailName);
                                goodsSpecDetail.setCreateTime(LocalDateTime.now());
                                goodsSpecDetail.setUpdateTime(LocalDateTime.now());
                                GoodsSpecDetail savedSpecDetail = goodsSpecDetailRepository.save(goodsSpecDetail);
                                StandardSpecDetail standardSpecDetail = new StandardSpecDetail();
                                standardSpecDetail.setGoodsId(oldStandardGoods.getGoodsId());
                                standardSpecDetail.setSpecId(standardSpec.getSpecId());
                                standardSpecDetail.setDelFlag(DeleteFlag.NO);
                                standardSpecDetail.setCreateTime(LocalDateTime.now());
                                standardSpecDetail.setUpdateTime(LocalDateTime.now());
                                standardSpecDetail.setDetailName(finalDetailName);
                                StandardSpecDetail savedStandardSpecDetail = standardSpecDetailRepository.save(standardSpecDetail);
                                GoodsInfoSpecDetailRel goodsInfoSpecDetailRel = new GoodsInfoSpecDetailRel();
                                goodsInfoSpecDetailRel.setGoodsId(oldLinkedMallGoodsList.get(0).getGoodsId());
                                goodsInfoSpecDetailRel.setGoodsInfoId(savedGoodsInfo.getGoodsInfoId());
                                goodsInfoSpecDetailRel.setSpecDetailId(savedSpecDetail.getSpecDetailId());
                                goodsInfoSpecDetailRel.setSpecId(goodsSpec.getSpecId());
                                goodsInfoSpecDetailRel.setCreateTime(LocalDateTime.now());
                                goodsInfoSpecDetailRel.setDelFlag(DeleteFlag.NO);
                                goodsInfoSpecDetailRel.setDetailName(savedSpecDetail.getDetailName());
                                goodsInfoSpecDetailRel.setUpdateTime(LocalDateTime.now());
                                goodsInfoSpecDetailRelRepository.save(goodsInfoSpecDetailRel);
                                StandardSkuSpecDetailRel savedStandardSkuSpecDetailRel = new StandardSkuSpecDetailRel();
                                savedStandardSkuSpecDetailRel.setGoodsId(oldStandardGoods.getGoodsId());
                                savedStandardSkuSpecDetailRel.setGoodsInfoId(savedStandardSku.getGoodsInfoId());
                                savedStandardSkuSpecDetailRel.setDetailName(savedStandardSpecDetail.getDetailName());
                                savedStandardSkuSpecDetailRel.setCreateTime(LocalDateTime.now());
                                savedStandardSkuSpecDetailRel.setUpdateTime(LocalDateTime.now());
                                savedStandardSkuSpecDetailRel.setDelFlag(DeleteFlag.NO);
                                savedStandardSkuSpecDetailRel.setSpecId(standardSpec.getSpecId());
                                savedStandardSkuSpecDetailRel.setSpecDetailId(savedStandardSpecDetail.getSpecDetailId());
                                standardSkuSpecDetailRelRepository.save(savedStandardSkuSpecDetailRel);

                            }
                            if (specAndDetail.find()) {
                                spec = specAndDetail.group();
                                specName = spec.substring(1, spec.length() - 1);
                            }
                        }
                    }

                }
            }
            standardIds.add(oldStandardGoods.getGoodsId());
        } else {
            //2-2， 如果spu不存在，新增spu及所属sku
            GoodsSaveRequest goodsSaveRequest = wrapSaveRequest(linkedMallGoodsDetail, store, storeDefaultCates);
            List<GoodsImage> goodsImages = goodsSaveRequest.getImages();
            List<GoodsInfo> goodsInfos = goodsSaveRequest.getGoodsInfos();
            //2-2-1，新增linkedmall-spu（以供应商角色）
            Goods savedGoods = goodsRepository.save(goodsSaveRequest.getGoods());
            final String savedGoodsId = savedGoods.getGoodsId();

            //2-2-2新增图片
            if (CollectionUtils.isNotEmpty(goodsImages)) {
                goodsImages.forEach(goodsImage -> {
                    goodsImage.setGoodsId(savedGoodsId);
                });
                goodsImageRepository.saveAll(goodsImages);
            }
            List<Long> storeCateIds = savedGoods.getStoreCateIds();
            //2-2-3, 新增商品-店铺分类关联
            if (osUtil.isS2b() && CollectionUtils.isNotEmpty(storeCateIds)) {
                List<StoreCateGoodsRela> relas = storeCateIds.stream().map(v -> {
                    StoreCateGoodsRela rela = new StoreCateGoodsRela();
                    rela.setGoodsId(savedGoodsId);
                    rela.setStoreCateId(v);
                    return rela;
                }).collect(Collectors.toList());
                storeCateGoodsRelaRepository.saveAll(relas);
            }

            List<GoodsSpec> specs = goodsSaveRequest.getGoodsSpecs();
            List<GoodsSpecDetail> specDetails = goodsSaveRequest.getGoodsSpecDetails();

            List<GoodsInfoSpecDetailRel> specDetailRels = new ArrayList<>();
            //如果是多规格
            if (CollectionUtils.isNotEmpty(specs)) {
                //填放可用SKU相应的规格\规格值
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
                //2-2-4， 新增规格
                specs.stream()
//                        .filter(goodsSpec -> Constants.yes.equals(isSpecEnable.get(goodsSpec.getMockSpecId()))) //如果SKU有这个规格
                        .forEach(goodsSpec -> {
                            goodsSpec.setGoodsId(savedGoodsId);
                            goodsSpec.setSpecId(goodsSpecRepository.save(goodsSpec).getSpecId());
                        });
                //2-2-5， 新增规格值
                specDetails.stream()
                        .filter(goodsSpecDetail -> Constants.yes.equals(isSpecDetailEnable.get(goodsSpecDetail.getMockSpecDetailId()))) //如果SKU有这个规格值
                        .forEach(goodsSpecDetail -> {
                            Optional<GoodsSpec> specOpt = specs.stream().filter(goodsSpec -> goodsSpec.getMockSpecId().equals(goodsSpecDetail.getMockSpecId())).findFirst();
                            if (specOpt.isPresent()) {
                                goodsSpecDetail.setGoodsId(savedGoodsId);
                                goodsSpecDetail.setSpecId(specOpt.get().getSpecId());
                                goodsSpecDetail.setSpecDetailId(goodsSpecDetailRepository.save(goodsSpecDetail).getSpecDetailId());
                            }
                        });
            }

            BigDecimal minPrice = savedGoods.getMarketPrice();//最小sku市场价
            //新增的sku
            List<GoodsInfoSpecDetailRel> detailRels = new ArrayList<>();
            //2-2-6， 新增linkedmal-sku
            for (GoodsInfo sku : goodsInfos) {
                sku.setGoodsId(savedGoodsId);
                String goodsInfoId = goodsInfoRepository.save(sku).getGoodsInfoId();
                sku.setGoodsInfoId(goodsInfoId);
                log.info("供应商新增linkedmall-sku，所属linkedmall商品spuId:" + sku.getThirdPlatformSpuId() + ",linkedmall-skuId:" + sku.getThirdPlatformSkuId());
                sku.setGoodsInfoId(goodsInfoId);
                //如果是多规格,新增SKU与规格明细值的关联表
                if (CollectionUtils.isNotEmpty(specs)) {
                    for (GoodsSpec spec : specs) {
                        if (sku.getMockSpecIds().contains(spec.getMockSpecId())) {
                            detailRels.addAll(specDetails.stream()
                                    .filter(detail -> spec.getMockSpecId().equals(detail.getMockSpecId()) && sku.getMockSpecDetailIds().contains(detail.getMockSpecDetailId()))
                                    .map(detail -> {
                                        GoodsInfoSpecDetailRel detailRel = new GoodsInfoSpecDetailRel();
                                        detailRel.setGoodsId(savedGoodsId);
                                        detailRel.setGoodsInfoId(goodsInfoId);
                                        detailRel.setSpecId(spec.getSpecId());
                                        detailRel.setSpecDetailId(detail.getSpecDetailId());
                                        detailRel.setDetailName(detail.getDetailName());
                                        detailRel.setCreateTime(detail.getCreateTime());
                                        detailRel.setUpdateTime(detail.getUpdateTime());
                                        detailRel.setDelFlag(detail.getDelFlag());
                                        detailRel.setSpecName(spec.getSpecName());
                                        return detailRel;
                                    }).collect(Collectors.toList()));
                        }
                    }
                    goodsInfoSpecDetailRelRepository.saveAll(detailRels);
                }

                if (minPrice == null || minPrice.compareTo(sku.getMarketPrice()) > 0) {
                    minPrice = sku.getMarketPrice();
                }
            }
            savedGoods.setSkuMinMarketPrice(minPrice);
            log.info("供应商新增linkedmall-spu，linkedmall商品spuId:" + linkedMallGoodsDetail.getItemId());
            GoodsRequest goodsRequest = new GoodsRequest();
            goodsRequest.setGoodsIds(Collections.singletonList(savedGoodsId));

            //规格映射Map,商品规格Id -> 新商品库规格Id
            Map<Long, Long> mappingSpec = new HashMap<>();
            //规格映射Map,商品规格值Id -> 新商品库规格值Id
            Map<Long, Long> mappingDetail = new HashMap<>();
            //规格映射Map,商品SkuId -> 新商品库SkuId
            Map<String, String> mappingSku = new HashMap<>();
            LocalDateTime now = LocalDateTime.now();

            //2-2-7,导入商品库Spu
            StandardGoods standardGoods = new StandardGoods();
            BeanUtils.copyProperties(savedGoods, standardGoods);
            standardGoods.setProviderName(savedGoods.getSupplierName());
            standardGoods.setGoodsId(null);
            standardGoods.setDelFlag(DeleteFlag.NO);
            standardGoods.setCreateTime(now);
            standardGoods.setUpdateTime(now);
            standardGoods.setStoreId(savedGoods.getStoreId());
            standardGoods.setGoodsNo(savedGoods.getGoodsNo());
            String newGoodsId = standardGoodsRepository.save(standardGoods).getGoodsId();

            //2-2-8, 关联商品与商品库
            StandardGoodsRel rel = new StandardGoodsRel();
            rel.setGoodsId(savedGoods.getGoodsId());
            rel.setStandardId(newGoodsId);
            rel.setStoreId(savedGoods.getStoreId());
            rel.setDelFlag(DeleteFlag.NO);
            standardGoodsRelRepository.save(rel);

            //2-2-9,导入商品库Sku
            goodsInfos.forEach(sku -> {
                StandardSku standardSku = new StandardSku();
                BeanUtils.copyProperties(sku, standardSku);
                standardSku.setProviderGoodsInfoId(sku.getGoodsInfoId());
                standardSku.setSupplyPrice(sku.getSupplyPrice());
                standardSku.setStock(sku.getStock());
                standardSku.setGoodsInfoId(null);
                standardSku.setGoodsId(newGoodsId);
                standardSku.setCreateTime(now);
                standardSku.setUpdateTime(now);
                standardSku.setGoodsInfoNo(sku.getGoodsInfoNo());
                standardSku.setGoodsSource(sku.getGoodsSource());
                mappingSku.put(sku.getGoodsInfoId(), standardSkuRepository.save(standardSku).getGoodsInfoId());
            });

            //2-2-10,导入商品库规格
            if (CollectionUtils.isNotEmpty(specs)) {
                specs.forEach(spec -> {
                    StandardSpec standardSpec = new StandardSpec();
                    BeanUtils.copyProperties(spec, standardSpec);
                    standardSpec.setSpecId(null);
                    standardSpec.setGoodsId(newGoodsId);
                    mappingSpec.put(spec.getSpecId(), standardSpecRepository.save(standardSpec).getSpecId());
                });
            }

            //2-2-11,导入商品库规格值
            if (CollectionUtils.isNotEmpty(specDetails)) {
                specDetails.forEach(specDetail -> {
                    StandardSpecDetail detail = new StandardSpecDetail();
                    BeanUtils.copyProperties(specDetail, detail);
                    detail.setSpecDetailId(null);
                    detail.setSpecId(mappingSpec.get(specDetail.getSpecId()));
                    detail.setGoodsId(newGoodsId);
                    mappingDetail.put(specDetail.getSpecDetailId(), standardSpecDetailRepository.save(detail).getSpecDetailId());
                });
            }

            //2-2-12, 导入商品库规格值与Sku的关系
            if (CollectionUtils.isNotEmpty(detailRels)) {
                detailRels.forEach(v -> {
                    StandardSkuSpecDetailRel standardRel = new StandardSkuSpecDetailRel();
                    BeanUtils.copyProperties(v, standardRel);
                    standardRel.setSpecDetailRelId(null);
                    standardRel.setSpecId(mappingSpec.get(v.getSpecId()));
                    standardRel.setSpecDetailId(mappingDetail.get(v.getSpecDetailId()));
                    standardRel.setGoodsInfoId(mappingSku.get(v.getGoodsInfoId()));
                    standardRel.setGoodsId(newGoodsId);
                    standardSkuSpecDetailRelRepository.save(standardRel);
                });
            }

            //2-2-13,导入商品库图片
            if (CollectionUtils.isNotEmpty(goodsImages)) {
                goodsImages.forEach(image -> {
                    StandardImage standardImage = new StandardImage();
                    BeanUtils.copyProperties(image, standardImage);
                    standardImage.setImageId(null);
                    standardImage.setGoodsId(newGoodsId);
                    standardImageRepository.save(standardImage);
                });
            }
            log.info("商品库新增linkedmall-spu，linkedmall商品spuId:" + linkedMallGoodsDetail.getItemId());

            standardIds.add(newGoodsId);
        }

        LinkedMallInitResponse response = new LinkedMallInitResponse();
        response.setStandardIds(standardIds);
        return response;
    }


    /**
     * 初始化包装linkedmall商品
     *
     * @param item
     * @return
     */
    public GoodsSaveRequest wrapSaveRequest(QueryItemDetailResponse.Item item, StoreVO store, List<Long> storeCates) {
        //spu上下架状态
        Integer spuAddedFlag = 0;
        if (item.getCanSell()) {
            long count = item.getSkus().stream().filter(v -> v.getCanSell() == false).count();
            if (count > 0) {
                spuAddedFlag = 2;
            } else {
                spuAddedFlag = 1;
            }
        }

        GoodsSaveRequest goodsSaveRequest = new GoodsSaveRequest();
        //查询此linkedmall商品类目关联平台的类目
        GoodsCateThirdCateRelQueryRequest goodsCateThirdCateRelQueryRequest = new GoodsCateThirdCateRelQueryRequest();
        goodsCateThirdCateRelQueryRequest.setDelFlag(DeleteFlag.NO);
        goodsCateThirdCateRelQueryRequest.setThirdPlatformType(ThirdPlatformType.LINKED_MALL);
        goodsCateThirdCateRelQueryRequest.setThirdCateId(item.getCategoryId());
        List<GoodsCateThirdCateRel> goodsCateThirdCateRels = goodsCateThirdCateRelService.list(goodsCateThirdCateRelQueryRequest);
        Long cateId = null;
        if (goodsCateThirdCateRels != null && goodsCateThirdCateRels.size() > 0) {
            cateId = goodsCateThirdCateRels.get(0).getCateId();
        }


        List<QueryItemDetailResponse.Item.Sku> skus = item.getSkus();
        //规格
        ArrayList<GoodsSpec> goodsSpecS = new ArrayList<>();
        if (skus.get(0).getSkuId() != -1) {
            String skuPropertiesJson = skus.get(0).getSkuPropertiesJson();
            Matcher goodsSpecAndDetail = Pattern.compile("\"[\\s\\S]+?\"").matcher(skuPropertiesJson);
            while (goodsSpecAndDetail.find()) {
                String specName = goodsSpecAndDetail.group();
                GoodsSpec spec = new GoodsSpec();
                spec.setSpecName(specName.substring(1, specName.length() - 1));
                spec.setMockSpecId((long) ((Math.random() * 9 + 1) * 1000000000));
                spec.setCreateTime(LocalDateTime.now());
                spec.setUpdateTime(LocalDateTime.now());
                spec.setDelFlag(DeleteFlag.NO);
                goodsSpecS.add(spec);
                goodsSpecAndDetail.find();
            }
        }

        //规格值
        ArrayList<GoodsSpecDetail> goodsSpecDetailS = new ArrayList<>();
        //包装sku
        ArrayList<GoodsInfo> goodsInfoS = new ArrayList<>();
        List<String> skuNos = skuNOsNotRepeatDateBase(skuNOsNotRepeat(skus.size()), skus.size());
        for (int i = 0; i < skus.size(); i++) {
            QueryItemDetailResponse.Item.Sku sku = skus.get(i);
            GoodsInfo goodsInfo = new GoodsInfo();
            ArrayList<Long> mockSpecDetailIds = new ArrayList<>();
            goodsInfo.setCreateTime(LocalDateTime.now());
            goodsInfo.setGoodsInfoNo(skuNos.get(i));
            goodsInfo.setAuditStatus(CheckStatus.CHECKED);
            goodsInfo.setCateId(cateId == null ? -1 : cateId);
            goodsInfo.setCompanyInfoId(store.getCompanyInfo().getCompanyInfoId());
            goodsInfo.setCompanyType(store.getCompanyInfo().getCompanyType());
            goodsInfo.setDelFlag(DeleteFlag.NO);
            goodsInfo.setGoodsInfoImg("https:" + sku.getSkuPicUrl());
            goodsInfo.setGoodsInfoName(item.getItemTitle());
            goodsInfo.setGoodsSource(GoodsSource.LINKED_MALL.toValue());
            goodsInfo.setGoodsStatus(sku.getQuantity() > 0 ? GoodsStatus.OK : GoodsStatus.OUT_STOCK);
            goodsInfo.setGoodsUnit("件");
            goodsInfo.setGoodsWeight(new BigDecimal("0.001"));
            goodsInfo.setGoodsCubage(new BigDecimal("0.000001"));
            goodsInfo.setSaleType(SaleType.RETAIL.toValue());
            goodsInfo.setStoreId(store.getStoreId());
            BigDecimal supplyPrice = new BigDecimal(sku.getPriceCent()).divide(new BigDecimal("100"));
            BigDecimal marketPrice = sku.getReservePrice() != null && sku.getReservePrice() > 0 ? new BigDecimal(sku.getReservePrice()).divide(new BigDecimal("100")) : supplyPrice;
            goodsInfo.setSupplyPrice(supplyPrice);
            goodsInfo.setMarketPrice(marketPrice);
            goodsInfo.setProviderId(store.getStoreId());
            goodsInfo.setUpdateTime(LocalDateTime.now());
            goodsInfo.setAddedFlag(sku.getCanSell() ? 1 : 0);
            goodsInfo.setSellerId(item.getSellerId());
            goodsInfo.setThirdPlatformSkuId(String.valueOf(sku.getSkuId()));
            goodsInfo.setThirdCateId(item.getCategoryId());
            goodsInfo.setStoreCateIds(storeCates);
            goodsInfo.setThirdPlatformSpuId(String.valueOf(item.getItemId()));
            goodsInfo.setStock(0L);
            goodsInfo.setAddedTime(LocalDateTime.now());
            goodsInfo.setPriceType(GoodsPriceType.MARKET.toValue());
            goodsInfo.setLevelDiscountFlag(Constants.no);
            goodsInfo.setCustomFlag(Constants.no);
            goodsInfo.setAloneFlag(Boolean.FALSE);
            goodsInfo.setAddedTimingFlag(false);
            goodsInfo.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
            //linkedmall规格json
            String skuPropertiesJson = sku.getSkuPropertiesJson();
            if (StringUtils.isNotBlank(skuPropertiesJson) && goodsSpecS.size() > 0) {
                Matcher specAndDetail = Pattern.compile("\"[\\s\\S]+?\"").matcher(skuPropertiesJson);
                specAndDetail.find();
                int count = 0;
                while (specAndDetail.find()) {
                    String detail = specAndDetail.group();
                    //去掉双引号
                    String detailName = detail.substring(1, detail.length() - 1);
                    //判断当前规格值是否存在
                    List<GoodsSpecDetail> exit = goodsSpecDetailS.stream()
                            .filter(v -> v.getDetailName().equals(detailName))
                            .collect(Collectors.toList());
                    if (exit != null && exit.size() > 0) {
                        mockSpecDetailIds.add(exit.get(0).getMockSpecDetailId());
                    } else {
                        GoodsSpecDetail goodsSpecDetail = new GoodsSpecDetail();
                        goodsSpecDetail.setMockSpecId(goodsSpecS.get(count).getMockSpecId());
                        goodsSpecDetail.setDetailName(detailName);
                        goodsSpecDetail.setCreateTime(LocalDateTime.now());
                        goodsSpecDetail.setUpdateTime(LocalDateTime.now());
                        goodsSpecDetail.setDelFlag(DeleteFlag.NO);
                        long mock = (long) ((Math.random() * 9 + 1) * 1000000000);
                        goodsSpecDetail.setMockSpecDetailId(mock);
                        mockSpecDetailIds.add(mock);
                        goodsSpecDetailS.add(goodsSpecDetail);
                    }
                    specAndDetail.find();
                    count++;
                }
                goodsInfo.setMockSpecIds(goodsSpecS.stream().map(v -> v.getMockSpecId()).collect(Collectors.toList()));
                goodsInfo.setMockSpecDetailIds(mockSpecDetailIds);
            }
            goodsInfoS.add(goodsInfo);
        }

        //包装goods
        Goods goods = new Goods();
        //商品类型，实体或虚拟
        String lmItemCategory = item.getLmItemCategory();
        Integer goodsType = null;
        switch (lmItemCategory) {
            case "entity":
                goodsType = 0;
                break;
            case "aliComBenifit":
                goodsType = 1;
                break;
        }
        goods.setCompanyInfoId(store.getCompanyInfo().getCompanyInfoId());
        goods.setCompanyType(store.getCompanyInfo().getCompanyType());
        goods.setStoreId(store.getStoreId());
        goods.setAuditStatus(CheckStatus.CHECKED);
        goods.setGoodsWeight(new BigDecimal("0.001"));
        goods.setGoodsCubage(new BigDecimal("0.000001"));
        goods.setStoreCateIds(storeCates);
        goods.setStock(0L);
        goods.setCateId(cateId == null ? -1 : cateId);
        goods.setProviderId(store.getStoreId());
        goods.setProviderName(store.getStoreName());
        goods.setSupplierName(store.getStoreName());
        goods.setGoodsDetail(item.getDescOption());
        goods.setGoodsImg("https:" + item.getMainPicUrl());
        goods.setGoodsName(item.getItemTitle());
        goods.setGoodsNo(spuNotRepeatDateBase("P".concat(String.valueOf(System.currentTimeMillis()).substring(4, 10)).concat(RandomStringUtils.randomNumeric(3))));
        goods.setGoodsSource(GoodsSource.LINKED_MALL.toValue());
        goods.setGoodsUnit("件");
        goods.setSaleType(SaleType.RETAIL.toValue());
        goods.setAddedTimingFlag(false);
        goods.setLinePrice(new BigDecimal(item.getReservePrice()).divide(new BigDecimal("100")));
        goods.setMarketPrice(new BigDecimal(item.getReservePrice()).divide(new BigDecimal("100")));
        goods.setThirdPlatformSpuId(String.valueOf(item.getItemId()));
        goods.setMoreSpecFlag(goodsSpecS.size() > 0 ? 1 : 0);
        goods.setSingleSpecFlag(goodsSpecS.size() < 1 ? true : false);
        goods.setProviderId(store.getStoreId());
        goods.setProviderName(store.getStoreName());
        goods.setSellerId(item.getSellerId());
        goods.setCreateTime(LocalDateTime.now());
        goods.setUpdateTime(LocalDateTime.now());
        goods.setThirdCateId(item.getCategoryId());
        goods.setAddedFlag(spuAddedFlag);
        goods.setDelFlag(DeleteFlag.NO);
        goods.setAddedTime(goods.getCreateTime());
        goods.setPriceType(GoodsPriceType.MARKET.toValue());
        goods.setCustomFlag(Constants.no);
        goods.setLevelDiscountFlag(Constants.no);
        goods.setGoodsCollectNum(0L);
        goods.setGoodsSalesNum(0L);
        goods.setGoodsEvaluateNum(0L);
        goods.setGoodsFavorableCommentNum(0L);
        goods.setShamSalesNum(0L);
        goods.setSortNo(0L);
        if (goodsType != null) {
            goods.setGoodsType(goodsType);
        }
        goodsSaveRequest.setGoods(goods);
        //商品相关图片
        List<GoodsImage> goodsImages = item.getItemImages().stream()
                .map(v -> new GoodsImage(null, null, null, "https:" + v, null, null, null,null,LocalDateTime.now(), LocalDateTime.now(), DeleteFlag.NO))
                .collect(Collectors.toList());
        goodsSaveRequest.setImages(goodsImages);

        goodsSaveRequest.setGoodsSpecs(goodsSpecS);
        goodsSaveRequest.setGoodsSpecDetails(goodsSpecDetailS);
        goodsSaveRequest.setGoodsInfos(goodsInfoS);
        return goodsSaveRequest;
    }


    /**
     * 生成本地不重复skuNO
     *
     * @param count
     * @return
     */
    public List<String> skuNOsNotRepeat(int count) {
        ArrayList<String> skuNOs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            skuNOs.add(getSku(skuNOs, "8".concat(String.valueOf(System.currentTimeMillis()).substring(4, 10)).concat(RandomStringUtils.randomNumeric(3))));
        }
        return skuNOs;
    }

    public String getSku(List<String> skuNos, String skuNo) {
        if (skuNos.contains(skuNo)) {
            return getSku(skuNos, "8".concat(String.valueOf(System.currentTimeMillis()).substring(4, 10)).concat(RandomStringUtils.randomNumeric(3)));
        } else {
            return skuNo;
        }
    }

    /**
     * 批量生成数据库不重复skuNO
     *
     * @param skuNos
     * @param count
     * @return
     */
    public List<String> skuNOsNotRepeatDateBase(List<String> skuNos, int count) {
        GoodsInfoQueryRequest goodsInfoQueryRequest = new GoodsInfoQueryRequest();
        goodsInfoQueryRequest.setGoodsInfoNos(skuNos);
        if (goodsInfoService.count(goodsInfoQueryRequest) > 0) {

            return skuNOsNotRepeatDateBase(skuNOsNotRepeat(count), count);
        } else {
            return skuNos;
        }
    }

    /**
     * 生成数据库不重复spuNO
     *
     * @param spuNo
     * @return
     */
    public String spuNotRepeatDateBase(String spuNo) {
        Goods goods = new Goods();
        goods.setGoodsNo(spuNo);
        if (goodsRepository.findAll(Example.of(goods)).size() > 0) {
            return spuNotRepeatDateBase("P".concat(String.valueOf(System.currentTimeMillis()).substring(4, 10)).concat(RandomStringUtils.randomNumeric(3)));
        } else {
            return spuNo;
        }
    }


    /**
     * 修改linkedmall商品
     */
    @Transactional
    public LinkedMallGoodsModifyResponse updateLinkedmallGoods(LinkedMallItemModificationDTO modificationDTO) {
        ArrayList<String> esGoodsInfoIds = new ArrayList<>();
        ArrayList<String> standardIds = new ArrayList<>();
        String thirdPlatformSpuId = modificationDTO.getItemId().toString();
        for (LinkedMallItemModificationDTO.LinkedMallSku sku : modificationDTO.getSkuList()) {
            String thirdPlatformSkuId = sku.getSkuId().toString();
            Integer skuAddedFlag = sku.getCanSell() ? 1 : 0;
            Integer vendibility = sku.getCanSell() ? 1 : 0;
            //更新linkedmall供应商-sku
            List<GoodsInfo> oldGoodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder()
                    .delFlag(DeleteFlag.NO.toValue())
                    .goodsSource(GoodsSource.LINKED_MALL.toValue())
                    .thirdPlatformSpuId(thirdPlatformSpuId)
                    .thirdPlatformSkuId(Collections.singletonList(thirdPlatformSkuId)).build());
            if (CollectionUtils.isEmpty(oldGoodsInfos)) {
                throw new SbcRuntimeException("K-030010", new Object[]{sku.getSkuId()});
            }
            GoodsInfo oldGoodsInfo = oldGoodsInfos.get(0);
            oldGoodsInfo.setSupplyPrice(new BigDecimal(sku.getPriceCent()).divide(new BigDecimal("100")));
            oldGoodsInfo.setUpdateTime(LocalDateTime.now());
            if (!skuAddedFlag.equals(oldGoodsInfo.getAddedFlag())) {
                oldGoodsInfo.setAddedFlag(skuAddedFlag);
                oldGoodsInfo.setAddedTime(LocalDateTime.now());
            }
            goodsInfoRepository.save(oldGoodsInfo);
            log.info("修改供应商linkedmall-sku，" + "所属linkedmall商品spuId:" + thirdPlatformSpuId + ",linkedmall商品skuId:" + thirdPlatformSkuId);
            //更改商品库linkedmall-sku
            StandardSku oldStandardSku = standardSkuRepository.findByDelFlagAndGoodsSourceAndThirdPlatformSpuIdAndThirdPlatformSkuId(DeleteFlag.NO, GoodsSource.LINKED_MALL.toValue(), thirdPlatformSpuId, thirdPlatformSkuId);
            if (oldStandardSku == null) {
                throw new SbcRuntimeException("K-030011", new Object[]{sku.getSkuId()});
            }
            oldStandardSku.setSupplyPrice(new BigDecimal(sku.getPriceCent()).divide(new BigDecimal("100")));
            oldStandardSku.setUpdateTime(LocalDateTime.now());
            if (!skuAddedFlag.equals(oldStandardSku.getAddedFlag())) {
                oldStandardSku.setAddedFlag(skuAddedFlag);
            }
            standardSkuRepository.save(oldStandardSku);
            log.info("修改商品库linkedmall-sku，" + "所属linkedmall商品spuId:" + thirdPlatformSpuId + ",linkedmall商品skuId:" + thirdPlatformSkuId);
            //更新商家导入的linkedmall-sku
            List<GoodsInfo> storeGoodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.SELLER.toValue()).thirdPlatformType(ThirdPlatformType.LINKED_MALL.toValue()).thirdPlatformSpuId(thirdPlatformSpuId).thirdPlatformSkuId(Collections.singletonList(thirdPlatformSkuId)).build());
            if (storeGoodsInfos != null && storeGoodsInfos.size() > 0) {
                for (GoodsInfo storeGoodsInfo : storeGoodsInfos) {
                    if (!vendibility.equals(storeGoodsInfo.getVendibility())) {
                        storeGoodsInfo.setVendibility(vendibility);
                        esGoodsInfoIds.add(storeGoodsInfo.getGoodsInfoId());
                    }
                    storeGoodsInfo.setSupplyPrice(new BigDecimal(sku.getPriceCent()).divide(new BigDecimal("100")));

                }
                goodsInfoRepository.saveAll(storeGoodsInfos);
                log.info("修改所有商家导入的linkedmall-sku，" + "所属linkedmall商品spuId:" + thirdPlatformSpuId + ",linkedmall商品skuId:" + thirdPlatformSkuId);
            }

        }
        //重新计算商家导入的linkedmall-spu可售状态
        List<Goods> storeLinkeMallGoods =
                goodsService.findAll(GoodsQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.SELLER.toValue()).thirdPlatformType(ThirdPlatformType.LINKED_MALL).thirdPlatformSpuId(thirdPlatformSpuId).build());
        if (storeLinkeMallGoods != null && storeLinkeMallGoods.size() > 0) {
            List<GoodsInfo> storeGoodsInfos =
                    goodsInfoRepository.findAll(GoodsInfoQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsId(storeLinkeMallGoods.get(0).getGoodsId()).build().getWhereCriteria());
            if (CollectionUtils.isNotEmpty(storeGoodsInfos)) {

                Integer newVendibility = storeGoodsInfos.stream().anyMatch(v -> Integer.valueOf(1).equals(v.getVendibility())) ? 1 : 0;
                if (!newVendibility.equals(storeLinkeMallGoods.get(0).getVendibility())) {
                    for (Goods storeLinkeMallGood : storeLinkeMallGoods) {
                        storeLinkeMallGood.setVendibility(newVendibility);
                        storeLinkeMallGood.setUpdateTime(LocalDateTime.now());
                    }
                    goodsRepository.saveAll(storeLinkeMallGoods);
                }
            }
        }
        //更改spu上下架状态
        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder()
                .delFlag(DeleteFlag.NO.toValue())
                .goodsSource(GoodsSource.LINKED_MALL.toValue())
                .thirdPlatformSpuId(thirdPlatformSpuId).build());
        if (CollectionUtils.isNotEmpty(goodsInfos)) {
            Integer spuAddedFlag = 0;
            long count = goodsInfos.stream().filter(v -> v.getAddedFlag() == 1).count();
            if (count > 0) {
                if (count == goodsInfos.size()) {
                    spuAddedFlag = 1;
                } else {
                    spuAddedFlag = 2;
                }
            }
            List<Goods> linkedMallGoodsList = goodsService.findAll(GoodsQueryRequest.builder()
                    .delFlag(DeleteFlag.NO.toValue())
                    .goodsSource(GoodsSource.LINKED_MALL.toValue())
                    .thirdPlatformSpuId(thirdPlatformSpuId).build());
            if (CollectionUtils.isNotEmpty(linkedMallGoodsList)) {
                Goods linkedMallGoods = linkedMallGoodsList.get(0);
                if (!spuAddedFlag.equals(linkedMallGoods.getAddedFlag())) {
                    linkedMallGoods.setAddedFlag(spuAddedFlag);
                    linkedMallGoods.setAddedTime(LocalDateTime.now());
                    goodsRepository.save(linkedMallGoods);
                }
            }
            //更改商品库spu上下架状态
            StandardGoods linkedMallStandardGoods =
                    standardGoodsRepository.findByDelFlagAndGoodsSourceAndThirdPlatformSpuId(DeleteFlag.NO, GoodsSource.LINKED_MALL.toValue(), thirdPlatformSpuId);
            if (linkedMallStandardGoods != null) {
                if (!spuAddedFlag.equals(linkedMallStandardGoods.getAddedFlag())) {
                    linkedMallStandardGoods.setAddedFlag(spuAddedFlag);
                    standardGoodsRepository.save(linkedMallStandardGoods);
                }
                standardIds.add(linkedMallStandardGoods.getGoodsId());
            }
        }
        LinkedMallGoodsModifyResponse response = new LinkedMallGoodsModifyResponse();
        response.setGoodsInfoIds(esGoodsInfoIds);
        response.setStandardIds(standardIds);
        return response;
    }

    /**
     * 删除linkedmall商品
     */
    @Transactional
    public LinkedMallGoodsDelResponse linkedMallGoodsDel(LinkedMallItemDelDTO linkedMallItemDelDTO) {
        ArrayList<String> esGoodsInfoIds = new ArrayList<>();
        List<String> standardIds = new ArrayList<>();
        List<String> delStandardIds = new ArrayList<>();
        String thirdPlatformSpuId = linkedMallItemDelDTO.getItemId().toString();
        for (Long thirdPlatformSkuId : linkedMallItemDelDTO.getSkuIdList()) {
            //删除linkedmall供应商-sku
            List<GoodsInfo> oldGoodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder()
                    .delFlag(DeleteFlag.NO.toValue())
                    .goodsSource(GoodsSource.LINKED_MALL.toValue())
                    .thirdPlatformSpuId(thirdPlatformSpuId)
                    .thirdPlatformSkuId(Collections.singletonList(thirdPlatformSkuId.toString())).build());
            if (oldGoodsInfos != null && oldGoodsInfos.size() > 0) {
                for (GoodsInfo oldGoodsInfo : oldGoodsInfos) {
                    oldGoodsInfo.setDelFlag(DeleteFlag.YES);
                }
                goodsInfoRepository.saveAll(oldGoodsInfos);
                goodsInfoSpecDetailRelRepository.deleteByGoodsInfoIds(Collections.singletonList(oldGoodsInfos.get(0).getGoodsInfoId()), oldGoodsInfos.get(0).getGoodsId());
                log.info("删除linkedmall供应商-sku，所属linkedmall商品spuId:" + linkedMallItemDelDTO.getItemId() + ",skuId:" + thirdPlatformSkuId);
            }
            //删除商品库linkedmall-sku
            StandardSku oldStandardSku = standardSkuRepository.findByDelFlagAndGoodsSourceAndThirdPlatformSpuIdAndThirdPlatformSkuId(DeleteFlag.NO, GoodsSource.LINKED_MALL.toValue(), thirdPlatformSpuId, thirdPlatformSkuId.toString());
            if (oldStandardSku != null) {
                oldStandardSku.setDelFlag(DeleteFlag.YES);
                standardSkuRepository.save(oldStandardSku);
                standardSkuSpecDetailRelRepository.deleteByGoodsInfoIds(Collections.singletonList(oldStandardSku.getGoodsInfoId()), oldStandardSku.getGoodsId());
                log.info("删除商品库linkedmall-sku，所属linkedmall商品spuId:" + linkedMallItemDelDTO.getItemId() + ",skuId:" + thirdPlatformSkuId);
            }
            //禁售商家导入的linkedmall-sku
            List<GoodsInfo> storeGoodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).vendibility(1).goodsSource(GoodsSource.SELLER.toValue()).thirdPlatformType(ThirdPlatformType.LINKED_MALL.toValue()).thirdPlatformSpuId(thirdPlatformSpuId).thirdPlatformSkuId(Collections.singletonList(thirdPlatformSkuId.toString())).build());
            if (storeGoodsInfos != null && storeGoodsInfos.size() > 0) {
                for (GoodsInfo storeGoodsInfo : storeGoodsInfos) {
                    storeGoodsInfo.setVendibility(0);
                    storeGoodsInfo.setUpdateTime(LocalDateTime.now());
                    esGoodsInfoIds.add(storeGoodsInfo.getGoodsInfoId());
                }
                goodsInfoRepository.saveAll(storeGoodsInfos);
            }

        }
        List<GoodsInfo> linkedMallGoodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.LINKED_MALL.toValue()).thirdPlatformSpuId(thirdPlatformSpuId).build());

        //删除linkemall供应商——spu
        if (linkedMallGoodsInfos == null || linkedMallGoodsInfos.size() < 1) {
            List<Goods> oldGoodsList = goodsService.findAll(GoodsQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.LINKED_MALL.toValue()).thirdPlatformSpuId(thirdPlatformSpuId).build());
            if (oldGoodsList != null && oldGoodsList.size() > 0) {
                Goods oldGoods = oldGoodsList.get(0);
                oldGoods.setDelFlag(DeleteFlag.YES);
                goodsRepository.save(oldGoods);
                goodsSpecRepository.deleteByGoodsId(oldGoods.getGoodsId());
                goodsSpecDetailRepository.deleteByGoodsId(oldGoods.getGoodsId());
                log.info("删除供应商linkedmall-spu，spuId:" + linkedMallItemDelDTO.getItemId());
            }
        } else {
            //spu上下架状态
            Integer spuAddedFlag = 0;
            long count = linkedMallGoodsInfos.stream().filter(v -> v.getAddedFlag() == 1).count();
            if (count > 0) {
                if (linkedMallGoodsInfos.size() == count) {
                    spuAddedFlag = 1;
                } else {
                    spuAddedFlag = 2;
                }
            }
            //更新linkedamll供应商-spu上下架状态
            Goods linkedMallGoods = goodsRepository.findByDelFlagAndGoodsSourceAndThirdPlatformSpuId(DeleteFlag.NO, GoodsSource.LINKED_MALL.toValue(), thirdPlatformSpuId);
            if (linkedMallGoods != null && !spuAddedFlag.equals(linkedMallGoods.getAddedFlag())) {
                linkedMallGoods.setAddedFlag(spuAddedFlag);
                linkedMallGoods.setAddedTime(LocalDateTime.now());
                linkedMallGoods.setUpdateTime(LocalDateTime.now());
                goodsRepository.save(linkedMallGoods);
            }
        }
//删除商品库linkemall——spu
        List<StandardSku> linkedMallStandardSkus = standardSkuRepository.findByDelFlagAndGoodsSourceAndThirdPlatformSpuId(DeleteFlag.NO, GoodsSource.LINKED_MALL.toValue(), thirdPlatformSpuId);
        if (linkedMallStandardSkus == null || linkedMallStandardSkus.size() < 1) {
            StandardGoods oldStandardGoods = standardGoodsRepository.findByDelFlagAndGoodsSourceAndThirdPlatformSpuId(DeleteFlag.NO, GoodsSource.LINKED_MALL.toValue(), thirdPlatformSpuId);
            if (oldStandardGoods != null) {
                oldStandardGoods.setDelFlag(DeleteFlag.YES);
                standardGoodsRepository.save(oldStandardGoods);
                standardSpecRepository.deleteByGoodsId(oldStandardGoods.getGoodsId());
                standardSpecDetailRepository.deleteByGoodsId(oldStandardGoods.getGoodsId());
                log.info("删除商品库linkedmall-spu，spuId:" + linkedMallItemDelDTO.getItemId());
                delStandardIds.add(oldStandardGoods.getGoodsId());
            }
        } else {
            //spu上下架状态
            Integer spuAddedFlag = 0;
            long count = linkedMallStandardSkus.stream().filter(v -> Integer.valueOf(1).equals(v.getAddedFlag())).count();
            if (count > 0) {
                if (linkedMallStandardSkus.size() == count) {
                    spuAddedFlag = 1;
                } else {
                    spuAddedFlag = 2;
                }
            }
            //更新商品库linkedamll-spu上下架状态
            StandardGoods linkedMallStandardGoods = standardGoodsRepository.findByDelFlagAndGoodsSourceAndThirdPlatformSpuId(DeleteFlag.NO, GoodsSource.LINKED_MALL.toValue(), thirdPlatformSpuId);
            if (linkedMallStandardGoods != null && !spuAddedFlag.equals(linkedMallStandardGoods.getAddedFlag())) {
                linkedMallStandardGoods.setAddedFlag(spuAddedFlag);
                linkedMallStandardGoods.setUpdateTime(LocalDateTime.now());
                standardGoodsRepository.save(linkedMallStandardGoods);
                standardIds.add(linkedMallStandardGoods.getGoodsId());
            }
        }
        //重新计算商家导入的linkedmall-spu可售状态
        List<Goods> storeLinkeMallGoods =
                goodsService.findAll(GoodsQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.SELLER.toValue()).thirdPlatformType(ThirdPlatformType.LINKED_MALL).thirdPlatformSpuId(thirdPlatformSpuId).build());
        if (storeLinkeMallGoods != null && storeLinkeMallGoods.size() > 0) {
            List<GoodsInfo> storeGoodsInfos =
                    goodsInfoRepository.findAll(GoodsInfoQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsId(storeLinkeMallGoods.get(0).getGoodsId()).build().getWhereCriteria());
            if (CollectionUtils.isNotEmpty(storeGoodsInfos)) {
                Integer newVendibility = storeGoodsInfos.stream().anyMatch(v -> Integer.valueOf(1).equals(v.getVendibility())) ? 1 : 0;
                if (!newVendibility.equals(storeLinkeMallGoods.get(0).getVendibility())) {
                    for (Goods storeLinkeMallGood : storeLinkeMallGoods) {
                        storeLinkeMallGood.setVendibility(newVendibility);
                        storeLinkeMallGood.setUpdateTime(LocalDateTime.now());
                    }
                    goodsRepository.saveAll(storeLinkeMallGoods);
                }
            }
        }
        LinkedMallGoodsDelResponse response = new LinkedMallGoodsDelResponse();
        response.setStandardIds(standardIds);
        response.setDelStandardIds(delStandardIds);
        response.setGoodsInfoIds(esGoodsInfoIds);
        return response;
    }

    /**
     *  删除商家，商品库所有linkedmall商品
     */
    @Transactional
    public void delAllLinkedMallGoods() {
        List<GoodsInfo> delGoodsInfos = new ArrayList<>();
        //所有linkedmall供应商-sku
        List<GoodsInfo> goodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.LINKED_MALL.toValue()).build());
        if (goodsInfos != null && goodsInfos.size() > 0) {
            for (GoodsInfo goodsInfo : goodsInfos) {
                goodsInfo.setDelFlag(DeleteFlag.YES);
                goodsInfo.setUpdateTime(LocalDateTime.now());
            }
            delGoodsInfos.addAll(goodsInfos);
        }
        //商家导入的linkedmall-sku
        List<GoodsInfo> storeGoodsInfos = goodsInfoService.findByParams(GoodsInfoQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.SELLER.toValue()).thirdPlatformType(ThirdPlatformType.LINKED_MALL.toValue()).build());
        if (storeGoodsInfos != null && storeGoodsInfos.size() > 0) {
            for (GoodsInfo storeGoodsInfo : storeGoodsInfos) {
                storeGoodsInfo.setDelFlag(DeleteFlag.YES);
                storeGoodsInfo.setUpdateTime(LocalDateTime.now());
            }
            delGoodsInfos.addAll(storeGoodsInfos);
        }
        goodsInfoRepository.saveAll(delGoodsInfos);
        goodsInfoSpecDetailRelRepository.deleteByGoodsInfoIds(delGoodsInfos.stream().map(v -> v.getGoodsInfoId()).collect(Collectors.toList()));
        //删除商品库linkedmall-sku
        List<StandardSku> standardSkus = standardSkuRepository.findByDelFlagAndGoodsSource(DeleteFlag.NO, GoodsSource.LINKED_MALL.toValue());
        if (standardSkus != null && standardSkus.size() > 0) {
            for (StandardSku skus : standardSkus) {
                skus.setDelFlag(DeleteFlag.YES);
                skus.setUpdateTime(LocalDateTime.now());
            }
            standardSkuRepository.saveAll(standardSkus);
            List<String> skuIds = standardSkus.stream().map(v -> v.getGoodsInfoId()).collect(Collectors.toList());
            standardSkuSpecDetailRelRepository.deleteByGoodsInfoIds(skuIds);
        }

        //linkemall供应商——spu
        ArrayList<Goods> delGoods = new ArrayList<>();
        List<Goods> goodsList = goodsService.findAll(GoodsQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.LINKED_MALL.toValue()).build());
        if (goodsList != null && goodsList.size() > 0) {
            for (Goods goods : goodsList) {
                goods.setDelFlag(DeleteFlag.YES);
                goods.setUpdateTime(LocalDateTime.now());
            }
            delGoods.addAll(goodsList);
        }
        //商家导入的linkemall供应商——spu
        List<Goods> storeGoodsList = goodsService.findAll(GoodsQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.SELLER.toValue()).thirdPlatformType(ThirdPlatformType.LINKED_MALL).build());
        if (storeGoodsList != null && storeGoodsList.size() > 0) {
            for (Goods goods : storeGoodsList) {
                goods.setDelFlag(DeleteFlag.YES);
                goods.setUpdateTime(LocalDateTime.now());
            }
            delGoods.addAll(storeGoodsList);
        }
        if (delGoods.size() > 0) {
            goodsRepository.saveAll(delGoods);
            List<String> delGoodsIds = delGoods.stream().map(v -> v.getGoodsId()).collect(Collectors.toList());
            goodsSpecRepository.deleteByGoodsIds(delGoodsIds);
            goodsSpecDetailRepository.deleteByGoodsIds(delGoodsIds);
        }
        //删除商品库linkemall——spu
        List<StandardGoods> standardGoodsList = standardGoodsRepository.findByDelFlagAndGoodsSource(DeleteFlag.NO, GoodsSource.LINKED_MALL.toValue());
        if (standardGoodsList != null && standardGoodsList.size() > 0) {
            for (StandardGoods standardGoods : standardGoodsList) {
                standardGoods.setDelFlag(DeleteFlag.YES);
                standardGoods.setUpdateTime(LocalDateTime.now());
            }
            standardGoodsRepository.saveAll(standardGoodsList);
            List<String> delGoodsIds = standardGoodsList.stream().map(v -> v.getGoodsId()).collect(Collectors.toList());
            goodsSpecRepository.deleteByGoodsIds(delGoodsIds);
            goodsSpecDetailRepository.deleteByGoodsIds(delGoodsIds);
        }


    }

    /**
     * 填充可售性
     *
     * @param goodsInfoVOS
     */
    public void fillGoodsVendibility(List<GoodsInfoVO> goodsInfoVOS) {
        //是否含linkedMall商品
        if (CollectionUtils.isEmpty(goodsInfoVOS)
                || goodsInfoVOS.stream().noneMatch(i -> ThirdPlatformType.LINKED_MALL.equals(i.getThirdPlatformType()))) {
            return;
        }
        ThirdPlatformConfigResponse response = thirdPlatformConfigQueryProvider.get(
                ThirdPlatformConfigByTypeRequest.builder().configType(ConfigType.THIRD_PLATFORM_LINKED_MALL.toValue()).build()).getContext();
        if (response == null) {
            return;
        }
        if (Constants.no.equals(response.getStatus())) {
            goodsInfoVOS.stream()
                    .filter(i -> ThirdPlatformType.LINKED_MALL.equals(i.getThirdPlatformType()) && Constants.yes.equals(i.getVendibility()))
                    .forEach(i -> i.setVendibility(Constants.no));
        }
    }
    /**
     * 删除linkedmall商品，linkedmall删除接口回调失败，手动删除
     * @return
     */
    @Transactional
    public List<String> delLinkedMallGoods(Map<String, List<String>> newLinkedMallGoods) {
        ArrayList<String> updateEs = new ArrayList<>();
        ArrayList<String> updateVendibility = new ArrayList<>();
        List<GoodsInfo> linkedMallGoodsInfos = goodsInfoRepository.findAll(GoodsInfoQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.LINKED_MALL.toValue()).build().getWhereCriteria());
        for (GoodsInfo linkedMallGoodsInfo : linkedMallGoodsInfos) {
            List<String> thirdSkuIds = newLinkedMallGoods.get(linkedMallGoodsInfo.getThirdPlatformSpuId());
            if (thirdSkuIds != null && thirdSkuIds.size() > 0) {
                if (!thirdSkuIds.contains(linkedMallGoodsInfo.getThirdPlatformSkuId())) {
                    //删除linkedmall供应商sku
                    goodsInfoRepository.deleteByGoodsInfoIds(Collections.singletonList(linkedMallGoodsInfo.getGoodsInfoId()));
                }
            } else {
                //删除linkedmall供应商spu
                goodsRepository.delByGoodsSourceAndThirdPlatformSpuId(GoodsSource.LINKED_MALL.toValue(), linkedMallGoodsInfo.getThirdPlatformSpuId());
                goodsInfoRepository.delByGoodsSourceAndThirdPlatformSpuId(GoodsSource.LINKED_MALL.toValue(), linkedMallGoodsInfo.getThirdPlatformSpuId());
            }
        }
        //删除商品库linkedmall-sku
        List<StandardSku> standardLinkedMallSkus = standardSkuRepository.findByDelFlagAndGoodsSource(DeleteFlag.NO, GoodsSource.LINKED_MALL.toValue());
        for (StandardSku standardSku : standardLinkedMallSkus) {
            List<String> thirdSkuIds = newLinkedMallGoods.get(standardSku.getThirdPlatformSpuId());
            if (thirdSkuIds != null && thirdSkuIds.size() > 0) {
                if (!thirdSkuIds.contains(standardSku.getThirdPlatformSkuId())) {
                    //删除商品库linkedmall-sku
                    standardSkuRepository.deleteByGoodsInfoId(standardSku.getGoodsInfoId());
                    //禁售商家导入的linkedmall商品
                    List<GoodsInfo> storeGoodsInfos = goodsInfoRepository.findAll(GoodsInfoQueryRequest.builder()
                            .delFlag(DeleteFlag.NO.toValue())
                            .goodsSource(GoodsSource.SELLER.toValue())
                            .thirdPlatformType(ThirdPlatformType.LINKED_MALL.toValue())
                            .thirdPlatformSpuId(standardSku.getThirdPlatformSpuId())
                            .thirdPlatformSkuId(Collections.singletonList(standardSku.getThirdPlatformSkuId()))
                            .build().getWhereCriteria());
                    for (GoodsInfo storeGoodsInfo : storeGoodsInfos) {
                        storeGoodsInfo.setVendibility(0);
                        storeGoodsInfo.setUpdateTime(LocalDateTime.now());
                    }
                    goodsInfoRepository.saveAll(storeGoodsInfos);
                    updateEs.addAll(storeGoodsInfos.stream().map(v -> v.getGoodsInfoId()).collect(Collectors.toList()));
                    if (!updateVendibility.contains(standardSku.getThirdPlatformSpuId())) {
                        updateVendibility.add(standardSku.getThirdPlatformSpuId());
                    }
                }
            } else {
                //删除商品库linkedmall-spu
                standardGoodsRepository.delByGoodsSourceAndThirdPlatformSpuId(GoodsSource.LINKED_MALL.toValue(), standardSku.getThirdPlatformSpuId());
                standardSkuRepository.delByGoodsSourceAndThirdPlatformSpuId(GoodsSource.LINKED_MALL.toValue(), standardSku.getThirdPlatformSpuId());
                //禁售商家导入的linkedmall-sku
                List<GoodsInfo> storeGoodsInfos =
                        goodsInfoRepository.findAll(GoodsInfoQueryRequest.builder()
                                .delFlag(DeleteFlag.NO.toValue())
                                .goodsSource(GoodsSource.SELLER.toValue())
                                .thirdPlatformType(ThirdPlatformType.LINKED_MALL.toValue())
                                .thirdPlatformSpuId(standardSku.getThirdPlatformSpuId())
                                .build().getWhereCriteria());
                if (storeGoodsInfos != null && storeGoodsInfos.size() > 0) {
                    for (GoodsInfo storeGoodsInfo : storeGoodsInfos) {
                        storeGoodsInfo.setVendibility(0);
                        storeGoodsInfo.setUpdateTime(LocalDateTime.now());
                    }
                    goodsInfoRepository.saveAll(storeGoodsInfos);
                    updateEs.addAll(storeGoodsInfos.stream().map(v -> v.getGoodsInfoId()).collect(Collectors.toList()));
                }
                //禁售商家导入的linkedmall-spu
                goodsRepository.vendibilityByGoodsSourceAndThirdPlatformTypeAndThirdPlatformSpuId(0, GoodsSource.SELLER.toValue(), ThirdPlatformType.LINKED_MALL, standardSku.getThirdPlatformSpuId());
            }
        }
        for (String thirdPlatformSpuId : updateVendibility) {
            //重新计算商家导入的linkedmall-spu可售状态
            List<Goods> storeLinkeMallGoods =
                    goodsService.findAll(GoodsQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsSource(GoodsSource.SELLER.toValue()).thirdPlatformType(ThirdPlatformType.LINKED_MALL).thirdPlatformSpuId(thirdPlatformSpuId).build());
            if (storeLinkeMallGoods != null && storeLinkeMallGoods.size() > 0) {
                List<GoodsInfo> storeGoodsInfos =
                        goodsInfoRepository.findAll(GoodsInfoQueryRequest.builder().delFlag(DeleteFlag.NO.toValue()).goodsId(storeLinkeMallGoods.get(0).getGoodsId()).build().getWhereCriteria());
                Integer newVendibility = storeGoodsInfos.stream().anyMatch(v -> Integer.valueOf(1).equals(v.getVendibility())) ? 1 : 0;
                if (!newVendibility.equals(storeLinkeMallGoods.get(0).getVendibility())) {
                    for (Goods storeLinkeMallGood : storeLinkeMallGoods) {
                        storeLinkeMallGood.setVendibility(newVendibility);
                        storeLinkeMallGood.setUpdateTime(LocalDateTime.now());
                    }
                    goodsRepository.saveAll(storeLinkeMallGoods);
                }
            }
        }
        return updateEs;
    }


    /**
     * 填充LM商品sku的库存
     *
     * @param goodsInfoList 商品SKu列表
     */
    public void fillLmStock(List<GoodsInfoVO> goodsInfoList) {
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = goodsInfoList.stream()
                .filter(v -> Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(v.getGoodsSource()) && !StringUtils.isNotBlank(v.getThirdPlatformSpuId()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(itemIds)) {
            return;
        }
        List<QueryItemInventoryResponse.Item> stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
        if (CollectionUtils.isNotEmpty(stocks)) {
            for (GoodsInfoVO goodsInfoVO : goodsInfoList) {
                for (QueryItemInventoryResponse.Item spuStock : stocks) {
                    Optional<QueryItemInventoryResponse.Item.Sku> stock = spuStock.getSkuList().stream()
                            .filter(v -> String.valueOf(spuStock.getItemId()).equals(goodsInfoVO.getThirdPlatformSpuId())
                                    && String.valueOf(v.getSkuId()).equals(goodsInfoVO.getThirdPlatformSkuId()))
                            .findFirst();
                    stock.ifPresent(sku -> goodsInfoVO.setStock(sku.getInventory().getQuantity()));
                }
            }
        }
    }
}
