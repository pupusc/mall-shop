package com.wanmi.sbc.setting.authority.service;

import com.google.common.base.Joiner;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.api.request.RoleMenuAuthSaveRequest;
import com.wanmi.sbc.setting.api.response.RoleMenuFuncIdsQueryResponse;
import com.wanmi.sbc.setting.authority.model.root.FunctionInfo;
import com.wanmi.sbc.setting.authority.model.root.MenuInfo;
import com.wanmi.sbc.setting.authority.model.root.RoleFunctionRela;
import com.wanmi.sbc.setting.authority.model.root.RoleMenuRela;
import com.wanmi.sbc.setting.authority.repository.FunctionRepository;
import com.wanmi.sbc.setting.authority.repository.MenuInfoRepository;
import com.wanmi.sbc.setting.authority.repository.RoleFunctionRelaRepository;
import com.wanmi.sbc.setting.authority.repository.RoleMenuRelaRepository;
import com.wanmi.sbc.setting.bean.enums.SystemAccount;
import com.wanmi.sbc.setting.bean.vo.AuthorityVO;
import com.wanmi.sbc.setting.bean.vo.MenuInfoVO;
import com.wanmi.sbc.setting.bean.vo.RoleInfoAndMenuInfoVO;
import com.wanmi.sbc.setting.redis.CacheKeyUtil;
import com.wanmi.sbc.setting.redis.RedisService;
import com.wanmi.sbc.setting.util.BeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色的菜单权限信息服务
 * Created by bail on 2017/12/28
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class RoleMenuService {

    @Autowired
    private RoleMenuRelaRepository roleMenuRelaRepository;

    @Autowired
    private RoleFunctionRelaRepository roleFunctionRelaRepository;

    @Autowired
    private MenuInfoRepository menuInfoRepository;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private RedisService redisService;

    /**
     * 查询角色拥有的菜单id和功能id信息
     * @param roleInfoId
     * @return
     */
    public RoleMenuFuncIdsQueryResponse queryRoleMenuFuncIds(Long roleInfoId) {
        RoleMenuFuncIdsQueryResponse response = new RoleMenuFuncIdsQueryResponse();
        // 1.查询角色拥有的菜单信息
        List<RoleMenuRela> roleMenuRelas = roleMenuRelaRepository.findByRoleInfoId(roleInfoId);
        if(CollectionUtils.isNotEmpty(roleMenuRelas)){
            response.setMenuIdList(roleMenuRelas.stream().map(roleMenuRela -> roleMenuRela.getMenuId()).collect(Collectors.toList()));
        }

        // 2.查询角色拥有的功能信息
        List<RoleFunctionRela> roleFunctionRelas = roleFunctionRelaRepository.findByRoleInfoId(roleInfoId);
        if(CollectionUtils.isNotEmpty(roleFunctionRelas)) {
            response.setFunctionIdList(roleFunctionRelas.stream().map(roleFunctionRela -> roleFunctionRela.getFunctionId()).collect(Collectors.toList()));
        }
        return response;
    }

    /**
     * 根据角色ID集合查询一级菜单权限集合
     * @param roleInfoIdList
     * @return
     */
    public List<RoleInfoAndMenuInfoVO> listRoleWithMenuNames(List<Long> roleInfoIdList) {
        List<RoleInfoAndMenuInfoVO> list = new ArrayList<>();
        List<String> menuNameList;
        String menuNames;
        RoleInfoAndMenuInfoVO roleInfoAndMenuInfoVO;
        for (Long roleInfoId : roleInfoIdList) {
            menuNameList = menuInfoRepository.findMenuNameByRoleInfoId(roleInfoId);
            menuNames = CollectionUtils.isNotEmpty(menuNameList) ? Joiner.on(",").join(menuNameList) : "";
            roleInfoAndMenuInfoVO = RoleInfoAndMenuInfoVO.builder().roleInfoId(roleInfoId).menuNames(menuNames).build();
            list.add(roleInfoAndMenuInfoVO);
        }
        return list;
    }

    /**
     * 查询角色拥有的菜单信息
     * @param roleInfoId
     * @return
     */
    public List<MenuInfoVO> queryRoleMenuInfoList(List<Long> roleInfoId) {
        List<MenuInfoVO> menuInfoResponseList = new ArrayList<>();
        // 1.查询角色拥有的菜单idList
        List<RoleMenuRela> roleMenuRelas = roleMenuRelaRepository.findByRoleInfoIdIn(roleInfoId);
        if(CollectionUtils.isNotEmpty(roleMenuRelas)){
            // 2.根据菜单idList查询菜单完整信息
            List<MenuInfo> menuInfoList = menuInfoRepository.findByMenuIdInAndDelFlagOrderBySort(roleMenuRelas.stream().map(roleMenuRela -> roleMenuRela.getMenuId()).collect(Collectors.toList()), DeleteFlag.NO);
            if(CollectionUtils.isNotEmpty(menuInfoList)){
                menuInfoResponseList = menuInfoList.stream().map(menuInfo -> BeanUtil.convertFromMenuInfo(menuInfo)).collect(Collectors.toList());
            }
        }

        return menuInfoResponseList;
    }

    /**
     * 查询所有的菜单信息
     * @return
     */
    public List<MenuInfoVO> queryAllRoleMenuInfoList(Platform systemTypeCd) {
        List<MenuInfoVO> menuInfoResponseList = new ArrayList<>();
        // 1.根据systemTypeCd查询所有菜单信息
        List<MenuInfo> menuInfoList = menuInfoRepository.findBySystemTypeCdAndDelFlagOrderBySort(systemTypeCd, DeleteFlag.NO);
        if(CollectionUtils.isNotEmpty(menuInfoList)){
            menuInfoResponseList = menuInfoList.stream().map(menuInfo -> BeanUtil.convertFromMenuInfo(menuInfo)).collect(Collectors.toList());
        }

        return menuInfoResponseList;
    }

    /**
     * 查询某角色的所有权限
     * @param roleInfoId
     * @return
     */
    public List<AuthorityVO> hasAuthorityList(Long roleInfoId){
        List<Object> objectList = roleFunctionRelaRepository.hasAuthorityList(roleInfoId);
        if(objectList!=null && objectList.size()>0){
            return objectList.stream().map(obj -> AuthorityVO.builder().authorityUrl((String)((Object[])obj)[0]).requestType((String)((Object[])obj)[1]).build()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 根据角色/功能名称list查询拥有的功能名称list
     * @param roleInfoId
     * @return
     */
    public List<String> hasFunctionList(Long roleInfoId, List<String> authorityNames){
        List<Object> objectList = roleFunctionRelaRepository.hasFunctionList(roleInfoId,authorityNames);
        if(objectList!=null && objectList.size()>0){
            return objectList.stream().map(obj -> obj.toString()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 根据角色查询拥有的功能名称list
     * @param roleInfoId
     * @return
     */
    public List<String> queryFunctionsByRoleId(Long roleInfoId){
        List<Object> objectList = roleFunctionRelaRepository.queryFunctionsByRoleId(roleInfoId);
        if(CollectionUtils.isNotEmpty(objectList)){
            return objectList.stream().map(obj -> obj.toString()).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    /**
     * 根据平台类别查询所有的功能名称list
     * @param systemTypeCd
     * @return
     */
    public List<String> queryFunctionsByPlatform(Platform systemTypeCd){
        List<FunctionInfo> functionInfoList= functionRepository.findBySystemTypeCdAndDelFlagOrderBySort(systemTypeCd, DeleteFlag.NO);
        if(CollectionUtils.isNotEmpty(functionInfoList)){
            return functionInfoList.stream().map(functionInfo -> functionInfo.getFunctionName()).distinct().collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * 保存角色拥有的菜单,功能,权限
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveRoleMenuAuth(RoleMenuAuthSaveRequest roleMenuFuncRequest) {
        Long roleInfoId = roleMenuFuncRequest.getRoleInfoId();
        if(roleMenuFuncRequest.getRoleInfoId() == null){
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        // 1.删除该角色对应的菜单,功能,权限信息
        roleMenuRelaRepository.deleteByRoleInfoId(roleInfoId);
        roleFunctionRelaRepository.deleteByRoleInfoId(roleInfoId);

        // 2.插入该角色新的菜单信息
        if(CollectionUtils.isNotEmpty(roleMenuFuncRequest.getMenuIdList())){
            roleMenuRelaRepository.saveAll(
                    roleMenuFuncRequest.getMenuIdList().stream().map(menuId ->
                            RoleMenuRela.builder().menuId(menuId).roleInfoId(roleInfoId).build()
                    ).collect(Collectors.toList())
            );
        }

        // 3.插入该角色的功能信息
        if(CollectionUtils.isNotEmpty(roleMenuFuncRequest.getFunctionIdList())){
            roleFunctionRelaRepository.saveAll(
                    roleMenuFuncRequest.getFunctionIdList().stream().map(funcId ->
                            RoleFunctionRela.builder().functionId(funcId).roleInfoId(roleInfoId).build()
                    ).collect(Collectors.toList())
            );
        }
    }

    /**
     * 根据roleId查询对应角色下所有的功能
     * @param roleId
     * @param systemTypeCd
     * @return
     */
    public List<String> queryFunctionsByRoleId(String roleId,Platform systemTypeCd){
        List<String> functionList  = redisService.getList(CacheKeyUtil.getRoleFunctionKey(roleId),String.class);
        if (!CollectionUtils.isEmpty(functionList)){
            return functionList;
        }
        // system账号特权 以及 店铺主账号查询对应平台的所有权限
        if (SystemAccount.SYSTEM.getDesc().equals(roleId)){
            functionList = this.queryFunctionsByPlatform(systemTypeCd);
        } else {
            functionList = this.queryFunctionsByRoleId(Long.parseLong(roleId));
        }
        return functionList;
    }

}
