package com.wanmi.sbc.goods.api.provider.cate;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateAddRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateDeleteByIdRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateModifyRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateBatchModifySortRequest;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateAddResponse;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateDeleteByIdResponse;
import com.wanmi.sbc.goods.api.response.cate.GoodsCateModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * com.wanmi.sbc.goods.api.provider.goodscate.GoodsCateProvider
 * 商品分类增、删、改接口
 * @author lipeng
 * @dateTime 2018/11/1 下午3:08
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsCateProvider")
public interface GoodsCateProvider {

    /**
     * 新增商品分类
     *
     * @param request {@link GoodsCateAddRequest}
     * @return 新增结果 {@link GoodsCateAddResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/add")
    BaseResponse<GoodsCateAddResponse> add(@RequestBody @Valid GoodsCateAddRequest request);

    /**
     * 修改商品分类
     *
     * @param request {@link GoodsCateModifyRequest}
     * @return 修改结果 {@link GoodsCateModifyResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/modify")
    BaseResponse<GoodsCateModifyResponse> modify(@RequestBody @Valid GoodsCateModifyRequest request);

    /**
     * 根据编号删除商品分类
     *
     * @param request {@link GoodsCateDeleteByIdRequest}
     * @return 删除的分类编号列表 {@link GoodsCateDeleteByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/delete-by-id")
    BaseResponse<GoodsCateDeleteByIdResponse> deleteById(@RequestBody @Valid GoodsCateDeleteByIdRequest request);

    /**
     * 批量修改分类排序
     *
     * @param request 批量分类排序信息结构 {@link GoodsCateBatchModifySortRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/cate/batch-modify-sort")
    BaseResponse batchModifySort(@RequestBody @Valid GoodsCateBatchModifySortRequest request);

    /**
     * 初始化商品分类信息
     *
     */
    @PostMapping("/goods/${application.goods.version}/cate/init")
    BaseResponse init();

    /**
     * 同步成长值购物规则为积分购物规则
     *
     */
    @PostMapping("/goods/${application.goods.version}/cate/synchronize-points-rule")
    BaseResponse synchronizePointsRule();
}
