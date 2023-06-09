package com.soybean.mall.freight;
import com.google.common.collect.Lists;

import com.alibaba.fastjson.JSON;
import com.soybean.common.resp.BaseFixedAddressResp;
import com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum;
import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.freight.req.FreightPriceListReq;
import com.soybean.mall.freight.req.FreightPriceReq;
import com.soybean.mall.freight.resp.FreightPriceListResp;
import com.soybean.mall.freight.resp.FreightPriceResp;
import com.soybean.mall.order.request.DiscountPriceReq;
import com.soybean.mall.order.response.TradeConfirmResp;
import com.soybean.mall.order.service.DiscountPriceService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.MaxDiscountPaidCardRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.dto.CustomerDTO;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.nacos.GoodsNacosConfigProvider;
import com.wanmi.sbc.goods.api.request.goods.GoodsListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoListByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.api.response.nacos.GoodsNacosConfigResp;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.goods.bean.enums.GoodsPriceType;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingPluginGoodsListFilterRequest;
import com.wanmi.sbc.marketing.api.response.info.GoodsInfoListByGoodsInfoResponse;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradeParamsRequest;
import com.wanmi.sbc.order.api.response.trade.TradeGetFreightResponse;
import com.wanmi.sbc.order.bean.dto.ConsigneeDTO;
import com.wanmi.sbc.order.bean.dto.SupplierDTO;
import com.wanmi.sbc.order.bean.dto.TradeItemDTO;
import com.wanmi.sbc.order.bean.dto.TradePriceDTO;
import com.wanmi.sbc.order.bean.vo.TradeConfirmItemVO;
import com.wanmi.sbc.order.bean.vo.TradeItemVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 运费信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/10/22 12:45 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Slf4j
@RestController
@RequestMapping("/freight")
public class FreightController {

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private GoodsNacosConfigProvider goodsNacosConfigProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private MarketingPluginProvider marketingPluginProvider;

    @Autowired
    private DiscountPriceService discountPriceService;

    @PostMapping("/computePayPrice")
    public BaseResponse<TradeConfirmResp> computePayPrice(@RequestBody DiscountPriceReq discountPriceReq) {
        String customerId = commonUtil.getOperatorId();
        CustomerGetByIdResponse customer =  null;
        if (!StringUtils.isEmpty(customerId)) {
             customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
        } else {
            customer = new CustomerGetByIdResponse();
        }
        TradeConfirmResp tradeConfirmResp = discountPriceService.computePayPrice(discountPriceReq, customer);
        return BaseResponse.success(tradeConfirmResp);
    }


