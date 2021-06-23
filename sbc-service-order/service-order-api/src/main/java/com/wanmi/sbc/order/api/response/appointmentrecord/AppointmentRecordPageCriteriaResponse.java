package com.wanmi.sbc.order.api.response.appointmentrecord;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.AppointmentRecordVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class AppointmentRecordPageCriteriaResponse implements Serializable {

    private static final long serialVersionUID = 8440625361456823268L;
    /**
     * 分页数据
     */
    @ApiModelProperty(value = "分页数据")
    private MicroServicePage<AppointmentRecordVO> appointmentRecordPage;

}
