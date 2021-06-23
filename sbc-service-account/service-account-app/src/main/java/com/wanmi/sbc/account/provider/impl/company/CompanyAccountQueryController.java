package com.wanmi.sbc.account.provider.impl.company;

import com.wanmi.sbc.account.api.provider.company.CompanyAccountQueryProvider;
import com.wanmi.sbc.account.api.request.company.CompanyAccountByCompanyInfoIdAndDefaultFlagRequest;
import com.wanmi.sbc.account.api.request.company.CompanyAccountCountByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.request.company.CompanyAccountFindByAccountIdRequest;
import com.wanmi.sbc.account.api.response.company.CompanyAccountCountResponse;
import com.wanmi.sbc.account.api.response.company.CompanyAccountFindResponse;
import com.wanmi.sbc.account.api.response.company.CompanyAccountListResponse;
import com.wanmi.sbc.account.bean.vo.CompanyAccountVO;
import com.wanmi.sbc.account.company.model.root.CompanyAccount;
import com.wanmi.sbc.account.company.service.CompanyAccountService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>对商家收款账户查询接口</p>
 * Created by daiyitian on 2018-10-13-下午6:23.
 */
@RestController
@Validated
public class CompanyAccountQueryController implements CompanyAccountQueryProvider {

    @Autowired
    private CompanyAccountService companyAccountService;

    /**
     * 根据商户编号和默认标识查询商家收款账户列表
     *
     * @param request 查询商家收款账户数据结构 {@link CompanyAccountByCompanyInfoIdAndDefaultFlagRequest}
     * @return {@link BaseResponse<CompanyAccountListResponse>}
     */

    public BaseResponse<CompanyAccountListResponse> listByCompanyInfoIdAndDefaultFlag(@RequestBody @Valid
                                                                                              CompanyAccountByCompanyInfoIdAndDefaultFlagRequest request) {
        List<CompanyAccountVO> companyAccounts = companyAccountService.findOfflineAccounts(request.getCompanyInfoId()
                , request.getDefaultFlag()).stream()
                .map(account -> {
                    CompanyAccountVO vo = new CompanyAccountVO();
                    KsBeanUtil.copyPropertiesThird(account, vo);
                    return vo;
                }).collect(Collectors.toList());
        return BaseResponse.success(CompanyAccountListResponse.builder().companyAccountVOList(companyAccounts).build());
    }

    /**
     * 统计商家收款账户
     *
     * @param request 统计商家收款账户数据结构 {@link CompanyAccountCountByCompanyInfoIdRequest}
     * @return {@link BaseResponse<CompanyAccountCountResponse>}
     */

    public BaseResponse<CompanyAccountCountResponse> countByCompanyInfoId(@RequestBody @Valid
                                                                                  CompanyAccountCountByCompanyInfoIdRequest
                                                                                  request) {
        return BaseResponse.success(CompanyAccountCountResponse.builder()
                .count(companyAccountService.countOffline(request.getCompanyInfoId())).build());
    }

    /**
     * 根据账户id查询商家收款账户信息
     *
     * @param request 账户id {@link CompanyAccountFindByAccountIdRequest}
     * @return
     */
    @Override
    public BaseResponse<CompanyAccountFindResponse> getByAccountId(@RequestBody @Valid CompanyAccountFindByAccountIdRequest request) {
        CompanyAccountFindResponse response = new CompanyAccountFindResponse();
        companyAccountService.findByAccountId(request.getAccountId()).ifPresent(account -> wraperVo(account, response));
        return BaseResponse.success(response);
    }

    /**
     * 转换实体类 entity to vo
     *
     * @param account   实体
     * @param accountVO 实体视图
     */
    private void wraperVo(CompanyAccount account, CompanyAccountVO accountVO) {
        KsBeanUtil.copyPropertiesThird(account, accountVO);
    }
}
