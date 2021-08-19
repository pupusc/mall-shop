package com.wanmi.sbc.goods.api.request.standard;

import com.wanmi.sbc.goods.bean.dto.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * <p>批量新增商品库请求类</p>
 * Date: 2018-12-20
 * @author dyt
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StandardGoodsBatchAddRequest implements Serializable {

    private static final long serialVersionUID = 6739797416667654644L;

    /**
     * 商品库批量信息
     */
    @ApiModelProperty(value = "商品库批量信息")
    @NotEmpty
    private List<BatchStandardGoodsDTO> goodsList;

    /**
     * 商品库SKU批量信息
     */
    @ApiModelProperty(value = "商品库SKU批量信息")
    @NotEmpty
    private List<BatchStandardSkuDTO> skuList;

    /**
     * 商品库规格批量信息
     */
    @ApiModelProperty(value = "商品库规格批量信息")
    private List<BatchStandardSpecDTO> specList;

    /**
     * 商品库规格值批量信息
     */
    @ApiModelProperty(value = "商品库规格值批量信息")
    private List<BatchStandardSpecDetailDTO> specDetailList;

    /**
     * 图片批量信息
     */
    @ApiModelProperty(value = "图片批量信息")
    private List<BatchStandardImageDTO> imageList;
}

