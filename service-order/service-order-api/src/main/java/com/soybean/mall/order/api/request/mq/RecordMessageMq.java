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
     * 业务id
     */
    private String businessId;

    /**
     * 渠道类型
     */
    private List<Integer> channelTypes;

    /**
     * 消息类型 1 下单 2支付 {@link com.soybean.mall.order.api.enums.RecordMessageTypeEnum}
     */
    private Integer recordMessageType;
}
