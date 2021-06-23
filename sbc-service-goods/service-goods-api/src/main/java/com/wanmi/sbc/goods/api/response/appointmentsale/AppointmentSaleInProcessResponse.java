package com.wanmi.sbc.goods.api.response.appointmentsale;

import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>正在进行中的预约抢购信息response</p>
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleInProcessResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 预约抢购信息
     */
    @ApiModelProperty(value = "预约抢购信息列表")
    private List<AppointmentSaleVO> appointmentSaleVOList;
}
