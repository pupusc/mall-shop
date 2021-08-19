package com.wanmi.sbc.intercepter;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.provider.SensitiveWordsQueryProvider;
import com.wanmi.sbc.setting.api.request.SensitiveWordsBadWordRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auther: jiaojiao
 * @Date: 2019/3/3 14:35
 * @Description: 关键字拦截器
 */
public class BadWordInterceptor implements HandlerInterceptor {

    @Autowired
    private SensitiveWordsQueryProvider queryProvider;
    /**
     * 拦截的api rest url Map(包括uri 和 reqMethod)
     */
    private JSONObject apiBadWordsAddPathUrlsMap;;

    /**
     * 路径比较器
     */
    private static AntPathMatcher antPathMatcher = new AntPathMatcher();

    public BadWordInterceptor() {
    }

    public BadWordInterceptor( String apiBadWordsAddPathUrls) {
        this.apiBadWordsAddPathUrlsMap = JSONObject.parseObject(apiBadWordsAddPathUrls);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        String uri = request.getServletPath();
        if ("/error".equalsIgnoreCase(uri)) {
            return true;
        }

        String requestType = request.getMethod();
        if (AddPathUrlFilter(this.apiBadWordsAddPathUrlsMap, uri, requestType)) {
            String body= readAsChars(request);
            if (StringUtils.isNotBlank(body) && !"{}".equals(body)) {
                SensitiveWordsBadWordRequest badWordRequest = new SensitiveWordsBadWordRequest();
                badWordRequest.setTxt(body);
                Set<String> set =queryProvider.getBadWord(badWordRequest).getContext();
                if (set.size() > 0) {
                    notAllowed(response,request, "包括敏感词：" + set.toString());
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {

    }

    /**
     * @param AddPathRestUrlsMap 可以直接跳过拦截器的rest url
     * @param uri                 当前请求的uri
     * @param requestType         当前请求类型
     * @return true:需要在拦截器中做一些处理 false:直接跳过拦截器
     */
    private boolean AddPathUrlFilter(JSONObject AddPathRestUrlsMap, String uri, String requestType) {
        if (AddPathRestUrlsMap != null && !AddPathRestUrlsMap.isEmpty()) {
            List<String> list = AddPathRestUrlsMap.keySet().stream().filter(AddPathRestUrl -> antPathMatcher.match
                    (AddPathRestUrl, uri)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(list)) {
                for (String restUrl : list) {
                    String reqMethodStr = AddPathRestUrlsMap.getString(restUrl);
                    if (reqMethodStr != null) {
                        if (reqMethodStr.contains(requestType)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 含有敏感词 提示信息
     *
     * @param response
     * @throws Exception
     */
    private void notAllowed(HttpServletResponse response,HttpServletRequest request, String message) throws Exception {
        response.setStatus(HttpStatus.OK.value());
//        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
//        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSONObject.toJSONString(BaseResponse.info(CommonErrorCode.INCLUDE_BAD_WORD, message)));
        response.getWriter().flush();
        response.getWriter().close();
    }


    // 字符串读取
    // 方法一
    public static String readAsChars(HttpServletRequest request) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
        return sb.toString();
    }


    public JSONObject getApiBadWordsAddPathUrlsMap() {
        return apiBadWordsAddPathUrlsMap;
    }

    public void setApiBadWordsAddPathUrlsMap(JSONObject apiBadWordsAddPathUrlsMap) {
        this.apiBadWordsAddPathUrlsMap = apiBadWordsAddPathUrlsMap;
    }
}