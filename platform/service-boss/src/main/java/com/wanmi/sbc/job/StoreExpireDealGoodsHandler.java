package com.wanmi.sbc.job;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.customer.api.provider.store.StoreProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.ListStoreRequest;
import com.wanmi.sbc.customer.api.request.store.StoreSwitchBatchRequest;
import com.wanmi.sbc.customer.api.response.store.ListStoreResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoRequest;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商店铺过期定时任务
 *
 * @author liutao 2019-02-27
 */
@JobHandler(value = "StoreExpireDealGoodsHandler")
@Component
@Slf4j
public class StoreExpireDealGoodsHandler extends IJobHandler {

    @Autowired
    private StoreQueryProvider storeQueryProvider;

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private StoreProvider storeProvider;

    private static final String storeClosedReason = "店铺过期自动关店";

    @Override
    public ReturnT<String> execute(String param) throws Exception {
//        Integer total = NumberUtils.INTEGER_ZERO;
//        XxlJobLogger.log("店铺过期定时任务执行开始 " + LocalDateTime.now());
//
//        ListStoreResponse listStoreResponse = storeQueryProvider.listStore(ListStoreRequest.builder()
//                .storeType(StoreType.SUPPLIER)
//                .delFlag(DeleteFlag.NO)
//                .auditState(CheckState.CHECKED).build()).getContext();
//
//        if (listStoreResponse != null) {
//            List<StoreVO> storeList = listStoreResponse.getStoreVOList();
//            LocalDateTime now = LocalDateTime.now();
//            List<StoreVO> expiredStores = storeList.stream()
//                    .filter(store -> !((now.isBefore(store.getContractEndDate()) || now.isEqual(store.getContractEndDate()))
//                            && (now.isAfter(store.getContractStartDate()) || now.isEqual(store.getContractStartDate()))))
//                    .collect(Collectors.toList());
//            if (CollectionUtils.isNotEmpty(expiredStores)) {
//                List<Long> storeIds = expiredStores.stream().map(StoreVO::getStoreId).collect(Collectors.toList());
//                storeIds.forEach(id -> esGoodsInfoElasticProvider.initEsGoodsInfo(EsGoodsInfoRequest.builder().storeId(id).build()));
//                XxlJobLogger.log("处理店铺：" + storeIds);
//            }
//            total = expiredStores.size();
//
//        }
//        XxlJobLogger.log("店铺过期定时任务执行结束 " + LocalDateTime.now() + ",处理总数为：" + total);
        return SUCCESS;
    }

}
