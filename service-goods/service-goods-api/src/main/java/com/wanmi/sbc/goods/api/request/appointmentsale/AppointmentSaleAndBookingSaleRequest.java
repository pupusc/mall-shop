package com.wanmi.sbc.goods.api.request.appointmentsale;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyVO;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedValidateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;


@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentSaleAndBookingSaleRequest extends BaseQueryRequest {


    private static final long serialVersionUID = -7552208113924726472L;

    @ApiModelProperty(value = "skuId")
    @NotEmpty
    private List<String> goodsInfoIdList;

}