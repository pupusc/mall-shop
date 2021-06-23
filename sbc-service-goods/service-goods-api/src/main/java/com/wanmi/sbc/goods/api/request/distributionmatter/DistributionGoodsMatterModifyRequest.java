package com.wanmi.sbc.goods.api.request.distributionmatter;

import com.wanmi.sbc.goods.bean.enums.MatterType;
import com.wanmi.sbc.goods.bean.enums.MediaType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class DistributionGoodsMatterModifyRequest {

    @ApiModelProperty(value = "id")
    @NotBlank
    private String id;


    @ApiModelProperty(value = "商品sku的id")
    private String goodsInfoId;

    @ApiModelProperty(value = "素材类型")
    @NotNull
    private MatterType matterType;

    /**
     * 素材
     */
    @ApiModelProperty(value = "素材")
    @NotBlank
    private String matter;

    /**
     * 推荐语
     */
    @ApiModelProperty(value = "推荐语")
    @NotBlank
    private String recommend;

    /**
     * 发布者id
     */
    @ApiModelProperty(value = "发布者id")
    private String operatorId;
}
