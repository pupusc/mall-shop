package com.wanmi.sbc.customer.follow.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.customer.api.constant.StoreErrorCode;
import com.wanmi.sbc.customer.api.request.follow.StoreCustomerFollowRequest;
import com.wanmi.sbc.customer.api.request.follow.StoreFollowBystoreIdRequest;
import com.wanmi.sbc.customer.api.request.store.StoreQueryRequest;
import com.wanmi.sbc.customer.api.response.store.StoreBaseInfoResponse;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.follow.model.root.StoreCustomerFollow;
import com.wanmi.sbc.customer.follow.repository.StoreCustomerFollowRepository;
import com.wanmi.sbc.customer.store.model.root.Store;
import com.wanmi.sbc.customer.store.repository.StoreRepository;
import com.wanmi.sbc.customer.store.service.StoreWhereCriteriaBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 店铺信息服务
 * Created by CHENLI on 2017/11/2.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class StoreCustomerFollowService {


    @Autowired
    private StoreCustomerFollowRepository storeCustomerFollowRepository;

    @Autowired
    private StoreRepository storeRepository;

    /**
     * 分页查询店铺
     *
     * @param request 参数
     * @return
     */
    public Page<StoreBaseInfoResponse> page(StoreCustomerFollowRequest request) {
        Page<Object> objectPage;
        if(Objects.nonNull(request.getStoreId())){
            objectPage = storeCustomerFollowRepository.pageFollowAndStoreId(request.getCustomerId()
                    , request.getStoreId()
                    , request.getPageRequest());
        }else {
            objectPage = storeCustomerFollowRepository.pageFollow(request.getCustomerId(), request
                    .getPageRequest());
        }
        List<StoreBaseInfoResponse> responses = objectPage.getContent().stream()
                .map(o -> new StoreBaseInfoResponse().convertFromNativeSQLResult(o))
                .collect(Collectors.toList());
        return new PageImpl<>(responses, request.getPageable(), objectPage.getTotalElements());
    }

    /**
     * 关注店铺
     *
     * @param request 参数
     */
    @Transactional
    public void save(StoreCustomerFollowRequest request) {
        List<Store> stores = storeRepository.findAll(StoreWhereCriteriaBuilder.build(
                                                        StoreQueryRequest.builder()
                                                                .storeIds(Collections.singletonList(request.getStoreId()))
                                                                .delFlag(DeleteFlag.NO)
                                                                .build()));
        if (CollectionUtils.isEmpty(stores)) {
            throw new SbcRuntimeException(StoreErrorCode.NOT_EXIST);
        }

        Store store = stores.get(0);
        if (CheckState.CHECKED.toValue() != store.getAuditState().toValue()) {
            throw new SbcRuntimeException(StoreErrorCode.REJECTED);
        }
        if (StoreState.CLOSED.toValue() == store.getStoreState().toValue()) {
            throw new SbcRuntimeException(StoreErrorCode.CLOSE);
        }

        //收藏限制
        if (queryStoreFollowNum(request.getCustomerId()) >= Constants.STORE_FOLLOW_MAX_SIZE) {
            throw new SbcRuntimeException(StoreErrorCode.FOLLOW_LIMIT, new Object[]{Constants.STORE_FOLLOW_MAX_SIZE});
        }

        List<StoreCustomerFollow> followList = storeCustomerFollowRepository.findByStoreId(Collections.singletonList
                (request.getStoreId()), request.getCustomerId());
        if (CollectionUtils.isEmpty(followList)) {
            StoreCustomerFollow follow = new StoreCustomerFollow();
            follow.setStoreId(store.getStoreId());
            follow.setCompanyInfoId(store.getCompanyInfo().getCompanyInfoId());
            follow.setCustomerId(request.getCustomerId());
            follow.setFollowTime(LocalDateTime.now());
            follow.setTerminalSource(request.getTerminalSource());
            storeCustomerFollowRepository.save(follow);
        }
    }

    /**
     * 取消关注
     *
     * @param request 参数
     */
    @Transactional
    public void delete(StoreCustomerFollowRequest request) {
        storeCustomerFollowRepository.deleteByFollowIds(request.getStoreIds(), request.getCustomerId());
    }

    /**
     * 哪些店铺被关注
     *
     * @param request
     * @return
     */
    public List<Long> isFollow(StoreCustomerFollowRequest request) {
        List<StoreCustomerFollow> follows = storeCustomerFollowRepository.findByStoreId(request.getStoreIds(),
                request.getCustomerId());
        if (CollectionUtils.isEmpty(follows)) {
            return Collections.emptyList();
        }
        return follows.stream().map(StoreCustomerFollow::getStoreId).collect(Collectors.toList());
    }

    /**
     * 统计店铺关注数量
     *
     * @param customerId 客户ID
     * @return
     */
    public Long queryStoreFollowNum(String customerId) {
        return storeCustomerFollowRepository.queryStoreFollowNum(customerId);
    }


    /**
     * 店铺是否被关注
     *
     * @param storeId    店铺Id
     * @param customerId 用户Id
     * @return
     */
    public Boolean isFollowed(Long storeId, String customerId) {
        List<StoreCustomerFollow> follows = storeCustomerFollowRepository.findByStoreId(Arrays.asList(storeId),
                customerId);
        return CollectionUtils.isNotEmpty(follows);
    }

    /**
     * @Description: 统计店铺关注总数
     * @param request 店铺ID
     * @Author: Bob
     * @Date: 2019-04-03 10:29
     */
    public long count(StoreFollowBystoreIdRequest request){
        return storeCustomerFollowRepository.countByStoreId(request.getStoreId());
    }
}
