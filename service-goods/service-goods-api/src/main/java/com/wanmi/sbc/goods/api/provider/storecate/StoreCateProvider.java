package com.wanmi.sbc.goods.api.provider.storecate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.storecate.*;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateAddResponse;
import com.wanmi.sbc.goods.api.response.storecate.StoreCateDeleteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/11/1 9:52
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "StoreCateProvider")
public interface StoreCateProvider {

    /**
     * 根据店铺ID初始化分类，生成默认分类
     * @param storeCateInitByStoreIdRequest {@link StoreCateInitByStoreIdRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/init-by-store-id")
    BaseResponse initByStoreId(@RequestBody @Valid StoreCateInitByStoreIdRequest storeCateInitByStoreIdRequest);

    /**
     * 新增商品店铺分类
     * @param storeCateAddRequest {@link StoreCateAddRequest }
     * @return 商品店铺分类 {@link StoreCateAddResponse }
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/add")
    BaseResponse<StoreCateAddResponse> add(@RequestBody @Valid StoreCateAddRequest storeCateAddRequest);

    /**
     * 编辑店铺商品分类
     * @param storeCateModifyRequest {@link StoreCateModifyRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/modify")
    BaseResponse modify(@RequestBody @Valid StoreCateModifyRequest storeCateModifyRequest);

    /**
     * 删除店铺商品分类
     * @param storeCateDeleteRequest {@link StoreCateDeleteRequest }
     * @return {@link StoreCateDeleteResponse }
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/delete")
    BaseResponse<StoreCateDeleteResponse> delete(@RequestBody @Valid StoreCateDeleteRequest storeCateDeleteRequest);

    /**
     * 商家APP里店铺分类排序
     * @param storeCateBatchSortRequest {@link StoreCateBatchSortRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/batch-sort")
    BaseResponse batchSort(@RequestBody @Valid StoreCateBatchSortRequest storeCateBatchSortRequest);

    /**
     * 批量修改分类排序
     * @param request 批量分类排序信息结构 {@link StoreCateBatchModifySortRequest }
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/store/cate/batch-modify-sort")
    BaseResponse batchModifySort(@RequestBody @Valid StoreCateBatchModifySortRequest request);
}
