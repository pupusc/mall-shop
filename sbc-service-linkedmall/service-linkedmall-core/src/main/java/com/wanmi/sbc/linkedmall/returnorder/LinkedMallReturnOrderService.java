package com.wanmi.sbc.linkedmall.returnorder;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.linkedmall.model.v20180116.*;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.linkedmall.api.request.returnorder.*;
import com.wanmi.sbc.linkedmall.util.LinkedMallUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.awt.image.ImageWatched;

@Service
@Slf4j
public class LinkedMallReturnOrderService {


    @Autowired
    private IAcsClient iAcsClient;

    @Autowired
    private LinkedMallUtil linkedMallUtil;
    public InitApplyRefundResponse.InitApplyRefundData initApplyRefund(SbcInitApplyRefundRequest sbcInitApplyRefundRequest) {
        InitApplyRefundRequest request = new InitApplyRefundRequest();
        request.setBizId(linkedMallUtil.getLinkedMallBizId());
        request.setSubLmOrderId(sbcInitApplyRefundRequest.getSubLmOrderId());
        request.setBizClaimType(sbcInitApplyRefundRequest.getBizClaimType());
        request.setGoodsStatus(sbcInitApplyRefundRequest.getGoodsStatus());
        request.setBizUid(sbcInitApplyRefundRequest.getBizUid());
        request.setAccountType(LinkedMallUtil.ACCOUNT_TYPE);
        request.setUseAnonymousTbAccount(true);
        request.setThirdPartyUserId(sbcInitApplyRefundRequest.getBizUid());
        InitApplyRefundResponse response;
        try {
            response = iAcsClient.getAcsResponse(request);
        } catch (ClientException e) {
            throw new SbcRuntimeException(CommonErrorCode.ALIYUN_CONNECT_ERROR);
        }
        log.info("initApplyRefund订单逆向渲染:\r\n请求参数：{}，\r\n响应结果：{}", JSON.toJSONString(request),JSON.toJSONString(response));
        if (!StringUtils.equals(response.getCode(),LinkedMallUtil.SUCCESS_CODE)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{response.getMessage()});
        }
        return response.getInitApplyRefundData();
    }

    public ApplyRefundResponse.RefundApplicationData applyRefund(SbcApplyRefundRequest sbcApplyRefundRequest) {
        ApplyRefundRequest request = new ApplyRefundRequest();
        request.setBizId(linkedMallUtil.getLinkedMallBizId());
        request.setSubLmOrderId(sbcApplyRefundRequest.getSubLmOrderId());
        request.setBizUid(sbcApplyRefundRequest.getBizUid());
        request.setBizClaimType(sbcApplyRefundRequest.getBizClaimType());
        request.setApplyRefundFee(sbcApplyRefundRequest.getApplyRefundFee());
        request.setApplyRefundFee(sbcApplyRefundRequest.getApplyRefundFee());
        request.setApplyReasonTextId(sbcApplyRefundRequest.getApplyReasonTextId());
        request.setLeaveMessage(sbcApplyRefundRequest.getLeaveMessage());
        request.setLeavePictureLists(sbcApplyRefundRequest.getLeavePictureList());
        request.setGoodsStatus(sbcApplyRefundRequest.getGoodsStatus());
        request.setUseAnonymousTbAccount(true);
        request.setAccountType(LinkedMallUtil.ACCOUNT_TYPE);
        request.setThirdPartyUserId(sbcApplyRefundRequest.getBizUid());
        ApplyRefundResponse response;
        try {
            response = iAcsClient.getAcsResponse(request);
        } catch (ClientException e) {
            throw new SbcRuntimeException(CommonErrorCode.ALIYUN_CONNECT_ERROR);
        }
        log.info("applyRefund订单逆向申请：\r\n请求参数：{}，\r\n响应结果：{}", JSON.toJSONString(request),JSON.toJSONString(response));
        if (!StringUtils.equals(response.getCode(),LinkedMallUtil.SUCCESS_CODE)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{response.getMessage()});
        }
        return response.getRefundApplicationData();
    }


    public CancelRefundResponse.RefundApplicationData cancelRefund(SbcCancelRefundRequest sbcCancelRefundRequest){
        CancelRefundRequest request = new CancelRefundRequest();
        request.setBizId(linkedMallUtil.getLinkedMallBizId());
        request.setBizUid(sbcCancelRefundRequest.getBizUid());
        request.setSubLmOrderId(sbcCancelRefundRequest.getSubLmOrderId());
        request.setAccountType(LinkedMallUtil.ACCOUNT_TYPE);
        request.setUseAnonymousTbAccount(true);
        request.setThirdPartyUserId(sbcCancelRefundRequest.getBizUid());
        request.setDisputeId(sbcCancelRefundRequest.getDisputeId());
        CancelRefundResponse response;
        try {
            response = iAcsClient.getAcsResponse(request);
        } catch (ClientException e) {
            throw new SbcRuntimeException(CommonErrorCode.ALIYUN_CONNECT_ERROR);
        }
        log.info("cancelRefund取消退款申请:\r\n请求参数：{}，\r\n响应结果：{}", JSON.toJSONString(request),JSON.toJSONString(response));
        if (!StringUtils.equals(response.getCode(),LinkedMallUtil.SUCCESS_CODE)) {
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{response.getMessage()});
        }
        return response.getRefundApplicationData();
    }


    public QueryRefundApplicationDetailResponse.RefundApplicationDetail queryRefundApplicationDetail(SbcQueryRefundApplicationDetailRequest sbcQueryRefundApplicationDetailRequest) {
        QueryRefundApplicationDetailRequest request = new QueryRefundApplicationDetailRequest();
        request.setBizId(linkedMallUtil.getLinkedMallBizId());
        request.setBizUid(sbcQueryRefundApplicationDetailRequest.getBizUid());
        request.setUseAnonymousTbAccount(true);
        request.setAccountType(LinkedMallUtil.ACCOUNT_TYPE);
        request.setSubLmOrderId(sbcQueryRefundApplicationDetailRequest.getSubLmOrderId());
        request.setThirdPartyUserId(sbcQueryRefundApplicationDetailRequest.getBizUid());
        QueryRefundApplicationDetailResponse response;
        try {
            response = iAcsClient.getAcsResponse(request);
        } catch (ClientException e) {
            throw new SbcRuntimeException(CommonErrorCode.ALIYUN_CONNECT_ERROR);
        }
        log.info("queryRefundApplicationDetail 查询订单逆向申请详情: \r\n请求参数：{}， \r\n响应结果：{}", JSON.toJSONString(request),JSON.toJSONString(response));
        if (!StringUtils.equals(response.getCode(),LinkedMallUtil.SUCCESS_CODE)) {
            if("1004".equalsIgnoreCase(response.getCode())){
                throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{"该订单没有申请退款"});
            }
            throw new SbcRuntimeException(CommonErrorCode.SPECIFIED, new Object[]{response.getMessage()});
        }
        return response.getRefundApplicationDetail();
    }

    public BaseResponse submitReturnGoodLogistics(SbcSubmitReturnGoodLogisticsRequest sbcSubmitReturnGoodLogisticsRequest) {
        SubmitReturnGoodLogisticsRequest request = new SubmitReturnGoodLogisticsRequest();
        request.setBizId(linkedMallUtil.getLinkedMallBizId());
        request.setBizUid(sbcSubmitReturnGoodLogisticsRequest.getBizUid());
        request.setUseAnonymousTbAccount(true);
        request.setAccountType(LinkedMallUtil.ACCOUNT_TYPE);
        request.setThirdPartyUserId(sbcSubmitReturnGoodLogisticsRequest.getBizUid());
        request.setDisputeId(sbcSubmitReturnGoodLogisticsRequest.getDisputeId());
        request.setSubLmOrderId(sbcSubmitReturnGoodLogisticsRequest.getSubLmOrderId());
        request.setCpCode(sbcSubmitReturnGoodLogisticsRequest.getCpCode());
        request.setLogisticsNo(sbcSubmitReturnGoodLogisticsRequest.getLogisticsNo());
        SubmitReturnGoodLogisticsResponse response;
        try {
            response = iAcsClient.getAcsResponse(request);
        } catch (ClientException e) {
            throw new SbcRuntimeException(CommonErrorCode.ALIYUN_CONNECT_ERROR );
        }
        log.info("submitReturnGoodLogistics提交退货物流信息:\r\n请求参数：{}，\r\n响应结果：{}", JSON.toJSONString(request),JSON.toJSONString(response));
        if (StringUtils.equals(LinkedMallUtil.SUCCESS_CODE,response.getCode())) {
            return BaseResponse.SUCCESSFUL();
        } else {
            return BaseResponse.error(response.getMessage());
        }
    }
}
