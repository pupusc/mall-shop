package com.wanmi.sbc.goods.api.response.brand;

import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 品牌查询响应
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsBrandByIdsResponse implements Serializable {

    private static final long serialVersionUID = -6942228033110682924L;

    /**
     * 品牌列表
     */
    @ApiModelProperty(value = "品牌列表")
    private List<GoodsBrandVO> goodsBrandVOList;
}