    /**
     * 根据skuid获取商品标签/运费信息
     * @param freightPriceReq
     * @menu 搜索功能
     * @return
     */
    @PostMapping("/getFreightPrice")
    public BaseResponse<FreightPriceResp> getFreightPrice(@RequestBody FreightPriceReq freightPriceReq){
        if (StringUtils.isBlank(freightPriceReq.getSkuId())) {
            throw new SbcRuntimeException("999999", "skuId为空");
        }
        //获取49包邮标签信息
        GoodsInfoViewByIdRequest goodsInfoViewByIdRequest = new GoodsInfoViewByIdRequest();
        goodsInfoViewByIdRequest.setGoodsInfoId(freightPriceReq.getSkuId());
        GoodsInfoViewByIdResponse context = goodsInfoQueryProvider.getViewById(goodsInfoViewByIdRequest).getContext();
        if (context.getGoods() == null || context.getGoodsInfo() == null) {
            throw new SbcRuntimeException("skuId对应的商品信息不存在");
        }

        BaseFixedAddressResp fixedAddress = null;
        if (StringUtils.isBlank(freightPriceReq.getProvinceId()) || StringUtils.isBlank(freightPriceReq.getCityId())) {
            fixedAddress = commonUtil.getFixedAddress();
        } else {
            fixedAddress = new BaseFixedAddressResp();
            fixedAddress.setProvinceId(freightPriceReq.getProvinceId());
            fixedAddress.setCityId(freightPriceReq.getCityId());
        }

        String customerId = commonUtil.getOperatorId();
        CustomerGetByIdResponse customer = null;
        if (!StringUtils.isEmpty(customerId)) {
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
        }

        List<DiscountPriceReq.DiscountPriceSkuReq> skus = new ArrayList<>();
        DiscountPriceReq.DiscountPriceSkuReq discountPriceSkuReq = new DiscountPriceReq.DiscountPriceSkuReq();
        discountPriceSkuReq.setSkuId(freightPriceReq.getSkuId());
        discountPriceSkuReq.setNum(1);
        skus.add(discountPriceSkuReq);

        DiscountPriceReq discountPriceReq = new DiscountPriceReq();
        discountPriceReq.setItems(skus);
        discountPriceReq.setMarketings(new ArrayList<>());
        TradeConfirmResp tradeConfirmResp = discountPriceService.computePayPrice(discountPriceReq, customer);
        log.info("FreightController listFreightPrice tradeConfirmResp {}", JSON.toJSONString(tradeConfirmResp));
        if (CollectionUtils.isEmpty(tradeConfirmResp.getTradeConfirmItems())) {
            throw new SbcRuntimeException("999999", "根据skuId计算运费失败");
        }

        BigDecimal sumPrice = tradeConfirmResp.getTotalPrice();
        TradeConfirmItemVO tradeConfirmItemVO = tradeConfirmResp.getTradeConfirmItems().get(0);


        FreightPriceResp freightPriceResp = new FreightPriceResp();
        //获取地址信息
        GoodsVO goods = context.getGoods();
        TradeParamsRequest tradeParamsRequest = new TradeParamsRequest();
        ConsigneeDTO consigneeDTO = new ConsigneeDTO();
        consigneeDTO.setProvinceId(Long.valueOf(fixedAddress.getProvinceId()));
        consigneeDTO.setCityId(Long.valueOf(fixedAddress.getCityId()));
        tradeParamsRequest.setConsignee(consigneeDTO);

        tradeParamsRequest.setDeliverWay(DeliverWay.EXPRESS);
        TradePriceDTO tradePriceDTO = new TradePriceDTO();
        tradePriceDTO.setTotalPrice(sumPrice);
        tradeParamsRequest.setTradePrice(tradePriceDTO);

        List<TradeItemDTO> tradeItemDTOList = new ArrayList<>();
        for (TradeItemVO tradeItemVO : tradeConfirmItemVO.getTradeItems()) {
            TradeItemDTO tradeItemDTO = new TradeItemDTO();
            tradeItemDTO.setGoodsType(tradeItemVO.getGoodsType());
            tradeItemDTO.setFreightTempId(tradeItemVO.getFreightTempId());
            tradeItemDTO.setNum(1L);
            tradeItemDTO.setGoodsWeight(tradeItemVO.getGoodsWeight());
            tradeItemDTO.setGoodsCubage(tradeItemVO.getGoodsCubage());
            tradeItemDTO.setSplitPrice(tradeItemVO.getSplitPrice());
            tradeItemDTOList.add(tradeItemDTO);
        }

        tradeParamsRequest.setOldTradeItems(tradeItemDTOList);

        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setStoreId(goods.getStoreId());
        supplierDTO.setFreightTemplateType(DefaultFlag.YES);
        tradeParamsRequest.setSupplier(supplierDTO);
        BaseResponse<TradeGetFreightResponse> freight = tradeQueryProvider.getFreight(tradeParamsRequest);
        if(CommonErrorCode.SUCCESSFUL.equals(freight.getCode()) && freight.getContext() == null) {
            throw new SbcRuntimeException("999999", "所选地区不支持配送");
        }

        GoodsNacosConfigResp nacosConfigRespContext = goodsNacosConfigProvider.getNacosConfig().getContext();
        FreightPriceResp.Label preightPriceLabel = new FreightPriceResp.Label();
        if (goods.getFreightTempId() != null && Objects.equals(goods.getFreightTempId().toString(), nacosConfigRespContext.getFreeDelivery49())) {
            SearchSpuNewLabelCategoryEnum freeDelivery = SearchSpuNewLabelCategoryEnum.FREE_DELIVERY_49;
            preightPriceLabel.setLabelName(freeDelivery.getMessage());
            preightPriceLabel.setLabelCategory(freeDelivery.getCode());
            preightPriceLabel.setDeliveryPrice(freight.getContext().getDeliveryPrice().toString());
        } else if (freight.getContext().getDeliveryPrice().compareTo(BigDecimal.ZERO) == 0) {
            SearchSpuNewLabelCategoryEnum freeDelivery = SearchSpuNewLabelCategoryEnum.FREE_DELIVERY;
            preightPriceLabel.setLabelName(freeDelivery.getMessage());
            preightPriceLabel.setLabelCategory(freeDelivery.getCode());
            preightPriceLabel.setDeliveryPrice(freight.getContext().getDeliveryPrice().toString());
        } else {
            preightPriceLabel.setDeliveryPrice(freight.getContext().getDeliveryPrice().toString());
        }
        freightPriceResp.setFreightLabel(preightPriceLabel);
        return BaseResponse.success(freightPriceResp);
    }


