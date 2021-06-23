package com.wanmi.sbc.customer.api.provider.storelevel;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.storelevel.*;
import com.wanmi.sbc.customer.api.response.storelevel.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>商户客户等级表查询服务Provider</p>
 *
 * @author yang
 * @date 2019-02-27 19:51:30
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreLevelQueryProvider")
public interface StoreLevelQueryProvider {

    /**
     * 分页查询商户客户等级表API
     *
     * @param storeLevelPageReq 分页请求参数和筛选对象 {@link StoreLevelPageRequest}
     * @return 商户客户等级表分页列表信息 {@link StoreLevelPageResponse}
     * @author yang
     */
    @PostMapping("/customer/${application.customer.version}/storelevel/page")
    BaseResponse<StoreLevelPageResponse> page(@RequestBody @Valid StoreLevelPageRequest storeLevelPageReq);

    /**
     * 列表查询商户客户等级表API
     *
     * @param storeLevelListReq 列表请求参数和筛选对象 {@link StoreLevelListRequest}
     * @return 商户客户等级表的列表信息 {@link StoreLevelListResponse}
     * @author yang
     */
    @PostMapping("/customer/${application.customer.version}/storelevel/list")
    BaseResponse<StoreLevelListResponse> list(@RequestBody @Valid StoreLevelListRequest storeLevelListReq);

    /**
     * 根据等级名称查询API
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/storeleve/get-by-level-name-and-store-id")
    BaseResponse<StoreLevelListResponse> getByStoreIdAndLevelName(@RequestBody @Valid StoreLevelByStoreIdAndLevelNameRequest request);

    /**
     * 根据商铺id查询商户客户等级
     *
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/level/list-all-store-level")
    BaseResponse<StoreLevelListResponse> listAllStoreLevelByStoreId(@RequestBody @Valid StoreLevelListRequest storeLevelListReq);

    /**
     * 单个查询商户客户等级表API
     *
     * @param storeLevelByIdRequest 单个查询商户客户等级表请求参数 {@link StoreLevelByIdRequest}
     * @return 商户客户等级表详情 {@link StoreLevelByIdResponse}
     * @author yang
     */
    @PostMapping("/customer/${application.customer.version}/storelevel/get-by-id")
    BaseResponse<StoreLevelByIdResponse> getById(@RequestBody @Valid StoreLevelByIdRequest storeLevelByIdRequest);

    /**
     * 查询客户店铺信息
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/storelevel/get-by-customer-id-and-store-id")
    BaseResponse<StoreLevelByCustomerIdAndStoreIdResponse> getByCustomerIdAndStoreId(@RequestBody @Valid StoreLevelByCustomerIdAndStoreIdRequest request);

    /**
     * 查询客户店铺信息
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/storelevel/info")
    BaseResponse<StroeLevelInfoResponse> queryStoreLevelInfo(@RequestBody @Valid StoreLevelByStoreIdRequest request);


    /**
     * 查询客户等级信息
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/customerlevel/info")
    BaseResponse<CustomerLevelInfoResponse> queryCustomerLevelInfo(@RequestBody @Valid CustomerLevelRequest request);



    /**
     * 查询客户店铺信息
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/storelevel/get-by-store-level-id")
    BaseResponse<StroeLevelInfoByIdResponse> queryStoreLevelInfoByStoreLevelId(@RequestBody @Valid StoreLevelByIdRequest request);

    /**
     * 查询满足升级条件的店铺等级列表
     */
    @PostMapping("/customer/${application.customer.version}/storelevel/get-by-levelup-condition")
    BaseResponse<StoreLevelByIdResponse> queryByLevelUpCondition(@RequestBody @Valid StoreLevelQueryRequest request);
}

