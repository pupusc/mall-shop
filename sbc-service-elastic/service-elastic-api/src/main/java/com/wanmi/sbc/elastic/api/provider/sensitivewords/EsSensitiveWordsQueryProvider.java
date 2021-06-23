package com.wanmi.sbc.elastic.api.provider.sensitivewords;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.sensitivewords.EsSensitiveWordsQueryRequest;
import com.wanmi.sbc.elastic.api.response.sensitivewords.EsSensitiveWordsPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author houshuai
 * @date 2020/12/11 16:02
 * @description <p> </p>
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsSensitiveWordsQueryProvider")
@Validated
public interface EsSensitiveWordsQueryProvider {


    /**
     * 分页查询API
     *
     * @param request 分页请求参数和筛选对象 {@link EsSensitiveWordsPageResponse}
     * @return 分页列表信息 {@link EsSensitiveWordsQueryRequest}
     */
    @PostMapping("/elastic/${application.elastic.version}/sensitivewords/page")
    BaseResponse<EsSensitiveWordsPageResponse> page(@RequestBody @Valid EsSensitiveWordsQueryRequest request);
}
