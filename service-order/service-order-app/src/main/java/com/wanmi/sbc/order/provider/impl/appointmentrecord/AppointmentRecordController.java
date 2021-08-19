package com.wanmi.sbc.order.provider.impl.appointmentrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.bean.enums.CustomerStatus;
import com.wanmi.sbc.order.api.provider.appointmentrecord.AppointmentRecordProvider;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordRequest;
import com.wanmi.sbc.order.appointmentrecord.model.root.AppointmentRecord;
import com.wanmi.sbc.order.appointmentrecord.model.root.Customer;
import com.wanmi.sbc.order.appointmentrecord.service.AppointmentRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>预约抢购记录Provider</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@RestController
@Validated
public class AppointmentRecordController implements AppointmentRecordProvider {

    @Autowired
    private AppointmentRecordService appointmentRecordService;


    @Autowired
    private CustomerQueryProvider customerQueryProvider;

    /**
     * 预约
     *
     * @param request 预约参数结构 {@link AppointmentRecordRequest}
     * @return 新增的预约信息 {@link }
     * @author zxd
     */
    @Override
    public BaseResponse add(@RequestBody @Valid AppointmentRecordRequest request) {

        // 客户信息
        CustomerGetByIdResponse customer = customerQueryProvider.getCustomerById(new CustomerGetByIdRequest
                (request.getBuyerId())).getContext();

        if (customer == null) {
            throw new SbcRuntimeException("K-010014");
        }
        if (customer.getCustomerDetail().getCustomerStatus() == CustomerStatus.DISABLE) {
            throw new SbcRuntimeException("K-010015");
        }
        AppointmentRecord appointmentRecord = KsBeanUtil.convert(request, AppointmentRecord.class);
        appointmentRecord.setCustomer(Customer.fromCustomer(customer));
        appointmentRecordService.add(appointmentRecord);

        return BaseResponse.SUCCESSFUL();
    }

}

