package com.wanmi.sbc.customer.provider.impl.follow;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.follow.StoreCustomerFollowQueryProvider;
import com.wanmi.sbc.customer.api.request.follow.*;
import com.wanmi.sbc.customer.api.response.follow.*;
import com.wanmi.sbc.customer.api.response.store.StoreBaseInfoResponse;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerFollowVO;
import com.wanmi.sbc.customer.follow.service.StoreCustomerFollowService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 店铺收藏-店铺收藏查询API
 *
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@RestController
@Validated
public class StoreCustomerFollowQueryController implements StoreCustomerFollowQueryProvider {

    @Autowired
    private StoreCustomerFollowService storeCustomerFollowService;

    @Override
    public BaseResponse<StoreCustomerFollowPageResponse> pageStoreCustomerFollow(@RequestBody @Valid
                                                                                         StoreCustomerFollowPageRequest
                                                                                         request) {
        StoreCustomerFollowRequest followRequest = new StoreCustomerFollowRequest();
        KsBeanUtil.copyPropertiesThird(request, followRequest);
        Page<StoreBaseInfoResponse> pages = storeCustomerFollowService.page(followRequest);
        List<StoreCustomerFollowVO> voList = wraperVos(pages.getContent());
        StoreCustomerFollowPageResponse response = StoreCustomerFollowPageResponse.builder()
                .customerFollowVOPage(new MicroServicePage<>(voList, request.getPageable(), pages.getTotalElements()))
                .build();
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<StoreCustomerFollowExistsBatchResponse> queryStoreCustomerFollowByStoreIds(@RequestBody @Valid
                                                                                                           StoreCustomerFollowExistsBatchRequest request) {
        StoreCustomerFollowRequest followRequest = new StoreCustomerFollowRequest();
        KsBeanUtil.copyPropertiesThird(request, followRequest);
        return BaseResponse.success(
                StoreCustomerFollowExistsBatchResponse.builder()
                        .storeIds(storeCustomerFollowService.isFollow(followRequest)).build());
    }

    @Override

    public BaseResponse<StoreCustomerFollowExistsResponse> queryStoreCustomerFollowByStoreId(@RequestBody @Valid
                                                                                                     StoreCustomerFollowExistsRequest
                                                                                                     request) {
        return BaseResponse.success(
                StoreCustomerFollowExistsResponse.builder()
                        .isFollowed(storeCustomerFollowService.isFollowed(request.getStoreId(), request.getCustomerId
                                ()))
                        .build());
    }

    @Override

    public BaseResponse<StoreCustomerFollowCountResponse> queryStoreCustomerFollowCountByCustomerId(@RequestBody @Valid
                                                                                                            StoreCustomerFollowCountRequest
                                                                                                            request) {
        return BaseResponse.success(
                StoreCustomerFollowCountResponse.builder()
                        .followNum(storeCustomerFollowService.queryStoreFollowNum(request.getCustomerId()))
                        .build());
    }

    @Override
    public BaseResponse<StoreFollowCountBystoreIdResponse> queryStoreCustomerFollowCountByStoreId(@RequestBody @Valid StoreFollowBystoreIdRequest
                                                                                                              request) {
        long count = storeCustomerFollowService.count(request);
        return BaseResponse.success(StoreFollowCountBystoreIdResponse.builder().count(count).build());
    }

    private List<StoreCustomerFollowVO> wraperVos(List<StoreBaseInfoResponse> storeBaseInfoResponseList) {
        if (CollectionUtils.isNotEmpty(storeBaseInfoResponseList)) {
            return storeBaseInfoResponseList.stream().map(info -> {
                StoreCustomerFollowVO vo = new StoreCustomerFollowVO();
                KsBeanUtil.copyPropertiesThird(info, vo);
                return vo;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
