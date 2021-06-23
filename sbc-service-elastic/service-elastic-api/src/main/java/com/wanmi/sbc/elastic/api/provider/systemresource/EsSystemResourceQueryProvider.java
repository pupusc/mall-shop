package com.wanmi.sbc.elastic.api.provider.systemresource;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.systemresource.EsSystemResourcePageRequest;
import com.wanmi.sbc.elastic.api.response.systemresource.EsSystemRessourcePageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/14 10:19
 * @description <p> </p>
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsSystemResourceQueryProvider")
public interface EsSystemResourceQueryProvider {


    /**
     * 分页查询平台素材资源API
     *
     * @author lq
     * @param systemResourcePageReq 分页请求参数和筛选对象 {@link EsSystemResourcePageRequest}
     * @return 平台素材资源分页列表信息 {@link EsSystemRessourcePageResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/systemresource/page")
    BaseResponse<EsSystemRessourcePageResponse> page(@RequestBody @Valid EsSystemResourcePageRequest systemResourcePageReq);

}
