package com.wanmi.sbc.elastic.provider.impl.storeInformation;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.provider.storeInformation.EsStoreInformationQueryProvider;
import com.wanmi.sbc.elastic.api.request.storeInformation.EsCompanyAccountQueryRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.EsCompanyPageRequest;
import com.wanmi.sbc.elastic.api.request.storeInformation.StoreInfoQueryPageRequest;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsCompanyAccountResponse;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsCompanyInfoResponse;
import com.wanmi.sbc.elastic.api.response.storeInformation.EsListStoreByNameForAutoCompleteResponse;
import com.wanmi.sbc.elastic.storeInformation.service.EsCompanyAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author yangzhen
 * @Description //商家店铺信息
 * @Date 19:05 2020/12/7
 * @Param
 * @return
 **/
@RestController
@Validated
public class EsStoreInformationQueryController implements EsStoreInformationQueryProvider {

    @Autowired
    private EsCompanyAccountService esCompanyAccountService;


    @Override
    public BaseResponse<EsCompanyAccountResponse> companyAccountPage(@RequestBody @Valid EsCompanyAccountQueryRequest request) {
        return BaseResponse.success(esCompanyAccountService.companyAccountPage(request));
    }

    @Override
    public BaseResponse<EsCompanyInfoResponse> companyInfoPage(@RequestBody @Valid EsCompanyPageRequest request) {
        return BaseResponse.success(esCompanyAccountService.companyInfoPage(request));
    }

    @Override
    public BaseResponse<EsListStoreByNameForAutoCompleteResponse> queryStoreByNameAndStoreTypeForAutoComplete(@RequestBody @Valid StoreInfoQueryPageRequest request) {
        return BaseResponse.success(esCompanyAccountService.queryStoreByNameAndStoreTypeForAutoComplete(request));
    }

}
