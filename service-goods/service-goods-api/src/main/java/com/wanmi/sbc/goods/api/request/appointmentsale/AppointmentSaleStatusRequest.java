package com.wanmi.sbc.goods.api.request.appointmentsale;

import com.wanmi.sbc.goods.api.request.GoodsBaseRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * <p>活动开始暂停</p>
 *
 * @author zxd
 * @date 2020-05-21 10:32:23
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleStatusRequest extends GoodsBaseRequest {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @NotNull
    private Long id;


    @ApiModelProperty(value = "是否暂停 0:否 1:是")
    @NotNull
    private Integer pauseFlag;

    @ApiModelProperty(value = "商铺id")
    private Long storeId;

}
