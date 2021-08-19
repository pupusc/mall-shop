package com.wanmi.sbc.order.bean.dto.yzorder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 收货地址信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressInfoDTO implements Serializable {

    private static final long serialVersionUID = -4644429435535071327L;

    /**
     * 同城送预计送达时间-结束时间 非同城送以及没有开启定时达的订单不返回
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delivery_end_time;

    /**
     * 同城送预计送达时间-开始时间 非同城送以及没有开启定时达的订单不返回
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime delivery_start_time;

    /**
     *省
     */
    private String delivery_province;

    /**
     * 市
     */
    private String delivery_city;

    /**
     * 区
     */
    private String delivery_district;

    /**
     * 邮政编码
     */
    private String delivery_postal_code;

    /**
     * 收货人手机号
     */
    private String receiver_tel;

    /**
     * 地址扩展信息，字段为json格式
     */
    private String address_extra;

    /**
     * 到店自提信息 json格式
     */
    private String self_fetch_info;

    /**
     * 详细地址
     */
    private String delivery_address;

    /**
     *收货人姓名
     */
    private String receiver_name;

}
