package com.wanmi.sbc.goods.api.response.flashsale;

import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @program: sbc-micro-service
 * @description: 参与活动商品详情列表数据返回值
 * @create: 2019-06-12 11:14
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailListResp implements Serializable {
    private static final long serialVersionUID = -8453878414313275810L;

    @ApiModelProperty(value = "参与活动商品信息")
    private List<FlashSaleGoodsVO> flashSaleGoodsVOS;
}