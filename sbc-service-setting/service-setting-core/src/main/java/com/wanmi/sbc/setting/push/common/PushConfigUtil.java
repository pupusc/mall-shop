package com.wanmi.sbc.setting.push.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据2.0写法 设置推送类型
 *
 * @author chenyufei
 */
public class PushConfigUtil {

    /**
     * 推送类型
     */
    public final static Map<Integer, Object> TYPE_CONFIG = new HashMap<Integer, Object>() {{
        put(1, "订单支付成功通知");
        put(2, "订单审核通知");
        put(3, "订单发货通知");
        put(4, "订单完成通知");
        put(5, "售后申请通知");
        put(6, "售后进度更新");
        put(7, "退款结果通知");
        put(8, "优惠券到期提醒");
    }};
}
