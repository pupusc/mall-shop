package com.wanmi.sbc.customer.provider.impl.storelevel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storelevel.StoreLevelQueryProvider;
import com.wanmi.sbc.customer.api.request.storelevel.*;
import com.wanmi.sbc.customer.api.response.storelevel.*;
import com.wanmi.sbc.customer.bean.vo.CustomerLevelVO;
import com.wanmi.sbc.customer.bean.vo.StoreLevelVO;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.detail.service.CustomerDetailService;
import com.wanmi.sbc.customer.level.model.root.CustomerLevel;
import com.wanmi.sbc.customer.level.service.CustomerLevelService;
import com.wanmi.sbc.customer.store.model.root.Store;
import com.wanmi.sbc.customer.store.service.StoreService;
import com.wanmi.sbc.customer.storecustomer.request.StoreCustomerRequest;
import com.wanmi.sbc.customer.storecustomer.root.StoreCustomerRela;
import com.wanmi.sbc.customer.storecustomer.service.StoreCustomerService;
import com.wanmi.sbc.customer.storelevel.model.root.StoreLevel;
import com.wanmi.sbc.customer.storelevel.service.StoreLevelService;
import com.wanmi.sbc.customer.storestatistics.model.root.StoreConsumerStatistics;
import com.wanmi.sbc.customer.storestatistics.service.StoreConsumerStatisticsService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>商户客户等级表查询服务接口实现</p>
 *
 * @author yang
 * @date 2019-02-27 19:51:30
 */
@RestController
@Validated
public class StoreLevelQueryController implements StoreLevelQueryProvider {
    @Autowired
    private StoreLevelService storeLevelService;

    @Autowired
    private CustomerDetailService customerDetailService;

    @Autowired
    private StoreCustomerService storeCustomerService;

    @Autowired
    private StoreConsumerStatisticsService storeConsumerStatisticsService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private CustomerLevelService customerLevelService;

