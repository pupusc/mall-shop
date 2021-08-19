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
 * @author: wanggang
 * @createDate: 2018/11/1 11:27
 * @version: 1.0
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreCateListGoodsRelByStoreCateIdAndIsHaveSelfResponse implements Serializable {
    private static final long serialVersionUID = -4423959090774434266L;

    @ApiModelProperty(value = "商品-店铺分类关联")
    private List<StoreCateGoodsRelaVO> storeCateGoodsRelaVOList;
}
