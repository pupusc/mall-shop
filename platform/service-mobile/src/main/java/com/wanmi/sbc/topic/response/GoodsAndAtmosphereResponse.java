package com.wanmi.sbc.topic.response;

import com.wanmi.sbc.booklistmodel.response.GoodsCustomResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class GoodsAndAtmosphereResponse extends GoodsCustomResponse implements Serializable {
    private static final long serialVersionUID = -2138644833913932141L;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("元素1,左侧第一行文字")
    private String elementOne;

    @ApiModelProperty("元素2：左侧第二行文字")
    private String elementTwo;

    @ApiModelProperty("元素3：右侧第一行文字")
    private String elementThree;

    @ApiModelProperty("元素4：右侧第二行文字")
    private String elementFour;
}
