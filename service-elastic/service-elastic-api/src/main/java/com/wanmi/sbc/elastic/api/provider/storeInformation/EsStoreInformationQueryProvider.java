package com.wanmi.sbc.elastic.api.provider.storeInformation;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.storeInformation.EsCompanyAccountQueryRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.EsCompanyPageRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.StoreInfoQueryPageRequest;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsCompanyAccountResponse;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsCompanyInfoResponse;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsListStoreByNameForAutoCompleteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author yangzhen
 * @Description // 商家店铺信息
 * @Date 18:27 2020/12/7
 * @Param
 * @return
 **/
@FeignClient(value = "${application.elastic.name}", contextId = "EsStoreInformationQueryProvider")
public interface EsStoreInformationQueryProvider {


    @PostMapping("/elastic/${application.elastic.version}/storeInformation/company-account-page")
    BaseResponse<EsCompanyAccountResponse> companyAccountPage(@RequestBody @Valid EsCompanyAccountQueryRequest request);

    @PostMapping("/elastic/${application.elastic.version}/storeInformation/company-info-page")
    BaseResponse<EsCompanyInfoResponse> companyInfoPage(@RequestBody @Valid EsCompanyPageRequest request);

    @PostMapping("/elastic/${application.elastic.version}/storeInformation/es-list-by-name-for-auto-complete")
    BaseResponse<EsListStoreByNameForAutoCompleteResponse> queryStoreByNameAndStoreTypeForAutoComplete(@RequestBody @Valid StoreInfoQueryPageRequest request);

}
