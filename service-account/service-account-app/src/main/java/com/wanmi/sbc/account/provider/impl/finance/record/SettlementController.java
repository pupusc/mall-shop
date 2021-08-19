package com.wanmi.sbc.account.provider.impl.finance.record;

import com.wanmi.sbc.account.api.provider.finance.record.SettlementProvider;
import com.wanmi.sbc.account.api.request.finance.record.SettlementAddRequest;
import com.wanmi.sbc.account.api.request.finance.record.SettlementBatchModifyStatusRequest;
import com.wanmi.sbc.account.api.request.finance.record.SettlementDeleteRequest;
import com.wanmi.sbc.account.api.response.finance.record.SettlementAddResponse;
import com.wanmi.sbc.account.finance.record.model.entity.Settlement;
import com.wanmi.sbc.account.finance.record.service.SettlementService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>对结算单操作接口</p>
 * Created by daiyitian on 2018-10-13-下午6:23.
 */
@RestController
@Validated
public class SettlementController implements SettlementProvider {

    @Autowired
    private SettlementService settlementService;

    @Override
    public BaseResponse<SettlementAddResponse> add(@RequestBody SettlementAddRequest request){
        Settlement settlement = KsBeanUtil.convert(request, Settlement.class);
        SettlementAddResponse response = KsBeanUtil.convert(settlementService.saveSettlement(settlement), SettlementAddResponse.class);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse delete(@RequestBody @Valid SettlementDeleteRequest request){
        settlementService.deleteSettlement(request.getStoreId(), request.getStartTime(), request.getEndTime());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse batchModifyStatus(@RequestBody @Valid SettlementBatchModifyStatusRequest request){
        settlementService.updateSettleStatus(request.getSettleIdList(), request.getStatus());
        return BaseResponse.SUCCESSFUL();
    }
}
