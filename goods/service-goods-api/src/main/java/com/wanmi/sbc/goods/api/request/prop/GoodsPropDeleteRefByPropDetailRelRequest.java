package com.wanmi.sbc.goods.api.request.prop;


import com.wanmi.sbc.goods.bean.dto.GoodsPropDetailRelDTO;
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
public class GoodsPropDeleteRefByPropDetailRelRequest implements Serializable{

   private static final long serialVersionUID = 3831426506018723571L;

   @ApiModelProperty(value = "商品属性详情")
   @NotNull
   private List<GoodsPropDetailRelDTO> goodsPropDetailRels;
}
