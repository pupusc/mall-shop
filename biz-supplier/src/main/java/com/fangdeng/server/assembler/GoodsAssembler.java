package com.fangdeng.server.assembler;

import com.fangdeng.server.client.request.bookuu.BookuuStockQueryRequest;
import com.fangdeng.server.client.response.bookuu.BookuuPriceQueryResponse;
import com.fangdeng.server.client.response.bookuu.BookuuStockQueryResponse;
import com.fangdeng.server.dto.BookuuGoodsDTO;
import com.fangdeng.server.dto.GoodsPriceSyncDTO;
import com.fangdeng.server.dto.GoodsStockSyncDTO;
import com.fangdeng.server.dto.GoodsSyncDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsAssembler {

    public static GoodsSyncDTO convertGoodsDTO(BookuuGoodsDTO goodsDTO) {
        return GoodsSyncDTO.builder().goodsNo(goodsDTO.getBookId())
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
                .status((byte) 1)
                // todo 改配置
                .providerId(123458074L)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
    }

    public static List<GoodsPriceSyncDTO> convertPriceList(List<BookuuPriceQueryResponse.BookuuPrice> priceList) {
        List<GoodsPriceSyncDTO> list = new ArrayList<>(priceList.size());
        priceList.forEach(p -> {
            list.add(GoodsPriceSyncDTO.builder().goodsNo(p.getBookID()).price(p.getPrice()).sellPrice(p.getSellPrice()).build());
        });
        return list;
    }

    public static List<GoodsStockSyncDTO> convertStockList(List<BookuuStockQueryResponse.BookuuStock> stockList) {
        List<GoodsStockSyncDTO> list = new ArrayList<>(stockList.size());
        stockList.forEach(p -> {
            list.add(GoodsStockSyncDTO.builder().goodsNo(p.getBookId()).stock(p.getStock()).stockChangeTime(p.getZjtbkcsj()).build());
        });
        return list;
    }

}
