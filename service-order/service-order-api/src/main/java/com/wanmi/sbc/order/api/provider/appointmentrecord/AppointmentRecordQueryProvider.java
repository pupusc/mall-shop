package com.wanmi.sbc.order.api.provider.appointmentrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordPageCriteriaRequest;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordQueryRequest;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordRequest;
import com.wanmi.sbc.order.api.response.appointmentrecord.AppointmentRecordListResponse;
import com.wanmi.sbc.order.api.response.appointmentrecord.AppointmentRecordPageCriteriaResponse;
import com.wanmi.sbc.order.api.response.appointmentrecord.AppointmentRecordResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>预约抢购记录查询Provider</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@FeignClient(value = "${application.order.name}", contextId = "AppointmentRecordQueryProvider")
public interface AppointmentRecordQueryProvider {

    /**
     * 判断用户是否已经预约
     *
     * @param request 预约参数结构 {@link AppointmentRecordRequest}
     * @return 预约信息 {@link }
     * @author zxd
     */
    @PostMapping("/order/${application.order.version}/appointmentrecord/get-appointment-info")
    BaseResponse<AppointmentRecordResponse> getAppointmentInfo(@RequestBody @Valid AppointmentRecordQueryRequest request);


    /**
     * 获取预约信息
     *
     * @param request 预约参数结构 {@link AppointmentRecordRequest}
     * @return 预约信息 {@link }
     * @author zxd
     */
    @PostMapping("/order/${application.order.version}/appointmentrecord/page")
    BaseResponse<AppointmentRecordPageCriteriaResponse> pageCriteria(@RequestBody @Valid AppointmentRecordPageCriteriaRequest request);


    /**
     * 获取订阅了未开始的活动
     * @return
     */
    @PostMapping("/order/${application.order.version}/appointmentrecord/list-subscribe-not-start-activity")
    BaseResponse<AppointmentRecordListResponse> listSubscribeNotStartActivity(@RequestBody @Valid AppointmentRecordPageCriteriaRequest request);
}

