package com.wanmi.sbc.order.bean.enums;

import com.wanmi.sbc.common.annotation.ApiEnum;
import com.wanmi.sbc.common.annotation.ApiEnumProperty;

/**
 * 发货人 类型  (0商家，1供应商)
 */
@ApiEnum(dataType = "java.lang.String")
public enum ShipperType {

    @ApiEnumProperty("0: 商家")
    SUPPLIER("SUPPLIER","商家"),

    @ApiEnumProperty("1: 已发货")
    PROVIDER("PROVIDER","供应商");

    private String typeId;

    private String description;

    ShipperType(String typeId, String description) {
        this.typeId = typeId;
        this.description = description;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getDescription() {
        return description;
    }
}
