package com.wanmi.sbc.job;

import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.order.api.provider.paycallbackresult.PayCallBackResultProvider;
import com.wanmi.sbc.order.api.provider.paycallbackresult.PayCallBackResultQueryProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeProvider;
import com.wanmi.sbc.order.api.provider.trade.TradeQueryProvider;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultModifyResultStatusRequest;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultPageRequest;
import com.wanmi.sbc.order.api.request.trade.TradeGetByIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradeListByParentIdRequest;
import com.wanmi.sbc.order.api.request.trade.TradePayOnlineCallBackRequest;
import com.wanmi.sbc.order.api.response.paycallbackresult.PayCallBackResultPageResponse;
import com.wanmi.sbc.order.api.response.trade.TradeGetByIdResponse;
import com.wanmi.sbc.order.api.response.trade.TradeListByParentIdResponse;
import com.wanmi.sbc.order.bean.enums.PayCallBackResultStatus;
import com.wanmi.sbc.order.bean.enums.PayCallBackType;
import com.wanmi.sbc.order.bean.vo.PayCallBackResultVO;
import com.wanmi.sbc.order.bean.vo.TradeVO;
import com.wanmi.sbc.pay.PayCallBackTaskService;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.provider.WxPayProvider;
import com.wanmi.sbc.pay.api.request.ChannelItemByIdRequest;
import com.wanmi.sbc.pay.api.request.TradeRecordByOrderCodeRequest;
import com.wanmi.sbc.pay.api.request.WxPayOrderDetailRequest;
import com.wanmi.sbc.pay.api.response.PayTradeRecordResponse;
import com.wanmi.sbc.pay.api.response.WxPayOrderDetailReponse;
import com.wanmi.sbc.pay.bean.enums.WxPayTradeType;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName PayCallBackJobHandle
 * @Description 支付回调出错补偿定时任务
 * @Author lvzhenwei
 * @Date 2020/7/6 11:21
 **/
@JobHandler(value="payCallBackJobHandler")
@Component
@Slf4j
public class PayCallBackJobHandle extends IJobHandler {

    private static final int ERROR_MAX_NUM = 5;

    private static final int PAGE_SIZE = 100;

    private static final String TODO = "0";

    private static final String HANDLING = "1";

    private static final String FAILED = "3";

    private static final String ORDER_CODE = "4";

    @Autowired
    private PayCallBackResultQueryProvider payCallBackResultQueryProvider;

    @Autowired
    private PayCallBackResultProvider payCallBackResultProvider;

    @Autowired
    private PayCallBackTaskService payCallBackTaskService;

    @Autowired
    private WxPayProvider wxPayProvider;

    @Autowired
    private TradeQueryProvider tradeQueryProvider;

    @Autowired
    private PayQueryProvider payQueryProvider;

