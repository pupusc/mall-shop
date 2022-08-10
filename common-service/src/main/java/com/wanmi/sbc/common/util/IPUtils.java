package com.wanmi.sbc.common.util;

import com.jthinking.common.util.ip.IPInfo;
import com.jthinking.common.util.ip.IPInfoUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Liang Jun
 * @date 2022-08-03 14:23:00
 */
public final class IPUtils {
    static {
        IPInfoUtils.init();
    }

    private IPUtils() {
    }

    public static String getIpPlace(String ip) {
        IPInfo ipInfo = IPInfoUtils.getIpInfo(ip);
        String place = ipInfo.isOverseas() ? ipInfo.getCountry() : ipInfo.getProvince();
        return StringUtils.isBlank(place) ? "未知" : place;
    }
}
