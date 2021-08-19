package com.wanmi.sbc.customer.provider.impl.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.store.StoreProvider;
import com.wanmi.sbc.customer.api.request.store.*;
import com.wanmi.sbc.customer.api.response.store.*;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.EsStoreInfoVo;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.customer.store.model.root.Store;
import com.wanmi.sbc.customer.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>店铺操作接口实现</p>
 * Created by of628-wenzhi on 2018-09-25-下午3:18.
 */
@RestController
@Validated
public class StoreController implements StoreProvider {

    @Autowired
    private StoreService storeService;

    @Override
    public BaseResponse<InitStoreByCompanyResponse> initStoreByCompany(@RequestBody @Valid InitStoreByCompanyRequest
                                                                               initStoreByCompanyRequest) {
        Store store = storeService.getStore(initStoreByCompanyRequest.getCompanyInfoId());
        return BaseResponse.success(new InitStoreByCompanyResponse(storeService.wraper2VoFromStore(store)));
    }

    @Override
    public BaseResponse<StoreAddResponse> add(@RequestBody @Valid StoreAddRequest storeAddRequest) {
        StoreSaveRequest saveRequest = new StoreSaveRequest();
        KsBeanUtil.copyPropertiesThird(storeAddRequest, saveRequest);
        return BaseResponse.success(new StoreAddResponse(storeService.wraper2VoFromStore(storeService.saveStore
                (saveRequest))));
    }

    @Override
    public BaseResponse modifyStoreBaseInfo(@RequestBody @Valid StoreModifyLogoRequest storeModifyLogoRequest) {
        storeService.updateStoreBaseInfo(storeModifyLogoRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<StoreModifyResponse> modifyStoreInfo(@RequestBody @Valid StoreModifyRequest request) {
        Store store = storeService.modifyStoreBaseInfo(request);

        StoreModifyResponse response = new StoreModifyResponse();

        KsBeanUtil.copyPropertiesThird(store, response);
        CompanyInfoVO companyInfoVO = new CompanyInfoVO();
        response.setCompanyInfo(companyInfoVO);
        KsBeanUtil.copyPropertiesThird(store.getCompanyInfo(),companyInfoVO);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<AccountDateModifyResponse> accountDateModify(@RequestBody @Valid AccountDateModifyRequest
                                                                                 request) {
        Store store = storeService.update(request);
        return BaseResponse.success(new AccountDateModifyResponse(storeService.wraper2VoFromStore(store)));
    }

    @Override
    public BaseResponse<StoreSwitchResponse> switchStore(@RequestBody @Valid StoreSwitchRequest storeSwitchRequest) {
        Store store = storeService.closeOrOpen(storeSwitchRequest);
        return BaseResponse.success(new StoreSwitchResponse(KsBeanUtil.convert(store, StoreVO.class)));
    }

    @Override
    public BaseResponse<StoreAuditResponse> auditStore(@RequestBody @Valid StoreAuditRequest storeAuditRequest) {
        Store store = storeService.rejectOrPass(storeAuditRequest);
        return BaseResponse.success(new StoreAuditResponse(storeService.wraper2VoFromStore(store)));
    }

    @Override
    public BaseResponse<StoreCheckResponse> checkStore(@RequestBody @Valid StoreCheckRequest storeCheckRequest) {
        boolean result = storeService.checkStore(storeCheckRequest.getIds());
        return BaseResponse.success(new StoreCheckResponse(result));
    }

    @Override
    public BaseResponse<StoreContractModifyResponse> modifyStoreContract(@RequestBody @Valid
                                                                                     StoreContractModifyRequest
                                                                                     storeContractModifyRequest) {
        Store store = storeService.updateStoreContract(storeContractModifyRequest);
        return BaseResponse.success(new StoreContractModifyResponse(storeService.wraper2VoFromStore(store)));
    }

    @Override
    public BaseResponse modifyAuditState(@RequestBody @Valid StoreAuditStateModifyRequest request) {
        storeService.modifyAuditState(request);

        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateStoreSmallProgram(@RequestBody @Valid StoreInfoSmallProgramRequest request){
        storeService.updateStoreSmallProgram(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse clearStoreProgramCode(){
        storeService.clearStoreProgramCode();
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<EsStoreInfoResponse> queryEsStoreInfoByPage(@RequestBody @Valid EsStoreInfoQueryRequest request){
        List<EsStoreInfoVo> lists = storeService.queryEsStoreInfoByPage(request.getPageNum(),request.getPageSize());
        return BaseResponse.success(new EsStoreInfoResponse(lists));
    }

    @Override
    public BaseResponse switchStoreBatch(@RequestBody @Valid StoreSwitchBatchRequest storeSwitchBatchRequest) {
        storeService.closeOrOpenBatch(storeSwitchBatchRequest);
        return BaseResponse.SUCCESSFUL();
    }
}
