package com.wanmi.sbc.customer.api.provider.storeconsumerstatistics;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.storeconsumerstatistics.StoreConsumerStatisticsAddRequest;
import com.wanmi.sbc.customer.api.request.storeconsumerstatistics.StoreConsumerStatisticsModifyRequest;
import com.wanmi.sbc.customer.api.request.storeconsumerstatistics.StoreConsumerStatisticsQueryRequest;
import com.wanmi.sbc.customer.api.response.storeconsumerstatistics.StoreConsumerStatisticsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>店铺客户消费统计表查询服务Provider</p>
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreConsumerStatisticsProvider")
public interface StoreConsumerStatisticsProvider {

    /**
     * 单个查询店铺客户消费统计表API
     *
     * @param storeConsumerStatisticsQueryRequest 单个查询店铺客户消费统计表请求参数 {@link StoreConsumerStatisticsQueryRequest}
     * @return 店铺客户消费统计表详情 {@link StoreConsumerStatisticsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/storeconsumerstatistics/get-by-customerid-storeid")
    BaseResponse<StoreConsumerStatisticsResponse> getByCustomerIdAndStoreId(@RequestBody @Valid StoreConsumerStatisticsQueryRequest storeConsumerStatisticsQueryRequest);

    /**
     * 新增店铺客户消费统计表API
     *
     * @param storeConsumerStatisticsAddRequest 店铺客户消费统计表新增参数结构 {@link StoreConsumerStatisticsAddRequest}
     * @return 新增的店铺客户消费统计表信息 {@link StoreConsumerStatisticsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/storeconsumerstatistics/add")
    BaseResponse<StoreConsumerStatisticsResponse> add(@RequestBody @Valid StoreConsumerStatisticsAddRequest storeConsumerStatisticsAddRequest);

    /**
     * 修改店铺客户消费统计表API
     *
     * @param storeConsumerStatisticsModifyRequest 店铺客户消费统计表修改参数结构 {@link StoreConsumerStatisticsModifyRequest}
     * @return 修改的店铺客户消费统计表信息 {@link StoreConsumerStatisticsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/storeconsumerstatistics/modify")
    BaseResponse<StoreConsumerStatisticsResponse> modify(@RequestBody @Valid StoreConsumerStatisticsModifyRequest storeConsumerStatisticsModifyRequest);

}

