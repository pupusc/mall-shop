package com.wanmi.sbc.goods.api.request.common;

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
 * 批量导入商品信息请求对象
 * @author daiyitian
 * @dateTime 2018/11/2 上午9:54
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCommonBatchAddRequest implements Serializable {

    private static final long serialVersionUID = 2373497523561481281L;

    /**
     * 商品批量信息
     */
    @ApiModelProperty(value = "商品批量信息")
    @NotEmpty
    private List<BatchGoodsDTO> goodsList;

    /**
     * 商品SKU批量信息
     */
    @ApiModelProperty(value = "商品SKU批量信息")
    @NotEmpty
    private List<BatchGoodsInfoDTO> goodsInfoList;

    /**
     * 商品规格批量信息
     */
    @ApiModelProperty(value = "商品规格批量信息")
    private List<BatchGoodsSpecDTO> specList;

    /**
     * 商品规格值批量信息
     */
    @ApiModelProperty(value = "商品规格值批量信息")
    private List<BatchGoodsSpecDetailDTO> specDetailList;

    /**
     * 图片批量信息
     */
    @ApiModelProperty(value = "图片批量信息")
    private List<BatchGoodsImageDTO> imageList;
}
