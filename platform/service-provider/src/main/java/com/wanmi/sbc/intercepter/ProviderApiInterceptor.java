package com.wanmi.sbc.intercepter;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.store.NoDeleteStoreByIdRequest;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import io.jsonwebtoken.Claims;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 供应商 菜单方法拦截器
 * Created by zhangjin on 2017/6/21.
 */
public class ProviderApiInterceptor implements HandlerInterceptor {

    public ProviderApiInterceptor() {
    }

    public ProviderApiInterceptor(String jwtExcludedRestUrls, String apiExcludedRestUrls) {
        this.jwtExcludedRestUrlsMap = JSONObject.parseObject(jwtExcludedRestUrls);
        this.apiExcludedRestUrlsMap = JSONObject.parseObject(apiExcludedRestUrls);
    }

    @Autowired
//    private StoreService storeService;
    private StoreQueryProvider storeQueryProvider;

    /**
     * 排除的jwt rest url Map(包括uri 和 reqMethod
     */
    private JSONObject jwtExcludedRestUrlsMap;

    /**
     * 排除的api rest url Map(包括uri 和 reqMethod)
     */
    private JSONObject apiExcludedRestUrlsMap;

    /**
     * 路径比较器
     */
    private static AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 非审核通过的供应商可以访问的API接口
     */
    private static List<String> limitUrls = Lists.newArrayList(
            "/store/storeInfo",
            "/company",
            "/contract/**",
            "/account/*",
            "/company",
            "/employee/*/valid",
            "password/**",
            "/goods/allGoodsBrands",
            "/store/info",
            "/store/uploadStoreImage",
            "/store/uploadStoreResource",
            "/store/getS2bSupplierQrcode/*",
            "/account/list",
            "/account/base/bank",
            "/goods/goodsCatesTree"
    );

    /**
     * 登录找回密码不校验
     * 有功能的方法都应该校验uri
     * 导出功能的api不是走jwt, 逻辑特殊处理
     * 对非审核通过的供应商，进行受限处理
     *
     * @param request  请求
     * @param response 请求
     * @param handler  请求
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getServletPath();
        String requestType = request.getMethod();
        //如果是错误问题，直接返回
        if ("/error".equalsIgnoreCase(uri)) {
            return true;
        }
        // 0.先判断当前restful的请求是否可以直接跳过拦截器
        if (excludedUrlFilter(this.jwtExcludedRestUrlsMap, uri, requestType)) {
            return true;
        } else if (excludedUrlFilter(this.apiExcludedRestUrlsMap, uri, requestType)) {
            return true;
        }
        Claims claims;

        // 1.获取登录人信息
        claims = (Claims) request.getAttribute("claims");
        if (claims == null || claims.get("storeId") == null) {
            notAllowed(request, response);
            return false;
        }

        // 2.根据id查询登录人的店铺信息
        Long storeId = Long.valueOf(claims.get("storeId").toString());
//        Store store = storeService.findOne(storeId);
        StoreVO store = storeQueryProvider.getNoDeleteStoreById(new NoDeleteStoreByIdRequest(storeId)).getContext().getStoreVO();
        //当店铺处于审核待审核、审核未通过
        if (store.getAuditState() == null || Objects.equals(store.getAuditState(), CheckState.NOT_PASS)
                || Objects.equals(store.getAuditState(), CheckState.WAIT_CHECK)) {
            //不在受限路径
            if (limitUrls.stream().noneMatch(api -> antPathMatcher.match(api, uri))) {
                notAllowed(request, response);
                return false;
            }
        }
        return true;
    }

    /**
     * 将excludedRestUrls中的情况过滤掉
     *
     * @param excludedRestUrlsMap 可以直接跳过拦截器的rest url
     * @param uri                 当前请求的uri
     * @param requestType         当前请求类型
     * @return true:直接跳过拦截器 false:需要在拦截器中做一些处理
     */
    private boolean excludedUrlFilter(JSONObject excludedRestUrlsMap, String uri, String requestType) {
        if (excludedRestUrlsMap != null && !excludedRestUrlsMap.isEmpty()) {
            List<String> list = excludedRestUrlsMap.keySet().stream().filter(excludedRestUrl -> antPathMatcher.match
                    (excludedRestUrl, uri)).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(list)) {
                for (String restUrl : list) {
                    String reqMethodStr = excludedRestUrlsMap.getString(restUrl);
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
     * 没有权限时的提示信息
     *
     * @param response
     * @throws Exception
     */
    private void notAllowed(HttpServletRequest request,HttpServletResponse response) throws Exception {
        response.setStatus(HttpStatus.OK.value());
        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSONObject.toJSONString(new BaseResponse(CommonErrorCode.METHOD_NOT_ALLOWED)));
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
