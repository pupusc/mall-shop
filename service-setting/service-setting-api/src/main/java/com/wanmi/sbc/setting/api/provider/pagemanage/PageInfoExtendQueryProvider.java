package com.wanmi.sbc.setting.api.provider.pagemanage;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.request.pagemanage.PageInfoExtendByIdRequest;
import com.wanmi.sbc.setting.api.request.pagemanage.PageInfoExtendByPageIdRequest;
import com.wanmi.sbc.setting.api.response.pagemanage.PageInfoExtendByIdResponse;
import com.wanmi.sbc.setting.api.response.presetsearch.PresetSearchTermsQueryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>页面投放查询接口</p>
 * @author dyt
 * @date 2020-04-16
 */
@FeignClient(value = "${application.setting.name}", contextId = "PageInfoExtendQueryProvider")
public interface PageInfoExtendQueryProvider {

    /**
     * 查询预置搜索词
     */
    @PostMapping("/setting/${application.setting.version}/page-info-extend/find-by-id")
    BaseResponse<PageInfoExtendByIdResponse> findById(@RequestBody PageInfoExtendByIdRequest request);

    /**
     * 查询招募会员页面投放信息
     */
    @PostMapping("/setting/${application.setting.version}/page-info-extend/find-recruiting-customers-by-id")
    BaseResponse<PageInfoExtendByIdResponse> findRecruitingCustomersById(@RequestBody PageInfoExtendByPageIdRequest request);
}
