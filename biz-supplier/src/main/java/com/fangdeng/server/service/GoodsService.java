package com.fangdeng.server.service;

import com.fangdeng.server.assembler.GoodsAssembler;
import com.fangdeng.server.client.BookuuClient;
import com.fangdeng.server.client.request.bookuu.BookuuGoodsQueryRequest;
import com.fangdeng.server.client.request.bookuu.BookuuPriceChangeRequest;
import com.fangdeng.server.client.request.bookuu.BookuuPriceQueryRequest;
import com.fangdeng.server.client.request.bookuu.BookuuStockQueryRequest;
import com.fangdeng.server.client.response.bookuu.BookuuGoodsQueryResponse;
import com.fangdeng.server.client.response.bookuu.BookuuPriceChangeResponse;
import com.fangdeng.server.client.response.bookuu.BookuuPriceQueryResponse;
import com.fangdeng.server.client.response.bookuu.BookuuStockQueryResponse;
import com.fangdeng.server.dto.*;
import com.fangdeng.server.mapper.GoodsPriceSyncMapper;
import com.fangdeng.server.mapper.GoodsStockSyncMapper;
import com.fangdeng.server.mapper.GoodsSyncMapper;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GoodsService {
    @Autowired
    private BookuuClient bookuuClient;

    @Autowired
    private GoodsSyncMapper goodsSyncMapper;

    @Autowired
    private GoodsPriceSyncMapper goodsPriceSyncMapper;

    @Autowired
    private GoodsStockSyncMapper goodsStockSyncMapper;

    public void syncGoodsInfo(SyncGoodsQueryDTO queryDTO) {
        BookuuGoodsQueryRequest request = new BookuuGoodsQueryRequest();
        Integer page = 1;
        if(StringUtils.isNotEmpty(queryDTO.getBookId())){
            request.setId(queryDTO.getBookId());
        }else {
            request.setEtime(queryDTO.getEtime());
            request.setPage(page);
            request.setStime(queryDTO.getStime());
        }
        while (true) {
            BookuuGoodsQueryResponse response = bookuuClient.getGoodsList(request);
            if (response != null && CollectionUtils.isNotEmpty(response.getBookList())) {
                batchAdd(response.getBookList());
                if(StringUtils.isNotEmpty(queryDTO.getBookId())){
                    break;
                }
                request.setPage(++page);
            } else {
                break;
            }
        }
    }


    private void batchAdd(List<BookuuGoodsDTO> goodsDTOS) {
        List<GoodsSyncDTO> list = new ArrayList(30);
        List<GoodsImageSyncDTO> imageList = new ArrayList<>();
        goodsDTOS.forEach(g -> {
            list.add(GoodsAssembler.convertGoodsDTO(g));
        });
        try {
            goodsSyncMapper.batchInsert(list);
        } catch (Exception e) {
            log.warn("batch insert goods sync error,goods:{}", list, e);
        }
    }


    /**
     * 更新商品价格
     *
     */
    public void syncGoodsPrice(SyncGoodsQueryDTO queryDTO) {
        Integer maxPage = getMaxPage(queryDTO.getStime(), queryDTO.getEtime());
        if (maxPage < 1) {
            log.info("max page is 0");
            return;
        }
        for (int page = 1; page <= maxPage; page++) {
            BookuuPriceQueryResponse response = queryBookuuPrice(queryDTO.getStime(), queryDTO.getEtime(), page);
            if (response == null || CollectionUtils.isEmpty(response.getPriceList())) {
                log.warn("there is no list,stime:{},etime:{},page:{}", queryDTO.getStime(), queryDTO.getEtime(), page);
                return;
            }
            //落表
            goodsPriceSyncMapper.batchInsert(GoodsAssembler.convertPriceList(response.getPriceList()));
        }
    }

    private Integer getMaxPage(String startTime, String eTime) {
        BookuuPriceChangeRequest priceChangeRequest = new BookuuPriceChangeRequest();
        priceChangeRequest.setPage(1);
        priceChangeRequest.setStime(startTime);
        priceChangeRequest.setEtime(eTime);
        BookuuPriceChangeResponse priceChangeResponse = bookuuClient.queryPriceChange(priceChangeRequest);
        if (priceChangeResponse == null || priceChangeResponse.getMaxPages() == null) {
            return 0;
        }
        return priceChangeResponse.getMaxPages();
    }

    private BookuuPriceQueryResponse queryBookuuPrice(String startTime, String eTime, Integer page) {
        //博库价格分两部，第一步：根据日期查询价格变动的商品，第二步：根据返回的商品id查询价格
        BookuuPriceChangeRequest priceChangeRequest = new BookuuPriceChangeRequest();
        priceChangeRequest.setPage(page);
        priceChangeRequest.setStime(startTime);
        priceChangeRequest.setEtime(eTime);
        BookuuPriceChangeResponse priceChangeResponse = bookuuClient.queryPriceChange(priceChangeRequest);
        if (priceChangeResponse == null || priceChangeResponse.getCount() == 0 || CollectionUtils.isEmpty(priceChangeResponse.getBookList())) {
            log.info("there is not price change,request:{}", priceChangeRequest);
            return null;
        }
        List<String> bookIds = priceChangeResponse.getBookList().stream().map(BookuuPriceChangeResponse.BookItem::getBookId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(bookIds)) {
            log.info("there is not price change,request:{}", priceChangeRequest);
            return null;
        }
        BookuuPriceQueryRequest priceQueryRequest = new BookuuPriceQueryRequest();
        priceQueryRequest.setBookID(String.join(",", bookIds));
        BookuuPriceQueryResponse response = bookuuClient.queryPrice(priceQueryRequest);
        return response;
    }


    /**
     * 同步库存并落表
     */
    public void syncGoodsStock(SyncGoodsQueryDTO queryDTO) {
//        LocalDateTime eTime = LocalDateTime.now();
//        LocalDateTime sTime = eTime.minusMinutes(5);
//        String startTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(sTime);
//        String endTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(eTime);
        BookuuStockQueryRequest request = new BookuuStockQueryRequest();
        if(StringUtils.isNotEmpty(queryDTO.getBookId())){
            request.setBookID(queryDTO.getBookId());
        }else{
            request.setStime(queryDTO.getStime());
            request.setEtime(queryDTO.getEtime());
        }
        BookuuStockQueryResponse response = bookuuClient.queryStock(request);
        if (response == null || CollectionUtils.isEmpty(response.getBookList())) {
            log.info("there is no stock change,queryDTO:{}", queryDTO);
            return;
        }
        goodsStockSyncMapper.batchInsert(GoodsAssembler.convertStockList(response.getBookList()));
    }

}
