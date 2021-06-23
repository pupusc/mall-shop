package com.wanmi.sbc.goods.standard.service;

import com.aliyuncs.linkedmall.model.v20180116.QueryItemInventoryResponse;
import com.wanmi.sbc.goods.bean.enums.GoodsSource;
import com.wanmi.sbc.goods.bean.vo.StandardSkuVO;
import com.wanmi.sbc.goods.standard.model.root.StandardGoods;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import com.wanmi.sbc.goods.standardspec.model.root.StandardSkuSpecDetailRel;
import com.wanmi.sbc.goods.standardspec.repository.StandardSkuSpecDetailRelRepository;
import com.wanmi.sbc.linkedmall.api.provider.stock.LinkedMallStockQueryProvider;
import com.wanmi.sbc.linkedmall.api.request.stock.GoodsStockGetRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;

/**
 * LM商品库服务
 * Created by daiyitian on 2017/4/11.
 */
@Service
public class LmStandardService {

    @Autowired
    private LinkedMallStockQueryProvider linkedMallStockQueryProvider;


    /**
     * 填充LM商品sku的库存
     *
     * @param standardSkuList 商品库SKu列表
     */
    public void fillLmStock(List<StandardSkuVO> standardSkuList) {
        //如果是linkedmall商品，实时查库存
        List<Long> itemIds = standardSkuList.stream()
                .filter(v -> Integer.valueOf(GoodsSource.LINKED_MALL.toValue()).equals(v.getGoodsSource()) && !StringUtils.isNotBlank(v.getThirdPlatformSpuId()))
                .map(v -> Long.valueOf(v.getThirdPlatformSpuId()))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(itemIds)) {
            return;
        }
        List<QueryItemInventoryResponse.Item> stocks = linkedMallStockQueryProvider.batchGoodsStockByDivisionCode(new GoodsStockGetRequest(itemIds, "0", null)).getContext();
        if (CollectionUtils.isNotEmpty(stocks)) {
            for (StandardSkuVO standardSku : standardSkuList) {
                for (QueryItemInventoryResponse.Item spuStock : stocks) {
                    Optional<QueryItemInventoryResponse.Item.Sku> stock = spuStock.getSkuList().stream()
                            .filter(v -> String.valueOf(spuStock.getItemId()).equals(standardSku.getThirdPlatformSpuId())
                                    && String.valueOf(v.getSkuId()).equals(standardSku.getThirdPlatformSkuId()))
                            .findFirst();
                    stock.ifPresent(sku -> standardSku.setStock(sku.getInventory().getQuantity()));
                }
            }
        }
    }
}
