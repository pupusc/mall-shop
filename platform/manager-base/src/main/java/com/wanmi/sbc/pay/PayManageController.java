package com.wanmi.sbc.pay;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.pay.api.provider.PayProvider;
import com.wanmi.sbc.pay.api.provider.PayQueryProvider;
import com.wanmi.sbc.pay.api.request.*;
import com.wanmi.sbc.pay.api.response.PayGatewayResponse;
import com.wanmi.sbc.pay.bean.enums.PayGatewayEnum;
import com.wanmi.sbc.pay.bean.vo.PayGatewayVO;
import com.wanmi.sbc.pay.reponse.PayGatewayReponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * boss交易管理模块
 * Created by sunkun on 2017/8/8.
 */
@Api(tags = "PayManageController", description = "boss交易管理模块 Api")
@RestController
@RequestMapping("/tradeManage")
public class PayManageController {

    @Autowired
    private PayQueryProvider payQueryProvider;

    @Autowired
    private PayProvider payProvider;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private CommonUtil commonUtil;
    /**
     * 获取网关列表
     *
     * @return
     */
    @ApiOperation(value = "获取网关列表")
    @RequestMapping(value = "/gateways", method = RequestMethod.GET)
    public BaseResponse<List<PayGatewayVO>> gateways() {
        Long storeIdWithDefault = commonUtil.getStoreIdWithDefault();
        GatewayByStoreIdRequest request = GatewayByStoreIdRequest.builder().storeId(storeIdWithDefault).build();
        List<PayGatewayVO> payGatewayList = payQueryProvider.listGatewayByStoreId(request).getContext().getPayGatewayVOList();
        // 如果没有网关列表 给店铺端按照boss开启的数据初始化数据
        if(CollectionUtils.isEmpty(payGatewayList)){
            GatewayInitByStoreIdRequest gatewayInitByStoreIdRequest = GatewayInitByStoreIdRequest.builder().storeId(commonUtil.getStoreIdWithDefault()).build();
            payGatewayList= payQueryProvider.initGatewayByStoreId(gatewayInitByStoreIdRequest).getContext().getPayGatewayVOList();

        }
        return BaseResponse.success(payGatewayList);
    }

    /**
     * 根据网关id查询支付渠道项
     *
     * @param id 网关id
     * @return
     */
    @ApiOperation(value = "根据网关id查询支付渠道项")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "id", value = "网关Id", required = true)
    @RequestMapping("/items/{id}")
    public BaseResponse<PayGatewayReponse> items(@PathVariable Long id) {
        PayGatewayResponse payGateway = payQueryProvider.getGatewayById(new GatewayByIdRequest(id, commonUtil.getStoreIdWithDefault())).getContext();
        return BaseResponse.success(PayGatewayReponse.builder().
                id(payGateway.getId()).
                name(payGateway.getName().toString()).
                isOpen(payGateway.getIsOpen()).
                type(payGateway.getType()).
                payGatewayConfig(payGateway.getConfig()).
                channelItemList(payGateway.getPayChannelItemList()).build());
    }

    /**
     * 保存
     *
     * @param payGatewayRequest
     * @return
     */
    @ApiOperation(value = "保存")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public BaseResponse save(@RequestBody @Valid PayGatewaySaveRequest payGatewayRequest) {
        payGatewayRequest.setStoreId(commonUtil.getStoreIdWithDefault());
        if (PayGatewayEnum.WECHAT.toValue().equals(payGatewayRequest.getName())) {
            payGatewayRequest.getPayGatewayConfig().setAppId(null);
            payGatewayRequest.getPayGatewayConfig().setSecret(null);
            payGatewayRequest.getPayGatewayConfig().setOpenPlatformAppId(null);
            payGatewayRequest.getPayGatewayConfig().setOpenPlatformSecret(null);
            payGatewayRequest.getPayGatewayConfig().setOpenPlatformAccount(payGatewayRequest.getPayGatewayConfig().getAccount());
            payGatewayRequest.getPayGatewayConfig().setOpenPlatformApiKey(payGatewayRequest.getPayGatewayConfig().getApiKey());
        }
        payProvider.savePayGateway(payGatewayRequest);
        //操作日志记录
        String op = "";
        if (PayGatewayEnum.PING.toValue().equals(payGatewayRequest.getName())) {
            op = "编辑聚合支付";
        }
        if (PayGatewayEnum.UNIONB2B.toValue().equals(payGatewayRequest.getName())) {
            op = "编辑企业支付";
        }
        if (PayGatewayEnum.ALIPAY.toValue().equals(payGatewayRequest.getName())) {
            op = "编辑支付宝支付";
        }
        if (PayGatewayEnum.WECHAT.toValue().equals(payGatewayRequest.getName())) {
            op = "编辑微信支付";
        }
        if (PayGatewayEnum.UNIONPAY.toValue().equals(payGatewayRequest.getName())) {
            op = "编辑银联支付";
        }
        if (PayGatewayEnum.BALANCE.toValue().equals(payGatewayRequest.getName())) {
            op = "余额支付";
        }
        operateLogMQUtil.convertAndSend("财务", op, op);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 上传微信支付证书
     *
     * @param multipartFiles
     * @param gatewayConfigId
     * @param type
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "上传微信支付证书")
    @RequestMapping(value = "/uploadPayCertificate", method = RequestMethod.POST)
    public BaseResponse uploadPayCertificate(@RequestParam("uploadFile") List<MultipartFile> multipartFiles, Long
            gatewayConfigId, Integer type) throws IOException {

        //验证上传参数
        if (CollectionUtils.isEmpty(multipartFiles)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        for (MultipartFile file : multipartFiles) {
            if (file == null || file.getSize() == 0) {
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
        }
        PayGatewayUploadPayCertificateRequest payGatewayUploadPayCertificateRequest = new PayGatewayUploadPayCertificateRequest();
        payGatewayUploadPayCertificateRequest.setId(gatewayConfigId);
        payGatewayUploadPayCertificateRequest.setPayCertificateType(type);
        payGatewayUploadPayCertificateRequest.setPayCertificate(multipartFiles.get(0).getBytes());
        return payProvider.uploadPayCertificate(payGatewayUploadPayCertificateRequest);
    }
}
