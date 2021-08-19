package com.wanmi.sbc.order.appointmentrecord.model.root;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName RushToAppointmentSaleGoodsRequest
 * @Description 预约记录
 * @Author zhangxiaodong
 * @Date 2019/6/14 9:38
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRecord implements Serializable {


    private static final long serialVersionUID = -3199567834192501453L;

    /**
     * id
     */
    @Id
    private String id;


    /**
     * 会员id
     */
    private String buyerId;

    /**
     * 活动id
     */
    private Long appointmentSaleId;

    /**
     * skuId
     */
    private String goodsInfoId;

    /**
     * 商家信息
     */
    private Supplier supplier;

    private Customer customer;

    /**
     * 活动信息
     */
    private AppointmentSaleInfo appointmentSaleInfo;

    /**
     * 活动商品信息
     */
    private AppointmentSaleGoodsInfo appointmentSaleGoodsInfo;


    /**
     * 预约创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

}
