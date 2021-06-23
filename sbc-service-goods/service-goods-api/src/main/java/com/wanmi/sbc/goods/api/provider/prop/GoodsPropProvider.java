package com.wanmi.sbc.goods.api.provider.prop;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.prop.*;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropAddDefaultRefResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropAddResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropDeleteByPropIdResponse;
import com.wanmi.sbc.goods.api.response.prop.GoodsPropModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @author: wanggang
 * @createDate: 2018/10/31 14:38
 * @version: 1.0
 */
@FeignClient(value = "${application.goods.name}", contextId = "GoodsPropProvider")
public interface GoodsPropProvider {

    /**
     * 批量修改商品属性排序
     * @param goodsPropModifySortRequest {@link GoodsPropModifySortRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/prop/modify-sort")
    BaseResponse modifySort(@RequestBody @Valid GoodsPropModifySortRequest goodsPropModifySortRequest);

    /**
     * 新增商品类目属性
     * @param goodsPropAddRequest {@link GoodsPropAddRequest }
     * @return 类别下所有spuId集合 {@link GoodsPropAddResponse }
     */
    @PostMapping("/goods/${application.goods.version}/prop/add")
    BaseResponse<GoodsPropAddResponse> add(@RequestBody @Valid GoodsPropAddRequest goodsPropAddRequest);

    /**
     * 编辑商品类目属性
     * @param goodsPropModifyRequest {@link GoodsPropModifyRequest }
     * @return {@link GoodsPropModifyResponse }
     */
    @PostMapping("/goods/${application.goods.version}/prop/modify")
    BaseResponse<GoodsPropModifyResponse> modify(@RequestBody @Valid GoodsPropModifyRequest goodsPropModifyRequest);

    /**
     * 删除商品类目属性
     * @param goodsPropDeleteByPropIdRequest {@link GoodsPropDeleteByPropIdRequest }
     * @return {@link GoodsPropDeleteByPropIdResponse }
     */
    @PostMapping("/goods/${application.goods.version}/prop/delete-by-prop-id")
    BaseResponse<GoodsPropDeleteByPropIdResponse> deleteByPropId(@RequestBody @Valid GoodsPropDeleteByPropIdRequest goodsPropDeleteByPropIdRequest);

    /**
     * 解除商品类目属性与SPU的关联
     * @param goodsPropDeleteRefByPropIdRequest {@link GoodsPropDeleteRefByPropIdRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/prop/delete-ref-by-prop-id")
    BaseResponse deleteRefByPropId(@RequestBody @Valid GoodsPropDeleteRefByPropIdRequest goodsPropDeleteRefByPropIdRequest);

    /**
     * 解除商品类目属性值与SPU的关联
     * @param goodsPropDeleteRefByPropDetailRequest {@link GoodsPropDeleteRefByPropDetailRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/prop/delete-ref-by-prop-detail")
    BaseResponse deleteRefByPropDetail(@RequestBody @Valid GoodsPropDeleteRefByPropDetailRequest goodsPropDeleteRefByPropDetailRequest);

    /**
     * 解除关联操作
     * @param goodsPropDeleteRefByPropDetailRelRequest {@link GoodsPropDeleteRefByPropDetailRelRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/prop/delete-ref-by-prop-detail-rel")
    BaseResponse deleteRefByPropDetailRel(@RequestBody @Valid GoodsPropDeleteRefByPropDetailRelRequest goodsPropDeleteRefByPropDetailRelRequest);

    /**
     * 编辑索引
     * @param goodsPropModifyIndexRequest {@link GoodsPropModifyIndexRequest }
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/prop/modify-index")
    BaseResponse modifyIndex(@RequestBody @Valid GoodsPropModifyIndexRequest goodsPropModifyIndexRequest);

    /**
     * 保存默认spu与默认属性的关联
     * @param goodsPropAddDefaultRefRequest {@link GoodsPropAddDefaultRefRequest }
     * @return  {@link GoodsPropAddDefaultRefResponse }
    */
    @PostMapping("/goods/${application.goods.version}/prop/add-default-ref")
    BaseResponse<GoodsPropAddDefaultRefResponse> addDefaultRef(@RequestBody @Valid GoodsPropAddDefaultRefRequest goodsPropAddDefaultRefRequest);
}