    /**
     * 获取49包邮运费信息
     * @menu 搜索功能
     * @param freightPriceListReq
     * @return
     */
    @PostMapping("/getFreightPriceList")
    public BaseResponse<FreightPriceListResp> listFreightPrice(@RequestBody FreightPriceListReq freightPriceListReq){
        if (CollectionUtils.isEmpty(freightPriceListReq.getSkus())) {
            throw new SbcRuntimeException("999999", "请传递商品信息");
        }

        List<String> goodsInfoIds = new ArrayList<>();

        for (FreightPriceListReq.FreightSkuReq sku : freightPriceListReq.getSkus()) {
            if (sku.getNum() == null || sku.getNum() <= 0) {
                throw new SbcRuntimeException("999999", "传递的数量有误");
            }
            goodsInfoIds.add(sku.getSkuId());
        }
        log.info("FreightController listFreightPrice request {}", JSON.toJSONString(freightPriceListReq));
//        List<DiscountPriceReq.DiscountMarketingSkuReq> marketings = new ArrayList<>();
//        if (!CollectionUtils.isEmpty(freightPriceListReq.getMarketings())) {
//            for (FreightPriceListReq.DiscountMarketingSkuReq marketing : freightPriceListReq.getMarketings()) {
//                DiscountPriceReq.DiscountMarketingSkuReq discountMarketingSkuReq = new DiscountPriceReq.DiscountMarketingSkuReq();
//                discountMarketingSkuReq.setMarketingId(marketing.getMarketingId());
//                discountMarketingSkuReq.setMarketingLevelId(marketing.getMarketingLevelId());
//                if (CollectionUtils.isEmpty(marketing.getSkuIds())) {
//                    continue;
//                }
//                discountMarketingSkuReq.setSkuIds(marketing.getSkuIds());
//                marketings.add(discountMarketingSkuReq);
//            }
//        }

        String customerId = commonUtil.getOperatorId();
        CustomerGetByIdResponse customer = null;
        if (!StringUtils.isEmpty(customerId)) {
            customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
        }

        //只是获取49包邮的商品列表
        GoodsInfoListByIdsRequest goodsInfoListByIdsRequest = new GoodsInfoListByIdsRequest();
        goodsInfoListByIdsRequest.setGoodsInfoIds(goodsInfoIds);
        GoodsInfoListByIdsResponse context = goodsInfoQueryProvider.listByIds(goodsInfoListByIdsRequest).getContext();
        if (CollectionUtils.isEmpty(context.getGoodsInfos())) {
            throw new SbcRuntimeException("skuId对应的商品信息不存在");
        }
        //对应goodsId到goodsInfo的关系
        Map<String, List<GoodsInfoVO>> goodsId2GoodsInfoListMap = new HashMap<>();
        for (GoodsInfoVO goodsInfo : context.getGoodsInfos()) {
            List<GoodsInfoVO> goodsInfoVOS = goodsId2GoodsInfoListMap.get(goodsInfo.getGoodsId());
            if (CollectionUtils.isEmpty(goodsInfoVOS)) {
                goodsInfoVOS = new ArrayList<>();
                goodsId2GoodsInfoListMap.put(goodsInfo.getGoodsId(), goodsInfoVOS);
            }
            goodsInfoVOS.add(goodsInfo);
        }
        GoodsListByIdsRequest goodsListByIdsRequest = new GoodsListByIdsRequest();
        goodsListByIdsRequest.setGoodsIds(new ArrayList<>(goodsId2GoodsInfoListMap.keySet()));
        GoodsListByIdsResponse goodsListByIdsResponse = goodsQueryProvider.listByIds(goodsListByIdsRequest).getContext();
        if (CollectionUtils.isEmpty(goodsListByIdsResponse.getGoodsVOList())) {
            throw new SbcRuntimeException("spId对应的商品信息不存在");
        }

        //过滤非49包邮的运费模版
        GoodsNacosConfigResp nacosConfigRespContext = goodsNacosConfigProvider.getNacosConfig().getContext();

        List<DiscountPriceReq.DiscountPriceSkuReq> skus = new ArrayList<>();
        List<DiscountPriceReq.DiscountMarketingSkuReq> marketings = new ArrayList<>();

        for (GoodsVO goodsVO : goodsListByIdsResponse.getGoodsVOList()) {
            List<GoodsInfoVO> goodsInfoVOS = goodsId2GoodsInfoListMap.get(goodsVO.getGoodsId());
            if (CollectionUtils.isEmpty(goodsInfoVOS) || goodsVO.getFreightTempId() == null) {
                continue;
            }
            if (Objects.equals(goodsVO.getFreightTempId().toString(), nacosConfigRespContext.getFreeDelivery49())) {
                List<String> skuIdList = goodsInfoVOS.stream().map(GoodsInfoVO::getGoodsInfoId).collect(Collectors.toList());
                for (FreightPriceListReq.FreightSkuReq sku : freightPriceListReq.getSkus()){
                    if (skuIdList.contains(sku.getSkuId())) {
                        DiscountPriceReq.DiscountPriceSkuReq discountPriceSkuReq = new DiscountPriceReq.DiscountPriceSkuReq();
                        discountPriceSkuReq.setSkuId(sku.getSkuId());
                        discountPriceSkuReq.setNum(sku.getNum());
                        skus.add(discountPriceSkuReq);
                    }
                }
            }
        }

        if (!CollectionUtils.isEmpty(freightPriceListReq.getMarketings())) {
            List<String> skuIds = skus.stream().map(DiscountPriceReq.DiscountPriceSkuReq::getSkuId).collect(Collectors.toList());
            for (FreightPriceListReq.DiscountMarketingSkuReq marketing : freightPriceListReq.getMarketings()) {
                DiscountPriceReq.DiscountMarketingSkuReq discountMarketingSkuReq = new DiscountPriceReq.DiscountMarketingSkuReq();
                discountMarketingSkuReq.setMarketingId(marketing.getMarketingId());
                discountMarketingSkuReq.setMarketingLevelId(marketing.getMarketingLevelId());
                if (CollectionUtils.isEmpty(marketing.getSkuIds())) {
                    continue;
                }
                List<String> marketSkuIds = new ArrayList<>();
                for (String skuId : marketing.getSkuIds()) {
                    if (!skuIds.contains(skuId)) {
                        continue;
                    }
                    marketSkuIds.add(skuId);
                }
                discountMarketingSkuReq.setSkuIds(marketSkuIds);
                if (!CollectionUtils.isEmpty(marketSkuIds)) {
                    marketings.add(discountMarketingSkuReq);
                }
            }
        }

        BigDecimal sumPrice = BigDecimal.ZERO;
        Map<String, TradeItemVO> skuId2TradeItemMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(skus)) {
            DiscountPriceReq discountPriceReq = new DiscountPriceReq();
            discountPriceReq.setItems(skus);
            discountPriceReq.setMarketings(marketings);
            TradeConfirmResp tradeConfirmResp = discountPriceService.computePayPrice(discountPriceReq, customer);
            log.info("FreightController listFreightPrice tradeConfirmResp {}", JSON.toJSONString(tradeConfirmResp));
            if (CollectionUtils.isEmpty(tradeConfirmResp.getTradeConfirmItems())) {
                throw new SbcRuntimeException("999999", "根据skuId计算运费失败");
            }
            sumPrice = tradeConfirmResp.getTotalPrice();
            TradeConfirmItemVO tradeConfirmItemVO = tradeConfirmResp.getTradeConfirmItems().get(0);
            for (TradeItemVO tradeItem : tradeConfirmItemVO.getTradeItems()) {
                skuId2TradeItemMap.put(tradeItem.getSkuId(), tradeItem);
            }
        }




