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
public class GoodsPropDeleteByPropIdRequest implements Serializable{

   private static final long serialVersionUID = -2971933125097365489L;

   @ApiModelProperty(value = "属性Id")
   @NotNull
   private Long propId;
}
