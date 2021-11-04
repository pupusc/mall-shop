package com.fangdeng.server.assembler;

import com.fangdeng.server.client.request.bookuu.BookuuOrderAddRequest;
import com.fangdeng.server.dto.OrderTradeDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderAssembler {
    public static BookuuOrderAddRequest convert(OrderTradeDTO order){
        BookuuOrderAddRequest request = new BookuuOrderAddRequest();
        request.setSequence(order.getPlatformCode());

        BookuuOrderAddRequest.ReceiveInfo receiveInfo = new BookuuOrderAddRequest.ReceiveInfo();
        receiveInfo.setAddressLevel1(order.getReceiverProvince());
        receiveInfo.setAddressLevel2(order.getReceiverCity());
        receiveInfo.setAddressLevel3(order.getReceiverDistrict());
        receiveInfo.setTotalAddress(order.getReceiverAddress());
        receiveInfo.setZipCode(order.getReceiverZip());
        receiveInfo.setPostFee(order.getPostFee());
        receiveInfo.setRecvName(order.getReceiverName());
        receiveInfo.setRecvMobile(order.getReceiverMobile());
        receiveInfo.setRecvPhone(order.getReceiverPhone());
        request.setRecvInfo(receiveInfo);

        List<BookuuOrderAddRequest.Product> list= new ArrayList<>();
        order.getDetails().forEach(d->{
            BookuuOrderAddRequest.Product product = new BookuuOrderAddRequest.Product();
            product.setBookID(d.getItemCode());
            product.setBookNum(d.getQty());
            product.setUnitPrice(d.getCostPrice()!=null?new BigDecimal(d.getCostPrice()) :new BigDecimal(d.getPrice()));
            list.add(product);
        });
        request.setProductList(list);
        return request;
    }
}
