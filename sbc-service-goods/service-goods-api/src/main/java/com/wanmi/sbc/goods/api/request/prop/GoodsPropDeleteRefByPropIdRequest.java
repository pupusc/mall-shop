package com.wanmi.sbc.goods.api.request.prop;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsPropDeleteRefByPropIdRequest implements Serializable{

   private static final long serialVersionUID = 8787724809947148768L;

   @ApiModelProperty(value = "属性Id")
   @NotNull
   private Long propId;
}
