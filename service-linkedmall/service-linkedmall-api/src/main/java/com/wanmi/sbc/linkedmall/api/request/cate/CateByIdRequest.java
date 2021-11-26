package com.wanmi.sbc.linkedmall.api.request.cate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class CateByIdRequest implements Serializable {
    private static final long serialVersionUID = -2990773690109718099L;
    @ApiModelProperty("类目id")
    private Long cateId;
}
