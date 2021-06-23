package com.wanmi.sbc.order.api.request.appointmentrecord;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.order.bean.dto.AppointmentQueryDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class AppointmentRecordPageCriteriaRequest extends BaseQueryRequest {


    private static final long serialVersionUID = 3654578476737468221L;

    @ApiModelProperty(value = "搜索信息")
    private AppointmentQueryDTO appointmentQueryDTO;


}
