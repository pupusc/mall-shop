package com.wanmi.sbc.booklistmodel.response;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Description: 商品列表简化对象
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2021/9/10 3:56 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class GoodsCustomResponse {

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

}
