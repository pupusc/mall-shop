package com.wanmi.sbc.customer.provider.impl.company;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.company.*;
import com.wanmi.sbc.customer.api.response.company.*;
import com.wanmi.sbc.customer.api.response.store.StoreListForDistributionResponse;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.customer.company.model.root.CompanyInfo;
import com.wanmi.sbc.customer.company.request.CompanyRequest;
import com.wanmi.sbc.customer.company.service.CompanyInfoService;
import com.wanmi.sbc.customer.store.model.root.Store;
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
 * <p>公司信息查询接口实现</p>
 * Created by of628-wenzhi on 2018-08-18-下午4:41.
 */
@RestController
@Validated
public class CompanyInfoQueryController implements CompanyInfoQueryProvider {

    @Autowired
    private CompanyInfoService companyInfoService;


    @Override

    public BaseResponse<CompanyInfoGetResponse> getCompanyInfo() {
        CompanyInfoGetResponse response = new CompanyInfoGetResponse();
        wraperVo(companyInfoService.findCompanyInfo(), response);
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CompanyInfoGetWithAddResponse> getCompanyInfoWithAdd() {
        CompanyInfoGetWithAddResponse response = new CompanyInfoGetWithAddResponse();
        wraperVo(companyInfoService.getCompanyInfo(), response);
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CompanyInfoByIdResponse> getCompanyInfoById(@RequestBody CompanyInfoByIdRequest request) {
        CompanyInfoByIdResponse response = new CompanyInfoByIdResponse();
        wraperVo(companyInfoService.findOne(request.getCompanyInfoId()), response);
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CompanyInfoPageResponse> pageCompanyInfo(@RequestBody CompanyPageRequest request) {
        CompanyRequest companyRequest = new CompanyRequest();
        KsBeanUtil.copyPropertiesThird(request, companyRequest);
        Page<CompanyInfo> companyInfoPage = companyInfoService.findAll(companyRequest);
        List<CompanyInfoVO> voList = wraperCompanyList(companyInfoPage.getContent());
        CompanyInfoPageResponse response = CompanyInfoPageResponse.builder()
                .companyInfoVOPage(new MicroServicePage<>(voList, request.getPageable(), companyInfoPage.getTotalElements()))
                .build();
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CompanyInfoListResponse> listCompanyInfo(@RequestBody CompanyListRequest request) {
        CompanyRequest companyRequest = new CompanyRequest();
        KsBeanUtil.copyPropertiesThird(request, companyRequest);
        CompanyInfoListResponse response = CompanyInfoListResponse.builder()
                .companyInfoVOList(wraperCompanyList(companyInfoService.findAllList(companyRequest)))
                .build();
        return BaseResponse.success(response);
    }

    @Override

    public BaseResponse<CompanyAccountPageResponse> pageCompanyAccount(@RequestBody CompanyAccountPageRequest request) {
        CompanyRequest companyRequest = new CompanyRequest();
        KsBeanUtil.copyPropertiesThird(request, companyRequest);
        Page<CompanyAccountResponse> responsePage = companyInfoService.accountList(companyRequest);
        List<CompanyAccountVO> voList = wraperCompanyAccountList(responsePage.getContent());
        CompanyAccountPageResponse response = CompanyAccountPageResponse.builder()
                .companyAccountVOPage(new MicroServicePage<>(voList, request.getPageable(), responsePage.getTotalElements()))
                .build();
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<CompanyWaitCheckCountResponse> countCompanyByWaitCheck() {
        CompanyWaitCheckCountResponse response = CompanyWaitCheckCountResponse.builder().count(companyInfoService
                .countByTodo())
                .build();
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<CompanyInfoQueryByIdsResponse> queryByCompanyInfoIds(@RequestBody @Valid CompanyInfoQueryByIdsRequest request) {
        List<CompanyInfo> companyInfoList = companyInfoService.queryByCompanyinfoIds(request);
        CompanyInfoQueryByIdsResponse response = new CompanyInfoQueryByIdsResponse();
        List<CompanyInfoVO> companyInfoVOList = new ArrayList<>();
        companyInfoList.forEach(info->{
            CompanyInfoVO vo = new CompanyInfoVO();
            KsBeanUtil.copyProperties(info,vo);
            vo.setStoreVOList(KsBeanUtil.copyListProperties(info.getStoreList(),StoreVO.class));
            vo.setEmployeeVOList(KsBeanUtil.copyListProperties(info.getEmployeeList(),EmployeeVO.class));
            companyInfoVOList.add(vo);
        });
        response.setCompanyInfoList(companyInfoVOList);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<StoreListForDistributionResponse> queryByCompanyCode(@RequestBody @Valid
                                                                                             CompanyInfoForDistributionRecordRequest recordRequest){
        List<StoreSimpleInfo> storeInfoResponses = new ArrayList<>();
        CompanyRequest companyRequest = new CompanyRequest();
        KsBeanUtil.copyPropertiesThird(recordRequest, companyRequest);
        Page<CompanyInfo> companyInfoPage = companyInfoService.findAll(companyRequest);
        if(CollectionUtils.isNotEmpty(companyInfoPage.getContent())){
            companyInfoPage.getContent().forEach(companyInfo -> {
                StoreSimpleInfo storeSimpleInfo = new StoreSimpleInfo();
                List<Store> stores = companyInfo.getStoreList();
                //这里由于目前一个商家开一个店铺，所以获取第一个店铺的信息
                if(CollectionUtils.isNotEmpty(stores) && stores.size() > 0){
                    storeSimpleInfo.setStoreId(stores.get(0).getStoreId());
                    storeSimpleInfo.setCompanyCode(companyInfo.getCompanyCode());
                    storeSimpleInfo.setStoreName(stores.get(0).getStoreName());
                }
                storeInfoResponses.add(storeSimpleInfo);
            });
        }
        StoreListForDistributionResponse response = new StoreListForDistributionResponse(storeInfoResponses);
        return BaseResponse.success(response);
    }

    private List<CompanyAccountVO> wraperCompanyAccountList(List<CompanyAccountResponse> accountResponseList) {
        if (CollectionUtils.isNotEmpty(accountResponseList)) {
            return accountResponseList.stream().map(response -> {
                CompanyAccountVO vo = new CompanyAccountVO();
                KsBeanUtil.copyPropertiesThird(response, vo);
                return vo;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private List<CompanyInfoVO> wraperCompanyList(List<CompanyInfo> companyInfos) {
        if (CollectionUtils.isNotEmpty(companyInfos)) {
            return companyInfos.stream().map(companyInfo -> {
                CompanyInfoVO vo = new CompanyInfoVO();
                wraperVo(companyInfo, vo);
                return vo;
            }).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private void wraperVo(CompanyInfo companyInfo, CompanyInfoVO companyInfoVO) {
        if (companyInfo != null) {
            KsBeanUtil.copyPropertiesThird(companyInfo, companyInfoVO);

            if(CollectionUtils.isNotEmpty(companyInfo.getStoreList())) {
                companyInfoVO.setStoreVOList(companyInfo.getStoreList().stream().map(store -> {
                    StoreVO vo = new StoreVO();
                    KsBeanUtil.copyPropertiesThird(store, vo);
                    return vo;
                }).collect(Collectors.toList()));
            }

            if(CollectionUtils.isNotEmpty(companyInfo.getEmployeeList())) {
                companyInfoVO.setEmployeeVOList(companyInfo.getEmployeeList().stream().map(employee -> {
                    EmployeeVO vo = new EmployeeVO();
                    KsBeanUtil.copyPropertiesThird(employee, vo);
                    return vo;
                }).collect(Collectors.toList()));
            }
        }
    }
}
