package com.wanmi.sbc.order.bean.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 子订单信息
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/2/18 5:38 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class ProviderTradeSimpleVO implements Serializable {

    /**
     * 主单id
     */
    private String tid;

    /**
     * 子单id
     */
    private String pid;

    /**
     * 完成运费价格
     */
    private String deliveryCompletePrice;

    /**
     *  正在退款中的运费价格
     */
    private String deliveryIngPrice;

    /**
     * 运费
     */
    private String deliveryPrice;

    /**
     * 订单商品列表
     */
    private List<TradeItemSimpleVO> tradeItems;
}
