package com.wanmi.sbc.goods.api.request.appointmentsale;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;


@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleByGoodsIdRequest extends BaseQueryRequest {


    private static final long serialVersionUID = -5483219515387655159L;
    @ApiModelProperty(value = "goodsId")
    @NotBlank
    private String goodsId;

}