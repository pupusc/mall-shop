package com.wanmi.sbc.elastic.api.provider.sensitivewords;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.customer.EsDistributionCustomerPageRequest;
import com.wanmi.sbc.elastic.api.request.sensitivewords.EsSensitiveWordsQueryRequest;
import com.wanmi.sbc.elastic.api.request.sensitivewords.EsSensitiveWordsSaveRequest;
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
@FeignClient(value = "${application.elastic.name}", contextId = "EsSensitiveWordsProvider")
@Validated
public interface EsSensitiveWordsProvider {


    /**
     * 新增
     *
     * @param request  {@link EsSensitiveWordsSaveRequest}
     */
    @PostMapping("/elastic/${application.elastic.version}/sensitivewords/add")
    BaseResponse addSensitiveWords(@RequestBody @Valid EsSensitiveWordsSaveRequest request);



    /**
     * 初始化敏感词库列表数据
     * @param request
     * @return
     */
    @PostMapping("/elastic/${application.elastic.version}/sensitivewords/init")
    BaseResponse init(@RequestBody @Valid EsSensitiveWordsQueryRequest request);


}
