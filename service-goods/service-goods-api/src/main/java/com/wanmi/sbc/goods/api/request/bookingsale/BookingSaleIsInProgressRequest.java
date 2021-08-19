package com.wanmi.sbc.goods.api.request.bookingsale;

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
public class BookingSaleIsInProgressRequest extends BaseQueryRequest {


    private static final long serialVersionUID = -3671027532438062137L;
    @ApiModelProperty(value = "skuId")
    @NotBlank
    private String goodsInfoId;

}