package com.wanmi.sbc.setting.redis;

import com.wanmi.sbc.common.enums.SpecialSymbols;
import com.wanmi.sbc.common.redis.CacheKeyConstant;

public class CacheKeyUtil {

    /**
     * 获得用户下的employee的key
     *
     * @param userId
     * @return
     */
    public final static String getUserEmployeeKey(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(CacheKeyConstant.USER_EMPLOYEE).append(SpecialSymbols.COLON.toValue())
                .append(userId);
        return sb.toString();
    }

    /**
     * 获得角色下的所有功能的key
     *
     * @param roleId
     * @return
     */
    public final static String getRoleFunctionKey( String roleId) {
        StringBuilder sb = new StringBuilder();
        sb.append(CacheKeyConstant.ROLE_FUNCTION).append(SpecialSymbols.COLON.toValue())
                .append(roleId);
        return sb.toString();
    }

    /**
     * 获得用户下的role的key
     *
     * @param userId
     * @return
     */
    public final static String getUserRoleKey(String userId) {
        StringBuilder sb = new StringBuilder();
        sb.append(CacheKeyConstant.USER_EMPLOYEE).append(SpecialSymbols.COLON.toValue())
                .append(userId);
        return sb.toString();
    }
}