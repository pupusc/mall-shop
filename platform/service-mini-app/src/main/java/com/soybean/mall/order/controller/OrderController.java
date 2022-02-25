package com.soybean.mall.order.controller;

import com.alibaba.fastjson.JSON;
import com.soybean.mall.order.bean.vo.OrderCommitResultVO;
import com.soybean.mall.order.response.OrderConfirmResponse;
import com.soybean.mall.vo.WxAddressInfoVO;
import com.soybean.mall.vo.WxOrderCommitResultVO;
import com.soybean.mall.vo.WxOrderPaymentVO;
import com.soybean.mall.vo.WxProductInfoVO;
import com.soybean.mall.wx.mini.order.bean.dto.*;
import com.wanmi.sbc.common.annotation.MultiSubmitWithToken;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.MD5Util;
import com.wanmi.sbc.customer.api.provider.address.CustomerDeliveryAddressQueryProvider;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.address.CustomerDeliveryAddressByIdRequest;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.api.response.address.CustomerDeliveryAddressByIdResponse;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingLevelPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingLevelGoodsListFilterRequest;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.VerifyQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeCommitRequest;
import com.wanmi.sbc.order.api.request.trade.VerifyGoodsRequest;
import com.wanmi.sbc.order.bean.dto.TradeGoodsInfoPageDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.vo.SupplierVO;
import com.wanmi.sbc.order.bean.vo.TradeConfirmItemVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import com.wanmi.sbc.order.bean.vo.TradePriceVO;
import com.wanmi.sbc.order.request.TradeItemConfirmRequest;
import com.wanmi.sbc.setting.api.provider.platformaddress.PlatformAddressQueryProvider;
import com.wanmi.sbc.setting.api.request.platformaddress.PlatformAddressVerifyRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

;

@Slf4j
@RestController
@RequestMapping("/wx/order")
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
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private VerifyQueryProvider verifyQueryProvider;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CustomerDeliveryAddressQueryProvider customerDeliveryAddressQueryProvider;

    @Autowired
    private PlatformAddressQueryProvider platformAddressQueryProvider;

    @Autowired
    private TradeProvider tradeProvider;

    /**
     * 提交订单，用于生成订单操作
     */
//    @ApiOperation(value = "提交订单，用于生成订单操作")
//    @RequestMapping(value = "/commit", method = RequestMethod.POST)
//    @MultiSubmitWithToken
//    //@GlobalTransactional
//    public BaseResponse<WxOrderPaymentVO> commit(@RequestBody @Valid TradeCommitRequest tradeCommitRequest) {
//        //校验是否需要完善地址信息
//        CustomerDeliveryAddressByIdResponse address = null;
//        customerDeliveryAddressQueryProvider.getById(new CustomerDeliveryAddressByIdRequest(tradeCommitRequest.getConsigneeId())).getContext();
//        if (address != null) {
//            PlatformAddressVerifyRequest platformAddressVerifyRequest = new PlatformAddressVerifyRequest();
//            if (Objects.nonNull(address.getProvinceId())) {
//                platformAddressVerifyRequest.setProvinceId(String.valueOf(address.getProvinceId()));
//            }
//            if (Objects.nonNull(address.getCityId())) {
//                platformAddressVerifyRequest.setCityId(String.valueOf(address.getCityId()));
//            }
//            if (Objects.nonNull(address.getAreaId())) {
//                platformAddressVerifyRequest.setAreaId(String.valueOf(address.getAreaId()));
//            }
//            if (Objects.nonNull(address.getStreetId())) {
//                platformAddressVerifyRequest.setStreetId(String.valueOf(address.getStreetId()));
//            }
//            if (Boolean.TRUE.equals(platformAddressQueryProvider.verifyAddress(platformAddressVerifyRequest).getContext())) {
//                throw new SbcRuntimeException("K-220001");
//            }
//        }
//        RLock rLock = redissonClient.getFairLock(commonUtil.getOperatorId());
//        rLock.lock();
//        List<OrderCommitResultVO> successResults;
//        try {
//            Operator operator = commonUtil.getOperator();
//            tradeCommitRequest.setOperator(operator);
//            successResults = tradeProvider.commitTrade(tradeCommitRequest).getContext().getOrderCommitResults();
//
//        } catch (Exception e) {
//            throw e;
//        } finally {
//            rLock.unlock();
//        }
//        return BaseResponse.success(getOrderPaymentResult(successResults,""));
//
//    }

