package com.wanmi.sbc.linkedmall.goods;

import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.linkedmall.model.v20180116.QueryBizItemListRequest;
import com.aliyuncs.linkedmall.model.v20180116.QueryBizItemListResponse;
import com.aliyuncs.linkedmall.model.v20180116.QueryItemDetailRequest;
import com.aliyuncs.linkedmall.model.v20180116.QueryItemDetailResponse;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.enums.NodeType;
import com.wanmi.sbc.common.enums.node.AccoutAssetsType;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.linkedmall.api.request.goods.GoodsDetailQueryRequest;
import com.wanmi.sbc.linkedmall.api.response.goods.LinkedMallGoodsPageResponse;
import com.wanmi.sbc.linkedmall.util.LinkedMallUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * linkedmall-spu
 */
@Service
@Slf4j
public class LinkedMallGoodsService {
    @Autowired
    private IAcsClient iAcsClient;

    @Autowired
    private LinkedMallUtil linkedMallUtil;

    /**
     * 查询spu详情
     *
     * @param providerGoodsId
     * @return
     */
    public QueryItemDetailResponse.Item getGoodsDetailById(long providerGoodsId) {
        QueryItemDetailRequest queryItemDetailRequest = new QueryItemDetailRequest();
        queryItemDetailRequest.setBizId(linkedMallUtil.getLinkedMallBizId());
        queryItemDetailRequest.setItemId(providerGoodsId);
        QueryItemDetailResponse.Item item = null;
        try {
            item = iAcsClient.getAcsResponse(queryItemDetailRequest).getItem();
        } catch (ClientException e) {
            log.error("查询linkedmall商品详情异常", e);
            throw new SbcRuntimeException("K-300001");
        }
        return item;
    }

    /**
     * 分页获取客户商品库商品(spu维度)
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public LinkedMallGoodsPageResponse<QueryBizItemListResponse.Item> getGoodsPage(Integer pageNum, Integer pageSize) {
        QueryBizItemListRequest queryBizItemListRequest = new QueryBizItemListRequest();
        queryBizItemListRequest.setBizId(linkedMallUtil.getLinkedMallBizId());
        queryBizItemListRequest.setPageNumber(pageNum);
        queryBizItemListRequest.setPageSize(pageSize);
        LinkedMallGoodsPageResponse<QueryBizItemListResponse.Item> response = new LinkedMallGoodsPageResponse<>();
        try {
            QueryBizItemListResponse acsResponse = iAcsClient.getAcsResponse(queryBizItemListRequest);
            if ("SUCCESS".equals(acsResponse.getCode())) {
                List<QueryBizItemListResponse.Item> itemList = acsResponse.getItemList();
                response.setContent(itemList);
                response.setTotal(acsResponse.getTotalCount());
                response.setSize(pageSize);
                response.setNumber(pageNum);
            }
        } catch (ClientException e) {
            log.error("分页查询linkedmall商品异常", e);
        }
        return response;
    }
        //批量查询linkedmall商品详情
    public List<QueryItemDetailResponse.Item> getGoodsDetailBatch(GoodsDetailQueryRequest request) {
        ArrayList<FutureTask<QueryItemDetailResponse.Item>> futureTasks = new ArrayList<>();
        ArrayList<QueryItemDetailResponse.Item> items = new ArrayList<>();
        String bizId = linkedMallUtil.getLinkedMallBizId();
        for (Long itemId : request.getItemIds()) {
            FutureTask<QueryItemDetailResponse.Item> futureTask = new FutureTask<>(new Callable<QueryItemDetailResponse.Item>() {
                @Override
                public QueryItemDetailResponse.Item call() throws Exception {
                    QueryItemDetailRequest queryItemDetailRequest = new QueryItemDetailRequest();
                    queryItemDetailRequest.setBizId(bizId);
                    queryItemDetailRequest.setItemId(itemId);
                    QueryItemDetailResponse.Item item = null;
                    try {
                        item = iAcsClient.getAcsResponse(queryItemDetailRequest).getItem();
                    } catch (ClientException e) {
                        log.error("查询linkedmall商品详情异常,linkedmall商品itemId："+itemId, e);
                        throw new SbcRuntimeException("K-300001");
                    }
                    return item;
                }
            });
            futureTasks.add(futureTask);
            new Thread(futureTask).start();
        }
        for (FutureTask<QueryItemDetailResponse.Item> futureTask : futureTasks) {
            try {
                QueryItemDetailResponse.Item item = futureTask.get();
                if (item != null) {
                    items.add(item);
                }
            } catch (InterruptedException e) {
                log.error("查询linkedmall商品详情异常", e);
                throw new SbcRuntimeException("K-300001");
            } catch (ExecutionException e) {
                log.error("查询linkedmall商品详情异常", e);
                throw new SbcRuntimeException("K-300001");
            }
        }
        return items;
    }
}
