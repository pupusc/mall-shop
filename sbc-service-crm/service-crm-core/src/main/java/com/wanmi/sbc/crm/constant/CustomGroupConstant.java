package com.wanmi.sbc.crm.constant;

import java.util.Arrays;
import java.util.List;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-12
 * \* Time: 15:43
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
public class CustomGroupConstant {

    //默认的统计会员的时间周期
    public final static List<Integer> DEFAULT_PERIOD = Arrays.asList(
                1,
                7,
                30,
                60,
                90,
                180,
                365);

    public final static String FM_REGIONS = "所在地区%s；";

    public final static String FM_LAST_TRADE = "最近一次消费在%d天内；";

    public final static String FM_TRADE_COUNT = "近%d天累计消费%s次；";

    public final static String FM_TRADE_AMOUNT = "近%d天内累计消费%s元；";

    public final static String FM_AVG_TRADE_AMOUNT = "近%d天内笔单价%s元；";

    public final static String FM_CUSTOMER_LEVEL = "会员等级为%s；";

    public final static String FM_CUSTOMER_GROWTH = "会员成长值%s；";

    public final static String FM_CUSTOMER_POINTS = "会员积分%s分；";

    public final static String FM_CUSTOMER_BALANCE = "会员余额%s元；";

    public final static String FM_RECENT_PAY_TRADE = "最近%d天有付款；";

    public final static String FM_NO_RECENT_PAY_TRADE = "最近%d天无付款；";

    public final static String FM_RECENT_TRADE = "最近%d天有下单；";

    public final static String FM_NO_RECENT_TRADE = "最近%d天无下单；";

    public final static String FM_RECENT_CART = "最近%d天有加购；";

    public final static String FM_NO_RECENT_CART = "最近%d天无加购；";

    public final static String FM_RECENT_FLOW = "最近%d天有访问；";

    public final static String FM_NO_RECENT_FLOW = "最近%d天无访问；";

    public final static String FM_RECENT_FAVORITE = "最近%d天有收藏；";

    public final static String FM_NO_RECENT_FAVORITE = "最近%d天无收藏；";

    public final static String FM_CUSTOMER_TAG = "会员标签：%s；";

    public final static String FM_CUSTOMER_GENDER = "性别";

    public final static String FM_CUSTOMER_AGE = "年龄%s岁；";

    public final static String FM_CUSTOMER_ADMISSIONTIME = "入会时间%s天；";

}
