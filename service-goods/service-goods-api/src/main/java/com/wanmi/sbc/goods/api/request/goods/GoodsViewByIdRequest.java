package com.wanmi.sbc.goods.api.request.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.*;

/**
 * com.wanmi.sbc.goods.api.request.goods.GoodsByIdRequest
 * 根据编号查询商品信息请求对象
 * @author lipeng
 * @dateTime 2018/11/5 上午9:40
 */
@ApiModel
@Data
public class GoodsViewByIdRequest implements Serializable {

    private static final long serialVersionUID = 5594325220431537194L;

    @ApiModelProperty(value = "商品Id")
    @NotBlank
    private String goodsId;

    @ApiModelProperty(value = "是否需要返回标签数据 true:需要，false或null:不需要")
    private Boolean showLabelFlag;

    @ApiModelProperty(value = "当showLabelFlag=true时，true:返回开启状态的标签，false或null:所有标签")
    private Boolean showSiteLabelFlag;

    /**
     * 商品打包信息
     */
    private Boolean showGoodsPackFlag = false;
}
