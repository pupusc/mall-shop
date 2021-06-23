package com.wanmi.sbc.elastic.api.provider.groupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.groupon.EsGrouponActivityPageRequest;
import com.wanmi.sbc.elastic.api.response.groupon.EsGrouponActivityPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>拼团分类信息表查询服务Provider</p>
 *
 * @author groupon
 * @date 2019-05-15 14:13:58
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsGrouponActivityQueryProvider")
public interface EsGrouponActivityQueryProvider {


    @PostMapping("/elastic/${application.elastic.version}/groupon/activity/page")
    BaseResponse<EsGrouponActivityPageResponse> page(@RequestBody @Valid EsGrouponActivityPageRequest request);
}

