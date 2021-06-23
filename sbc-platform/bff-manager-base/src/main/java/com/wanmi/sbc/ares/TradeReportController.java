package com.wanmi.sbc.ares;

import com.wanmi.ares.provider.TradeServiceProvider;
import com.wanmi.ares.request.flow.FlowRequest;
import com.wanmi.ares.view.trade.TradePageView;
import com.wanmi.ares.view.trade.TradeView;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Objects.nonNull;

/**
 * Created by sunkun on 2017/10/16.
 */
@Api(tags = "TradeReportController", description = "订单报表")
@RestController
@Slf4j
@RequestMapping("/tradeReport")
public class TradeReportController {

    @Autowired
    private TradeServiceProvider tradeServiceProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "订单列表")
    @RequestMapping("/list")
    public BaseResponse<List<TradeView>> list(@RequestBody FlowRequest request) {
        try {
            Long companyInfoId = commonUtil.getCompanyInfoId();
            if (nonNull(companyInfoId)) {
                request.setCompanyId(companyInfoId.toString());
            }
            List<TradeView> list = tradeServiceProvider.getTradeList(request);
            return BaseResponse.success(list);
        } catch (Exception ex) {
            log.error("查询订单列表异常,", ex);
            throw new SbcRuntimeException("查询订单列表异常");
        }
    }

    @ApiOperation(value = "订单列表分页")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<TradePageView> page(@RequestBody FlowRequest request) {
        try {
            Long companyInfoId = commonUtil.getCompanyInfoId();
            if (nonNull(companyInfoId)) {
                request.setCompanyId(companyInfoId.toString());
            }
            TradePageView tradePageView = tradeServiceProvider.getTradePage(request);
            return BaseResponse.success(tradePageView);
        } catch (Exception ex) {
            log.error("查询订单列表分页异常,", ex);
            throw new SbcRuntimeException("查询订单列表分页异常");
        }
    }

    @ApiOperation(value = "店铺订单列表分类")
    @RequestMapping(value = "/store", method = RequestMethod.POST)
    public BaseResponse<TradePageView> storePage(@RequestBody FlowRequest request) {
        try {
            TradePageView tradePageView = tradeServiceProvider.getStoreTradePage(request);
            return BaseResponse.success(tradePageView);
        } catch (Exception ex) {
            log.error("店铺订单列表分类,", ex);
            throw new SbcRuntimeException("店铺订单列表分类");
        }
    }

    @ApiOperation("订单列表概览")
    @RequestMapping(value = "overview", method = RequestMethod.POST)
    public BaseResponse<TradeView> getOverview(@RequestBody FlowRequest request) {
        try {
            Long companyInfoId = commonUtil.getCompanyInfoId();
            if (nonNull(companyInfoId)) {
                request.setCompanyId(companyInfoId.toString());
            }
            TradeView tradeView = tradeServiceProvider.getOverview(request);
            return BaseResponse.success(tradeView);
        } catch (Exception ex) {
            log.error("订单列表概览,", ex);
            throw new SbcRuntimeException("订单列表概览");
        }
    }
}
