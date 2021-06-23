package com.wanmi.sbc.customer.api.provider.levelrights;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsQueryRequest;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsListResponse;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsPageResponse;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员等级权益表查询服务Provider</p>
 *
 * @author minchen
 * @date 2019-02-21 14:01:26
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerLevelRightsQueryProvider")
public interface CustomerLevelRightsQueryProvider {

    /**
     * 分页查询会员等级权益表API
     *
     * @param customerLevelRightsPageReq 分页请求参数和筛选对象 {@link CustomerLevelRightsQueryRequest}
     * @return 会员等级权益表分页列表信息 {@link CustomerLevelRightsPageResponse}
     * @author minchen
     */
    @PostMapping("/customer/${application.customer.version}/customerlevelrights/page")
    BaseResponse<CustomerLevelRightsPageResponse> page(@RequestBody @Valid CustomerLevelRightsQueryRequest customerLevelRightsPageReq);

    /**
     * 列表查询会员等级权益表API
     *
     * @param customerLevelRightsListReq 列表请求参数和筛选对象 {@link CustomerLevelRightsQueryRequest}
     * @return 会员等级权益表的列表信息 {@link CustomerLevelRightsListResponse}
     * @author minchen
     */
    @PostMapping("/customer/${application.customer.version}/customerlevelrights/list")
    BaseResponse<CustomerLevelRightsListResponse> list(@RequestBody @Valid CustomerLevelRightsQueryRequest customerLevelRightsListReq);

    /**
     * 单个查询会员等级权益表API
     *
     * @param customerLevelRightsByIdRequest 单个查询会员等级权益表请求参数 {@link CustomerLevelRightsQueryRequest}
     * @return 会员等级权益表详情 {@link CustomerLevelRightsResponse}
     * @author minchen
     */
    @PostMapping("/customer/${application.customer.version}/customerlevelrights/get-by-id")
    BaseResponse<CustomerLevelRightsResponse> getById(@RequestBody @Valid CustomerLevelRightsQueryRequest customerLevelRightsByIdRequest);

}

