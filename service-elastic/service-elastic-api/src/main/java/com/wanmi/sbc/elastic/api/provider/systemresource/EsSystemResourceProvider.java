package com.wanmi.sbc.elastic.api.provider.systemresource;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.sensitivewords.EsSensitiveWordsQueryRequest;
import com.wanmi.sbc.elastic.api.request.systemresource.EsSystemResourcePageRequest;
import com.wanmi.sbc.elastic.api.request.systemresource.EsSystemResourceSaveRequest;
import com.wanmi.sbc.elastic.api.response.systemresource.EsSystemRessourcePageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/14 10:19
 * @description <p> </p>
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsSystemResourceProvider")
public interface EsSystemResourceProvider {


    /**
     * 分页查询平台素材资源API
     *
     * @author lq
     * @param request 分页请求参数和筛选对象 {@link EsSystemResourcePageRequest}
     * @return 平台素材资源分页列表信息
     */
    @PostMapping("/elastic/${application.elastic.version}/systemresource/add")
    BaseResponse add(@RequestBody @Valid EsSystemResourceSaveRequest request);


    /**
     * 初始化素材资源列表数据
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/systemresource/init")
    BaseResponse init(@RequestBody @Validated EsSystemResourcePageRequest request);

}
