package com.wanmi.sbc.customer.provider.impl.ares;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.ares.CustomerAresProvider;
import com.wanmi.sbc.customer.api.request.ares.DispatcherFunctionRequest;
import com.wanmi.sbc.customer.ares.CustomerAresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>会员服务与数据统计系统交互接口</p>
 * Created by of628-wenzhi on 2018-09-25-下午4:40.
 */
@RestController
@Validated
public class CustomerAresController implements CustomerAresProvider {

    @Autowired
    private CustomerAresService customerAresService;

    @Override
    public BaseResponse initCustomerData() {
        customerAresService.initCustomerES();
        return BaseResponse.SUCCESSFUL();
    }

    @Override

    public BaseResponse initCustomerLevelData() {
        customerAresService.initCustomerLevelES();
        return BaseResponse.SUCCESSFUL();
    }

    @Override

    public BaseResponse initStoreCustomerRelaData() {
        customerAresService.initStoreCustomerRelaES();
        return BaseResponse.SUCCESSFUL();
    }

    @Override

    public BaseResponse initEmployeeData() {
        customerAresService.initEmployeeES();
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse initStoreData() {
        customerAresService.initStoreES();
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse dispatchFunction(@RequestBody @Valid DispatcherFunctionRequest dispatcherFunctionRequest) {
        customerAresService.dispatchFunction(dispatcherFunctionRequest.getFuncType(), dispatcherFunctionRequest
                .getObjs());
        return BaseResponse.SUCCESSFUL();
    }
}
