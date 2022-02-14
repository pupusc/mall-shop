package com.fangdeng.server.service;

import com.fangdeng.server.assembler.GoodsAssembler;
import com.fangdeng.server.client.BookuuClient;
import com.fangdeng.server.client.request.bookuu.*;
import com.fangdeng.server.client.response.bookuu.*;
import com.fangdeng.server.dto.*;
import com.fangdeng.server.entity.GoodsStockSync;
import com.fangdeng.server.entity.GoodsSync;
import com.fangdeng.server.entity.GoodsSyncRelation;
import com.fangdeng.server.entity.RiskVerify;
import com.fangdeng.server.enums.GoodsSyncStatusEnum;
import com.fangdeng.server.mapper.*;
import com.fangdeng.server.mapper.GoodsSpecialPriceSyncMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    private RiskVerifyMapper riskVerifyMapper;

    @Autowired
    private GoodsSyncRelationMapper goodsSyncRelationMapper;

    @Autowired
    private GoodsSpecialPriceSyncMapper goodsSpecialPriceSyncMapper;

    @Value("${bookuu.providerId}")
    private  Long providerId;

    @Value("${stock.sync.second:300}")
    private Long stockSyncSecond;


    public void syncGoodsInfo(SyncGoodsQueryDTO queryDTO) {
        BookuuGoodsQueryRequest request = new BookuuGoodsQueryRequest();
        //将父类目落标
        if(StringUtils.isNotEmpty(queryDTO.getBookId())){
            //循环
            List<String> goodsIds = Arrays.asList(queryDTO.getBookId().split(","));
            List<String> goodsNo = goodsIds.stream().distinct().collect(Collectors.toList());
            for(int i=0;i< goodsNo.size();i++){
                request.setId(goodsNo.get(i));
                syncGoods(request);
            }
            return;
        }
        if(StringUtils.isNotEmpty(queryDTO.getIsbn())){
            //循环
            List<String> isbn = Arrays.asList(queryDTO.getIsbn().split(","));
            List<String> goodsNo = isbn.stream().distinct().collect(Collectors.toList());
            for(int i=0;i< goodsNo.size();i++){
                request.setIsbn(goodsNo.get(i));
                syncGoods(request);
            }
            return;
        }


        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.parse(queryDTO.getStime(),df);
        LocalDateTime endTime = LocalDateTime.parse(queryDTO.getEtime(),df);
        LocalDateTime tempTime = startTime.minusDays(-5);
        if(tempTime.compareTo(endTime) >0){
            tempTime = endTime;
        }
        while(tempTime.compareTo(endTime) <= 0 && startTime.compareTo(tempTime) <0 ){
            request.setStime(startTime.format(df));
            request.setPage(1);
            request.setEtime(tempTime.format(df));
            while (true){
                try {
                    BookuuGoodsQueryResponse response = bookuuClient.getGoodsList(request);
                    if (CollectionUtils.isEmpty(response.getBookList()) && response.getFlag().equals(0)) {
                        break;
                    }
                    syncPriceAndAdd(response.getBookList());
                }catch (Exception e){
                    log.warn("get book error,request:{}",request,e);
                }
                request.setPage(request.getPage() + 1);
            }
            startTime=tempTime;
            tempTime = tempTime.minusDays(-5);
            if(tempTime.compareTo(endTime) >0){
                tempTime = endTime;
            }


        }
    }

    private void syncPriceAndAdd(List<BookuuGoodsDTO> bookList){
        //查询价格
        String goodsIDs = String.join(",", bookList.stream().map(BookuuGoodsDTO::getBookId).collect(Collectors.toList()));
        BookuuPriceQueryRequest priceQueryRequest = new BookuuPriceQueryRequest();
        priceQueryRequest.setBookID(goodsIDs);
        BookuuPriceQueryResponse bookuuPriceQueryResponse = bookuuClient.queryPrice(priceQueryRequest);
        batchAdd(bookList, bookuuPriceQueryResponse);
    }

    private void syncGoods(BookuuGoodsQueryRequest request){
        try {
            BookuuGoodsQueryResponse response = bookuuClient.getGoodsList(request);
            if (response != null && CollectionUtils.isNotEmpty(response.getBookList())) {
                //查询价格
                syncPriceAndAdd(response.getBookList());
            }
        }catch (Exception e){
            log.warn("get book error,request:{}",request,e);
        }
    }


    private void batchAdd(List<BookuuGoodsDTO> goodsDTOS,BookuuPriceQueryResponse priceQueryResponse) {
        List<GoodsSync> list = new ArrayList(30);
        List<RiskVerify> imageList = new ArrayList<>();
        goodsDTOS.forEach(g -> {
            GoodsSync goodsSync = GoodsAssembler.convertGoodsDTO(g,priceQueryResponse);
            goodsSync.setProviderId(providerId);
            List<RiskVerify> imgList = GoodsAssembler.getImageList(g);
            if(CollectionUtils.isEmpty(imgList)){
                goodsSync.setStatus(GoodsSyncStatusEnum.AUDITED.getKey());
            }
            list.add(goodsSync);
            imageList.addAll(imgList);

        });
        try {
            goodsSyncMapper.batchInsert(list);
            riskVerifyMapper.batchInsert(imageList);
        } catch (Exception e) {
            log.warn("batch insert goods sync error,goods:{}", list, e);
        }
    }



    /**
     * 更新商品价格
     *
     */
    public void syncGoodsPrice(List<String> goodsIds) {
//        Integer maxPage = getMaxPage(queryDTO.getStime(), queryDTO.getEtime());
//        if (maxPage < 1) {
//            log.info("max page is 0");
//            return;
//        }
//        for (int page = 1; page <= maxPage; page++) {
//            BookuuPriceQueryResponse response = queryBookuuPrice(queryDTO.getStime(), queryDTO.getEtime(), page);
//            if (response == null || CollectionUtils.isEmpty(response.getPriceList())) {
//                log.warn("there is no list,stime:{},etime:{},page:{}", queryDTO.getStime(), queryDTO.getEtime(), page);
//                return;
//            }
//            //落表
//            goodsPriceSyncMapper.batchInsert(GoodsAssembler.convertPriceList(response.getPriceList()));
//        }
        //plan b 根据发布商品同步价格
        Long startId = 0L;
        while (true){
            List<GoodsSyncRelation> goodsNo = goodsSyncRelationMapper.list(startId,goodsIds);
            if(CollectionUtils.isEmpty(goodsNo)){
                break;
            }
            startId = goodsNo.stream().mapToLong(GoodsSyncRelation::getId).max().getAsLong();
            BookuuPriceQueryRequest priceQueryRequest = new BookuuPriceQueryRequest();
            priceQueryRequest.setBookID(String.join(",", goodsNo.stream().map(GoodsSyncRelation::getGoodsNo).collect(Collectors.toList())));
            BookuuPriceQueryResponse response = bookuuClient.queryPrice(priceQueryRequest);
            if(response == null || CollectionUtils.isEmpty(response.getPriceList())){
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
    public void syncGoodsStock() {

//        BookuuStockQueryRequest request = new BookuuStockQueryRequest();
//        if(StringUtils.isNotEmpty(queryDTO.getBookId())){
//            request.setBookID(queryDTO.getBookId());
//        }else{
//            request.setStime(queryDTO.getStime());
//            request.setEtime(queryDTO.getEtime());
//            LocalDateTime eTime = LocalDateTime.now();
//            if(StringUtils.isEmpty(request.getEtime())){
//                request.setEtime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(eTime));
//            }
//            if(StringUtils.isEmpty(request.getStime())){
//                LocalDateTime sTime = eTime.minusMinutes(5);
//                request.setStime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(sTime));
//            }
//        }
        //plan b
        Long startId = 0L;
        while (true){
            List<GoodsSyncRelation> goodsNo = goodsSyncRelationMapper.list(startId,null);
            if(CollectionUtils.isEmpty(goodsNo)){
                break;
            }
            startId = goodsNo.stream().mapToLong(GoodsSyncRelation::getId).max().getAsLong();
            BookuuStockQueryRequest request = new BookuuStockQueryRequest();
            request.setBookID(String.join(",", goodsNo.stream().map(GoodsSyncRelation::getGoodsNo).collect(Collectors.toList())));
            BookuuStockQueryResponse response = bookuuClient.queryStock(request);
            if (response == null || CollectionUtils.isEmpty(response.getBookList())) {
                log.info("there is no stock change,queryDTO:{}", request);
                return;
            }
            //落表，根据最后更新时间过滤
            List<GoodsStockSync> list = GoodsAssembler.convertStockList(response.getBookList(),stockSyncSecond);
            if(CollectionUtils.isNotEmpty(list)){
                goodsStockSyncMapper.batchInsert(list);
            }

        }
    }

    public void auditGoods(String goodsNo){
        goodsSyncMapper.updateStatus(goodsNo);
        riskVerifyMapper.updateStatus(goodsNo);
    }

    /**
     * 促销成本价
     * T-1有促销价，更新促销价，
     * T-1无促销价，若T-2有促销价且在有效期内，更新促销价
     * T-1无促销价，若T-2无促销价或不在有效期内，则无促销价
     */
    public void syncSpecialPrice(){
        //促销成本价格
        Integer maxPage = Integer.MAX_VALUE;
        BookuuSpecialPriceQueryRequest specialPriceQueryRequest = new BookuuSpecialPriceQueryRequest();
        specialPriceQueryRequest.setPage(1);
        String sTime = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        specialPriceQueryRequest.setStime(sTime);
        specialPriceQueryRequest.setEtime(sTime);
        while (specialPriceQueryRequest.getPage() < maxPage){
            BookuuSpecialPriceQueryResponse priceQueryResponse = bookuuClient.querySpecialPrice(specialPriceQueryRequest);
            specialPriceQueryRequest.setPage(specialPriceQueryRequest.getPage() +1);
            if(priceQueryResponse != null && priceQueryResponse.getMaxPages() != null){
                maxPage = priceQueryResponse.getMaxPages();
            }
            if(priceQueryResponse != null && CollectionUtils.isEmpty(priceQueryResponse.getBookList())){
                break;
            }
            List<BookuuSpecialPriceQueryResponse.BookuuSpecialPrice> specialPrices = priceQueryResponse.getBookList();
            List<String> bookIds = specialPrices.stream().map(BookuuSpecialPriceQueryResponse.BookuuSpecialPrice::getBookId).collect(Collectors.toList());
            List<String> bookNos = goodsSyncRelationMapper.listByGoodsNos(bookIds);
            if(CollectionUtils.isEmpty(bookNos)){
                log.info("there is no goods to sync special price,bookids:{}",bookIds);
                continue;
            }
            //商城上架的商品才需要同步价格
            List<BookuuSpecialPriceQueryResponse.BookuuSpecialPrice> priceList = specialPrices.stream().filter(p->bookNos.contains(p.getBookId())).collect(Collectors.toList());
            goodsSpecialPriceSyncMapper.updateStatus(priceList.stream().map(BookuuSpecialPriceQueryResponse.BookuuSpecialPrice::getBookId).collect(Collectors.toList()));
            goodsSpecialPriceSyncMapper.batchInsert(GoodsAssembler.convertSpecialPriceList(priceList));

        }
    }

}
