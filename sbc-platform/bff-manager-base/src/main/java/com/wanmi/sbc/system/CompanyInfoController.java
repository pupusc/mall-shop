package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.setting.api.provider.companyinfo.CompanyInfoQueryProvider;
import com.wanmi.sbc.setting.api.provider.companyinfo.CompanyInfoSaveProvider;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoRopResponse;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoModifyRequest;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 公司信息服务
 * Created by CHENLI on 2017/5/12.
 */
@RestController
@Api(tags = "CompanyInfoController", description = "S2B 管理端公用-公司信息管理API")
public class CompanyInfoController {

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private CompanyInfoSaveProvider companyInfoSaveProvider;

    @Autowired
    private CommonUtil commonUtil;

    /**
     * 查询公司信息
     * @return
     */
    @ApiOperation(value = "查询公司信息")
    @RequestMapping(value = "/companyInfo", method = RequestMethod.GET)
    public BaseResponse<CompanyInfoRopResponse> findCompanyInfo(){
        return companyInfoQueryProvider.findCompanyInfos();
//        CompositeResponse<CompanyInfoRopResponse> ropResponse = sdkClient.buildClientRequest()
//                .get(CompanyInfoRopResponse.class, "companyInfo.find", "1.0.0");
//        return BaseResponse.success(ropResponse.getSuccessResponse());
    }

    /**
     * 查询公司信息，此接口与上面接口的区别在于，不需要鉴权
     * @return
     */
    @ApiOperation(value = "查询公司信息，此接口与上面接口的区别在于，不需要鉴权")
    @RequestMapping(value = "/getCompanyInfo", method = RequestMethod.GET)
    public BaseResponse<CompanyInfoRopResponse> getCompanyInfo(){
        return companyInfoQueryProvider.findCompanyInfos();
//        CompositeResponse<CompanyInfoRopResponse> ropResponse = sdkClient.buildClientRequest()
//                .get(CompanyInfoRopResponse.class, "companyInfo.find", "1.0.0");
//        return BaseResponse.success(ropResponse.getSuccessResponse());
    }

    /**
     * 保存公司信息
     * @param saveRopRequest
     * @return
     */
//    @ApiOperation(value = "保存公司信息")
//    @RequestMapping(value = "/companyInfo", method = RequestMethod.POST)
//    public BaseResponse<CompanyInfoRopResponse> saveCompanyInfo(@RequestBody CompanyInfoSaveRopRequest saveRopRequest){
//        if(StringUtils.isEmpty(saveRopRequest.getCompanyName())){
//            throw new SbcRuntimeException("K-000009");
//        }
//        CompositeResponse<CompanyInfoRopResponse> ropResponse = sdkClient.buildClientRequest()
//                .post(saveRopRequest, CompanyInfoRopResponse.class, "companyInfo.save", "1.0.0");
//        return BaseResponse.success(ropResponse.getSuccessResponse());
//
//    }

    /**
     * 修改公司信息
     * @param companyInfoModifyRequest
     * @return
     */
    @ApiOperation(value = "修改公司信息")
    @RequestMapping(value = "/companyInfo", method = RequestMethod.PUT)
    public BaseResponse updateCompanyInfo(@RequestBody CompanyInfoModifyRequest companyInfoModifyRequest){

        if(Objects.isNull(companyInfoModifyRequest.getCompanyInfoId())){
            throw new SbcRuntimeException("K-000009");
        }
        companyInfoSaveProvider.modify(companyInfoModifyRequest);
//        sdkClient.buildClientRequest()
//                .post(saveRopRequest, CompanyInfoRopResponse.class, "companyInfo.modify", "1.0.0");
//
//        operateLogMQUtil.convertAndSend("设置", "编辑公司信息", "编辑公司信息");
        return BaseResponse.SUCCESSFUL();

    }
}
