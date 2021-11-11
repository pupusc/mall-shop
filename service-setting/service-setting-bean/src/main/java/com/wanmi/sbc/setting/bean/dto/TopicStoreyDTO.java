package com.wanmi.sbc.setting.bean.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel
public class TopicStoreyDTO implements Serializable {
    private static final long serialVersionUID = 4625188265391998076L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("楼层名称")
    private String name;

    @ApiModelProperty("导航名称")
    private String navigationName;

    @ApiModelProperty("楼层类型1：一层一个2：一层两个，3：一层3个")
    private String storeyType;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("排序")
    private Integer sorting;

    @ApiModelProperty("背景色")
    private String background;

    @ApiModelProperty("商品信息")
    private List<TopicStoreyGoodsDTO> goods;
}
