package com.wanmi.sbc.account.provider.impl.finance.record;

import com.wanmi.sbc.account.bean.enums.AccountRecordType;
import com.wanmi.sbc.account.api.provider.finance.record.AccountRecordQueryProvider;
import com.wanmi.sbc.account.api.request.finance.record.AccountDetailsExportRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountDetailsPageRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountGatherListRequest;
import com.wanmi.sbc.account.api.request.finance.record.AccountRecordPageRequest;
import com.wanmi.sbc.account.api.response.finance.record.AccountDetailsExportResponse;
import com.wanmi.sbc.account.api.response.finance.record.AccountDetailsPageResponse;
import com.wanmi.sbc.account.api.response.finance.record.AccountGatherListResponse;
import com.wanmi.sbc.account.api.response.finance.record.AccountRecordPageResponse;
import com.wanmi.sbc.account.bean.vo.AccountDetailsVO;
import com.wanmi.sbc.account.bean.vo.AccountGatherVO;
import com.wanmi.sbc.account.bean.vo.AccountRecordVO;
import com.wanmi.sbc.account.finance.record.model.response.AccountDetails;
import com.wanmi.sbc.account.finance.record.model.response.AccountGather;
import com.wanmi.sbc.account.finance.record.model.response.AccountRecord;
import com.wanmi.sbc.account.finance.record.service.AccountRecordService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>对账记录查询接口</p>
 * Created by of628-wenzhi on 2018-10-12-下午5:51.
 */
@RestController
@Validated
public class AccountRecordQueryController implements AccountRecordQueryProvider {

    @Autowired
    private AccountRecordService accountRecordService;

    /**
     * 分页查询对账记录
     *
     * @param request 查询条件 {@link AccountRecordPageRequest}
     * @return 带分页的对账记录列表 {@link AccountRecordPageResponse}
     */

    public BaseResponse<AccountRecordPageResponse> pageAccountRecord(@RequestBody @Valid AccountRecordPageRequest
                                                                                 request) {
        Page<AccountRecord> page = accountRecordService.page(request, toType(request.getAccountRecordType()));
        List<AccountRecordVO> voList = page.getContent().stream().map(record -> {
            AccountRecordVO vo = new AccountRecordVO();
            KsBeanUtil.copyPropertiesThird(record, vo);
            return vo;
        }).collect(Collectors.toList());
        AccountRecordPageResponse response = AccountRecordPageResponse.builder()
                .accountRecordVOPage(new MicroServicePage<>(voList, request.getPageRequest(),  page.getTotalElements()))
                .build();
        return BaseResponse.success(response);
    }

    /**
     * 对账记录汇总
     *
     * @param request 汇总条件 {@link AccountGatherListRequest}
     * @return 对账汇总列表 {@link AccountGatherListResponse}
     */

    public BaseResponse<AccountGatherListResponse> listAccountGather(@RequestBody @Valid AccountGatherListRequest
                                                                                 request) {
        AccountRecordPageRequest pageRequest = new AccountRecordPageRequest();
        KsBeanUtil.copyPropertiesThird(request, pageRequest);
        List<AccountGather> gatherList = accountRecordService.summarizing(pageRequest, toType(request.getAccountRecordType()));
        List<AccountGatherVO> voList = gatherList.stream().map(gather -> {
            AccountGatherVO vo = new AccountGatherVO();
            KsBeanUtil.copyPropertiesThird(gather, vo);
            return vo;
        }).collect(Collectors.toList());
        return BaseResponse.success(AccountGatherListResponse.builder().accountGatherVOList(voList).build());
    }

    /**
     * 分页查询对账明细
     *
     * @param request 带分页的对账明细查询参数 {@link AccountDetailsPageRequest}
     * @return 对账明细分页记录 {@link AccountDetailsPageResponse}
     */

    public BaseResponse<AccountDetailsPageResponse> pageAccountDetails(@RequestBody @Valid AccountDetailsPageRequest
                                                                                   request) {
        AccountDetailsPageRequest pageRequest = new AccountDetailsPageRequest();
        KsBeanUtil.copyPropertiesThird(request, pageRequest);
        Page<AccountDetails> page = accountRecordService.pageDetails(pageRequest,
                toType(request.getAccountRecordType()));
        MicroServicePage<AccountDetailsVO> voPage =  new MicroServicePage<>(warperVos(page.getContent()),
                request.getPageRequest(), page.getTotalElements());
        return BaseResponse.success(AccountDetailsPageResponse.builder().accountDetailsVOPage(voPage).build());
    }

    /**
     * 对账明细(收入/退款)导出数据查询
     * @param request 账户明细导出数据查询请求参数 {@link AccountDetailsExportRequest}
     * @return （收入/退款）明细导出查询返回数据结构 {@link AccountDetailsExportResponse}
     */

    public BaseResponse<AccountDetailsExportResponse> exportAccountDetailsLoad(@RequestBody @Valid AccountDetailsExportRequest
                                                                                           request) {
        return BaseResponse.success(AccountDetailsExportResponse.builder().accountDetailsVOList(
                warperVos(accountRecordService.exportDetailsLoad(request, toType(request.getAccountRecordType())))
        ).build());
    }

    private List<AccountDetailsVO> warperVos(List<AccountDetails> details){
        if(CollectionUtils.isNotEmpty(details)){
            return details.stream().map(detail -> {
                AccountDetailsVO vo = new AccountDetailsVO();
                KsBeanUtil.copyPropertiesThird(detail, vo);
                return vo;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private Byte toType(AccountRecordType type){
        return Byte.parseByte(String.valueOf(type.toValue()));
    }
}
