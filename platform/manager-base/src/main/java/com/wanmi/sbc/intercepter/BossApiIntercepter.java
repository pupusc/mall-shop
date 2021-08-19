package com.wanmi.sbc.intercepter;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.employee.RoleInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.EmployeeByIdRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeeListRequest;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoListRequest;
import com.wanmi.sbc.customer.api.response.employee.EmployeeByIdResponse;
import com.wanmi.sbc.customer.bean.enums.AccountState;
import com.wanmi.sbc.customer.bean.vo.EmployeeListVO;
import com.wanmi.sbc.customer.bean.vo.RoleInfoVO;
import com.wanmi.sbc.setting.api.provider.RoleMenuQueryProvider;
import com.wanmi.sbc.setting.api.request.AuthorityListRequest;
import com.wanmi.sbc.setting.bean.enums.SystemAccount;
import com.wanmi.sbc.setting.bean.vo.AuthorityVO;
import io.jsonwebtoken.Claims;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * boss 菜单方法拦截器 😜😜
 * Created by zhangjin on 2017/6/21.
 */
public class BossApiIntercepter implements HandlerInterceptor {

    public BossApiIntercepter() {
    }

    public BossApiIntercepter(String jwtExcludedRestUrls, String apiExcludedRestUrls) {
        this.jwtExcludedRestUrlsMap = JSONObject.parseObject(jwtExcludedRestUrls);
        this.apiExcludedRestUrlsMap = JSONObject.parseObject(apiExcludedRestUrls);
    }


    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private RoleMenuQueryProvider roleMenuQueryProvider;

    @Autowired
    private RoleInfoQueryProvider roleInfoQueryProvider;

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
     * 权限的统一拦截入口
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
            Exception {
        String uri = request.getServletPath();
        String requestType = request.getMethod();
        // 0.先判断当前restful的请求是否可以直接跳过拦截器
        if (excludedUrlFilter(this.jwtExcludedRestUrlsMap, uri, requestType)) {
            return true;
        } else if (excludedUrlFilter(this.apiExcludedRestUrlsMap, uri, requestType)) {
            return true;
        }
        Claims claims;

        // 1.获取登录人信息
        claims = (Claims) request.getAttribute("claims");
        if (claims == null || claims.get("employeeId") == null) {
            notAllowed(request, response);
            return false;
        }

        // 2.根据id查询登录用户信息
        String employeeId = claims.get("employeeId").toString();

        EmployeeByIdResponse employee = employeeQueryProvider.getById(
                EmployeeByIdRequest.builder().employeeId(employeeId).build()
        ).getContext();

        if (Objects.isNull(employee) || Objects.equals(AccountState.DISABLE, employee.getAccountState())) {
            disable(request, response);
            return false;
        }

        // 3.system账号特权 以及 店铺主账号特权
        if (SystemAccount.SYSTEM.getDesc().equals(employee.getAccountName()) || Objects.equals(DefaultFlag.YES
                .toValue(), employee.getIsMasterAccount())) {
            return true;
        }

        //商家业务员，查询主账号是否被禁用
        if (Objects.equals(DefaultFlag.NO.toValue(), employee.getIsMasterAccount()) && Objects.nonNull(employee.getCompanyInfoId())) {
            EmployeeListRequest employeeRequest = new EmployeeListRequest();
            employeeRequest.setCompanyInfoId(employee.getCompanyInfoId());
            employeeRequest.setIsMasterAccount(Constants.yes);
            List<EmployeeListVO> masterEmployee = employeeQueryProvider.list(employeeRequest).getContext().getEmployeeList();
            if (CollectionUtils.isEmpty(masterEmployee) || Objects.equals(AccountState.DISABLE, masterEmployee.get(0).getAccountState())) {
                disable(request, response);
            }
        }

        // 5.判断登录人角色针对 当前url 与 请求方式 是否有权限访问
        List<AuthorityVO> authorityList = new ArrayList<>();
        List<Long> ids = Arrays.asList(employee.getRoleIds().split(",")).stream()
                .mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
        ids = this.filterRoleId(ids, employee.getCompanyInfoId());
        ids.stream().forEach(roleId -> {
            List<String> urls = authorityList.stream().map(AuthorityVO::getAuthorityUrl).collect(Collectors.toList());
            AuthorityListRequest request1 = new AuthorityListRequest();
            request1.setRoleInfoId(Long.valueOf(roleId));
            List<AuthorityVO> list = roleMenuQueryProvider.listAuthority(request1).getContext().getAuthorityVOList();
            if(CollectionUtils.isNotEmpty(list)){
                List<AuthorityVO> vos = list.stream().filter(authorityVO -> !urls.contains(authorityVO.getAuthorityUrl()))
                        .collect(Collectors.toList());
                authorityList.addAll(vos);
            }
        });

        if (authorityList.stream().noneMatch(authority -> antPathMatcher.match(authority.getAuthorityUrl(), uri)
                && authority.getRequestType().equals(requestType))) {
            notAllowed(request, response);
            return false;
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
     * @param request
     * @param response
     * @throws Exception
     */
    private void notAllowed(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setStatus(HttpStatus.OK.value());
//        response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
//        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSONObject.toJSONString(new BaseResponse(CommonErrorCode.METHOD_NOT_ALLOWED)));
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * 被删除或禁用时的提示信息
     *
     *
     * @param request
     * @param response
     * @throws Exception
     */
    private void disable(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setStatus(HttpStatus.OK.value());
        response.addHeader("Access-Control-Allow-Headers", "authorization,content-type,x-requested-with,systemId," +
                "Platform,reqId");
        //response.addHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        //response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSONObject.toJSONString(new BaseResponse(CommonErrorCode.EMPLOYEE_DISABLE)));
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
            modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception
            ex) throws Exception {

    }

    /**
     * 过滤roleId
     * @return
     */
    private List<Long> filterRoleId(List<Long> ids, Long companyInfoId){
        RoleInfoListRequest roleInfoListRequest = new RoleInfoListRequest();
        roleInfoListRequest.setCompanyInfoId(companyInfoId);
        List<RoleInfoVO> roleInfoVOList = roleInfoQueryProvider.listByCompanyInfoId(roleInfoListRequest).getContext().getRoleInfoVOList();
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(roleInfoVOList)){
            List<Long> roleInfoIds = roleInfoVOList.stream().map(RoleInfoVO::getRoleInfoId).collect(Collectors.toList());
            //过滤不存在的角色
            ids = ids.stream().filter(id -> roleInfoIds.contains(id)).collect(Collectors.toList());
        }
        return ids;
    }

}
