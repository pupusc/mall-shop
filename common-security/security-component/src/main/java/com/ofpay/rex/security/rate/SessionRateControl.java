package com.ofpay.rex.security.rate;

import java.util.Date;
import java.util.Stack;
import javax.servlet.http.HttpSession;

import com.ofpay.rex.control.Base64;

/**
 * User: Administrator
 * Date: 15-3-5
 * Time: 上午9:35
 */
public class SessionRateControl {

    /**
     * key前缀
     */
    private static final String TIMES = "times";

    /**
     * 每个urlKey的最近hit次的访问时间全部记录在session中
     *
     * @param session
     * @param urlKey  session中保存的key,用以区分不同的url正则,类+方法名
     * @param hit     次数上限(由于最近hit次会存在session中,所以请尽量配置小值,并且必须大于1)
     * @param period  周期时间
     * @return
     */
    public static boolean checkRate(HttpSession session, String urlKey, int hit, int period) {
        synchronized (session.getId().intern()) {
            String timesKey = TIMES + Base64.encode(urlKey);
            Stack<Date> times = (Stack<Date>) session.getAttribute(timesKey);
            if (times == null) {
                times = new Stack<Date>();
                session.setAttribute(timesKey, times);
            }
            Date newest = new Date();
            times.push(newest);

            if (times.size() >= hit) {
                Date oldest = times.remove(0);
                long elapsed = newest.getTime() - oldest.getTime();
                if (elapsed < period * 1000) {
                    return true;
                }
            }
        }
        return false;
    }
}
