package com.wanmi.sbc.marketing.api.provider.distributionrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.distributionrecord.*;
import com.wanmi.sbc.marketing.api.response.distributionrecord.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>DistributionRecord查询服务Provider</p>
 *
 * @author baijz
 * @date 2019-02-27 18:56:40
 */
@FeignClient(value = "${application.marketing.name}", contextId = "DistributionRecordQueryProvider")
public interface DistributionRecordQueryProvider {

    /**
     * 分页查询DistributionRecordAPI
     *
     * @param distributionRecordPageReq 分页请求参数和筛选对象 {@link DistributionRecordPageRequest}
     * @return DistributionRecord分页列表信息 {@link DistributionRecordPageResponse}
     * @author baijz
     */
    @PostMapping("/marketing/${application.marketing.version}/distributionrecord/page")
    BaseResponse<DistributionRecordPageResponse> page(@RequestBody @Valid DistributionRecordPageRequest
                                                              distributionRecordPageReq);

    /**
     * 导出查询DistributionRecordAPI
     *
     * @param distributionRecordExportReq 导出请求参数和筛选对象 {@link DistributionRecordPageRequest}
     * @return DistributionRecord导出信息 {@link DistributionRecordPageResponse}
     * @author of2975
     */
    @PostMapping("/marketing/${application.marketing.version}/distributionrecord/export")
    BaseResponse<DistributionRecordExportResponse> export(@RequestBody @Valid DistributionRecordExportRequest
                                                              distributionRecordExportReq);

    /**
     * 列表查询DistributionRecordAPI
     *
     * @param distributionRecordListReq 列表请求参数和筛选对象 {@link DistributionRecordListRequest}
     * @return DistributionRecord的列表信息 {@link DistributionRecordListResponse}
     * @author baijz
     */
    @PostMapping("/marketing/${application.marketing.version}/distributionrecord/list")
    BaseResponse<DistributionRecordListResponse> list(@RequestBody @Valid DistributionRecordListRequest distributionRecordListReq);

    /**
     * 单个查询DistributionRecordAPI
     *
     * @param distributionRecordByIdRequest 单个查询DistributionRecord请求参数 {@link DistributionRecordByIdRequest}
     * @return DistributionRecord详情 {@link DistributionRecordByIdResponse}
     * @author baijz
     */
    @PostMapping("/marketing/${application.marketing.version}/distributionrecord/get-by-id")
    BaseResponse<DistributionRecordByIdResponse> getById(@RequestBody @Valid DistributionRecordByIdRequest distributionRecordByIdRequest);


    /**
     * 根据会员的Id查询该分销员的业绩
     *
     * @param distributionRecordByIdRequest 单个查询DistributionRecord请求参数 {@link DistributionRecordByIdRequest}
     * @return DistributionRecord详情 {@link DistributionRecordByIdResponse}
     * @author baijz
     */
    @PostMapping("/marketing/${application.marketing.version}/distributionrecord/get-by-customerId-id")
    BaseResponse<DistributionRecordByInviteeIdResponse> getPerformanceByCustomerId(@RequestBody @Valid DistributionRecordByCustomerIdRequest distributionRecordByIdRequest);


}

