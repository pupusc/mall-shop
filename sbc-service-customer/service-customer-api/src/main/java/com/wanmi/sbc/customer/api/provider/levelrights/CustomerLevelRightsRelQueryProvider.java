package com.wanmi.sbc.customer.api.provider.levelrights;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsRelRequest;
import com.wanmi.sbc.customer.api.response.levelrights.CustomerLevelRightsRelListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员等级与权益关联表查询服务</p>
 *
 * @author yang
 * @since 2019/2/27
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerLevelRightsRelQueryProvider")
public interface CustomerLevelRightsRelQueryProvider {

    @PostMapping("/customer/${application.customer.version}/customerlevelrightsrel/list")
    BaseResponse<CustomerLevelRightsRelListResponse> listByRightsId(@RequestBody @Valid CustomerLevelRightsRelRequest customerLevelRightsRelQueryRequest);
}
