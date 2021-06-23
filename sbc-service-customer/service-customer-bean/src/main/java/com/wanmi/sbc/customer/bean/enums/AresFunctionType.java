package com.wanmi.sbc.customer.bean.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * <p>用于Ares数据处理的方法枚举</p>
 * Created by of628-wenzhi on 2018-09-20-下午4:06.
 */
@ApiEnum
public enum AresFunctionType {
    @ApiEnumProperty("新增会员等级")
    ADD_CUSTOMER_LEVEL,
    @ApiEnumProperty("编辑会员等级")
    EDIT_CUSTOMER_LEVEL,
    @ApiEnumProperty("删除会员等级")
    DEL_CUSTOMER_LEVEL,
    @ApiEnumProperty("注册会员")
    REGISTER_CUSTOMER,
    @ApiEnumProperty("新增会员")
    ADD_CUSTOMER,
    @ApiEnumProperty("编辑会员")
    EDIT_CUSTOMER,
    @ApiEnumProperty("修改会员绑定手机号")
    EDIT_CUSTOMER_PHONE,
    @ApiEnumProperty("修改会员审核状态")
    EDIT_CUSTOMER_CHECK_STATE,
    @ApiEnumProperty("单个或批量删除会员")
    DEL_CUSTOMER,
    @ApiEnumProperty("新增店铺会员(会员等级)关系")
    ADD_STORE_CUSTOMER_RELA,
    @ApiEnumProperty("编辑店铺会员(会员等级)关系")
    EDIT_STORE_CUSTOMER_RELA,
    @ApiEnumProperty("删除店铺会员(会员等级)关系")
    DEL_STORE_CUSTOMER_RELA,
    @ApiEnumProperty("新增业务员")
    ADD_EMPLOYEE,
    @ApiEnumProperty("编辑业务员")
    EDIT_EMPLOYEE,
    @ApiEnumProperty("删除业务员")
    DEL_EMPLOYEE,
    @ApiEnumProperty("新增店铺")
    ADD_STORE,
    @ApiEnumProperty("编辑店铺")
    EDIT_STORE;

    @JsonCreator
    public AresFunctionType fromValue(String name) {
        return AresFunctionType.valueOf(name);
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
