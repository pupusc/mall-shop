package com.wanmi.sbc.order.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.distribution.ConsumeRecordAddRequest;
import com.wanmi.sbc.order.api.request.distribution.ConsumeRecordModifyRequest;
import com.wanmi.sbc.order.api.response.distribution.ConsumeRecordAddResponse;
import com.wanmi.sbc.order.api.response.distribution.ConsumeRecordModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Description: 消费记录Provider
 * @Autho qiaokang
 * @Date：2019-03-05 18:40:30
 */
@FeignClient(value = "${application.order.name}", contextId = "ConsumeRecordProvider")
public interface ConsumeRecordProvider {

    /**
     * 新增消费记录
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/distribution/add-consume-record")
    BaseResponse<ConsumeRecordAddResponse> addConsumeRecord(@RequestBody @Valid ConsumeRecordAddRequest request);

    /**
     * 更新消费记录
     * @param request
     * @return
     */
    @PostMapping("/order/${application.order.version}/distribution/modify-consume-record")
    BaseResponse<ConsumeRecordModifyResponse> modifyConsumeRecord(@RequestBody @Valid ConsumeRecordModifyRequest request);

}
