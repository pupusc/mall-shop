package com.wanmi.sbc.customer.provider.impl.employee;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.employee.RoleInfoQueryProvider;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoListRequest;
import com.wanmi.sbc.customer.api.request.employee.RoleInfoQueryRequest;
import com.wanmi.sbc.customer.api.response.employee.RoleInfoQueryResponse;
import com.wanmi.sbc.customer.api.response.employee.RoleInfoListResponse;
import com.wanmi.sbc.customer.bean.vo.RoleInfoVO;
import com.wanmi.sbc.customer.employee.model.root.RoleInfo;
import com.wanmi.sbc.customer.employee.service.RoleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: wanggang
 * @CreateDate: 2018/9/17 14:22
 * @Version: 1.0
 */
@Validated
@RestController
public class RoleInfoQueryController implements RoleInfoQueryProvider {

    @Autowired
    private RoleInfoService roleInfoService;

    /**
     * 根据公司编号查询角色信息列表
     *
     * @param roleInfoListRequest
     * @return
     */

    @Override
    public BaseResponse<RoleInfoListResponse> listByCompanyInfoId(@RequestBody @Valid RoleInfoListRequest roleInfoListRequest) {
        List<RoleInfo> roleInfoList = roleInfoService.listByCompanyId(roleInfoListRequest.getCompanyInfoId());
        if (CollectionUtils.isEmpty(roleInfoList)) {
            return BaseResponse.SUCCESSFUL();
        }
        List<RoleInfoVO> roleInfoVOList = new ArrayList<>();
        KsBeanUtil.copyList(roleInfoList, roleInfoVOList);
        return BaseResponse.success(RoleInfoListResponse.builder().roleInfoVOList(roleInfoVOList).build());
    }

    /**
     * 根据角色ID查询角色信息
     *
     * @param request {@link RoleInfoQueryRequest}
     * @return 角色信息 {@link RoleInfoQueryResponse}
     */
    @Override
    public BaseResponse<RoleInfoQueryResponse> getRoleInfoById(@RequestBody @Valid RoleInfoQueryRequest request) {
        Optional<RoleInfo> roleInfoOptional = roleInfoService.getRoleInfoById(request.getRoleInfoId());
        if (!roleInfoOptional.isPresent()) {
            return BaseResponse.SUCCESSFUL();
        }
        RoleInfoVO roleInfoVO = new RoleInfoVO();
        RoleInfo roleInfo = roleInfoOptional.get();
        KsBeanUtil.copyPropertiesThird(roleInfo, roleInfoVO);
        RoleInfoQueryResponse response = new RoleInfoQueryResponse();
        response.setRoleInfoVO(roleInfoVO);
        return BaseResponse.success(response);
    }
}
