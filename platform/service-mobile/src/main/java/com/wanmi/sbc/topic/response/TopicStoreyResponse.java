package com.wanmi.sbc.topic.response;

import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.home.response.NoticeResponse;
import com.wanmi.sbc.setting.api.response.mixedcomponentV2.TopicStoreyMixedComponentResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class TopicStoreyResponse implements Serializable {
    private static final long serialVersionUID = -5138062021719883495L;

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("楼层名称")
    private String name;

    @ApiModelProperty("导航名称")
    private String navigationName;

    @ApiModelProperty("楼层类型1：一行一个图片+商品，2：一行两个图片+商品3：一行两个商品4：一行三个图片 5：瀑布流 6 轮播 7异形轮播 8 导航 9优惠券")
    private Integer storeyType;

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

    @ApiModelProperty("滚动消息内容")
    private NoticeResponse notes;

    @ApiModelProperty("用户积分信息")
    private TopicCustomerPointsResponse points;
    
    @ApiModelProperty("楼层内容")
    private List<TopicStoreyContentReponse> contents;

    @ApiModelProperty("背景图片")
    private String backgroundImageUrl;

    @ApiModelProperty("链接")
    private String linkUrl;

    @ApiModelProperty("颜色，导航类型对应导航文字颜色")
    private String color;

    @ApiModelProperty("新书速递栏目信息")
    private List<NewBookPointResponse> newBookPointResponseList;

    @ApiModelProperty("榜单相关")
    private List<RankResponse> rankList;

    @ApiModelProperty("三本好书栏目信息")
    private List<ThreeGoodBookResponse> threeGoodBookResponses;

    @ApiModelProperty("商品栏目信息")
    private List<GoodsOrBookResponse> GoodsResponses;

    @ApiModelProperty("图书栏目信息")
    private List<GoodsOrBookResponse> BooksResponses;

    @ApiModelProperty("混合栏目信息")
    private TopicStoreyMixedComponentResponse topicStoreyMixedComponentResponse;
}