        BaseFixedAddressResp fixedAddress = null;
        if (StringUtils.isBlank(freightPriceListReq.getProvinceId()) || StringUtils.isBlank(freightPriceListReq.getCityId())) {
            fixedAddress = commonUtil.getFixedAddress();
        } else {
            fixedAddress = new BaseFixedAddressResp();
            fixedAddress.setProvinceId(freightPriceListReq.getProvinceId());
            fixedAddress.setCityId(freightPriceListReq.getCityId());
        }

        //获取地址信息
        TradeParamsRequest tradeParamsRequest = new TradeParamsRequest();
        ConsigneeDTO consigneeDTO = new ConsigneeDTO();
        consigneeDTO.setProvinceId(Long.valueOf(fixedAddress.getProvinceId()));
        consigneeDTO.setCityId(Long.valueOf(fixedAddress.getCityId()));
        tradeParamsRequest.setConsignee(consigneeDTO);

        tradeParamsRequest.setDeliverWay(DeliverWay.EXPRESS);

//        BigDecimal sumPrice = BigDecimal.ZERO;
        Long storeId = 0L;
        List<TradeItemDTO> tradeItemDTOList = new ArrayList<>();
        boolean hasFreeDelivery49 = false;
        for (FreightPriceListReq.FreightSkuReq sku : freightPriceListReq.getSkus()) {
            TradeItemVO tradeItemVO = skuId2TradeItemMap.get(sku.getSkuId());
            if (tradeItemVO == null) {
                continue;
            }
//            GoodsVO goodsVO = spuId2ModelMap.get(tradeItemVO.getSpuId());
//            if (goodsVO == null) {
//                continue;
//            }

            if (Objects.equals(tradeItemVO.getFreightTempId().toString(), nacosConfigRespContext.getFreeDelivery49())) {
                hasFreeDelivery49 = true;
//                sumPrice = sumPrice.add(goodsInfoVO.getSalePrice());

                storeId = tradeItemVO.getStoreId();
                TradeItemDTO tradeItemDTO = new TradeItemDTO();
                tradeItemDTO.setGoodsType(tradeItemVO.getGoodsType());
                tradeItemDTO.setFreightTempId(tradeItemVO.getFreightTempId());
                tradeItemDTO.setNum(sku.getNum().longValue());
                tradeItemDTO.setGoodsWeight(tradeItemVO.getGoodsWeight());
                tradeItemDTO.setGoodsCubage(tradeItemVO.getGoodsCubage());
                tradeItemDTO.setSplitPrice(tradeItemVO.getSplitPrice());
                tradeItemDTOList.add(tradeItemDTO);
            }
        }

