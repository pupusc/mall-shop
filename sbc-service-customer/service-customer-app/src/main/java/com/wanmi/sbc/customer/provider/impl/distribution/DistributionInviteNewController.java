package com.wanmi.sbc.customer.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionInviteNewQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.*;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerExportRequest;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewExportResponse;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewListByInvitedCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewListResponse;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewPageResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCountByRequestCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCountCouponByRequestCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCountInvitedCustResponse;
import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewForPageVO;
import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewRecordVO;
import com.wanmi.sbc.customer.distribution.model.root.InviteNewRecord;
import com.wanmi.sbc.customer.distribution.service.CustomerDistributionInviteNewService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by feitingting on 2019/2/21.
 */
@RestController
public class DistributionInviteNewController implements DistributionInviteNewQueryProvider {

    @Autowired
    private CustomerDistributionInviteNewService customerDistributionInviteNewService;

    @Override
    public BaseResponse<DistributionInviteNewPageResponse> findDistributionInviteNewRecord(@RequestBody @Valid
                                                                                                   DistributionInviteNewPageRequest request) {
        DistributionInviteNewPageResponse response = customerDistributionInviteNewService.page(request);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<DistributionInviteNewExportResponse> exportDistributionInviteNewRecord(@RequestBody @Valid
                                                                                                           DistributionInviteNewExportRequest request) {
        DistributionInviteNewPageRequest pageReq = new DistributionInviteNewPageRequest();
        KsBeanUtil.copyPropertiesThird(request, pageReq);

        List<DistributionInviteNewForPageVO> dataRecords = new ArrayList<>();
        // 分批次查询，避免出现hibernate事务超时 共查询1000条
        for (int i = 0; i < 10; i++) {
            pageReq.setPageNum(i);
            pageReq.setPageSize(100);
            List<DistributionInviteNewForPageVO> data = customerDistributionInviteNewService.page(pageReq).getRecordList();
            dataRecords.addAll(data);
            if (data.size() < 100) {
                break;
            }
        }
        return BaseResponse.success(new DistributionInviteNewExportResponse(dataRecords));
    }

    /**
     * 根据条件查询所有邀新记录
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DistributionInviteNewListResponse> listDistributionInviteNewRecord(@RequestBody @Valid
                                                                                                   DistributionInviteNewListRequest request) {
        DistributionInviteNewListResponse response = customerDistributionInviteNewService.list(request);
        return BaseResponse.success(response);
    }

    /**
     * 统计邀新记录数量
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DistributionCountInvitedCustResponse> countInvitedCustomers(@RequestBody @Valid
                                                                                            DistributionCountInvitedCustRequest request) {
        DistributionCountInvitedCustResponse response = customerDistributionInviteNewService
                .countCashByRequestCustomerId(request.getCustomerId());
        return BaseResponse.success(response);
    }

    /**
     * 根据受邀人会员ID查询邀新记录
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DistributionInviteNewListByInvitedCustomerIdResponse> listByInvitedCustomerId(@RequestBody
                                                                                                      @Valid DistributionInviteNewListByInviteCustomerIdRequest request) {
        List<InviteNewRecord> inviteNewRecordList = customerDistributionInviteNewService.findByInvitedCustomerId
                (request.getInvitedCustomerId());
        if (CollectionUtils.isEmpty(inviteNewRecordList)) {
            return BaseResponse.success(new DistributionInviteNewListByInvitedCustomerIdResponse());
        }
        List<DistributionInviteNewRecordVO> list = KsBeanUtil.convert(inviteNewRecordList,
                DistributionInviteNewRecordVO.class);
        return BaseResponse.success(new DistributionInviteNewListByInvitedCustomerIdResponse(list));
    }


    /**
     * 根据邀请人会员ID统计邀新总人数、有效邀新人数、奖励已入账邀新人数（优惠券）
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DistributionCountCouponByRequestCustomerIdResponse> countCouponByRequestCustomerId
    (@RequestBody @Valid DistributionCountCouponByRequestCustomerIdRequest request) {
        DistributionCountCouponByRequestCustomerIdResponse response = customerDistributionInviteNewService
                .countCouponByRequestCustomerId(request.getCustomerId());
        return BaseResponse.success(response);
    }

    /**
     * 根据邀请人会员ID统计邀新总人数、有效邀新人数、奖励已入账邀新人数（优惠券 & 奖金）
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DistributionCountByRequestCustomerIdResponse> countByRequestCustomerId(@RequestBody @Valid
                                                                                                       DistributionCountByRequestCustomerIdRequest request) {
        DistributionCountByRequestCustomerIdResponse response = customerDistributionInviteNewService
                .countByRequestCustomerId(request.getCustomerId());
        return BaseResponse.success(response);
    }

    /**
     * 根据条件分页查询邀新记录
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DistributionInviteNewPageResponse> findInviteNewRecordPage(@RequestBody @Valid
                                                                                           DistributionInviteNewPageRequest request) {
        DistributionInviteNewPageResponse response = customerDistributionInviteNewService.findInviteNewRecordPage
                (request);
        return BaseResponse.success(response);
    }

    /**
     * 统计邀新记录数量
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<DistributionCountInvitedCustResponse> distinctCountInvitedCustomers
            (@RequestBody @Valid DistributionCountInvitedCustRequest request) {
        DistributionCountInvitedCustResponse response = customerDistributionInviteNewService.distinctCountInvitedCustomers(request);
        return BaseResponse.success(response);
    }
}
