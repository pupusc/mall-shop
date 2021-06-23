package com.wanmi.sbc.setting.authority.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.Platform;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.setting.authority.model.root.Authority;
import com.wanmi.sbc.setting.authority.model.root.FunctionInfo;
import com.wanmi.sbc.setting.authority.model.root.MenuInfo;
import com.wanmi.sbc.setting.authority.repository.AuthorityRepository;
import com.wanmi.sbc.setting.authority.repository.FunctionRepository;
import com.wanmi.sbc.setting.authority.repository.MenuInfoRepository;
import com.wanmi.sbc.setting.bean.vo.MenuInfoVO;
import com.wanmi.sbc.setting.util.BeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单信息服务
 * Created by bail on 2017/12/28
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class MenuInfoService {


    @Autowired
    private MenuInfoRepository menuInfoRepository;

    @Autowired
    private FunctionRepository functionRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    /**
     * 查询系统所有的菜单,功能信息
     * @param systemTypeCd
     * @return
     */
    public List<MenuInfoVO> queryMenuAndFunctionList(Platform systemTypeCd) {
        // 1.查询菜单信息
        List<MenuInfo> menuInfoList = menuInfoRepository.findBySystemTypeCdAndDelFlagOrderBySort(systemTypeCd, DeleteFlag.NO);
        List<MenuInfoVO> voList = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(menuInfoList)){
            // 2.转换成菜单返回对象类型
            voList = menuInfoList.stream().map(menuInfo -> BeanUtil.convertFromMenuInfo(menuInfo)).collect(Collectors.toList());
        }

        // 3.查询功能信息
        List<FunctionInfo> functionInfoList = functionRepository.findBySystemTypeCdAndDelFlagOrderBySort(systemTypeCd, DeleteFlag.NO);
        if(CollectionUtils.isNotEmpty(functionInfoList)){
            // 4.追加功能信息到返回对象List中
            voList.addAll(functionInfoList.stream().map(functionInfo -> BeanUtil.convertFromFunction(functionInfo)).collect(Collectors.toList()));
        }

        return voList;
    }

    /**
     * 查询系统所有的菜单,功能,权限信息
     * @param systemTypeCd
     * @return
     */
    public List<MenuInfoVO> queryMenuAndAuthorityList(Platform systemTypeCd) {
        // 1.查询菜单,功能信息
        List<MenuInfoVO> voList = queryMenuAndFunctionList(systemTypeCd);
        // 2.查询权限信息
        List<Authority> authorityList = authorityRepository.findBySystemTypeCdAndDelFlagOrderBySort(systemTypeCd, DeleteFlag.NO);
        if(CollectionUtils.isNotEmpty(authorityList)){
            // 3.追加权限信息到返回对象List中
            voList.addAll(authorityList.stream().map(authority -> BeanUtil.convertFromAuthority(authority)).collect(Collectors.toList()));
        }
        return voList;
    }

    /**
     * 插入菜单
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertMenu(MenuInfo menuInfo) {
        if(StringUtils.isEmpty(menuInfo.getParentMenuId())){
            // 父级id必须存在,否则无法插入菜单
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        // 1.设置菜单的层级
        if(!"0".equals(menuInfo.getParentMenuId())){
            MenuInfo pareMenu = menuInfoRepository.findById(menuInfo.getParentMenuId()).orElse(null);
            if(pareMenu == null || pareMenu.getMenuGrade() == null){
                // 若父级菜单不存在,则无法插入菜单
                throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
            }
            menuInfo.setMenuGrade(pareMenu.getMenuGrade() + 1);
        }else{
            menuInfo.setMenuGrade(1);
        }

        // 2.设置其他属性(创建时间,删除标识)
        menuInfo.setCreateTime(LocalDateTime.now());
        menuInfo.setDelFlag(DeleteFlag.NO);
        menuInfoRepository.save(menuInfo);
    }

    /**
     * 更新菜单
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(MenuInfo menuInfo) {
        if(StringUtils.isEmpty(menuInfo.getMenuId())){
            // id必须存在,否则无法更新菜单
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        MenuInfo oldMenu = menuInfoRepository.findById(menuInfo.getMenuId()).orElse(null);
        if(oldMenu == null){
            // 若菜单不存在,则无法更新菜单
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        // 将oldMenu的信息赋值到menuInfo中,稍后整体更新
        menuInfo.setParentMenuId(oldMenu.getParentMenuId());
        menuInfo.setMenuGrade(oldMenu.getMenuGrade());
        menuInfo.setCreateTime(oldMenu.getCreateTime());
        menuInfo.setDelFlag(oldMenu.getDelFlag());

        menuInfoRepository.save(menuInfo);
    }

    /**
     * 删除菜单
     */
    @Transactional
    public void deleteMenu(String menuId) {
        menuInfoRepository.deleteMenuInfo(menuId, DeleteFlag.YES);
    }

    /**
     * 插入功能
     */
    @Transactional
    public void insertFunction(FunctionInfo functionInfo) {
        if(StringUtils.isEmpty(functionInfo.getMenuId())){
            // 菜单id必须存在,否则无法插入功能
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        // 设置其他属性(创建时间,删除标识)
        functionInfo.setCreateTime(LocalDateTime.now());
        functionInfo.setDelFlag(DeleteFlag.NO);
        functionRepository.save(functionInfo);
    }

    /**
     * 更新功能
     */
    @Transactional
    public void updateFunction(FunctionInfo functionInfo) {
        if(StringUtils.isEmpty(functionInfo.getFunctionId())){
            // id必须存在,否则无法更新
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        FunctionInfo oldFunc = functionRepository.findById(functionInfo.getFunctionId()).orElse(null);
        if(oldFunc == null){
            // 若不存在,则无法更新
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        // 将oldFunc的信息赋值到functionInfo中,稍后整体更新
        functionInfo.setMenuId(oldFunc.getMenuId());
        functionInfo.setCreateTime(oldFunc.getCreateTime());
        functionInfo.setDelFlag(oldFunc.getDelFlag());
        functionRepository.save(functionInfo);
    }

    /**
     * 删除功能
     */
    @Transactional
    public void deleteFunction(String functionId) {
        functionRepository.deleteFunctionInfo(functionId, DeleteFlag.YES);
    }

    /**
     * 插入权限
     */
    @Transactional
    public void insertAuthority(Authority authority) {
        if(StringUtils.isEmpty(authority.getFunctionId())){
            // 功能id必须存在,否则无法插入权限
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        // 设置其他属性(创建时间,删除标识)
        authority.setCreateTime(LocalDateTime.now());
        authority.setDelFlag(DeleteFlag.NO);
        authorityRepository.save(authority);
    }

    /**
     * 更新权限
     */
    @Transactional
    public void updateAuthority(Authority authority) {
        if(StringUtils.isEmpty(authority.getAuthorityId())){
            // id必须存在,否则无法更新
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        Authority oldAuth = authorityRepository.findById(authority.getAuthorityId()).orElse(null);
        if(oldAuth == null){
            // 若不存在,则无法更新
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }

        // 将oldAuth的信息赋值到authority中,稍后整体更新
        authority.setFunctionId(oldAuth.getFunctionId());
        authority.setCreateTime(oldAuth.getCreateTime());
        authority.setDelFlag(oldAuth.getDelFlag());
        authorityRepository.save(authority);
    }

    /**
     * 删除权限
     */
    @Transactional
    public void deleteAuthority(String authorityId) {
        authorityRepository.deleteAuthority(authorityId, DeleteFlag.YES);
    }

}
