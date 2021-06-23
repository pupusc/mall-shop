package com.wanmi.sbc.goods.api.request.prop;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsPropAddDefaultRefRequest implements Serializable{

   private static final long serialVersionUID = 9017663098063542040L;

   @ApiModelProperty(value = "商品Id")
   @NotNull
   private List<String> goodsIds;

   @ApiModelProperty(value = "属性Id")
   @NotNull
   private Long propId;
}
