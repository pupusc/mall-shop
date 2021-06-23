package com.wanmi.sbc.goods.api.response.appointmentsalegoods;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.AppointmentSaleGoodsVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>预约抢购分页结果</p>
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleGoodsPageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 预约抢购分页结果
     */
    @ApiModelProperty(value = "预约抢购分页结果")
    private MicroServicePage<AppointmentSaleGoodsVO> appointmentSaleGoodsVOPage;
}
