package com.wanmi.sbc.order.api.response.appointmentrecord;

import com.wanmi.sbc.goods.bean.vo.AppointmentRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName AppointmentRecordResponse
 * @Description 预约信息
 * @Author zhangxiaodong
 * @Date 2020/5/25 9:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRecordResponse implements Serializable {


    private static final long serialVersionUID = -3199567834192501453L;

    private AppointmentRecordVO appointmentRecordVO;

}