//    private WxOrderPaymentVO getOrderPaymentResult(List<OrderCommitResultVO> trades,String openId){
//        WxOrderPaymentVO wxOrderPaymentVO = new WxOrderPaymentVO();
//        wxOrderPaymentVO.setTimeStamp(String.valueOf((new Date()).getTime()/1000));
//        wxOrderPaymentVO.setNonceStr(RandomStringUtils.randomAlphanumeric(32));
//        wxOrderPaymentVO.setOrderInfo(convertResult(trades,openId));
//        wxOrderPaymentVO.setPrepayId(trades.get(0).getId());
//        String sign = getSign(wxOrderPaymentVO);
//    }

    private WxOrderCommitResultVO convertResult(List<OrderCommitResultVO> trades,String openId) {
        WxOrderCommitResultVO result = new WxOrderCommitResultVO();
        result.setOutOrderId(trades.get(0).getId());
        result.setCreateTime(DateUtil.format(LocalDateTime.now(),DateUtil.FMT_TIME_1));
        result.setOpenid(openId);
        result.setPath("test");
        OrderCommitResultVO trade = trades.get(0);
        WxOrderDetailDTO detail = new WxOrderDetailDTO();
        List<WxProductInfoDTO> productInfoDTOS = new ArrayList<>();
        trade.getTradeItems().forEach(tradeItem -> {
            productInfoDTOS.add(WxProductInfoDTO.builder()
                    .outProductId(tradeItem.getSpuId())
                    .outSkuId(tradeItem.getSkuId())
                    .productNum(tradeItem.getNum())
                    .salePrice(tradeItem.getOriginalPrice())
                    .realPrice(tradeItem.getSplitPrice())
                    .title(tradeItem.getSkuName())
                    .headImg(tradeItem.getPic()).build());
        });
        detail.setProductInfos(productInfoDTOS);

        detail.setPayInfo(WxPayInfoDTO.builder().payMethodType(0)
                .prepayId(trades.get(0).getId())
                .prepayTime(DateUtil.format(LocalDateTime.now(),DateUtil.FMT_TIME_1)).build());

        WxPriceInfoDTO priceInfo = new WxPriceInfoDTO();
        priceInfo.setOrderPrice(trade.getTradePrice().getTotalPrice());
        priceInfo.setFreight(trade.getTradePrice().getDeliveryPrice());
        detail.setPriceInfo(priceInfo);

        WxAddressInfoDTO addressInfo = new WxAddressInfoDTO();
        addressInfo.setCity(trade.getConsignee().getCityName());
        addressInfo.setReceiverName(trade.getConsignee().getName());
        addressInfo.setDetailedAddress(trade.getConsignee().getDetailAddress());
        addressInfo.setProvince(trade.getConsignee().getProvinceName());
        addressInfo.setTown(trade.getConsignee().getAreaName());
        addressInfo.setTelNumber(trade.getConsignee().getPhone());
        result.setAddressInfo(addressInfo);
        result.setOrderDetail(detail);
        return result;


    }

    /**
     * 获取签名
     * @param orderPaymentVO
     * @return
     */
    private String getSign(WxOrderPaymentVO orderPaymentVO){
        Map<String,Object> map =new HashMap<>();
        map.put("timeStamp", orderPaymentVO.getTimeStamp());
        map.put("nonceStr", orderPaymentVO.getNonceStr());
        map.put("package",orderPaymentVO.getPrepayId());
        map.put("orderInfo",JSON.toJSONString(orderPaymentVO.getOrderInfo()));
        String sign = getSignByMap(map);
        return sign;

    }

    /**
     * 生成签名
     * 第一步，设所有发送或者接收到的数据为集合M，将集合M内非空参数值的参数按照参数名ASCII码从小到大排序（字典序），使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串stringA。
     * 第二步，在stringA最后拼接上key=(API密钥的值)得到stringSignTemp字符串，并对stringSignTemp进行MD5运算，再将得到的字符串所有字符转换为大写，得到sign值signValue。
     * @param params
     * @return
     */
    public String getSignByMap(Map<String,Object> params){
        String[] sortedKeys = params.keySet().toArray(new String[]{});
        Arrays.sort(sortedKeys);// 排序请求参数
        StringBuilder sb = new StringBuilder();
        for (String key : sortedKeys) {
            sb.append(key).append("=").append(params.get(key)).append("&");
        }
        sb.append("key=").append("Fandengdushudianshang20220223kai");
        return MD5Util.md5Hex(sb.toString(), "utf-8").toUpperCase();
    }

    /**
     * 用于确认订单后，创建订单前的获取订单商品信息
     */
    @ApiOperation(value = "用于确认订单后，创建订单前的获取订单商品信息")
    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
    //@GlobalTransactional
    public BaseResponse<OrderConfirmResponse> getPurchaseItems(@RequestBody TradeItemConfirmRequest request) {

        OrderConfirmResponse confirmResponse = new OrderConfirmResponse();
        String customerId = commonUtil.getOperatorId();
        //验证用户
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (customerId)).getContext();
        //入参是商品sku和num，返回商品信息和价格信息
        List<TradeItemDTO> tradeItems = KsBeanUtil.convertList(request.getTradeItems(), TradeItemDTO.class);
        List<String> skuIds = tradeItems.stream().map(TradeItemDTO::getSkuId).collect(Collectors.toList());

        //获取订单商品详情和会员价salePrice
        GoodsInfoResponse skuResp = getGoodsResponse(skuIds, customer);
        List<TradeConfirmItemVO> items = new ArrayList<>(1);
        //一期只能购买一个商品，只有一个商家
        StoreVO store = storeQueryProvider.getNoDeleteStoreById(NoDeleteStoreByIdRequest.builder().storeId(skuResp.getGoodsInfos().get(0).getStoreId())
                .build())
                .getContext().getStoreVO();

        TradeConfirmItemVO tradeConfirmItemVO = new TradeConfirmItemVO();
        //商品验证并填充商品价格
        List<TradeItemVO> tradeItemVOList =
                verifyQueryProvider.verifyGoods(new VerifyGoodsRequest(tradeItems, Collections.emptyList(),
                        KsBeanUtil.convert(skuResp, TradeGoodsInfoPageDTO.class),
                        store.getStoreId(), true)).getContext().getTradeItems();
        tradeConfirmItemVO.setTradeItems(tradeItemVOList);
        tradeConfirmItemVO.setTradePrice(calPrice(tradeItemVOList));

        DefaultFlag freightTemplateType = store.getFreightTemplateType();
        SupplierVO supplier = SupplierVO.builder()
                .storeId(store.getStoreId())
                .storeName(store.getStoreName())
                .isSelf(store.getCompanyType() == BoolFlag.NO)
                .supplierCode(store.getCompanyInfo().getCompanyCode())
                .supplierId(store.getCompanyInfo().getCompanyInfoId())
                .supplierName(store.getCompanyInfo().getSupplierName())
                .freightTemplateType(freightTemplateType)
                .build();
        tradeConfirmItemVO.setSupplier(supplier);
        items.add(tradeConfirmItemVO);
        confirmResponse.setTradeConfirmItems(items);
        return BaseResponse.success(confirmResponse);
    }

    /**
     * 获取订单商品详情,会员价
     */
    private GoodsInfoResponse getGoodsResponse(List<String> skuIds, CustomerVO customer) {
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


    /**
     * 计算商品价格
     *
     * @param tradeItems 多个订单项(商品)
     */
    private TradePriceVO calPrice(List<TradeItemVO> tradeItems) {
        TradePriceVO tradePrice = new TradePriceVO();
        tradePrice.setGoodsPrice(BigDecimal.ZERO);
        tradePrice.setOriginPrice(BigDecimal.ZERO);
        tradePrice.setTotalPrice(BigDecimal.ZERO);
        tradePrice.setBuyPoints(null);
        tradeItems.forEach(t -> {
            BigDecimal buyItemPrice = t.getPrice().multiply(BigDecimal.valueOf(t.getNum()));
            BigDecimal originalPrice = t.getOriginalPrice().multiply(BigDecimal.valueOf(t.getNum()));
            //总价，有定价=定价*数量，否则=原价
            BigDecimal totalPrice = t.getPropPrice() != null ? (new BigDecimal(t.getPropPrice()).multiply(BigDecimal.valueOf(t.getNum()))) : originalPrice;
            // 订单商品总价
            tradePrice.setGoodsPrice(tradePrice.getGoodsPrice().add(buyItemPrice));
            // 订单总金额
            tradePrice.setTotalPrice(tradePrice.getTotalPrice().add(totalPrice));
            // 订单原始总金额
            tradePrice.setOriginPrice(tradePrice.getOriginPrice().add(originalPrice));
            //优惠金额=定价-原价
            tradePrice.setDiscountsPrice(totalPrice.subtract(originalPrice));
            //会员优惠
            tradePrice.setVipDiscountPrice(originalPrice.subtract(buyItemPrice));
        });
        return tradePrice;
    }

}
