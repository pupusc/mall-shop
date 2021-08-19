package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.provider.expresscompany.ExpressCompanyQueryProvider;
import com.wanmi.sbc.setting.api.provider.storeexpresscompanyrela.StoreExpressCompanyRelaQueryProvider;
import com.wanmi.sbc.setting.api.provider.storeexpresscompanyrela.StoreExpressCompanyRelaSaveProvider;
import com.wanmi.sbc.setting.api.request.StoreExpressRequest;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyByIdRequest;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaAddRequest;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaByIdRequest;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaDelByIdRequest;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaListRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyByIdResponse;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyListResponse;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaListResponse;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressRelaRopResponse;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * 店铺-物流公司配置Controller
 * Created by bail on 2017/11/20.
 */
@Api(tags = "StoreExpressCompanyController", description = "店铺-物流公司配置 API")
@RestController
@RequestMapping("/store/expressCompany")
public class StoreExpressCompanyController {

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private ExpressCompanyQueryProvider expressCompanyQueryProvider;

    @Autowired
    private StoreExpressCompanyRelaQueryProvider storeExpressCompanyRelaQueryProvider;

    @Autowired
    private StoreExpressCompanyRelaSaveProvider storeExpressCompanyRelaSaveProvider;


    /**
     * 查询所有有效的物流公司列表
     * @author bail
     */
    @ApiOperation(value = "查询所有有效的物流公司列表")
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public BaseResponse<List> allExpressCompanyList() {
        ExpressCompanyListResponse response = expressCompanyQueryProvider.list().getContext();
        //如果是个空对象集合
        if (response.getExpressCompanyVOList().get(0).getExpressCompanyId() == null) {
            return BaseResponse.success(new ArrayList());
        }
        return BaseResponse.success(response.getExpressCompanyVOList());
//        CompositeResponse<List> response = sdkClient.buildClientRequest().post(List.class, "platformExpressCom.list", "1.0.0");
//        return BaseResponse.success(response.getSuccessResponse());
    }

    /**
     * 查询店铺正在使用的物流公司
     * @return
     */
    @ApiOperation(value = "查询店铺正在使用的物流公司")
    @RequestMapping(method = RequestMethod.GET)
    public BaseResponse<List> findCheckedExpressCompanies() {
        StoreExpressCompanyRelaListRequest queryRopRequest = new StoreExpressCompanyRelaListRequest();
        queryRopRequest.setStoreId(commonUtil.getStoreId());
        StoreExpressCompanyRelaListResponse response = storeExpressCompanyRelaQueryProvider.list(queryRopRequest).getContext();
        if (Objects.isNull(response)) {
            return BaseResponse.success(null);
        }
        // CompositeResponse<List> response = sdkClient.buildClientRequest().post(queryRopRequest, List.class, "storeExpCom.listChecked", "1.0.0");
        return BaseResponse.success(response.getStoreExpressCompanyRelaVOList());
    }

    /**
     * 为店铺设置需要使用的物流公司们
     * @param queryRequest
     */
    @ApiOperation(value = "为店铺设置需要使用的物流公司")
    @RequestMapping(method = RequestMethod.POST)
    public BaseResponse<StoreExpressRelaRopResponse> saveStoreExpressCompany(@RequestBody StoreExpressRequest queryRequest) {
        StoreExpressCompanyRelaAddRequest request = new StoreExpressCompanyRelaAddRequest();
        if (StringUtils.isBlank(queryRequest.getExpressCompanyId().toString())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        // StoreExpComSaveRopRequest expComSaveRopRequest = new StoreExpComSaveRopRequest();
        request.setStoreId(commonUtil.getStoreId());
        request.setCompanyInfoId(Integer.parseInt(commonUtil.getCompanyInfoId().toString()));
        request.setExpressCompanyId(queryRequest.getExpressCompanyId());
        return BaseResponse.success(storeExpressCompanyRelaSaveProvider.add(request).getContext());
//     CompositeResponse<StoreExpressRelaRopResponse> response = sdkClient.buildClientRequest().post(expComSaveRopRequest, StoreExpressRelaRopResponse.class, "storeExpCom.save", "1.0.0");
//        if (!response.isSuccessful()) {
//            return BaseResponse.error(response.getErrorResponse().getSubErrors().get(0).getMessage());
//        }
//        CompositeResponse<ExpressCompany> company =
//                sdkClient.buildClientRequest().post(expComSaveRopRequest, ExpressCompany.class, "expressCompany.detail",
//                        "1.0.0");
//        if (company.isSuccessful()) {
//            operateLogMQUtil.convertAndSend("设置", "开启物流公司", "开启物流公司：" + company.getSuccessResponse().getExpressName());
//        }
//        return BaseResponse.success(response.getSuccessResponse());
    }

    /**
     * 删除正在使用的某个物流公司
     * @param id
     */
    @ApiOperation(value = "删除正在使用的某个物流公司")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long",
                    name = "id", value = "店铺物流Id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "Long",
                    name = "expressCompanyId", value = "物流公司Id", required = true)
    })
    @RequestMapping(value = "/{id}/{expressCompanyId}", method = RequestMethod.DELETE)
    public BaseResponse deleteStoreExpressCompany(@PathVariable("id") Long id,
                                                  @PathVariable("expressCompanyId") Long expressCompanyId) {

        if (StringUtils.isBlank(id.toString()) || StringUtils.isBlank(expressCompanyId.toString())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        StoreExpressCompanyRelaByIdRequest request = new StoreExpressCompanyRelaByIdRequest();
        request.setId(id);
        //如果查询不到，抛出异常
        if (Objects.isNull(storeExpressCompanyRelaQueryProvider.getById(request).getContext())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        //做删除
        StoreExpressCompanyRelaDelByIdRequest deleteRopRequest = new StoreExpressCompanyRelaDelByIdRequest();
        deleteRopRequest.setId(id);
        storeExpressCompanyRelaSaveProvider.deleteById(deleteRopRequest);

        //输出操作日志
        ExpressCompanyByIdRequest expressCompanyByIdRequest = new ExpressCompanyByIdRequest();
        expressCompanyByIdRequest.setExpressCompanyId(expressCompanyId);
        ExpressCompanyByIdResponse response = expressCompanyQueryProvider.getById(expressCompanyByIdRequest).getContext();

//        StoreExpComSaveRopRequest expComSaveRopRequest = new StoreExpComSaveRopRequest();
//        expComSaveRopRequest.setExpressCompanyId(expressCompanyId);
//
//        CompositeResponse<ExpressCompany> company =
//                sdkClient.buildClientRequest().post(expComSaveRopRequest, ExpressCompany.class, "expressCompany.detail",
//                        "1.0.0");

        operateLogMQUtil.convertAndSend("设置", "取消物流公司", "取消物流公司：" + response.getExpressCompanyVO().getExpressName());
        return BaseResponse.SUCCESSFUL();
    }
}