    @Override
    public ReturnT<String> execute(String paramStr) throws Exception {
        PayCallBackResultPageRequest payCallBackResultPageRequest = new PayCallBackResultPageRequest();
        payCallBackResultPageRequest.setPageSize(PAGE_SIZE);
        if(StringUtils.isNotBlank(paramStr)){
            String[] paramStrArr = paramStr.split("&");
            String type = paramStrArr[0];
            if(type.equals(TODO)) {
                payCallBackResultPageRequest.setResultStatus(PayCallBackResultStatus.TODO);
            } else if(type.equals(HANDLING)){
                payCallBackResultPageRequest.setResultStatus(PayCallBackResultStatus.HANDLING);
                if(paramStrArr.length>1&&StringUtils.isNotBlank(paramStrArr[1])){
                    String endTime = paramStrArr[1];
                    payCallBackResultPageRequest.setCreateTimeEnd(LocalDateTime.parse(endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                } else {
                    payCallBackResultPageRequest.setCreateTimeEnd(LocalDateTime.now().minusMinutes(30));
                }
            } else if(type.equals(FAILED)){
                //查询处理失败的支付回调记录，并且失败的次数《=5次
                payCallBackResultPageRequest.setResultStatus(PayCallBackResultStatus.FAILED);
                payCallBackResultPageRequest.setErrorNum(ERROR_MAX_NUM);
            } else if(type.equals(ORDER_CODE)){
                if(paramStrArr.length>1&&StringUtils.isNotBlank(paramStrArr[1])){
                    String orderCodeStr = paramStrArr[1];
                    List<String> businessIds = new ArrayList<>();
                    Collections.addAll(businessIds, orderCodeStr.split(","));
                    payCallBackResultPageRequest.setBusinessIds(businessIds);
                }
            }
            payCallBackResultInfo(payCallBackResultPageRequest);
        } else {
            //查询处理失败的支付回调记录，并且失败的次数《=5次
            payCallBackResultPageRequest.setResultStatus(PayCallBackResultStatus.FAILED);
            payCallBackResultPageRequest.setErrorNum(ERROR_MAX_NUM);
            payCallBackResultInfo(payCallBackResultPageRequest);
            //查询未处理状态的数据
            payCallBackResultPageRequest.setResultStatus(PayCallBackResultStatus.TODO);
            payCallBackResultInfo(payCallBackResultPageRequest);
        }

        return SUCCESS;
    }

    /**
     * @Author lvzhenwei
     * @Description 获取对应需要处理的回调数据
     * @Date 11:46 2020/7/29
     * @Param [payCallBackResultPageRequest]
     * @return void
     **/
    public void payCallBackResultInfo(PayCallBackResultPageRequest payCallBackResultPageRequest){
        PayCallBackResultPageResponse payCallBackResultPageResponse = payCallBackResultQueryProvider.page(payCallBackResultPageRequest).getContext();
        //分页处理
        long totalPages = payCallBackResultPageResponse.getPayCallBackResultVOPage().getTotal();
        int size = payCallBackResultPageResponse.getPayCallBackResultVOPage().getSize();
        int pageNum = (int) Math.ceil((double )(totalPages/size));
        if(totalPages>1){
            payCallBackHandle(payCallBackResultPageResponse);
            for(int num=1;num<pageNum-1;num++){
                payCallBackResultPageRequest.setPageNum(num);
                payCallBackResultPageResponse = payCallBackResultQueryProvider.page(payCallBackResultPageRequest).getContext();
                payCallBackHandle(payCallBackResultPageResponse);
            }
        } else {
            payCallBackHandle(payCallBackResultPageResponse);
        }
    }

    @Transactional
    @GlobalTransactional
    public void payCallBackHandle(PayCallBackResultPageResponse payCallBackResultPageResponse){
        List<PayCallBackResultVO> payCallBackResultVOList = payCallBackResultPageResponse.getPayCallBackResultVOPage().getContent();
        if(CollectionUtils.isNotEmpty(payCallBackResultVOList)){
            payCallBackResultVOList.forEach(payCallBackResultVO -> {
                if(payCallBackResultVO.getResultStatus() != PayCallBackResultStatus.SUCCESS){
                    payCallBackResultProvider.modifyResultStatusByBusinessId(PayCallBackResultModifyResultStatusRequest.builder()
                            .businessId(payCallBackResultVO.getBusinessId())
                            .resultStatus(PayCallBackResultStatus.HANDLING)
                            .build());
                    try{
                        if(payCallBackResultVO.getPayType()== PayCallBackType.WECAHT){
                            //查询微信支付单报文，根据支付单支付的状态判断判断是否是已支付完成
                            WxPayOrderDetailReponse wxPayOrderDetailReponse = wxPayProvider.getWxPayOrderDetail(WxPayOrderDetailRequest.builder()
                                    .businessId(payCallBackResultVO.getBusinessId())
                                    .storeId(Constants.BOSS_DEFAULT_STORE_ID)
                                    .build()).getContext();
                            if("SUCCESS".equals(wxPayOrderDetailReponse.getReturn_code())&&"SUCCESS".equals(wxPayOrderDetailReponse.getResult_code())
                                    &&"SUCCESS".equals(wxPayOrderDetailReponse.getTrade_state())){
                                payCallBackTaskService.payCallBack(TradePayOnlineCallBackRequest.builder().payCallBackType(payCallBackResultVO.getPayType())
                                        .wxPayCallBackResultStr(payCallBackResultVO.getResultContext())
                                        .wxPayCallBackResultXmlStr(payCallBackResultVO.getResultXml())
                                        .build());
                            }
                        } else if(payCallBackResultVO.getPayType()== PayCallBackType.ALI) {
                            payCallBackTaskService.payCallBack(TradePayOnlineCallBackRequest.builder().payCallBackType(payCallBackResultVO.getPayType())
                                    .aliPayCallBackResultStr(payCallBackResultVO.getResultContext())
                                    .build());
                        }
                    } catch (Exception e) {
                        log.error("定时任务补偿回调时报，businessId="+payCallBackResultVO.getBusinessId(),e.getMessage());
                    }
                }
            });
        }
    }
}
