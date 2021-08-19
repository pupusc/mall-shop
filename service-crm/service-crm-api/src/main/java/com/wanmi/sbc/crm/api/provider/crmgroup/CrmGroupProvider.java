package com.wanmi.sbc.crm.api.provider.crmgroup;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.crmgroup.CrmGroupRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-9
 * \* Time: 16:10
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@FeignClient(value = "${application.crm.name}",contextId = "CrmGroupProvider")
public interface CrmGroupProvider {

    @PostMapping("/crm/${application.crm.version}/crmgroup/queryCustomerPhone")
    BaseResponse<List<String>> queryCustomerPhone(@RequestBody @Valid CrmGroupRequest request);

    @PostMapping("/crm/${application.crm.version}/crmgroup/queryCustomerPhoneCount")
    BaseResponse<Long> queryCustomerPhoneCount(@RequestBody @Valid CrmGroupRequest request);

    /**
     * 根据人群id查询相关会员
     * @param request
     * @return
     */
    @PostMapping("/crm/${application.crm.version}/customgrouprel/query-list-by-group-id")
    BaseResponse<List<String>> queryListByGroupId(@RequestBody @Valid CrmGroupRequest request);

    /**
     * 根据人群id统计信息
     * @param request
     * @return
     */
    @PostMapping("/crm/${application.crm.version}/crmgroup/countby-group-ids")
    BaseResponse<Long> countByGroupIds(@RequestBody @Valid CrmGroupRequest request);

}
