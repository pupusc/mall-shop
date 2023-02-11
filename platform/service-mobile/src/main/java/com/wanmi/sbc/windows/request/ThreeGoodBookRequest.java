package com.wanmi.sbc.windows.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ThreeGoodBookRequest {


    private Integer id;

    private Integer pageNum = 0;

    private Integer pageSize = 10;

}
