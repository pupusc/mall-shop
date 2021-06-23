package com.wanmi.sbc.goods.api.response.standard;

import com.wanmi.sbc.goods.bean.vo.StandardSkuVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-07
 */
@ApiModel
@Data
public class StandardPartColsListByGoodsIdsResponse implements Serializable {

    private static final long serialVersionUID = -3802536433354214574L;

    /**
     * 商品SKU全部数据
     */
    @ApiModelProperty(value = "商品SKU全部数据")
    private List<StandardSkuVO> standardSkuList = new ArrayList<>();
}
