package com.wanmi.sbc.customer.api.provider.storesharerecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordByIdRequest;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordListRequest;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordPageRequest;
import com.wanmi.sbc.customer.api.response.storesharerecord.StoreShareRecordByIdResponse;
import com.wanmi.sbc.customer.api.response.storesharerecord.StoreShareRecordListResponse;
import com.wanmi.sbc.customer.api.response.storesharerecord.StoreShareRecordPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商城分享查询服务Provider</p>
 *
 * @author zhangwenchang
 * @date 2020-03-06 13:48:42
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreShareRecordQueryProvider")
public interface StoreShareRecordQueryProvider {

    /**
     * 分页查询商城分享API
     *
     * @param storeShareRecordPageReq 分页请求参数和筛选对象 {@link StoreShareRecordPageRequest}
     * @return 商城分享分页列表信息 {@link StoreShareRecordPageResponse}
     * @author zhangwenchang
     */
    @PostMapping("/customer/${application.customer.version}/storesharerecord/page")
    BaseResponse<StoreShareRecordPageResponse> page(@RequestBody @Valid StoreShareRecordPageRequest storeShareRecordPageReq);

    /**
     * 列表查询商城分享API
     *
     * @param storeShareRecordListReq 列表请求参数和筛选对象 {@link StoreShareRecordListRequest}
     * @return 商城分享的列表信息 {@link StoreShareRecordListResponse}
     * @author zhangwenchang
     */
    @PostMapping("/customer/${application.customer.version}/storesharerecord/list")
    BaseResponse<StoreShareRecordListResponse> list(@RequestBody @Valid StoreShareRecordListRequest storeShareRecordListReq);

    /**
     * 单个查询商城分享API
     *
     * @param storeShareRecordByIdRequest 单个查询商城分享请求参数 {@link StoreShareRecordByIdRequest}
     * @return 商城分享详情 {@link StoreShareRecordByIdResponse}
     * @author zhangwenchang
     */
    @PostMapping("/customer/${application.customer.version}/storesharerecord/get-by-id")
    BaseResponse<StoreShareRecordByIdResponse> getById(@RequestBody @Valid StoreShareRecordByIdRequest storeShareRecordByIdRequest);

}

