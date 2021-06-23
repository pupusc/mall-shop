package com.wanmi.sbc.goods.api.request.appointmentsale;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleInProgressRequest extends BaseQueryRequest {


    private static final long serialVersionUID = -7552208113924726472L;

    @ApiModelProperty(value = "skuId")
    @NotEmpty
    private List<String> goodsInfoIdList;

}