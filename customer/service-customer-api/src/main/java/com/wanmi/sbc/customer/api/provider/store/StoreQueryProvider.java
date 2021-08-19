package com.wanmi.sbc.customer.api.provider.store;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.store.*;
import com.wanmi.sbc.customer.api.response.store.*;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>店铺查询Provider</p>
 * Created by of628-wenzhi on 2018-09-11-下午6:15.
 */
@FeignClient(value = "${application.customer.name}", contextId = "StoreQueryProvider")
public interface StoreQueryProvider {
    /**
     * 分页查询店铺信息
     *
     * @param storePageRequest 分页请求参数和筛选对象 {@link StorePageRequest}
     * @return 带分页的店铺信息 {@link StorePageResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/page")
    BaseResponse<StorePageResponse> page(@RequestBody @Valid StorePageRequest storePageRequest);

    /**
     * 根据storeId查询店铺信息（包含已删除），若未找到任何匹配则做异常抛出
     *
     * @param storeByIdRequest 带storeId的请求参数 {@link StoreByIdRequest}
     * @return 店铺信息 {@link StoreByIdRequest}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-by-id")
    BaseResponse<StoreByIdResponse> getById(@RequestBody @Valid StoreByIdRequest storeByIdRequest);
    /**
     * 根据companySourceType查询店铺信息
     *
     */
    @PostMapping("/customer/${application.customer.version}/store/getBycompanySourceType")
    BaseResponse<StoreVO> getBycompanySourceType(@RequestBody @Valid StoreBycompanySourceType request);

