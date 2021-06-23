package com.wanmi.sbc.goods.api.request.prop;


import com.wanmi.sbc.goods.bean.dto.GoodsPropDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsPropModifyRequest extends GoodsPropDTO implements Serializable{

   private static final long serialVersionUID = 8709366381114453307L;
}
