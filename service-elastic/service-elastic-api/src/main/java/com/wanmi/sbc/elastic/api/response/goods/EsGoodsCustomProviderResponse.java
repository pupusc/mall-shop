package com.wanmi.sbc.elastic.api.response.goods;

import lombok.Data;

import java.io.Serializable;
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
public class EsGoodsCustomProviderResponse implements Serializable {

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
    private String goodsInfo;

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
     * 展示价格[整理显示规则]
     */
    private String showPrice;

    /**
     * 划线价格
     */
    private String linePrice;

    /**
     * 是否有优惠券
     */
    private List<String> couponLabel;

    /**
     * 商品标签
     */
    private String goodsLabel;

    /**
     * 商品分数
     */
    private String goodsScore;

    /**
     * 属性信息
     */
    private List<Map<String, String>> properties;

}
