package com.wanmi.sbc.goods.api.response.cate;

import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>查询店铺已签约的类目列表相应类</p>
 * author: sunkun
 * Date: 2018-11-05
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractCateListCateByStoreIdResponse implements Serializable {

    private static final long serialVersionUID = -6233232067574897602L;

    /**
     * 商品类目
     */
    @ApiModelProperty(value = "商品类目")
    private List<GoodsCateVO> goodsCateList;
}
