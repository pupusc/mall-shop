package com.wanmi.sbc.order.api.request.trade;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * 周期购订单顺延/取消顺延入参
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CycleBuyPostponementRequest implements Serializable {


    private static final long serialVersionUID = 8344167034944489955L;

    /**
     * 订单id
     */
    @ApiModelProperty(value = "订单id")
    @NotNull
    private String tid;


    /**
     * 顺延日期
     */
    @ApiModelProperty(value = "顺延日期")
    @NotNull
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
    private LocalDateTime localDate;


    /**
     * 顺延标识：ture：顺延，false：取消顺延
     */
    @ApiModelProperty(value = "顺延标识：ture：顺延，false：取消顺延")
    @NotNull
    private Boolean isPostponement;


}
