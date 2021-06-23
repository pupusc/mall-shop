package com.wanmi.sbc.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.provider.expresscompany.ExpressCompanyQueryProvider;
import com.wanmi.sbc.setting.api.provider.storeexpresscompanyrela.StoreExpressCompanyRelaQueryProvider;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaListRequest;
import com.wanmi.sbc.setting.api.response.expresscompany.ExpressCompanyListResponse;
import com.wanmi.sbc.setting.api.response.storeexpresscompanyrela.StoreExpressCompanyRelaListResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 店铺-物流公司Controller
 * Created by bail on 2017/12/14.
 */
@Api(tags = "StoreExpressCompanyBaseController", description = "店铺-物流公司 API")
@RestController
public class StoreExpressCompanyBaseController {


    @Autowired
    private ExpressCompanyQueryProvider expressCompanyQueryProvider;

    @Autowired
    private StoreExpressCompanyRelaQueryProvider storeExpressCompanyRelaQueryProvider;

    /**
     * 查询店铺正在使用的物流公司
     * @return
     */
    @ApiOperation(value = "查询店铺正在使用的物流公司")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "storeId", value = "店铺Id", required = true)
    @RequestMapping(value = "/store/expressCompany/{storeId}", method = RequestMethod.GET)
    public BaseResponse<List> findCheckedExpressCompanies(@PathVariable("storeId") Long storeId) {

        StoreExpressCompanyRelaListRequest queryRopRequest = new StoreExpressCompanyRelaListRequest();
        queryRopRequest.setStoreId(storeId);
        StoreExpressCompanyRelaListResponse response = storeExpressCompanyRelaQueryProvider.list(queryRopRequest).getContext();
        if (Objects.isNull(response)) {
            return BaseResponse.success(null);
        }
        return BaseResponse.success(response.getStoreExpressCompanyRelaVOList());
//        StoreQueryRopRequest queryRopRequest = new StoreQueryRopRequest();
//        queryRopRequest.setStoreId( storeId);
//
//        CompositeResponse<List> response = sdkClient.buildClientRequest().post( queryRopRequest,List.class, "storeExpCom.listChecked", "1.0.0");
//        return BaseResponse.success( response.getSuccessResponse());
    }

    /**
     * pc/h5修改退货物流公司为平台配置物流
     * @return
     */
    @ApiOperation(value = "pc/h5修改退货物流公司为平台配置物流")
    @GetMapping(value = "/boss/expressCompany")
    public BaseResponse<List> allExpressCompanyList() {
        ExpressCompanyListResponse response = expressCompanyQueryProvider.list().getContext();
        //如果是个空对象集合
        if (response.getExpressCompanyVOList().get(0).getExpressCompanyId() == null) {
            return BaseResponse.success(new ArrayList());
        }
        return BaseResponse.success(response.getExpressCompanyVOList());
//        CompositeResponse<List> response = sdkClient.buildClientRequest().post(List.class, "platformExpressCom.list", "1.0.0");
//        return BaseResponse.success( response.getSuccessResponse());
    }

}
