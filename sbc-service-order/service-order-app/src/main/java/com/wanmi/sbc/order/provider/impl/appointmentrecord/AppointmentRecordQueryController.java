package com.wanmi.sbc.order.provider.impl.appointmentrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.provider.appointmentrecord.AppointmentRecordQueryProvider;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordPageCriteriaRequest;
import com.wanmi.sbc.order.api.request.appointmentrecord.AppointmentRecordQueryRequest;
import com.wanmi.sbc.order.api.response.appointmentrecord.AppointmentRecordListResponse;
import com.wanmi.sbc.order.api.response.appointmentrecord.AppointmentRecordPageCriteriaResponse;
import com.wanmi.sbc.order.api.response.appointmentrecord.AppointmentRecordResponse;
import com.wanmi.sbc.order.appointmentrecord.model.root.AppointmentRecord;
import com.wanmi.sbc.order.appointmentrecord.request.AppointmentQueryRequest;
import com.wanmi.sbc.order.appointmentrecord.service.AppointmentRecordService;
import com.wanmi.sbc.goods.bean.vo.AppointmentRecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>预约抢购记录Provider</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@RestController
@Validated
public class AppointmentRecordQueryController implements AppointmentRecordQueryProvider {

    @Autowired
    private AppointmentRecordService appointmentRecordService;


    @Override
    public BaseResponse<AppointmentRecordResponse> getAppointmentInfo(@Valid AppointmentRecordQueryRequest request) {
        AppointmentRecord appointmentRecord = appointmentRecordService.getAppointmentInfo(request);
        return BaseResponse.success(AppointmentRecordResponse.builder().appointmentRecordVO(
                KsBeanUtil.convert(appointmentRecord, AppointmentRecordVO.class)).build());
    }

    @Override
    public BaseResponse<AppointmentRecordPageCriteriaResponse> pageCriteria(@RequestBody @Valid AppointmentRecordPageCriteriaRequest request) {
        AppointmentQueryRequest appointmentQueryRequest = KsBeanUtil.convert(request.getAppointmentQueryDTO(), AppointmentQueryRequest.class);
        Criteria criteria = appointmentQueryRequest.getWhereCriteria();
        Page<AppointmentRecord> page = appointmentRecordService.page(criteria, appointmentQueryRequest);
        MicroServicePage<AppointmentRecordVO> appointmentRecordVOS = KsBeanUtil.convertPage(page, AppointmentRecordVO.class);
        return BaseResponse.success(AppointmentRecordPageCriteriaResponse.builder().appointmentRecordPage(appointmentRecordVOS).build());
    }

    @Override
    public BaseResponse<AppointmentRecordListResponse> listSubscribeNotStartActivity(@RequestBody @Valid AppointmentRecordPageCriteriaRequest request) {
        AppointmentQueryRequest appointmentQueryRequest = KsBeanUtil.convert(request.getAppointmentQueryDTO(), AppointmentQueryRequest.class);
        Criteria criteria = appointmentQueryRequest.getWhereCriteria();
        List<AppointmentRecord> appointmentRecordList = appointmentRecordService.listSubscribeNotStartActivityCriteria(criteria);
        return BaseResponse.success(AppointmentRecordListResponse.builder().appointmentRecordVOList
                (KsBeanUtil.convert(appointmentRecordList, AppointmentRecordVO.class)).build());
    }
}

