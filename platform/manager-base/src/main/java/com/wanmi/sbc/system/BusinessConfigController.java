package com.wanmi.sbc.system;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.provider.businessconfig.BusinessConfigQueryProvider;
import com.wanmi.sbc.setting.api.provider.businessconfig.BusinessConfigSaveProvider;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigAddRequest;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigAddResponse;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigRopResponse;
import com.wanmi.sbc.setting.api.response.businessconfig.BusinessConfigModifyResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "BusinessConfigController", description = "招商页设置Api")
@RestController
@RequestMapping("/business")
public class BusinessConfigController {

//    @Autowired
//    private SdkClient sdkClient;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private BusinessConfigQueryProvider businessConfigQueryProvider;

    @Autowired
    private BusinessConfigSaveProvider businessConfigSaveProvider;

    /**
     * 查询招商页设置
     * @return
     */
    @ApiOperation(value = "查询招商页设置")
    @RequestMapping(value = "/config", method = RequestMethod.GET)
    public BaseResponse<BusinessConfigRopResponse> findConfig(){
        return businessConfigQueryProvider.getInfo();
//        CompositeResponse<BusinessConfigRopResponse> response =  sdkClient.buildClientRequest()
//                .get(BusinessConfigRopResponse.class, "businessConfig.query", "1.0.0");
//        return BaseResponse.success( response.getSuccessResponse() );
    }


    /**
     * 保存招商页设置
     * @param addRequest
     * @return
     */
    @ApiOperation(value = "保存招商页设置")
    @RequestMapping(value = "/config", method = RequestMethod.POST)
    public BaseResponse<BusinessConfigRopResponse> saveBaseConfig(@RequestBody BusinessConfigAddRequest addRequest) {
//        CompositeResponse<BusinessConfigRopResponse> response;
//        response =sdkClient.buildClientRequest()
//                .post( saveRopRequest, BusinessConfigRopResponse.class, "businessConfig.save", "1.0.0");
//        if (!response.isSuccessful()) {
//            return BaseResponse.FAILED();
//        }
        BusinessConfigAddResponse response = businessConfigSaveProvider.add(addRequest).getContext();
        operateLogMQUtil.convertAndSend("设置", "保存招商页设置","保存招商页设置");
        return BaseResponse.success(KsBeanUtil.convert(response.getBusinessConfigVO(),BusinessConfigRopResponse.class));
    }


    /**
     * 修改基本设置
     * @param
     * @return
     */
    @ApiOperation(value = "修改基本设置")
    @RequestMapping(value = "/config", method = RequestMethod.PUT)
    public BaseResponse<BusinessConfigRopResponse> updateBaseConfig(@RequestBody BusinessConfigModifyRequest updateRopRequest){
        if(StringUtils.isEmpty(updateRopRequest.getBusinessConfigId())){
            throw new SbcRuntimeException("K-000009");
        }
        BusinessConfigModifyResponse response = businessConfigSaveProvider.modify(updateRopRequest).getContext();

//        CompositeResponse<BusinessConfigRopResponse> response =sdkClient.buildClientRequest()
//                .post( updateRopRequest, BusinessConfigRopResponse.class, "businessConfig.modify", "1.0.0");
//        if (!response.isSuccessful()) {
//            return BaseResponse.error( response.getErrorResponse().getSubErrors().get(0).getMessage());
//        }
        operateLogMQUtil.convertAndSend("设置", "编辑招商页设置","编辑招商页设置");
        return BaseResponse.success(KsBeanUtil.convert(response.getBusinessConfigVO(),BusinessConfigRopResponse.class));
    }
}