    /**
     * 根据storeId查询未删除店铺信息，不存在则做异常抛出
     *
     * @param noDeleteStoreByIdRequest 带storeId的请求参数 {@link NoDeleteStoreByIdRequest}
     * @return 店铺信息 {@link NoDeleteStoreByIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-no-delete-store-by-id")
    BaseResponse<NoDeleteStoreByIdResponse> getNoDeleteStoreById(@RequestBody @Valid NoDeleteStoreByIdRequest
                                                                         noDeleteStoreByIdRequest);

    /**
     * 根据storeId查询有效店铺信息，不存在，已删除，已过期，已关店则做异常抛出
     *
     * @param validStoreByIdRequest 带storeId的请求参数 {@link ValidStoreByIdRequest}
     * @return 有效店铺信息 {@link ValidStoreByIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-valid-store-by-id")
    BaseResponse<ValidStoreByIdResponse> getValidStoreById(@RequestBody @Valid ValidStoreByIdRequest
                                                                   validStoreByIdRequest);

    /**
     * 根据storeId查询店铺基础信息，针对前端用户查询店铺首页场景，隐藏了一些不必要的店铺信息
     *
     * @param storeBaseInfoByIdRequest 带storeId的请求参数 {@link StoreBaseInfoByIdRequest}
     * @return 店铺基础信息 {@link StoreBaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-store-base-info-by-id")
    BaseResponse<StoreBaseResponse> getStoreBaseInfoById(@RequestBody @Valid StoreBaseInfoByIdRequest
                                                                 storeBaseInfoByIdRequest);

    /**
     * 根据storeId查询店铺基础信息，针对商家查询自己店铺信息场景
     *
     * @param bossStoreBaseInfoByIdRequest 带storeId的请求参数 {@link StoreBaseInfoByIdRequest}
     * @return 店铺基础信息 {@link BossStoreBaseInfoResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-boss-store-base-info-by-id")
    BaseResponse<BossStoreBaseInfoResponse> getBossStoreBaseInfoById(@RequestBody @Valid BossStoreBaseInfoByIdRequest
                                                                             bossStoreBaseInfoByIdRequest);

    /**
     * 根据storeId查询包含商家店铺和主账号的信息
     *
     * @param storeInfoByIdRequest 带storeId的请求参数 {@link StoreInfoByIdRequest}
     * @return 店铺基础信息 {@link StoreInfoResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-store-info-by-id")
    BaseResponse<StoreInfoResponse> getStoreInfoById(@RequestBody @Valid StoreInfoByIdRequest
                                                             storeInfoByIdRequest);

    /**
     * 根据商家id查询店铺信息
     *
     * @param storeByCompanyInfoIdRequest 带商家id的请求参数 {@link StoreByCompanyInfoIdRequest}
     * @return 店铺信息 {@link StoreByCompanyInfoIdResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-store-by-company-info-id")
    BaseResponse<StoreByCompanyInfoIdResponse> getStoreByCompanyInfoId(@RequestBody @Valid StoreByCompanyInfoIdRequest
                                                                               storeByCompanyInfoIdRequest);

    /**
     * 根据storeId 查询店铺档案信息(会员查看店铺档案时使用)
     *
     * @param storeDocumentByIdRequest 带storeId的请求参数 {@link StoreDocumentByIdRequest}
     * @return 店铺档案信息 {@link StoreDocumentResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-store-document-by-id")
    BaseResponse<StoreDocumentResponse> getStoreDocumentById(@RequestBody @Valid StoreDocumentByIdRequest
                                                                     storeDocumentByIdRequest);

    /**
     * 根据ids查询未删除的店铺列表
     *
     * @param listNoDeleteStoreByIdsRequest 带storeId集合的请求参数 {@link ListNoDeleteStoreByIdsRequest}
     * @return 店铺列表信息 {@link ListNoDeleteStoreByIdsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-no-delete-store-by-ids")
    BaseResponse<ListNoDeleteStoreByIdsResponse> listNoDeleteStoreByIds(@RequestBody @Valid
                                                                                ListNoDeleteStoreByIdsRequest
                                                                                listNoDeleteStoreByIdsRequest);


    @PostMapping("/customer/${application.customer.version}/store/list-company-store-by-company-ids")
    BaseResponse<ListCompanyStoreByCompanyIdsResponse> listCompanyStoreByCompanyIds(@RequestBody @Valid ListCompanyStoreByCompanyIdsRequest request);

    /**
     * 根据ids查询（包含已删除）店铺列表
     *
     * @param listStoreByIdsRequest 带storeId集合的请求参数 {@link ListStoreByIdsRequest}
     * @return 包含已删除的店铺列表信息 {@link ListStoreByIdsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-by-ids")
    BaseResponse<ListStoreByIdsResponse> listByIds(@RequestBody @Valid ListStoreByIdsRequest
                                                           listStoreByIdsRequest);

    /**
     * 根据结算周期获取账期内有效店铺,进行结算明细的定时任务
     *
     * @param listStoreForSettleRequest 传入的结算周期单位为日 {@link ListStoreForSettleRequest}
     * @return 返回账期内有效店铺列表 {@link ListStoreForSettleResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-for-settle")
    BaseResponse<ListStoreForSettleResponse> listForSettle(@RequestBody @Valid ListStoreForSettleRequest
                                                                   listStoreForSettleRequest);

    /**
     * 根据店铺名称模糊匹配未删除店铺列表
     *
     * @param listStoreByNameRequest 包含店铺名称关键字 {@link ListStoreByNameRequest}
     * @return 返回匹配的店铺列表 {@link ListStoreByNameResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-by-name")
    BaseResponse<ListStoreByNameResponse> listByName(@RequestBody @Valid ListStoreByNameRequest
                                                             listStoreByNameRequest);

    /**
     * 根据店铺名称模糊匹配店铺列表，自动关联5条信息
     *
     * @param listStoreByNameForAutoCompleteRequest 包含店铺名称关键字 {@link ListStoreByNameForAutoCompleteRequest}
     * @return 返回匹配的店铺列表 {@link ListStoreByNameForAutoCompleteResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-by-name-for-auto-complete")
    BaseResponse<ListStoreByNameForAutoCompleteResponse> listByNameForAutoComplete(@RequestBody @Valid
                                                                                           ListStoreByNameForAutoCompleteRequest
                                                                                           listStoreByNameForAutoCompleteRequest);

    /**
     * 条件筛选所有店铺列表
     *
     * @param listStoreRequest 筛选条件 {@link ListStoreRequest}
     * @return 店铺列表信息 {@link ListStoreResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-store")
    BaseResponse<ListStoreResponse> listStore(@RequestBody @Valid ListStoreRequest listStoreRequest);

    /**
     * 根据店铺ids查询店铺自定义字段列表
     *
     * @param request 筛选条件 {@link StorePartColsListByIdsRequest}
     * @return 店铺列表信息 {@link StorePartColsListByIdsResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-store-part-cols-by-ids")
    BaseResponse<StorePartColsListByIdsResponse> listStorePartColsByIds(@RequestBody @Valid StorePartColsListByIdsRequest request);


    /**
     * 根据店铺id查询店铺主页信息
     *
     * @param storeHomeInfoRequest 包含店铺id {@link StoreHomeInfoRequest}
     * @return 店铺主页信息 {@link StoreHomeInfoResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-store-home-info")
    BaseResponse<StoreHomeInfoResponse> getStoreHomeInfo(@RequestBody @Valid StoreHomeInfoRequest storeHomeInfoRequest);

    /**
     * 根据店铺名称获取未删除店铺信息
     *
     * @param request {@link NoDeleteStoreGetByStoreNameRequest}
     * @return 店铺信息 {@link NoDeleteStoreByStoreNameResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/get-no-delete-store-by-store-name")
    BaseResponse<NoDeleteStoreByStoreNameResponse> getNoDeleteStoreByStoreName(@RequestBody @Valid
                                                                                       NoDeleteStoreGetByStoreNameRequest
                                                                                       request);

    /**
     * 根据店铺名称模糊匹配未删除店铺列表
     *
     * @param listStoreByNameRequest 包含店铺名称关键字 {@link ListStoreByNameRequest}
     * @return 返回匹配的店铺列表 {@link ListStoreByNameResponse}
     */
    @PostMapping("/customer/${application.customer.version}/store/list-by-store-name")
    BaseResponse<StoreListForDistributionResponse> listByStoreName(@RequestBody @Valid ListStoreByNameRequest
                                                                           listStoreByNameRequest);

    /**
     * 根据店铺ID集合查询已过期的店铺ID集合
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/store/list-by-store-ids")
    BaseResponse<StoreListByStoreIdsResponse> listByStoreIds(@RequestBody @Valid StoreListByStoreIdsRequest request);

    /**
     * 根据店铺id列表查询店铺名称
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/store/list-store-name-by-store-ids")
    BaseResponse<StoreNameListByStoreIdsResponse> listStoreNameByStoreIds(@RequestBody @Valid StoreNameListByStoreIdsResquest request);

    /**
     * 检验店铺信息
     *
     * @param storeByStoreIdAndCompanyInfoIdRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/store/check-info")
    BaseResponse<Boolean> checkStoreInfo(@RequestBody @Valid StoreByStoreIdAndCompanyInfoIdRequest storeByStoreIdAndCompanyInfoIdRequest);
}
