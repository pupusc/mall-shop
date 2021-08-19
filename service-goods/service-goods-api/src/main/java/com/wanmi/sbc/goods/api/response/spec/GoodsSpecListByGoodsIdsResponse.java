package com.wanmi.sbc.goods.api.response.spec;

import com.wanmi.sbc.goods.bean.vo.GoodsSpecVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据GoodsIds查询规格列表响应结构
 * @author daiyitian
 * @dateTime 2018/11/13 14:59
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsSpecListByGoodsIdsResponse implements Serializable {

    private static final long serialVersionUID = -8085616862425886066L;

    @ApiModelProperty(value = "商品规格")
    private List<GoodsSpecVO> goodsSpecVOList;
}
