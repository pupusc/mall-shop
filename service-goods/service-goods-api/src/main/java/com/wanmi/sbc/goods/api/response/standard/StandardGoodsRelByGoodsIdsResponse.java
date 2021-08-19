package com.wanmi.sbc.goods.api.response.standard;

import com.wanmi.sbc.goods.bean.vo.StandardGoodsRelVO;
import com.wanmi.sbc.goods.bean.vo.StandardSkuVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * author: dyt
 * Date: 2020-11-07
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardGoodsRelByGoodsIdsResponse implements Serializable {

    private static final long serialVersionUID = -3802536433354214574L;

    /**
     * 商品SPU关联数据
     */
    @ApiModelProperty(value = "商品SPU关联数据")
    private List<StandardGoodsRelVO> standardGoodsRelList = new ArrayList<>();
}
