package com.wanmi.sbc.goods.provider.impl.appointmentsalegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.appointmentsalegoods.AppointmentSaleGoodsProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.RushToAppointmentSaleGoodsRequest;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.*;
import com.wanmi.sbc.goods.api.response.appointmentsalegoods.AppointmentSaleGoodsAddResponse;
import com.wanmi.sbc.goods.api.response.appointmentsalegoods.AppointmentSaleGoodsModifyResponse;
import com.wanmi.sbc.goods.appointmentsalegoods.model.root.AppointmentSaleGoods;
import com.wanmi.sbc.goods.appointmentsalegoods.service.AppointmentSaleGoodsService;
import com.wanmi.sbc.goods.bean.dto.AppointmentSaleGoodsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>预约抢购保存服务接口实现</p>
 *
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@RestController
@Validated
public class AppointmentSaleGoodsController implements AppointmentSaleGoodsProvider {
    @Autowired
    private AppointmentSaleGoodsService appointmentSaleGoodsService;

    @Override
    public BaseResponse<AppointmentSaleGoodsAddResponse> add(@RequestBody @Valid AppointmentSaleGoodsAddRequest appointmentSaleGoodsAddRequest) {
        AppointmentSaleGoods appointmentSaleGoods = KsBeanUtil.convert(appointmentSaleGoodsAddRequest, AppointmentSaleGoods.class);
        return BaseResponse.success(new AppointmentSaleGoodsAddResponse(
                appointmentSaleGoodsService.wrapperVo(appointmentSaleGoodsService.add(appointmentSaleGoods))));
    }

    @Override
    public BaseResponse<AppointmentSaleGoodsModifyResponse> modify(@RequestBody @Valid AppointmentSaleGoodsModifyRequest appointmentSaleGoodsModifyRequest) {
        AppointmentSaleGoods appointmentSaleGoods = KsBeanUtil.convert(appointmentSaleGoodsModifyRequest, AppointmentSaleGoods.class);
        return BaseResponse.success(new AppointmentSaleGoodsModifyResponse(
                appointmentSaleGoodsService.wrapperVo(appointmentSaleGoodsService.modify(appointmentSaleGoods))));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid AppointmentSaleGoodsDelByIdRequest appointmentSaleGoodsDelByIdRequest) {
        appointmentSaleGoodsService.deleteById(appointmentSaleGoodsDelByIdRequest.getId());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid AppointmentSaleGoodsDelByIdListRequest appointmentSaleGoodsDelByIdListRequest) {
        appointmentSaleGoodsService.deleteByIdList(appointmentSaleGoodsDelByIdListRequest.getIdList());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateAppointmentCount(@RequestBody @Valid RushToAppointmentSaleGoodsRequest request) {
        appointmentSaleGoodsService.updateAppointmentCount(AppointmentSaleGoodsDTO.builder().goodsInfoId(request.getSkuId()).
                appointmentSaleId(request.getAppointmentSaleId()).build());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse updateBuyCount(@RequestBody @Valid AppointmentSaleGoodsCountRequest request) {
        appointmentSaleGoodsService.updateBuyCount(AppointmentSaleGoodsDTO.builder().goodsInfoId(request.getGoodsInfoId()).
                appointmentSaleId(request.getAppointmentSaleId()).stock(request.getStock().intValue()).build());
        return BaseResponse.SUCCESSFUL();
    }
}

