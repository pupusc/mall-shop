package com.wanmi.sbc.goods.bean.vo;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>预约抢购VO</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentVO implements Serializable {
    private static final long serialVersionUID = 1L;

   private AppointmentSaleVO appointmentSale;

   private AppointmentSaleGoodsVO appointmentSaleGoods;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
   private LocalDateTime serverTime;

}