    @Override
    public BaseResponse<StoreLevelPageResponse> page(@RequestBody @Valid StoreLevelPageRequest storeLevelPageReq) {
        StoreLevelQueryRequest queryReq = new StoreLevelQueryRequest();
        KsBeanUtil.copyPropertiesThird(storeLevelPageReq, queryReq);
        Page<StoreLevel> storeLevelPage = storeLevelService.page(queryReq);
        Page<StoreLevelVO> newPage = storeLevelPage.map(entity -> storeLevelService.wrapperVo(entity));
        MicroServicePage<StoreLevelVO> microPage = new MicroServicePage<>(newPage, storeLevelPageReq.getPageable());
        StoreLevelPageResponse finalRes = new StoreLevelPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<StoreLevelListResponse> list(@RequestBody @Valid StoreLevelListRequest storeLevelListReq) {
        StoreLevelQueryRequest queryReq = new StoreLevelQueryRequest();
        KsBeanUtil.copyPropertiesThird(storeLevelListReq, queryReq);
        queryReq.setDelFlag(DeleteFlag.NO.toValue());
        List<StoreLevel> storeLevelList = storeLevelService.list(queryReq);
        List<StoreLevelVO> newList = storeLevelList.stream().map(entity -> storeLevelService.wrapperVo(entity))
                .collect(Collectors.toList());
        return BaseResponse.success(new StoreLevelListResponse(newList));
    }

    @Override
    public BaseResponse<StoreLevelListResponse> getByStoreIdAndLevelName(@RequestBody @Valid
                                                                                     StoreLevelByStoreIdAndLevelNameRequest request) {
        List<StoreLevel> storeLevels = storeLevelService.getByStoreIdAndLevelName(request.getStoreId(), request
                .getLevelName());
        List<StoreLevelVO> newList = storeLevels.stream().map(entity -> storeLevelService.wrapperVo(entity)).collect
                (Collectors.toList());
        return BaseResponse.success(StoreLevelListResponse.builder().storeLevelVOList(newList).build());
    }

    @Override
    public BaseResponse<StoreLevelListResponse> listAllStoreLevelByStoreId(@RequestBody @Valid StoreLevelListRequest
                                                                                       storeLevelListReq) {
        List<StoreLevel> allLevel = storeLevelService.findAllLevelByStoreId(storeLevelListReq.getStoreId());
        List<StoreLevelVO> newList = allLevel.stream().map(entity -> storeLevelService.wrapperVo(entity)).collect
                (Collectors.toList());
        return BaseResponse.success(new StoreLevelListResponse(newList));
    }

    @Override
    public BaseResponse<StoreLevelByIdResponse> getById(@RequestBody @Valid StoreLevelByIdRequest
                                                                    storeLevelByIdRequest) {
        StoreLevel storeLevel = storeLevelService.getById(storeLevelByIdRequest.getStoreLevelId());
        return BaseResponse.success(new StoreLevelByIdResponse(storeLevelService.wrapperVo(storeLevel)));
    }

    @Override
    public BaseResponse<StoreLevelByCustomerIdAndStoreIdResponse> getByCustomerIdAndStoreId(@RequestBody @Valid
                                                                                                        StoreLevelByCustomerIdAndStoreIdRequest request) {
        StoreLevelByCustomerIdAndStoreIdResponse response = new StoreLevelByCustomerIdAndStoreIdResponse();
        CustomerDetail customerDetail = customerDetailService.findAnyByCustomerId(request.getCustomerId());
        response.setCustomerName(customerDetail.getCustomerName());
        StoreCustomerRequest storeCustomerRequest = new StoreCustomerRequest();
        storeCustomerRequest.setCustomerId(request.getCustomerId());
        storeCustomerRequest.setStoreId(request.getStoreId());
        List<StoreCustomerRela> customerRelas = storeCustomerService.list(storeCustomerRequest);
        if (customerRelas != null && customerRelas.size() > 0) {
            StoreCustomerRela storeCustomerRela = customerRelas.get(0);
            StoreLevel storeLevel = storeLevelService.getById(storeCustomerRela.getStoreLevelId());
            response.setStoreLevelVO(storeLevelService.wrapperVo(storeLevel));
        }
        // 查询店铺消费情况
        StoreConsumerStatistics storeConsumerStatistics = storeConsumerStatisticsService.getByCustomerIdAndStoreId
                (request.getCustomerId(), request.getStoreId());
        if (storeConsumerStatistics != null) {
            response.setTotalOrder(storeConsumerStatistics.getTradeCount());
            response.setTotalAmount(storeConsumerStatistics.getTradePriceCount());
        } else {
            response.setTotalOrder(0);
            response.setTotalAmount(BigDecimal.ZERO);
        }
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<StroeLevelInfoResponse> queryStoreLevelInfo(@RequestBody @Valid StoreLevelByStoreIdRequest
                                                                                request) {
        Long storeId = request.getStoreId();
        Store store = storeService.queryStoreCommon(storeId);
        List<CustomerLevelVO> customerLevelVOList = new ArrayList<>();
        // 自营店铺
        if (store.getCompanyType().equals(BoolFlag.NO)) {
            customerLevelVOList= getCustomerLevelVOList();
        }
        //非自营店铺
        if (store.getCompanyType().equals(BoolFlag.YES)) {
            customerLevelVOList= getStoreCustomerLevelVOList(request.getStoreId());
        }
        return BaseResponse.success(StroeLevelInfoResponse.builder().customerLevelVOList(customerLevelVOList).build());
    }


    @Override
    public BaseResponse<CustomerLevelInfoResponse> queryCustomerLevelInfo(@RequestBody @Valid CustomerLevelRequest
                                                                                      request) {
        List<CustomerLevelVO> customerLevelVOList = new ArrayList<>();
        // 平台等级
        if (DefaultFlag.YES.equals(request.getLevelType())) {
            customerLevelVOList= getCustomerLevelVOList();
        }
        //店铺等级
        if (DefaultFlag.NO.equals(request.getLevelType())){
            customerLevelVOList= getStoreCustomerLevelVOList(request.getStoreId());
        }
        return BaseResponse.success(CustomerLevelInfoResponse.builder().customerLevelVOList(customerLevelVOList).build());
    }


    /**
     * 平台客户等级
     *
     * @return
     */
    private List<CustomerLevelVO> getCustomerLevelVOList() {
        List<CustomerLevelVO> customerLevelVOList = new ArrayList<>();
        // 自营店铺

        List<CustomerLevel> customerLevelList = customerLevelService.findAllLevel();
        if (CollectionUtils.isNotEmpty(customerLevelList)) {
            customerLevelVOList = customerLevelList.stream()
                    .map(customerLevel -> {
                        CustomerLevelVO vo = new CustomerLevelVO();
                        KsBeanUtil.copyPropertiesThird(customerLevel, vo);
                        return vo;
                    }).collect(Collectors.toList());
        }


        return customerLevelVOList;
    }
    /**
     * 店铺客户等级
     *
     * @return
     */
    private List<CustomerLevelVO> getStoreCustomerLevelVOList(Long storeId ) {
        List<CustomerLevelVO> customerLevelVOList = new ArrayList<>();
        List<StoreLevel> storeLevels = storeLevelService.findAllLevelByStoreId(storeId);
        if (CollectionUtils.isNotEmpty(storeLevels)) {
            customerLevelVOList = storeLevels.stream()
                    .map(storeLevel -> {
                        CustomerLevelVO vo = new CustomerLevelVO();
                        vo.setCustomerLevelId(storeLevel.getStoreLevelId());
                        vo.setCustomerLevelName(storeLevel.getLevelName());
                        vo.setCustomerLevelDiscount(storeLevel.getDiscountRate());
                        return vo;
                    }).collect(Collectors.toList());
        }


        return customerLevelVOList;
    }


    @Override
    public BaseResponse<StroeLevelInfoByIdResponse> queryStoreLevelInfoByStoreLevelId(@RequestBody @Valid
                                                                                                  StoreLevelByIdRequest request) {
        Long storeLevelId = request.getStoreLevelId();
        StoreLevel storeLevel = storeLevelService.getById(storeLevelId);
        CustomerLevelVO customerLevelVO = new CustomerLevelVO();
        customerLevelVO.setCustomerLevelId(storeLevel.getStoreLevelId());
        customerLevelVO.setCustomerLevelName(storeLevel.getLevelName());
        customerLevelVO.setCustomerLevelDiscount(storeLevel.getDiscountRate());
        return BaseResponse.success(StroeLevelInfoByIdResponse.builder().customerLevelVO(customerLevelVO).build());
    }

    @Override
    public BaseResponse<StoreLevelByIdResponse> queryByLevelUpCondition(@RequestBody @Valid StoreLevelQueryRequest
                                                                                    request) {
        List<StoreLevel> storeLevels = storeLevelService.queryByLevelUpCondition(
                request.getStoreId(), request.getAmountConditions(), request.getOrderConditions());
        StoreLevelVO vo = null;
        if (CollectionUtils.isNotEmpty(storeLevels)) {
            vo = new StoreLevelVO();
            KsBeanUtil.copyPropertiesThird(storeLevels.get(0), vo);
        }
        return BaseResponse.success(StoreLevelByIdResponse.builder().storeLevelVO(vo).build());
    }

}

