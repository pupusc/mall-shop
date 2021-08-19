package com.wanmi.sbc.goods.provider.impl.pointsgoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.EnableStatus;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsSaveProvider;
import com.wanmi.sbc.goods.api.request.pointsgoods.*;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsAddResponse;
import com.wanmi.sbc.goods.api.response.pointsgoods.PointsGoodsModifyResponse;
import com.wanmi.sbc.goods.pointsgoods.model.root.PointsGoods;
import com.wanmi.sbc.goods.pointsgoods.service.PointsGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>积分商品表保存服务接口实现</p>
 *
 * @author yang
 * @date 2019-05-07 15:01:41
 */
@RestController
@Validated
public class PointsGoodsSaveController implements PointsGoodsSaveProvider {
    @Autowired
    private PointsGoodsService pointsGoodsService;

    @Override
    public BaseResponse<PointsGoodsAddResponse> add(@RequestBody @Valid PointsGoodsAddRequest pointsGoodsAddRequest) {
        pointsGoodsAddRequest.setStatus(EnableStatus.ENABLE);
        pointsGoodsAddRequest.setDelFlag(DeleteFlag.NO);
        pointsGoodsAddRequest.setSales((long) 0);
        if (Objects.isNull(pointsGoodsAddRequest.getRecommendFlag())) {
            pointsGoodsAddRequest.setRecommendFlag(BoolFlag.NO);
        }
        PointsGoods pointsGoods = new PointsGoods();
        KsBeanUtil.copyPropertiesThird(pointsGoodsAddRequest, pointsGoods);
        return BaseResponse.success(new PointsGoodsAddResponse(
                pointsGoodsService.wrapperVo(pointsGoodsService.add(pointsGoods))));
    }

    @Override
    public BaseResponse batchAdd(@RequestBody @Valid PointsGoodsAddListRequest pointsGoodsAddListRequest) {
        List<PointsGoodsAddRequest> pointsGoodsAddRequestList = pointsGoodsAddListRequest.getPointsGoodsAddRequestList();
        List pointsGoodsList = KsBeanUtil.copyListProperties(pointsGoodsAddRequestList, PointsGoods.class);
        pointsGoodsService.batchAdd(pointsGoodsList);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse<PointsGoodsModifyResponse> modify(@RequestBody @Valid PointsGoodsModifyRequest pointsGoodsModifyRequest) {
        PointsGoods pointsGoods = new PointsGoods();
        KsBeanUtil.copyPropertiesThird(pointsGoodsModifyRequest, pointsGoods);
        return BaseResponse.success(new PointsGoodsModifyResponse(
                pointsGoodsService.wrapperVo(pointsGoodsService.modify(pointsGoods))));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid PointsGoodsDelByIdRequest pointsGoodsDelByIdRequest) {
        pointsGoodsService.deleteById(pointsGoodsDelByIdRequest.getPointsGoodsId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid PointsGoodsDelByIdListRequest pointsGoodsDelByIdListRequest) {
        pointsGoodsService.deleteByIdList(pointsGoodsDelByIdListRequest.getPointsGoodsIdList());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyStatus(@RequestBody @Valid PointsGoodsSwitchRequest request) {
        PointsGoods pointsGoods = pointsGoodsService.getById(request.getPointsGoodsId());
        pointsGoods.setStatus(request.getStatus());
        pointsGoods.setUpdateTime(request.getUpdateTime());
        pointsGoods.setUpdatePerson(request.getUpdatePerson());
        pointsGoodsService.modifyStatus(pointsGoods);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse minusStock(@RequestBody @Valid PointsGoodsMinusStockRequest request) {
        pointsGoodsService.subStockById(request.getStock(), request.getPointsGoodsId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse resetStockById(@RequestBody @Valid PointsGoodsMinusStockRequest request) {
        pointsGoodsService.resetStockById(request.getPointsGoodsId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updatePointsGoodsSalesNum(@RequestBody @Valid PointsGoodsSalesModifyRequest pointsGoodsSalesModifyRequest) {
        pointsGoodsService.updatePointsGoodsSalesNum(pointsGoodsSalesModifyRequest);
        return BaseResponse.SUCCESSFUL();
    }
}

