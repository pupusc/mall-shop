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
public class AppointmentSaleGoodsDetailResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 预约抢购信息
     */
    @ApiModelProperty(value = "预约抢购信息")
    private List<AppointmentSaleGoodsVO> appointmentSaleGoodsVOList;
}
