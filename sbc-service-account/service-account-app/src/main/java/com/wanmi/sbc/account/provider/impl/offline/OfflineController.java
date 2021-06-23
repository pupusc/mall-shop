package com.wanmi.sbc.account.provider.impl.offline;

import com.wanmi.sbc.account.api.provider.offline.OfflineProvider;
import com.wanmi.sbc.account.api.request.offline.*;
import com.wanmi.sbc.account.api.response.offline.*;
import com.wanmi.sbc.account.offline.OfflineAccount;
import com.wanmi.sbc.account.offline.OfflineService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@Validated
public class OfflineController implements OfflineProvider {

    @Autowired
    private OfflineService offlineService;

    /**
     * 新增线下账户
     *
     * {@link OfflineService#addOffLineAccount}
     */

    @Override
    public BaseResponse<OfflineAccountAddResponse> add(@RequestBody @Valid OfflineAccountAddRequest request) {
        Optional<OfflineAccount> optional = offlineService.addOffLineAccount(request);

        OfflineAccountAddResponse response = new OfflineAccountAddResponse();

        optional.ifPresent(offlineAccount -> KsBeanUtil.copyPropertiesThird(offlineAccount, response));

        return BaseResponse.success(response);
    }

    /**
     * 修改线下账户
     *
     * {@link OfflineService#modifyLineAccount}
     */

    @Override
    public BaseResponse<OfflineAccountModifyResponse> modify(@RequestBody @Valid OfflineAccountModifyRequest request) {
        Optional<OfflineAccount> optional = offlineService.modifyLineAccount(request);

        OfflineAccountModifyResponse response = new OfflineAccountModifyResponse();

        optional.ifPresent(offlineAccount -> KsBeanUtil.copyPropertiesThird(offlineAccount, response));

        return BaseResponse.success(response);
    }

    /**
     * 删除线下账户
     *
     * {@link OfflineService#removeOfflineById}
     */

    @Override
    public BaseResponse<OfflineAccountDeleteByIdResponse> deleteById(@RequestBody @Valid OfflineAccountDeleteByIdRequest request) {
        int count  = offlineService.removeOfflineById(request.getOfflineAccountId());

        return BaseResponse.success(new OfflineAccountDeleteByIdResponse(count));
    }

    /**
     * 禁用银行账号
     *
     * {@link OfflineService#disableOfflineById}
     */

    @Override
    public BaseResponse<OfflineDisableByIdResponse> disableById(@RequestBody @Valid OfflineDisableByIdRequest request) {
        int count = offlineService.disableOfflineById(request.getOfflineAccountId());

        return BaseResponse.success(new OfflineDisableByIdResponse(count));
    }

    /**
     * 启用银行账号
     *
     * {@link OfflineService#enableOfflineById}
     */

    @Override
    public BaseResponse<OfflineEnableByIdResponse> enableById(@RequestBody @Valid OfflineEnableByIdRequest request) {
        int count = offlineService.enableOfflineById(request.getOfflineAccountId());

        return BaseResponse.success(new OfflineEnableByIdResponse(count));
    }

    /**
     * 更新商家财务信息(新增、修改、删除)
     *
     * {@link OfflineService#renewalOfflines}
     */

    @Override
    public BaseResponse renewalOfflines(@RequestBody @Valid OfflinesRenewalRequest request) {
        offlineService.renewalOfflines(request.getSaveDTOList(), request.getIds(), request.getCompanyInfoId());

        return BaseResponse.SUCCESSFUL();
    }
}