        BigDecimal freightPrice = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(tradeItemDTOList)) {
            tradeParamsRequest.setOldTradeItems(tradeItemDTOList);

            TradePriceDTO tradePriceDTO = new TradePriceDTO();
            tradePriceDTO.setTotalPrice(sumPrice);
            tradeParamsRequest.setTradePrice(tradePriceDTO);

            SupplierDTO supplierDTO = new SupplierDTO();
            supplierDTO.setStoreId(storeId);
            supplierDTO.setFreightTemplateType(DefaultFlag.YES);
            tradeParamsRequest.setSupplier(supplierDTO);
            log.info("FreightController listFreightPrice invoke getFreight param {}", JSON.toJSONString(tradeParamsRequest));
            BaseResponse<TradeGetFreightResponse> freight = tradeQueryProvider.getFreight(tradeParamsRequest);
            log.info("FreightController listFreightPrice invoke getFreight result {}", JSON.toJSONString(freight));
            if(CommonErrorCode.SUCCESSFUL.equals(freight.getCode()) && freight.getContext() == null) {
                throw new SbcRuntimeException("999999", "所选地区不支持配送");
            }
            freightPrice = freight.getContext().getDeliveryPrice();
        }
        BigDecimal diffSumPrice = new BigDecimal(SearchSpuNewLabelCategoryEnum.FREE_DELIVERY_49.getRealValue().toString()).subtract(sumPrice);
        diffSumPrice = diffSumPrice.compareTo(BigDecimal.ZERO) > 0 ? diffSumPrice : BigDecimal.ZERO;
        FreightPriceListResp freightPriceListResp = new FreightPriceListResp();
        freightPriceListResp.setDeliveryPrice(freightPrice.toString());
        freightPriceListResp.setHasFreeDelivery(hasFreeDelivery49);
        freightPriceListResp.setDiffFreeDelivery(freightPrice.compareTo(BigDecimal.ZERO) > 0 ? diffSumPrice.toString() : freightPrice.toString());
        return BaseResponse.success(freightPriceListResp);
    }
}
