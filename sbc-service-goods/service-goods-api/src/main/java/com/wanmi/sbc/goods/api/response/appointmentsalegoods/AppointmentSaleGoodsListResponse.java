package com.wanmi.sbc.goods.api.response.appointmentsalegoods;

import com.wanmi.sbc.goods.bean.vo.AppointmentSaleGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>预约抢购列表结果</p>
 * @author zxd
 * @date 2020-05-21 13:47:11
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleGoodsListResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 预约抢购列表结果
     */
    @ApiModelProperty(value = "预约抢购列表结果")
    private List<AppointmentSaleGoodsVO> appointmentSaleGoodsVOList;
}
