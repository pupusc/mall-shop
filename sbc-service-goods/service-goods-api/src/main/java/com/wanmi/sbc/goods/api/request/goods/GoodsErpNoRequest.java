package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsErpNoRequest implements Serializable {

    private static final long serialVersionUID = 3653093007419951040L;

    @ApiModelProperty(value = "erpSpu编码")
    @NotBlank
    private String erpGoodsNo;

    @ApiModelProperty(value = "erpSku编码")
    @NotEmpty
    private List<String> erpGoodsInfoNos;
}
