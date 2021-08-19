package com.wanmi.sbc.account.provider.impl.finance.record;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wanmi.sbc.account.api.provider.finance.record.SettlementQueryProvider;
import com.wanmi.sbc.account.api.request.finance.record.*;
import com.wanmi.sbc.account.api.response.finance.record.*;
import com.wanmi.sbc.account.bean.vo.SettlementTotalVO;
import com.wanmi.sbc.account.bean.vo.SettlementViewVO;
import com.wanmi.sbc.account.finance.record.model.entity.Settlement;
import com.wanmi.sbc.account.finance.record.model.request.SettlementQueryRequest;
import com.wanmi.sbc.account.finance.record.model.response.SettlementView;
import com.wanmi.sbc.account.finance.record.service.SettlementService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>对结算单查询接口</p>
 * Created by daiyitian on 2018-10-13-下午6:23.
 */
@RestController
@Validated
public class SettlementQueryController implements SettlementQueryProvider {

    @Autowired
    private SettlementService settlementService;

    @Override
    public BaseResponse<SettlementPageResponse> page(@RequestBody SettlementPageRequest request) {
        SettlementQueryRequest queryRequest = new SettlementQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);
        Page<SettlementView> page = settlementService.querySettlementPage(queryRequest);
        List<SettlementViewVO> vos = page.getContent().stream().map(view -> {
            SettlementViewVO vo = new SettlementViewVO();
            KsBeanUtil.copyPropertiesThird(view, vo);
            return vo;
        }).collect(Collectors.toList());
        SettlementPageResponse response = SettlementPageResponse.builder()
                .settlementViewVOPage(new MicroServicePage<>(vos, request.getPageRequest(), page.getTotalElements())).build();
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<SettlementGetViewResponse> getView(@RequestBody @Valid SettlementGetViewRequest request) {
        Settlement settlement = KsBeanUtil.convert(request, Settlement.class, SerializerFeature.WriteDateUseDateFormat);
        SettlementGetViewResponse response = KsBeanUtil.convert(settlementService.getSettlementById(settlement), SettlementGetViewResponse.class);
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<SettlementGetByIdResponse> getById(@RequestBody @Valid SettlementGetByIdRequest request) {
        SettlementGetByIdResponse response = new SettlementGetByIdResponse();
        Settlement settlement = settlementService.querySettlementById(request.getSettleId());
        if (Objects.nonNull(settlement)) {
            response = KsBeanUtil.convert(settlement, SettlementGetByIdResponse.class);
        }
        return BaseResponse.success(response);
    }

    @Override
    public BaseResponse<SettlementCountResponse> count(@RequestBody SettlementCountRequest request) {
        SettlementQueryRequest queryRequest = new SettlementQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);
        return BaseResponse.success(
                SettlementCountResponse.builder().count(settlementService.countByTodo(queryRequest)).build());
    }

    @Override
    public BaseResponse<SettlementTotalResponse> countByStoreId(@RequestBody @Valid SettlementTotalByStoreIdRequest
                                                                        request) {
        SettlementTotalResponse response = SettlementTotalResponse.builder().settlementTotalVOList(
                settlementService.queryToTalSettlement(request.getStoreId())
                        .stream()
                        .map(total -> {
                            SettlementTotalVO vo = new SettlementTotalVO();
                            KsBeanUtil.copyPropertiesThird(total, vo);
                            return vo;
                        }).collect(Collectors.toList())
        ).build();
        return BaseResponse.success(response);
    }

    /**
     * 导出财务结算数据
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<SettlementToExcelResponse> getSettlementExportData(@RequestBody @Valid SettlementToExcelRequest request) {
        return BaseResponse.success(settlementService.getExportData(request));
    }

    @Override
    public BaseResponse<SettlementLastResponse> getLastSettlementByStoreId(@RequestBody @Valid SettlementLastByStoreIdRequest request){
        Settlement settlement = settlementService.getLastSettlementByStoreId(request.getStoreId());
        SettlementLastResponse response = new SettlementLastResponse();
        if (Objects.nonNull(settlement)) {
            response = KsBeanUtil.convert(settlement, SettlementLastResponse.class);
        }else {
            response = null;
        }
        return BaseResponse.success(response);
    }



    @Override
    public BaseResponse<SettlementListResponse> initEsPage(@RequestBody @Valid SettlementPageRequest request){
        SettlementQueryRequest queryRequest = new SettlementQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);
        return BaseResponse.success(new SettlementListResponse(settlementService.initEsPage(queryRequest)));
    }
}
