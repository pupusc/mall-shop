package com.wanmi.sbc.customer.provider.impl.points;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.customer.api.provider.points.CustomerPointsDetailQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsDetailQueryRequest;
import com.wanmi.sbc.customer.api.request.points.CustomerPointsStatisticsQueryRequest;
import com.wanmi.sbc.customer.api.response.points.*;
import com.wanmi.sbc.customer.bean.vo.CustomerPointsDetailVO;
import com.wanmi.sbc.customer.bean.vo.CustomerStatisticsPointsVO;
import com.wanmi.sbc.customer.points.model.root.CustomerPointsDetail;
import com.wanmi.sbc.customer.points.service.CustomerPointsDetailService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>会员积分明细查询服务接口实现</p>
 */
@RestController
@Validated
public class CustomerPointsDetailQueryController implements CustomerPointsDetailQueryProvider {

    @Autowired
    private CustomerPointsDetailService customerPointsDetailService;

    @Override
    public BaseResponse<CustomerPointsDetailListResponse> list(@RequestBody @Valid CustomerPointsDetailQueryRequest customerPointsDetailPageReq) {
        List<CustomerPointsDetail> customerPointsDetailList = customerPointsDetailService.list(customerPointsDetailPageReq);
        List<CustomerPointsDetailVO> newList = customerPointsDetailList.stream().map(entity -> customerPointsDetailService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new CustomerPointsDetailListResponse(newList));
    }

    @Override
    public BaseResponse<CustomerPointsDetailPageResponse> page(@RequestBody @Valid CustomerPointsDetailQueryRequest customerPointsDetailPageReq) {
        Page<CustomerPointsDetail> customerPointsDetailPage = customerPointsDetailService.page(customerPointsDetailPageReq);
        Page<CustomerPointsDetailVO> newPage = customerPointsDetailPage.map(entity -> customerPointsDetailService.wrapperVo(entity));
        MicroServicePage<CustomerPointsDetailVO> microPage = new MicroServicePage<>(newPage, customerPointsDetailPageReq.getPageable());
        CustomerPointsDetailPageResponse finalRes = new CustomerPointsDetailPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<CustomerPointsDetailResponse> getOne(@RequestBody @Valid CustomerPointsDetailQueryRequest customerPointsDetailPageReq) {
        List<CustomerPointsDetail> customerPointsDetailList = customerPointsDetailService.list(customerPointsDetailPageReq);
        CustomerPointsDetailVO detailVO = new CustomerPointsDetailVO();
        if (CollectionUtils.isNotEmpty(customerPointsDetailList)) {
            detailVO = customerPointsDetailService.wrapperVo(customerPointsDetailList.get(0));
        }
        return BaseResponse.success(new CustomerPointsDetailResponse(detailVO));
    }

    @Override
    public BaseResponse<CustomerPointsStatisticsResponse> queryIssueStatistics(@RequestBody CustomerPointsStatisticsQueryRequest request) {
        if (Objects.nonNull(request.getEmployeeId()) && CollectionUtils.isNotEmpty(request.getCustomerIdList())) {
            return BaseResponse.success(customerPointsDetailService.queryIssueStatisticsByCustomerIds(request.getCustomerIdList()));
        } else {
            return BaseResponse.success(customerPointsDetailService.queryIssueStatistics());
        }
    }

    @Override
    public BaseResponse<CustomerPointsExpireResponse> queryWillExpirePoints(@RequestBody @Valid CustomerGetByIdRequest request){
        return BaseResponse.success(customerPointsDetailService.queryWillExpirePoints(request.getCustomerId()));
    }

    @Override
    public BaseResponse<CustomerPointsExpireResponse> queryWillExpirePointsForCronJob(@RequestBody @Valid CustomerGetByIdRequest request){
        return BaseResponse.success(customerPointsDetailService.queryWillExpirePointsForCronJob(request.getCustomerId()));
    }

    @Override
    public BaseResponse<CustomerPointsDetailPageStatisticGroupByResponse> getPointsStatisticsGroupByCustomer(@RequestBody CustomerPointsDetailQueryRequest request) {
        Page<CustomerStatisticsPointsVO> page = customerPointsDetailService.getPointsStatisticsGroupByCustomer(request);
        MicroServicePage<CustomerStatisticsPointsVO> microPage = new MicroServicePage<>(page, page.getPageable());
        return  BaseResponse.success(new CustomerPointsDetailPageStatisticGroupByResponse(microPage));
    }

}

