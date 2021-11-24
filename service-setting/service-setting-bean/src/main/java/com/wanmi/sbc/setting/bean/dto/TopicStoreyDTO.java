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

    @ApiModelProperty("楼层类型1：一行一个图片+商品，2：一行两个图片+商品3：一行两个商品4：一行三个图片 5：瀑布流 6 轮播")
    private Integer storeyType;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("排序")
    private Integer sorting;

    @ApiModelProperty("背景色")
    private String background;

    @ApiModelProperty("是否有间距0无间距1有间距")
    private Integer hasPadding;
    
    @ApiModelProperty("楼层内容")
    private List<TopicStoreyContentDTO> contents;

}
