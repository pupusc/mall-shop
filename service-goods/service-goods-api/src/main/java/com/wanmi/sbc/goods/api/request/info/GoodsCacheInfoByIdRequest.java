package com.wanmi.sbc.goods.api.request.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 根据商品SKU编号查询请求
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsCacheInfoByIdRequest implements Serializable {

    private static final long serialVersionUID = 8415135684020619843L;

    /**
     * SKU编号
     */
    @ApiModelProperty(value = "SKU编号")
    private String goodsInfoId;

    @ApiModelProperty(value = "商品id")
    private String goodsId;

    @ApiModelProperty(value = "店铺Id")
    private Long storeId;


    @ApiModelProperty(value = "customerId")
    private String customerId;

    @ApiModelProperty(value = "是否需要返回标签数据 true:需要，false或null:不需要")
    private Boolean showLabelFlag;

    @ApiModelProperty(value = "当showLabelFlag=true时，true:返回开启状态的标签，false或null:所有标签")
    private Boolean showSiteLabelFlag;

}
