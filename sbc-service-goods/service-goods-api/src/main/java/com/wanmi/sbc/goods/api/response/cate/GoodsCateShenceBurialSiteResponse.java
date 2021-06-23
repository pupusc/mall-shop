package com.wanmi.sbc.goods.api.response.cate;

import com.wanmi.sbc.goods.bean.vo.GoodsCateShenceBurialSiteVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p>获取一二三级分类列表</p>
 * author: weiwenhao
 * Date: 2020-06-02
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCateShenceBurialSiteResponse implements Serializable {


    private static final long serialVersionUID = 7092668084973344549L;

    /**
     * 商品类目
     */
    @ApiModelProperty(value = "商品类目")
    private List<GoodsCateShenceBurialSiteVO> goodsCateList;
}
