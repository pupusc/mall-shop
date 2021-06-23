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
public class GoodsPropQueryIsChildNodeRequest implements Serializable{

   private static final long serialVersionUID = 712274830035383617L;

   @ApiModelProperty(value = "分类Id")
   @NotNull
   private Long cateId;
}
