package com.wanmi.sbc.goods.api.request.cyclebuy;

import com.wanmi.sbc.goods.bean.dto.CycleBuyDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsSpecDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsSpecDetailDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CycleBuyModifyRequest extends CycleBuyDTO implements Serializable {

    private static final long serialVersionUID = 7903154763141904043L;
    /**
     * 周期购Id
     */
    @ApiModelProperty(value = "周期购Id")
    @NotNull
    private Long id;

    /**
     * 商品规格列表
     */
    @ApiModelProperty(value = "商品规格列表")
    private List<GoodsSpecDTO> goodsSpecs;

    /**
     * 商品规格值列表
     */
    @ApiModelProperty(value = "商品规格值列表")
    private List<GoodsSpecDetailDTO> goodsSpecDetails;

    /**
     * 周期购商品SKU列表
     */
    @ApiModelProperty(value = "周期购商品SKU列表")
    private List<GoodsInfoDTO> goodsInfoDTOS;


    /**
     * erp商品编码
     */
    @ApiModelProperty(value = "erp商品编码")
    private String erpGoodsNo;


}
