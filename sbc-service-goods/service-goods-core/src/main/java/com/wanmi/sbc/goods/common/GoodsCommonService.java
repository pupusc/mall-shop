package com.wanmi.sbc.goods.common;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.OsUtil;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.MaxDiscountPaidCardRequest;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import com.wanmi.sbc.goods.api.request.common.GoodsCommonBatchAddRequest;
import com.wanmi.sbc.goods.api.request.common.InfoForPurchaseRequest;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedBatchValidateRequest;
import com.wanmi.sbc.goods.api.response.common.GoodsInfoForPurchaseResponse;
import com.wanmi.sbc.goods.bean.dto.BatchGoodsImageDTO;
import com.wanmi.sbc.goods.bean.dto.BatchGoodsInfoDTO;
import com.wanmi.sbc.goods.bean.dto.BatchGoodsSpecDTO;
import com.wanmi.sbc.goods.bean.dto.BatchGoodsSpecDetailDTO;
import com.wanmi.sbc.goods.bean.enums.*;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.goods.goodsrestrictedsale.service.GoodsRestrictedSaleService;
import com.wanmi.sbc.goods.images.GoodsImage;
import com.wanmi.sbc.goods.images.GoodsImageRepository;
import com.wanmi.sbc.goods.info.model.root.Goods;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.info.service.GoodsCacheService;
import com.wanmi.sbc.goods.marketing.model.data.GoodsMarketing;
import com.wanmi.sbc.goods.marketing.service.GoodsMarketingService;
import com.wanmi.sbc.goods.price.model.root.GoodsCustomerPrice;
import com.wanmi.sbc.goods.price.repository.GoodsCustomerPriceRepository;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpec;
import com.wanmi.sbc.goods.spec.model.root.GoodsSpecDetail;
import com.wanmi.sbc.goods.spec.repository.GoodsInfoSpecDetailRelRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecDetailRepository;
import com.wanmi.sbc.goods.spec.repository.GoodsSpecRepository;
import com.wanmi.sbc.goods.storecate.model.root.StoreCateGoodsRela;
import com.wanmi.sbc.goods.storecate.repository.StoreCateGoodsRelaRepository;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.provider.thirdaddress.ThirdAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressListRequest;
import com.wanmi.sbc.setting.api.response.thirdaddress.ThirdAddressListResponse;
import com.wanmi.sbc.setting.bean.vo.ThirdAddressVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品公共服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
@Transactional(readOnly = true)
public class GoodsCommonService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private GoodsImageRepository goodsImageRepository;

    @Autowired
    private GoodsSpecRepository goodsSpecRepository;

    @Autowired
    private GoodsSpecDetailRepository goodsSpecDetailRepository;

    @Autowired
    private GoodsInfoSpecDetailRelRepository goodsInfoSpecDetailRelRepository;

    @Autowired
    private StoreCateGoodsRelaRepository storeCateGoodsRelaRepository;

    @Autowired
    private OsUtil osUtil;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    @Autowired
    private GoodsCacheService goodsCacheService;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private GoodsMarketingService goodsMarketingService;

    @Autowired
    private GoodsCustomerPriceRepository goodsCustomerPriceRepository;

    @Autowired
    private GoodsRestrictedSaleService goodsRestrictedSaleService;

    @Autowired
    private ThirdAddressQueryProvider thirdAddressQueryProvider;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    /**
     * 批量导入商品数据
     * @param request 商品批量信息
     * @return 批量新增的skuId
     */
    @Transactional
    public List<String> batchAdd(GoodsCommonBatchAddRequest request){
        Map<String, List<BatchGoodsInfoDTO>> skus = request.getGoodsInfoList().stream()
                .collect(Collectors.groupingBy(BatchGoodsInfoDTO::getMockGoodsId));

        Map<String, List<BatchGoodsSpecDTO>> allSpecs = new HashMap<>();
        if (CollectionUtils.isNotEmpty(request.getSpecList())) {
            allSpecs.putAll(request.getSpecList().stream()
                    .collect(Collectors.groupingBy(BatchGoodsSpecDTO::getMockGoodsId)));
        }

        Map<String, List<BatchGoodsSpecDetailDTO>> allSpecDetails = new HashMap<>();
        if (CollectionUtils.isNotEmpty(request.getSpecList())) {
            allSpecDetails.putAll(request.getSpecDetailList().stream()
                    .collect(Collectors.groupingBy(BatchGoodsSpecDetailDTO::getMockGoodsId)));
        }

        Map<String, List<BatchGoodsImageDTO>> images = new HashMap<>();
        if (CollectionUtils.isNotEmpty(request.getImageList())) {
            images.putAll(request.getImageList().stream()
                    .collect(Collectors.groupingBy(BatchGoodsImageDTO::getMockGoodsId)));
        }

        List<String> newSkuIds = new ArrayList<>();
        request.getGoodsList().forEach(goods -> {
            goods.setCreateTime(LocalDateTime.now());
            goods.setAddedTime(goods.getCreateTime());
            goods.setUpdateTime(goods.getCreateTime());
            goods.setCustomFlag(Constants.no);
            goods.setLevelDiscountFlag(Constants.no);
            goods.setDelFlag(DeleteFlag.NO);
            goods.setPriceType(GoodsPriceType.MARKET.toValue());

            List<BatchGoodsInfoDTO> goodsInfoList = skus.get(goods.getGoodsNo());
            goods.setMoreSpecFlag(Constants.no);
            if (goodsInfoList.stream().anyMatch(goodsInfo -> CollectionUtils.isNotEmpty(goodsInfo.getMockSpecDetailIds()))) {
                goods.setMoreSpecFlag(Constants.yes);
            }

            //判定上下架
            long yes_addedFlag = goodsInfoList.stream().filter(goodsInfo -> AddedFlag.YES.toValue() == goodsInfo.getAddedFlag()).count();
            goods.setAddedFlag(AddedFlag.PART.toValue());
            if (goodsInfoList.size() == yes_addedFlag) {
                goods.setAddedFlag(AddedFlag.YES.toValue());
            } else if (yes_addedFlag == 0) {
                goods.setAddedFlag(AddedFlag.NO.toValue());
            }

            Goods tempGoods = KsBeanUtil.convert(goods, Goods.class);
            this.setCheckState(tempGoods);
            goods.setAuditStatus(tempGoods.getAuditStatus());
            tempGoods.setGoodsSalesNum(0L);
            tempGoods.setGoodsCollectNum(0L);
            tempGoods.setGoodsEvaluateNum(0L);
            tempGoods.setGoodsFavorableCommentNum(0L);
            tempGoods.setStock(goodsInfoList.stream().filter(g -> g.getStock() != null).mapToLong(BatchGoodsInfoDTO::getStock).sum());
            String goodsId = goodsRepository.save(tempGoods).getGoodsId();

            List<BatchGoodsSpecDTO> specs = allSpecs.getOrDefault(goods.getMockGoodsId(), Collections.emptyList());
            List<BatchGoodsSpecDetailDTO> specDetails = allSpecDetails.getOrDefault(goods.getMockGoodsId(), Collections.emptyList());

            //如果是多规格
            if (Constants.yes.equals(goods.getMoreSpecFlag())) {
                //新增含有规格值的规格
                specs.stream()
                        .filter(goodsSpec -> specDetails.stream().anyMatch(goodsSpecDetail ->
                                goodsSpecDetail.getMockSpecId().equals(goodsSpec.getMockSpecId())))
                        .forEach(goodsSpec -> {
                            goodsSpec.setCreateTime(goods.getCreateTime());
                            goodsSpec.setUpdateTime(goods.getCreateTime());
                            goodsSpec.setGoodsId(goodsId);
                            goodsSpec.setDelFlag(DeleteFlag.NO);
                            goodsSpec.setSpecId(goodsSpecRepository.save(
                                    KsBeanUtil.convert(goodsSpec, GoodsSpec.class)).getSpecId());
                        });
                //新增规格值
                specDetails.forEach(goodsSpecDetail -> {
                    Optional<BatchGoodsSpecDTO> specOpt = specs.stream().filter(goodsSpec -> goodsSpec.getMockSpecId
                            ().equals(goodsSpecDetail.getMockSpecId())).findFirst();
                    if (specOpt.isPresent()) {
                        goodsSpecDetail.setCreateTime(goods.getCreateTime());
                        goodsSpecDetail.setUpdateTime(goods.getCreateTime());
                        goodsSpecDetail.setGoodsId(goodsId);
                        goodsSpecDetail.setDelFlag(DeleteFlag.NO);
                        goodsSpecDetail.setSpecId(specOpt.get().getSpecId());
                        goodsSpecDetail.setSpecDetailId(goodsSpecDetailRepository.save(
                                KsBeanUtil.convert(goodsSpecDetail, GoodsSpecDetail.class)).getSpecDetailId());
                    }
                });
            }

            goodsInfoList.forEach(goodsInfo -> {
                goodsInfo.setGoodsId(goodsId);
                goodsInfo.setGoodsInfoName(goods.getGoodsName());
                goodsInfo.setCreateTime(goods.getCreateTime());
                goodsInfo.setUpdateTime(goods.getCreateTime());
                goodsInfo.setAddedTime(goods.getCreateTime());
                goodsInfo.setDelFlag(goods.getDelFlag());
                goodsInfo.setCompanyInfoId(goods.getCompanyInfoId());
                goodsInfo.setPriceType(goods.getPriceType());
                goodsInfo.setCustomFlag(goods.getCustomFlag());
                goodsInfo.setLevelDiscountFlag(goods.getLevelDiscountFlag());
                goodsInfo.setCateId(goods.getCateId());
                goodsInfo.setBrandId(goods.getBrandId());
                goodsInfo.setStoreId(goods.getStoreId());
                goodsInfo.setAuditStatus(goods.getAuditStatus());
                goodsInfo.setCompanyType(goods.getCompanyType());
                goodsInfo.setAloneFlag(Boolean.FALSE);
                goodsInfo.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
                goodsInfo.setSaleType(goods.getSaleType());
                goodsInfo.setAddedTimingFlag(goods.getAddedTimingFlag());
                String skuId = goodsInfoRepository.save(KsBeanUtil.convert(goodsInfo, GoodsInfo.class))
                        .getGoodsInfoId();
                newSkuIds.add(skuId);
                //存储规格
                //如果是多规格,新增SKU与规格明细值的关联表
                if (Constants.yes.equals(goods.getMoreSpecFlag())) {
                    for (BatchGoodsSpecDTO spec : specs) {
                        if (goodsInfo.getMockSpecIds().contains(spec.getMockSpecId())) {
                            for (BatchGoodsSpecDetailDTO detail : specDetails) {
                                if (spec.getMockSpecId().equals(detail.getMockSpecId()) && goodsInfo
                                        .getMockSpecDetailIds().contains(detail.getMockSpecDetailId())) {
                                    GoodsInfoSpecDetailRel detailRel = new GoodsInfoSpecDetailRel();
                                    detailRel.setGoodsId(goodsId);
                                    detailRel.setGoodsInfoId(skuId);
                                    detailRel.setSpecId(spec.getSpecId());
                                    detailRel.setSpecDetailId(detail.getSpecDetailId());
                                    detailRel.setDetailName(detail.getDetailName());
                                    detailRel.setCreateTime(detail.getCreateTime());
                                    detailRel.setUpdateTime(detail.getUpdateTime());
                                    detailRel.setDelFlag(detail.getDelFlag());
                                    detailRel.setSpecName(spec.getSpecName());
                                    goodsInfoSpecDetailRelRepository.save(detailRel);
                                }
                            }
                        }
                    }
                }
            });

            if (CollectionUtils.isNotEmpty(goods.getStoreCateIds())) {
                goods.getStoreCateIds().forEach(cateId -> {
                    StoreCateGoodsRela rela = new StoreCateGoodsRela();
                    rela.setGoodsId(goodsId);
                    rela.setStoreCateId(cateId);
                    storeCateGoodsRelaRepository.save(rela);
                });
            }

            //批量保存
            List<BatchGoodsImageDTO> imageUrls = images.get(goods.getGoodsNo());
            if (CollectionUtils.isNotEmpty(imageUrls)) {
                imageUrls.forEach(img -> {
                    img.setGoodsId(goodsId);
                    img.setCreateTime(goods.getCreateTime());
                    img.setUpdateTime(goods.getCreateTime());
                    img.setDelFlag(goods.getDelFlag());
                    goodsImageRepository.save(KsBeanUtil.convert(img, GoodsImage.class));
                });
            }
        });
        return newSkuIds;
    }



    /**
     * 新增/编辑操作中，商品审核状态
     *
     * @param goods 商品
     */
    public void setCheckState(Goods goods) {
        //B2B模式直接审核通过
        if (osUtil.isB2b()) {
            goods.setAuditStatus(CheckStatus.CHECKED);
            goods.setSubmitTime(LocalDateTime.now());
            return;
        }
        //新增商品
        if (Objects.isNull(goods.getAuditStatus())) {
            if (Objects.equals(BoolFlag.NO, goods.getCompanyType())) {
                if (auditQueryProvider.isBossGoodsAudit().getContext().isAudit()) {
                    goods.setAuditStatus(CheckStatus.WAIT_CHECK);
                    return;
                }
                goods.setAuditStatus(CheckStatus.CHECKED);
                goods.setSubmitTime(LocalDateTime.now());
            } else {
                if (auditQueryProvider.isSupplierGoodsAudit().getContext().isAudit()) {
                    goods.setAuditStatus(CheckStatus.WAIT_CHECK);
                    return;
                }
                goods.setAuditStatus(CheckStatus.CHECKED);
                goods.setSubmitTime(LocalDateTime.now());
            }
        } else if (Objects.equals(CheckStatus.NOT_PASS, goods.getAuditStatus()) || Objects.equals(CheckStatus.FORBADE, goods.getAuditStatus())) {//审核未通过/禁售中商品
            //自营
            if (Objects.equals(BoolFlag.NO, goods.getCompanyType())) {
                if (auditQueryProvider.isBossGoodsAudit().getContext().isAudit()) {
                    goods.setAuditStatus(CheckStatus.WAIT_CHECK);
                } else {
                    goods.setAuditStatus(CheckStatus.CHECKED);
                    goods.setSubmitTime(LocalDateTime.now());
                }
            } else {//第三方
                if (auditQueryProvider.isSupplierGoodsAudit().getContext().isAudit()) {
                    goods.setAuditStatus(CheckStatus.WAIT_CHECK);
                } else {
                    goods.setAuditStatus(CheckStatus.CHECKED);
                    goods.setSubmitTime(LocalDateTime.now());
                }
            }

        }
    }

    public GoodsInfoForPurchaseResponse queryInfoForPurchase(InfoForPurchaseRequest request) {

        GoodsInfoForPurchaseResponse response = new GoodsInfoForPurchaseResponse();
        List<String> goodsInfoIds = request.getGoodsInfoIds();
        CustomerVO customer = request.getCustomer();

        // 查询商品信息、单品信息
        List<GoodsInfoVO> goodsInfoList = goodsCacheService.listGoodsInfosByIds(goodsInfoIds, Objects.nonNull(customer)
                && EnterpriseCheckState.CHECKED.equals(customer.getEnterpriseCheckState()) ? PriceType.ENTERPRISE_SKU : PriceType.SKU);
        if (CollectionUtils.isEmpty(goodsInfoList)) return response;

        List<GoodsVO> goodsList = goodsCacheService.listGoodsByIds(
                goodsInfoList.stream().map(i -> i.getGoodsId()).distinct().collect(Collectors.toList())
        );
        //如果是linkedmall商品，实时查库存,根据区域码查库存
        List<Long> itemIds = goodsList.stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .distinct()
                .collect(Collectors.toList());
        if (itemIds.size() > 0) {
            String thirdAddrId = null;
            if (request.getAreaId() != null) {
                List<ThirdAddressVO> thirdAddressList = thirdAddressQueryProvider.list(ThirdAddressListRequest.builder()
                        .platformAddrIdList(Collections.singletonList(Objects.toString(request.getAreaId())))
                        .thirdFlag(ThirdPlatformType.LINKED_MALL)
                        .build()).getContext().getThirdAddressList();
                if (CollectionUtils.isNotEmpty(thirdAddressList)) {
                    thirdAddrId = thirdAddressList.get(0).getThirdAddrId();
                }
            }
            List<QueryItemInventoryResponse.Item> stocks = null;
            stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, thirdAddrId == null ? "0" : thirdAddrId, null)).getContext();
            if (stocks != null) {
                for (GoodsInfoVO goodsInfo : goodsInfoList) {
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
                for (GoodsVO goodsVO : goodsList) {
                    if (ThirdPlatformType.LINKED_MALL.equals(goodsVO.getThirdPlatformType())) {
                        Optional<QueryItemInventoryResponse.Item> optional = stocks.stream()
                                .filter(v -> String.valueOf(v.getItemId()).equals(goodsVO.getThirdPlatformSpuId())).findFirst();
                        if (optional.isPresent()) {
                            Long spuStock = optional.get().getSkuList().stream()
                                    .map(v -> v.getInventory().getQuantity())
                                    .reduce(0L, (aLong, aLong2) -> aLong + aLong2);
                            goodsVO.setStock(spuStock);
                        }
                    }
                }
            }
        }

        goodsList.forEach(goods ->
                goodsInfoList.stream().filter(i -> i.getGoodsId().equals(goods.getGoodsId())).forEach(goodsInfo -> {
                    goodsInfo.setCateId(goods.getCateId());
                    goodsInfo.setBrandId(goods.getBrandId());
                    goodsInfo.setPriceType(goods.getPriceType());
                    goodsInfo.setCompanyType(goods.getCompanyType());
                    goodsInfo.setSalePrice(Objects.isNull(goodsInfo.getMarketPrice()) ? BigDecimal.ZERO : goodsInfo.getMarketPrice());
                    goodsInfo.setGoodsInfoImg(Objects.nonNull(goodsInfo.getGoodsInfoImg()) ?
                            goodsInfo.getGoodsInfoImg() : goods.getGoodsImg());
                })
        );

        List<Long> storeIds = goodsInfoList.stream().map(i -> i.getStoreId()).collect(Collectors.toList());

        // 商品起限定价
        if (Objects.nonNull(customer)) {
            List<GoodsRestrictedValidateVO> goodsRestrictedValidateVOS = goodsInfoList.stream()
                    .map(g -> GoodsRestrictedValidateVO.builder().num(g.getBuyCount()).skuId(g.getGoodsInfoId()).build()).collect(Collectors.toList());
            List<GoodsRestrictedPurchaseVO> goodsRestrictedPurchaseVOS = goodsRestrictedSaleService.getGoodsRestrictedInfo(
                    GoodsRestrictedBatchValidateRequest.builder().goodsRestrictedValidateVOS(goodsRestrictedValidateVOS).customerVO(customer).build());
            if (CollectionUtils.isNotEmpty(goodsRestrictedPurchaseVOS)) {
                Map<String, GoodsRestrictedPurchaseVO> purchaseMap = goodsRestrictedPurchaseVOS.stream().collect((Collectors.toMap(GoodsRestrictedPurchaseVO::getGoodsInfoId, g -> g)));
                goodsInfoList.stream().forEach(g -> {
                    GoodsRestrictedPurchaseVO goodsRestrictedPurchaseVO = purchaseMap.get(g.getGoodsInfoId());
                    if (Objects.nonNull(goodsRestrictedPurchaseVO)) {
                        if (DefaultFlag.YES.equals(goodsRestrictedPurchaseVO.getDefaultFlag())) {
                            g.setMaxCount(goodsRestrictedPurchaseVO.getRestrictedNum());
                            g.setCount(goodsRestrictedPurchaseVO.getStartSaleNum());
                        } else {
                            //限售没有资格购买时，h5需要的商品状态是正常
//                            g.setMaxCount(0L);
                            g.setGoodsStatus(GoodsStatus.NO_AUTH);
                        }
                    }
                });
            }
        }


        // 会员等级
        HashMap<Long, CommonLevelVO> levelsMap = new HashMap<>();
        if (Objects.nonNull(customer)) {
            levelsMap = goodsCacheService.listCustomerLevelMapByCustomerIdAndIds(customer.getCustomerId(), storeIds);
        }

        // 商品选择的营销
        if (Objects.nonNull(customer)) {
            List<GoodsMarketing> goodsMarketingList = goodsMarketingService.queryGoodsMarketingList(customer.getCustomerId());
            goodsInfoList.forEach(goodsInfo -> {
                GoodsMarketing marketing = goodsMarketingList.stream()
                        .filter(i -> i.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).findFirst().orElse(null);
                goodsInfo.setGoodsMarketing(KsBeanUtil.convert(marketing, GoodsMarketingVO.class));
            });
        }

        // 按客户单独设价信息
        List<String> cusSkuIds = goodsInfoList.stream()
                .filter(i -> Constants.yes.equals(i.getCustomFlag())).map(i -> i.getGoodsInfoId()).collect(Collectors.toList());
        List<GoodsCustomerPrice> goodsCustomerPrices = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(cusSkuIds) && Objects.nonNull(customer)) {
            goodsCustomerPrices = goodsCustomerPriceRepository.findSkuByGoodsInfoIdAndCustomerId(cusSkuIds, customer.getCustomerId());
        }
        Map<String, GoodsCustomerPrice> customerPriceMap = goodsCustomerPrices.stream().collect(Collectors.toMap(GoodsCustomerPrice::getGoodsInfoId, c -> c));

        // 商品设价方式信息设置
        HashMap<Long, CommonLevelVO> finalLevelsMap = levelsMap;
        List<PaidCardVO> paidCardVOList = null;
        if (Objects.nonNull(customer)) {
            paidCardVOList = paidCardCustomerRelQueryProvider.getMaxDiscountPaidCard(MaxDiscountPaidCardRequest.builder()
                    .customerId(customer.getCustomerId())
                    .build()).getContext();
        }

        BigDecimal discountRate = null;
        if(CollectionUtils.isNotEmpty(paidCardVOList)){

            discountRate = paidCardVOList.get(0).getDiscountRate();
        }
        BigDecimal finalDiscountRate = discountRate;
        goodsInfoList.forEach(goodsInfo -> {
            Integer priceType = Objects.nonNull(goodsInfo.getPriceType()) ? goodsInfo.getPriceType() : GoodsPriceType.MARKET.toValue();
            if (priceType.equals(GoodsPriceType.MARKET.toValue())) {
                // 按市场价销售
                // 添加付费会员折扣
                if(Objects.nonNull(finalDiscountRate)){
                    goodsInfo.setSalePrice(goodsInfo.getMarketPrice().multiply(finalDiscountRate).setScale(2, BigDecimal.ROUND_HALF_UP));
                }else{
                    goodsInfo.setSalePrice(goodsInfo.getMarketPrice());
                }

            }
            if (priceType.equals(GoodsPriceType.CUSTOMER.toValue())) {

                if(Objects.nonNull(finalDiscountRate)) {
                    goodsInfo.setSalePrice(goodsInfo.getMarketPrice().multiply(finalDiscountRate).setScale(2, BigDecimal.ROUND_HALF_UP));
                } else {
                    // 按客户设价--级别价
                    CommonLevelVO customerLevel = finalLevelsMap.get(goodsInfo.getStoreId());
                    if (Objects.nonNull(customerLevel) && Objects.nonNull(customerLevel.getLevelDiscount())) {
                        goodsInfo.setSalePrice(goodsInfo.getSalePrice().multiply(customerLevel.getLevelDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    }
                    if (Objects.nonNull(customerLevel)) {
                        goodsInfo.getGoodsLevelPriceList().stream()
                                .filter(i -> i.getLevelId().equals(customerLevel.getLevelId())).findFirst()
                                .ifPresent((levelPrice -> {
                                    if(Objects.nonNull(levelPrice.getPrice())) {
                                        goodsInfo.setSalePrice(levelPrice.getPrice());
                                    }
                                    goodsInfo.setCount(levelPrice.getCount());
                                    goodsInfo.setMaxCount(levelPrice.getMaxCount());
                                }));
                    }

                    // 按客户设价--客户单独定价
                    GoodsCustomerPrice customerPrice = customerPriceMap.get(goodsInfo.getGoodsInfoId());
                    if (Objects.nonNull(customerPrice)) {
                        if(customerPrice.getPrice() != null) {
                            goodsInfo.setSalePrice(customerPrice.getPrice());
                        }
                        goodsInfo.setCount(customerPrice.getCount());
                        goodsInfo.setMaxCount(customerPrice.getMaxCount());
                    }
                }
            }
            if (priceType.equals(GoodsPriceType.STOCK.toValue())) {
                // 按订货量设价
                CommonLevelVO customerLevel = finalLevelsMap.get(goodsInfo.getStoreId());
                List<GoodsIntervalPriceVO> intervalPrices = goodsInfo.getIntervalPriceList();
                if (Constants.yes.equals(goodsInfo.getLevelDiscountFlag())
                        && Objects.nonNull(customerLevel) && Objects.nonNull(customerLevel.getLevelDiscount())) {
                    intervalPrices.forEach(intervalPrice -> {
                        intervalPrice.setPrice(intervalPrice.getPrice().multiply(customerLevel.getLevelDiscount()).setScale(2, BigDecimal.ROUND_HALF_UP));
                    });
                }
                List<BigDecimal> intervalPriceList = intervalPrices.stream().map(GoodsIntervalPriceVO::getPrice).collect(Collectors.toList());
                goodsInfo.setIntervalPriceIds(intervalPrices.stream().map(GoodsIntervalPriceVO::getIntervalPriceId).collect(Collectors.toList()));
                goodsInfo.setIntervalMinPrice(intervalPriceList.stream().filter(Objects::nonNull).min(BigDecimal::compareTo).orElse(null));
                goodsInfo.setIntervalMaxPrice(intervalPriceList.stream().filter(Objects::nonNull).max(BigDecimal::compareTo).orElse(null));
                goodsInfo.setIntervalPriceList(intervalPrices);
            }

        });

        response.setGoodsInfoList(goodsInfoList);
        response.setGoodsList(goodsList);
        response.setLevelsMap(levelsMap);
        return response;
    }


    /**
     * 递归方式，获取全局唯一SPU编码
     * @return Spu编码
     */
    public String getSpuNoByUnique(){
        String spuNo = getSpuNo();
        GoodsQueryRequest queryRequest = new GoodsQueryRequest();
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setGoodsNo(spuNo);
        if (goodsRepository.count(queryRequest.getWhereCriteria()) > 0) {
            return getSpuNoByUnique();
        }
        return spuNo;
    }

    /**
     * 获取Spu编码
     * @return Spu编码
     */
    public String getSpuNo() {
        return "P".concat(String.valueOf(System.currentTimeMillis()).substring(4, 10)).concat(RandomStringUtils.randomNumeric(3));
    }

    /**
     * 递归方式，获取全局唯一SPU编码
     * @return Sku编码
     */
    public String getSkuNoByUnique(){
        String skuNo = getSkuNo();
        GoodsInfoQueryRequest queryRequest = new GoodsInfoQueryRequest();
        queryRequest.setDelFlag(DeleteFlag.NO.toValue());
        queryRequest.setGoodsInfoNos(Collections.singletonList(skuNo));
        if (goodsInfoRepository.count(queryRequest.getWhereCriteria()) > 0) {
            return getSkuNoByUnique();
        }
        return skuNo;
    }

    /**
     * 获取Sku编码
     * @return Sku编码
     */
    public String getSkuNo() {
        return "8".concat(String.valueOf(System.currentTimeMillis()).substring(4, 10)).concat(RandomStringUtils.randomNumeric(3));
    }
}
