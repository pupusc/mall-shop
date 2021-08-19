package com.wanmi.sbc.goods.api.provider.pointsgoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsByIdRequest;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsByStoreIdRequest;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsListRequest;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsPageRequest;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsByIdResponse;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsListResponse;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>积分商品表查询服务Provider</p>
 *
 * @author yang
 * @date 2019-05-07 15:01:41
 */
@FeignClient(value = "${application.goods.name}", contextId = "PointsGoodsQueryProvider")
public interface PointsGoodsQueryProvider {

    /**
     * 分页查询积分商品表API
     *
     * @param pointsGoodsPageReq 分页请求参数和筛选对象 {@link PointsGoodsPageRequest}
     * @return 积分商品表分页列表信息 {@link PointsGoodsPageResponse}
     * @author yang
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/page")
    BaseResponse<PointsGoodsPageResponse> page(@RequestBody @Valid PointsGoodsPageRequest pointsGoodsPageReq);

    /**
     * 列表查询积分商品表API
     *
     * @param pointsGoodsListReq 列表请求参数和筛选对象 {@link PointsGoodsListRequest}
     * @return 积分商品表的列表信息 {@link PointsGoodsListResponse}
     * @author yang
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/list")
    BaseResponse<PointsGoodsListResponse> list(@RequestBody @Valid PointsGoodsListRequest pointsGoodsListReq);

    /**
     * 单个查询积分商品表API
     *
     * @param pointsGoodsByIdRequest 单个查询积分商品表请求参数 {@link PointsGoodsByIdRequest}
     * @return 积分商品表详情 {@link PointsGoodsByIdResponse}
     * @author yang
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/get-by-id")
    BaseResponse<PointsGoodsByIdResponse> getById(@RequestBody @Valid PointsGoodsByIdRequest pointsGoodsByIdRequest);

    /**
     * 查询过期积分商品
     *
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/query_overdue_list")
    BaseResponse<PointsGoodsListResponse> queryOverdueList();

    /**
     * 根据店铺id查询积分商品表
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/query-by-store-id")
    BaseResponse<PointsGoodsListResponse> getByStoreId(@RequestBody @Valid PointsGoodsByStoreIdRequest request);

    /**
     * 分页查询积分商品表API
     *
     * @param pointsGoodsPageReq 分页请求参数和筛选对象 {@link PointsGoodsPageRequest}
     * @return 积分商品表分页列表信息 {@link PointsGoodsPageResponse}
     * @author yang
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/page-new")
    BaseResponse<PointsGoodsPageResponse> pageNew(@RequestBody @Valid PointsGoodsPageRequest pointsGoodsPageReq);

}

