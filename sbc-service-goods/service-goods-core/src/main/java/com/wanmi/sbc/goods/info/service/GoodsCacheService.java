package com.wanmi.sbc.goods.info.service;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelMapByCustomerIdAndStoreIdsRequest;
import com.wanmi.sbc.customer.bean.vo.CommonLevelVO;
import com.wanmi.sbc.goods.appointmentsale.model.root.AppointmentSaleDO;
import com.wanmi.sbc.goods.appointmentsale.service.AppointmentSaleService;
import com.wanmi.sbc.goods.bean.enums.PriceType;
import com.wanmi.sbc.goods.bean.enums.SaleType;
import com.wanmi.sbc.goods.bean.vo.*;
import com.wanmi.sbc.goods.bookingsale.model.root.BookingSale;
import com.wanmi.sbc.goods.bookingsale.service.BookingSaleService;
import com.wanmi.sbc.goods.cache.WmCacheConfig;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsQueryRequest;
import com.wanmi.sbc.goods.price.model.root.GoodsIntervalPrice;
import com.wanmi.sbc.goods.price.model.root.GoodsLevelPrice;
import com.wanmi.sbc.goods.price.repository.GoodsIntervalPriceRepository;
import com.wanmi.sbc.goods.price.repository.GoodsLevelPriceRepository;
import com.wanmi.sbc.goods.spec.model.root.GoodsInfoSpecDetailRel;
import com.wanmi.sbc.goods.spec.repository.GoodsInfoSpecDetailRelRepository;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsCacheService {

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private GoodsInfoSpecDetailRelRepository goodsInfoSpecDetailRelRepository;

    @Autowired
    private GoodsLevelPriceRepository goodsLevelPriceRepository;

    @Autowired
    private GoodsIntervalPriceRepository goodsIntervalPriceRepository;

    @Autowired
    private BookingSaleService bookingSaleService;

    @Autowired
    private AppointmentSaleService appointmentSaleService;

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private CustomerLevelQueryProvider customerLevelQueryProvider;

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;

//    @ListCache(prefix = CacheKeyConstant.PURCHASE_GOODS, clazz = GoodsVO.class, seconds = 60 * 5)
    public List<GoodsVO> listGoodsByIds(List<String> goodsIds) {
        List<GoodsVO> goodsList = KsBeanUtil.convertList(goodsRepository.findAll(
                GoodsQueryRequest.builder().goodsIds(goodsIds).delFlag(DeleteFlag.NO.toValue()).build().getWhereCriteria()), GoodsVO.class);
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = goodsList.stream()
                .filter(v -> ThirdPlatformType.LINKED_MALL.equals(v.getThirdPlatformType()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .collect(Collectors.toList());
        if (itemIds.size() > 0) {
            List<QueryItemInventoryResponse.Item> stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
            if (stocks != null) {
                for (GoodsVO goodsVO : goodsList) {
                    if (ThirdPlatformType.LINKED_MALL.equals(goodsVO.getThirdPlatformType())) {
                        Optional<QueryItemInventoryResponse.Item> optional = stocks.stream().filter(v -> String.valueOf(v.getItemId()).equals(goodsVO.getThirdPlatformSpuId())).findFirst();
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
        goodsList.forEach(goods -> {
            goods.setGoodsDetail(null);
        });
        return goodsList;
    }

//    @ListCache(prefix = CacheKeyConstant.PURCHASE_GOODS_INFO, clazz = GoodsInfoVO.class, seconds = 60 * 5)
    public List<GoodsInfoVO> listGoodsInfosByIds(List<String> goodsInfoIds, PriceType type) {
        // 单品查询
        List<GoodsInfo> goodsInfoList = goodsInfoRepository.findAll(
                GoodsInfoQueryRequest.builder().goodsInfoIds(goodsInfoIds).build().getWhereCriteria());
        goodsInfoService.fillGoodsStatus(goodsInfoList);
        List<GoodsInfoVO> goodsInfos = KsBeanUtil.convertList(goodsInfoList, GoodsInfoVO.class);

        List<String> ids = goodsInfoList.stream().filter(goodsInfo -> Objects.nonNull(goodsInfo.getSaleType()) && goodsInfo.getSaleType() == SaleType.WHOLESALE.toValue())
                .map(GoodsInfo::getGoodsInfoId).collect(Collectors.toList());

        // 单品关联的规格值、级别价、区间价、预售活动、预约活动
        List<GoodsInfoSpecDetailRel> relList = goodsInfoSpecDetailRelRepository.findByAllGoodsInfoIds(goodsInfoIds);
        List<GoodsLevelPrice> goodsLevelPrices = goodsLevelPriceRepository.findSkuByGoodsInfoIdsAndType(goodsInfoIds, type);
        List<GoodsIntervalPrice> intervalPrices = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(ids)){
            intervalPrices = goodsIntervalPriceRepository.findSkuByGoodsInfoIds(goodsInfoIds);
        }
        List<BookingSale> bookingSales = bookingSaleService.inProgressBookingSaleInfoByGoodsInfoIdList(goodsInfoIds);
        List<AppointmentSaleDO> appointmentSaleDOS = appointmentSaleService.inProgressAppointmentSaleInfoByGoodsInfoIdList(goodsInfoIds);

        // 组合信息
        List<GoodsIntervalPrice> finalIntervalPrices = intervalPrices;
        goodsInfos.forEach(goodsInfo -> {
            goodsInfo.setGoodsInfoSpecDetailRelList(KsBeanUtil.convertList(
                    relList.stream().filter(i -> goodsInfo.getGoodsInfoId().equals(i.getGoodsInfoId())).collect(Collectors.toList()), GoodsInfoSpecDetailRelVO.class));

            goodsInfo.setSpecText(StringUtils.join(goodsInfo.getGoodsInfoSpecDetailRelList().stream()
                    .map(GoodsInfoSpecDetailRelVO::getDetailName)
                    .collect(Collectors.toList()), " "));

            goodsInfo.setGoodsLevelPriceList(KsBeanUtil.convertList(
                    goodsLevelPrices.stream().filter(i -> goodsInfo.getGoodsInfoId().equals(i.getGoodsInfoId())).collect(Collectors.toList()), GoodsLevelPriceVO.class));
            goodsInfo.setIntervalPriceList(KsBeanUtil.convertList(
                    finalIntervalPrices.stream().filter(i -> goodsInfo.getGoodsInfoId().equals(i.getGoodsInfoId())).collect(Collectors.toList()), GoodsIntervalPriceVO.class));

            goodsInfo.setBookingSaleVO(KsBeanUtil.convert(
                    bookingSales.stream().filter(i -> goodsInfo.getGoodsInfoId().equals(i.getBookingSaleGoods().getGoodsInfoId())).findFirst().orElse(null), BookingSaleVO.class));

            goodsInfo.setAppointmentSaleVO(KsBeanUtil.convert(
                    appointmentSaleDOS.stream().filter(i -> goodsInfo.getGoodsInfoId().equals(i.getAppointmentSaleGood().getGoodsInfoId())).findFirst().orElse(null), AppointmentSaleVO.class));

        });
        return goodsInfos;
    }


    @Cacheable(value = WmCacheConfig.GOODS,keyGenerator = WmCacheConfig.DEFAULT_KEY_GENERATOR)
    public HashMap<Long, CommonLevelVO> listCustomerLevelMapByCustomerIdAndIds(String customerId, List<Long> storeIds) {
        return customerLevelQueryProvider.listCustomerLevelMapByCustomerIdAndIds(
                new CustomerLevelMapByCustomerIdAndStoreIdsRequest(customerId, storeIds)
        ).getContext().getCommonLevelVOMap();
    }
}
