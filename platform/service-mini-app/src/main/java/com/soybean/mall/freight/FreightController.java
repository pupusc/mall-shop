package com.soybean.mall.freight;

import com.alibaba.fastjson.JSON;
import com.soybean.common.resp.BaseFixedAddressResp;
import com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum;
import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.freight.req.FreightPriceListReq;
import com.soybean.mall.freight.req.FreightPriceReq;
import com.soybean.mall.freight.resp.FreightPriceListResp;
import com.soybean.mall.freight.resp.FreightPriceResp;
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
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsListByIdsResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdResponse;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdsResponse;
import com.wanmi.sbc.goods.api.response.nacos.GoodsNacosConfigResp;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
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

        //计算营销价格
        String customerId = commonUtil.getOperatorId();
        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(Collections.singletonList(context.getGoodsInfo()), GoodsInfoDTO.class));
        filterRequest.setIsIndependent(Boolean.FALSE);
        if (!StringUtils.isEmpty(customerId)) {
            CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
            if (Objects.nonNull(customer)) {
                filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
            }
        }
        GoodsInfoListByGoodsInfoResponse contextGoodsInfo = marketingPluginProvider.goodsListFilter(filterRequest).getContext();
        GoodsInfoVO goodsInfoVONew = null;
        for (GoodsInfoVO goodsInfoVO : contextGoodsInfo.getGoodsInfoVOList()) {
            goodsInfoVONew = goodsInfoVO;
        }
        if (goodsInfoVONew == null) {
            throw new SbcRuntimeException("999999", "商品信息不存在");
        }

        FreightPriceResp freightPriceResp = new FreightPriceResp();
        //获取地址信息
        GoodsVO goods = context.getGoods();
        GoodsInfoVO goodsInfo = context.getGoodsInfo();
        TradeParamsRequest tradeParamsRequest = new TradeParamsRequest();
        ConsigneeDTO consigneeDTO = new ConsigneeDTO();
        consigneeDTO.setProvinceId(Long.valueOf(fixedAddress.getProvinceId()));
        consigneeDTO.setCityId(Long.valueOf(fixedAddress.getCityId()));
        tradeParamsRequest.setConsignee(consigneeDTO);

        tradeParamsRequest.setDeliverWay(DeliverWay.EXPRESS);
        TradePriceDTO tradePriceDTO = new TradePriceDTO();
        tradePriceDTO.setTotalPrice(goodsInfoVONew.getSalePrice());
        tradeParamsRequest.setTradePrice(tradePriceDTO);

        TradeItemDTO tradeItemDTO = new TradeItemDTO();
        tradeItemDTO.setGoodsType(GoodsType.fromValue(goods.getGoodsType()));
        tradeItemDTO.setFreightTempId(goods.getFreightTempId());
        tradeItemDTO.setNum(1L);
        tradeItemDTO.setGoodsWeight(goods.getGoodsWeight());
        tradeItemDTO.setGoodsCubage(goods.getGoodsCubage());
        tradeItemDTO.setSplitPrice(goods.getMarketPrice());
        tradeParamsRequest.setOldTradeItems(Collections.singletonList(tradeItemDTO));

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
        if (Objects.equals(goods.getFreightTempId().toString(), nacosConfigRespContext.getFreeDelivery49())) {
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


        //获取sku信息
        GoodsInfoViewByIdsRequest goodsInfoViewByIdsRequest = new GoodsInfoViewByIdsRequest();
        goodsInfoViewByIdsRequest.setGoodsInfoIds(goodsInfoIds);
        goodsInfoViewByIdsRequest.setDeleteFlag(DeleteFlag.NO);
        GoodsInfoViewByIdsResponse goodsInfoViewByIdsResponse = goodsInfoQueryProvider.listViewByIds(goodsInfoViewByIdsRequest).getContext();
        if (CollectionUtils.isEmpty(goodsInfoViewByIdsResponse.getGoodsInfos())) {
            throw new SbcRuntimeException("99999", "sku信息不存在");
        }


        Set<String> spuIds = new HashSet<>();
        for (GoodsInfoVO goodsInfo : goodsInfoViewByIdsResponse.getGoodsInfos()) {
            spuIds.add(goodsInfo.getGoodsId());
        }

        //获取spu信息
        GoodsListByIdsRequest goodsListByIdsRequest = new GoodsListByIdsRequest();
        goodsListByIdsRequest.setGoodsIds(new ArrayList<>(spuIds));
        GoodsListByIdsResponse spuContext = goodsQueryProvider.listByIds(goodsListByIdsRequest).getContext();
        if (CollectionUtils.isEmpty(spuContext.getGoodsVOList())) {
            throw new SbcRuntimeException("99999", "spu信息不存在");
        }
        if (spuContext.getGoodsVOList().size() != spuIds.size()) {
            throw new SbcRuntimeException("99999", "spu信息数量不同");
        }

        //获取营销价格信息
        Map<String, GoodsInfoVO> skuId2GoodsInfoNewVoMap = new HashMap<>();

        String customerId = commonUtil.getOperatorId();
        //计算营销价格
        MarketingPluginGoodsListFilterRequest filterRequest = new MarketingPluginGoodsListFilterRequest();
        filterRequest.setGoodsInfos(KsBeanUtil.convert(goodsInfoViewByIdsResponse.getGoodsInfos(), GoodsInfoDTO.class));
        filterRequest.setIsIndependent(Boolean.FALSE);
        if (!StringUtils.isEmpty(customerId)) {
            CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest(customerId)).getContext();
            if (Objects.nonNull(customer)) {
                filterRequest.setCustomerDTO(KsBeanUtil.convert(customer, CustomerDTO.class));
            }
        }
        GoodsInfoListByGoodsInfoResponse contextGoodsInfo = marketingPluginProvider.goodsListFilter(filterRequest).getContext();
        for (GoodsInfoVO goodsInfoVO : contextGoodsInfo.getGoodsInfoVOList()) {
            skuId2GoodsInfoNewVoMap.put(goodsInfoVO.getGoodsInfoId(), goodsInfoVO);
        }

        Map<String, GoodsVO> spuId2ModelMap =
                spuContext.getGoodsVOList().stream().collect(Collectors.toMap(GoodsVO::getGoodsId, Function.identity(), (k1, k2) -> k1));

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

        BigDecimal sumPrice = BigDecimal.ZERO;
        Long storeId = 0L;
        List<TradeItemDTO> tradeItemDTOList = new ArrayList<>();
        boolean hasFreeDelivery49 = false;
        for (FreightPriceListReq.FreightSkuReq sku : freightPriceListReq.getSkus()) {
            GoodsInfoVO goodsInfoVO = skuId2GoodsInfoNewVoMap.get(sku.getSkuId());
            if (goodsInfoVO == null) {
                continue;
            }
            GoodsVO goodsVO = spuId2ModelMap.get(goodsInfoVO.getGoodsId());
            if (goodsVO == null) {
                continue;
            }

            GoodsNacosConfigResp nacosConfigRespContext = goodsNacosConfigProvider.getNacosConfig().getContext();
            if (Objects.equals(goodsVO.getFreightTempId().toString(), nacosConfigRespContext.getFreeDelivery49())) {
                hasFreeDelivery49 = true;
                sumPrice = sumPrice.add(goodsInfoVO.getSalePrice());

                storeId = goodsVO.getStoreId();
                TradeItemDTO tradeItemDTO = new TradeItemDTO();
                tradeItemDTO.setGoodsType(GoodsType.fromValue(goodsVO.getGoodsType()));
                tradeItemDTO.setFreightTempId(goodsVO.getFreightTempId());
                tradeItemDTO.setNum(sku.getNum().longValue());
                tradeItemDTO.setGoodsWeight(goodsVO.getGoodsWeight());
                tradeItemDTO.setGoodsCubage(goodsVO.getGoodsCubage());
                tradeItemDTO.setSplitPrice(goodsInfoVO.getMarketPrice());
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
