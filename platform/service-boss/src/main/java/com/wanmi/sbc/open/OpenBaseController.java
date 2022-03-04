package com.wanmi.sbc.open;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Liang Jun
 * @date 2022-02-22 21:37:00
 */
@Slf4j
public class OpenBaseController {
    @Value("${open.fdds.appsecret}")
    private String openFddsAppsecret;

    protected void checkSign() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        StringBuffer body = new StringBuffer();
        String line;

        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        } catch (IOException e) {
            log.error("read http request failed.", e);
        }

        JSONObject jsonParams = JSON.parseObject(body.toString());
        Map<String, String> mapParams = Maps.newTreeMap();
        spreadParams(jsonParams, mapParams, null);

        String localSign = createSign(getAppSecret(), mapParams);
        if (!localSign.equals(mapParams.get("sign"))) {
            log.warn("验签错误，paramSign = {}, localSign = {}", mapParams.get("sign"), localSign);
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
    }

    private static void spreadParams(JSONObject jsonParams, Map<String, String> mapParams, String prefix) {
        if (Objects.isNull(jsonParams) || Objects.isNull(mapParams)) {
            return;
        }
        if (Objects.isNull(prefix)) {
            prefix = "";
        }

        Set<Map.Entry<String, Object>> entries = jsonParams.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            if (entry.getValue() instanceof JSONObject) {
                //递归平铺参数
                spreadParams((JSONObject) entry.getValue(), mapParams, prefix + entry.getKey() + ".");
                continue;
            }
            mapParams.put(prefix + entry.getKey(), Objects.isNull(entry.getValue()) ? "" : entry.getValue().toString());
        }
    }

    private static String createSign(String appKey, Map<String, String> parameters) {
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

    public static void main(String[] args) {
        String params = "{\n" +
                "    \"fddsUserId\": 19187,\n" +
                "    \"outTradeNo\": \"dd000006\",\n" +
                "    \"buyerRemark\": \"乌克兰战争\",\n" +
                "    \"consigneeAddress\": \"安徽省合肥市庐阳区庐阳工业区文一名门首府\",\n" +
                "        \"consignee\": {\n" +
                "        \"provinceId\": \"340000\",\n" +
                "        \"cityId\": \"340100\",\n" +
                "        \"areaId\": \"340103\",\n" +
                "        \"streetId\": \"340103400\",\n" +
                "        \"address\": \"文一名门首府\",\n" +
                "        \"name\": \"千鹤\",\n" +
                "        \"phone\": \"18788891200\"\n" +
                "    },\n" +
                "    \"tradeItems\": [\n" +
                "        {\n" +
                "            \"specDetails\": null,\n" +
                "            \"skuId\": \"2c90c8647f361c2f017f3cab3d06007a\",\n" +
                "            \"num\": 1,\n" +
                "            \"marketingIds\": []\n" +
                "        }\n" +
                "    ],\n" +
                "    \n" +
                "    \"t\":123324092384034\n" +
                "}";

        String appKey = "fdds-mall-deliver-secret-xxoo";
        JSONObject jsonParams = JSON.parseObject(params);
        Map<String, String> mapParams = Maps.newTreeMap();

        spreadParams(jsonParams, mapParams, null);
        System.out.println("------------------------------------");
        System.out.println(createSign(appKey, mapParams));
        System.out.println("------------------------------------");
    }
}
