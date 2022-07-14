package com.soybean.mall.order.api.request.record;

import lombok.Data;

import java.util.List;

/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/7/13 11:56 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class OrderGiftRecordSearchReq {

    /**
     * 客户id
     */
    private String customerId;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 活动id
     */
    private String activityId;

    /**
     * 记录分类
     */
    private Integer recordCategory;

    /**
     * 记录状态
     */
    private List<Integer> recordStatus;

    /**
     * 引用表id
     *
     */
    private String quoteId;
}
