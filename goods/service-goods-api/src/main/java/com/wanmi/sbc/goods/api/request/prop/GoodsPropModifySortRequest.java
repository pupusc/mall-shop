package com.wanmi.sbc.goods.api.request.prop;


import com.wanmi.sbc.goods.bean.dto.GoodsPropDTO;
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
public class GoodsPropModifySortRequest implements Serializable{

   private static final long serialVersionUID = -6196967584289017418L;

   @ApiModelProperty(value = "商品属性")
   @NotNull
   private List<GoodsPropDTO> goodsProps;
}
