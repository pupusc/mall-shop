package com.wanmi.sbc.customer.provider.impl.storeconsumerstatistics;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storeconsumerstatistics.StoreConsumerStatisticsProvider;
import com.wanmi.sbc.customer.api.request.storeconsumerstatistics.StoreConsumerStatisticsAddRequest;
import com.wanmi.sbc.customer.api.request.storeconsumerstatistics.StoreConsumerStatisticsModifyRequest;
import com.wanmi.sbc.customer.api.request.storeconsumerstatistics.StoreConsumerStatisticsQueryRequest;
import com.wanmi.sbc.customer.api.response.storeconsumerstatistics.StoreConsumerStatisticsResponse;
import com.wanmi.sbc.customer.storestatistics.model.root.StoreConsumerStatistics;
import com.wanmi.sbc.customer.storestatistics.service.StoreConsumerStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>店铺客户消费统计表查询服务接口实现</p>
 */
@RestController
@Validated
public class StoreConsumerStatisticsController implements StoreConsumerStatisticsProvider {
    @Autowired
    private StoreConsumerStatisticsService storeConsumerStatisticsService;

    @Override
    public BaseResponse<StoreConsumerStatisticsResponse> getByCustomerIdAndStoreId(@RequestBody @Valid StoreConsumerStatisticsQueryRequest request) {
        StoreConsumerStatistics storeConsumerStatistics = storeConsumerStatisticsService.getByCustomerIdAndStoreId(request.getCustomerId(), request.getStoreId());
        return BaseResponse.success(new StoreConsumerStatisticsResponse(storeConsumerStatisticsService.wrapperVo(storeConsumerStatistics)));
    }

    @Override
    public BaseResponse<StoreConsumerStatisticsResponse> add(@RequestBody @Valid StoreConsumerStatisticsAddRequest storeConsumerStatisticsAddRequest) {
        StoreConsumerStatistics storeConsumerStatistics = new StoreConsumerStatistics();
        KsBeanUtil.copyPropertiesThird(storeConsumerStatisticsAddRequest, storeConsumerStatistics);
        return BaseResponse.success(new StoreConsumerStatisticsResponse(
                storeConsumerStatisticsService.wrapperVo(storeConsumerStatisticsService.add(storeConsumerStatistics))));
    }

    @Override
    public BaseResponse<StoreConsumerStatisticsResponse> modify(@RequestBody @Valid StoreConsumerStatisticsModifyRequest storeConsumerStatisticsModifyRequest) {
        StoreConsumerStatistics storeConsumerStatistics = new StoreConsumerStatistics();
        KsBeanUtil.copyPropertiesThird(storeConsumerStatisticsModifyRequest, storeConsumerStatistics);
        return BaseResponse.success(new StoreConsumerStatisticsResponse(
                storeConsumerStatisticsService.wrapperVo(storeConsumerStatisticsService.modify(storeConsumerStatistics))));
    }

}

