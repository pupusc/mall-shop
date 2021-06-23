package com.wanmi.sbc.order.provider.impl.settlement;

import com.wanmi.sbc.account.api.response.finance.record.SettlementAddResponse;
import com.wanmi.sbc.account.bean.vo.SettlementViewVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.settlement.SettlementAnalyseProvider;
import com.wanmi.sbc.order.api.request.settlement.SettlementAnalyseRequest;
import com.wanmi.sbc.order.api.response.settlement.SettlementForEsResponse;
import com.wanmi.sbc.order.settlement.SettlementAnalyseService;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-12-07 13:51
 */
@Validated
@RestController
public class SettlementAnalyseController implements SettlementAnalyseProvider {

    @Autowired
    private SettlementAnalyseService settlementAnalyseService;

    /**
     * 分析结算单
     *
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    @GlobalTransactional
    @Transactional
    public BaseResponse<SettlementForEsResponse> analyse(@RequestBody @Valid SettlementAnalyseRequest request) {
        List<SettlementAddResponse> responseList = settlementAnalyseService.analyseSettlement(new Date(), request);

        if(CollectionUtils.isEmpty(responseList)){
            return BaseResponse.success(new SettlementForEsResponse(Collections.emptyList()));
        }
        return BaseResponse.success( new SettlementForEsResponse(responseList.stream().map(settlementViewVo -> {
            SettlementViewVO vo = new SettlementViewVO();
            KsBeanUtil.copyPropertiesThird(settlementViewVo, vo);
            vo.setSettleCode(String.format("S%07d", vo.getSettleId()));
            return vo;
        }).collect(Collectors.toList())));

    }
}
