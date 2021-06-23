package com.wanmi.sbc.goods.api.request.prop;


import com.wanmi.sbc.goods.bean.dto.GoodsPropDetailDTO;
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
public class GoodsPropQueryPropDetailsOverStepRequest implements Serializable{

   private static final long serialVersionUID = 6819162905846629082L;

   @ApiModelProperty(value = "商品属性详情")
   @NotNull
   private List<GoodsPropDetailDTO> goodsPropDetails;
}
