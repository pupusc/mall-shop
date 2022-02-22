package com.soybean.mall.order.controller;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.store.ListNoDeleteStoreByIdsRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.enterprise.goods.EnterprisePriceGetRequest;
import com.wanmi.sbc.goods.api.request.flashsalegoods.FlashSaleGoodsListRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.enterprise.EnterprisePriceResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.api.response.price.GoodsIntervalPriceByCustomerIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.DistributionGoodsAudit;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsLevelPriceVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.intervalprice.GoodsIntervalPriceService;
import com.wanmi.sbc.marketing.api.provider.markup.MarkupQueryProvider;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeListForUseByCustomerIdRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupListRequest;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.marketing.bean.dto.TradeItemInfoDTO;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelVO;
import com.wanmi.sbc.marketing.bean.vo.TradeMarketingVO;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.VerifyQueryProvider;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.TradeGetGoodsResponse;
import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.dto.TradeGoodsListDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemGroupDTO;
import com.wanmi.sbc.order.bean.enums.BookingType;
import com.wanmi.sbc.order.bean.vo.*;
import com.wanmi.sbc.order.request.TradeItemConfirmRequest;
import com.wanmi.sbc.order.response.TradeConfirmResponse;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private MarketingLevelPluginProvider marketingLevelPluginProvider;

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private VerifyQueryProvider verifyQueryProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;
    /**
     * 用于确认订单后，创建订单前的获取订单商品信息
     */
    @ApiOperation(value = "用于确认订单后，创建订单前的获取订单商品信息")
    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
    @GlobalTransactional
    public BaseResponse<TradeConfirmResponse> getPurchaseItems(@RequestBody TradeItemConfirmRequest request) {

        TradeConfirmResponse confirmResponse = new TradeConfirmResponse();
        String customerId = commonUtil.getOperatorId();
        //验证用户
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (customerId)).getContext();
        //入参是商品sku和num，返回商品信息和价格信息
        List<TradeItemDTO> tradeItems = KsBeanUtil.convertList(request.getTradeItems(),TradeItemDTO.class);
        List<String> skuIds = tradeItems.stream().map(TradeItemDTO::getSkuId).collect(Collectors.toList());

        //获取订单商品详情和会员价salePrice
        GoodsInfoResponse skuResp = getGoodsResponse(skuIds, customer);
        List<TradeConfirmItemVO> items= new ArrayList<>(1);
        //一期只能购买一个商品，只有一个商家
        Map<Long, StoreVO> storeMap = storeQueryProvider.listNoDeleteStoreByIds(new ListNoDeleteStoreByIdsRequest
                (skuResp.getGoodsInfos().stream().map(g -> g.getStoreId()).collect(Collectors.toList()))).getContext().getStoreVOList().stream().
                        collect(Collectors.toMap(StoreVO::getStoreId, s -> s));

        TradeConfirmItemVO tradeConfirmItemVO = new TradeConfirmItemVO();
        List<TradeItemVO> tradeItemVOS = new ArrayList<>();
        TradePriceVO tradePrice = new TradePriceVO();
        //填充商品信息和价格
        tradeItems.forEach(tradeItemDTO -> {
            Optional<GoodsInfoVO> optionalGoodsInfoVO = skuResp.getGoodsInfos().stream().filter(p->p.getGoodsInfoId().equals(tradeItemDTO.getSkuId())).findFirst();
            if(optionalGoodsInfoVO.isPresent()){
                TradeItemVO tradeItemVO = KsBeanUtil.convert(optionalGoodsInfoVO.get(),TradeItemVO.class);
                tradeItemVO.setNum(tradeItemDTO.getNum());
                tradeItemVOS.add(tradeItemVO);
            }
        });
        tradeConfirmItemVO.setTradeItems(tradeItemVOS);

        //设置购买总积分
        confirmResponse.setTotalBuyPoint(items.stream().flatMap(i -> i.getTradeItems().stream())
                .filter(i -> Objects.isNull(i.getIsMarkupGoods()) || !i.getIsMarkupGoods()).mapToLong(v -> Objects.isNull(v.getBuyPoint()) ? 0 : v.getBuyPoint() * v.getNum()).sum());
        confirmResponse.setTradeConfirmItems(items);
        return BaseResponse.success(confirmResponse);
    }

    /**
     * 获取订单商品详情,会员价
     */
    private GoodsInfoResponse getGoodsResponse(List<String> skuIds, CustomerVO customer) {
        //性能优化，原来从order服务绕道，现在直接从goods服务直行
        GoodsInfoViewByIdsRequest goodsInfoRequest = GoodsInfoViewByIdsRequest.builder()
                .goodsInfoIds(skuIds)
                .isHavSpecText(Constants.yes)
                .build();
        GoodsInfoViewByIdsResponse response = goodsInfoQueryProvider.listViewByIds(goodsInfoRequest).getContext();

        //获取客户的等级
        if (StringUtils.isNotBlank(customer.getCustomerId())) {
            //计算会员价
            List<GoodsInfoVO> goodsInfoVOList = marketingLevelPluginProvider.goodsListFilter(MarketingLevelGoodsListFilterRequest.builder()
                    .goodsInfos(KsBeanUtil.convertList(response.getGoodsInfos(), GoodsInfoDTO.class))
                    .customerDTO(KsBeanUtil.convert(customer, CustomerDTO.class)).build())
                    .getContext().getGoodsInfoVOList();
            response.setGoodsInfos(goodsInfoVOList);
        }
        return GoodsInfoResponse.builder().goodsInfos(response.getGoodsInfos())
                .goodses(response.getGoodses())
                .build();
    }

    public TradePriceVO getTradePrice(List<TradeItemVO> tradeItemVOS) {
        TradePriceVO price = new TradePriceVO();
        item.setTradeItems(g.getTradeItems());
        item.setSupplier(g.getSupplier());
        //计算商品总价
        handlePrice(g.getTradeItems(), price);
        //验证并计算各营销活动的优惠金额,实付金额,赠品List
        List<TradeMarketingVO> tradeMarketings = wrapperMarketingForConfirm(g.getTradeItems(), g.getTradeMarketingList());
        List<Discounts> discountsList = new ArrayList<>();
        //每个订单的多个优惠信息(满折优惠了xx,满减优惠了yy)
        item.setDiscountsPrice(discountsList);


        List<TradeMarketingVO> tempList = tradeMarketings.stream().filter(i -> i.getMarketingType() != MarketingType.GIFT
                && i.getMarketingType() != MarketingType.MARKUP).collect(Collectors.toList());
        tempList.forEach(i -> {
            Discounts discounts = Discounts.builder()
                    .amount(i.getDiscountsAmount())
                    .type(i.getMarketingType())
                    .build();
            discountsList.add(discounts);
            //设置营销商品优惠后的均摊价 (用于计算运费)
            List<TradeItem> items = item.getTradeItems().stream().filter(t -> i.getSkuIds().contains(t.getSkuId()))
                    .collect(Collectors.toList());
            tradeItemService.clacSplitPrice(items, i.getRealPayAmount());
        });

        //应付金额 = 商品总金额 - 优惠总金额
        if (!price.isSpecial()) {
            BigDecimal discountsPrice = tempList.stream().map(TradeMarketingVO::getDiscountsAmount).reduce(BigDecimal
                    .ZERO, BigDecimal::add);
            price.setTotalPrice(price.getTotalPrice().subtract(discountsPrice));
        }

        // 加价购商品
        if (CollectionUtils.isNotEmpty(markupList)) {
            BigDecimal markupPrice = markupList.stream().map(TradeItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            price.setMarkupPrice(markupPrice);
            price.setTotalPrice(price.getTotalPrice().add(markupPrice));
            price.setGoodsPrice(price.getGoodsPrice().add(markupPrice));
        }
        item.setTradePrice(price);
        //赠品信息
        item.setGifts(wrapperGifts(g.getTradeMarketingList(), tradeMarketings, gifts));
        item.setGifts(giftNumCheck(item.getGifts()));
        item.getTradeItems().addAll(markupList);
        return item;
    }

}
