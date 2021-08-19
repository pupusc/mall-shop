package com.wanmi.sbc.order.provider.impl.settlement;

import com.wanmi.sbc.order.api.provider.settlement.SettlementDetailProvider;
import com.wanmi.sbc.order.api.request.settlement.SettlementDetailAddRequest;
import com.wanmi.sbc.order.api.request.settlement.SettlementDetailDeleteRequest;
import com.wanmi.sbc.order.api.request.settlement.SettlementDetailListAddRequest;
import com.wanmi.sbc.order.settlement.model.root.SettlementDetail;
import com.wanmi.sbc.order.settlement.service.SettlementDetailService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 结算明细
 */
@RestController
@Validated
public class SettlementDetailController implements SettlementDetailProvider {

    @Autowired
    private SettlementDetailService settlementDetailService;

    /**
     * 新增计算明细列表
     *
     * @param settlementDetailListAddRequest 新增结算明细列表数据结构 {@link SettlementDetailListAddRequest}
     * @return {@link BaseResponse}
     */

    @Override
    public BaseResponse addList(@RequestBody @Valid SettlementDetailListAddRequest settlementDetailListAddRequest) {
        List<SettlementDetail> settlementDetailList = KsBeanUtil.convert(settlementDetailListAddRequest.getSettlementDetailDTOList(),SettlementDetail.class);
        settlementDetailService.save(settlementDetailList);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 新增单条结算明细
     *
     * @param settlementDetailAddRequest 新增结算明细数据结构 {@link SettlementDetailAddRequest}
     * @return {@link BaseResponse}
     */

    @Override
    public BaseResponse add(@RequestBody @Valid SettlementDetailAddRequest settlementDetailAddRequest) {
        SettlementDetail settlementDetail = KsBeanUtil.convert(settlementDetailAddRequest.getSettlementDetailDTO(),SettlementDetail.class);
        settlementDetailService.save(settlementDetail);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据条件删除结算明细
     *
     * @param settlementDetailDeleteRequest 删除条件 {@link SettlementDetailDeleteRequest}
     * @return {@link BaseResponse}
     */

    @Override
    public BaseResponse delete(@RequestBody @Valid SettlementDetailDeleteRequest settlementDetailDeleteRequest) {
        settlementDetailService.deleteSettlement(settlementDetailDeleteRequest.getStoreId(),settlementDetailDeleteRequest.getStartDate(),settlementDetailDeleteRequest.getEndDate());
        return BaseResponse.SUCCESSFUL();
    }
}
