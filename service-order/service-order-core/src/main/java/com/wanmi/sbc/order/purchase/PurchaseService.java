package com.wanmi.sbc.order.purchase;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.DistributeChannel;
import com.wanmi.sbc.common.base.VASEntity;
import com.wanmi.sbc.common.constant.VASStatus;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.SortType;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.enums.VASConstants;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SiteResultCode;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.enterpriseinfo.EnterpriseInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.fandeng.ExternalProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoQueryByIdsRequest;
import com.wanmi.sbc.customer.api.request.company.CompanyListRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoQueryByIdsResponse;
import com.wanmi.sbc.customer.api.response.store.ListCompanyStoreByCompanyIdsResponse;
import com.wanmi.sbc.customer.api.response.store.StoreByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.MiniCompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.MiniStoreVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.enums.GoodsChannelTypeEnum;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.blacklist.GoodsBlackListProvider;
import com.wanmi.sbc.goods.api.provider.common.GoodsCommonQueryProvider;
import com.wanmi.sbc.goods.api.provider.distributor.goods.DistributorGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.enterprise.EnterpriseGoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.goodsrestrictedsale.GoodsRestrictedSaleQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.marketing.GoodsMarketingProvider;
import com.wanmi.sbc.goods.api.provider.marketing.GoodsMarketingQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsIntervalPriceProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsLevelPriceQueryProvider;
import com.wanmi.sbc.goods.api.provider.price.GoodsPriceAssistProvider;
import com.wanmi.sbc.goods.api.provider.spec.GoodsInfoSpecDetailRelQueryProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.AppointmentSaleInProgressRequest;
import com.wanmi.sbc.goods.api.request.common.InfoForPurchaseRequest;
import com.wanmi.sbc.goods.api.request.distributor.goods.DistributorGoodsInfoVerifyRequest;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterprisePriceGetRequest;
import com.wanmi.sbc.goods.api.request.goodsrestrictedsale.GoodsRestrictedBatchValidateRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoFillGoodsStatusRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.ProviderGoodsStockSyncRequest;
import com.wanmi.sbc.goods.api.request.marketing.GoodsMarketingBatchAddRequest;
import com.wanmi.sbc.goods.api.request.marketing.GoodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest;
import com.wanmi.sbc.goods.api.request.marketing.GoodsMarketingDeleteByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.marketing.GoodsMarketingListByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.marketing.GoodsMarketingModifyRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsIntervalPriceByCustomerIdRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsLevelPriceBySkuIdsRequest;
import com.wanmi.sbc.goods.api.request.price.GoodsPriceSetBatchByIepRequest;
import com.wanmi.sbc.goods.api.request.spec.GoodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleInProcessResponse;
import com.wanmi.sbc.goods.api.response.common.GoodsInfoForPurchaseResponse;
import com.wanmi.sbc.goods.api.response.enterprise.EnterprisePriceResponse;
import com.wanmi.sbc.goods.api.response.goodsrestrictedsale.GoodsRestrictedSalePurchaseResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsPriceSetBatchByIepResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsMarketingDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.enums.EnterpriseAuditState;
import com.wanmi.sbc.goods.bean.enums.EnterprisePriceType;
import com.wanmi.sbc.goods.bean.enums.GoodsPriceType;
import com.wanmi.sbc.goods.bean.enums.GoodsStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.enums.PriceType;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoResponseVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoSpecDetailRelVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsIntervalPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsMarketingVO;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedPurchaseVO;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedValidateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCacheProvider;
import com.wanmi.sbc.marketing.api.provider.distribution.DistributionSettingQueryProvider;
import com.wanmi.sbc.marketing.api.provider.market.MarketingCommonQueryProvider;
import com.wanmi.sbc.marketing.api.provider.market.MarketingQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCacheListForGoodsGoodInfoListRequest;
import com.wanmi.sbc.marketing.api.request.distribution.DistributionStoreSettingGetByStoreIdRequest;
import com.wanmi.sbc.marketing.api.request.market.InfoForPurchseRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingGetByIdRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginByGoodsInfoListAndCustomerRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGetCustomerLevelsByStoreIdsRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGetCustomerLevelsRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCacheListForGoodsDetailResponse;
import com.wanmi.sbc.marketing.api.response.distribution.DistributionStoreSettingGetByStoreIdResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketInfoForPurchaseResponse;
import com.wanmi.sbc.marketing.bean.constant.MarketingErrorCode;
import com.wanmi.sbc.marketing.bean.enums.CouponType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.CouponCacheVO;
import com.wanmi.sbc.marketing.bean.vo.GoodsInfoMarketingVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingBuyoutPriceLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingForEndVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullDiscountLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftDetailVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullGiftLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullReductionLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingHalfPriceSecondPieceLevelVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingViewVO;
import com.wanmi.sbc.order.api.enums.ShopCartSourceEnum;
import com.wanmi.sbc.order.api.request.purchase.PurchaseFrontMiniRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseFrontRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseInfoRequest;
import com.wanmi.sbc.order.api.request.purchase.PurchaseMergeRequest;
import com.wanmi.sbc.order.api.response.purchase.MiniPurchaseResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseGetGoodsMarketingResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseGoodsReponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseListResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseMarketingCalcResponse;
import com.wanmi.sbc.order.api.response.purchase.PurchaseResponse;
import com.wanmi.sbc.order.bean.dto.PurchaseGoodsInfoDTO;
import com.wanmi.sbc.order.bean.dto.PurchaseMergeDTO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.vo.PurchaseGoodsViewVO;
import com.wanmi.sbc.order.bean.vo.PurchaseMarketingCalcVO;
import com.wanmi.sbc.order.bean.vo.PurchaseVO;
import com.wanmi.sbc.order.common.SystemPointsConfigService;
import com.wanmi.sbc.order.constant.OrderErrorCode;
import com.wanmi.sbc.order.customer.service.CustomerCommonService;
import com.wanmi.sbc.order.follow.model.root.GoodsCustomerFollow;
import com.wanmi.sbc.order.follow.repository.GoodsCustomerFollowRepository;
import com.wanmi.sbc.order.follow.request.GoodsCustomerFollowQueryRequest;
import com.wanmi.sbc.order.purchase.mapper.CustmerMapper;
import com.wanmi.sbc.order.purchase.mapper.GoodsInfoMapper;
import com.wanmi.sbc.order.purchase.request.PurchaseRequest;
import com.wanmi.sbc.order.redis.RedisService;
import com.wanmi.sbc.order.trade.service.TradeCacheService;
import com.wanmi.sbc.order.trade.service.VerifyService;
import com.wanmi.sbc.setting.api.provider.thirdaddress.ThirdAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.thirdaddress.ThirdAddressListRequest;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.vo.ThirdAddressVO;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * 采购单服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
@Transactional(readOnly = true)
public class PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);

    @Autowired
    private GoodsCustomerFollowRepository goodsCustomerFollowRepository;

    @Autowired
    private CustomerCommonService customerCommonService;

    @Autowired
    private GoodsInfoProvider goodsInfoProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private GoodsInfoSpecDetailRelQueryProvider goodsInfoSpecDetailRelQueryProvider;

    @Autowired
    private GoodsIntervalPriceProvider goodsIntervalPriceProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private MarketingPluginQueryProvider marketingPluginQueryProvider;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private MarketingQueryProvider marketingQueryProvider;

    @Autowired
    private MarketingLevelPluginProvider marketingLevelPluginProvider;

    @Autowired
    private GoodsMarketingProvider goodsMarketingProvider;

    @Autowired
    private GoodsMarketingQueryProvider goodsMarketingQueryProvider;

    @Autowired
    private CouponCacheProvider couponCacheProvider;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;


    @Resource
    private DistributorGoodsInfoQueryProvider distributorGoodsInfoQueryProvider;

    @Autowired
    private GoodsPriceAssistProvider goodsPriceAssistProvider;

    @Autowired
    private DistributionSettingQueryProvider distributionSettingQueryProvider;

    @Autowired
    private VerifyService verifyService;

    @Autowired
    private AppointmentSaleQueryProvider appointmentSaleQueryProvider;

    @Autowired
    private GoodsRestrictedSaleQueryProvider goodsRestrictedSaleQueryProvider;

    @Autowired
    private GoodsCommonQueryProvider goodsCommonQueryProvider;

    @Autowired
    private MarketingCommonQueryProvider marketingCommonQueryProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SystemPointsConfigService systemPointsConfigService;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

    @Autowired
    private ThirdAddressQueryProvider thirdAddressQueryProvider;

    @Autowired
    private TradeCacheService tradeCacheService;

    @Autowired
    private GoodsInfoMapper goodsInfoMapper;

    @Autowired
    private CustmerMapper custmerMapper;

    @Autowired
    private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;

    @Autowired
    private EnterpriseInfoQueryProvider enterpriseInfoQueryProvider;

    @Autowired
    private EnterpriseGoodsInfoQueryProvider enterpriseGoodsInfoQueryProvider;

    @Autowired
    private GoodsLevelPriceQueryProvider goodsLevelPriceQueryProvider;
    @Autowired
    private GoodsBlackListProvider goodsBlackListProvider;
    @Autowired
    private ExternalProvider externalProvider;

    /**
     * 新增采购单
     *
     * @param request 参数
     */
    @Transactional
    public void save(PurchaseRequest request) {
        GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(
                GoodsInfoByIdRequest.builder().goodsInfoId(request.getGoodsInfoId()).build()
        ).getContext();
        //如果是linkedmall商品，实时查库存
        if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {
            List<QueryItemInventoryResponse.Item> stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(Collections.singletonList(Long.valueOf(goodsInfo.getThirdPlatformSpuId())), "0", null)).getContext();
            if (stocks != null) {
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
        if (request.getVerifyStock() && goodsInfo.getStock() < request.getGoodsNum()) {
            throw new SbcRuntimeException("K-030302", new Object[]{goodsInfo.getStock()});
        }
        if (goodsInfo == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
        }
        Purchase purchase = purchaseRepository.findOne(request.getWhereCriteria()).orElse(null);
        if (Objects.nonNull(purchase)) {
            request.setGoodsNum(request.getGoodsNum() + purchase.getGoodsNum());
        }
        Integer countNum = countGoods(request.getCustomerId(), request.getInviteeId());
        addPurchase(goodsInfo, countNum, request);
    }

    /**
     * 批量加入采购单
     *
     * @param request
     */
    @Transactional
    public void batchSave(PurchaseRequest request) {
        //List<GoodsInfo> goodsInfoList = goodsInfoRepository.findAll(GoodsInfoQueryRequest.builder().delFlag(0).
        //      goodsInfoIds(request.getGoodsInfos().stream().map(GoodsInfo::getGoodsInfoId).collect(Collectors
        //      .toList())).build().getWhereCriteria());
        List<GoodsInfoVO> goodsInfoList = goodsInfoQueryProvider.listByCondition(
                GoodsInfoListByConditionRequest.builder().delFlag(Constants.no)
                        .goodsInfoIds(request.getGoodsInfos().stream().map(GoodsInfoDTO::getGoodsInfoId).collect(Collectors.toList()))
                        .build()
        ).getContext().getGoodsInfos();

        goodsInfoList.forEach(goodsInfoVO -> {
            if (GoodsType.CYCLE_BUY.ordinal() == goodsInfoVO.getGoodsType()) {
                throw new SbcRuntimeException(OrderErrorCode.PURCHASE_GOODS_NOT_ALLOWED);
            }
        });

        List<Purchase> purchaseList = purchaseRepository.findAll(PurchaseRequest.builder()
                .customerId(request.getCustomerId()).inviteeId(request.getInviteeId())
                .goodsInfoIds(request.getGoodsInfos().stream().map(GoodsInfoDTO::getGoodsInfoId).collect(Collectors.toList())).build().getWhereCriteria());

        Integer countNum = countGoods(request.getCustomerId(), request.getInviteeId());
        goodsInfoList.forEach(goods -> {
            Purchase purchases = purchaseList.stream().map(purchase -> {
                if (goods.getGoodsInfoId().equals(purchase.getGoodsInfoId())) {
                    return purchase;
                }
                return null;
            }).filter(Objects::nonNull).findFirst().orElse(null);
            request.getGoodsInfos().forEach(info -> {
                if (info.getBuyCount() <= 0) {
                    throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
                }
                if (goods.getGoodsInfoId().equals(info.getGoodsInfoId())) {
                    request.setGoodsNum(info.getBuyCount());
                }
            });
            if (purchases != null) {
                //商品详情页加入购物车不需要校验购物车中已选商品数量
//                if (goods.getStock() < (request.getGoodsNum() + purchases.getGoodsNum())) {
//                    throw new SbcRuntimeException("K-030302", new Object[]{goods.getStock()});
//                }
                updatePurchase(purchases, request);
            } else {
                List<GoodsInfoDTO> goodsInfos =
                        request.getGoodsInfos().stream().filter(info -> info.getGoodsInfoId().equals(goods.getGoodsInfoId())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(goodsInfos)) {
                    request.setCreateTime(goodsInfos.get(0).getCreateTime());
                }
                addPurchase(goods, countNum, request);
            }
        });

    }

    /**
     * 合并登录前后采购单
     *
     * @param request
     */
    @Transactional
    public void mergePurchase(PurchaseMergeRequest request) {
        CustomerDTO customer = request.getCustomer();
        //获取商品列表
        List<GoodsInfoVO> goodsInfoList = goodsInfoQueryProvider.listByCondition(
                GoodsInfoListByConditionRequest.builder().delFlag(Constants.no)
                        .goodsInfoIds(request.getPurchaseMergeDTOList().stream().map(PurchaseMergeDTO::getGoodsInfoId).collect(Collectors.toList()))
                        .build()
        ).getContext().getGoodsInfos();
        //根据用户获取采购单列表
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setCustomerId(customer.getCustomerId());
        purchaseRequest.setInviteeId(request.getInviteeId());
        purchaseRequest.setSortColumn("createTime");
        purchaseRequest.setSortType("desc");
        List<Purchase> purchaseList;
        Sort sort = purchaseRequest.getSort();
        if (Objects.nonNull(sort)) {
            purchaseList = purchaseRepository.findAll(purchaseRequest.getWhereCriteria(), sort);
        } else {
            purchaseList = purchaseRepository.findAll(purchaseRequest.getWhereCriteria());
        }
        //待插入采购单商品集合
        List<GoodsInfoDTO> goodsInfos = new ArrayList<>();
        goodsInfoList.forEach(goods -> {
            // 存在重复商品 替换采购单购买数量
            Optional<Purchase> purchaseOptional =
                    purchaseList.stream().filter(p -> StringUtils.equals(goods.getGoodsInfoId(), p.getGoodsInfoId())).findFirst();
            if (purchaseOptional.isPresent()) {
                Purchase purchase = purchaseOptional.get();
                Long goodsNum =
                        request.getPurchaseMergeDTOList().stream().filter(p -> StringUtils.equals(p.getGoodsInfoId(),
                                purchase.getGoodsInfoId()))
                                .findFirst().get().getGoodsNum() + purchase.getGoodsNum();
                this.updatePurchase(purchase, PurchaseRequest.builder().goodsNum(goodsNum).isCover(true).build());
//                this.save(PurchaseRequest.builder().customerId(customer.getCustomerId()).goodsInfoId(purchase
//                .getGoodsInfoId())
//                        .goodsNum(goodsNum).isCover(true).build());
            } else {
                //不重复，加入集合待插入采购单
                GoodsInfoDTO goodsInfo = new GoodsInfoDTO();
                BeanUtils.copyProperties(goods, goodsInfo);
                goodsInfos.add(goodsInfo);
            }
        });
        if (goodsInfos.size() > 0) {
            if (goodsInfos.size() == Constants.PURCHASE_MAX_SIZE && purchaseList.size() > 0) {
                purchaseRepository.deleteByGoodsInfoids(purchaseList.stream().map(Purchase::getGoodsInfoId).collect(Collectors.toList()), customer.getCustomerId(), request.getInviteeId());
            } else if ((goodsInfos.size() + purchaseList.size()) > Constants.PURCHASE_MAX_SIZE) {
                int num = (goodsInfos.size() + purchaseList.size()) - Constants.PURCHASE_MAX_SIZE;
                if (num >= purchaseList.size() && purchaseList.size() > 0) {
                    purchaseRepository.deleteByGoodsInfoids(purchaseList.stream().map(Purchase::getGoodsInfoId).collect(Collectors.toList()), customer.getCustomerId(), request.getInviteeId());
                } else {
                    purchaseRepository.deleteByGoodsInfoids(purchaseList.subList(purchaseList.size() - num,
                            purchaseList.size()).stream()
                                    .map(Purchase::getGoodsInfoId).collect(Collectors.toList()),
                            customer.getCustomerId(),
                            request.getInviteeId());
                }
            }
            LocalDateTime dateTime = LocalDateTime.now();
            request.getPurchaseMergeDTOList().forEach(info -> {
                goodsInfos.stream().filter(obj -> obj.getGoodsInfoId().equals(info.getGoodsInfoId())).findFirst().ifPresent(goodsInfo -> {
                    goodsInfo.setCreateTime(dateTime.minusSeconds(request.getPurchaseMergeDTOList().indexOf(info)));
                    goodsInfo.setBuyCount(info.getGoodsNum());
                });
            });
            this.batchSave(PurchaseRequest.builder().inviteeId(request.getInviteeId()).customerId(customer.getCustomerId()).goodsInfos(goodsInfos).terminalSource(request.getTerminalSource()).build());
        }
    }

    /**
     * 加入采购单
     *
     * @param goodsInfo
     * @param countNum
     * @param request
     */
    private void addPurchase(GoodsInfoVO goodsInfo, Integer countNum, PurchaseRequest request) {
        request.setGoodsInfoId(goodsInfo.getGoodsInfoId());
        request.setGoodsInfoIds(null);
        Purchase purchase = purchaseRepository.findOne(request.getWhereCriteria()).orElse(null);
        if (Objects.nonNull(purchase)) {
            purchase.setUpdateTime(LocalDateTime.now());
            purchase.setGoodsNum(request.getGoodsNum());
            purchaseRepository.save(purchase);
            return;
        }
        if (countNum >= Constants.PURCHASE_MAX_SIZE) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_050121, new Object[]{Constants.PURCHASE_MAX_SIZE});
        }
        purchase = new Purchase();
        purchase.setCompanyInfoId(goodsInfo.getCompanyInfoId());
        purchase.setGoodsInfoId(goodsInfo.getGoodsInfoId());
        purchase.setInviteeId(request.getInviteeId());
        purchase.setCustomerId(request.getCustomerId());
        purchase.setGoodsId(goodsInfo.getGoodsId());
        purchase.setGoodsNum(request.getGoodsNum());
        purchase.setCreateTime(Objects.nonNull(request.getCreateTime()) ? request.getCreateTime() :
                LocalDateTime.now());
        purchase.setUpdateTime(Objects.nonNull(request.getCreateTime()) ? request.getCreateTime() :
                LocalDateTime.now());
        purchase.setStoreId(goodsInfo.getStoreId());
        purchase.setCateTopId(goodsInfo.getCateTopId());
        purchase.setCateId(goodsInfo.getCateId());
        purchase.setBrandId(goodsInfo.getBrandId());
        purchase.setTerminalSource(request.getTerminalSource());
        purchaseRepository.save(purchase);
    }

    /**
     * 修改采购单
     *
     * @param purchase
     * @param request
     */
    private void updatePurchase(Purchase purchase, PurchaseRequest request) {
        if (request.getIsCover()) {
            purchase.setGoodsNum(request.getGoodsNum());
        } else {
            purchase.setGoodsNum(purchase.getGoodsNum() + request.getGoodsNum());
        }
        purchase.setUpdateTime(LocalDateTime.now());
        purchaseRepository.save(purchase);
    }

    /**
     * 未登录时,根据前端缓存信息查询迷你购物车信息
     *
     * @param frontReq 前端缓存的采购单信息
     * @return
     */
    public MiniPurchaseResponse miniListFront(PurchaseFrontMiniRequest frontReq) {
        MiniPurchaseResponse miniPurchaseResponse = new MiniPurchaseResponse();
        miniPurchaseResponse.setGoodsList(Collections.EMPTY_LIST);
        miniPurchaseResponse.setGoodsIntervalPrices(Collections.EMPTY_LIST);
        miniPurchaseResponse.setPurchaseCount(0);
        miniPurchaseResponse.setNum(0L);
        if (CollectionUtils.isEmpty(frontReq.getGoodsInfoDTOList())) {
            return miniPurchaseResponse;
        }
        GoodsInfoRequest goodsInfoRequest = new GoodsInfoRequest();
        List<String> skuIdList = frontReq.getGoodsInfoDTOList().stream()
                .map(PurchaseGoodsInfoDTO::getGoodsInfoId).collect(Collectors.toList());
        goodsInfoRequest.setGoodsInfoIds(skuIdList);
        // 需要显示规格值
        goodsInfoRequest.setIsHavSpecText(Constants.yes);
        GoodsInfoViewByIdsResponse response = getPurchaseGoodsResponse(goodsInfoRequest);
        if (response == null) {
            return miniPurchaseResponse;
        }

        List<GoodsInfoVO> goodsInfoList = response.getGoodsInfos();
        // 按照前端传入的采购单顺序进行排序
        goodsInfoList.sort(Comparator.comparingInt((goods) -> skuIdList.indexOf(goods.getGoodsInfoId())));

        goodsInfoList.forEach(goodsInfo -> {
            // 填充前端传入的商品购买数量
            goodsInfo.setBuyCount(0L);
            List<PurchaseGoodsInfoDTO> dtoList = frontReq.getGoodsInfoDTOList().stream().filter(sku -> goodsInfo
                    .getGoodsInfoId().equals(sku.getGoodsInfoId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(dtoList)) {
                goodsInfo.setBuyCount(dtoList.get(0).getGoodsNum());
            }
        });
        // 计算区间价
//        response.setGoodsIntervalPrices(goodsIntervalPriceService.putIntervalPrice(response.getGoodsInfos(), null));
        GoodsIntervalPriceByCustomerIdRequest priceRequest = new GoodsIntervalPriceByCustomerIdRequest();
        priceRequest.setGoodsInfoDTOList(KsBeanUtil.copyListProperties(response.getGoodsInfos(), GoodsInfoDTO.class));
        GoodsIntervalPriceByCustomerIdResponse priceResponse =
                goodsIntervalPriceProvider.putByCustomerId(priceRequest).getContext();
        miniPurchaseResponse.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
//        response.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
        // 计算营销价格
        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(response.getGoodsInfos(), GoodsInfoDTO.class));
        response.setGoodsInfos(marketingPluginProvider.goodsListFilter(filterRequest).getContext()
                .getGoodsInfoVOList());
        List<PurchaseGoodsReponse> purchaseGoodsReponseList = goodsInfoList.stream().map(info -> {
            PurchaseGoodsReponse purchaseGoodsReponse = new PurchaseGoodsReponse();
            BeanUtils.copyProperties(info, purchaseGoodsReponse);
            purchaseGoodsReponse.setGoodsName(response.getGoodses().stream().filter(goods -> info.getGoodsId().equals(goods.getGoodsId())).findFirst().get().getGoodsName());
            return purchaseGoodsReponse;
        }).collect(Collectors.toList());
        miniPurchaseResponse.setGoodsList(purchaseGoodsReponseList);

        return miniPurchaseResponse;
    }

    /**
     * 查询迷你采购单
     *
     * @param request
     * @return
     */
    public MiniPurchaseResponse miniList(PurchaseRequest request, CustomerDTO customer) {
        //按创建时间倒序
        request.putSort("createTime", SortType.DESC.toValue());
        request.setPageSize(5);
        Page<Purchase> purchases = purchaseRepository.findAll(request.getWhereCriteria(), request.getPageRequest());
        MiniPurchaseResponse miniPurchaseResponse = new MiniPurchaseResponse();
        miniPurchaseResponse.setGoodsList(Collections.EMPTY_LIST);
        miniPurchaseResponse.setGoodsIntervalPrices(Collections.EMPTY_LIST);
        miniPurchaseResponse.setPurchaseCount(0);
        miniPurchaseResponse.setNum(0L);
        if (purchases.getContent() == null || purchases.getContent().size() == 0) {
            return miniPurchaseResponse;
        }
        GoodsInfoViewByIdsRequest goodsInfoRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoRequest.setGoodsInfoIds(purchases.getContent().stream().map(Purchase::getGoodsInfoId).collect(Collectors.toList()));
        //需要显示规格值
        goodsInfoRequest.setIsHavSpecText(Constants.yes);
        GoodsInfoViewByIdsResponse idsResponse = goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();

        //计算区间价
        GoodsIntervalPriceByCustomerIdRequest priceRequest = new GoodsIntervalPriceByCustomerIdRequest();
        priceRequest.setGoodsInfoDTOList(KsBeanUtil.convert(idsResponse.getGoodsInfos(), GoodsInfoDTO.class));
        priceRequest.setCustomerId(customer.getCustomerId());
        GoodsIntervalPriceByCustomerIdResponse priceResponse =
                goodsIntervalPriceProvider.putByCustomerId(priceRequest).getContext();
        miniPurchaseResponse.setGoodsIntervalPrices(priceResponse.getGoodsIntervalPriceVOList());
        idsResponse.setGoodsInfos(priceResponse.getGoodsInfoVOList());

        //计算营销价格,首先排除pc过滤时分销商品会将企业标识重置问题
        idsResponse.getGoodsInfos().forEach(goodsInfoVO -> {

            Boolean pcAndNoOpenAndNoStoreOpenFlag = Boolean.FALSE;
            if (Objects.nonNull(goodsInfoVO.getStoreId())) {
                DistributionStoreSettingGetByStoreIdResponse setting = distributionSettingQueryProvider.getStoreSettingByStoreId(
                        new DistributionStoreSettingGetByStoreIdRequest(String.valueOf(goodsInfoVO.getStoreId()))).getContext();
                if (Objects.isNull(setting) || DefaultFlag.NO.equals(setting.getOpenFlag())) {
                    pcAndNoOpenAndNoStoreOpenFlag = Boolean.TRUE;
                }
            }

            if (Objects.nonNull(request.getPcAndNoOpenFlag()) || Boolean.TRUE.equals(request.getPcAndNoOpenFlag()) || Boolean.TRUE.equals(pcAndNoOpenAndNoStoreOpenFlag)) {
                goodsInfoVO.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
            }
        });

        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(idsResponse.getGoodsInfos(), GoodsInfoDTO.class));
        filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
        idsResponse.setGoodsInfos(marketingPluginProvider.goodsListFilter(filterRequest).getContext()
                .getGoodsInfoVOList());

        //企业购分支，设置企业会员价
        GoodsPriceSetBatchByIepRequest iepRequest = GoodsPriceSetBatchByIepRequest.builder()
                .customer(KsBeanUtil.convert(customer, CustomerVO.class))
                .goodsInfos(idsResponse.getGoodsInfos())
                .goodsIntervalPrices(miniPurchaseResponse.getGoodsIntervalPrices())
                .filteredGoodsInfoIds(idsResponse.getGoodsInfos().stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList()))
                .build();

        GoodsPriceSetBatchByIepResponse iepResponse = goodsPriceAssistProvider.goodsPriceSetBatchByIep(iepRequest).getContext();
        idsResponse.setGoodsInfos(iepResponse.getGoodsInfos());
        miniPurchaseResponse.setGoodsIntervalPrices(iepResponse.getGoodsIntervalPrices());

        //填充SKU的购买数
        this.fillBuyCount(idsResponse.getGoodsInfos(), purchases.getContent());
        List<PurchaseGoodsReponse> purchaseGoodsReponseList = new ArrayList<>();
        purchases.forEach(purchase -> {
            idsResponse.getGoodsInfos().forEach(info -> {
                if (purchase.getGoodsInfoId().equals(info.getGoodsInfoId())) {
                    PurchaseGoodsReponse purchaseGoodsReponse = new PurchaseGoodsReponse();
                    BeanUtils.copyProperties(info, purchaseGoodsReponse);
                    purchaseGoodsReponse.setGoodsName(idsResponse.getGoodses().stream().filter(goods -> info.getGoodsId().equals(goods.getGoodsId())).findFirst().get().getGoodsName());
                    purchaseGoodsReponseList.add(purchaseGoodsReponse);
                }
            });
        });
        miniPurchaseResponse.setGoodsList(purchaseGoodsReponseList);

        miniPurchaseResponse.setPurchaseCount(countGoods(customer.getCustomerId(), request.getInviteeId()));
        miniPurchaseResponse.setNum(purchaseRepository.queryGoodsNum(customer.getCustomerId(), request.getInviteeId()));

        return miniPurchaseResponse;
    }

    /**
     * 采购单按spu,按store组装 公共方法
     *
     * @return
     * @throws SbcRuntimeException
     */
    private PurchaseResponse listBase(List<GoodsInfoVO> goodsInfoList, List<GoodsVO> goodsList) throws SbcRuntimeException {
        //建立商户->SPU的扁平化结构  companyInfoQueryProvider
        List<Long> companyInfoIds =
                goodsInfoList.stream().map(GoodsInfoVO::getCompanyInfoId).collect(Collectors.toList());
        CompanyInfoQueryByIdsRequest request = new CompanyInfoQueryByIdsRequest();
        request.setCompanyInfoIds(companyInfoIds);
        request.setDeleteFlag(DeleteFlag.NO);
        CompanyInfoQueryByIdsResponse response = companyInfoQueryProvider.queryByCompanyInfoIds(request).getContext();
        List<CompanyInfoVO> companyInfoList = response.getCompanyInfoList().stream().map(companyInfo -> {
            companyInfo.setGoodsIds(goodsList.stream().filter(goodsinfo -> goodsinfo.getCompanyInfoId().longValue() == companyInfo.getCompanyInfoId().longValue())
                    .map(GoodsVO::getGoodsId).collect(Collectors.toList()));
            return companyInfo;
        }).collect(Collectors.toList());
//        List<CompanyInfo> companyInfoList = companyInfoRepository.queryByCompanyinfoIds(companyInfoIds, DeleteFlag
//        .NO).stream().map(companyInfo -> {
//            companyInfo.setGoodsIds(goodsList.stream().filter(goodsinfo -> goodsinfo.getCompanyInfoId().longValue()
//            == companyInfo.getCompanyInfoId().longValue())
//                    .map(GoodsDTO::getGoodsId).collect(Collectors.toList()));
//            return companyInfo;
//        }).collect(Collectors.toList());

        //店铺列表
        List<StoreVO> storeList = companyInfoList.stream().map(companyInfo -> {
            if (CollectionUtils.isNotEmpty(companyInfo.getStoreVOList())) {
                StoreVO store = companyInfo.getStoreVOList().get(0);
                store.setGoodsIds(goodsList.stream()
                        .filter(goodsinfo -> goodsinfo.getCompanyInfoId().longValue() == companyInfo.getCompanyInfoId().longValue())
                        .map(GoodsVO::getGoodsId).collect(Collectors.toList()));
                return store;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> storeIds = goodsList.stream().map(GoodsVO::getStoreId).distinct().collect(Collectors.toList());
        storeList.sort(Comparator.comparingInt((store) -> storeIds.indexOf(store.getStoreId())));
        return PurchaseResponse.builder()
                .goodses(goodsList)
                .goodsInfos(goodsInfoList)
                .companyInfos(companyInfoList)
                .stores(storeList)
                .build();
    }

    /**
     * 此方法仅用于采购单商品查询
     * 若采购单中商品都不存在,则返回空的Response,而不是将异常抛给前端
     */
    private GoodsInfoViewByIdsResponse getPurchaseGoodsResponse(GoodsInfoRequest goodsInfoRequest) throws SbcRuntimeException {
        GoodsInfoViewByIdsRequest request = new GoodsInfoViewByIdsRequest();
        request.setGoodsInfoIds(goodsInfoRequest.getGoodsInfoIds());
        request.setIsHavSpecText(goodsInfoRequest.getIsHavSpecText());
        BaseResponse<GoodsInfoViewByIdsResponse> response = goodsInfoQueryProvider.listViewByIds(request);
        if (!CommonErrorCode.SUCCESSFUL.equals(response.getCode())) {
            if ("K-030001".equals(response.getCode())) {
                return null;
            } else {
                throw new SbcRuntimeException(response.getCode(), response.getMessage());
            }
        }
        return response.getContext();
    }

    /**
     * 未登陆时,根据前端传入的采购单信息,查询组装必要信息
     *
     * @param request 前端传入的采购单信息(skuList)
     * @return 采购单数据
     * @throws SbcRuntimeException
     * @author bail
     */
    public PurchaseResponse listFront(PurchaseFrontRequest request) throws SbcRuntimeException {
        PurchaseResponse emptyResponse = PurchaseResponse.builder()
                .goodsInfos(Collections.emptyList())
                .companyInfos(Collections.emptyList())
                .goodses(Collections.emptyList())
                .goodsIntervalPrices(Collections.emptyList())
                .build();
        if (CollectionUtils.isEmpty(request.getGoodsInfoDTOList())) {
            // 若传入的前端采购单为空,则构建空的采购单作为返回值
            return emptyResponse;
        }

        GoodsInfoRequest goodsInfoRequest = new GoodsInfoRequest();
        List<String> skuIdList = request.getGoodsInfoDTOList().stream()
                .map(PurchaseGoodsInfoDTO::getGoodsInfoId).collect(Collectors.toList());
        goodsInfoRequest.setGoodsInfoIds(skuIdList);
        goodsInfoRequest.setIsHavSpecText(Constants.yes);
        GoodsInfoViewByIdsResponse response = getPurchaseGoodsResponse(goodsInfoRequest);
        if (response == null) {
            return emptyResponse;
        }

        final List<GoodsInfoVO> goodsInfoList = response.getGoodsInfos();
        LocalDateTime dateTime = LocalDateTime.now();
        // 按照前端传入的采购单顺序进行排序
        goodsInfoList.sort(Comparator.comparingInt((goods) -> skuIdList.indexOf(goods.getGoodsInfoId())));
        List<GoodsInfoVO> goodsInfoListNew = goodsInfoList.stream().map(goodsInfo -> {
            List<GoodsInfoSpecDetailRelVO> goodsInfoSpecDetailRelList =
                    goodsInfoSpecDetailRelQueryProvider.listByGoodsIdAndSkuId(new GoodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest(goodsInfo.getGoodsId(), goodsInfo.getGoodsInfoId())).getContext().getGoodsInfoSpecDetailRelVOList();

//            List<GoodsInfoSpecDetailRel> goodsInfoSpecDetailRelList =
//                    goodsInfoSpecDetailRelRepository.findByGoodsIdAndGoodsInfoId(
//                            goodsInfo.getGoodsId(), goodsInfo.getGoodsInfoId());
            goodsInfo.setSpecDetailRelIds(goodsInfoSpecDetailRelList.stream()
                    .map(GoodsInfoSpecDetailRelVO::getSpecDetailRelId).collect(Collectors.toList()));
            // 填充前端传入的商品购买数量
            goodsInfo.setBuyCount(0L);
            List<PurchaseGoodsInfoDTO> dtoList = request.getGoodsInfoDTOList().stream().filter(purchase -> goodsInfo
                    .getGoodsInfoId().equals(purchase.getGoodsInfoId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(dtoList)) {
                goodsInfo.setBuyCount(dtoList.get(0).getGoodsNum());
            }
            // 前端根据此time倒序排列(虽然感觉有点蠢,但以前就是这么做的,减少改动的临时做法)
            goodsInfo.setCreateTime(dateTime.minusSeconds(goodsInfoList.indexOf(goodsInfo)));
            return goodsInfo;
        }).collect(Collectors.toList());

        List<GoodsVO> goodsList = response.getGoodses().stream()
                .map(goods -> {
                    goods.setGoodsDetail("");
                    return goods;
                }).collect(Collectors.toList());

        List<String> goodsIds =
                goodsInfoListNew.stream().map(GoodsInfoVO::getGoodsId).distinct().collect(Collectors.toList());
        // spu也同样按照前端传入的顺序进行排序
        goodsList.sort(Comparator.comparingInt((goods) -> goodsIds.indexOf(goods.getGoodsId())));
        return listBase(goodsInfoListNew, goodsList);
    }

    /**
     * 登陆后,查询采购单列表
     *
     * @param request 参数
     * @return 采购单数据
     * @throws SbcRuntimeException
     */
    public PurchaseResponse list(PurchaseRequest request) throws SbcRuntimeException {
        PurchaseResponse emptyResponse = PurchaseResponse.builder().goodsInfos(Collections.emptyList()).companyInfos
                (Collections.emptyList()).stores(Collections.emptyList())
                .goodses(Collections.emptyList()).goodsIntervalPrices(Collections.emptyList()).build();
        //按创建时间倒序
        request.putSort("createTime", SortType.DESC.toValue());
        List<Purchase> follows;
        Sort sort = request.getSort();
        if (Objects.nonNull(sort)) {
            follows = purchaseRepository.findAll(request.getWhereCriteria(), sort);
        } else {
            follows = purchaseRepository.findAll(request.getWhereCriteria());
        }
        if (CollectionUtils.isEmpty(follows)) {
            return emptyResponse;
        }

        GoodsInfoViewByIdsRequest goodsInfoRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoRequest.setGoodsInfoIds(follows.stream().map(Purchase::getGoodsInfoId).collect(Collectors.toList()));
        //需要显示规格值
        goodsInfoRequest.setIsHavSpecText(Constants.yes);
        GoodsInfoViewByIdsResponse response = goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();

        response.getGoodsInfos().forEach(goodsInfo -> {
            List<GoodsInfoSpecDetailRelVO> goodsInfoSpecDetailRelList =
                    goodsInfoSpecDetailRelQueryProvider.listByGoodsIdAndSkuId(new GoodsInfoSpecDetailRelByGoodsIdAndSkuIdRequest(goodsInfo.getGoodsId(), goodsInfo.getGoodsInfoId())).getContext().getGoodsInfoSpecDetailRelVOList();
            goodsInfo.setSpecDetailRelIds(goodsInfoSpecDetailRelList.stream().map(GoodsInfoSpecDetailRelVO::getSpecDetailRelId).collect(Collectors.toList()));
        });

        //如果是linkedmall商品，实时查库存,根据区域码查库存
        List<Long> itemIds = response.getGoodses().stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .distinct()
                .collect(Collectors.toList());
        List<QueryItemInventoryResponse.Item> stocks = null;
        if (itemIds.size() > 0) {
            String thirdAddrId = null;
            if (request.getAreaId() != null) {
                List<ThirdAddressVO> thirdAddressVOS =
                        thirdAddressQueryProvider.list(ThirdAddressListRequest.builder().platformAddrIdList(Collections.singletonList(Objects.toString(request.getAreaId())))
                                .thirdFlag(ThirdPlatformType.LINKED_MALL).build())
                                .getContext().getThirdAddressList();

                if (CollectionUtils.isNotEmpty(thirdAddressVOS)) {
                    thirdAddrId = thirdAddressVOS.get(0).getThirdAddrId();
                }
            }
            stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, thirdAddrId == null ? "0" : thirdAddrId, null)).getContext();
        }
        if (stocks != null) {
            for (GoodsVO goodsVO : response.getGoodses()) {
                if (ThirdPlatformType.LINKED_MALL.equals(goodsVO.getThirdPlatformType())) {
                    Optional<QueryItemInventoryResponse.Item> optional = stocks.stream()
                            .filter(v -> v.getItemId().equals(Long.valueOf(goodsVO.getThirdPlatformSpuId())))
                            .findFirst();
                    if (optional.isPresent()) {

                        Long totalStock = optional.get().getSkuList().stream()
                                .map(v -> v.getInventory().getQuantity())
                                .reduce(0L, ((aLong, aLong2) -> aLong + aLong2));
                        goodsVO.setStock(totalStock);
                    }
                }
            }
            for (GoodsInfoVO goodsInfo : response.getGoodsInfos()) {
                for (QueryItemInventoryResponse.Item spuStock : stocks) {
                    if (ThirdPlatformType.LINKED_MALL.equals(goodsInfo.getThirdPlatformType())) {
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
        List<GoodsInfoDTO> goodsInfoDTOS = KsBeanUtil.convert(response.getGoodsInfos(), GoodsInfoDTO.class);
        response.setGoodsInfos(goodsInfoProvider.providerGoodsStockSync(new ProviderGoodsStockSyncRequest(goodsInfoDTOS))
                .getContext().getGoodsInfoList());

        //采购单中skuId
        List<String> goodsIds = follows.stream().map(Purchase::getGoodsId).distinct().collect(Collectors.toList());
        List<GoodsVO> goodsList = response.getGoodses().stream()
                .filter(goods -> goodsIds.contains(goods.getGoodsId()))
                .map(goods -> {
                    goods.setGoodsDetail("");
                    return goods;
                }).collect(Collectors.toList());

        goodsList.sort(Comparator.comparingInt((goods) -> goodsIds.indexOf(goods.getGoodsId())));

        List<GoodsInfoVO> goodsInfoList = IteratorUtils.zip(response.getGoodsInfos(), follows,
                (GoodsInfoVO sku, Purchase f) -> sku.getGoodsInfoId().equals(f.getGoodsInfoId()),
                (GoodsInfoVO sku, Purchase f) -> {
                    sku.setCreateTime(f.getCreateTime());
                });
        //建立商户->SPU的扁平化结构
        List<Long> companyInfoIds = goodsInfoList.stream().map(GoodsInfoVO::getCompanyInfoId)
                .collect(Collectors.toList());

        List<CompanyInfoVO> companyInfoList = customerCommonService.listCompanyInfoByCondition(
                CompanyListRequest.builder().companyInfoIds(companyInfoIds).deleteFlag(DeleteFlag.NO).build())
                .stream().map(companyInfo -> {
                    companyInfo.setGoodsIds(goodsList.stream()
                            .filter(goodsinfo -> goodsinfo.getCompanyInfoId().longValue() == companyInfo
                                    .getCompanyInfoId().longValue())
                            .map(GoodsVO::getGoodsId).collect(Collectors.toList()));
                    return companyInfo;
                }).collect(Collectors.toList());

        //店铺列表
        List<StoreVO> storeList = companyInfoList.stream().map(companyInfo -> {
            if (CollectionUtils.isNotEmpty(companyInfo.getStoreVOList())) {
                StoreVO store = companyInfo.getStoreVOList().get(0);
                store.setGoodsIds(goodsList.stream()
                        .filter(goodsinfo -> goodsinfo.getCompanyInfoId().longValue() == companyInfo.getCompanyInfoId().longValue())
                        .map(GoodsVO::getGoodsId).collect(Collectors.toList()));
                return store;
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> storeIds = goodsList.stream().map(GoodsVO::getStoreId).distinct().collect(Collectors.toList());
        storeList.sort(Comparator.comparingInt((store) -> storeIds.indexOf(store.getStoreId())));
        //填充SKU的购买数
        this.fillBuyCount(response.getGoodsInfos(), follows);
        return PurchaseResponse.builder()
                .goodses(goodsList)
                .goodsInfos(goodsInfoList)
                .companyInfos(CollectionUtils.isEmpty(companyInfoList) ? Collections.EMPTY_LIST : companyInfoList)
                .stores(CollectionUtils.isEmpty(storeList) ? Collections.EMPTY_LIST : storeList)
                .build();
    }

    /**
     * 查询采购单
     *
     * @param customerId
     * @param goodsInfoIds
     * @return
     */
    public List<Purchase> queryPurchase(String customerId, List<String> goodsInfoIds, String inviteeId) {
        //分页查询SKU信息列表
        PurchaseRequest request = PurchaseRequest.builder()
                .customerId(customerId)
                .goodsInfoIds(goodsInfoIds)
                .inviteeId(inviteeId)
                .build();
        request.setSortColumn("updateTime");
        request.setSortType("DESC");
        Sort sort = request.getSort();
        List<Purchase> list;
        if (Objects.nonNull(sort)) {
            list = purchaseRepository.findAll(request.getWhereCriteria(), sort);
        } else {
            list = purchaseRepository.findAll(request.getWhereCriteria());
        }

        return list;
    }

    /**
     * 获取采购单商品数量
     *
     * @param userId
     * @return
     */
    public Integer countGoods(String userId, String inviteeId) {
        return purchaseRepository.countByCustomerIdAndInviteeId(userId, inviteeId);
    }

    /**
     * 获取采购单商品数量
     *
     * @param userId
     * @param inviteeId
     * @param companyInfoId
     * @return
     */
    public Integer countGoodsByCompanyInfoId(String userId, String inviteeId, Long companyInfoId) {
        return purchaseRepository.countByCustomerIdAndInviteeIdAndCompanyInfoId(userId, inviteeId, companyInfoId);
    }

    /**
     * 商品收藏调整商品数量
     *
     * @param request 参数
     */
    @Transactional
    public void updateNum(PurchaseRequest request) {
        GoodsInfoVO goodsInfo = goodsInfoQueryProvider.getById(
                GoodsInfoByIdRequest.builder().goodsInfoId(request.getGoodsInfoId()).build()
        ).getContext();
        if (goodsInfo == null) {
            throw new SbcRuntimeException(SiteResultCode.ERROR_000009);
        }
        List<Purchase> purchaseList = purchaseRepository.findAll(PurchaseRequest.builder()
                .customerId(request.getCustomerId())
                .goodsInfoId(request.getGoodsInfoId())
                .inviteeId(request.getInviteeId())
                .build().getWhereCriteria());
        Integer countNum = countGoods(request.getCustomerId(), request.getInviteeId());
        //如果存在，更新标识和数量
        if (CollectionUtils.isNotEmpty(purchaseList)) {
            Purchase purchase = purchaseList.get(0);
            //更新标识：收藏->all
            if (request.getVerifyStock() && goodsInfo.getStock() < request.getGoodsNum() && purchase.getGoodsNum() < request.getGoodsNum()) {
                throw new SbcRuntimeException("K-030302", new Object[]{goodsInfo.getStock()});
            }

            if (BoolFlag.YES.equals(request.getUpdateTimeFlag())) {
                purchase.setUpdateTime(LocalDateTime.now());
            }
            purchase.setGoodsNum(request.getGoodsNum());
            purchaseRepository.save(purchase);
        } else {
            if (countNum >= Constants.PURCHASE_MAX_SIZE) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_050121, new Object[]{Constants.PURCHASE_MAX_SIZE});
            }
            //如果数据不存在，自动加入采购单
            Purchase purchase = new Purchase();
            BeanUtils.copyProperties(request, purchase);
            purchase.setCreateTime(LocalDateTime.now());
            purchase.setUpdateTime(LocalDateTime.now());
            purchase.setCompanyInfoId(goodsInfo.getCompanyInfoId());
            purchase.setGoodsId(goodsInfo.getGoodsId());
            purchase.setStoreId(goodsInfo.getStoreId());
            purchase.setCateTopId(goodsInfo.getCateTopId());
            purchase.setCateId(goodsInfo.getCateId());
            purchase.setBrandId(goodsInfo.getBrandId());
            purchase.setTerminalSource(request.getTerminalSource());
            purchaseRepository.save(purchase);
        }
    }

    /**
     * 删除采购单
     *
     * @param request 参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(PurchaseRequest request) {
        purchaseRepository.deleteByGoodsInfoids(request.getGoodsInfoIds(), request.getCustomerId(),
                request.getInviteeId());
        GoodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest goodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest = new GoodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest();
        goodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest.setGoodsInfoIds(request.getGoodsInfoIds());
        goodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest.setCustomerId(request.getCustomerId());
        goodsMarketingProvider.deleteByCustomerIdAndGoodsInfoIds(goodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest);
    }

    /**
     * 采购单商品移入收藏夹
     *
     * @param queryRequest
     * @return
     */
    @Transactional
    public void addFollow(PurchaseRequest queryRequest) {
        //获取所有收藏商品
        GoodsCustomerFollowQueryRequest followQueryRequest = GoodsCustomerFollowQueryRequest.builder()
                .customerId(queryRequest.getCustomerId())
                .build();
        List<GoodsCustomerFollow> existSku =
                goodsCustomerFollowRepository.findAll(followQueryRequest.getWhereCriteria());
        List<String> newSkuIds;
        if (CollectionUtils.isNotEmpty(existSku)) {
            List<String> existSkuIds =
                    existSku.stream().map(GoodsCustomerFollow::getGoodsInfoId).collect(Collectors.toList());
            //提取没有收藏的skuId
            newSkuIds =
                    queryRequest.getGoodsInfoIds().stream().filter(s -> !existSkuIds.contains(s)).collect(Collectors.toList());
            //如果有新收藏的skuId,验认收藏最大限制
            if (CollectionUtils.isNotEmpty(newSkuIds) && existSkuIds.size() + newSkuIds.size() > Constants.FOLLOW_MAX_SIZE) {
                throw new SbcRuntimeException(SiteResultCode.ERROR_030401, new Object[]{Constants.FOLLOW_MAX_SIZE});
            }
        } else {
            newSkuIds = queryRequest.getGoodsInfoIds();
        }

        List<Purchase> purchaseList =
                purchaseRepository.queryPurchaseByGoodsIdsAndCustomerId(queryRequest.getGoodsInfoIds(),
                        queryRequest.getCustomerId(), queryRequest.getInviteeId());
        List<String> newSkuIdsBack = newSkuIds;
        purchaseList.stream().forEach(info -> {
            GoodsCustomerFollow follow = new GoodsCustomerFollow();
            BeanUtils.copyProperties(info, follow);
            follow.setFollowTime(LocalDateTime.now());
            if (CollectionUtils.isNotEmpty(newSkuIdsBack) && newSkuIdsBack.contains(follow.getGoodsInfoId())) {
                goodsCustomerFollowRepository.save(follow);
            }

            // 非赠品才删除
            if (!queryRequest.getIsGift()) {
                purchaseRepository.delete(info);
            }
        });
    }

    /**
     * 清除失效商品
     *
     * @param distributeChannel
     */
    @Transactional
    public void clearLoseGoods(CustomerVO customerVO, DistributeChannel distributeChannel) {
        List<Purchase> purchaseList = purchaseRepository.queryPurchaseByCustomerIdAndInviteeId(customerVO.getCustomerId(),
                distributeChannel.getInviteeId());
        //筛选商品id
        List<String> goodsIds = purchaseList.stream().map(Purchase::getGoodsInfoId).collect(Collectors.toList());
        GoodsInfoListByIdsRequest goodsInfoQueryRequest = new GoodsInfoListByIdsRequest();
        //查询商品列表
        goodsInfoQueryRequest.setGoodsInfoIds(goodsIds);
        List<GoodsInfoVO> goodsInfoList =
                goodsInfoQueryProvider.listByIds(goodsInfoQueryRequest).getContext().getGoodsInfos();
        verifyDistributorGoodsInfo(distributeChannel, goodsInfoList);
        //店铺id
        List<Long> storeIds = goodsInfoList.stream().map(GoodsInfoVO::getStoreId).collect(Collectors.toList());
        //查询店铺列表
        List<StoreVO> storeList = storeQueryProvider
                .listNoDeleteStoreByIds(ListNoDeleteStoreByIdsRequest.builder().storeIds(storeIds).build())
                .getContext().getStoreVOList();
//        customerCommonService.
        List<GoodsRestrictedValidateVO> GoodsRestrictedList = purchaseList.stream().map((item) -> {
            GoodsRestrictedValidateVO rvv = new GoodsRestrictedValidateVO();
            rvv.setNum(item.getGoodsNum());
            rvv.setSkuId(item.getGoodsInfoId());
            return rvv;
        }).collect(Collectors.toList());
        this.setRestrictedNum(goodsInfoList, GoodsRestrictedList, customerVO);

        purchaseList.forEach(item -> {
            GoodsInfoVO goodsInfo = goodsInfoList.stream().filter(goods -> goods.getGoodsInfoId().equals(item
                    .getGoodsInfoId())).findFirst().orElse(null);
            if (Objects.nonNull(goodsInfo)) {
                StoreVO store =
                        storeList.stream().filter(store1 -> store1.getStoreId().longValue() == goodsInfo.getStoreId().longValue()).findFirst().orElse(null);
                if (Objects.nonNull(store)) {
                    Duration duration = Duration.between(store.getContractEndDate(), LocalDateTime.now());
                    if (goodsInfo.getAddedFlag() == 0 || goodsInfo.getDelFlag() == DeleteFlag.YES || goodsInfo.getAuditStatus() == CheckStatus.FORBADE || goodsInfo.getGoodsStatus() == GoodsStatus.INVALID
                            || store.getStoreState() == StoreState.CLOSED || duration.toMinutes() >= 0 || buildGoodsInfoVendibility(goodsInfo) == Constants.no) {
                        purchaseRepository.delete(item);
                    }
                }
            }
        });
    }

    public Integer buildGoodsInfoVendibility(GoodsInfoVO goodsInfo) {

        Integer vendibility = Constants.yes;

        LocalDateTime now = LocalDateTime.now();

        String providerGoodsInfoId = goodsInfo.getProviderGoodsInfoId();

        if (StringUtils.isNotBlank(providerGoodsInfoId)) {


            GoodsInfoVO providerGoodsInfo = goodsInfoQueryProvider.getById(GoodsInfoByIdRequest.builder().goodsInfoId(providerGoodsInfoId).build()).getContext();

            if (providerGoodsInfo != null) {
                if (!(Objects.equals(DeleteFlag.NO, providerGoodsInfo.getDelFlag())
                        && Objects.equals(CheckStatus.CHECKED, providerGoodsInfo.getAuditStatus())
                        && Objects.equals(AddedFlag.YES.toValue(), providerGoodsInfo.getAddedFlag()))) {
                    vendibility = Constants.no;
                }

                Long storeId = goodsInfo.getProviderId();

                if (storeId != null) {
                    StoreByIdResponse storeByIdResponse = storeQueryProvider.getById(StoreByIdRequest.builder().storeId(storeId).build()).getContext();
                    if (storeByIdResponse != null) {
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
     * 设置限售数据
     *
     * @param goodsInfoVOS
     * @param customerVO
     * @return
     */
    private List<GoodsInfoVO> setRestrictedNum(List<GoodsInfoVO> goodsInfoVOS,
                                               List<GoodsRestrictedValidateVO> goodsRestrictedValidateVOS,
                                               CustomerVO customerVO) {
        if (CollectionUtils.isNotEmpty(goodsInfoVOS)) {

            GoodsRestrictedSalePurchaseResponse response = goodsRestrictedSaleQueryProvider.validatePurchaseRestricted(
                    GoodsRestrictedBatchValidateRequest.builder()
                            .goodsRestrictedValidateVOS(goodsRestrictedValidateVOS)
                            .customerVO(customerVO)
                            .build()).getContext();
            if (Objects.nonNull(response) && CollectionUtils.isNotEmpty(response.getGoodsRestrictedPurchaseVOS())) {
                List<GoodsRestrictedPurchaseVO> goodsRestrictedPurchaseVOS = response.getGoodsRestrictedPurchaseVOS();
                Map<String, GoodsRestrictedPurchaseVO> purchaseMap = goodsRestrictedPurchaseVOS.stream().collect((Collectors.toMap(GoodsRestrictedPurchaseVO::getGoodsInfoId, g -> g)));
                goodsInfoVOS.stream().forEach(g -> {
                    GoodsRestrictedPurchaseVO goodsRestrictedPurchaseVO = purchaseMap.get(g.getGoodsInfoId());
                    if (Objects.nonNull(goodsRestrictedPurchaseVO)) {
                        if (DefaultFlag.YES.equals(goodsRestrictedPurchaseVO.getDefaultFlag())) {
                            g.setMaxCount(goodsRestrictedPurchaseVO.getRestrictedNum());
                            g.setCount(goodsRestrictedPurchaseVO.getStartSaleNum());
                        } else {
                            g.setGoodsStatus(GoodsStatus.INVALID);
                        }
                    }
                });
            }
            return goodsInfoVOS;
        }
        return goodsInfoVOS;
    }

    /**
     * 填充客户购买数
     *
     * @param goodsInfoList SKU商品
     * @param customerId    客户编号
     */
    public List<GoodsInfoVO> fillBuyCount(List<GoodsInfoVO> goodsInfoList, String customerId, String inviteeId) {
        //分页查询SKU信息列表
        List<Purchase> follows = purchaseRepository.findAll(PurchaseRequest.builder()
                .customerId(customerId).inviteeId(inviteeId)
                .build().getWhereCriteria());
        this.fillBuyCount(goodsInfoList, follows);
        return goodsInfoList;
    }

    /**
     * 填充客户购买数
     *
     * @param goodsInfoList SKU商品
     * @param followList    SKU商品
     */
    public void fillBuyCount(List<GoodsInfoVO> goodsInfoList, List<Purchase> followList) {
        if (CollectionUtils.isEmpty(goodsInfoList) || CollectionUtils.isEmpty(followList)) {
            return;
        }
        //填充SKU的购买数
        goodsInfoList.stream().forEach(goodsInfo -> {
            goodsInfo.setBuyCount(0L);
            List<Purchase> purchaseList =
                    followList.stream().filter(purchase -> goodsInfo.getGoodsInfoId().equals(purchase.getGoodsInfoId())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(purchaseList)) {
                goodsInfo.setBuyCount(purchaseList.get(0).getGoodsNum());
            }
        });
    }

    /**
     * [公共方法]获取采购单中 参加同种营销的商品列表/总额/优惠
     * 同时处理未登录(customer==null) , 已登录的场景(customer!=null)
     *
     * @param marketingId  营销id
     * @param customer     登陆用户信息
     * @param frontReq     前端传入的采购单信息 以及 勾选skuIdList
     * @param goodsInfoIds 已勾选的,参加同种营销的 商品idList
     * @param isPurchase   是否采购单
     * @return 采购单计算结果集
     * @author bail
     */
    public PurchaseMarketingCalcResponse calcMarketingByMarketingIdBase(Long marketingId,
                                                                        CustomerVO customer,
                                                                        PurchaseFrontRequest frontReq,
                                                                        List<String> goodsInfoIds,
                                                                        boolean isPurchase) {
        PurchaseMarketingCalcResponse response = new PurchaseMarketingCalcResponse();

        // 查询该营销活动参与的商品列表
        MarketingGetByIdRequest marketingGetByIdRequest = new MarketingGetByIdRequest();
        marketingGetByIdRequest.setMarketingId(marketingId);
        MarketingForEndVO marketingResponse =
                marketingQueryProvider.getByIdForCustomer(marketingGetByIdRequest).getContext().getMarketingForEndVO();

        // 获取用户在店铺里的等级
        Map<Long, CommonLevelVO> levelMap = this.getLevelMapByStoreIds(Arrays.asList(marketingResponse.getStoreId())
                , customer);

        List<PaidCardCustomerRelVO> relVOList = new ArrayList<>();
        if (Objects.nonNull(customer)) {
            relVOList = paidCardCustomerRelQueryProvider
                    .list(PaidCardCustomerRelListRequest.builder().customerId(customer.getCustomerId()).build())
                    .getContext().getPaidCardCustomerRelVOList();
        }

        if (!validMarketing(marketingResponse, levelMap, customer, relVOList)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, marketingResponse.getMarketingName() + "无效！");
        }

        List<GoodsInfoVO> goodsInfoList = Collections.EMPTY_LIST;
        long lackCount = 0L;
        BigDecimal lackAmount = BigDecimal.ZERO;
        BigDecimal discount = BigDecimal.ZERO;

        // 采购单勾选的商品中没有参与该营销的情况，取营销的最低等级
        if (isPurchase && (goodsInfoIds == null || goodsInfoIds.isEmpty())) {
            // 满金额
            if (marketingResponse.getSubType() == MarketingSubType.REDUCTION_FULL_AMOUNT || marketingResponse.getSubType() == MarketingSubType.DISCOUNT_FULL_AMOUNT || marketingResponse.getSubType() == MarketingSubType.GIFT_FULL_AMOUNT) {

                BigDecimal levelAmount;

                switch (marketingResponse.getMarketingType()) {
                    case REDUCTION:
                        levelAmount = marketingResponse.getFullReductionLevelList().get(0).getFullAmount();
                        discount = marketingResponse.getFullReductionLevelList().get(0).getReduction();
                        response.setFullReductionLevel(marketingResponse.getFullReductionLevelList().get(0));

                        break;
                    case DISCOUNT:
                        levelAmount = marketingResponse.getFullDiscountLevelList().get(0).getFullAmount();
                        discount = marketingResponse.getFullDiscountLevelList().get(0).getDiscount();
                        response.setFullDiscountLevel(marketingResponse.getFullDiscountLevelList().get(0));

                        break;
                    case GIFT:
                        levelAmount = marketingResponse.getFullGiftLevelList().get(0).getFullAmount();
                        response.setFullGiftLevel(marketingResponse.getFullGiftLevelList().get(0));
                        response.setFullGiftLevelList(marketingResponse.getFullGiftLevelList());
                        break;
                    default:
                        levelAmount = BigDecimal.ZERO;
                }

                response.setLack(levelAmount);

            } else if (marketingResponse.getSubType() == MarketingSubType.REDUCTION_FULL_COUNT
                    || marketingResponse.getSubType() == MarketingSubType.DISCOUNT_FULL_COUNT
                    || marketingResponse.getSubType() == MarketingSubType.GIFT_FULL_COUNT
                    || marketingResponse.getSubType() == MarketingSubType.BUYOUT_PRICE
                    || marketingResponse.getSubType() == MarketingSubType.HALF_PRICE_SECOND_PIECE) {
                // 满数量
                // 计算达到营销级别的数量
                Long levelCount;

                switch (marketingResponse.getMarketingType()) {
                    case REDUCTION:
                        levelCount = marketingResponse.getFullReductionLevelList().get(0).getFullCount();
                        discount = marketingResponse.getFullReductionLevelList().get(0).getReduction();
                        response.setFullReductionLevel(marketingResponse.getFullReductionLevelList().get(0));

                        break;
                    case DISCOUNT:
                        levelCount = marketingResponse.getFullDiscountLevelList().get(0).getFullCount();
                        discount = marketingResponse.getFullDiscountLevelList().get(0).getDiscount();
                        response.setFullDiscountLevel(marketingResponse.getFullDiscountLevelList().get(0));

                        break;
                    case GIFT:
                        levelCount = marketingResponse.getFullGiftLevelList().get(0).getFullCount();
                        response.setFullGiftLevel(marketingResponse.getFullGiftLevelList().get(0));

                        response.setFullGiftLevelList(marketingResponse.getFullGiftLevelList());

                        break;
                    case BUYOUT_PRICE:
                        levelCount = marketingResponse.getBuyoutPriceLevelList().get(0).getChoiceCount();
                        response.setBuyoutPrice(marketingResponse.getBuyoutPriceLevelList().get(0).getFullAmount());
                        response.setBuyoutPriceLevel(marketingResponse.getBuyoutPriceLevelList().get(0));
                        break;
                    case HALF_PRICE_SECOND_PIECE:
                        levelCount = marketingResponse.getHalfPriceSecondPieceLevel().get(0).getNumber();
                        discount = marketingResponse.getHalfPriceSecondPieceLevel().get(0).getDiscount();
                        response.setHalfPriceSecondPieceLevel(marketingResponse.getHalfPriceSecondPieceLevel().get(0));
                        break;
                    default:
                        levelCount = 0L;
                }

                response.setLack(BigDecimal.valueOf(levelCount));
            } else if (marketingResponse.getSubType() != MarketingSubType.SUITS_GOODS) {
                throw new SbcRuntimeException(MarketingErrorCode.NOT_EXIST);
            }

            response.setGoodsInfoList(goodsInfoList);
            response.setDiscount(discount);
        } else {

            GoodsInfoViewByIdsResponse goodsInfoResponse;
            GoodsInfoViewByIdsRequest goodsInfoRequest = new GoodsInfoViewByIdsRequest();

            if (customer != null) {
                PurchaseRequest request = PurchaseRequest.builder()
                        .customerId(customer.getCustomerId()).inviteeId(Constants.PURCHASE_DEFAULT)
                        .goodsInfoIds(goodsInfoIds)
                        .build();
                List<Purchase> follows;
                Sort sort = request.getSort();
                if (Objects.nonNull(sort)) {
                    follows = purchaseRepository.findAll(request.getWhereCriteria(), sort);
                } else {
                    follows = purchaseRepository.findAll(request.getWhereCriteria());
                }

                goodsInfoRequest.setGoodsInfoIds(follows.stream().map(Purchase::getGoodsInfoId).collect(Collectors.toList()));
                goodsInfoResponse = goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();
                //填充SKU的购买数
                this.fillBuyCount(goodsInfoResponse.getGoodsInfos(), follows);

            } else {
                goodsInfoRequest.setGoodsInfoIds(goodsInfoIds);
                goodsInfoResponse = goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();
                if (goodsInfoResponse.getGoodsInfos() != null) {
                    goodsInfoResponse.getGoodsInfos().forEach((goodsInfo) -> {
                        goodsInfo.setBuyCount(0L);
                        List<PurchaseGoodsInfoDTO> dtoList =
                                frontReq.getGoodsInfoDTOList().stream().filter((purchase) ->
                                        goodsInfo.getGoodsInfoId().equals(purchase.getGoodsInfoId())).collect(Collectors.toList());
                        if (CollectionUtils.isNotEmpty(dtoList)) {
                            goodsInfo.setBuyCount((dtoList.get(0)).getGoodsNum());
                        }
                    });
                }
            }


            //设定SKU状态
            goodsInfoResponse.setGoodsInfos(
                    goodsInfoProvider.fillGoodsStatus(
                            GoodsInfoFillGoodsStatusRequest.builder()
                                    .goodsInfos(KsBeanUtil.convertList(goodsInfoResponse.getGoodsInfos(),
                                            GoodsInfoDTO.class))
                                    .build()
                    ).getContext().getGoodsInfos()
            );

            List<GoodsMarketingVO> goodsMarketingList;
            if (CollectionUtils.isNotEmpty(goodsInfoResponse.getGoodsInfos())) {
                if (!isPurchase) {
                    if (customer != null) {
                        goodsMarketingList = this.queryGoodsMarketingList(customer.getCustomerId());
                    } else {
                        goodsMarketingList = frontReq.getGoodsMarketingDTOList().stream().map((dto) ->
                                GoodsMarketingVO.builder().marketingId(dto.getMarketingId()).goodsInfoId(dto.getGoodsInfoId()).build()
                        ).collect(Collectors.toList());
                    }

                    Map<String, Long> goodsMarketingMap =
                            goodsMarketingList.stream().collect(Collectors.toMap(GoodsMarketingVO::getGoodsInfoId,
                                    GoodsMarketingVO::getMarketingId));

                    // 凑单页，过滤出参加营销的商品
                    goodsInfoList = goodsInfoResponse.getGoodsInfos().stream()
                            .filter(goodsInfo -> goodsInfo.getGoodsStatus() == GoodsStatus.OK
                                    && marketingResponse.getMarketingScopeList().stream().anyMatch(scope -> scope.getScopeId().equals(goodsInfo.getGoodsInfoId())))
                            .map(goodsInfo -> {
                                //
                                if (!marketingId.equals(goodsMarketingMap.get(goodsInfo.getGoodsInfoId()))) {
                                    goodsInfo.setBuyCount(0L);
                                }

                                return goodsInfo;
                            }).collect(Collectors.toList());
                } else {
                    // 过滤出参加营销的商品
                    goodsInfoList = goodsInfoResponse.getGoodsInfos().stream()
                            .filter(goodsInfo -> goodsInfo.getGoodsStatus() == GoodsStatus.OK
                                    && marketingResponse.getMarketingScopeList().stream().anyMatch(scope -> scope.getScopeId().equals(goodsInfo.getGoodsInfoId())))
                            .collect(Collectors.toList());
                }
            }

            //计算区间价
            GoodsIntervalPriceByCustomerIdResponse intervalPriceResponse =
                    goodsIntervalPriceProvider.putByCustomerId(
                            GoodsIntervalPriceByCustomerIdRequest.builder()
                                    .goodsInfoDTOList(KsBeanUtil.convert(goodsInfoList, GoodsInfoDTO.class))
                                    .customerId(customer != null ? customer.getCustomerId() : null).build()).getContext();
            List<GoodsIntervalPriceVO> goodsIntervalPrices = intervalPriceResponse.getGoodsIntervalPriceVOList();
            goodsInfoList = intervalPriceResponse.getGoodsInfoVOList();

            //计算级别价格
            goodsInfoList = marketingLevelPluginProvider.goodsListFilter(
                    MarketingLevelGoodsListFilterRequest.builder()
                            .customerDTO(customer != null ? KsBeanUtil.convert(customer, CustomerDTO.class) : null)
                            .goodsInfos(KsBeanUtil.convert(goodsInfoList, GoodsInfoDTO.class)).build())
                    .getContext().getGoodsInfoVOList();

            Map<String, BigDecimal> skuSalePriceMap = new HashMap<>();
            List<GoodsLevelPriceVO> goodsLevelPrices = this.getGoodsLevelPrices(null, customer, goodsInfoList);
            goodsInfoList.forEach(goodsInfo -> {
                //企业价
                Boolean IEPFlag = Boolean.FALSE;
                //判断当前会员是否是企业购会员，商品是否是企业购商品
                if (Objects.nonNull(customer) && customer.getEnterpriseCheckState() == EnterpriseCheckState.CHECKED
                        && Objects.nonNull(goodsInfo.getEnterPriseAuditState())
                        && goodsInfo.getEnterPriseAuditState() == EnterpriseAuditState.CHECKED
                        && Objects.nonNull(goodsInfo.getEnterPrisePrice())) {
                    Map<String, Object> vasList = redisService.hgetAll(ConfigKey.VALUE_ADDED_SERVICES.toString());
                    List<VASEntity> VASList = vasList.entrySet().stream().map(m -> {
                        VASEntity vasEntity = new VASEntity();
                        vasEntity.setServiceName(VASConstants.fromValue(m.getKey()));
                        vasEntity.setServiceStatus(StringUtils.equals(VASStatus.ENABLE.toValue(), m.getValue().toString()));
                        return vasEntity;
                    }).collect(Collectors.toList());
                    VASEntity vasEntity = VASList.stream()
                            .filter(f -> StringUtils.equals(f.getServiceName().toValue(), VASConstants.VAS_IEP_SETTING.toValue()) && f.isServiceStatus())
                            .findFirst().orElse(null);
                    if (Objects.nonNull(vasEntity) && vasEntity.isServiceStatus()) {
                        IEPFlag = Boolean.TRUE;
                    }
                }
                if (IEPFlag) {
                    BigDecimal price = goodsInfo.getEnterPrisePrice();
                    if(EnterprisePriceType.STOCK.toValue() == goodsInfo.getEnterprisePriceType()) {
                        // 按区间设价，获取满足的多个等级的区间价里的最大价格
                        Optional<GoodsIntervalPriceVO> optional = goodsInfo.getIntervalPriceList().stream().filter(goodsIntervalPrice
                                -> goodsIntervalPrice.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId()))
                                .filter(goodsIntervalPrice -> goodsInfo.getBuyCount().compareTo(goodsIntervalPrice.getCount()) >= 0).max(Comparator.comparingLong(GoodsIntervalPriceVO::getCount));
                        if (optional.isPresent()) {
                            price = optional.get().getPrice();
                        } else {
                            price = BigDecimal.ZERO;
                        }
                    } else if(EnterprisePriceType.CUSTOMER.toValue() == goodsInfo.getEnterprisePriceType()) {
                        //判断当前用户对应企业购商品等级企业价
                        if (CollectionUtils.isNotEmpty(goodsLevelPrices)) {
                            Optional<GoodsLevelPriceVO> first = goodsLevelPrices.stream()
                                    .filter(goodsLevelPrice -> goodsLevelPrice.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId()))
                                    .findFirst();
                            if (first.isPresent()) {
                                price = first.get().getPrice();
                            } else {
                                price = BigDecimal.ZERO;
                            }
                        }
                    }

                    skuSalePriceMap.put(goodsInfo.getGoodsInfoId(), price);
                } else if (goodsInfo.getPriceType() == GoodsPriceType.STOCK.toValue()) {
                    // 按区间设价，获取满足的多个等级的区间价里的最大价格
                    Optional<GoodsIntervalPriceVO> optional = goodsIntervalPrices.stream().filter(goodsIntervalPrice
                            -> goodsIntervalPrice.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId()))
                            .filter(goodsIntervalPrice -> goodsInfo.getBuyCount().compareTo(goodsIntervalPrice.getCount()) >= 0).max(Comparator.comparingLong(GoodsIntervalPriceVO::getCount));
                    if (optional.isPresent()) {
                        skuSalePriceMap.put(goodsInfo.getGoodsInfoId(), optional.get().getPrice());
                    } else {
                        skuSalePriceMap.put(goodsInfo.getGoodsInfoId(), BigDecimal.ZERO);
                    }
                } else if(Objects.nonNull(goodsInfo.getPaidCardIcon())) {
                    //付费会员价
                    skuSalePriceMap.put(goodsInfo.getGoodsInfoId(), goodsInfo.getSalePrice());
                } else if (goodsInfo.getPriceType() == GoodsPriceType.CUSTOMER.toValue()) {
                    // 按级别设价，获取级别价
                    skuSalePriceMap.put(goodsInfo.getGoodsInfoId(), goodsInfo.getSalePrice());
                } else {
                    skuSalePriceMap.put(goodsInfo.getGoodsInfoId(), goodsInfo.getMarketPrice());
                }
            });

            // 计算商品总额
            BigDecimal totalAmount = goodsInfoList.stream()
                    .map(goodsInfo -> skuSalePriceMap.getOrDefault(goodsInfo.getGoodsInfoId(), BigDecimal.ZERO).multiply(BigDecimal.valueOf(goodsInfo.getBuyCount())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 计算商品总数
            Long totalCount = goodsInfoList.stream().map(GoodsInfoVO::getBuyCount).reduce(0L, Long::sum);
            // 满金额
            if (marketingResponse.getSubType() == MarketingSubType.REDUCTION_FULL_AMOUNT || marketingResponse.getSubType() == MarketingSubType.DISCOUNT_FULL_AMOUNT || marketingResponse.getSubType() == MarketingSubType.GIFT_FULL_AMOUNT) {
                // 计算达到营销级别的金额
                BigDecimal levelAmount;

                // 根据不用的营销类型，计算满足条件的营销等级里最大的一个，如果不满足营销等级里任意一个，则默认取最低等级
                switch (marketingResponse.getMarketingType()) {
                    case REDUCTION:
                        Optional<MarketingFullReductionLevelVO> fullReductionLevelOptional =
                                marketingResponse.getFullReductionLevelList().stream().filter(level -> level.getFullAmount().compareTo(totalAmount) <= 0)
                                        .max(Comparator.comparing(MarketingFullReductionLevelVO::getFullAmount));

                        // 满足条件的最大的等级
                        if (fullReductionLevelOptional.isPresent()) {
                            levelAmount = fullReductionLevelOptional.get().getFullAmount();
                            discount = fullReductionLevelOptional.get().getReduction();
                            response.setFullReductionLevel(fullReductionLevelOptional.get());
                        } else {
                            // 没有满足条件，默认取最低等级
                            levelAmount = marketingResponse.getFullReductionLevelList().get(0).getFullAmount();
                            discount = marketingResponse.getFullReductionLevelList().get(0).getReduction();
                            response.setFullReductionLevel(marketingResponse.getFullReductionLevelList().get(0));
                        }

                        break;
                    case DISCOUNT:
                        Optional<MarketingFullDiscountLevelVO> fullDiscountLevelOptional =
                                marketingResponse.getFullDiscountLevelList().stream().filter(level -> level.getFullAmount().compareTo(totalAmount) <= 0)
                                        .max(Comparator.comparing(MarketingFullDiscountLevelVO::getFullAmount));

                        // 满足条件的最大的等级
                        if (fullDiscountLevelOptional.isPresent()) {
                            levelAmount = fullDiscountLevelOptional.get().getFullAmount();
                            discount = fullDiscountLevelOptional.get().getDiscount();
                            response.setFullDiscountLevel(fullDiscountLevelOptional.get());
                        } else {
                            // 没有满足条件，默认取最低等级
                            levelAmount = marketingResponse.getFullDiscountLevelList().get(0).getFullAmount();
                            discount = marketingResponse.getFullDiscountLevelList().get(0).getDiscount();
                            response.setFullDiscountLevel(marketingResponse.getFullDiscountLevelList().get(0));
                        }

                        break;
                    case GIFT:
                        List<MarketingFullGiftLevelVO> levels =
                                marketingResponse.getFullGiftLevelList().stream().filter(level -> level.getFullAmount().compareTo(totalAmount) <= 0).collect(Collectors.toList());

                        Optional<MarketingFullGiftLevelVO> fullGiftLevelOptional =
                                levels.stream().max(Comparator.comparing(MarketingFullGiftLevelVO::getFullAmount));

                        // 满足条件的最大的等级
                        if (fullGiftLevelOptional.isPresent()) {
                            levelAmount = fullGiftLevelOptional.get().getFullAmount();
                            response.setFullGiftLevel(fullGiftLevelOptional.get());
                        } else {
                            // 没有满足条件，默认取最低等级
                            levelAmount = marketingResponse.getFullGiftLevelList().get(0).getFullAmount();
                            response.setFullGiftLevel(marketingResponse.getFullGiftLevelList().get(0));
                        }

                        response.setFullGiftLevelList(marketingResponse.getFullGiftLevelList());

                        break;
                    default:
                        levelAmount = BigDecimal.ZERO;
                }

                // 计算达到营销级别的差额
                lackAmount = levelAmount.compareTo(totalAmount) <= 0 ? BigDecimal.ZERO :
                        levelAmount.subtract(totalAmount);

                response.setLack(lackAmount);

            } else if (marketingResponse.getSubType() == MarketingSubType.REDUCTION_FULL_COUNT
                    || marketingResponse.getSubType() == MarketingSubType.DISCOUNT_FULL_COUNT
                    || marketingResponse.getSubType() == MarketingSubType.GIFT_FULL_COUNT
                    || marketingResponse.getSubType() == MarketingSubType.BUYOUT_PRICE
                    || marketingResponse.getSubType() == MarketingSubType.HALF_PRICE_SECOND_PIECE) {
                // 满数量
                // 计算达到营销级别的数量
                Long levelCount;

                switch (marketingResponse.getMarketingType()) {
                    case REDUCTION:
                        Optional<MarketingFullReductionLevelVO> fullReductionLevelOptional =
                                marketingResponse.getFullReductionLevelList().stream().filter(level -> level.getFullCount().compareTo(totalCount) <= 0)
                                        .max(Comparator.comparing(MarketingFullReductionLevelVO::getFullCount));

                        // 满足条件的最大的等级
                        if (fullReductionLevelOptional.isPresent()) {
                            levelCount = fullReductionLevelOptional.get().getFullCount();
                            discount = fullReductionLevelOptional.get().getReduction();
                            response.setFullReductionLevel(fullReductionLevelOptional.get());
                        } else {
                            // 没有满足条件，默认取最低等级
                            levelCount = marketingResponse.getFullReductionLevelList().get(0).getFullCount();
                            discount = marketingResponse.getFullReductionLevelList().get(0).getReduction();
                            response.setFullReductionLevel(marketingResponse.getFullReductionLevelList().get(0));
                        }

                        break;
                    case DISCOUNT:
                        Optional<MarketingFullDiscountLevelVO> fullDiscountLevelOptional =
                                marketingResponse.getFullDiscountLevelList().stream().filter(level -> level.getFullCount().compareTo(totalCount) <= 0)
                                        .max(Comparator.comparing(MarketingFullDiscountLevelVO::getFullCount));

                        // 满足条件的最大的等级
                        if (fullDiscountLevelOptional.isPresent()) {
                            levelCount = fullDiscountLevelOptional.get().getFullCount();
                            discount = fullDiscountLevelOptional.get().getDiscount();
                            response.setFullDiscountLevel(fullDiscountLevelOptional.get());
                        } else {
                            // 没有满足条件，默认取最低等级
                            levelCount = marketingResponse.getFullDiscountLevelList().get(0).getFullCount();
                            discount = marketingResponse.getFullDiscountLevelList().get(0).getDiscount();
                            response.setFullDiscountLevel(marketingResponse.getFullDiscountLevelList().get(0));
                        }

                        break;
                    case GIFT:
                        List<MarketingFullGiftLevelVO> giftLevels =
                                marketingResponse.getFullGiftLevelList().stream().filter(level -> level.getFullCount().compareTo(totalCount) <= 0).collect(Collectors.toList());

                        Optional<MarketingFullGiftLevelVO> fullGiftLevelOptional =
                                giftLevels.stream().max(Comparator.comparing(MarketingFullGiftLevelVO::getFullCount));

                        // 满足条件的最大的等级
                        if (fullGiftLevelOptional.isPresent()) {
                            levelCount = fullGiftLevelOptional.get().getFullCount();
                            response.setFullGiftLevel(fullGiftLevelOptional.get());
                        } else {
                            // 没有满足条件，默认取最低等级
                            levelCount = marketingResponse.getFullGiftLevelList().get(0).getFullCount();
                            response.setFullGiftLevel(marketingResponse.getFullGiftLevelList().get(0));
                        }

                        response.setFullGiftLevelList(marketingResponse.getFullGiftLevelList());

                        break;
                    case BUYOUT_PRICE:
                        List<MarketingBuyoutPriceLevelVO> buyoutPriceLevels =
                                marketingResponse.getBuyoutPriceLevelList().stream().filter(level -> level.getChoiceCount().compareTo(totalCount) <= 0).collect(Collectors.toList());
                        MarketingBuyoutPriceLevelVO levelVO = buyoutPriceLevels.stream().max(Comparator.comparing(MarketingBuyoutPriceLevelVO::getChoiceCount)).orElse(null);
                        if (Objects.nonNull(levelVO)) {
                            levelCount = levelVO.getChoiceCount();
                            //按价格倒序,以便优先以价格高的商品凑单计算优惠金额
                            List<GoodsInfoVO> marketInfoInfoList = goodsInfoList.stream()
                                    .sorted((v1, v2) -> skuSalePriceMap.getOrDefault(v2.getGoodsInfoId(), BigDecimal.ZERO).compareTo(skuSalePriceMap.getOrDefault(v1.getGoodsInfoId(), BigDecimal.ZERO)))
                                    .collect(Collectors.toList());
                            BigDecimal noDiscountPrice = BigDecimal.ZERO;
                            Long mod = NumberUtils.LONG_ZERO;
                            BigDecimal fullAmount = BigDecimal.ZERO;
                            int size = marketInfoInfoList.size();
                            int number = 1;
                            for (GoodsInfoVO info : marketInfoInfoList) {
                                mod += info.getBuyCount();
                                if (mod > levelCount) {
                                    fullAmount = fullAmount.add(BigDecimal.valueOf(mod / levelCount).multiply(levelVO.getFullAmount()));
                                    mod = mod % levelCount;
                                }
                                if (mod.equals(levelCount)) {
                                    fullAmount = fullAmount.add(levelVO.getFullAmount());
                                    mod = NumberUtils.LONG_ZERO;
                                }
                                if (number == size) {
                                    noDiscountPrice = noDiscountPrice.add(skuSalePriceMap.getOrDefault(info.getGoodsInfoId(), BigDecimal.ZERO).multiply(BigDecimal.valueOf((mod))));
                                } else {
                                    number++;
                                }
                            }
                            //一口价+部分未优惠的单价
                            BigDecimal newPrice = fullAmount.add(noDiscountPrice);
                            response.setBuyoutPrice(newPrice);
                            discount = totalAmount.compareTo(newPrice) < 0 ? BigDecimal.ZERO : totalAmount.subtract(newPrice);
                            response.setBuyoutPriceLevel(levelVO);
                        } else {
                            levelCount = marketingResponse.getBuyoutPriceLevelList().get(0).getChoiceCount();
                            response.setBuyoutPrice(marketingResponse.getBuyoutPriceLevelList().get(0).getFullAmount());
                            discount = totalAmount.compareTo(response.getBuyoutPrice()) < 0 ? BigDecimal.ZERO : totalAmount.subtract(response.getBuyoutPrice());
                            response.setBuyoutPriceLevel(marketingResponse.getBuyoutPriceLevelList().get(0));
                        }
                        break;
                    case HALF_PRICE_SECOND_PIECE:
                        //获取第二件半价营销规则
                        MarketingHalfPriceSecondPieceLevelVO halfPriceSecondPieceLevel = marketingResponse.getHalfPriceSecondPieceLevel().get(0);
                        // 满足条件，获取所有参与第二件半价商品的最小价格
                        if (halfPriceSecondPieceLevel.getNumber().compareTo(totalCount) <= 0) {
                            //按价格倒序,以便优先以价格高的商品凑单计算优惠金额
                            List<GoodsInfoVO> marketInfoInfoList = goodsInfoList.stream()
                                    .sorted((v1, v2) -> skuSalePriceMap.getOrDefault(v1.getGoodsInfoId(),
                                            BigDecimal.ZERO).compareTo(skuSalePriceMap.getOrDefault(v2.getGoodsInfoId(), BigDecimal.ZERO)))
                                    .collect(Collectors.toList());
                            BigDecimal halfPriceSecondPiece = BigDecimal.ZERO;
                            //判断购买数量是否满足折扣数量,
                            //1.购买一件，多次达到等级优惠，2.购买两件，多次达到购买优惠
                            Long grade = totalCount / halfPriceSecondPieceLevel.getNumber();
                            if (grade > 0) {
                                if (marketInfoInfoList.size() < grade) {
                                    if (marketInfoInfoList.size() == 1) {
                                        for (int i = 0; i < grade; i++) {
                                            if (halfPriceSecondPieceLevel.getDiscount().compareTo(BigDecimal.ZERO) == 0) {
                                                //第N件0折，第N件是买N件送一件，以价格最低的送
                                                halfPriceSecondPiece = halfPriceSecondPiece.add(skuSalePriceMap.getOrDefault(marketInfoInfoList.get(0).getGoodsInfoId(), BigDecimal.ZERO));
                                            } else {
                                                //第N件以最小的商品金额计算折扣
                                                halfPriceSecondPiece = halfPriceSecondPiece.add(skuSalePriceMap.getOrDefault(marketInfoInfoList.get(0).getGoodsInfoId(), BigDecimal.ZERO).multiply((BigDecimal.valueOf(10).subtract(halfPriceSecondPieceLevel.getDiscount()).multiply(BigDecimal.valueOf(0.1)))).setScale(2, BigDecimal.ROUND_HALF_UP));
                                            }
                                        }
                                    } else {
                                        for (GoodsInfoVO gradeGoods : marketInfoInfoList) {
                                            if (halfPriceSecondPieceLevel.getDiscount().compareTo(BigDecimal.ZERO) == 0) {
                                                //第N件0折，第N件是买N件送一件，以价格最低的送
                                                halfPriceSecondPiece = halfPriceSecondPiece.add(skuSalePriceMap.getOrDefault(gradeGoods.getGoodsInfoId(), BigDecimal.ZERO));
                                            } else {
                                                //第N件以最小的商品金额计算折扣
                                                halfPriceSecondPiece =
                                                        halfPriceSecondPiece.add(skuSalePriceMap.getOrDefault(gradeGoods.getGoodsInfoId(), BigDecimal.ZERO).multiply((BigDecimal.valueOf(10).subtract(halfPriceSecondPieceLevel.getDiscount()).multiply(BigDecimal.valueOf(0.1)))).setScale(2, BigDecimal.ROUND_HALF_UP));
                                            }
                                        }

                                        for (int i = 0; i < grade - marketInfoInfoList.size(); i++) {
                                            if (halfPriceSecondPieceLevel.getDiscount().compareTo(BigDecimal.ZERO) == 0) {
                                                //第N件0折，第N件是买N件送一件，以价格最低的送
                                                halfPriceSecondPiece =
                                                        halfPriceSecondPiece.add(skuSalePriceMap.getOrDefault(marketInfoInfoList.get(marketInfoInfoList.size() - 1).getGoodsInfoId(), BigDecimal.ZERO));
                                            } else {
                                                //第N件以最小的商品金额计算折扣
                                                halfPriceSecondPiece =
                                                        halfPriceSecondPiece.add(skuSalePriceMap.getOrDefault(marketInfoInfoList.get(marketInfoInfoList.size() - 1).getGoodsInfoId(), BigDecimal.ZERO).multiply((BigDecimal.valueOf(10).subtract(halfPriceSecondPieceLevel.getDiscount()).multiply(BigDecimal.valueOf(0.1)))).setScale(2, BigDecimal.ROUND_HALF_UP));
                                            }
                                        }
                                    }
                                } else {//当购买的商品数量大于优惠等级，根本优惠等级获取价格最小的商品进行优惠折扣计算
                                    List<GoodsInfoVO> grademarketInfoInfoList = marketInfoInfoList.subList(0, grade.intValue());
                                    for (GoodsInfoVO gradeGoods : grademarketInfoInfoList) {
                                        if (halfPriceSecondPieceLevel.getDiscount().compareTo(BigDecimal.ZERO) == 0) {
                                            //第N件0折，第N件是买N件送一件，以价格最低的送
                                            halfPriceSecondPiece =
                                                    halfPriceSecondPiece.add(skuSalePriceMap.getOrDefault(gradeGoods.getGoodsInfoId(), BigDecimal.ZERO));
                                        } else {
                                            //第N件以最小的商品金额计算折扣
                                            halfPriceSecondPiece =
                                                    halfPriceSecondPiece.add(skuSalePriceMap.getOrDefault(gradeGoods.getGoodsInfoId(), BigDecimal.ZERO).multiply((BigDecimal.valueOf(10).subtract(halfPriceSecondPieceLevel.getDiscount()).multiply(BigDecimal.valueOf(0.1)))).setScale(2, BigDecimal.ROUND_HALF_UP));
                                        }
                                    }
                                }
                            }
                            levelCount = marketingResponse.getHalfPriceSecondPieceLevel().get(0).getNumber();
                            discount = totalAmount.compareTo(halfPriceSecondPiece) < 0 ? BigDecimal.ZERO : halfPriceSecondPiece;
                            response.setHalfPriceSecondPieceLevel(halfPriceSecondPieceLevel);
                        } else {
                            levelCount = marketingResponse.getHalfPriceSecondPieceLevel().get(0).getNumber();
                            discount = BigDecimal.ZERO;
                            response.setHalfPriceSecondPieceLevel(halfPriceSecondPieceLevel);
                        }
                        break;
                    default:
                        levelCount = 0L;
                }

                // 计算达到营销级别缺少的数量
                lackCount = levelCount.compareTo(totalCount) <= 0 ? 0L :
                        levelCount.longValue() - totalCount.longValue();

                response.setLack(BigDecimal.valueOf(lackCount));
            } else if (marketingResponse.getSubType() != MarketingSubType.SUITS_GOODS) {
                throw new SbcRuntimeException(MarketingErrorCode.NOT_EXIST);
            }

            response.setGoodsInfoList(goodsInfoList);
            response.setTotalCount(totalCount);
            response.setTotalAmount(totalAmount);
            response.setDiscount(discount);
//            }
        }

        // 如果有赠品，则查询赠品商品的详细信息
        if (response.getFullGiftLevelList() != null && !response.getFullGiftLevelList().isEmpty()) {
            List<String> skuIds =
                    response.getFullGiftLevelList().stream().flatMap(marketingFullGiftLevel -> marketingFullGiftLevel.getFullGiftDetailList().stream().map(MarketingFullGiftDetailVO::getProductId)).distinct().collect(Collectors.toList());
            GoodsInfoViewByIdsRequest goodsInfoRequest = GoodsInfoViewByIdsRequest.builder()
                    .goodsInfoIds(skuIds)
                    .isHavSpecText(Constants.yes)
                    .build();
            response.setGiftGoodsInfoResponse(KsBeanUtil.convert(goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext(), PurchaseGoodsViewVO.class));
        }

        response.setStoreId(marketingResponse.getStoreId());
        response.setMarketingId(marketingResponse.getMarketingId());
        response.setMarketingType(marketingResponse.getMarketingType());
        response.setSubType(marketingResponse.getSubType());


        return response;
    }

    /**
     * 获取商品营销信息
     *
     * @return
     */
    public PurchaseGetGoodsMarketingResponse getGoodsMarketing(List<GoodsInfoVO> goodsInfos, CustomerVO customer) {
        Map<String, List<MarketingViewVO>> resMap = new LinkedHashMap<>();
        PurchaseGetGoodsMarketingResponse marketingResponse = new PurchaseGetGoodsMarketingResponse();

        if (goodsInfos.isEmpty()) {
            marketingResponse.setMap(resMap);
            marketingResponse.setGoodsInfos(goodsInfos);
            return marketingResponse;
        }

        List<GoodsInfoDTO> goodsInfoList = goodsInfoMapper.goodsInfoVOsToGoodsInfoDTOs(goodsInfos);
        CustomerDTO customerDTO = custmerMapper.customerVOToCustomerDTO(customer);

        // 设置级别价
        goodsInfos = marketingLevelPluginProvider.goodsListFilter(MarketingLevelGoodsListFilterRequest.builder()
                .goodsInfos(goodsInfoList).customerDTO(customerDTO).build()).getContext().getGoodsInfoVOList();
        goodsInfoList = goodsInfoMapper.goodsInfoVOsToGoodsInfoDTOs(goodsInfos);

        //获取营销
        Map<String, List<MarketingViewVO>> marketingMap = marketingPluginQueryProvider.getByGoodsInfoListAndCustomer(
                MarketingPluginByGoodsInfoListAndCustomerRequest.builder().goodsInfoList(goodsInfoList)
                        .customerDTO(customerDTO).build()).getContext().getMarketingMap();


        marketingMap.forEach((key, value) -> {
            // 商品营销排序 减>折>赠
            List<MarketingViewVO> marketingResponses = value.stream().filter(marketingViewVO -> !Objects.equals(marketingViewVO.getMarketingType(), MarketingType.SUITS))
                    .sorted(Comparator.comparing(MarketingViewVO::getMarketingType)).collect(Collectors.toList());

            if (!marketingResponses.isEmpty()) {
                // 获取该商品的满赠营销
                Optional<MarketingViewVO> fullGiftMarketingOptional = marketingResponses.stream()
                        .filter(marketing -> marketing.getMarketingType() == MarketingType.GIFT).findFirst();

                fullGiftMarketingOptional.ifPresent(fullGiftMarketingResponse -> {
                    List<String> skuIds = fullGiftMarketingResponse.getFullGiftLevelList().stream()
                            .flatMap(marketingFullGiftLevel ->
                                    marketingFullGiftLevel.getFullGiftDetailList().stream()
                                            .map(MarketingFullGiftDetailVO::getProductId))
                            .distinct().collect(Collectors.toList());
                    GoodsInfoViewByIdsRequest goodsInfoRequest = GoodsInfoViewByIdsRequest.builder()
                            .goodsInfoIds(skuIds)
                            .isHavSpecText(Constants.yes)
                            .build();
                    GoodsInfoViewByIdsResponse idsResponse =
                            goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();
                    fullGiftMarketingResponse.setGoodsList(GoodsInfoResponseVO.builder()
                            .goodsInfos(idsResponse.getGoodsInfos()).goodses(idsResponse.getGoodses()).build());
                });

                resMap.put(key, marketingResponses);
            }
        });

        marketingResponse.setMap(resMap);
        marketingResponse.setGoodsInfos(goodsInfos);
        return marketingResponse;
    }


    /**
     * 验证营销是否可用
     *
     * @param marketingResponse
     * @param levelMap
     * @return
     */
    private boolean validMarketing(MarketingForEndVO marketingResponse, Map<Long, CommonLevelVO> levelMap, CustomerVO customer, List<PaidCardCustomerRelVO> relVOList) {
        boolean valid = true;
        // 判断会员等级是否满足要求
        //校验营销活动
        if (marketingResponse.getIsPause() == BoolFlag.YES || marketingResponse.getDelFlag() == DeleteFlag.YES || marketingResponse.getBeginTime().isAfter(LocalDateTime.now())
                || marketingResponse.getEndTime().isBefore(LocalDateTime.now())) {
            valid = false;
        }

        //校验用户级别
        CommonLevelVO level = levelMap.get(marketingResponse.getStoreId());
        switch (marketingResponse.getMarketingJoinLevel()) {
            case ALL_CUSTOMER:
                break;
            case ALL_LEVEL:
//                if (level == null) {
//                    valid = false;
//                }
                break;
            case LEVEL_LIST:
                if (level == null || !marketingResponse.getJoinLevelList().contains(level.getLevelId())) {
                    valid = false;
                }
                break;
            case PAID_CARD_CUSTOMER:
                if (CollectionUtils.isEmpty(relVOList)) {
                    valid = false;
                }
                break;
            case ENTERPRISE_CUSTOMER:
                if (Objects.nonNull(customer) && !EnterpriseCheckState.CHECKED.equals(customer.getEnterpriseCheckState())) {
                    valid = false;
                }
                break;
            default:
                break;
        }

        return valid;
    }

    /**
     * 获取用户在店铺里的等级
     *
     * @return
     */
    private Map<Long, CommonLevelVO> getLevelMap(List<GoodsInfoVO> goodsInfos, CustomerVO customer) {
        return marketingPluginQueryProvider.getCustomerLevelsByGoodsInfoListAndCustomer(
                MarketingPluginGetCustomerLevelsRequest.builder()
                        .goodsInfoList(KsBeanUtil.convert(goodsInfos, GoodsInfoDTO.class))
                        .customer(KsBeanUtil.convert(customer, CustomerDTO.class)).build()
        ).getContext().getCommonLevelVOMap();
    }

    /**
     * 获取用户在店铺里的等级
     *
     * @return
     */
    private Map<Long, CommonLevelVO> getLevelMapByStoreIds(List<Long> storeIds, CustomerVO customer) {
        return marketingPluginQueryProvider.getCustomerLevelsByStoreIds(
                MarketingPluginGetCustomerLevelsByStoreIdsRequest.builder().storeIds(storeIds)
                        .customer(KsBeanUtil.convert(customer, CustomerDTO.class)).build()).getContext()
                .getCommonLevelVOMap();
    }

    /**
     * 获取采购单商品选择的营销
     *
     * @param customerId
     * @return
     */
    public List<GoodsMarketingVO> queryGoodsMarketingList(String customerId) {
        GoodsMarketingListByCustomerIdRequest request = new GoodsMarketingListByCustomerIdRequest();
        request.setCustomerId(customerId);
        List<GoodsMarketingVO> voList = goodsMarketingQueryProvider.listByCustomerId(request).getContext()
                .getGoodsMarketings();
        return Objects.isNull(voList) ? Collections.emptyList() : voList;
    }

    /**
     * [公共方法]获取店铺营销信息
     * 同时处理未登录 / 已登录时的逻辑
     *
     * @param goodsMarketings 商品营销信息
     * @param customer        登录人信息
     * @param frontReq        前端传入的采购单信息
     * @param goodsInfoIdList 勾选的skuIdList
     * @return
     */
    public Map<Long, List<PurchaseMarketingCalcResponse>> getStoreMarketingBase(List<GoodsMarketingDTO> goodsMarketings,
                                                                                CustomerVO customer,
                                                                                PurchaseFrontRequest frontReq,
                                                                                List<String> goodsInfoIdList) {
        Map<Long, List<PurchaseMarketingCalcResponse>> storeMarketingMap = new HashMap<>();

        if (CollectionUtils.isEmpty(goodsMarketings)) {
            return storeMarketingMap;
        }

        // 参加同种营销的商品列表
        Map<Long, List<String>> marketingGoodsesMap = new HashMap<>();

        IteratorUtils.groupBy(goodsMarketings, GoodsMarketingDTO::getMarketingId).entrySet().stream().forEach(set -> {
            marketingGoodsesMap.put(set.getKey(), set.getValue().stream()
                    .filter(goodsMarketing -> goodsInfoIdList != null
                            && goodsInfoIdList.contains(goodsMarketing.getGoodsInfoId()))
                    .map(GoodsMarketingDTO::getGoodsInfoId).collect(Collectors.toList()));
        });

        // 根据同种营销的商品列表，计算营销
        marketingGoodsesMap.entrySet().stream().forEach(set -> {
            PurchaseMarketingCalcResponse response;
            if (customer != null) {
                response = this.calcMarketingByMarketingIdBase(Long.valueOf(set.getKey()), customer, null,
                        set.getValue(), true);
            } else {
                response = this.calcMarketingByMarketingIdBase(Long.valueOf(set.getKey()), customer, frontReq,
                        set.getValue(), true);
            }

            if (storeMarketingMap.get(response.getStoreId()) == null) {
                List<PurchaseMarketingCalcResponse> calcResponses = new ArrayList<>();
                calcResponses.add(response);

                storeMarketingMap.put(response.getStoreId(), calcResponses);
            } else {
                storeMarketingMap.get(response.getStoreId()).add(response);
            }
        });

        // 营销按减>折>赠排序
        storeMarketingMap.entrySet().stream().forEach(set -> {
            set.setValue(set.getValue().stream().sorted(Comparator.comparing(PurchaseMarketingCalcResponse::getMarketingType)).collect(Collectors.toList()));
        });

        return storeMarketingMap;
    }

    /**
     * 获取店铺营销信息
     * 已登录场景
     *
     * @param customer
     * @param goodsInfoIdList
     * @return
     */
    public Map<Long, List<PurchaseMarketingCalcResponse>> getStoreMarketing(List<GoodsMarketingDTO> goodsMarketings,
                                                                            CustomerVO customer,
                                                                            List<String> goodsInfoIdList) {
        return this.getStoreMarketingBase(goodsMarketings, customer, null, goodsInfoIdList);
    }

    /**
     * 获取店铺下，是否有优惠券营销，展示优惠券标签
     *
     * @param goodsInfoList
     * @param customer
     * @return
     */
//    public Map<Long, Boolean> getStoreCouponExist(List<GoodsInfo> goodsInfoList, Customer customer) {
    public Map<Long, Boolean> getStoreCouponExist(List<GoodsInfoVO> goodsInfoList, CustomerVO customer) {
        Map<Long, Boolean> result = new HashMap<>();
        if (CollectionUtils.isEmpty(goodsInfoList)) {
            return result;
        }
        //  List<CouponCache> couponCaches = couponCacheService.listCouponForGoodsList(goodsInfoList, customer);

        CouponCacheListForGoodsGoodInfoListRequest request =
                CouponCacheListForGoodsGoodInfoListRequest.builder().customer(customer)
                        .goodsInfoList(goodsInfoList).build();

        BaseResponse<CouponCacheListForGoodsDetailResponse> cacheresponse =
                couponCacheProvider.listCouponForGoodsList(request);

        List<CouponCacheVO> couponCaches = cacheresponse.getContext().getCouponCacheVOList();

        boolean hasPlatformCoupon =
                couponCaches.stream().filter(marketInfo -> marketInfo.getCouponInfo().getCouponType().equals(CouponType.GENERAL_VOUCHERS))
                        .collect(Collectors.toList()).size() > 0;
        Map<Long, List<CouponCacheVO>> couponCacheMap =
                couponCaches.stream().collect(Collectors.groupingBy(marketInfo -> marketInfo.getCouponInfo().getStoreId()));
        goodsInfoList.stream().map(GoodsInfoVO::getStoreId).forEach(marketInfo -> {
            if (hasPlatformCoupon) {
                result.put(marketInfo, true);
            } else {
                result.put(marketInfo, CollectionUtils.isNotEmpty(couponCacheMap.get(marketInfo)));
            }
        });
        return result;
    }

    /**
     * 计算采购单金额
     *
     * @param response
     * @param goodsInfoIds
     */
    public PurchaseListResponse calcAmount(PurchaseListResponse response, List<String> goodsInfoIds,
                                           String customerId) {
        // 没有勾选商品，默认金额为0
        if (goodsInfoIds == null || goodsInfoIds.isEmpty()) {
            return response;
        }

        // 参与营销的商品
        List<String> marketingGoodsInfoIds =
                response.getGoodsMarketingMap().entrySet().stream().map(set -> set.getKey()).collect(Collectors.toList());

        Boolean isGoodsPoint = systemPointsConfigService.isGoodsPoint();

        List<String> pointPriceGoodsInfoIds = response.getGoodsInfos().stream().filter(goodsInfoVO -> isGoodsPoint && Objects.nonNull(goodsInfoVO.getBuyPoint()) && goodsInfoVO.getBuyPoint() > 0).map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());

        List<String> excludePointPriceGoodsInfoIds = goodsInfoIds;
        if (CollectionUtils.isNotEmpty(pointPriceGoodsInfoIds)) {
            excludePointPriceGoodsInfoIds = goodsInfoIds.stream().filter(g -> !pointPriceGoodsInfoIds.contains(g)).collect(Collectors.toList());
        }
        List<String> appointGoodsInfoIdList = new ArrayList<>();
        AppointmentSaleInProcessResponse appointmentSaleInProcessResponse = null;
        if (CollectionUtils.isNotEmpty(excludePointPriceGoodsInfoIds)) {
            appointmentSaleInProcessResponse = appointmentSaleQueryProvider.inProgressAppointmentSaleInfoByGoodsInfoIdList(AppointmentSaleInProgressRequest.builder().
                    goodsInfoIdList(excludePointPriceGoodsInfoIds).build()).getContext();
            appointGoodsInfoIdList = appointmentSaleInProcessResponse.getAppointmentSaleVOList().stream().map(m -> m.getAppointmentSaleGood().getGoodsInfoId()).collect(Collectors.toList());
        }
        BigDecimal appointmentTotalPrice = BigDecimal.ZERO;
        List<String> appointGoodsInfoIds = appointGoodsInfoIdList;
        if (CollectionUtils.isNotEmpty(appointGoodsInfoIds)) {

            appointGoodsInfoIds.forEach(aid -> {
                if (CollectionUtils.isNotEmpty(marketingGoodsInfoIds) && marketingGoodsInfoIds.contains(aid)) {
                    response.getGoodsMarketingMap().remove(aid);
                }
            });

            Map<String, GoodsInfoVO> goodsInfoVOMap = response.getGoodsInfos().stream().collect(Collectors.toMap(GoodsInfoVO::getGoodsInfoId, v -> v));
            appointmentTotalPrice = appointmentSaleInProcessResponse.getAppointmentSaleVOList().stream().map(
                    a -> Objects.isNull(a.getAppointmentSaleGood().getPrice()) ?
                            goodsInfoVOMap.get(a.getAppointmentSaleGood().getGoodsInfoId()).getMarketPrice().multiply(BigDecimal.valueOf(goodsInfoVOMap.get(a.getAppointmentSaleGood().getGoodsInfoId()).getBuyCount())) :
                            a.getAppointmentSaleGood().getPrice().multiply(BigDecimal.valueOf(goodsInfoVOMap.get(a.getAppointmentSaleGood().getGoodsInfoId()).getBuyCount()))
            ).reduce(BigDecimal.ZERO, BigDecimal::add);
        }


        BigDecimal marketingGoodsTotalPrice = response.getStoreMarketingMap().values().parallelStream()
                .flatMap(storeMarketings -> storeMarketings.parallelStream())
                .map(PurchaseMarketingCalcVO::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 勾选的商品中没有参与营销的商品总额
        BigDecimal noMarketingGoodsTotalPrice =
                response.getGoodsInfos().stream().filter(goodsInfo -> goodsInfoIds.contains(goodsInfo.getGoodsInfoId()) && !marketingGoodsInfoIds.contains(goodsInfo.getGoodsInfoId()) && !appointGoodsInfoIds.contains(goodsInfo.getGoodsInfoId())).map(goodsInfo -> {
                    goodsInfo.setMarketPrice(Objects.isNull(goodsInfo.getMarketPrice()) ? BigDecimal.ZERO : goodsInfo
                            .getMarketPrice());
                    // 社交分销-购物车-分销价2
                    if (Objects.equals(goodsInfo.getDistributionGoodsAudit(), DistributionGoodsAudit.CHECKED)) {
                        //取市场价
                        return goodsInfo.getMarketPrice().multiply(BigDecimal.valueOf(goodsInfo.getBuyCount()));
                    }
                    if (goodsInfo.getPriceType() == GoodsPriceType.STOCK.toValue()) {
                        // 按区间设价，获取满足的多个等级的区间价里的最大价格
                        Optional<GoodsIntervalPriceVO> optional =
                                response.getGoodsIntervalPrices().stream().filter(goodsIntervalPrice -> goodsIntervalPrice.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId()))
                                        .filter(goodsIntervalPrice -> goodsInfo.getBuyCount().compareTo(goodsIntervalPrice.getCount()) >= 0).max(Comparator.comparingLong(GoodsIntervalPriceVO::getCount));

                        if (optional.isPresent()) {
                            return optional.get().getPrice().multiply(BigDecimal.valueOf(goodsInfo.getBuyCount()));
                        } else {
                            return BigDecimal.ZERO;
                        }
                    } else if (goodsInfo.getPriceType() == GoodsPriceType.CUSTOMER.toValue()) {
                        // 按级别设价，获取级别价
                        return goodsInfo.getSalePrice().multiply(BigDecimal.valueOf(goodsInfo.getBuyCount()));
                    } else {
                        // 默认取市场价
                        return goodsInfo.getMarketPrice().multiply(BigDecimal.valueOf(goodsInfo.getBuyCount()));
                    }


                }).reduce(BigDecimal.ZERO, BigDecimal::add);

        // 优惠总额
        BigDecimal discountTotalPrice =
                response.getStoreMarketingMap().values().parallelStream().flatMap(storeMarketings -> storeMarketings.parallelStream()).map(storeMarketing -> {
                    if (BigDecimal.ZERO.compareTo(storeMarketing.getLack()) == 0) {
                        BigDecimal discountPrice = BigDecimal.ZERO;
                        BigDecimal totalAmount = storeMarketing.getTotalAmount();

                        // 满减
                        if (storeMarketing.getMarketingType() == MarketingType.REDUCTION) {
                            discountPrice = storeMarketing.getDiscount();
                        } else if (storeMarketing.getMarketingType() == MarketingType.DISCOUNT) {
                            // 满折
                            discountPrice = totalAmount.multiply(BigDecimal.ONE.subtract(storeMarketing.getDiscount()));
                        } else if (storeMarketing.getMarketingType() == MarketingType.BUYOUT_PRICE) {
                            discountPrice = storeMarketing.getDiscount();
                        } else if (storeMarketing.getMarketingType() == MarketingType.HALF_PRICE_SECOND_PIECE) {
                            discountPrice = storeMarketing.getDiscount();
                        }

                        // 只有参与营销活动的商品总金额不小于优惠金额时，才返回优惠金额，否则返回商品总额
                        if (totalAmount.compareTo(discountPrice) >= 0) {
                            return discountPrice;
                        } else {
                            return totalAmount;
                        }
                    }

                    return BigDecimal.ZERO;
                }).reduce(BigDecimal.ZERO, BigDecimal::add);

        Long sumBuyPoint = response.getGoodsInfos().stream()
                .filter(goodsInfo -> goodsInfoIds.contains(goodsInfo.getGoodsInfoId()) && Objects.nonNull(goodsInfo.getBuyPoint()))
                .mapToLong(goodsInfo -> goodsInfo.getBuyPoint() * goodsInfo.getBuyCount())
                .sum();
        response.setBuyPointValid(true);
        if (response.getTotalBuyPoint() != null && response.getTotalBuyPoint() > 0
                && customerId != null
                && (!verifyService.verifyBuyPoints(response.getTotalBuyPoint(), customerId))) {
            response.setBuyPointValid(false);
        }

        // 商品总额=营销商品总额+非营销商品总额
        BigDecimal totalPrice = marketingGoodsTotalPrice.add(noMarketingGoodsTotalPrice).add(appointmentTotalPrice);

        BigDecimal tradePrice = totalPrice.subtract(discountTotalPrice);

        // 避免总价为负数
        if (tradePrice.compareTo(BigDecimal.ZERO) < 0) {
            tradePrice = BigDecimal.ZERO;
        }
        //社交分销-购物车-分销佣金2
        //分销佣金
        BigDecimal distributeCommission =
                response.getGoodsInfos().stream()
                        .filter(goodsInfo -> goodsInfoIds.contains(goodsInfo.getGoodsInfoId()) && Objects.equals(goodsInfo.getDistributionGoodsAudit(), DistributionGoodsAudit.CHECKED))
                        .map(goodsInfo -> goodsInfo.getDistributionCommission().multiply(BigDecimal.valueOf(goodsInfo.getBuyCount())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        response.setTotalPrice(totalPrice);
        response.setTradePrice(tradePrice);
        response.setDiscountPrice(discountTotalPrice);
        response.setDistributeCommission(distributeCommission);
        response.setTotalBuyPoint(sumBuyPoint);
        return response;
    }

    /**
     * 未登录时,验证并设置前端传入的商品使用营销信息
     *
     * @param response              采购单返回对象
     * @param goodsMarketingDTOList 前端传入的用户针对sku选择的营销活动信息
     */
    @Transactional(rollbackFor = Exception.class)
    public PurchaseResponse validateAndSetGoodsMarketings(PurchaseResponse response,
                                                          List<GoodsMarketingDTO> goodsMarketingDTOList) {
        Map<String, List<MarketingViewVO>> goodsMarketingMap = response.getGoodsMarketingMap();
        List<GoodsMarketingVO> goodsMarketingList = goodsMarketingDTOList.stream().map(dto ->
                GoodsMarketingVO.builder().goodsInfoId(dto.getGoodsInfoId()).marketingId(dto.getMarketingId()).build())
                .collect(Collectors.toList());

        if (MapUtils.isEmpty(goodsMarketingMap) && CollectionUtils.isNotEmpty(goodsMarketingList)) {
            goodsMarketingList = new ArrayList<>();
        } else if (MapUtils.isNotEmpty(goodsMarketingMap)) {
            // 过滤出仍然有效的商品营销信息
            goodsMarketingList = goodsMarketingList.stream()
                    .filter(goodsMarketing -> {
                        String goodsInfoId = goodsMarketing.getGoodsInfoId();
                        // 选择的营销存在 并且 采购单商品参与了该营销
                        return goodsMarketingMap.get(goodsInfoId) != null
                                && goodsMarketingMap.get(goodsInfoId).stream().anyMatch(marketingResponse ->
                                marketingResponse.getMarketingId().equals(goodsMarketing.getMarketingId()));
                    }).collect(Collectors.toList());
            Map<String, Long> oldMap = goodsMarketingList.stream()
                    .collect(Collectors.toMap(GoodsMarketingVO::getGoodsInfoId, GoodsMarketingVO::getMarketingId));

            // 过滤出用户未选择的商品营销活动信息
            List<GoodsMarketingVO> addList = goodsMarketingMap.entrySet().stream()
                    .filter(set -> oldMap.get(set.getKey()) == null)
                    .map(set -> GoodsMarketingVO.builder()
                            .goodsInfoId(set.getKey())
                            .marketingId(set.getValue().get(0).getMarketingId())
                            .build())
                    .collect(Collectors.toList());
            if (!addList.isEmpty()) {
                goodsMarketingList.addAll(addList);
            }
        }
        response.setGoodsMarketings(goodsMarketingList);
        return response;
    }

    /**
     * 同步商品使用的营销
     *
     * @param goodsMarketingMap
     * @param customerId
     */
    @Transactional(rollbackFor = Exception.class)
    public void syncGoodsMarketings(Map<String, List<MarketingViewVO>> goodsMarketingMap, String customerId) {
        List<GoodsMarketingVO> goodsMarketingList = this.queryGoodsMarketingList(customerId);

        if (goodsMarketingMap.isEmpty() && CollectionUtils.isNotEmpty(goodsMarketingList)) {
            goodsMarketingProvider.deleteByCustomerId(new GoodsMarketingDeleteByCustomerIdRequest(customerId));
        } else if (!goodsMarketingMap.isEmpty()) {
            Map<String, Long> oldMap = goodsMarketingList.stream()
                    .collect(Collectors.toMap(GoodsMarketingVO::getGoodsInfoId, GoodsMarketingVO::getMarketingId));

            List<String> delList = goodsMarketingList.stream()
                    .filter(goodsMarketing -> {
                        String goodsInfoId = goodsMarketing.getGoodsInfoId();

                        // 数据库里存的采购单商品没有参与营销或者选择的营销不存在了，则要删除该条记录
                        return goodsMarketingMap.get(goodsInfoId) == null
                                || !goodsMarketingMap.get(goodsInfoId).stream().anyMatch(marketingResponse ->
                                marketingResponse.getMarketingId().equals(goodsMarketing.getMarketingId()));
                    }).map(GoodsMarketingVO::getGoodsInfoId).collect(Collectors.toList());

            List<GoodsMarketingVO> addList = goodsMarketingMap.entrySet().stream()
                    .filter(set -> oldMap.get(set.getKey()) == null
                            || delList.contains(set.getKey()))
                    .map(set -> GoodsMarketingVO.builder()
                            .customerId(customerId)
                            .goodsInfoId(set.getKey())
                            .marketingId(set.getValue().get(0).getMarketingId())
                            .build())
                    .collect(Collectors.toList());

            // 先删除
            if (!delList.isEmpty()) {
                GoodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest goodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest
                        = new GoodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest();
                goodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest.setCustomerId(customerId);
                goodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest.setGoodsInfoIds(delList);
                goodsMarketingProvider.deleteByCustomerIdAndGoodsInfoIds(goodsMarketingDeleteByCustomerIdAndGoodsInfoIdsRequest);
            }

            // 再增加
            if (!addList.isEmpty()) {
                List<GoodsMarketingDTO> goodsMarketingDTOS = addList.stream().map(info -> {
                    GoodsMarketingDTO goodsMarketingDTO = new GoodsMarketingDTO();
                    goodsMarketingDTO.setCustomerId(info.getCustomerId());
                    goodsMarketingDTO.setGoodsInfoId(info.getGoodsInfoId());
                    goodsMarketingDTO.setMarketingId(info.getMarketingId());
                    goodsMarketingDTO.setId(info.getId());
                    return goodsMarketingDTO;
                }).collect(Collectors.toList());
                GoodsMarketingBatchAddRequest goodsMarketingBatchAddRequest = new GoodsMarketingBatchAddRequest();
                goodsMarketingBatchAddRequest.setGoodsMarketings(goodsMarketingDTOS);
                goodsMarketingProvider.batchAdd(goodsMarketingBatchAddRequest);
            }
        }
    }


    /**
     * 修改商品使用的营销
     *
     * @param goodsInfoId
     * @param marketingId
     * @param customer
     */
    @GlobalTransactional
    public void modifyGoodsMarketing(String goodsInfoId, Long marketingId, CustomerVO customer) {
//        GoodsInfoEditResponse response = goodsInfoService.findById(goodsInfoId);
        GoodsInfoViewByIdResponse response = goodsInfoQueryProvider.getViewById(
                GoodsInfoViewByIdRequest.builder().goodsInfoId(goodsInfoId).build()
        ).getContext();

        PurchaseGetGoodsMarketingResponse goodsMarketingMap =
                this.getGoodsMarketing(Arrays.asList(response.getGoodsInfo()), customer);

        List<MarketingViewVO> marketingResponses = goodsMarketingMap.getMap().get(goodsInfoId);

        if (marketingResponses != null && marketingResponses.stream().anyMatch(marketingResponse -> marketingResponse.getMarketingId().equals(marketingId))) {
            // 更新商品选择的营销
            GoodsMarketingModifyRequest goodsMarketingModifyRequest = new GoodsMarketingModifyRequest();
            goodsMarketingModifyRequest.setCustomerId(customer.getCustomerId());
            goodsMarketingModifyRequest.setGoodsInfoId(goodsInfoId);
            goodsMarketingModifyRequest.setMarketingId(marketingId);
            goodsMarketingProvider.modify(goodsMarketingModifyRequest);
        }
        //purchaseRepository.updateTime(LocalDateTime.now(), customer.getCustomerId(), goodsInfoId);
    }

    /**
     * 验证分销店铺商品
     *
     * @param goodsInfoVOList
     */
    public void verifyDistributorGoodsInfo(DistributeChannel channel, List<GoodsInfoVO> goodsInfoVOList) {

        // 如果是小店请求，验证商品是否是小店商品
        if (channel.getChannelType() == ChannelType.SHOP) {
            List<String> skuIdList =
                    goodsInfoVOList.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
            //验证是否为分销商品
            List<String> invalidDistributeIds =
                    goodsInfoVOList.stream().filter(goodsInfo -> !Objects.equals(goodsInfo.getDistributionGoodsAudit(), DistributionGoodsAudit.CHECKED)).map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
            DistributorGoodsInfoVerifyRequest verifyRequest = new DistributorGoodsInfoVerifyRequest();
            verifyRequest.setGoodsInfoIds(skuIdList);
            verifyRequest.setDistributorId(channel.getInviteeId());
            //验证是否在店铺精选范围内
            List<String> invalidIds = distributorGoodsInfoQueryProvider
                    .verifyDistributorGoodsInfo(verifyRequest).getContext().getInvalidIds();
            if (CollectionUtils.isNotEmpty(invalidDistributeIds)) {
                if (CollectionUtils.isEmpty(invalidIds)) {
                    invalidIds = invalidDistributeIds;
                } else {
                    invalidIds.addAll(invalidDistributeIds);
                }
            }
            if (CollectionUtils.isNotEmpty(invalidIds)) {
                List<String> ids = invalidIds;
                //叠加分销状态，并设为无效
                goodsInfoVOList.stream().filter(goodsInfo -> Objects.equals(GoodsStatus.OK,
                        goodsInfo.getGoodsStatus())).forEach(goodsInfo -> {
                    if (ids.contains(goodsInfo.getGoodsInfoId())) {
                        goodsInfo.setGoodsStatus(GoodsStatus.INVALID);
                    }
                });
            }
        }
    }

    /**
     * 查询购物车信息
     */
    public PurchaseListResponse purchaseInfo(PurchaseInfoRequest request) {
        PurchaseListResponse response = new PurchaseListResponse();
        CustomerVO customer = request.getCustomer();
//        //查询是否购买付费会员卡
//        PaidCardVO paidCardVO = new PaidCardVO();
//        if(Objects.nonNull(customer)) {
//            List<PaidCardCustomerRelVO> paidCardCustomerRelVOList = paidCardCustomerRelQueryProvider
//                    .listCustomerRelFullInfo(PaidCardCustomerRelListRequest.builder()
//                            .customerId(customer.getCustomerId())
//                            .delFlag(DeleteFlag.NO)
//                            .endTimeFlag(LocalDateTime.now())
//                            .build())
//                    .getContext();
//
//            if (CollectionUtils.isNotEmpty(paidCardCustomerRelVOList)) {
//                paidCardVO = paidCardCustomerRelVOList.stream()
//                        .map(PaidCardCustomerRelVO::getPaidCardVO)
//                        .min(Comparator.comparing(PaidCardVO::getDiscountRate)).get();
//            }
//        }

        List<String> goodsInfoIds = new ArrayList<>();
        Map<String, Long> buyCountMap = new HashMap<>();
        // 1.登录的情况，查询采购单信息
        List<PurchaseVO> purchaseList = null;
        boolean productMoreThanOne = false;
        if (Objects.nonNull(customer)) {
            purchaseList = KsBeanUtil.convertList(this.queryPurchase(customer.getCustomerId(), null, request.getInviteeId()), PurchaseVO.class);
            if(purchaseList.size() > 1){
                productMoreThanOne = true;
            }
            for (PurchaseVO purchaseVO : purchaseList) {
                goodsInfoIds.add(purchaseVO.getGoodsInfoId());
                buyCountMap.put(purchaseVO.getGoodsInfoId(), purchaseVO.getGoodsNum());
                if(purchaseVO.getGoodsNum() > 1){
                    productMoreThanOne = true;
                }
            }
            request.setGoodsInfoIds(goodsInfoIds);

            if (CollectionUtils.isEmpty(request.getGoodsInfoIds())) {
                return response;
            }
        } else {
            if (CollectionUtils.isEmpty(request.getGoodsInfoIds())) {
                return response;
            }
            goodsInfoIds.addAll(request.getGoodsInfoIds());
        }

        // 2.查询商品、店铺、营销相关信息
        GoodsInfoForPurchaseResponse goodsResp = goodsCommonQueryProvider.queryInfoForPurchase(
                InfoForPurchaseRequest.builder().goodsInfoIds(request.getGoodsInfoIds()).customer(customer).areaId(request.getAreaId()).build()).getContext();

        //小程序购物车处理
        filterShopCartGoods(request.getShopCartSource(), goodsResp, buyCountMap);

        if (CollectionUtils.isEmpty(goodsResp.getGoodsList())) return response;
        List<GoodsInfoVO> goodsInfoList = goodsResp.getGoodsInfoList();

        if (customer != null && StringUtils.isNotBlank(customer.getCustomerId()) && EnterpriseCheckState.CHECKED.equals(customer.getEnterpriseCheckState())) {
            //企业价
            EnterprisePriceGetRequest enterprisePriceGetRequest = new EnterprisePriceGetRequest();
            enterprisePriceGetRequest.setGoodsInfoIds(goodsInfoIds);
            enterprisePriceGetRequest.setCustomerId(customer.getCustomerId());
            enterprisePriceGetRequest.setBuyCountMap(buyCountMap);
            enterprisePriceGetRequest.setListFlag(false);
            enterprisePriceGetRequest.setOrderFlag(false);

            EnterprisePriceResponse enterprisePriceResponse = enterpriseGoodsInfoQueryProvider.userPrice(enterprisePriceGetRequest).getContext();

            goodsInfoList.forEach(e -> {
                BigDecimal price = enterprisePriceResponse.getPriceMap().get(e.getGoodsInfoId());
                if (price != null) {
                    e.setEnterPrisePrice(price);
                    List<GoodsIntervalPriceVO> list = enterprisePriceResponse.getIntervalMap().get(e.getGoodsInfoId());
                    if (list != null && !list.isEmpty()) {
                        e.setIntervalPriceList(list);
                    }
                }
            });
        }
        goodsInfoList = goodsInfoList.stream().sorted(Comparator.comparingInt(a -> goodsInfoIds.indexOf(a.getGoodsInfoId()))).collect(Collectors.toList());
        List<String> goodsIds = goodsInfoList.stream().map(GoodsInfoVO::getGoodsId).distinct().collect(Collectors.toList());
        List<GoodsVO> goodsList = goodsResp.getGoodsList();
        goodsList = goodsList.stream().sorted(Comparator.comparingInt(a -> goodsIds.indexOf(a.getGoodsId()))).collect(Collectors.toList());

        List<Long> companyIds = goodsList.stream().map(GoodsVO::getCompanyInfoId).distinct().collect(Collectors.toList());
        List<Long> storeIds = goodsList.stream().map(GoodsVO::getStoreId).distinct().collect(Collectors.toList());
        ListCompanyStoreByCompanyIdsResponse storeResp = tradeCacheService.listCompanyStoreByCompanyIds(companyIds, storeIds);
        List<MiniStoreVO> storeVOList = storeResp.getStoreVOList();
        storeVOList = storeVOList.stream().sorted(Comparator.comparingInt(a -> storeIds.indexOf(a.getStoreId()))).collect(Collectors.toList());
        List<MiniCompanyInfoVO> companyInfoVOList = storeResp.getCompanyInfoVOList();
        companyInfoVOList = companyInfoVOList.stream().sorted(Comparator.comparingInt(a -> companyIds.indexOf(a.getCompanyInfoId()))).collect(Collectors.toList());

        List<GoodsInfoMarketingVO> marketingInfos = goodsInfoList.stream().map(i ->
                GoodsInfoMarketingVO.builder().storeId(i.getStoreId()).goodsInfoId(i.getGoodsInfoId()).distributionGoodsAudit(i.getDistributionGoodsAudit()).build()
        ).collect(Collectors.toList());
        MarketInfoForPurchaseResponse marketResp = marketingCommonQueryProvider.queryInfoForPurchase(
                new InfoForPurchseRequest(marketingInfos, customer, goodsResp.getLevelsMap())).getContext();

//        Long currentPoint = -1L;
//        List<GoodsInfoMarketingVO> goodsMarketings = marketResp.getGoodsInfos();
//        for (GoodsInfoMarketingVO goodsMarketing : goodsMarketings) {
//            // 用户积分不足、商品大于一件、未登录的情况，去掉积分换购活动
//            List<MarketingViewVO> marketingViewList = goodsMarketing.getMarketingViewList();
//            if(CollectionUtils.isNotEmpty(marketingViewList)){
//                Iterator<MarketingViewVO> it = marketingViewList.iterator();
//                while (it.hasNext()) {
//                    MarketingViewVO marketingViewVO = it.next();
//                    if(MarketingSubType.POINT_BUY.equals(marketingViewVO.getSubType())){
//                        if(customer == null || productMoreThanOne){
//                            it.remove();
//                            continue;
//                        }
//                        if(currentPoint == -1){
//                            currentPoint = externalProvider.getByUserNoPoint(FanDengPointRequest.builder().userNo(customer.getFanDengUserNo()).build()).getContext().getCurrentPoint();
//                        }
//                        if(currentPoint == null || currentPoint < marketingViewVO.getPointBuyLevelList().get(0).getPointNeed()){
//                            it.remove();
//                        }
//                    }
//                }
//            }
//        }

        // 营销优先级过滤
        boolean isGoodsPoint = systemPointsConfigService.isGoodsPoint();
        this.getGoodsLevelPrices(response, customer, goodsInfoList);
//        List<String> unVipPriceBlackList = new ArrayList<>();
//        if (Objects.nonNull(paidCardVO.getDiscountRate())) {
//            //获取黑名单
//            GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest = new GoodsBlackListPageProviderRequest();
//            goodsBlackListPageProviderRequest.setBusinessCategoryColl(
//                    Collections.singletonList(GoodsBlackListCategoryEnum.UN_SHOW_VIP_PRICE.getCode()));
//            BaseResponse<GoodsBlackListPageProviderResponse> goodsBlackListPageProviderResponseBaseResponse = goodsBlackListProvider.listNoPage(goodsBlackListPageProviderRequest);
//            GoodsBlackListPageProviderResponse context = goodsBlackListPageProviderResponseBaseResponse.getContext();
//            if (context.getUnVipPriceBlackListModel() != null && !CollectionUtils.isEmpty(context.getUnVipPriceBlackListModel().getGoodsIdList())) {
//                unVipPriceBlackList.addAll(context.getUnVipPriceBlackListModel().getGoodsIdList());
//            }
//        }

        for (GoodsInfoVO goodsInfo : goodsInfoList) {
            //获取付费会员价
//            logger.info("PurchaseService  goodsId:{} 黑名单为：{}", goodsInfo.getGoodsId(), JSON.toJSONString(unVipPriceBlackList));
//            if (Objects.nonNull(paidCardVO.getDiscountRate()) && !unVipPriceBlackList.contains(goodsInfo.getGoodsId())) {
//                goodsInfo.setSalePrice(goodsInfo.getMarketPrice().multiply(paidCardVO.getDiscountRate()).setScale(2,BigDecimal.ROUND_HALF_UP));;
//            }
            // 是否积分价
            if (!isGoodsPoint) goodsInfo.setBuyPoint(null);
            boolean pointFlag = Objects.nonNull(goodsInfo.getBuyPoint()) && goodsInfo.getBuyPoint() > 0L;
            // 是否预约商品
            boolean appointmentFlag = Objects.nonNull(goodsInfo.getAppointmentSaleVO());
            // 是否预售商品
            LocalDateTime now = LocalDateTime.now();
            BookingSaleVO bookingSaleVO = goodsInfo.getBookingSaleVO();
            boolean bookingFlag = Objects.nonNull(goodsInfo.getBookingSaleVO())
                    && !(BookingType.EARNEST_MONEY.toValue() == bookingSaleVO.getBookingType()
                    && (now.isBefore(bookingSaleVO.getHandSelStartTime()) || now.isAfter(bookingSaleVO.getHandSelEndTime())));
            //是否分销商品
            boolean disFlag = DistributionGoodsAudit.CHECKED.equals(goodsInfo.getDistributionGoodsAudit());
            // 是否企业价商品
            boolean enterpriseFlag = customer != null
                    && EnterpriseCheckState.CHECKED.equals(customer.getEnterpriseCheckState())
                    && EnterpriseAuditState.CHECKED.equals(goodsInfo.getEnterPriseAuditState());

            if (pointFlag) {
                // 积分价商品，与预约、预售、分销、营销互斥，与设价方式叠加
                this.clearBooking(goodsInfo);
                this.clearAppointment(goodsInfo);
                this.clearDistribution(marketResp.getGoodsInfos(), goodsInfo);
                this.clearMarketings(marketResp.getGoodsInfos(), goodsInfo);
                if (goodsInfo.getPriceType() != null && goodsInfo.getPriceType() != GoodsPriceType.CUSTOMER.toValue()) {
                    goodsInfo.setSalePrice(goodsInfo.getMarketPrice());
                }
            } else if (appointmentFlag) {
                // 预约商品，与预售、分销、营销、设价方式互斥
                this.clearBooking(goodsInfo);
                this.clearDistribution(marketResp.getGoodsInfos(), goodsInfo);
                this.clearMarketings(marketResp.getGoodsInfos(), goodsInfo);
                this.clearPriceType(goodsInfo);
                AppointmentSaleGoodsVO appointmentSaleGood = goodsInfo.getAppointmentSaleVO().getAppointmentSaleGood();
                if (Objects.nonNull(appointmentSaleGood)) {
                    goodsInfo.setSalePrice(appointmentSaleGood.getPrice());
                }
            } else if (bookingFlag) {
                // 预售商品，与分销、营销、设价方式互斥
                BookingSaleGoodsVO bookingSaleGoods = goodsInfo.getBookingSaleVO().getBookingSaleGoods();
                this.clearMarketings(marketResp.getGoodsInfos(), goodsInfo);
                this.clearDistribution(marketResp.getGoodsInfos(), goodsInfo);
                this.clearPriceType(goodsInfo);
                if (Objects.nonNull(bookingSaleGoods) && BookingType.FULL_MONEY.toValue() == bookingSaleVO.getBookingType()) {
                    goodsInfo.setSalePrice(bookingSaleGoods.getBookingPrice());
                }
            } else if (disFlag) {
                // 分销商品，与营销、设价方式互斥
                this.clearMarketings(marketResp.getGoodsInfos(), goodsInfo);
                this.clearPriceType(goodsInfo);
                goodsInfo.setSalePrice(goodsInfo.getMarketPrice());
            } else if (enterpriseFlag) {
                // 企业购商品，与设价方式互斥
                this.clearPriceType(goodsInfo);
                //判断当前用户对应企业购商品等级企业价
                List<GoodsLevelPriceVO> goodsLevelPrices = response.getGoodsLevelPrices();
                if (CollectionUtils.isNotEmpty(goodsLevelPrices)) {
                    Optional<GoodsLevelPriceVO> first = goodsLevelPrices.stream()
                            .filter(goodsLevelPrice -> goodsLevelPrice.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId()))
                            .findFirst();
                    goodsInfo.setSalePrice(first.isPresent() ? first.get().getPrice() : goodsInfo.getEnterPrisePrice());
                } else {
                    goodsInfo.setSalePrice(goodsInfo.getEnterPrisePrice());
                }
            }
        }

        // 3.合并信息至response
        response.setStores(storeVOList);
        response.setCompanyInfos(companyInfoVOList);
        response.setGoodses(goodsList);
        response.setGoodsInfos(goodsInfoList);
        response.setGoodsIntervalPrices(new ArrayList<>());
        response.setAppointmentSaleVOList(new ArrayList<>());
        response.setBookingSaleVOList(new ArrayList<>());
        response.setGoodsMarketings(new ArrayList<>());
        response.setGoodsMarketingMap(new HashMap<>());

        // 设置冗余的store.goodsIds、goods.goodsInfoIds
        response.getStores().stream().forEach(store ->
                store.setGoodsIds(response.getGoodses().stream().filter(i -> store.getStoreId().equals(i.getStoreId())).map(i -> i.getGoodsId()).collect(Collectors.toList()))
        );
        response.getGoodses().stream().forEach(goods ->
                goods.setGoodsInfoIds(response.getGoodsInfos().stream().filter(i -> goods.getGoodsId().equals(i.getGoodsId())).map(i -> i.getGoodsInfoId()).collect(Collectors.toList()))
        );

        int size = goodsInfoList.size();
        for (int idx = 0; idx < size; idx++) {
            GoodsInfoVO goodsInfo = response.getGoodsInfos().get(idx);
            GoodsVO goods = response.getGoodses().stream().filter(i -> i.getGoodsId().equals(goodsInfo.getGoodsId())).findFirst().orElse(null);
            GoodsInfoMarketingVO marketInfo = marketResp.getGoodsInfos().stream().filter(i -> i.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).findFirst().orElse(null);
            if (Objects.nonNull(goods) && Objects.nonNull(marketInfo)) {

                // 分销
                goodsInfo.setDistributionGoodsAudit(marketInfo.getDistributionGoodsAudit());
                if (DistributionGoodsAudit.CHECKED.equals(goodsInfo.getDistributionGoodsAudit())) {
                    goodsInfo.setDistributionCommission(goodsInfo.getDistributionCommission().multiply(marketResp.getCommissionRate()));
                    goodsInfo.setSalePrice(goodsInfo.getMarketPrice());
                }

                // 订货量设价
                response.getGoodsIntervalPrices().addAll(goodsInfo.getIntervalPriceList());

                // 预售活动
                if (Objects.nonNull(goodsInfo.getBookingSaleVO())) {
                    response.getBookingSaleVOList().add(goodsInfo.getBookingSaleVO());
                    goodsInfo.setBookingSaleVO(null);
                }

                // 预约活动
                if (Objects.nonNull(goodsInfo.getAppointmentSaleVO())) {
                    response.getAppointmentSaleVOList().add(goodsInfo.getAppointmentSaleVO());
                    goodsInfo.setAppointmentSaleVO(null);
                }

                // 营销 (减、折、赠、打包一口价、第二件半价）
                if (Objects.nonNull(goodsInfo.getGoodsMarketing())) {
                    // 如果营销失效，置空
                    List<Long> marketingIds = new ArrayList<>();
                    if (CollectionUtils.isNotEmpty(marketInfo.getMarketingViewList())) {
                        marketingIds = marketInfo.getMarketingViewList().stream().map(i -> i.getMarketingId()).collect(Collectors.toList());
                    }
                    if (!marketingIds.contains(goodsInfo.getGoodsMarketing().getMarketingId())) {
                        goodsInfo.setGoodsMarketing(null);
                    }
                }
                if (CollectionUtils.isNotEmpty(marketInfo.getMarketingViewList())) {
                    marketInfo.setMarketingViewList(marketInfo.getMarketingViewList().stream().filter(m -> !MarketingType.MARKUP.equals(m.getMarketingType()))
                            .collect(Collectors.toList()));
                }
                if (CollectionUtils.isNotEmpty(marketInfo.getMarketingViewList())) {
                    marketInfo.getMarketingViewList().sort(Comparator.comparingInt(a -> a.getMarketingType().toValue()));
                    // goodsMarketings
                    if (Objects.nonNull(goodsInfo.getGoodsMarketing())) {
                        response.getGoodsMarketings().add(goodsInfo.getGoodsMarketing());
                    } else {
                        response.getGoodsMarketings().add(
                                GoodsMarketingVO.builder().goodsInfoId(marketInfo.getGoodsInfoId()).marketingId(marketInfo.getMarketingViewList().get(0).getMarketingId()).build()
                        );
                        if (Objects.nonNull(customer) && goodsInfo.getDelFlag() != DeleteFlag.YES) {
                            this.modifyGoodsMarketing(marketInfo.getGoodsInfoId(), marketInfo.getMarketingViewList().get(0).getMarketingId(), customer);
                        }
                    }
                    // goodsMarketingMap
                    response.getGoodsMarketingMap().put(marketInfo.getGoodsInfoId(), marketInfo.getMarketingViewList());
                }

                goodsInfo.setPriceType(goods.getPriceType());

                // 回填购买数量
                if (Objects.nonNull(purchaseList)) {
                    PurchaseVO match = purchaseList.stream()
                            .filter(i -> i.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).findFirst().get();
                    goodsInfo.setBuyCount(match.getGoodsNum());
                }
            }

        }

        if (Objects.nonNull(customer)) response.setPointsAvailable(customer.getPointsAvailable());

        return response;
    }

    private void filterShopCartGoods(ShopCartSourceEnum shopCartSource, GoodsInfoForPurchaseResponse goodsResp, Map<String, Long> buyCountMap) {
        String channel = ShopCartSourceEnum.WX_MINI.equals(shopCartSource) ? GoodsChannelTypeEnum.MALL_MINI.getCode() : GoodsChannelTypeEnum.MALL_H5.getCode();
        Iterator<GoodsVO> spuIter = goodsResp.getGoodsList().iterator();
        while (spuIter.hasNext()) {
            GoodsVO spu = spuIter.next();
            if (CollectionUtils.isEmpty(spu.getGoodsChannelTypeSet()) || !spu.getGoodsChannelTypeSet().contains(channel)) {
                spuIter.remove();
            }
        }
        Iterator<GoodsInfoVO> skuIter = goodsResp.getGoodsInfoList().iterator();
        while (skuIter.hasNext()) {
            GoodsInfoVO sku = skuIter.next();
            if (CollectionUtils.isEmpty(sku.getGoodsChannelTypeSet()) || !sku.getGoodsChannelTypeSet().contains(channel)) {
                skuIter.remove();
                buyCountMap.remove(sku.getGoodsInfoId());
            }
        }
    }

    private List<GoodsLevelPriceVO> getGoodsLevelPrices(PurchaseListResponse response, CustomerVO customer, List<GoodsInfoVO> goodsInfoList) {
        if (CollectionUtils.isEmpty(goodsInfoList)) {
            return Collections.emptyList();
        }
        //等级价格
        List<GoodsLevelPriceVO> goodsLevelPriceList = new ArrayList<>();
        if (Objects.nonNull(customer) && EnterpriseCheckState.CHECKED.equals(customer.getEnterpriseCheckState())) {
            List<String> skuIds = goodsInfoList.stream()
                    .map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
            GoodsLevelPriceBySkuIdsRequest goodsLevelPriceBySkuIdsRequest = new GoodsLevelPriceBySkuIdsRequest();
            goodsLevelPriceBySkuIdsRequest.setSkuIds(skuIds);
            goodsLevelPriceBySkuIdsRequest.setType(PriceType.ENTERPRISE_SKU);
            goodsLevelPriceList = goodsLevelPriceQueryProvider
                    .listBySkuIds(goodsLevelPriceBySkuIdsRequest).getContext().getGoodsLevelPriceList();
            if (CollectionUtils.isNotEmpty(goodsLevelPriceList)) {
                goodsLevelPriceList = goodsLevelPriceList.stream()
                        .filter(goodsLevelPrice -> goodsLevelPrice.getLevelId().equals(customer.getCustomerLevelId()))
                        .collect(Collectors.toList());
            }
            if (Objects.nonNull(response)) {
                response.setGoodsLevelPrices(goodsLevelPriceList);
            }
        }
        return goodsLevelPriceList;
    }

    private void clearPriceType(GoodsInfoVO goodsInfo) {
        goodsInfo.setIntervalPriceIds(new ArrayList<>());
        goodsInfo.setIntervalMinPrice(null);
        goodsInfo.setIntervalMaxPrice(null);
        goodsInfo.setPriceType(GoodsPriceType.MARKET.toValue());
        goodsInfo.setSalePrice(goodsInfo.getMarketPrice());
    }

    private void clearBooking(GoodsInfoVO goodsInfo) {
        goodsInfo.setBookingPrice(null);
        goodsInfo.setBookingSaleVO(null);
    }

    private void clearAppointment(GoodsInfoVO goodsInfo) {
        goodsInfo.setAppointmentPrice(null);
        goodsInfo.setAppointmentSaleVO(null);
    }

    private void clearDistribution(List<GoodsInfoMarketingVO> goodsInfos, GoodsInfoVO goodsInfo) {
        GoodsInfoMarketingVO marketInfo = goodsInfos.stream().filter(i -> i.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).findFirst().orElse(null);
        if (Objects.nonNull(marketInfo)) {
            marketInfo.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
        }
        goodsInfo.setDistributionGoodsAudit(DistributionGoodsAudit.COMMON_GOODS);
    }

    private void clearMarketings(List<GoodsInfoMarketingVO> goodsInfos, GoodsInfoVO goodsInfo) {
        GoodsInfoMarketingVO marketInfo = goodsInfos.stream().filter(i -> i.getGoodsInfoId().equals(goodsInfo.getGoodsInfoId())).findFirst().orElse(null);
        if (Objects.nonNull(marketInfo)) {
            marketInfo.setMarketingViewList(new ArrayList<>());
        }
        goodsInfo.setGoodsMarketing(null);
    }

}
