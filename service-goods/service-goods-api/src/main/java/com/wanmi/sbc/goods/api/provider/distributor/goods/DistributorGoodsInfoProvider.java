package com.wanmi.sbc.goods.api.provider.distributor.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.distributor.goods.*;
import com.wanmi.sbc.goods.api.response.distributor.goods.DistributorGoodsInfoAddResponse;
import com.wanmi.sbc.goods.api.response.distributor.goods.DistributorGoodsInfoDeleteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 分销员商品新增、修改、删除接口
 * @author: Geek Wang
 * @createDate: 2019/3/1 11:15
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "DistributorGoodsInfoProvider")
public interface DistributorGoodsInfoProvider {

    /**
     * 新增分销员商品
     * @param distributorGoodsInfoAddRequest  {@link DistributorGoodsInfoAddRequest}
     * @return {@link DistributorGoodsInfoAddResponse}
     */
    @PostMapping("/goods/${application.goods.version}/distributor/goods/add")
    BaseResponse<DistributorGoodsInfoAddResponse> add(@RequestBody @Valid DistributorGoodsInfoAddRequest distributorGoodsInfoAddRequest);

    /**
     * 根据分销员-会员ID和SKU编号删除分销员商品
     * @param distributorGoodsInfoDeleteRequest  {@link DistributorGoodsInfoDeleteRequest}
     * @return {@link DistributorGoodsInfoDeleteResponse}
     */
    @PostMapping("/goods/${application.goods.version}/distributor/goods/delete")
    BaseResponse<DistributorGoodsInfoDeleteResponse> delete(@RequestBody @Valid DistributorGoodsInfoDeleteRequest distributorGoodsInfoDeleteRequest);

    /**
     * 修改分销员商品排序
     * @param request {@link DistributorGoodsInfoModifySequenceRequest}
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/distributor/goods/modify-sequence")
    BaseResponse modifySequence(@RequestBody @Valid DistributorGoodsInfoModifySequenceRequest request);

    /**
     * 商家-社交分销开关，更新对应的分销员商品状态
     * @param request {@link DistributorGoodsInfoModifyByStoreIdAndStatusRequest}
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/distributor/goods/modify-by-store-id-and-status")
    BaseResponse modifyByStoreIdAndStatus(@RequestBody @Valid DistributorGoodsInfoModifyByStoreIdAndStatusRequest request);

    /**
     * 根据店铺ID删除分销员商品表数据
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/distributor/goods/delete-by-store-id")
    BaseResponse deleteByStoreId(@RequestBody @Valid DistributorGoodsInfoDeleteByStoreIdRequest request);

    /**
     * 根据店铺ID集合删除分销员商品表数据
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/distributor/goods/delete-by-store-ids")
    BaseResponse deleteByStoreIds(@RequestBody @Valid DistributorGoodsInfoDeleteByStoreIdsRequest request);

    /**
     * 根据会员id查询这个店铺下的分销商品数校验
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/distributor/goods/check-counts-by-customer-id")
    BaseResponse checkCountsByCustomerId(@RequestBody @Valid DistributorGoodsInfoByCustomerIdRequest request);
}
