package com.soybean.mall.freight;

import com.soybean.common.resp.BaseFixedAddressResp;
import com.soybean.elastic.api.enums.SearchSpuNewLabelCategoryEnum;
import com.soybean.mall.common.CommonUtil;
import com.soybean.mall.freight.req.FreightPriceReq;
import com.soybean.mall.freight.resp.FreightPriceResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.provider.nacos.GoodsNacosConfigProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoViewByIdResponse;
import com.wanmi.sbc.goods.api.response.nacos.GoodsNacosConfigResp;
import com.wanmi.sbc.goods.bean.enums.DeliverWay;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.goods.bean.vo.GoodsVO;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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

        BaseFixedAddressResp fixedAddress = commonUtil.getFixedAddress();

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
        tradePriceDTO.setTotalPrice(goodsInfo.getSalePrice());
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

}
