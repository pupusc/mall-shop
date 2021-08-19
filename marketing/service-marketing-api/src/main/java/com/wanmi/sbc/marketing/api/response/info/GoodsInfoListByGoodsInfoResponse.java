package com.wanmi.sbc.marketing.api.response.info;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-22
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfoListByGoodsInfoResponse implements Serializable {

    private static final long serialVersionUID = 2205540867848460192L;

    @ApiModelProperty(value = "单品信息列表")
    private List<GoodsInfoVO> goodsInfoVOList = new ArrayList<>();
}
