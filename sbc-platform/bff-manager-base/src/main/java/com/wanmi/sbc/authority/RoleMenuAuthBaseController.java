package com.wanmi.sbc.authority;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.constant.EmployeeErrorCode;
import com.wanmi.sbc.customer.api.provider.employee.EmployeeQueryProvider;
import com.wanmi.sbc.customer.api.provider.employee.RoleInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.EmployeeByIdRequest;
import com.wanmi.sbc.customer.api.request.employee.RoleIdByEmployeeIdRequest;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoListRequest;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoQueryRequest;
import com.wanmi.sbc.customer.api.response.employee.EmployeeByIdResponse;
import com.wanmi.sbc.customer.api.response.employee.RoleInfoQueryResponse;
import com.wanmi.sbc.customer.bean.vo.RoleInfoVO;
import com.wanmi.sbc.setting.api.provider.RoleMenuProvider;
import com.wanmi.sbc.setting.api.provider.RoleMenuQueryProvider;
import com.wanmi.sbc.setting.api.request.*;
import com.wanmi.sbc.setting.api.response.RoleMenuFuncIdsQueryResponse;
import com.wanmi.sbc.setting.bean.enums.SystemAccount;
import com.wanmi.sbc.setting.bean.vo.MenuInfoVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 角色菜单权限管理Controller
 * Author: bail
 * Time: 2017/12/29
 */
@Api(tags = "RoleMenuAuthBaseController", description = "角色菜单权限管理 Api")
@RestController
@RequestMapping("/roleMenuFunc")
public class RoleMenuAuthBaseController {

    @Autowired
    private RoleMenuProvider roleMenuProvider;

    @Autowired
    private RoleMenuQueryProvider roleMenuQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private EmployeeQueryProvider employeeQueryProvider;

    @Autowired
    private RoleInfoQueryProvider roleInfoQueryProvider;


