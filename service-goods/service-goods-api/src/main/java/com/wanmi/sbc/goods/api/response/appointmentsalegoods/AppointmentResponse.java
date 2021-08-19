package com.wanmi.sbc.goods.api.response.appointmentsalegoods;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.AppointmentVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>根据id查询任意（包含已删除）预约抢购信息详情response</p>
 *
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 预约抢购商品信息
     */
    @ApiModelProperty(value = "预约抢购商品信息")
    private MicroServicePage<AppointmentVO> appointmentVOPage;
}
