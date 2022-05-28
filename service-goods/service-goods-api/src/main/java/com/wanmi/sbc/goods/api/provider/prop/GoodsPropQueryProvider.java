package com.wanmi.sbc.goods.api.provider.prop;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.prop.GoodsPropListAllByCateIdRequest;
import com.wanmi.sbc.goods.api.request.prop.GoodsPropListByCateIdRequest;
import com.wanmi.sbc.goods.api.request.prop.GoodsPropListByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.request.prop.GoodsPropListIndexByCateIdRequest;
import com.wanmi.sbc.goods.api.request.prop.GoodsPropListInitSortRequest;
import com.wanmi.sbc.goods.api.request.prop.GoodsPropQueryIsChildNodeRequest;
import com.wanmi.sbc.goods.api.request.prop.GoodsPropQueryPropDetailsOverStepRequest;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropListAllByCateIdResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropListByCateIdResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropListByGoodsIdsResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropListIndexByCateIdResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropListInitSortResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropQueryIsChildNodeResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropQueryPropDetailsOverStepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/10/31 14:38
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsPropQueryProvider")
public interface GoodsPropQueryProvider {

    /**
     * 根据类目ID查询商品属性
     * @param goodsPropListAllByCateIdRequest {@link GoodsPropListAllByCateIdRequest }
     * @return 商品属性集合 {@link GoodsPropListAllByCateIdResponse }
    */
    @PostMapping("/goods/${application.goods.version}/prop/list-all-by-cate-id")
    BaseResponse<GoodsPropListAllByCateIdResponse> listAllByCateId(@RequestBody @Valid GoodsPropListAllByCateIdRequest goodsPropListAllByCateIdRequest);

    /**
     * 根据类目ID查询需要索引的商品属性列表
     * (供用户根据商品属性进行筛选商品)
     * @param goodsPropListIndexByCateIdRequest {@link GoodsPropListIndexByCateIdRequest }
     * @return 商品属性集合 {@link GoodsPropListIndexByCateIdResponse }
     */
    @PostMapping("/goods/${application.goods.version}/prop/list-index-by-cate-id")
    BaseResponse<GoodsPropListIndexByCateIdResponse> listIndexByCateId(@RequestBody @Valid GoodsPropListIndexByCateIdRequest goodsPropListIndexByCateIdRequest);

    /**
     * 每次新增初始化排序
     * @param goodsPropListInitSortRequest {@link GoodsPropListInitSortRequest }
     * @return 商品属性集合 {@link GoodsPropListInitSortResponse }
     */
    @PostMapping("/goods/${application.goods.version}/prop/list-init-sort")
    BaseResponse<GoodsPropListInitSortResponse> listInitSort(@RequestBody @Valid GoodsPropListInitSortRequest goodsPropListInitSortRequest);

    /**
     * 判断属性值是否超限
     * @param goodsPropQueryPropDetailsOverStepRequest {@link GoodsPropQueryPropDetailsOverStepRequest }
     * @return {@link GoodsPropQueryPropDetailsOverStepResponse }
    */
    @PostMapping("/goods/${application.goods.version}/prop/query-prop-details-over-step")
    BaseResponse<GoodsPropQueryPropDetailsOverStepResponse> queryPropDetailsOverStep(@RequestBody @Valid GoodsPropQueryPropDetailsOverStepRequest goodsPropQueryPropDetailsOverStepRequest);

    /**
     * 判断是否是商品属性三级节点
     * @param goodsPropQueryIsChildNodeRequest {@link GoodsPropQueryIsChildNodeRequest }
     * @return {@link GoodsPropQueryIsChildNodeResponse }
     */
    @PostMapping("/goods/${application.goods.version}/prop/query-is-child-node")
    BaseResponse<GoodsPropQueryIsChildNodeResponse> queryIsChildNode(@RequestBody @Valid GoodsPropQueryIsChildNodeRequest goodsPropQueryIsChildNodeRequest);

    /**
     * 根据类别Id查询该类别下所有spuId
     * @param goodsPropListByCateIdRequest {@link GoodsPropListByCateIdRequest }
     * @return 所有spuId集合 {@link GoodsPropListByCateIdResponse }
     */
    @PostMapping("/goods/${application.goods.version}/prop/list-by-cate-id")
    BaseResponse<GoodsPropListByCateIdResponse> listByCateId(@RequestBody @Valid GoodsPropListByCateIdRequest goodsPropListByCateIdRequest);

    @PostMapping("/goods/${application.goods.version}/prop/list-by-goods-ids")
    BaseResponse<List<GoodsPropListByGoodsIdsResponse>> listByGoodsIds(@RequestBody @Valid GoodsPropListByGoodsIdsRequest goodsPropListByGoodsIdsRequest);
}
