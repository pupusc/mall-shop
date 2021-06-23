package com.wanmi.sbc.goods.adjust;

import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoAdjustPriceRequest;
import com.wanmi.sbc.goods.adjust.request.PriceAdjustConfirmRequest;
import com.wanmi.sbc.goods.api.provider.priceadjustmentrecorddetail.PriceAdjustmentRecordDetailProvider;
import com.wanmi.sbc.goods.api.request.adjustprice.AdjustPriceExecuteFailRequest;
import com.wanmi.sbc.goods.api.request.adjustprice.AdjustPriceExecuteRequest;
import com.wanmi.sbc.goods.api.response.price.adjustment.AdjustPriceExecuteResponse;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentResult;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PriceAdjustExcuteService {

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private PriceAdjustmentRecordDetailProvider detailProvider;

    /**
     * 执行调价
     * @param request
     * @param storeId
     */
    @GlobalTransactional
    @Async
    public void excuteAdjustPrice(PriceAdjustConfirmRequest request, Long storeId){
        try {
            //更新数据库
            AdjustPriceExecuteRequest executeRequest = new AdjustPriceExecuteRequest();
            executeRequest.setAdjustNo(request.getAdjustNo());
            executeRequest.setStoreId(storeId);
            AdjustPriceExecuteResponse response = detailProvider.adjustPriceExecute(executeRequest).getContext();
            if(CollectionUtils.isNotEmpty(response.getSkuIds())) {
                EsGoodsInfoAdjustPriceRequest adjustPriceRequest = EsGoodsInfoAdjustPriceRequest.builder()
                        .goodsInfoIds(response.getSkuIds())
                        .type(response.getType()).build();
                //同步es
                esGoodsInfoElasticProvider.adjustPrice(adjustPriceRequest);
            }
        } catch (Exception e) {
            log.error("调价失败，" + e);
            AdjustPriceExecuteFailRequest failRequest = AdjustPriceExecuteFailRequest.builder()
                    .adjustNo(request.getAdjustNo())
                    .result(PriceAdjustmentResult.FAIL)
                    .failReason("系统异常").build();
            detailProvider.adjustPriceExecuteFail(failRequest);
        }
    }
}
