package com.wanmi.sbc.goods.api.request.prop;


import com.wanmi.sbc.goods.bean.dto.GoodsPropDetailDTO;
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
public class GoodsPropDeleteRefByPropDetailRequest implements Serializable{

   private static final long serialVersionUID = -1293273913075281095L;

   @ApiModelProperty(value = "商品属性详情")
   @NotNull
   private GoodsPropDetailDTO goodsPropDetail;
}
