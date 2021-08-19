package com.wanmi.sbc.linkedmall.api.request.goods;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class LinkedMallGoodsPageRequest  {
    /**
     * 第几页
     */
    @ApiModelProperty(value = "第几页")
    @Min(1)
    private Integer pageNum ;

    /**
     * 每页显示多少条
     */
    @ApiModelProperty(value = "每页显示多少条")
    private Integer pageSize = 10;

}
