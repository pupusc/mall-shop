package com.wanmi.sbc.authority;

import com.google.common.collect.Lists;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.employee.RoleInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.EmployeeByIdRequest;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoListRequest;
import com.wanmi.sbc.customer.api.response.employee.EmployeeByIdResponse;
import com.wanmi.sbc.customer.bean.vo.RoleInfoVO;
import com.wanmi.sbc.setting.bean.enums.SystemAccount;
import com.wanmi.sbc.setting.api.provider.RoleMenuQueryProvider;
import com.wanmi.sbc.setting.api.request.AuthorityListRequest;
import com.wanmi.sbc.setting.api.request.AuthorityRequest;
import com.wanmi.sbc.setting.api.request.FunctionListRequest;
import com.wanmi.sbc.setting.bean.vo.AuthorityVO;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 检查是否有权限的Controller
 * Author: bail
 * Time: 2017/01/09
 */
@Api(tags = "CheckAuthController", description = "检查是否有权限")
@RestController
public class CheckAuthController {

    @Autowired
    private RoleMenuQueryProvider roleMenuQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private RoleInfoQueryProvider roleInfoQueryProvider;

    /**
     * 验证用户是否有某一个url/requestType的接口权限
     */
    @ApiOperation(value = "验证用户是否有某一个url/requestType的接口权限", notes = "true: 拥有该权限, false: 没有权限")
    @RequestMapping(value = "/check-function-auth", method = RequestMethod.POST)
    public BaseResponse<Boolean> checkFunctionAuth(@RequestBody AuthorityRequest authorityRequest){
        String employeeId = commonUtil.getOperatorId();
        EmployeeByIdResponse employee = employeeQueryProvider.getById(
                EmployeeByIdRequest.builder().employeeId(employeeId).build()
        ).getContext();
        if(employee != null) {
            // 1.system账号与主账号特殊处理
            if (SystemAccount.SYSTEM.getDesc().equals(employee.getAccountName()) || Objects.equals(DefaultFlag.YES.toValue(),employee.getIsMasterAccount())) {
                return BaseResponse.success(Boolean.TRUE);
            }

            // 2.其他账号进行角色权限判定
            List<Long> ids = Arrays.asList(employee.getRoleIds().split(",")).stream()
                    .mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
            ids = this.filterRoleId(ids);
            List<AuthorityVO> authorityList = new ArrayList<>();
            ids.stream().forEach(roleId -> {
                List<String> urls = authorityList.stream().map(AuthorityVO::getAuthorityUrl).collect(Collectors.toList());
                AuthorityListRequest request = new AuthorityListRequest();
                request.setRoleInfoId(Long.valueOf(roleId));
                List<AuthorityVO> list = roleMenuQueryProvider.listAuthority(request).getContext().getAuthorityVOList();
                if(CollectionUtils.isNotEmpty(list)){
                    List<AuthorityVO> vos = list.stream().filter(authorityVO -> !urls.contains(authorityVO.getAuthorityUrl()))
                            .collect(Collectors.toList());
                    authorityList.addAll(vos);
                }
            });

            AntPathMatcher antPathMatcher = new AntPathMatcher();
            if (authorityList.stream().anyMatch(authority -> antPathMatcher.match(authority.getAuthorityUrl(), authorityRequest.getUrlPath())
                    && authority.getRequestType().equals(authorityRequest.getRequestType()))) {
                // 3.拥有该权限则返回true,否则返回false
                return BaseResponse.success(Boolean.TRUE);
            }
        }
        return BaseResponse.success(Boolean.FALSE);
    }

    /**
     * 判断登录用户是否具备这些功能
     * @param functions 判断当前登录用户是否具有传入的这些功能权限
     */
    @ApiOperation(value = "判断登录用户是否具备这些功能", notes = "拥有权限的功能")
    @ApiImplicitParam(paramType = "body", dataType = "List", name = "functions",
            value = "判断当前登录用户是否具有传入的这些功能权限", required = true)
    @RequestMapping(value = "/functions", method = RequestMethod.POST)
    public ResponseEntity<List<String>> findTodoFunctionIds(@RequestBody List<String> functions) {
        String employeeId = commonUtil.getOperatorId();

        EmployeeByIdResponse employee = employeeQueryProvider.getById(
                EmployeeByIdRequest.builder().employeeId(employeeId).build()
        ).getContext();
        if(employee != null) {
            // 1.system账号与主账号特殊处理
            if (SystemAccount.SYSTEM.getDesc().equals(employee.getAccountName()) || Objects.equals(DefaultFlag.YES.toValue(),employee.getIsMasterAccount())) {
                return ResponseEntity.ok(functions);
            }

            List<Long> ids = Arrays.asList(employee.getRoleIds().split(",")).stream()
                    .mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
            ids = this.filterRoleId(ids);
            Set<String> set = new HashSet<>();
            ids.stream().forEach(roleId -> {
                // 2.其他账号进行角色查询功能列表
                FunctionListRequest request = new FunctionListRequest();
                request.setRoleInfoId(Long.valueOf(roleId));
                request.setAuthorityNames(functions);
                set.addAll(roleMenuQueryProvider.listFunction(request).getContext().getFunctionList());
            });
            List<String> functionNameList = new ArrayList<>(set);

            return ResponseEntity.ok(functionNameList);
        }
        return ResponseEntity.ok(Lists.newArrayList());
    }

    /**
     * 过滤roleId
     * @return
     */
    private List<Long> filterRoleId(List<Long> ids){
        RoleInfoListRequest roleInfoListRequest = new RoleInfoListRequest();
        roleInfoListRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        List<RoleInfoVO> roleInfoVOList = roleInfoQueryProvider.listByCompanyInfoId(roleInfoListRequest).getContext().getRoleInfoVOList();
        if(org.apache.commons.collections.CollectionUtils.isNotEmpty(roleInfoVOList)){
            List<Long> roleInfoIds = roleInfoVOList.stream().map(RoleInfoVO::getRoleInfoId).collect(Collectors.toList());
            //过滤不存在的角色
            ids = ids.stream().filter(id -> roleInfoIds.contains(id)).collect(Collectors.toList());
        }
        return ids;
    }

}
