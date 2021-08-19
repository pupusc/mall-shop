package com.wanmi.sbc.trade;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.aop.EmployeeCheck;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.trade.TradePageCriteriaRequest;
import com.wanmi.sbc.order.bean.dto.TradeQueryDTO;
import com.wanmi.sbc.order.bean.enums.DeliverStatus;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.TradeConfigGetByTypeRequest;
import com.wanmi.sbc.setting.api.response.TradeConfigGetByTypeResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by sunkun on 2017/11/23.
 */
@Api(tags = "StoreTradeController", description = "店铺订单服务API")
@RestController
@RequestMapping("/trade")
public class StoreTradeController {

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Resource
    private CommonUtil commonUtil;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    /**
     * 分页查询可退订单 from ES
     *
     * @param returnQueryRequest
     * @return
     */
    @ApiOperation(value = "分页查询可退订单 from ES")
    @EmployeeCheck
    @RequestMapping(value = "/list/return", method = RequestMethod.POST)
    public ResponseEntity<Page<TradeVO>> pageCanReturn(@RequestBody TradeQueryDTO returnQueryRequest) {
        returnQueryRequest.setSortType(FieldType.Date.toString());
        returnQueryRequest.setSupplierId(commonUtil.getCompanyInfoId());
        TradeConfigGetByTypeRequest request = new TradeConfigGetByTypeRequest();
        request.setConfigType(ConfigType.ORDER_SETTING_APPLY_REFUND);
        TradeConfigGetByTypeResponse config = auditQueryProvider.getTradeConfigByType(request).getContext();
        JSONObject content = JSON.parseObject(config.getContext());
        Integer day = content.getObject("day", Integer.class);
        returnQueryRequest.setStatus(config.getStatus());
        if (Objects.nonNull(config.getStatus()) && config.getStatus() == 1 && Objects.nonNull(day) && day > 0) {
            returnQueryRequest.setDay(day);
        }

        //周期购订单查询部分发货的订单
        returnQueryRequest.setPeriodicPurchaseRefund(Boolean.TRUE);

        MicroServicePage<TradeVO> tradePage = tradeQueryProvider.pageCriteria(TradePageCriteriaRequest.builder()
                .isReturn(true).tradePageDTO(returnQueryRequest).build()).getContext()
                .getTradePage();
        return ResponseEntity.ok(new PageImpl<>(tradePage.getContent(),
                 PageRequest.of(returnQueryRequest.getPageNum(), returnQueryRequest.getPageSize()), tradePage.getTotalElements()));
    }
}
