package com.wanmi.sbc.order.api.provider.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.trade.*;
import com.wanmi.sbc.order.api.response.trade.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Description:
 * @Date: 2018-12-03 17:24
 */
@FeignClient(value = "${application.order.name}", contextId = "GrouponInstanceQueryProvider")
public interface GrouponInstanceQueryProvider {


    /**
     * 条件分页
     * @param grouponInstancePageRequest 带参分页参数 {@link TradePageCriteriaRequest}
     * @return
     */
    @PostMapping("/order/${application.order.version}/groupon/instance/page-criteria")
    BaseResponse<GrouponInstancePageResponse> pageCriteria(@RequestBody @Valid GrouponInstancePageRequest grouponInstancePageRequest );


    /**
     * 条件分页
     * 返回结果包装用户信息
     * @param grouponInstancePageRequest 带参分页参数 {@link TradePageCriteriaRequest}
     * @return
     */
    @PostMapping("/order/${application.order.version}/groupon/instance/page-criteria-with-customer-info")
    BaseResponse<GrouponInstancePageWithCustomerInfoResponse> pageCriteriaWithCustomerInfoResponse(@RequestBody @Valid GrouponInstancePageRequest grouponInstancePageRequest );

    /**
     * 根据groupoNo，查询团活动信息
     */
    @PostMapping("/order/${application.order.version}/groupon/instance/groupon-no")
    BaseResponse<GrouponInstanceByGrouponNoResponse> detailByGrouponNo(@RequestBody @Valid GrouponInstanceByGrouponNoRequest
                                                                     request);

    @PostMapping("/order/${application.order.version}/groupon/instance/latest-group")
    BaseResponse<GrouponInstanceByActivityIdResponse>getGrouponLatestInstanceByActivityId(@RequestBody @Valid GrouponInstancePageRequest request);

    }


