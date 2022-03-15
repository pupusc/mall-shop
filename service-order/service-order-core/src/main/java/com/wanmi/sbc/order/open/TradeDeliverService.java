package com.wanmi.sbc.order.open;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.open.vo.ConsigneeResDTO;
import com.wanmi.sbc.open.vo.DeliverItemResDTO;
import com.wanmi.sbc.open.vo.LogisticsResDTO;
import com.wanmi.sbc.order.bean.enums.OutTradePlatEnum;
import com.wanmi.sbc.order.open.model.DeliverResDTO;
import com.wanmi.sbc.order.open.model.OrderDeliverInfoResDTO;
import com.wanmi.sbc.order.trade.model.entity.value.Consignee;
import com.wanmi.sbc.order.trade.model.entity.value.Logistics;
import com.wanmi.sbc.order.trade.model.entity.value.ShippingItem;
import com.wanmi.sbc.order.trade.model.root.Trade;
import com.wanmi.sbc.order.trade.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Liang Jun
 * @desc 订单发货
 * @date 2022-02-20 18:00:00
 */
@Service
@Slf4j
public class TradeDeliverService {
    @Autowired
    private TradeService tradeService;
    @Autowired
    private ExternalService externalService;

    /**
     * 订单已发货
     */
    public void onDeliveredForOutPlat(String orderId) {
        //回调通知樊登读书履约中台
        if (Objects.isNull(orderId)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        Trade trade = tradeService.detail(orderId);
        if (Objects.isNull(trade)) {
            return;
        }
        if (!OutTradePlatEnum.FDDS_PERFORM.getCode().equals(trade.getOutTradePlat())) {
            return;
        }
        //通知给第三方
        OrderDeliverInfoResDTO resultDTO = buildOrderDeliverInfo(trade);
        externalService.callbackForDeliver(resultDTO);
    }

    public OrderDeliverInfoResDTO buildOrderDeliverInfo(Trade trade) {
        OrderDeliverInfoResDTO resultDTO = new OrderDeliverInfoResDTO();
        resultDTO.setTradeNo(trade.getId());
        resultDTO.setOutTradeNo(trade.getOutTradeNo());
        resultDTO.setOrderStatus(Objects.nonNull(trade.getTradeState()) && Objects.nonNull(trade.getTradeState().getFlowState())
                ? trade.getTradeState().getFlowState().getStateId() : "OTHER");
        resultDTO.setDeliverStatus(Objects.nonNull(trade.getTradeState()) && Objects.nonNull(trade.getTradeState().getDeliverStatus())
                ? trade.getTradeState().getDeliverStatus().getStatusId() : "OTHER");
        //发货单
        if (!CollectionUtils.isEmpty(trade.getTradeDelivers())) {
            List<DeliverResDTO> deliverResDTOs = trade.getTradeDelivers().stream().map(item -> {
                DeliverResDTO deliverResDTO = new DeliverResDTO();
                deliverResDTO.setDeliverId(item.getDeliverId());
                //收货人信息
                fillConsinee(deliverResDTO, item.getConsignee());
                //物流信息
                fillLogistics(deliverResDTO, item.getLogistics());
                //发货清单
                fillDeliverItems(deliverResDTO, item.getShippingItems());
                return deliverResDTO;
            }).collect(Collectors.toList());
            resultDTO.setDelivers(deliverResDTOs);
        }
        return resultDTO;
    }

    private void fillConsinee(DeliverResDTO deliverResDTO, Consignee consignee) {
        if (Objects.isNull(deliverResDTO) || Objects.isNull(consignee)) {
            return;
        }
        ConsigneeResDTO consigneeResDTO = new ConsigneeResDTO();
        consigneeResDTO.setProvinceId(consignee.getProvinceId());
        consigneeResDTO.setProvinceName(consignee.getProvinceName());
        consigneeResDTO.setCityId(consignee.getCityId());
        consigneeResDTO.setCityName(consignee.getCityName());
        consigneeResDTO.setAreaId(consignee.getAreaId());
        consigneeResDTO.setAreaName(consignee.getAreaName());
        consigneeResDTO.setAddress(consignee.getAddress());
        consigneeResDTO.setDetailAddress(consignee.getDetailAddress());
        consigneeResDTO.setName(consignee.getName());
        consigneeResDTO.setPhone(consignee.getPhone());
        consigneeResDTO.setExpectTime(consignee.getExpectTime());
        consigneeResDTO.setUpdateTime(consignee.getUpdateTime());
        deliverResDTO.setConsignee(consigneeResDTO);
    }

    private void fillLogistics(DeliverResDTO deliverResDTO, Logistics logistics) {
        if (Objects.isNull(deliverResDTO) || Objects.isNull(logistics)) {
            return;
        }
        LogisticsResDTO logisticsResDTO = new LogisticsResDTO();
        logisticsResDTO.setShipMethodId(logistics.getShipMethodId());
        logisticsResDTO.setShipMethodName(logistics.getShipMethodName());
        logisticsResDTO.setLogisticNo(logistics.getLogisticNo());
        logisticsResDTO.setLogisticFee(logistics.getLogisticFee());
        logisticsResDTO.setLogisticCompanyId(logistics.getLogisticCompanyId());
        logisticsResDTO.setLogisticCompanyName(logistics.getLogisticCompanyName());
        logisticsResDTO.setLogisticStandardCode(logistics.getLogisticStandardCode());
        logisticsResDTO.setBuyerId(logistics.getBuyerId());
        deliverResDTO.setLogistics(logisticsResDTO);
    }

    private void fillDeliverItems(DeliverResDTO deliverResDTO, List<ShippingItem> shippingItems) {
        if (Objects.isNull(deliverResDTO) || Objects.isNull(shippingItems)) {
            return;
        }

        List<DeliverItemResDTO> deliverItems = shippingItems.stream().map(item -> {
            DeliverItemResDTO deliverItem = new DeliverItemResDTO();
            deliverItem.setListNo(item.getListNo());
            deliverItem.setItemName(item.getItemName());
            deliverItem.setItemNum(item.getItemNum());
            deliverItem.setSkuId(item.getSkuId());
            deliverItem.setSkuNo(item.getSkuNo());
            deliverItem.setPic(item.getPic());
            deliverItem.setSpecDetails(item.getSpecDetails());
            deliverItem.setUnit(item.getUnit());
            return deliverItem;
        }).collect(Collectors.toList());

        deliverResDTO.setDeliverItems(deliverItems);
    }
}
