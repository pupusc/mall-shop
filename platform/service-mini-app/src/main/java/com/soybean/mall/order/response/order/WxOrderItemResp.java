package com.soybean.mall.order.response.order;

import lombok.Data;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/6/7 5:30 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class WxOrderItemResp {

    /**
     *
     */
    private String spuId;

    private String skuId;

    /**
     * 标题
     */
    private String title;

    /**
     * 规格
     */
    private String specs;

    /**
     * 售价
     */
    private String salePrice;

    /**
     * 积分信息
     */
    private Integer points;

    /**
     * 数量
     */
    private Integer num;

    /**
     * 商品图片
     */
    private String pic;
}
