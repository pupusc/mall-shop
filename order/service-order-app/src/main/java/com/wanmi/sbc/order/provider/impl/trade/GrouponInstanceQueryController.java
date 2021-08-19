package com.wanmi.sbc.order.provider.impl.trade;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.SensitiveUtils;
import com.wanmi.sbc.customer.api.provider.quicklogin.ThirdLoginRelationQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerIdsListRequest;
import com.wanmi.sbc.customer.api.request.quicklogin.ThirdLoginRelationByCustomerRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerIdsListResponse;
import com.wanmi.sbc.customer.bean.enums.ThirdLoginType;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailWithImgVO;
import com.wanmi.sbc.order.api.provider.trade.GrouponInstanceQueryProvider;
import com.wanmi.sbc.order.api.request.trade.GrouponInstanceByGrouponNoRequest;
import com.wanmi.sbc.order.api.request.trade.GrouponInstancePageRequest;
import com.wanmi.sbc.order.api.request.trade.TradePageCriteriaRequest;
import com.wanmi.sbc.order.api.response.trade.GrouponInstanceByActivityIdResponse;
import com.wanmi.sbc.order.api.response.trade.GrouponInstanceByGrouponNoResponse;
import com.wanmi.sbc.order.api.response.trade.GrouponInstancePageResponse;
import com.wanmi.sbc.order.api.response.trade.GrouponInstancePageWithCustomerInfoResponse;
import com.wanmi.sbc.order.bean.vo.GrouponInstanceVO;
import com.wanmi.sbc.order.bean.vo.GrouponInstanceWithCustomerInfoVO;
import com.wanmi.sbc.order.trade.model.root.GrouponInstance;
import com.wanmi.sbc.order.trade.request.GrouponInstanceQueryRequest;
import com.wanmi.sbc.order.trade.service.GrouponInstanceService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-04 10:04
 */
@Validated
@RestController
public class GrouponInstanceQueryController implements GrouponInstanceQueryProvider {

    @Autowired
    private GrouponInstanceService grouponInstanceService;


    @Autowired
    private ThirdLoginRelationQueryProvider thirdLoginRelationQueryProvider;

    /**
     * @param grouponInstancePageRequest 带参分页参数 {@link TradePageCriteriaRequest}
     * @return
     */
    @Override
    public BaseResponse<GrouponInstancePageResponse> pageCriteria(@RequestBody @Valid GrouponInstancePageRequest
                                                                            grouponInstancePageRequest ) {
        GrouponInstanceQueryRequest grouponInstanceQueryRequest = KsBeanUtil.convert(
                grouponInstancePageRequest, GrouponInstanceQueryRequest.class);

        Page<GrouponInstance> page = grouponInstanceService.page(grouponInstanceQueryRequest.getWhereCriteria(), grouponInstanceQueryRequest);
        MicroServicePage<GrouponInstanceVO> grouponInstanceVOS = KsBeanUtil.convertPage(page, GrouponInstanceVO.class);
        return BaseResponse.success(GrouponInstancePageResponse.builder().grouponInstanceVOS(grouponInstanceVOS).build());
    }

