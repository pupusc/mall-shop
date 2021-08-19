package com.wanmi.sbc.umeng;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailListByConditionRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import com.wanmi.sbc.message.api.provider.pushsendnode.PushSendNodeQueryProvider;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeListRequest;
import com.wanmi.sbc.setting.api.provider.umengpushconfig.UmengPushConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigByIdRequest;
import com.wanmi.sbc.setting.api.response.umengpushconfig.UmengPushConfigByIdResponse;
import com.wanmi.sbc.message.api.provider.pushcustomerenable.PushCustomerEnableProvider;
import com.wanmi.sbc.message.api.provider.pushcustomerenable.PushCustomerEnableQueryProvider;
import com.wanmi.sbc.message.api.provider.umengtoken.UmengTokenSaveProvider;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableByIdRequest;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableModifyRequest;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenAddRequest;
import com.wanmi.sbc.message.api.response.pushcustomerenable.PushCustomerEnableByIdResponse;
import com.wanmi.sbc.message.api.response.umengtoken.UmengTokenAddResponse;
import com.wanmi.sbc.message.bean.vo.PushCustomerEnableVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;


@Api(description = "友盟推送设置管理API", tags = "UmengConfigController")
@RestController
@RequestMapping(value = "/umengConfig")
public class UmengConfigController {

    @Autowired
    private UmengTokenSaveProvider umengTokenSaveProvider;

    @Autowired
    private PushCustomerEnableQueryProvider pushCustomerEnableQueryProvider;

    @Autowired
    private PushCustomerEnableProvider pushCustomerEnableProvider;

    @Autowired
    private UmengPushConfigQueryProvider umengPushConfigQueryProvider;

    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "新增友盟推送设备与会员关系")
    @PostMapping("/addToken")
    public BaseResponse<UmengTokenAddResponse> add(@RequestBody @Valid UmengTokenAddRequest addReq) {
        addReq.setCustomerId(commonUtil.getOperatorId());
        addReq.setBindingTime(LocalDateTime.now());
        return umengTokenSaveProvider.add(addReq);
    }

    @ApiOperation(value = "会员推送开关查询")
    @GetMapping("/getConfigDetail")
    public BaseResponse<PushCustomerEnableByIdResponse> getConfigDetail() {
        PushCustomerEnableByIdResponse response = pushCustomerEnableQueryProvider.getById(
                PushCustomerEnableByIdRequest.builder().customerId(commonUtil.getOperatorId()).build()).getContext();

        //非分销员不展示分销业务通知开关
        CustomerDetailListByConditionRequest request = CustomerDetailListByConditionRequest.builder()
                .customerId(commonUtil.getOperatorId()).build();
        List<CustomerDetailVO> detailResponseList = customerQueryProvider.listCustomerDetailByCondition(request)
                .getContext().getDetailResponseList();
        if(CollectionUtils.isNotEmpty(detailResponseList)){
            CustomerDetailVO customerDetailVO = detailResponseList.get(0);
            if(customerDetailVO.getIsDistributor() == DefaultFlag.NO){
                response.getPushCustomerEnableVO().setDistribution(null);
            }
        }
        return BaseResponse.success(response);
    }

    @ApiOperation(value = "会员推送开关保存")
    @PostMapping("/modifyConfig")
    public BaseResponse modifyConfig(@RequestBody @Valid PushCustomerEnableModifyRequest modifyRequest) {
        modifyRequest.setCustomerId(commonUtil.getOperatorId());
        pushCustomerEnableProvider.modify(modifyRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @ApiOperation(value = "友盟推送配置查询")
    @GetMapping("/getKey")
    public BaseResponse<UmengPushConfigByIdResponse> getUmengConfig() {
        UmengPushConfigByIdRequest idReq = new UmengPushConfigByIdRequest();
        idReq.setId(-1);
        return umengPushConfigQueryProvider.getById(idReq);
    }
}
