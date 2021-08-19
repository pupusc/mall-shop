package com.wanmi.sbc.order.api.request.appointmentrecord;

import com.wanmi.sbc.common.base.BaseQueryRequest;
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
public class AppointmentRecordPageRequest extends BaseQueryRequest {


    private static final long serialVersionUID = 8086401568045453018L;
    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    private String buyerId;


}
