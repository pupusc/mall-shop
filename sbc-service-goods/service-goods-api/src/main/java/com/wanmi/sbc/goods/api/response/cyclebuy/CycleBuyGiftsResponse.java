package com.wanmi.sbc.goods.api.response.cyclebuy;

import com.wanmi.sbc.goods.bean.vo.CycleBuyGiftVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CycleBuyGiftsResponse {

    /**
     * 赠品列表
     */
    @ApiModelProperty(value = "赠品列表")
    private List<GoodsInfoVO> giftList;

    /**
     * 活动规则列表
     */
    @ApiModelProperty(value = "活动规则列表")
    private List<CycleBuyGiftVO> cycleBuyGiftVOList;


}
