package com.wanmi.sbc.job;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.StoreType;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.elastic.api.provider.settlement.EsSettlementProvider;
import com.wanmi.sbc.elastic.api.request.settlement.EsSettlementListRequest;
import com.wanmi.sbc.elastic.bean.dto.settlement.EsSettlementDTO;
import com.wanmi.sbc.order.api.provider.settlement.SettlementAnalyseProvider;
import com.wanmi.sbc.order.api.request.settlement.SettlementAnalyseRequest;
import com.wanmi.sbc.order.api.response.settlement.SettlementForEsResponse;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 定时任务Handler（Bean模式）
 * 商铺结算任务
 *
 * @author bail 2019-3-24
 */
@JobHandler(value="settlementAnalyseJobHandler")
@Component
@Slf4j
public class SettlementAnalyseJobHandler extends IJobHandler {
    @Autowired
    private SettlementAnalyseProvider settlementAnalyseProvider;


    @Autowired
    private EsSettlementProvider esSettlementProvider;

    @Override
    public ReturnT<String> execute(String param) throws Exception {

        BaseResponse<SettlementForEsResponse> response = settlementAnalyseProvider.analyse(new SettlementAnalyseRequest(param, StoreType.SUPPLIER));
        SettlementForEsResponse context = response.getContext();
        if(Objects.nonNull(context)){
            //同步结算单到es
            if(CollectionUtils.isNotEmpty(context.getLists())){
                esSettlementProvider.initSettlementList(EsSettlementListRequest.builder().lists(
                        context.getLists().stream().map(settlementViewVo -> {
                            EsSettlementDTO dto =  KsBeanUtil.convert(settlementViewVo, EsSettlementDTO.class);
                            return dto;
                        }).collect(Collectors.toList())
                ).build());

            }
        }
        return SUCCESS;
    }

}
