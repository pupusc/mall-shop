package com.wanmi.sbc.system;

import com.wanmi.sbc.setting.api.response.syssms.SmsSupplierRopResponse;
import com.wanmi.sbc.setting.api.provider.syssms.SysSmsQueryProvider;
import com.wanmi.sbc.setting.api.provider.syssms.SysSmsSaveProvider;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsByIdRequest;
import com.wanmi.sbc.setting.api.request.syssms.SmsSupplierSaveRopRequest;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsModifyRequest;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsAddRequest;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 短信接口服务
 * Created by CHENLI on 2017/5/16.
 */
@Api(tags = "SmsSupplierController", description = "短信接口服务 Api")
@RestController
public class SmsSupplierController {
    @Autowired
    private SysSmsQueryProvider sysSmsQueryProvider;

    @Autowired
    private SysSmsSaveProvider sysSmsSaveProvider;

    /**
     * 获取短信提供商
     * @return
     */
    @ApiOperation(value = "获取短信提供商")
    @RequestMapping(value = "/smsSuppliers", method = RequestMethod.GET)
    public BaseResponse<List> findEnableSmsSupplier(){
        return BaseResponse.success(sysSmsQueryProvider.list().getContext().getSmsSupplierRopResponses());
//        //不为空，转成前台需要的格式
//        if(!CollectionUtils.isEmpty(sysSmsVOS)){
//            return BaseResponse.success(sysSmsVOS.stream().map(sysSmsVO -> {
//                SmsSupplierRopResponse response = new SmsSupplierRopResponse()
//                        .builder()
//                        .id(sysSmsVO.getSmsId())
//                        .name(sysSmsVO.getSmsProvider())
//                        .account(sysSmsVO.getSmsName())
//                        .status(sysSmsVO.getIsOpen()==null?"0":sysSmsVO.getIsOpen().toString())
//                        .interfaceUrl(sysSmsVO.getSmsUrl())
//                        .siteAddress(sysSmsVO.getSmsAddress())
//                        .template(sysSmsVO.getSmsGateway())
//                        .password(sysSmsVO.getSmsPass())
//                        .smsContent(sysSmsVO.getSmsContent())
//                        .build();
//                return response;
//            }).collect(Collectors.toList()));
//        }else{
//            //为空，返回空数组
//            return BaseResponse.success(Collections.singletonList(new SmsSupplierRopResponse()));
//        }

        //return BaseResponse.success(List<SystemConfigRopResponse>.getContext(),List.class);
//        CompositeResponse<ArrayList<SmsSupplierRopResponse>> response
//                = sdkClient.buildClientRequest().get(ArrayList.class, "smsSupplier.list", "1.0.0");
//        return BaseResponse.success( response.getSuccessResponse());

    }

    /**
     * 通过ID查询短信提供商
     * @param supplierId
     * @return
     */
    @ApiOperation(value = "通过ID查询短信提供商")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "supplierId", value = "商家Id", required = true)
    @RequestMapping(value = "/smsSupplier/{supplierId}", method = RequestMethod.GET)
    public BaseResponse<SmsSupplierRopResponse> findBySupplierId(@PathVariable String supplierId){
        SysSmsByIdRequest request = new SysSmsByIdRequest();
        request.setSmsId(supplierId);
//        SmsSupplierQueryRopRequest ropRequest = new SmsSupplierQueryRopRequest();
//        ropRequest.setSupplierId( supplierId);
//        CompositeResponse<SmsSupplierRopResponse> response
//                = sdkClient.buildClientRequest().get( ropRequest, SmsSupplierRopResponse.class, "smsSupplier.detail", "1.0.0");
//        return BaseResponse.success(response.getSuccessResponse());
        return BaseResponse.success(sysSmsQueryProvider.getById(request).getContext());

    }

    /**
     * 保存短信服务商
     * @param saveRopRequest
     * @return
     */
    @ApiOperation(value = "保存短信服务商")
    @RequestMapping(value = "/smsSupplier", method = RequestMethod.POST)
    public BaseResponse<SmsSupplierRopResponse> saveSmsSupplier(@Valid @RequestBody SmsSupplierSaveRopRequest saveRopRequest){

        SysSmsAddRequest request = new SysSmsAddRequest();
        //组装参数
        request.builder()
                .smsName(saveRopRequest.getAccount())
                .smsProvider(saveRopRequest.getName())
                .smsUrl(saveRopRequest.getInterfaceUrl())
                .smsPass(saveRopRequest.getPassword())
                .smsAddress(saveRopRequest.getSiteAddress())
                .isOpen(Integer.parseInt(saveRopRequest.getStatus().toValue()))
                .build();
        return BaseResponse.success(sysSmsSaveProvider.add(request).getContext());

//        CompositeResponse<SmsSupplierRopResponse> response
//                = sdkClient.buildClientRequest().post( saveRopRequest, SmsSupplierRopResponse.class, "smsSupplier.save", "1.0.0");
//
//        operateLogMQUtil.convertAndSend("设置", "保存短信接口", "保存短信接口");
//        return BaseResponse.success( response.getSuccessResponse());

    }

    /**
     * 修改短信服务商
     * @param saveRopRequest
     * @return
     */
    @ApiOperation(value = "修改短信服务商")
    @RequestMapping(value = "/smsSupplier", method = RequestMethod.PUT)
    public BaseResponse<SmsSupplierRopResponse> updateSmsSupplier(@RequestBody SmsSupplierSaveRopRequest saveRopRequest){

        if(StringUtils.isEmpty(saveRopRequest.getId())){
            throw new SbcRuntimeException("K-000009");
        }

        SmsSupplierRopResponse old  = sysSmsQueryProvider.getById(new SysSmsByIdRequest(saveRopRequest.getId())).getContext();
        if(Objects.isNull(old)){
           return BaseResponse.FAILED();
        }

        //组装参数
        SysSmsModifyRequest request = new SysSmsModifyRequest().builder()
                .smsId(saveRopRequest.getId())
                .smsName(saveRopRequest.getAccount())
                .smsProvider(saveRopRequest.getName())
                .smsUrl(saveRopRequest.getInterfaceUrl())
                .smsPass(saveRopRequest.getPassword())
                .smsAddress(saveRopRequest.getSiteAddress())
                .isOpen(Integer.parseInt(saveRopRequest.getStatus().toValue()))
                .smsContent(saveRopRequest.getSmsContent())
                .smsGateway(saveRopRequest.getTemplate())
                .modifyTime(LocalDateTime.now())
                .createTime(old.getCreateTime())
                .build();
        return BaseResponse.success(sysSmsSaveProvider.modify(request).getContext());

//        CompositeResponse<SmsSupplierRopResponse> response
//                = sdkClient.buildClientRequest().post( saveRopRequest, SmsSupplierRopResponse.class, "smsSupplier.modify", "1.0.0");
//        operateLogMQUtil.convertAndSend("设置", "编辑短信接口", "编辑短信接口");
//        return BaseResponse.success( response.getSuccessResponse());

    }
}
