package com.wanmi.sbc.goods.bean.wx.vo.assistant;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/23 2:58 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/

@Data
public class WxLiveAssistantGoodsConfigVo {

    /**
     * 商品id
     */
    private String goodsId;

    /**
     * 微信对应的价格
     */
    private BigDecimal skuMiniMarketPrice;

    /**
     * 微信对应的库存
     */
    private Long sumStock;
}
