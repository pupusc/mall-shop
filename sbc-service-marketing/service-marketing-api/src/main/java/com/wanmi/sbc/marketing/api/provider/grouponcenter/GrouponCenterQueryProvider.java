package com.wanmi.sbc.marketing.api.provider.grouponcenter;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.grouponcenter.GrouponCenterListRequest;
import com.wanmi.sbc.marketing.api.response.grouponcenter.GrouponCenterListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;


/**
 * <p>H5-拼团活动首页列表查询服务Provider</p>
 *
 * @author chenli
 * @date 2019-05-21 14:02:38
 */
@FeignClient(value = "${application.marketing.name}", contextId = "GrouponCenterQueryProvider")
public interface GrouponCenterQueryProvider {

    /**
     * 拼团活动首页列表查询列表API
     *
     * @param listRequest 列表请求参数和筛选对象 {@link GrouponCenterListRequest}
     * @return 拼团活动首页列表查询列表结果 {@link GrouponCenterListResponse}
     * @author groupon
     */
    @PostMapping("/marketing/${application.marketing.version}/groupon/center/list")
    BaseResponse<GrouponCenterListResponse> list(@RequestBody @Valid GrouponCenterListRequest listRequest);
 }

