package com.wanmi.sbc.goods.api.request.prop;


import com.wanmi.sbc.goods.bean.dto.GoodsPropRequestDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

@ApiModel
@Data
public class GoodsPropListInitSortRequest extends GoodsPropRequestDTO implements Serializable{

    private static final long serialVersionUID = 7507962208183111098L;

}
