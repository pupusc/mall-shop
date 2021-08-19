package com.wanmi.sbc.order.api.provider.growthvalue;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.growthvalue.OrderGrowthValueTempQueryRequest;
import com.wanmi.sbc.order.api.response.growthvalue.OrderGrowthValueTempListResponse;
import com.wanmi.sbc.order.api.response.growthvalue.OrderGrowthValueTempPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员权益处理订单成长值 临时表查询服务Provider</p>
 */
@FeignClient(value = "${application.order.name}", contextId = "OrderGrowthValueTempProvider")
public interface OrderGrowthValueTempProvider {

    /**
     * 分页查询会员权益处理订单成长值 临时表API
     *
     * @param orderGrowthValueTempPageReq 分页请求参数和筛选对象 {@link OrderGrowthValueTempQueryRequest}
     * @return 会员权益处理订单成长值 临时表分页列表信息 {@link OrderGrowthValueTempPageResponse}
     */
    @PostMapping("/order/${application.order.version}/ordergrowthvaluetemp/page")
    BaseResponse<OrderGrowthValueTempPageResponse> page(@RequestBody @Valid OrderGrowthValueTempQueryRequest orderGrowthValueTempPageReq);

    /**
     * 列表查询会员权益处理订单成长值 临时表API
     *
     * @param orderGrowthValueTempListReq 列表请求参数和筛选对象 {@link OrderGrowthValueTempQueryRequest}
     * @return 会员权益处理订单成长值 临时表的列表信息 {@link OrderGrowthValueTempListResponse}
     */
    @PostMapping("/order/${application.order.version}/ordergrowthvaluetemp/list")
    BaseResponse<OrderGrowthValueTempListResponse> list(@RequestBody @Valid OrderGrowthValueTempQueryRequest orderGrowthValueTempListReq);

    /**
     * 单个删除会员权益处理订单成长值 临时表API
     *
     * @param orderGrowthValueTempDelByIdRequest 单个删除参数结构 {@link OrderGrowthValueTempQueryRequest}
     * @return 删除结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/ordergrowthvaluetemp/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid OrderGrowthValueTempQueryRequest orderGrowthValueTempDelByIdRequest);

    /**
     * 批量删除会员权益处理订单成长值 临时表API
     *
     * @param orderGrowthValueTempDelByIdListRequest 批量删除参数结构 {@link OrderGrowthValueTempQueryRequest}
     * @return 删除结果 {@link BaseResponse}
     */
    @PostMapping("/order/${application.order.version}/ordergrowthvaluetemp/delete-by-id-list")
    BaseResponse deleteByIdList(@RequestBody @Valid OrderGrowthValueTempQueryRequest orderGrowthValueTempDelByIdListRequest);

}

