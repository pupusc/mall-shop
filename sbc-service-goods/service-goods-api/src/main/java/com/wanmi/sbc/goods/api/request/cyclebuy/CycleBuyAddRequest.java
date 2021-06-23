package com.wanmi.sbc.goods.api.request.cyclebuy;

import com.wanmi.sbc.goods.bean.dto.*;
import com.wanmi.sbc.goods.bean.vo.GoodsTabRelaVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;


/**
 * <p>周期购活动新增请求参数</p>
 * @author weiwenhao
 * @date 2021-01-21 20:01:50
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CycleBuyAddRequest extends CycleBuyDTO implements Serializable {


    private static final long serialVersionUID = 8063962653950464832L;
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
     * 商品详情模板关联
     */
    @ApiModelProperty(value = "商品详情模板关联")
    private List<GoodsTabRelaVO> goodsTabRelas;


    /**
     * SPU编码
     */
    @ApiModelProperty(value = "SPU编码")
    @NotBlank
    private String goodsNo;


    /**
     * erp商品编码
     */
    @ApiModelProperty(value = "erp商品编码")
    private String erpGoodsNo;


}
