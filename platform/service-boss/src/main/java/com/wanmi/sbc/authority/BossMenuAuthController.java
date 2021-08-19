package com.wanmi.sbc.authority;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.setting.api.provider.MenuInfoProvider;
import com.wanmi.sbc.setting.api.provider.MenuInfoQueryProvider;
import com.wanmi.sbc.setting.api.request.*;
import com.wanmi.sbc.setting.bean.vo.MenuInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单权限管理Controller
 * Author: bail
 * Time: 2017/01/03
 */
@RestController
@RequestMapping("/menuAuth")
@Api(tags = "BossMenuAuthController", description = "S2B 平台端-菜单权限管理API")
public class BossMenuAuthController {

    @Autowired
    private MenuInfoProvider menuInfoProvider;

    @Autowired
    private MenuInfoQueryProvider menuInfoQueryProvider;


    /**
     * 查询所有的菜单,功能信息
     */
    @ApiOperation(value = "查询菜单及功能列表")
    @GetMapping(value = "/func")
    public BaseResponse<List<MenuInfoVO>> getFunc(){
        MenuAndFunctionListRequest request = new MenuAndFunctionListRequest();
        request.setSystemTypeCd(Platform.PLATFORM);

        return BaseResponse.success(menuInfoQueryProvider.listMenuAndFunction(request).getContext().getMenuInfoVOList());
    }

    /**
     * 查询所有的菜单,功能,权限信息
     */
    @ApiOperation(value = "查询菜单及权限列表")
    @ApiImplicitParam(paramType = "path", dataType = "String", name = "currPlatform", value = "平台类型 3 商家端 4 平台端",
            required = true)
    @GetMapping(value = "/{currPlatform}")
    public BaseResponse<List<MenuInfoVO>> get(@PathVariable String currPlatform){
        MenuAndAuthorityListRequest request = new MenuAndAuthorityListRequest();
        request.setSystemTypeCd(Platform.forValue(currPlatform));

        return BaseResponse.success(menuInfoQueryProvider.listMenuAndAuthority(request).getContext().getMenuInfoVOList());
    }

    /**
     * 添加菜单
     */
    @ApiOperation(value = "添加菜单")
    @PostMapping(value = "/menu")
    public BaseResponse addMenu(@RequestBody MenuAddRequest menuInfo){
        menuInfoProvider.addMenuInfo(menuInfo);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改菜单
     */
    @ApiOperation(value = "修改菜单")
    @PutMapping(value = "/menu")
    public BaseResponse updateMenu(@RequestBody MenuModifyRequest menuInfo){
        menuInfo.setDelFlag(DeleteFlag.NO);
        menuInfoProvider.modifyMenuInfo(menuInfo);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除菜单
     */
    @ApiOperation(value = "删除菜单")
    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "menuId", value = "菜单id", required = true)
    @DeleteMapping(value = "/menu/{menuId}")
    public BaseResponse deleteMenu(@PathVariable String menuId){
        MenuDeleteRequest request = new MenuDeleteRequest();
        request.setMenuId(menuId);

        menuInfoProvider.deleteMenuInfo(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 添加功能
     */
    @ApiOperation(value = "添加功能")
    @PostMapping(value = "/func")
    public BaseResponse addFunc(@RequestBody FunctionInfoAddRequest functionInfo){
        menuInfoProvider.addFunction(functionInfo);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改功能
     */
    @ApiOperation(value = "修改功能")
    @PutMapping(value = "/func")
    public BaseResponse updateFunc(@RequestBody FunctionModifyRequest functionInfo){
        functionInfo.setDelFlag(DeleteFlag.NO);
        menuInfoProvider.modifyFunction(functionInfo);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除功能
     */
    @ApiOperation(value = "删除功能")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "funcId", value = "功能id", required = true)
    @DeleteMapping(value = "/func/{funcId}")
    public BaseResponse deleteFunc(@PathVariable String funcId){
        FunctionDeleteRequest request = new FunctionDeleteRequest();
        request.setFunctionId(funcId);

        menuInfoProvider.deleteFunction(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 添加权限
     */
    @ApiOperation(value = "添加权限")
    @PostMapping(value = "/auth")
    public BaseResponse addAuth(@RequestBody AuthorityAddRequest authority){
        menuInfoProvider.addAuthority(authority);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 修改权限
     */
    @ApiOperation(value = "修改权限")
    @PutMapping(value = "/auth")
    public BaseResponse updateAuth(@RequestBody AuthorityModifyRequest authority){
        authority.setDelFlag(DeleteFlag.NO);
        menuInfoProvider.modifyAuthority(authority);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 删除权限
     */
    @ApiOperation(value = "删除权限")
    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "authId", value = "权限id", required = true)
    @DeleteMapping(value = "/auth/{authId}")
    public BaseResponse deleteAuth(@PathVariable String authId){
        AuthorityDeleteRequest request = new AuthorityDeleteRequest();
        request.setAuthorityId(authId);

        menuInfoProvider.deleteAuthority(request);
        return BaseResponse.SUCCESSFUL();
    }
}
