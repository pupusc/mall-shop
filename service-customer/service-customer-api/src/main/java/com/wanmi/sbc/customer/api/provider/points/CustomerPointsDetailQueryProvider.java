package com.wanmi.sbc.customer.api.provider.points;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailQueryRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsStatisticsQueryRequest;
import com.wanmi.sbc.customer.api.response.points.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员积分明细查询服务Provider</p>
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerPointsDetailQueryProvider")
public interface CustomerPointsDetailQueryProvider {

    /**
     * 列表查询会员积分明细API
     *
     * @author minchen
     * @param customerPointsDetailListReq 列表请求参数和筛选对象 {@link CustomerPointsDetailQueryRequest}
     * @return 会员积分明细的列表信息 {@link CustomerPointsDetailListResponse}
     */
    @PostMapping("/customer/${application.customer.version}/customerpointsdetail/list")
    BaseResponse<CustomerPointsDetailListResponse> list(@RequestBody @Valid CustomerPointsDetailQueryRequest customerPointsDetailListReq);

    /**
     * 分页查询会员积分明细API
     *
     * @param customerPointsDetailPageReq 分页请求参数和筛选对象 {@link CustomerPointsDetailQueryRequest}
     * @return 会员积分明细分页列表信息 {@link CustomerPointsDetailPageResponse}
     */
    @PostMapping("/customer/${application.customer.version}/customerpointsdetail/page")
    BaseResponse<CustomerPointsDetailPageResponse> page(@RequestBody @Valid CustomerPointsDetailQueryRequest customerPointsDetailPageReq);

    /**
     * 查询单个会员积分明细API
     *
     * @param customerPointsDetailListReq 列表请求参数和筛选对象 {@link CustomerPointsDetailQueryRequest}
     * @return 会员积分明细详细信息 {@link CustomerPointsDetailResponse}
     * @author minchen
     */
    @PostMapping("/customer/${application.customer.version}/customerpointsdetail/getone")
    BaseResponse<CustomerPointsDetailResponse> getOne(@RequestBody @Valid CustomerPointsDetailQueryRequest customerPointsDetailListReq);

    /**
     * 查询平台历史积分统计API
     *
     * @return 会员积分明细的列表信息 {@link CustomerPointsStatisticsResponse}
     * @author minchen
     */
    @PostMapping("/customer/${application.customer.version}/customerpointsdetail/statistics")
    BaseResponse<CustomerPointsStatisticsResponse> queryIssueStatistics(@RequestBody CustomerPointsStatisticsQueryRequest customerPointsStatisticsQueryRequest);

    /**
     * 查询单个会员即将过期积分 用于C端展示
     *
     * @param request 用户ID请求参数 {@link CustomerGetByIdRequest}
     * @return 会员积分明细详细信息 {@link CustomerPointsDetailResponse}
     * @author minchen
     */
    @PostMapping("/customer/${application.customer.version}/customerpointsdetail/willexpire")
    BaseResponse<CustomerPointsExpireResponse> queryWillExpirePoints(@RequestBody @Valid CustomerGetByIdRequest request);

    /**
     * 查询单个会员即将过期积分 用于定时任务
     *
     * @param request 用户ID请求参数 {@link CustomerGetByIdRequest}
     * @return 会员积分明细详细信息 {@link CustomerPointsDetailResponse}
     * @author minchen
     */
    @PostMapping("/customer/${application.customer.version}/customerpointsdetail/willexpire-for-cronjob")
    BaseResponse<CustomerPointsExpireResponse> queryWillExpirePointsForCronJob(@RequestBody @Valid CustomerGetByIdRequest request);

    /**
     * 查询会员这段时间内获得的总计分数
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/customerpointsdetail/customer_statistics_points")
    BaseResponse<CustomerPointsDetailPageStatisticGroupByResponse> getPointsStatisticsGroupByCustomer(@RequestBody CustomerPointsDetailQueryRequest request);

}

