package com.wanmi.sbc.booklistmodel.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Description: 商品列表简化对象
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/10 3:56 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsCustomResponse implements Serializable{

    private static final long serialVersionUID = 7583391826867054305L;
    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 商品 spu
     */
    private String goodsNo;

    /**
     * goodsInfoId
     */
    private String goodsInfoId;

    /**
     * goodsInfoNo
     */
    private String goodsInfoNo;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品副标题
     */
    private String goodsSubName;

    /**
     * 封面图
     */
    private String goodsCoverImg;

    /**
     * 无背景图
     */
    private String goodsUnBackImg;

    /**
     * 知识顾问专享 0：不是，1：是
     */
    private Integer cpsSpecial = 0;

    /**
     * 展示价格[整理显示规则]
     */
    private BigDecimal showPrice;

    /**
     * 划线价格
     */
    private BigDecimal linePrice;

    /**
     * 市场价
     */
    private BigDecimal marketingPrice;

    /**
     * 是否有优惠券
     */
    private List<String> couponLabelList;

    /**
     * 商品标签
     */
    private List<String> goodsLabelList;

    /**
     * 商品评分
     */
    private String goodsScore;


    /**
     * 扩展信息
     */
    private GoodsExtPropertiesCustomResponse goodsExtProperties;

    @ApiModelProperty("图片地址")
    private String imageUrl;

    @ApiModelProperty("氛围类型1通用2积分")
    private Integer atmosType;

    @ApiModelProperty("元素1,左侧第一行文字")
    private String elementOne;

    @ApiModelProperty("元素2：左侧第二行文字")
    private String elementTwo;

    @ApiModelProperty("元素3：右侧第一行文字")
    private String elementThree;

    @ApiModelProperty("元素4：右侧第二行文字")
    private String elementFour;

    @ApiModelProperty("商品库存")
    private Long stock;

    /**
     * 樊登读书、非凡精度、樊登直播、李雷慢读
     */
    private String anchorPush;

    /**
     * 活动信息
     */
    private List<NormalActivity> activities;

    /**
     * 活动信息
     */
    @Data
    public static class NormalActivity {
        /**
         * 份数
         */
        private Integer num;

        /**
         * 活动展示
         */
        private String activityShow;

    }

}