    /**
     * 查询角色拥有的菜单id与权限id信息
     */
    @ApiOperation(value = "查询角色拥有的菜单id与权限id信息")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "roleInfoId", value = "角色Id", required = true)
    @RequestMapping(value = "/{roleInfoId}", method = RequestMethod.GET)
    public BaseResponse<RoleMenuFuncIdsQueryResponse> get(@PathVariable Long roleInfoId) {
        RoleMenuFuncIdsQueryRequest request = new RoleMenuFuncIdsQueryRequest();
        request.setRoleInfoId(roleInfoId);

        return roleMenuQueryProvider.queryRoleMenuFuncIds(request);
    }

    /**
     * 编辑角色的菜单与权限信息
     *
     * @param roleMenuFuncRequest 该角色的菜单与权限信息
     */
    @ApiOperation(value = "编辑角色的菜单与权限信息")
    @RequestMapping(method = RequestMethod.PUT)
    public BaseResponse edit(@RequestBody RoleMenuAuthSaveRequest roleMenuFuncRequest) {
        RoleInfoQueryRequest queryRequest = new RoleInfoQueryRequest();
        queryRequest.setRoleInfoId(roleMenuFuncRequest.getRoleInfoId());
        RoleInfoQueryResponse queryResponse = roleInfoQueryProvider.getRoleInfoById(queryRequest).getContext();

        roleMenuProvider.saveRoleMenuAuth(roleMenuFuncRequest);

        //操作日志记录
        operateLogMQUtil.convertAndSend("设置", "编辑角色权限",
                "编辑角色权限：" + (Objects.nonNull(queryResponse) ? queryResponse.getRoleInfoVO().getRoleName() : ""));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 查询登录用户的所有的菜单信息
     */
    @ApiOperation(value = "查询登录用户的所有的菜单信息")
    @RequestMapping(value = "/menus", method = RequestMethod.GET)
    public BaseResponse<List<MenuInfoVO>> getLoginMenus() {
        Operator operator = commonUtil.getOperator();

        EmployeeByIdResponse employee = employeeQueryProvider.getById(
                EmployeeByIdRequest.builder().employeeId(operator.getUserId()).build()
        ).getContext();

        List<MenuInfoVO> menuInfoResponseList = new ArrayList<>();
        if (employee != null) {
            // system账号特权 以及 店铺主账号查询对应平台的所有权限
            if (SystemAccount.SYSTEM.getDesc().equals(employee.getAccountName()) || Objects.equals(DefaultFlag.YES.toValue(), employee.getIsMasterAccount())) {
                AllRoleMenuInfoListRequest request = new AllRoleMenuInfoListRequest();
                request.setSystemTypeCd(operator.getPlatform());
                menuInfoResponseList =
                        roleMenuQueryProvider.listAllRoleMenuInfo(request).getContext().getMenuInfoVOList();
            } else if (StringUtils.isBlank(employee.getRoleIds())) {
                // 若没有角色,提示无权限联系管理员
                throw new SbcRuntimeException(EmployeeErrorCode.ACCESS_DENIED, new Object[]{"，请联系您的管理员"});
            } else {
                List<Long> ids = Arrays.asList(employee.getRoleIds().split(",")).stream()
                        .mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
                //过滤不存在的角色
                ids = this.filterRoleId(ids);
                if(CollectionUtils.isNotEmpty(ids)){
                    RoleMenuInfoListRequest request = new RoleMenuInfoListRequest();
                    request.setRoleInfoId(ids);
                    menuInfoResponseList = roleMenuQueryProvider.listRoleMenuInfo(request).getContext().getMenuInfoVOList();
                }
            }
            // 若没有任何菜单权限,请联系管理员
            if (CollectionUtils.isEmpty(menuInfoResponseList)) {
                throw new SbcRuntimeException(EmployeeErrorCode.ACCESS_DENIED, new Object[]{"，请联系您的管理员"});
            }
            return BaseResponse.success(menuInfoResponseList);
        } else {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /**
     * 查询登录用户拥有的功能
     */
    @ApiOperation(value = "查询登录用户拥有的功能", notes = "拥有权限的功能")
    @RequestMapping(value = "/functions", method = RequestMethod.GET)
    public BaseResponse<List<String>> getLoginFunctions() {
        Operator operator = commonUtil.getOperator();
        String roleId = employeeQueryProvider.getRoleIdByEmployeeId(
                RoleIdByEmployeeIdRequest.builder().employeeId(operator.getUserId()).build()
        ).getContext().getRoleId();
        List<String> functionList = new ArrayList<>();
        if (roleId != null) {
            Set<String> set = new HashSet<>();
            List<Long> ids = new ArrayList<>();
            if(!SystemAccount.SYSTEM.getDesc().equals(roleId)){
                 ids = Arrays.asList(roleId.split(",")).stream()
                        .mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
                //过滤不存在的角色
                ids = this.filterRoleId(ids);
                ids.stream().forEach(id -> {
                    FunctionListByRoleIdRequest request = new FunctionListByRoleIdRequest();
                    request.setRoleId(id.toString());
                    request.setSystemTypeCd(operator.getPlatform());
                    List<String> list = roleMenuQueryProvider.listFunctionsByRoleId(request).getContext().getFunctionList();
                    if(CollectionUtils.isNotEmpty(list)){
                        set.addAll(list);
                    }
                });
                functionList.addAll(set);
            }else{
                FunctionListByRoleIdRequest request = new FunctionListByRoleIdRequest();
                request.setRoleId(roleId);
                request.setSystemTypeCd(operator.getPlatform());
                functionList = roleMenuQueryProvider.listFunctionsByRoleId(request).getContext().getFunctionList();
            }


            if(CollectionUtils.isEmpty(functionList)){
                throw new SbcRuntimeException("K-040023", new Object[]{"，请联系您的管理员"});
            }
            return BaseResponse.success(functionList);
        } else {
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }
    }

    /**
     * 过滤roleId
     * @return
     */
    private List<Long> filterRoleId(List<Long> ids){
        RoleInfoListRequest roleInfoListRequest = new RoleInfoListRequest();
        roleInfoListRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        List<RoleInfoVO> roleInfoVOList = roleInfoQueryProvider.listByCompanyInfoId(roleInfoListRequest).getContext().getRoleInfoVOList();
        if(CollectionUtils.isNotEmpty(roleInfoVOList)){
            List<Long> roleInfoIds = roleInfoVOList.stream().map(RoleInfoVO::getRoleInfoId).collect(Collectors.toList());
            //过滤不存在的角色
            ids = ids.stream().filter(id -> roleInfoIds.contains(id)).collect(Collectors.toList());
        }
        return ids;
    }

}
