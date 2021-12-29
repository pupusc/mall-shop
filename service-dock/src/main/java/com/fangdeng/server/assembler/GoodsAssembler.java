package com.fangdeng.server.assembler;

import com.fangdeng.server.client.response.bookuu.BookuuPriceQueryResponse;
import com.fangdeng.server.client.response.bookuu.BookuuStockQueryResponse;
import com.fangdeng.server.dto.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsAssembler {




    public static GoodsSyncDTO convertGoodsDTO(BookuuGoodsDTO goodsDTO,BookuuPriceQueryResponse priceResponse) {
        GoodsSyncDTO goodsSyncDTO =  GoodsSyncDTO.builder().goodsNo(goodsDTO.getBookId())
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
            return goodsSyncDTO;
        }
        goodsSyncDTO.setBasePrice(priceResponse.getPriceList().stream().filter(p->p.getBookID().equals(goodsDTO.getBookId())).findFirst().get().getPrice());

        return goodsSyncDTO;
    }

    public static List<GoodsPriceSyncDTO> convertPriceList(List<BookuuPriceQueryResponse.BookuuPrice> priceList) {
        List<GoodsPriceSyncDTO> list = new ArrayList<>(priceList.size());
        priceList.forEach(p -> {
            list.add(GoodsPriceSyncDTO.builder().goodsNo(p.getBookID()).price(p.getPrice()).sellPrice(p.getSellPrice()).build());
        });
        return list;
    }

    public static List<GoodsStockSyncDTO> convertStockList(List<BookuuStockQueryResponse.BookuuStock> stockList,Long stockSyncSecond) {
        List<GoodsStockSyncDTO> list = new ArrayList<>(stockList.size());
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now().minusSeconds(stockSyncSecond);
        stockList.forEach(p -> {
            if(LocalDateTime.parse(p.getZjtbkcsj(),df).compareTo(now) >0) {
                list.add(GoodsStockSyncDTO.builder().goodsNo(p.getBookId()).stock(p.getStock()).stockChangeTime(LocalDateTime.parse(p.getZjtbkcsj(),df)).build());
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

}
