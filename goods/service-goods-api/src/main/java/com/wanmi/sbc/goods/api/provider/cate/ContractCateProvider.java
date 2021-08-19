package com.wanmi.sbc.goods.api.provider.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.cate.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>签约分类操作服务</p>
 * author: sunkun
 * Date: 2018-11-05
 */
@FeignClient(value = "${application.goods.name}", contextId = "ContractCateProvider")
public interface ContractCateProvider {

    /**
     * 新增签约分类服务
     * @param request 新增签约分类服务 {@link ContractCateAddRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/contract/cate/add")
    BaseResponse add(@RequestBody @Valid ContractCateAddRequest request);

    /**
     * 根据主键id删除签约分类
     * @param request 根据主键id删除签约分类 {@link ContractCateDeleteByIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/contract/cate/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid ContractCateDeleteByIdRequest request);

    /**
     * 根据主键集合和店铺id删除签约分类
     * @param request 根据主键集合和店铺id删除签约分类 {@link ContractCateDeleteByIdsAndStoreIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/contract/cate/delete-by-ids-and-store-id")
    BaseResponse deleteByIdsAndStoreId(@RequestBody @Valid ContractCateDeleteByIdsAndStoreIdRequest request);

    /**
     * 修改签约分类
     * @param request 修改签约分类 {@link ContractCateModifyRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/contract/cate/modify")
    BaseResponse modify(@RequestBody @Valid ContractCateModifyRequest request);

    /**
     * 根据平台分类主键列表修改签约分类扣率
     * @param request 根据平台分类主键列表修改签约分类扣率 {@link ContractCateModifyCateRateByCateIdsRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/contract/cate/modify-cate-rate-by-cate-ids")
    BaseResponse modifyCateRateBycateIds(@RequestBody @Valid ContractCateModifyCateRateByCateIdsRequest request);

}
