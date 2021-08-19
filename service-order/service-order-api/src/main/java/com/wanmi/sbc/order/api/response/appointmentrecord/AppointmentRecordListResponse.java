package com.wanmi.sbc.order.api.response.appointmentrecord;

import com.wanmi.sbc.goods.bean.vo.AppointmentRecordVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName AppointmentRecordListResponse
 * @Description 预约信息
 * @Author zhangxiaodong
 * @Date 2020/5/25 9:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRecordListResponse implements Serializable {


    private static final long serialVersionUID = -7375265369219638627L;
    private List<AppointmentRecordVO> appointmentRecordVOList;

}
