package com.wanmi.sbc.account.api.provider.finance.record;

import com.wanmi.sbc.account.api.request.finance.record.*;
import com.wanmi.sbc.account.api.response.finance.record.*;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对结算单操作接口</p>
 * Created by daiyitian on 2018-10-13-下午6:23.
 */
@FeignClient(value = "${application.account.name}", contextId = "SettlementQueryProvider")
public interface SettlementQueryProvider {

    /**
     * 结算单视图分页查询
     *
     * @param request 结算单视图分页条件数据结构 {@link SettlementPageRequest}
     * @return {@link SettlementPageResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/settlement/page")
    BaseResponse<SettlementPageResponse> page(@RequestBody SettlementPageRequest request);

    /**
     * 获取结算单视图
     *
     * @param request 获取结算单视图数据结构 {@link SettlementGetViewRequest}
     * @return {@link SettlementGetViewResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/settlement/get-view")
    BaseResponse<SettlementGetViewResponse> getView(@RequestBody @Valid SettlementGetViewRequest request);

    /**
     * 根据编号获取结算单
     *
     * @param request 结算单编号数据结构 {@link SettlementGetByIdRequest}
     * @return {@link SettlementGetByIdResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/settlement/get-by-id")
    BaseResponse<SettlementGetByIdResponse> getById(@RequestBody @Valid SettlementGetByIdRequest request);

    /**
     * 结算单统计
     *
     * @param request 结算单统计数据结构 {@link SettlementCountRequest}
     * @return {@link SettlementCountResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/settlement/count")
    BaseResponse<SettlementCountResponse> count(@RequestBody SettlementCountRequest request);

    /**
     * 根据店铺ID统计结算单
     *
     * @param request 结算单统计数据结构 {@link SettlementTotalByStoreIdRequest}
     * @return {@link SettlementTotalResponse}
     */
    @PostMapping("/account/${application.account.version}/finance/record/settlement/count-by-store-id")
    BaseResponse<SettlementTotalResponse> countByStoreId(@RequestBody @Valid SettlementTotalByStoreIdRequest request);

    /**
     * 导出财务结算数据
     *
     * @param request
     * @return
     */
    @PostMapping("/account/${application.account.version}/finance/record/settlement/export")
    BaseResponse<SettlementToExcelResponse> getSettlementExportData(@RequestBody @Valid SettlementToExcelRequest request);

    @PostMapping("/account/${application.account.version}/finance/record/settlement/last")
    BaseResponse<SettlementLastResponse> getLastSettlementByStoreId(@RequestBody @Valid SettlementLastByStoreIdRequest request);


    /**
     * @Author yangzhen
     * @Description //分页查询 用于插入es
     * @Date 16:55 2020/12/11
     * @Param [request]
     * @return com.wanmi.sbc.common.base.BaseResponse<com.wanmi.sbc.account.api.response.finance.record.SettlementLastResponse>
     **/
    @PostMapping("/account/${application.account.version}/finance/record/settlement/init-es-page")
    BaseResponse<SettlementListResponse> initEsPage(@RequestBody @Valid SettlementPageRequest request);
}