    /**
     * @param grouponInstancePageRequest 带参分页参数 {@link TradePageCriteriaRequest}
     * @return
     */
    @Override
    public BaseResponse<GrouponInstancePageWithCustomerInfoResponse> pageCriteriaWithCustomerInfoResponse(@RequestBody @Valid
                                                                                       GrouponInstancePageRequest
                                                                          grouponInstancePageRequest ) {
        GrouponInstanceQueryRequest grouponInstanceQueryRequest = KsBeanUtil.convert(
                grouponInstancePageRequest, GrouponInstanceQueryRequest.class);

        Page<GrouponInstance> page = grouponInstanceService.page(grouponInstanceQueryRequest.getWhereCriteria(), grouponInstanceQueryRequest);
        List<GrouponInstance> grouponInstanceList =page.getContent();

        List<GrouponInstanceWithCustomerInfoVO> grouponInstanceWithCustomerInfos = KsBeanUtil.copyListProperties
                (grouponInstanceList, GrouponInstanceWithCustomerInfoVO.class);
        if (CollectionUtils.isNotEmpty(grouponInstanceList)) {
            //获取用户信息-头像、昵称
            CustomerIdsListRequest customerIdsListRequest = new CustomerIdsListRequest();
            List<String> customerIds = grouponInstanceList.stream().map(GrouponInstance::getCustomerId).collect
                    (Collectors.toList());
            customerIdsListRequest.setCustomerIds(customerIds);
            BaseResponse<CustomerIdsListResponse> listByCustomerIds = thirdLoginRelationQueryProvider
                    .listWithImgByCustomerIds(customerIdsListRequest);
            List<CustomerDetailWithImgVO> customerVOList = listByCustomerIds.getContext().getCustomerVOList();
            grouponInstanceWithCustomerInfos.forEach(vo -> {
                CustomerDetailWithImgVO customerDetailWithImgVO = customerVOList.stream().filter(ivo -> ivo
                        .getCustomerId().equals(vo.getCustomerId())).findFirst().orElse(new CustomerDetailWithImgVO());
                //头像
                vo.setHeadimgurl(customerDetailWithImgVO.getHeadimgurl());
                vo.setCustomerName(SensitiveUtils.handlerMobilePhone(customerDetailWithImgVO.getCustomerName()));
            });
        }
        Page<GrouponInstanceWithCustomerInfoVO> pageWithCustomerInfo =new PageImpl(grouponInstanceWithCustomerInfos,
                grouponInstancePageRequest.getPageable(),page.getTotalElements());

        MicroServicePage<GrouponInstanceWithCustomerInfoVO> grouponInstanceVOS = KsBeanUtil.convertPage(pageWithCustomerInfo, GrouponInstanceWithCustomerInfoVO.class);
        return BaseResponse.success(GrouponInstancePageWithCustomerInfoResponse.builder().grouponInstanceVOS(grouponInstanceVOS).build());
    }


    @Override
    public BaseResponse<GrouponInstanceByGrouponNoResponse> detailByGrouponNo(@RequestBody @Valid GrouponInstanceByGrouponNoRequest request) {
        //查询团实例
        GrouponInstance grouponInstance = grouponInstanceService.detailByGrouponNo(request.getGrouponNo());

        GrouponInstanceByGrouponNoResponse  response = new GrouponInstanceByGrouponNoResponse(
                grouponInstanceService.wrapperVo(grouponInstance));
        return BaseResponse.success(response);
    }


    /**
     * 查询指定活动ID下最近的待成团实例
     * @param grouponInstancePageRequest
     * @return
     */
    @Override
    public BaseResponse<GrouponInstanceByActivityIdResponse> getGrouponLatestInstanceByActivityId(@RequestBody @Valid GrouponInstancePageRequest grouponInstancePageRequest){
        GrouponInstance grouponInstance = grouponInstanceService.detailByActivityId(grouponInstancePageRequest.getGrouponActivityId());
        String customerName = "";
        //获取customerName
        if(grouponInstance!=null){
            CustomerIdsListRequest customerIdsListRequest = new CustomerIdsListRequest();
          List<String> customerIds = new ArrayList<>();
          customerIds.add(grouponInstance.getCustomerId());
            customerIdsListRequest.setCustomerIds(customerIds);
            CustomerIdsListResponse customerIdsListResponseList= thirdLoginRelationQueryProvider.listWithImgByCustomerIds(customerIdsListRequest).getContext();
            if(CollectionUtils.isNotEmpty(customerIdsListResponseList.getCustomerVOList())){
                customerName =  customerIdsListResponseList.getCustomerVOList().get(0).getCustomerName();
            }
        }
        GrouponInstanceByActivityIdResponse  response = new GrouponInstanceByActivityIdResponse(
                grouponInstanceService.wrapperVo(grouponInstance),customerName);
        return BaseResponse.success(response);
    }
}
