package com.wanmi.sbc.crm.customgroup;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.crm.api.provider.customgroup.CustomGroupProvider;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupListRequest;
import com.wanmi.sbc.crm.api.request.customgroup.CustomGroupRequest;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-11-15
 * \* Time: 15:33
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Api(description = "自定义人群管理", tags = "CustomGroupController")
@RestController
@RequestMapping(value = "/crm/customgroup")
public class CustomGroupController {

    @Autowired
    private CustomGroupProvider customGroupProvider;
    @Autowired
    private CommonUtil commonUtil;

    @ApiOperation(value = "新增自定义人群配置")
    @PostMapping("/add")
    BaseResponse add(@RequestBody @Valid CustomGroupRequest request){
        request.setCreateTime(LocalDateTime.now());
        request.setCreatePerson(commonUtil.getOperatorId());
        return this.customGroupProvider.add(request);
    }

    /**
     * 修改自定义人群
     * @param request
     * @return
     */
    @ApiOperation(value = "修改自定义人群配置")
    @PutMapping("/modify")
    BaseResponse modify(@RequestBody @Valid CustomGroupRequest request){
        request.setUpdateTime(LocalDateTime.now());
        request.setUpdatePerson(commonUtil.getOperatorId());
        return this.customGroupProvider.modify(request);
    }

    /**
     * 根据id删除自定义人群
     * @param id
     * @return
     */
    @ApiOperation(value = "删除自定义人群配置")
    @DeleteMapping("/{id}")
    BaseResponse deleteById(@PathVariable Long id) {
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        CustomGroupRequest request = CustomGroupRequest.builder().id(id).build();
        return this.customGroupProvider.deleteById(request);
    }

    /**
     * 根据id获取自定人群
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取自定义人群")
    @GetMapping("/{id}")
    BaseResponse queryById(@PathVariable Long id){
        if (id == null) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        CustomGroupRequest request = CustomGroupRequest.builder().id(id).build();
        return this.customGroupProvider.queryById(request);
    }

    @ApiOperation(value = "查询定义人群列表")
    @PostMapping("/page")
    BaseResponse list(@RequestBody @Valid CustomGroupListRequest request){
        return this.customGroupProvider.list(request);
    }

}
