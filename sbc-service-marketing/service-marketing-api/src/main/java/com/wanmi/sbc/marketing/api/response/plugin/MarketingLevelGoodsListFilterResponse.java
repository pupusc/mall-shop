package com.wanmi.sbc.marketing.api.response.plugin;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-22
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketingLevelGoodsListFilterResponse implements Serializable {

    private static final long serialVersionUID = 2205540867848460192L;

    @ApiModelProperty(value = "单品信息列表")
    private List<GoodsInfoVO> goodsInfoVOList;
}
