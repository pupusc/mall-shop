package com.wanmi.sbc.goods.api.request.prop;


import com.wanmi.sbc.goods.bean.dto.GoodsPropRequestDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsPropAddRequest extends GoodsPropRequestDTO implements Serializable{

   private static final long serialVersionUID = 3823325317214738348L;
}
