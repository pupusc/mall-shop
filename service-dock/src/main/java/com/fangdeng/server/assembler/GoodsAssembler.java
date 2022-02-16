package com.fangdeng.server.assembler;

import com.fangdeng.server.client.response.bookuu.BookuuPriceQueryResponse;
import com.fangdeng.server.client.response.bookuu.BookuuSpecialPriceQueryResponse;
import com.fangdeng.server.client.response.bookuu.BookuuStockQueryResponse;
import com.fangdeng.server.dto.*;
import com.fangdeng.server.entity.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsAssembler {




    public static GoodsSync convertGoodsDTO(BookuuGoodsDTO goodsDTO, BookuuPriceQueryResponse priceResponse) {
        GoodsSync goodsSync =  GoodsSync.builder().goodsNo(goodsDTO.getBookId())
                .goodsSupplierType((byte) 1)
                .isbn(goodsDTO.getIsbn())
                .author(goodsDTO.getAuthor())
                .authorDesc(goodsDTO.getZzjj())
                .basePrice(goodsDTO.getBasePrice())
                .salePrice(goodsDTO.getPrice())
                .price(goodsDTO.getPricing())
                .bottomUrl(goodsDTO.getFd())
                .category(goodsDTO.getCategory().longValue())
                .qty(goodsDTO.getStore())
                .editionNumber(goodsDTO.getBC())
                .printNumber(goodsDTO.getYC())
                .pulicateDate(goodsDTO.getPublication())
                .printDate(goodsDTO.getPrinted())
                .publishName(goodsDTO.getPublishing())
                .title(goodsDTO.getTitle())
                .format(goodsDTO.getFormat())
                .pageNumber(goodsDTO.getPage())
                .guide(goodsDTO.getGuide())
                .content(goodsDTO.getNrty())
                .wonderfulContent(goodsDTO.getJcy())
                .recommend(goodsDTO.getXy())
                .diretory(goodsDTO.getDirectory())
                .imageUrl(goodsDTO.getPicurl())
                .largeImageUrl(goodsDTO.getPicurllarge())
                .copyrightUrl(goodsDTO.getBcy())
                .status(1)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .detailImageUrl(goodsDTO.getXqt())
                .build();
        //设置成本价
        if(priceResponse ==null || CollectionUtils.isEmpty(priceResponse.getPriceList()) || !priceResponse.getPriceList().stream().anyMatch(p->p.getBookID().equals(goodsDTO.getBookId()))){
            return goodsSync;
        }
        goodsSync.setBasePrice(priceResponse.getPriceList().stream().filter(p->p.getBookID().equals(goodsDTO.getBookId())).findFirst().get().getPrice());

        return goodsSync;
    }

    public static List<GoodsPriceSync> convertPriceList(List<BookuuPriceQueryResponse.BookuuPrice> priceList) {
        List<GoodsPriceSync> list = new ArrayList<>(priceList.size());
        priceList.forEach(p -> {
            list.add(GoodsPriceSync.builder().goodsNo(p.getBookID()).price(p.getPrice()).sellPrice(p.getSellPrice()).build());
        });
        return list;
    }

    public static List<GoodsStockSync> convertStockList(List<BookuuStockQueryResponse.BookuuStock> stockList, Long stockSyncSecond) {
        List<GoodsStockSync> list = new ArrayList<>(stockList.size());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now().minusSeconds(stockSyncSecond);
        stockList.forEach(p -> {
            if(LocalDateTime.parse(p.getZjtbkcsj(),df).compareTo(now) >0) {
                list.add(GoodsStockSync.builder().goodsNo(p.getBookId()).stock(p.getStock()).stockChangeTime(LocalDateTime.parse(p.getZjtbkcsj(),df)).build());
            }
        });
        return list;
    }

    public static List<RiskVerify> getImageList(BookuuGoodsDTO goodsDTO){
        List<RiskVerify> list= new ArrayList<>();
        if(StringUtils.isNotEmpty(goodsDTO.getPicurllarge())){
            String[] imgs = goodsDTO.getPicurllarge().split("\\|");
            if(imgs!=null && imgs.length >0){
                for(int i=0;i<imgs.length;i++){
                    String url = i== 0? imgs[i] :("http://images.bookuu.com"+imgs[i]);
                    list.add(RiskVerify.builder().imageUrl(url).goodsNo(goodsDTO.getBookId()).verifyType(1).errorMsg("").status(0).build());
                }
            }
        }
        if(StringUtils.isNotEmpty(goodsDTO.getXqt())){
            String[] imgs = goodsDTO.getXqt().split("\\|");
            if(imgs!=null && imgs.length >0){
                for(int i=0;i<imgs.length;i++){
                    String url = "http://images.bookuu.com"+imgs[i];
                    list.add(RiskVerify.builder().imageUrl(url).goodsNo(goodsDTO.getBookId()).verifyType(2).errorMsg("").status(0).build());
                }
            }
        }
        return list;
    }

    public static List<GoodsSpecialPriceSync> convertSpecialPriceList(List<BookuuSpecialPriceQueryResponse.BookuuSpecialPrice> priceList) {
        List<GoodsSpecialPriceSync> list = new ArrayList<>(priceList.size());
        priceList.forEach(p -> {
            try{
                GoodsSpecialPriceSync  sync = GoodsSpecialPriceSync.builder().goodsNo(p.getBookId()).specialPrice(p.getSpecialPrice()).build();
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                sync.setStartTime(LocalDateTime.parse(p.getStartTime(), df));
                sync.setEndTime(LocalDateTime.parse(p.getEndTime(), df));
                list.add(sync);
            }catch (Exception e){

            }
        });
        return list;
    }

}
