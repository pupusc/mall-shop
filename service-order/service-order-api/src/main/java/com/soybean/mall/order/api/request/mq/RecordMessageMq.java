package com.soybean.mall.order.api.request.mq;

import lombok.Data;

import java.util.List;

/**
 * Description: mq消息对象
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/14 10:46 上午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class RecordMessageMq {

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 渠道类型
     */
    private List<Integer> channelTypes;
}
