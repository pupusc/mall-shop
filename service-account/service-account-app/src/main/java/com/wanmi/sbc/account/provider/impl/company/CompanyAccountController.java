package com.wanmi.sbc.account.provider.impl.company;

import com.wanmi.sbc.account.api.provider.company.CompanyAccountProvider;
import com.wanmi.sbc.account.api.request.company.*;
import com.wanmi.sbc.account.api.response.company.CompanyAccountAddResponse;
import com.wanmi.sbc.account.api.response.company.CompanyAccountModifyResponse;
import com.wanmi.sbc.account.api.response.storeInformation.StoreAuditStateResponse;
import com.wanmi.sbc.account.bean.enums.AuditStatus;
import com.wanmi.sbc.account.bean.vo.CompanyAccountVO;
import com.wanmi.sbc.account.company.model.root.CompanyAccount;
import com.wanmi.sbc.account.company.request.CompanyAccountSaveRequest;
import com.wanmi.sbc.account.company.service.CompanyAccountService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>对商家收款账户操作接口</p>
 * Created by of628-wenzhi on 2018-10-13-下午6:23.
 */
@RestController
@Validated
public class CompanyAccountController implements CompanyAccountProvider {

    @Autowired
    private CompanyAccountService companyAccountService;


    public BaseResponse<CompanyAccountAddResponse> add(@RequestBody @Valid CompanyAccountAddRequest request){
        CompanyAccountSaveRequest saveRequest = new CompanyAccountSaveRequest();
        KsBeanUtil.copyPropertiesThird(request, saveRequest);
        CompanyAccountAddResponse response = new CompanyAccountAddResponse();
        companyAccountService.addOffLineAccount(saveRequest).ifPresent(account -> wraperVo(account, response));
        return BaseResponse.success(response);
    }


    public BaseResponse delete(@RequestBody @Valid CompanyAccountDeleteByIdRequest request){
        if(companyAccountService.removeOfflineById(request.getOfflineAccountId()) > 0){
            return BaseResponse.SUCCESSFUL();
        }
        return BaseResponse.FAILED();
    }


    public BaseResponse<CompanyAccountModifyResponse> modify(@RequestBody @Valid CompanyAccountModifyRequest request){
        CompanyAccountSaveRequest saveRequest = new CompanyAccountSaveRequest();
        KsBeanUtil.copyPropertiesThird(request, saveRequest);
        CompanyAccountModifyResponse response = new CompanyAccountModifyResponse();
        companyAccountService.modifyLineAccount(saveRequest).ifPresent(account -> wraperVo(account, response));
        return BaseResponse.success(response);
    }


    public BaseResponse modifyPrimary(@RequestBody @Valid CompanyAccountModifyPrimaryRequest request){
        companyAccountService.setPrimary(request.getCompanyInfoId(), request.getAccountId());
        return BaseResponse.SUCCESSFUL();
    }


    public BaseResponse batchRenewal(@RequestBody @Valid CompanyAccountBatchRenewalRequest request){
        List<CompanyAccountSaveRequest> offlineAccounts =
                request.getCompanyAccountSaveDTOList().stream()
                .map(dto -> {
                    CompanyAccountSaveRequest saveRequest = new CompanyAccountSaveRequest();
                    KsBeanUtil.copyPropertiesThird(dto, saveRequest);
                    return saveRequest;
                }).collect(Collectors.toList());
        companyAccountService.renewalOfflines(offlineAccounts, request.getAccountIds(), request.getCompanyInfoId());
        return BaseResponse.SUCCESSFUL();
    }

    public BaseResponse<StoreAuditStateResponse> batchRenewalReturnState(@RequestBody @Valid CompanyAccountBatchRenewalRequest request){
        List<CompanyAccountSaveRequest> offlineAccounts =
                request.getCompanyAccountSaveDTOList().stream()
                .map(dto -> {
                    CompanyAccountSaveRequest saveRequest = new CompanyAccountSaveRequest();
                    KsBeanUtil.copyPropertiesThird(dto, saveRequest);
                    return saveRequest;
                }).collect(Collectors.toList());
        return BaseResponse.success(companyAccountService.renewalOfflinesReturnState(
                offlineAccounts, request.getAccountIds(), request.getCompanyInfoId()));
    }


    public BaseResponse remit(@RequestBody @Valid CompanyAccountRemitRequest request){
        CompanyAccountSaveRequest saveRequest = new CompanyAccountSaveRequest();
        KsBeanUtil.copyPropertiesThird(request, saveRequest);
        companyAccountService.accountRemit(saveRequest);
        return BaseResponse.SUCCESSFUL();
    }


    public BaseResponse affirmRemit(@RequestBody @Valid CompanyAccountAffirmRemitRequest request){
        companyAccountService.affirmRemit(request.getCompanyInfoId(), request.getAccountId());
        return BaseResponse.SUCCESSFUL();
    }


    public BaseResponse<BoolFlag> affirmRemitAndReturn(@RequestBody @Valid CompanyAccountAffirmRemitRequest request){
        return BaseResponse.success(companyAccountService.affirmRemitAndReturn(request.getCompanyInfoId(), request.getAccountId()));
    }

    /**
     * 转换实体类 entity to vo
     * @param account 实体
     * @param accountVO 实体视图
     */
    private void wraperVo(CompanyAccount account, CompanyAccountVO accountVO){
        KsBeanUtil.copyPropertiesThird(account, accountVO);
    }
}
