package com.wanmi.sbc.order.api.request.appointmentrecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName RushToAppointmentSaleGoodsRequest
 * @Description 预约记录
 * @Author zhangxiaodong
 * @Date 2020/05/25 9:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRecordQueryRequest implements Serializable {


    private static final long serialVersionUID = 6652059821827671328L;
    /**
     * id
     */
    private String id;


    /**
     * 会员id
     */
    @NotBlank
    private String buyerId;

    /**
     * 活动id
     */
    @NotNull
    private Long appointmentSaleId;

    /**
     * skuId
     */
    @NotBlank
    private String goodsInfoId;



}
