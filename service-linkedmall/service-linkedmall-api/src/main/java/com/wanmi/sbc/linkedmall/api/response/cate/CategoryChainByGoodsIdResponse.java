package com.wanmi.sbc.linkedmall.api.response.cate;

import com.aliyuncs.linkedmall.model.v20180116.GetCategoryChainResponse;
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
public class CategoryChainByGoodsIdResponse implements Serializable {
    private static final long serialVersionUID = 7304099547689273803L;
    @ApiModelProperty("类目链")
    private List<GetCategoryChainResponse.Category> categoryChain;
}
