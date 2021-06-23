package com.wanmi.sbc.goods.api.provider.appointmentsalegoods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.request.appointmentsale.RushToAppointmentSaleGoodsRequest;
import com.wanmi.sbc.goods.api.request.appointmentsalegoods.*;
import com.wanmi.sbc.goods.api.response.appointmentsalegoods.AppointmentSaleGoodsAddResponse;
import com.wanmi.sbc.goods.api.response.appointmentsalegoods.AppointmentSaleGoodsModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>预约抢购保存服务Provider</p>
 *
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@FeignClient(value = "${application.goods.name}", contextId = "AppointmentSaleGoodsProvider")
public interface AppointmentSaleGoodsProvider {

    /**
     * 新增预约抢购API
     *
     * @param appointmentSaleGoodsAddRequest 预约抢购新增参数结构 {@link AppointmentSaleGoodsAddRequest}
     * @return 新增的预约抢购信息 {@link AppointmentSaleGoodsAddResponse}
     * @author zxd
     */
    @PostMapping("/goods/${application.goods.version}/appointmentsalegoods/add")
    BaseResponse<AppointmentSaleGoodsAddResponse> add(@RequestBody @Valid AppointmentSaleGoodsAddRequest appointmentSaleGoodsAddRequest);

    /**
     * 修改预约抢购API
     *
     * @param appointmentSaleGoodsModifyRequest 预约抢购修改参数结构 {@link AppointmentSaleGoodsModifyRequest}
     * @return 修改的预约抢购信息 {@link AppointmentSaleGoodsModifyResponse}
     * @author zxd
     */
    @PostMapping("/goods/${application.goods.version}/appointmentsalegoods/modify")
    BaseResponse<AppointmentSaleGoodsModifyResponse> modify(@RequestBody @Valid AppointmentSaleGoodsModifyRequest appointmentSaleGoodsModifyRequest);

    /**
     * 单个删除预约抢购API
     *
     * @param appointmentSaleGoodsDelByIdRequest 单个删除参数结构 {@link AppointmentSaleGoodsDelByIdRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author zxd
     */
    @PostMapping("/goods/${application.goods.version}/appointmentsalegoods/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid AppointmentSaleGoodsDelByIdRequest appointmentSaleGoodsDelByIdRequest);

    /**
     * 批量删除预约抢购API
     *
     * @param appointmentSaleGoodsDelByIdListRequest 批量删除参数结构 {@link AppointmentSaleGoodsDelByIdListRequest}
     * @return 删除结果 {@link BaseResponse}
     * @author zxd
     */
    @PostMapping("/goods/${application.goods.version}/appointmentsalegoods/delete-by-id-list")
    BaseResponse deleteByIdList(@RequestBody @Valid AppointmentSaleGoodsDelByIdListRequest appointmentSaleGoodsDelByIdListRequest);


    /**
     * 更新预约数量
     *
     * @param request
     */
    @PostMapping("/goods/${application.goods.version}/appointmentsalegoods/update-appointment-count")
    BaseResponse updateAppointmentCount(@RequestBody @Valid RushToAppointmentSaleGoodsRequest request);


    /**
     * 更新购买数量
     *
     * @param request
     */
    @PostMapping("/goods/${application.goods.version}/appointmentsalegoods/update-buy-count")
    BaseResponse updateBuyCount(@RequestBody @Valid AppointmentSaleGoodsCountRequest request);
}

