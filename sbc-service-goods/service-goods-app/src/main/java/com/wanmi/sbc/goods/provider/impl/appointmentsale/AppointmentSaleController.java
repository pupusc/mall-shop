package com.wanmi.sbc.goods.provider.impl.appointmentsale;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.provider.appointmentsale.AppointmentSaleProvider;
import com.wanmi.sbc.goods.api.request.appointmentsale.*;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleAddResponse;
import com.wanmi.sbc.goods.api.response.appointmentsale.AppointmentSaleModifyResponse;
import com.wanmi.sbc.goods.appointmentsale.model.root.AppointmentSale;
import com.wanmi.sbc.goods.appointmentsale.service.AppointmentSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>预约抢购保存服务接口实现</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@RestController
@Validated
public class AppointmentSaleController implements AppointmentSaleProvider {
    @Autowired
    private AppointmentSaleService appointmentSaleService;

    @Override
    public BaseResponse<AppointmentSaleAddResponse> add(@RequestBody @Valid AppointmentSaleAddRequest appointmentSaleAddRequest) {
        AppointmentSale appointmentSale = KsBeanUtil.convert(appointmentSaleAddRequest, AppointmentSale.class);
        return BaseResponse.success(new AppointmentSaleAddResponse(
                appointmentSaleService.wrapperVo(appointmentSaleService.add(appointmentSale))));
    }

    @Override
    public BaseResponse<AppointmentSaleModifyResponse> modify(@RequestBody @Valid AppointmentSaleModifyRequest appointmentSaleModifyRequest) {
        AppointmentSale appointmentSale = KsBeanUtil.convert(appointmentSaleModifyRequest, AppointmentSale.class);
        return BaseResponse.success(new AppointmentSaleModifyResponse(
                appointmentSaleService.wrapperVo(appointmentSaleService.modify(appointmentSale))));
    }

    @Override
    public BaseResponse deleteById(@RequestBody @Valid AppointmentSaleDelByIdRequest appointmentSaleDelByIdRequest) {
        AppointmentSale appointmentSale = appointmentSaleService.getOne(appointmentSaleDelByIdRequest.getId(), appointmentSaleDelByIdRequest.getStoreId());
        if (Objects.isNull(appointmentSale)) {
            throw new SbcRuntimeException("K-000009");
        }
        appointmentSale.setDelFlag(DeleteFlag.YES);
        appointmentSaleService.deleteById(appointmentSale);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse deleteByIdList(@RequestBody @Valid AppointmentSaleDelByIdListRequest appointmentSaleDelByIdListRequest) {
        List<AppointmentSale> appointmentSaleList = appointmentSaleDelByIdListRequest.getIdList().stream()
                .map(Id -> {
                    AppointmentSale appointmentSale = KsBeanUtil.convert(Id, AppointmentSale.class);
                    appointmentSale.setDelFlag(DeleteFlag.YES);
                    return appointmentSale;
                }).collect(Collectors.toList());
        appointmentSaleService.deleteByIdList(appointmentSaleList);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyStatus(@RequestBody @Valid AppointmentSaleStatusRequest request) {
        AppointmentSale sale = appointmentSaleService.getOne(request.getId(), request.getStoreId());
        sale.setPauseFlag(request.getPauseFlag());
        appointmentSaleService.modifyStatus(sale);
        return BaseResponse.SUCCESSFUL();
    }
}

