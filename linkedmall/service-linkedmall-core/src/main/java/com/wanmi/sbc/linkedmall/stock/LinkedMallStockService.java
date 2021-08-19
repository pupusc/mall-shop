package com.wanmi.sbc.linkedmall.stock;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryRequest;
import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.linkedmall.util.LinkedMallUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * linkedmall库存
 */
@Service
@Slf4j
public class LinkedMallStockService {
    @Autowired
    private IAcsClient iAcsClient;

    @Autowired
    private LinkedMallUtil linkedMallUtil;

    /**
     * 根据四级配送区域编码批量查询spu库存属性
     *
     * @param divisionCode
     */
    public List<QueryItemInventoryResponse.Item> batchGoodsStockByDivisionCode(List<Long> providerGoodsIds, String divisionCode) {
        ArrayList<QueryItemInventoryRequest.ItemList> itemLists = new ArrayList<>();
        for (Long goodsId : providerGoodsIds) {
            QueryItemInventoryRequest.ItemList itemList = new QueryItemInventoryRequest.ItemList();
            itemList.setItemId(goodsId);
            itemLists.add(itemList);
        }
        QueryItemInventoryRequest queryItemInventoryRequest = new QueryItemInventoryRequest();
        queryItemInventoryRequest.setBizId(linkedMallUtil.getLinkedMallBizId());
        queryItemInventoryRequest.setItemLists(itemLists);
        queryItemInventoryRequest.setDivisionCode(divisionCode);
        List<QueryItemInventoryResponse.Item> items = null;
        try {
            QueryItemInventoryResponse acsResponse = iAcsClient.getAcsResponse(queryItemInventoryRequest);
            items = acsResponse.getItemList();
        } catch (ClientException e) {
            log.error("根据区域编码查询linkedmall商品库存异常", e);
        }
        return items;
    }

    /**
     * 根据ip批量查询spu库存属性
     */
    public List<QueryItemInventoryResponse.Item> batchGoodsStockByIp(List<Long> providerGoodsIds, String ip) {
        ArrayList<QueryItemInventoryRequest.ItemList> itemLists = new ArrayList<>();
        for (Long goodsId : providerGoodsIds) {
            QueryItemInventoryRequest.ItemList itemList = new QueryItemInventoryRequest.ItemList();
            itemList.setItemId(goodsId);
            itemLists.add(itemList);
        }
        QueryItemInventoryRequest queryItemInventoryRequest = new QueryItemInventoryRequest();
        queryItemInventoryRequest.setBizId(linkedMallUtil.getLinkedMallBizId());
        queryItemInventoryRequest.setItemLists(itemLists);
        queryItemInventoryRequest.setIp(ip);
        List<QueryItemInventoryResponse.Item> items = null;
        try {
            QueryItemInventoryResponse acsResponse = iAcsClient.getAcsResponse(queryItemInventoryRequest);
            items = acsResponse.getItemList();
        } catch (ClientException e) {
            log.error("根据ip查询linkedmall商品库存异常", e);
        }
        return items;
    }


}
