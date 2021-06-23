package com.wanmi.sbc.system;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.customer.api.provider.employee.RoleInfoProvider;
import com.wanmi.sbc.customer.api.provider.employee.RoleInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.*;
import com.wanmi.sbc.customer.api.response.employee.RoleInfoListResponse;
import com.wanmi.sbc.customer.api.response.employee.RoleInfoQueryResponse;
import com.wanmi.sbc.customer.bean.vo.RoleInfoVO;
import com.wanmi.sbc.setting.api.provider.RoleMenuQueryProvider;
import com.wanmi.sbc.setting.api.request.RoleInfoAndMenuInfoListRequest;
import com.wanmi.sbc.setting.api.response.RoleInfoAndMenuInfoListResponse;
import com.wanmi.sbc.setting.bean.vo.RoleInfoAndMenuInfoVO;
import com.wanmi.sbc.util.CommonUtil;
import com.wanmi.sbc.util.OperateLogMQUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 角色服务
 * Created by CHENLI on 2017/5/8.
 */
@Api(tags = "RoleInfoController", description = "角色服务 Api")
@RestController
public class RoleInfoController {

    @Autowired
    private RoleInfoProvider roleInfoProvider;

    @Autowired
    private RoleInfoQueryProvider roleInfoQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private OperateLogMQUtil operateLogMQUtil;

    @Autowired
    private RoleMenuQueryProvider roleMenuQueryProvider;

    /**
     * 查询所有角色
     *
     * @return ResponseEntity<List < RoleInfo>>
     */
    @ApiOperation(value = "查询所有角色")
    @RequestMapping(value = "/role-list", method = RequestMethod.GET)
    public BaseResponse<RoleInfoAndMenuInfoListResponse> findAllRolesWithMenu() {
        RoleInfoListRequest roleInfoListRequest = new RoleInfoListRequest();
        roleInfoListRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        BaseResponse<RoleInfoListResponse> roleInfoListResponseBaseResponse =
                roleInfoQueryProvider.listByCompanyInfoId(roleInfoListRequest);
        RoleInfoListResponse roleInfoListResponse = roleInfoListResponseBaseResponse.getContext();
        if (Objects.nonNull(roleInfoListResponse)) {
            List<RoleInfoVO> roleInfoVOList = roleInfoListResponse.getRoleInfoVOList();
            Map<Long,String> map  = roleInfoVOList.stream().collect(Collectors.toMap(RoleInfoVO::getRoleInfoId,RoleInfoVO::getRoleName));
            List<Long> roleInfoIds = roleInfoVOList.stream().map(RoleInfoVO::getRoleInfoId).collect(Collectors.toList());
            BaseResponse<RoleInfoAndMenuInfoListResponse> baseResponse = roleMenuQueryProvider.listRoleWithMenuNames(new RoleInfoAndMenuInfoListRequest(roleInfoIds));
            List<RoleInfoAndMenuInfoVO> roleInfoAndMenuInfoVOList = baseResponse.getContext().getRoleInfoAndMenuInfoVOList();
            roleInfoAndMenuInfoVOList = roleInfoAndMenuInfoVOList.stream().map(v -> {
                v.setRoleName(map.get(v.getRoleInfoId()));
                return v;
            }).collect(Collectors.toList());
            return BaseResponse.success(new RoleInfoAndMenuInfoListResponse(roleInfoAndMenuInfoVOList));
        }
        return BaseResponse.success(new RoleInfoAndMenuInfoListResponse(new ArrayList<>()));
    }

    /**
     * 保存角色信息
     *
     * @param saveRequest
     * @return
     */
    @ApiOperation(value = "保存角色信息")
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    public ResponseEntity<BaseResponse> saveRoleInfo(@RequestBody RoleInfoAddRequest saveRequest) {
        saveRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        roleInfoProvider.add(saveRequest);
        operateLogMQUtil.convertAndSend("设置", "新增角色", "新增角色：" + saveRequest.getRoleName());
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 修改角色信息
     *
     * @param editRequest
     * @return
     */
    @ApiOperation(value = "修改角色信息")
    @RequestMapping(value = "/role", method = RequestMethod.PUT)
    public ResponseEntity<BaseResponse> saveRoleInfo(@RequestBody RoleInfoModifyRequest editRequest) {
        // 查询角色信息
        RoleInfoQueryRequest request = new RoleInfoQueryRequest();
        request.setRoleInfoId(editRequest.getRoleInfoId());
        BaseResponse<RoleInfoQueryResponse> baseResponse = roleInfoQueryProvider.getRoleInfoById(request);
        RoleInfoQueryResponse response = baseResponse.getContext();

        // 修改角色信息
        editRequest.setCompanyInfoId(commonUtil.getCompanyInfoId());
        roleInfoProvider.modify(editRequest);
        if (Objects.nonNull(response)) {
            //操作日志记录
            operateLogMQUtil.convertAndSend("设置", "修改角色名称",
                    "修改角色名称: " + response.getRoleInfoVO().getRoleName() + " 修改为 " + editRequest.getRoleName());
        }
        return ResponseEntity.ok(BaseResponse.SUCCESSFUL());
    }

    /**
     * 删除角色信息
     * @return
     */
    @ApiOperation(value = "删除角色信息")
    @RequestMapping(value = "/role/{roleInfoId}", method = RequestMethod.DELETE)
    public BaseResponse delete(@PathVariable Long roleInfoId) {

       BaseResponse baseResponse = roleInfoProvider.delete(new RoleInfoDeleteRequest(roleInfoId));
        if (CommonErrorCode.SUCCESSFUL.equals(baseResponse.getCode())) {
            //操作日志记录
            operateLogMQUtil.convertAndSend("设置", "删除角色",
                    "删除角色ID: " + roleInfoId );
        }
        return BaseResponse.SUCCESSFUL();
    }


}
