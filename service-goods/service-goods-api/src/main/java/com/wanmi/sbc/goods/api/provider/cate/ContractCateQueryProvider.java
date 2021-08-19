package com.wanmi.sbc.goods.api.provider.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.cate.*;
import com.wanmi.sbc.goods.api.response.cate.ContractCateByIdResponse;
import com.wanmi.sbc.goods.api.response.cate.ContractCateListByConditionResponse;
import com.wanmi.sbc.goods.api.response.cate.ContractCateListCateByStoreIdResponse;
import com.wanmi.sbc.goods.api.response.cate.ContractCateListResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>签约分类查询服务</p>
 * author: sunkun
 * Date: 2018-11-05
 */
@FeignClient(value = "${application.goods.name}", contextId = "ContractCateQueryProvider")
public interface ContractCateQueryProvider {

    /**
     * 查询签约分类
     * @param request 查询签约分类 {@link ContractCateListRequest}
     * @return {@link ContractCateListResponse}
     */
    @PostMapping("/goods/${application.goods.version}/contract/cate/list")
    BaseResponse<ContractCateListResponse> list(@RequestBody @Valid ContractCateListRequest request);

    /**
     * 查询店铺已签约的类目列表，包含上级类目
     * @param request 查询店铺已签约的类目列表，包含上级类目 {@link ContractCateListCateByStoreIdRequest}
     * @return {@link ContractCateListCateByStoreIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/contract/cate/list-cate-by-store-id")
    BaseResponse<ContractCateListCateByStoreIdResponse> listCateByStoreId(@RequestBody @Valid ContractCateListCateByStoreIdRequest request);

    /**
     * 根据主键查询签约分类
     * @param request 根据主键查询签约分类 {@link ContractCateByIdRequest}
     * @return {@link ContractCateByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/contract/cate/get-by-id")
    BaseResponse<ContractCateByIdResponse> getById(@RequestBody @Valid ContractCateByIdRequest request);

    /**
     * 校验签约分类是否满足删除条件
     * @param request 校验签约分类是否满足删除条件 {@link ContractCateDelVerifyRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/contract/cate/del-verify")
    BaseResponse cateDelVerify(@RequestBody @Valid ContractCateDelVerifyRequest request);

    /**
     * 根据动态条件查询签约分类
     * @param request 条件查询签约分类 {@link ContractCateListByConditionRequest}
     * @return 签约分类列表 {@link ContractCateListByConditionResponse}
     */
    @PostMapping("/goods/${application.goods.version}/contract/cate/list-by-condition")
    BaseResponse<ContractCateListByConditionResponse> listByCondition(@RequestBody @Valid
                                                                              ContractCateListByConditionRequest
                                                                              request);

}
