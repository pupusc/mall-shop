package com.wanmi.sbc.trade;

import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.account.bean.enums.PayWay;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.request.trade.TradeDefaultPayBatchRequest;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.request.GatewayConfigByGatewayRequest;
import com.wanmi.sbc.pay.api.request.GatewayOpenedByStoreIdRequest;
import com.wanmi.sbc.pay.api.request.OpenedChannelItemRequest;
import com.wanmi.sbc.pay.api.request.PayResultByOrdercodeRequest;
import com.wanmi.sbc.pay.api.response.PayGatewayConfigResponse;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.enums.TerminalType;
import com.wanmi.sbc.pay.bean.enums.TradeStatus;
import com.wanmi.sbc.pay.bean.vo.PayChannelItemVO;
import com.wanmi.sbc.pay.bean.vo.PayGatewayConfigVO;
import com.wanmi.sbc.trade.request.DefaultPayBatchRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * 支付
 * Created by sunkun on 2017/8/31.
 */
@Api(tags = "PayBaseController", description = "支付 API")
@RestController
@RequestMapping("/pay")
@Validated
public class PayBaseController {

    @Autowired
    private PayQueryProvider payQueryProvider;

    @Autowired
    private TradeProvider tradeProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private PayBaseAsycService payBaseAsycService;

    /**
     * 查询支付记录状态
     *
     * @param tid 订单id
     * @return
     */
    @ApiOperation(value = "查询支付记录状态")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "tid", value = "订单id", required = true)
    @RequestMapping(value = "/record/status/{tid}", method = RequestMethod.GET)
    public BaseResponse<TradeStatus> getRecordStatus(@PathVariable String tid) {
        return BaseResponse.success(payQueryProvider.getPayResponseByOrdercode(new PayResultByOrdercodeRequest(tid))
                .getContext().getTradeStatus());
    }


    /**
     * 查询在线支付是否开启
     *
     * @param type [PC,H5,APP]
     * @return
     */
    @ApiOperation(value = "查询在线支付是否开启", notes = "type: PC,H5,APP")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "type", value = "终端类型", required = true)
    @RequestMapping(value = "/gateway/isopen/{type}", method = RequestMethod.GET)
    public BaseResponse<Boolean> queryGatewayIsOpen(@PathVariable String type) {
        GatewayOpenedByStoreIdRequest request = new GatewayOpenedByStoreIdRequest();
        request.setStoreId(Constants.BOSS_DEFAULT_STORE_ID);
        List<PayGatewayConfigVO> payGatewayConfigList = payQueryProvider.listOpenedGatewayConfig(request).getContext()
                .getGatewayConfigVOList();
        List<PayChannelItemVO> itemList = new ArrayList<>();
        payGatewayConfigList.forEach(config -> {
            List<PayChannelItemVO> payChannelItemList = payQueryProvider.listOpenedChannelItemByGatewayName(new
                    OpenedChannelItemRequest(config.getPayGateway().getName(), TerminalType.valueOf(type))).getContext
                    ().getPayChannelItemVOList();
            if (CollectionUtils.isNotEmpty(payChannelItemList)) {
                itemList.addAll(payChannelItemList);
            }
        });
        return BaseResponse.success(!itemList.isEmpty());
    }

    /**
     * 查询银联企业支付配置
     *
     * @return
     */
    @ApiOperation(value = "查询银联企业支付配置")
    @RequestMapping("/queryUnionB2bConfig")
    public BaseResponse<PayGatewayConfigResponse> queryUnionB2bConfig() {
        PayGatewayConfigResponse payGatewayConfigResponse = payQueryProvider.getGatewayConfigByGateway(
                new GatewayConfigByGatewayRequest(PayGatewayEnum.UNIONB2B, Constants.BOSS_DEFAULT_STORE_ID)).getContext();
        return BaseResponse.success(payGatewayConfigResponse);
    }

    /**
     * 0元订单批量支付（支付网关默认为银联）
     *
     * @param request 请求参数
     * @return {@link BaseResponse}
     */
    @ApiOperation("0元订单批量支付（支付网关默认为银联）")
    @GlobalTransactional
    @RequestMapping("/default")
    public BaseResponse defaultPayBatch(@RequestBody @Valid DefaultPayBatchRequest request) {
        tradeProvider.defaultPayBatch(new TradeDefaultPayBatchRequest(request.getTradeIds(), PayWay.UNIONPAY));
        //异步推送订单到ERP系统

        payBaseAsycService.batchPushOrderToErp(request.getTradeIds());
        return BaseResponse.SUCCESSFUL();
    }

}
