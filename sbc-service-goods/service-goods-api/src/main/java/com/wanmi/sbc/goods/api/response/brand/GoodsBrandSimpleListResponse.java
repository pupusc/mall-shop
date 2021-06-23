package com.wanmi.sbc.goods.api.response.brand;

import com.wanmi.sbc.goods.bean.vo.GoodsBrandSimpleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsBrandSimpleListResponse implements Serializable {

    private static final long serialVersionUID = -6942228033110682922L;

    /**
     * 品牌列表
     */
    @ApiModelProperty(value = "品牌列表")
    private List<GoodsBrandSimpleVO> goodsBrandVOList;
}
