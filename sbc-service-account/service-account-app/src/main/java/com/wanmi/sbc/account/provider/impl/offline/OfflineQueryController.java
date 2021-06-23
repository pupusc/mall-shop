package com.wanmi.sbc.account.provider.impl.offline;

import com.wanmi.sbc.account.api.provider.offline.OfflineQueryProvider;
import com.wanmi.sbc.account.api.request.offline.*;
import com.wanmi.sbc.account.api.response.offline.*;
import com.wanmi.sbc.account.bean.vo.OfflineAccountVO;
import com.wanmi.sbc.account.offline.OfflineAccount;
import com.wanmi.sbc.account.offline.OfflineService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Validated
public class OfflineQueryController implements OfflineQueryProvider {
    @Autowired
    private OfflineService offlineService;

    /**
     * 查询线下账户
     *
     * {@link OfflineService#findOfflineAccountById}
     */

    @Override
    public BaseResponse<OfflineAccountGetByIdResponse> getById(@RequestBody @Valid OfflineAccountGetByIdRequest request) {
        Optional<OfflineAccount> optional = offlineService.findOfflineAccountById(request.getOfflineAccountId());
        OfflineAccountGetByIdResponse response = new OfflineAccountGetByIdResponse();

        optional.ifPresent(offlineAccount -> KsBeanUtil.copyPropertiesThird(offlineAccount, response));

        return BaseResponse.success(response);
    }

    /**
     * 账号管理查询
     *
     * {@link OfflineService#findManagerAccounts}
     */

    @Override
    public BaseResponse<ManagerAccountListResponse> listManagerAccount() {
        List<OfflineAccount> list = offlineService.findManagerAccounts();

        return BaseResponse.success(new ManagerAccountListResponse(convertToVO(list)));
    }

    /**
     * 根据公司id和是否收到平台首次打款查询线下账户列表
     *
     * {@link OfflineService#findOfflineAccounts}
     */

    @Override
    public BaseResponse<OfflineAccountListByConditionResponse> listByCondition(@RequestBody @Valid OfflineAccountListByConditionRequest
                                                                                           request) {
        List<OfflineAccount> list = offlineService.findOfflineAccounts(request.getCompanyInfoId(), request
                .getDefaultFlag());

        return BaseResponse.success(new OfflineAccountListByConditionResponse(convertToVO(list)));
    }

    /**
     * 查询所有线下账户
     *
     * {@link OfflineService#findAll}
     */

    @Override
    public BaseResponse<OfflineAccountListResponse> list() {
        List<OfflineAccount> list = offlineService.findAll();

        return BaseResponse.success(new OfflineAccountListResponse(convertToVO(list)));
    }

    /**
     * 查询所有有效线下账户
     *
     * {@link OfflineService#findValidAccounts}
     */

    @Override
    public BaseResponse<ValidAccountListResponse> listValidAccount() {
        List<OfflineAccount> list = offlineService.findValidAccounts();

        return BaseResponse.success(new ValidAccountListResponse(convertToVO(list)));
    }

    /**
     * 统计商家财务信息
     *
     * {@link OfflineService#countOffline}
     */

    @Override
    public BaseResponse<OfflineCountResponse> countOffline(@RequestBody @Valid OfflineCountRequest request) {
        int count = offlineService.countOffline(request.getCompanyInfoId());

        return BaseResponse.success(new OfflineCountResponse(count));
    }

    /**
     * 根据银行账号查询所有账户
     *
     * @param request {@link OfflineAccountListByBankNoRequest}
     * @return 线下账户列表 {@link OfflineAccountListByBankNoResponse}
     */
    @Override

    public BaseResponse<OfflineAccountListByBankNoResponse> listByBankNo(@RequestBody @Valid OfflineAccountListByBankNoRequest request) {
        List<OfflineAccount> list = offlineService.listOfflineAccount(request.getBankNo());

        return BaseResponse.success(new OfflineAccountListByBankNoResponse(convertToVO(list)));
    }

    @Override
    public BaseResponse<OfflineAccountListWithoutDeleteFlagByBankNoResponse> listWithOutDeleteFlagByBankNo(@RequestBody @Valid OfflineAccountListWithoutDeleteFlagByBankNoRequest request) {
        List<OfflineAccount> list = offlineService.listOfflineAccountWithOutDeleteFlag(request.getBankNo());

        return BaseResponse.success(new OfflineAccountListWithoutDeleteFlagByBankNoResponse(convertToVO(list)));
    }

    /**
     * 对象转换
     *
     * @param list 线下账户列表
     * @return 线下账户VO列表
     */
    private List<OfflineAccountVO> convertToVO(List<OfflineAccount> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            return list.stream().map(offlineAccount -> {
                OfflineAccountVO vo = new OfflineAccountVO();

                KsBeanUtil.copyPropertiesThird(offlineAccount, vo);

                return vo;
            }).collect(Collectors.toList());
        } else {
            return Collections.EMPTY_LIST;
        }
    }
}
