package com.wanmi.sbc.order.api.provider.yzorder;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.yzorder.YzOrderAddBatchRequest;
import com.wanmi.sbc.order.api.request.yzorder.YzOrderPageQueryRequest;
import com.wanmi.sbc.order.api.response.yzorder.YzOrderListResponse;
import com.wanmi.sbc.order.api.response.yzorder.YzOrderPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.order.name}", contextId = "YzOrderProvider")
public interface YzOrderProvider {

    /**
     * 批量保存
     *
     * @param request 有赞订单 操作信息 {@link YzOrderAddBatchRequest}
     * @return BaseResponse
     */
    @PostMapping("/order/${application.order.version}/yzorder/add-batch")
    BaseResponse addBatch(@RequestBody @Valid YzOrderAddBatchRequest request);

    /**
     * 分页
     *
     * @param request 有赞订单 操作信息 {@link YzOrderPageQueryRequest}
     * @return BaseResponse
     */
    @PostMapping("/order/${application.order.version}/yzorder/page")
    BaseResponse<YzOrderPageResponse> page(@RequestBody @Valid YzOrderPageQueryRequest request);

    /**
     * 批量入库
     *
     * @param request 有赞订单 操作信息 {@link YzOrderPageQueryRequest}
     * @return BaseResponse
     */
    @PostMapping("/order/${application.order.version}/yzorder/convert-type")
    BaseResponse convertType(@RequestBody @Valid YzOrderPageQueryRequest request);

    /**
     * 会员对比
     *
     *
     * @return BaseResponse
     */
    @PostMapping("/order/${application.order.version}/yzorder/compare")
    BaseResponse compare(@RequestBody @Valid YzOrderPageQueryRequest request);

    /**
     * 获取订单id
     *
     *
     * @return BaseResponse
     */
    @PostMapping("/order/${application.order.version}/yzorder/get-tids")
    BaseResponse<YzOrderListResponse> getYzOrderIdList(@RequestBody @Valid YzOrderPageQueryRequest request);

    /**
     * 会员补偿入库
     *
     *
     * @return BaseResponse
     */
    @PostMapping("/order/${application.order.version}/yzorder/convert")
    BaseResponse convert(@RequestBody @Valid YzOrderPageQueryRequest request);

}
