package com.wanmi.sbc.returnorder.convert;

import com.wanmi.sbc.order.bean.dto.ReturnItemDTO;
import com.wanmi.sbc.order.bean.dto.ReturnOrderDTO;
import com.wanmi.sbc.order.bean.dto.ReturnPointsDTO;
import com.wanmi.sbc.order.bean.dto.ReturnPriceDTO;
import com.wanmi.sbc.returnorder.request.RemedyReturnRequest;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RemedyReturnRequest -> ReturnOrder
 * Created by jinwei on 25/4/2017.
 */
public class Remedy2ReturnOrder {

    public static ReturnOrderDTO convert(RemedyReturnRequest request) {
        List<ReturnItemDTO> returnItems = request.getReturnItemNums() == null ? null :
                request.getReturnItemNums().stream().map(
                        t -> ReturnItemDTO.builder().skuId(t.getSkuId()).num(t.getNum()).build()
                ).collect(Collectors.toList());

        return ReturnOrderDTO.builder()
                .id(request.getRid())
                .returnReason(request.getReturnReason())
                .returnWay(request.getReturnWay())
                .description(request.getDescription())
                .images(request.getImages())
                .returnItems(returnItems)
                .returnPrice(ReturnPriceDTO.builder()
                        .applyStatus(request.getReturnPriceRequest().getApplyStatus())
                        .applyPrice(request.getReturnPriceRequest().getApplyPrice())
                        .totalPrice(request.getReturnPriceRequest().getTotalPrice())
                        .build())
                .returnPoints(ReturnPointsDTO.builder()
                        .applyPoints(request.getReturnPointsRequest().getApplyPoints())
                        .build())
                .build();
    }
}
