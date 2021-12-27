package com.wanmi.sbc.setting.api.request.topicconfig;

import com.wanmi.sbc.setting.bean.dto.TopicStoreyDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
public class TopicStoreyAddRequest implements Serializable {
    private static final long serialVersionUID = -3354087848195632606L;

    @NotNull
    @ApiModelProperty("专题Id")
    private Integer topicId;

    @ApiModelProperty("楼层名称")
    private String name;

    @ApiModelProperty("导航名称")
    private String navigationName;

    @ApiModelProperty("楼层类型1：一行一个图片+商品，2：一行两个图片+商品3：一行两个商品4：一行三个图片 5：瀑布流 6 轮播 7  异形轮播")
    private String storeyType;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("排序")
    private Integer sorting;

    @ApiModelProperty("背景色")
    private String background;

    @ApiModelProperty("是否有间距0无间距1有间距")
    private Integer hasPadding;

    @ApiModelProperty("瀑布流类型：100=首页瀑布流、200=好书5折瀑布流、300=童书会场瀑布流")
    private Integer waterFallType;

    @ApiModelProperty("背景图片地址")
    private String backgroundImageUrl;


}
