package com.wanmi.sbc.customer.api.provider.ares;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.ares.DispatcherFunctionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>会员模块数据统计接口</p>
 * Created by of628-wenzhi on 2018-09-20-下午3:55.
 */
@FeignClient(value = "${application.customer.name}", contextId = "CustomerAresProvider")
public interface CustomerAresProvider {

    /**
     * ares初始化会员数据
     *
     * @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/ares/init-customer-data")
    BaseResponse initCustomerData();

    /**
     * ares初始化会员级别数据
     *
     * @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/ares/init-customer-level-data")
    BaseResponse initCustomerLevelData();

    /**
     * ares初始化店铺会员(会员等级)关系数据
     *
     * @return @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/ares/init-store-customer-rela-data")
    BaseResponse initStoreCustomerRelaData();

    /**
     * ares初始化店铺业务员数据
     *
     * @return @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/ares/init-employee-data")
    BaseResponse initEmployeeData();

    /**
     * ares初始化店铺数据
     *
     * @return @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/ares/init-store-data")
    BaseResponse initStoreData();


    /**
     * ares埋点处理分发
     *
     * @param dispatcherFunctionRequest 包含方法类型与参数Bean {@link DispatcherFunctionRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/ares/dispatch-function")
    BaseResponse dispatchFunction(@RequestBody @Valid DispatcherFunctionRequest dispatcherFunctionRequest);
}
