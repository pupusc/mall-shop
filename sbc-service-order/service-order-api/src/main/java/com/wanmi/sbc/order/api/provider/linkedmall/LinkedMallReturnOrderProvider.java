package com.wanmi.sbc.order.api.provider.linkedmall;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.linkedmall.LinkedMallReturnOrderApplyRequest;
import com.wanmi.sbc.order.api.response.returnorder.ReturnReasonListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>linkedMall退单服务接口</p>
 * @Author: daiyitian
 * @Description: 退单服务接口
 * @Date: 2018-12-03 15:40
 */
@FeignClient(value = "${application.order.name}", contextId = "SbcLinkedMallReturnOrderProvider")
public interface LinkedMallReturnOrderProvider {

    /**
     * 查询所有退货原因
     *
     * @return 退货原因列表 {@link ReturnReasonListResponse}
     */
    @PostMapping("/order/${application.order.version}/linked-mall-return/apply")
    BaseResponse apply(@RequestBody @Valid LinkedMallReturnOrderApplyRequest request);

    /**
     * 同步退单状态
     *
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/linked-mall-return/sync-status")
    BaseResponse syncStatus();
}
