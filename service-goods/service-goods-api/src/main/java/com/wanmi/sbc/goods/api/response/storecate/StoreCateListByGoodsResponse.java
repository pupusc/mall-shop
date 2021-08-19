package com.wanmi.sbc.goods.api.response.storecate;

import com.wanmi.sbc.goods.bean.vo.StoreCateGoodsRelaVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Author: bail
 * Time: 2017/11/13.10:25
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCateListByGoodsResponse implements Serializable {

    private static final long serialVersionUID = 7318610881404150632L;

    @ApiModelProperty(value = "商品-店铺分类关联")
    private List<StoreCateGoodsRelaVO> storeCateGoodsRelaVOList;
}
