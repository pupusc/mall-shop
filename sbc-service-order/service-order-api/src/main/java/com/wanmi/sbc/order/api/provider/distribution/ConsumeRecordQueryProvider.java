package com.wanmi.sbc.order.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.base.QueryByIdListRequest;
import com.wanmi.sbc.common.base.QueryByIdRequest;
import com.wanmi.sbc.order.api.request.distribution.PageByCustomerIdRequest;
import com.wanmi.sbc.order.api.response.distribution.CountConsumeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Description: 消费记录Provider
 * @Autho qiaokang
 * @Date：2019-03-05 18:40:30
 */
@FeignClient(value = "${application.order.name}", contextId = "ConsumeRecordQueryProvider")
public interface ConsumeRecordQueryProvider {

    /**
     * 统计分销员的顾客
     */
    @PostMapping("/order/${application.order.version}/distribution/count-by-distribution-customer-id")
    BaseResponse<Integer> countByDistributionCustomerId(@RequestBody @Valid QueryByIdRequest request);

    /**
     * 统计累计的有效消费金额，订单数，
     */
    @PostMapping("/order/${application.order.version}/distribution/count-valid-consume")
    BaseResponse<CountConsumeResponse> countValidConsume(@RequestBody @Valid QueryByIdListRequest request);

    /**
     * 统计累计的消费金额，订单数，
     */
    @PostMapping("/order/${application.order.version}/distribution/count-consume")
    BaseResponse<CountConsumeResponse> countConsume(@RequestBody @Valid QueryByIdListRequest request);

    /**
     * 分页，以客户id分组
     */
    @PostMapping("/order/${application.order.version}/distribution/page-by-customer-id")
    BaseResponse<MicroServicePage> pageByCustomerId(@RequestBody @Valid PageByCustomerIdRequest request);

}
