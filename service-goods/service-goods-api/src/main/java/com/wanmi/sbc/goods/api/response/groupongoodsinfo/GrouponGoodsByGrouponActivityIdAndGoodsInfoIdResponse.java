package com.wanmi.sbc.goods.api.response.groupongoodsinfo;

import com.wanmi.sbc.goods.bean.vo.GrouponGoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrouponGoodsByGrouponActivityIdAndGoodsInfoIdResponse implements Serializable {

    private static final long serialVersionUID = -4863055961805354652L;

    /**
     * 拼团商品信息
     */
    @ApiModelProperty(value = "拼团商品信息")
    private GrouponGoodsInfoVO grouponGoodsInfoVO;
}