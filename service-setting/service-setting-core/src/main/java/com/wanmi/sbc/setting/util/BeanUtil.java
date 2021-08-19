package com.wanmi.sbc.setting.util;

import com.wanmi.sbc.setting.authority.model.root.Authority;
import com.wanmi.sbc.setting.authority.model.root.FunctionInfo;
import com.wanmi.sbc.setting.authority.model.root.MenuInfo;
import com.wanmi.sbc.setting.bean.vo.MenuInfoVO;

public class BeanUtil {
    /**
     * 从菜单信息转换成返回对象
     */
    public static MenuInfoVO convertFromMenuInfo(MenuInfo menuInfo){
        MenuInfoVO menuInfoVO = new MenuInfoVO();

        menuInfoVO.setId("menu_".concat(menuInfo.getMenuId()));
        menuInfoVO.setPid("menu_".concat(menuInfo.getParentMenuId()));
        menuInfoVO.setRealId(menuInfo.getMenuId());
        menuInfoVO.setTitle(menuInfo.getMenuName());
        menuInfoVO.setGrade(menuInfo.getMenuGrade());
        menuInfoVO.setSort(menuInfo.getSort());
        menuInfoVO.setUrl(menuInfo.getMenuUrl());

        menuInfoVO.setIcon(menuInfo.getMenuIcon());
        return menuInfoVO;
    }

    /**
     * 从功能信息转换成返回对象
     */
    public static MenuInfoVO convertFromFunction(FunctionInfo functionInfo){
        MenuInfoVO menuInfoVO = new MenuInfoVO();

        menuInfoVO.setId("func_".concat(functionInfo.getFunctionId()));
        menuInfoVO.setPid("menu_".concat(functionInfo.getMenuId()));
        menuInfoVO.setRealId(functionInfo.getFunctionId());
        menuInfoVO.setTitle(functionInfo.getFunctionTitle());
        menuInfoVO.setGrade(888);//固定为888
        menuInfoVO.setSort(functionInfo.getSort());

        menuInfoVO.setAuthNm(functionInfo.getFunctionName());
        menuInfoVO.setAuthRemark(functionInfo.getRemark());
        return menuInfoVO;
    }

    /**
     * 从权限信息转换成返回对象
     */
    public static MenuInfoVO convertFromAuthority(Authority authority){
        MenuInfoVO menuInfoVO = new MenuInfoVO();

        menuInfoVO.setId("auth_".concat(authority.getAuthorityId()));
        menuInfoVO.setPid("func_".concat(authority.getFunctionId()));
        menuInfoVO.setRealId(authority.getAuthorityId());
        menuInfoVO.setTitle(authority.getAuthorityTitle());
        menuInfoVO.setGrade(999);//固定为999
        menuInfoVO.setSort(authority.getSort());
        menuInfoVO.setUrl(authority.getAuthorityUrl());

        menuInfoVO.setAuthNm(authority.getAuthorityName());
        menuInfoVO.setReqType(authority.getRequestType());
        menuInfoVO.setAuthRemark(authority.getRemark());
        return menuInfoVO;
    }
}
