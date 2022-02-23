package com.wanmi.sbc.open;

import com.google.common.collect.Maps;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Liang Jun
 * @date 2022-02-22 21:37:00
 */
@Slf4j
public class OpenBaseController {
    @Value("${open.fdds.appsecret}")
    private String openFddsAppsecret;

    protected void checkSign() {
        HttpServletRequest currRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        Map<String, String> params = Maps.newTreeMap();

        Enumeration<String> e = currRequest.getParameterNames();
        while (e.hasMoreElements()) {
            String parName = e.nextElement();
            params.put(parName, currRequest.getParameter(parName));

//            if (!parName.equalsIgnoreCase("sign")) {
//                params.put(parName, currRequest.getParameter(parName));
//            }
        }

        String localSign = createSign(getAppSecret(), params);
        if (!localSign.equals(params.get("sign"))) {
            log.warn("验签错误，paramSign = {}, localSign = {}", params.get("sign"), localSign);
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    public static String createSign(String appKey, Map<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        Iterator<Map.Entry<String, String>> it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            //去掉带sign的项
            String value = entry.getValue();
            if (null != value && !"".equals(value) && !"sign".equals(key)
                    && !"key".equals(key)) {
                sb.append(key + "=" + value + "&");
            }
        }
        sb.append("key=" + appKey);
        //注意sign转为大写
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes()).toUpperCase();
    }

    private String getAppSecret() {
        return openFddsAppsecret;
    }
}
