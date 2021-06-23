package com.wanmi.sbc.goods.api.provider.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.standard.StandardSkuByIdRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardPartColsListByGoodsIdsRequest;
import com.wanmi.sbc.goods.api.request.standard.StandardSkuByStandardIdRequest;
import com.wanmi.sbc.goods.api.response.standard.StandardSkuByIdResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardPartColsListByGoodsIdsResponse;
import com.wanmi.sbc.goods.api.response.standard.StandardSkuByStandardIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对商品库查询接口</p>
 * Created by daiyitian on 2018-11-5-下午6:23.
 */
@FeignClient(value = "${application.goods.name}", contextId = "StandardSkuQueryProvider")
public interface StandardSkuQueryProvider {

    /**
     * 根据id获取商品库信息
     *
     * @param request 包含id的商品库信息查询结构 {@link StandardSkuByIdRequest}
     * @return 商品库信息 {@link StandardSkuByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/sku/get-by-id")
    BaseResponse<StandardSkuByIdResponse> getById(@RequestBody @Valid StandardSkuByIdRequest request);

    /**
     * 根据商品库id获取商品库sku信息
     *
     * @param request 包含id的商品库信息查询结构 {@link StandardSkuByStandardIdRequest}
     * @return 商品库Sku信息 {@link StandardSkuByStandardIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/sku/list-by-standard-id")
    BaseResponse<StandardSkuByStandardIdResponse> listByStandardId(@RequestBody @Valid StandardSkuByStandardIdRequest request);

    /**
     * 根据goodsIds批量查询商品Sku库的局部字段信息
     *
     * @param request 包含查询结构 {@link StandardPartColsListByGoodsIdsRequest}
     * @return 局部字段信息 {@link StandardPartColsListByGoodsIdsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/sku/list-part-cols-by-goods-ids")
    BaseResponse<StandardPartColsListByGoodsIdsResponse> listPartColsByGoodsIds(@RequestBody @Valid StandardPartColsListByGoodsIdsRequest request);

}
