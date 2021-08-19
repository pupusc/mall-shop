package com.wanmi.sbc.goods.api.provider.standard;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.standard.*;
import com.wanmi.sbc.goods.api.response.standard.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>商品库查询接口</p>
 * author: sunkun
 * Date: 2018-11-07
 */
@FeignClient(value = "${application.goods.name}", contextId = "StandardGoodsQueryProvider")
public interface StandardGoodsQueryProvider {

    /**
     * 分页查询商品库
     * @param request 分页查询商品库 {@link StandardGoodsPageRequest}
     * @return {@link StandardGoodsPageResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/page")
    BaseResponse<StandardGoodsPageResponse> page(@RequestBody @Valid StandardGoodsPageRequest request);

    /**
     * 分页查询商品库
     * @param request 分页查询商品库 {@link StandardGoodsPageRequest}
     * @return {@link StandardGoodsPageResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/simple-page")
    BaseResponse<StandardGoodsPageResponse> simplePage(@RequestBody @Valid StandardGoodsPageRequest request);

    /**
     * 根据ID查询商品库
     * @param request 根据ID查询商品库 {@link StandardGoodsByIdRequest}
     * @return {@link StandardGoodsByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/get-by-id")
    BaseResponse<StandardGoodsByIdResponse> getById(@RequestBody @Valid StandardGoodsByIdRequest request);

    /**
     * 根据ID查询商品库
     * @param request 根据ID查询商品库 {@link StandardGoodsByIdRequest}
     * @return {@link StandardGoodsByIdResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/get-by-standard-id")
    BaseResponse<List<String>> getGoodsIdByStandardId(@RequestBody @Valid StandardGoodsByIdRequest request);


    /**
     * 列出已被导入的商品库ID
     * @param request 列出已被导入的商品库ID {@link StandardGoodsGetUsedStandardRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/get-used-standard")
    BaseResponse<StandardGoodsGetUsedStandardResponse> getUsedStandard(@RequestBody @Valid StandardGoodsGetUsedStandardRequest request);

    /**
     * 列出已被导入的商品ID
     * @param request 列出已被导入的商品ID {@link StandardGoodsGetUsedGoodsRequest}
     * @return {@link StandardGoodsGetUsedGoodsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/get-used-goods")
    BaseResponse<StandardGoodsGetUsedGoodsResponse> getUsedGoods(@RequestBody @Valid StandardGoodsGetUsedGoodsRequest request);

    /**
     * 列出已被导入的SKUID
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/standard/list-used-goods-id")
    BaseResponse<StandardGoodsListUsedGoodsIdResponse> listUsedGoodsId(@RequestBody @Valid StandardGoodsListUsedGoodsIdRequest request);

    /**
     * 列出需要同步的的商品库ID
     * @param request 列出已被导入的商品库ID {@link StandardGoodsGetUsedStandardRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/get-need-syn-standard")
    BaseResponse<StandardGoodsGetUsedStandardResponse> getNeedSynStandard(@RequestBody @Valid StandardGoodsGetUsedStandardRequest request);

    /**
     * 根据商品库Id查询关联信息
     * @param request 商品库ID {@link StandardGoodsRelByGoodsIdsRequest}
     * @return 关联信息 {@link StandardGoodsRelByGoodsIdsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/list-rel-by-goods-ids")
    BaseResponse<StandardGoodsRelByGoodsIdsResponse> listRelByGoodsIds(@RequestBody @Valid StandardGoodsRelByGoodsIdsRequest request);

    /**
     * 根据商品Id查询关联商品库Id信息
     * @param request 商品ID {@link StandardIdsByGoodsIdsRequest}
     * @return 关联商品库ID信息 {@link StandardIdsByGoodsIdsResponse}
     */
    @PostMapping("/goods/${application.goods.version}/standard/list-standard-ids-by-goods-ids")
    BaseResponse<StandardIdsByGoodsIdsResponse> listStandardIdsByGoodsIds(@RequestBody @Valid StandardIdsByGoodsIdsRequest request);
}
