package com.wanmi.sbc.goods.api.request.supplier;

import lombok.Data;

@Data
public class SecondLevelSupplierCreateUpdateRequest {

    private Long id;
    //名字
    private String name;
    //编码
    private String code;
}
