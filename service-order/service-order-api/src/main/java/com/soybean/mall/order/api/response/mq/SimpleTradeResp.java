package com.soybean.mall.order.api.response.mq;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/14 11:01 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class SimpleTradeResp {

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 客户id
     */
    private String customerId;

    /**
     * skuId
     */
    private List<String> skuIds;
}
