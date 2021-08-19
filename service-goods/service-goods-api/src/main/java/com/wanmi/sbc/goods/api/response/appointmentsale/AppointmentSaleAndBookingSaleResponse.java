package com.wanmi.sbc.goods.api.response.appointmentsale;

import com.wanmi.sbc.goods.bean.vo.AppointmentSaleVO;
import com.wanmi.sbc.goods.bean.vo.BookingSaleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @discription 合并预约预售返回
 * @author yangzhen
 * @date 2020/9/10 16:31
 * @param
 * @return
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleAndBookingSaleResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 预约抢购信息
     */
    @ApiModelProperty(value = "预约抢购信息列表")
    private List<AppointmentSaleVO> appointmentSaleVOList;


    /**
     * 预售抢购列表
     */
    @ApiModelProperty(value = "预售抢购列表")
    private  List<BookingSaleVO> bookingSaleVOList;


}
