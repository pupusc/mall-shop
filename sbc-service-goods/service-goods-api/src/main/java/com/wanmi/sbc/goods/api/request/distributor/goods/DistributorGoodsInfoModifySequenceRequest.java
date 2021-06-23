package com.wanmi.sbc.goods.api.request.distributor.goods;

import com.wanmi.sbc.goods.bean.dto.DistributorGoodsInfoModifySequenceDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 分销员商品-修改分销商品顺序对象
 * @author: Geek Wang
 * @createDate: 2019/2/28 14:22
 * @version: 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistributorGoodsInfoModifySequenceRequest implements Serializable {

    /**
     * 分销员商品对象集合
     */
    @ApiModelProperty(value = "分销员商品对象集合")
    @NotNull
    private List<DistributorGoodsInfoModifySequenceDTO> distributorGoodsInfoDTOList;
}
