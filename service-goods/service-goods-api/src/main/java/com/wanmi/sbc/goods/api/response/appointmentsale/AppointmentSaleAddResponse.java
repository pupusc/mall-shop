package com.wanmi.sbc.goods.api.response.appointmentsale;

import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>预约抢购新增结果</p>
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleAddResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 已新增的预约抢购信息
     */
    @ApiModelProperty(value = "已新增的预约抢购信息")
    private AppointmentSaleVO appointmentSaleVO;
}
