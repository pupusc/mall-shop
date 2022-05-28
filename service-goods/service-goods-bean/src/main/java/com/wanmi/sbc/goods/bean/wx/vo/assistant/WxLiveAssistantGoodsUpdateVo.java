package com.wanmi.sbc.goods.bean.wx.vo.assistant;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/5/24 2:24 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class WxLiveAssistantGoodsUpdateVo {


    private String goodsInfoId;

    /**
     * 商品免审金额
     */
    private BigDecimal wxPrice;


    /**
     * 商品免审库存
     */
    private Long wxStock;
}
