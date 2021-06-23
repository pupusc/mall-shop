package com.wanmi.sbc.goods.api.provider.pointsgoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.pointsgoods.*;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsAddResponse;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>积分商品表保存服务Provider</p>
 *
 * @author yang
 * @date 2019-05-07 15:01:41
 */
@FeignClient(value = "${application.goods.name}", contextId = "PointsGoodsSaveProvider")
public interface PointsGoodsSaveProvider {

    /**
     * 新增积分商品表API
     *
     * @param pointsGoodsAddRequest 积分商品表新增参数结构 {@link PointsGoodsAddRequest}
     * @return 新增的积分商品表信息 {@link PointsGoodsAddResponse}
     * @author yang
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/add")
    BaseResponse<PointsGoodsAddResponse> add(@RequestBody @Valid PointsGoodsAddRequest pointsGoodsAddRequest);

    /**
     * 批量新增积分商品表API
     *
     * @param pointsGoodsAddListRequest 积分商品表新增参数结构 {@link PointsGoodsAddRequest}
     * @return 新增的积分商品表信息 {@link PointsGoodsAddResponse}
     * @author yang
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/batchAdd")
    BaseResponse batchAdd(@RequestBody @Valid PointsGoodsAddListRequest pointsGoodsAddListRequest);

    /**
     * 修改积分商品表API
     *
     * @param pointsGoodsModifyRequest 积分商品表修改参数结构 {@link PointsGoodsModifyRequest}
     * @return 修改的积分商品表信息 {@link PointsGoodsModifyResponse}
     * @author yang
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/modify")
    BaseResponse<PointsGoodsModifyResponse> modify(@RequestBody @Valid PointsGoodsModifyRequest pointsGoodsModifyRequest);

    /**
     * 单个删除积分商品表API
     *
     * @param pointsGoodsDelByIdRequest 单个删除参数结构 {@link PointsGoodsDelByIdRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author yang
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid PointsGoodsDelByIdRequest pointsGoodsDelByIdRequest);

    /**
     * 批量删除积分商品表API
     *
     * @param pointsGoodsDelByIdListRequest 批量删除参数结构 {@link PointsGoodsDelByIdListRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author yang
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/delete-by-id-list")
    BaseResponse deleteByIdList(@RequestBody @Valid PointsGoodsDelByIdListRequest pointsGoodsDelByIdListRequest);

    /**
     * 修改积分商品状态API
     *
     * @param request 积分商品表修改状态参数结构 {@link PointsGoodsSwitchRequest}
     * @return 状态结果 {@link BaseResponse}
     * @author yang
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/modify-status")
    BaseResponse modifyStatus(@RequestBody @Valid PointsGoodsSwitchRequest request);

    /**
     * 扣除积分商品库存
     *
     * @param request 包含多个库存的sku库存减量结构 {@link PointsGoodsMinusStockRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/minus-stock")
    BaseResponse minusStock(@RequestBody @Valid PointsGoodsMinusStockRequest request);

    /**
     * 根据积分商品Id库存清零并停用
     *
     * @param request
     * @return
     */
    @PostMapping("/goods/${application.goods.version}/pointsgoods/reset-stock")
    BaseResponse resetStockById(@RequestBody @Valid PointsGoodsMinusStockRequest request);

    /**
     * @Author lvzhenwei
     * @Description 更新积分商品销量
     * @Date 10:43 2019/5/29
     * @Param [pointsGoodsSalesModifyRequest]
     * @return com.wanmi.sbc.common.base.BaseResponse
     **/
    @PostMapping("/goods/${application.goods.version}/pointsgoods/update-points-goods-sales-num")
    BaseResponse updatePointsGoodsSalesNum(@RequestBody @Valid PointsGoodsSalesModifyRequest pointsGoodsSalesModifyRequest);
}

