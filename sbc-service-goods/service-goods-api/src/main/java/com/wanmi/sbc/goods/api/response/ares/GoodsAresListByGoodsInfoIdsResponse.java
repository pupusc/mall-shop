package com.wanmi.sbc.goods.api.response.ares;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author: wanggang
 * @createDate: 2018/11/5 10:52
 * @version: 1.0
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsAresListByGoodsInfoIdsResponse implements Serializable {

    private static final long serialVersionUID = 2218281245780239156L;

    @ApiModelProperty(value = "商品SKU")
    private List<GoodsInfoVO> goodsInfoList;
}
