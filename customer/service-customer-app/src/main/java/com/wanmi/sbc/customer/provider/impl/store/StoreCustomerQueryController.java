package com.wanmi.sbc.customer.provider.impl.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreCustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoQueryRequest;
import com.wanmi.sbc.customer.api.request.store.*;
import com.wanmi.sbc.customer.api.response.company.CompanyInfoGetResponse;
import com.wanmi.sbc.customer.api.response.store.*;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerRelaVO;
import com.wanmi.sbc.customer.bean.vo.StoreCustomerVO;
import com.wanmi.sbc.customer.company.model.root.CompanyInfo;
import com.wanmi.sbc.customer.employee.model.root.Employee;
import com.wanmi.sbc.customer.employee.service.EmployeeService;
import com.wanmi.sbc.customer.storecustomer.request.StoreCustomerRequest;
import com.wanmi.sbc.customer.storecustomer.root.StoreCustomerRela;
import com.wanmi.sbc.customer.storecustomer.service.StoreCustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Validated
public class StoreCustomerQueryController implements StoreCustomerQueryProvider {
    @Autowired
    private StoreCustomerService storeCustomerService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * 根据店铺标识查询店铺的会员列表
     * <p>
     * {@link StoreCustomerService#findCustomerList}
     */
    @Override

    public BaseResponse<StoreCustomerResponse> listCustomerByStoreId(@RequestBody @Valid StoreCustomerQueryRequest
                                                                             storeCustomerQueryRequest) {
        List<StoreCustomerVO> voList = storeCustomerService.findCustomerList(storeCustomerQueryRequest.getStoreId());

        return BaseResponse.success(new StoreCustomerResponse(voList));
    }

    /**
     * 根据店铺标识查询店铺的会员列表，不区分会员禁用状态
     * <p>
     * {@link StoreCustomerService#findAllCustomerList}
     */
    @Override
    public BaseResponse<StoreCustomerResponse> listAllCustomer(@RequestBody @Valid StoreCustomerQueryRequest request) {
        List<StoreCustomerVO> voList = storeCustomerService.findAllCustomerList(request.getStoreId());
        return BaseResponse.success(new StoreCustomerResponse(voList));
    }

    public BaseResponse<StoreCustomerResponse> listCustomerByIds(@RequestBody @Valid StoreCustomerQueryRequest request) {
        List<StoreCustomerVO> voList = storeCustomerService.findCustomerListByIds(request.getStoreId(), request.getCustomerIds());
        return BaseResponse.success(new StoreCustomerResponse(voList));
    }

    @Override
    public BaseResponse<StoreCustomerResponse> listBossAllCustomer() {
        List<StoreCustomerVO> voList = storeCustomerService.findBossAllCustomerList();

        return BaseResponse.success(new StoreCustomerResponse(voList));
    }

    @Override
    public BaseResponse<StoreCustomerResponse> listBossCustomerByName(@RequestBody @Valid StoreCustomerQueryByCustomerNameRequest
                                                                              storeCustomerQueryByCustomerNameRequest) {
        List<StoreCustomerVO> voList = storeCustomerService.findBossAllCustomerListByName(storeCustomerQueryByCustomerNameRequest.getCustomerName());

        return BaseResponse.success(new StoreCustomerResponse(voList));
    }

    /**
     * 根据店铺标识查询店铺的会员列表
     * <p>
     * {@link StoreCustomerService#findCustomerList}
     */
    @Override

    public BaseResponse<StoreCustomerResponse> listCustomer(@RequestBody @Valid StoreCustomerQueryByEmployeeRequest
                                                                    storeCustomerQueryByEmployeeRequest) {
        String employeeId = storeCustomerQueryByEmployeeRequest.getEmployeeId();
        if (StringUtils.isBlank(employeeId)) {
            storeCustomerQueryByEmployeeRequest.setEmployeeId(null);
            storeCustomerQueryByEmployeeRequest.setEmployeeIds(null);
        } else {
            // 如果业务员是主账号，查询店铺下所有客户
            Employee employee = employeeService.findEmployeeById(employeeId).orElse(null);
            if (Objects.equals(DefaultFlag.YES.toValue(), employee.getIsMasterAccount())) {
                storeCustomerQueryByEmployeeRequest.setEmployeeId(null);
                storeCustomerQueryByEmployeeRequest.setEmployeeIds(null);
            }
        }
        List<StoreCustomerVO> voList = storeCustomerService.findCustomerList(storeCustomerQueryByEmployeeRequest
                        .getStoreId(),
                storeCustomerQueryByEmployeeRequest.getCustomerAccount(), storeCustomerQueryByEmployeeRequest
                        .getPageSize(), storeCustomerQueryByEmployeeRequest.getEmployeeIds());

        return BaseResponse.success(new StoreCustomerResponse(voList));
    }

    @Override
    public BaseResponse<StoreCustomerResponse> listBossCustomer(@RequestBody @Valid StoreCustomerQueryByEmployeeRequest
                                                                        storeCustomerQueryByEmployeeRequest) {
        String employeeId = storeCustomerQueryByEmployeeRequest.getEmployeeId();
        if (StringUtils.isBlank(employeeId)) {
            storeCustomerQueryByEmployeeRequest.setEmployeeId(null);
            storeCustomerQueryByEmployeeRequest.setEmployeeIds(null);
        } else {
            // 如果业务员是主账号，查询店铺下所有客户
            Employee employee = employeeService.findEmployeeById(employeeId).orElse(null);
            if (Objects.equals(DefaultFlag.YES.toValue(), employee.getIsMasterAccount())) {
                storeCustomerQueryByEmployeeRequest.setEmployeeId(null);
                storeCustomerQueryByEmployeeRequest.setEmployeeIds(null);
            }
        }
        List<StoreCustomerVO> voList = storeCustomerService.findBossCustomerList(
                storeCustomerQueryByEmployeeRequest.getCustomerAccount(), storeCustomerQueryByEmployeeRequest
                        .getPageSize(), storeCustomerQueryByEmployeeRequest.getEmployeeIds());

        return BaseResponse.success(new StoreCustomerResponse(voList));
    }

    /**
     * 通过客户Id获取商家记录
     * <p>
     * {@link StoreCustomerService#getCompanyInfoByCustomerId}
     */
    @Override

    public BaseResponse<CompanyInfoGetResponse> getCompanyInfoByCustomerId(@RequestBody @Valid
                                                                                   CompanyInfoQueryRequest
                                                                                   companyInfoQueryRequest) {
        CompanyInfo companyInfo = storeCustomerService.getCompanyInfoByCustomerId(companyInfoQueryRequest.getCustomerId
                ());

        CompanyInfoGetResponse response = new CompanyInfoGetResponse();

        KsBeanUtil.copyPropertiesThird(companyInfo, response);

        return BaseResponse.success(response);
    }

    /**
     * 获取客户-商家的从属记录，一个客户只会从属于一个商家
     * <p>
     * {@link StoreCustomerService#getCompanyInfoByCustomerId}
     */
    @Override
    public BaseResponse<CompanyInfoGetResponse> getCompanyInfoBelongByCustomerId(@RequestBody @Valid
                                                                                         CompanyInfoQueryRequest
                                                                                         companyInfoQueryRequest) {
        CompanyInfo companyInfo = storeCustomerService.getCompanyInfoBelongByCustomerId(companyInfoQueryRequest.getCustomerId
                ());

        CompanyInfoGetResponse response = new CompanyInfoGetResponse();

        KsBeanUtil.copyPropertiesThird(companyInfo, response);

        return BaseResponse.success(response);
    }

    /**
     * 获取客户-商家的关联记录  （原findCustomerRelatedForAll接口和findCustomerRelatedForPlatform合成一个)
     * <p>
     * {@link StoreCustomerService#findCustomerRelatedForAll}
     * {@link StoreCustomerService#findCustomerRelatedForPlatform}
     */
    @Override

    public BaseResponse<StoreCustomerRelaResponse> getCustomerRelated(@RequestBody @Valid StoreCustomerRelaQueryRequest
                                                                              storeCustomerRelaQueryRequest) {
        StoreCustomerRela storeCustomerRela = storeCustomerService.findCustomerRelated(storeCustomerRelaQueryRequest
                        .getCustomerId(),
                storeCustomerRelaQueryRequest.getCompanyInfoId(), storeCustomerRelaQueryRequest.getQueryPlatform());

        StoreCustomerRelaResponse response = null;

        if (Objects.nonNull(storeCustomerRela)) {
            response = new StoreCustomerRelaResponse();
            KsBeanUtil.copyPropertiesThird(storeCustomerRela, response);
        }
        return BaseResponse.success(response);
    }

    /**
     * 获取和某个商家有关联关系的记录
     * <p>
     * {@link StoreCustomerService#findRelatedCustomerByCompanyId}
     */
    @Override

    public BaseResponse<StoreCustomerRelaListResponse> listRelatedCustomerByCompanyId(@RequestBody @Valid
                                                                                              StoreCustomerRelaQueryRequest storeCustomerRelaQueryRequest) {
        List<StoreCustomerRela> relaList = storeCustomerService.findRelatedCustomerByCompanyId
                (storeCustomerRelaQueryRequest
                        .getCompanyInfoId());

        List<StoreCustomerRelaVO> relaVOList = relaList.stream().map(rela -> {
            StoreCustomerRelaVO vo = new StoreCustomerRelaVO();

            KsBeanUtil.copyPropertiesThird(rela, vo);

            return vo;
        }).collect(Collectors.toList());

        return BaseResponse.success(new StoreCustomerRelaListResponse(relaVOList));
    }

    /**
     * 获取客户-商家的从属记录，一个客户只会从属于一个商家
     * <p>
     * {@link StoreCustomerService#findCustomerBelong}
     */
    @Override

    public BaseResponse<StoreCustomerRelaResponse> getCustomerBelong(@RequestBody @Valid
                                                                             StoreCustomerRelaQueryRequest
                                                                             storeCustomerRelaQueryRequest) {

        StoreCustomerRela storeCustomerRela = storeCustomerService.findCustomerBelongTwo(storeCustomerRelaQueryRequest
                .getCustomerId(), storeCustomerRelaQueryRequest.getCompanyInfoId());

        StoreCustomerRelaResponse response = new StoreCustomerRelaResponse();

        KsBeanUtil.copyPropertiesThird(storeCustomerRela, response);

        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<StoreCustomerRelaListByConditionResponse> listByCondition
            (@RequestBody @Valid StoreCustomerRelaListByConditionRequest request) {
        StoreCustomerRequest storeCustomerRequest = new StoreCustomerRequest();

        KsBeanUtil.copyPropertiesThird(request, storeCustomerRequest);
        storeCustomerRequest.setStoreIds(request.getStoreIds());
        List<StoreCustomerRela> relaList = storeCustomerService.list(storeCustomerRequest);

        List<StoreCustomerRelaVO> relaVOList = relaList.stream().map(storeCustomerRela -> {
            StoreCustomerRelaVO vo = new StoreCustomerRelaVO();

            KsBeanUtil.copyPropertiesThird(storeCustomerRela, vo);

            return vo;
        }).collect(Collectors.toList());

        return BaseResponse.success(new StoreCustomerRelaListByConditionResponse(relaVOList));
    }

    @Override
    public BaseResponse<StoreCustomerRelaListCustomerIdByStoreIdResponse> listCustomerIdByStoreId(@RequestBody @Valid StoreCustomerRelaListCustomerIdByStoreIdRequest request) {
        List<String> list = storeCustomerService.findCustomerIdByStoreId(request.getStoreId(), request.getStoreLevelIds(), request.getPageRequest());
        return BaseResponse.success(new StoreCustomerRelaListCustomerIdByStoreIdResponse(list));
    }
}
