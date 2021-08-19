package com.wanmi.sbc.crm.api.provider.customgrouprel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.crm.api.request.crmgroup.CrmGroupRequest;
import com.wanmi.sbc.crm.api.request.customgrouprel.CustomGroupRelRequest;
import com.wanmi.sbc.crm.bean.vo.CustomGroupRelVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-15
 * \* Time: 14:44
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@FeignClient(value = "${application.crm.name}",contextId = "CustomGroupRelProvide")
public interface CustomGroupRelProvide {

    @PostMapping("/crm/${application.crm.version}/customgrouprel/queryListByCustomerId")
    BaseResponse<List<CustomGroupRelVo>> queryListByCustomerId(@RequestBody @Valid CustomGroupRelRequest relRequest);

    @PostMapping("/crm/${application.crm.version}/customgrouprel/queryListByGroupIds")
    BaseResponse<List<String>> queryListByGroupIds(@RequestBody @Valid CrmGroupRequest request);
}
