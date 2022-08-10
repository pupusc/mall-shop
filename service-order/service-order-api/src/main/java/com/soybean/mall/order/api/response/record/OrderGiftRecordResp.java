package com.soybean.mall.order.api.response.record;

import lombok.Data;


/**
 * Description:
 * Company    : 上海黄豆网络科技有限公司
 * Author     : duanlongshan@dushu365.com
 * Date       : 2022/8/2 8:03 下午
 * Modify     : 修改日期          修改人员        修改说明          JIRA编号
 ********************************************************************/
@Data
public class OrderGiftRecordResp {


    private Integer id;

    /**
     * 名称
     */
    private String name;

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
     * 份数
     */
    private Integer per;

    /**
     * 外部引用id
     */
    private String quoteId;

    /**
     * 记录分类 1 返积分活动
     */
    private Integer recordCategory;

    /**
     * 记录状态 0、创建 1 锁定 2 成功 3 普通取消 4 黑名单取消
     */
    private Integer recordStatus;
}
