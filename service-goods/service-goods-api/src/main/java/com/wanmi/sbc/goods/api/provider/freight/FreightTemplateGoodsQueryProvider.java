package com.wanmi.sbc.goods.api.provider.freight;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.freight.*;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateGoodsByIdResponse;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateGoodsByIdsResponse;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateGoodsDefaultByStoreIdResponse;
import com.wanmi.sbc.goods.api.response.freight.FreightTemplateGoodsListByStoreIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * <p>对单品运费模板查询接口</p>
 * Created by daiyitian on 2018-10-31-下午6:23.
 */
@FeignClient(value = "${application.goods.name}", contextId = "FreightTemplateGoodsQueryProvider")
public interface FreightTemplateGoodsQueryProvider {

    /**
     * 根据店铺id查询单品运费模板
     *
     * @param request 包含店铺id的查询请求结构 {@link FreightTemplateGoodsListByStoreIdRequest}
     * @return 单品运费模板列表 {@link FreightTemplateGoodsListByStoreIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/goods/list-by-store-id")
    BaseResponse<FreightTemplateGoodsListByStoreIdResponse> listByStoreId(@RequestBody @Valid
                                                                                  FreightTemplateGoodsListByStoreIdRequest request);

    /**
     * 根据批量单品运费模板id查询单品运费模板列表
     *
     * @param request 包含批量ids的查询请求结构 {@link FreightTemplateGoodsListByIdsRequest}
     * @return 单品运费模板列表 {@link FreightTemplateGoodsByIdsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/goods/list-by-ids")
    BaseResponse<FreightTemplateGoodsByIdsResponse> listByIds(@RequestBody @Valid FreightTemplateGoodsListByIdsRequest request);

    /**
     * 查询目标地区是否支持配送
     */
    @PostMapping("/goods/${application.goods.version}/freight/query-if-not-support")
    BaseResponse<Boolean> queryIfnotSupportArea(@RequestParam("provinceId") Long provinceId, @RequestParam("cityId") Long cityId);

    /**
     * 根据单品运费模板id查询单品运费模板
     *
     * @param request 包含id的查询请求结构 {@link FreightTemplateGoodsByIdRequest}
     * @return 单品运费模板 {@link FreightTemplateGoodsByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/goods/get-by-id")
    BaseResponse<FreightTemplateGoodsByIdResponse> getById(@RequestBody @Valid
                                                                   FreightTemplateGoodsByIdRequest request);


    /**
     * 根据单品运费模板id验证单品运费模板
     *
     * @param request 包含id的验证请求结构 {@link FreightTemplateGoodsExistsByIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/goods/exists-by-id")
    BaseResponse existsById(@RequestBody @Valid FreightTemplateGoodsExistsByIdRequest request);

    /**
     * 根据店铺id查询默认单品运费模板请求
     *
     * @param request 包含店铺id的查询请求结构 {@link FreightTemplateGoodsDefaultByStoreIdRequest}
     * @return 单品运费模板列表 {@link FreightTemplateGoodsDefaultByStoreIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/freight/goods/get-default-by-store-id")
    BaseResponse<FreightTemplateGoodsDefaultByStoreIdResponse> getDefaultByStoreId(@RequestBody @Valid
                                                                                           FreightTemplateGoodsDefaultByStoreIdRequest request);
}
