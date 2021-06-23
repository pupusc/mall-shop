package com.wanmi.sbc.linkedmall.api.response.cate;

import com.wanmi.sbc.linkedmall.bean.vo.LinkedMallGoodsCateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor

@ApiModel
public class LinkedMallCateGetResponse implements Serializable {
    @ApiModelProperty("linkedmall分类集合")
    private List<LinkedMallGoodsCateVO> linkedMallGoodsCateVOS;
}
