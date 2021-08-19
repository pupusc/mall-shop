package com.wanmi.sbc.order.api.request.appointmentrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import com.wanmi.sbc.goods.bean.dto.AppointmentSaleGoodsInfoDTO;
import com.wanmi.sbc.goods.bean.dto.AppointmentSaleInfoDTO;
import com.wanmi.sbc.goods.bean.dto.SupplierDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@ApiModel
public class AppointmentRecordRequest implements Serializable {


    private static final long serialVersionUID = -3199567834192501453L;

    /**
     * id
     */
    private String id;


    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
    @NotBlank
    private String buyerId;


    /**
     * 活动id
     */
    @ApiModelProperty(value = "活动id")
    @NotNull
    private Long appointmentSaleId;

    /**
     * skuId
     */
    @ApiModelProperty(value = "skuId")
    @NotBlank
    private String goodsInfoId;

    /**
     * 商家信息
     */
    @ApiModelProperty(value = "商家信息")
    @NotNull
    private SupplierDTO supplier;

    /**
     * 活动信息
     */
    @ApiModelProperty(value = "活动信息")
    @NotNull
    private AppointmentSaleInfoDTO appointmentSaleInfo;

    /**
     * 活动商品信息
     */
    @NotNull
    private AppointmentSaleGoodsInfoDTO appointmentSaleGoodsInfo;


    /**
     * 预约创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime createTime;


}
