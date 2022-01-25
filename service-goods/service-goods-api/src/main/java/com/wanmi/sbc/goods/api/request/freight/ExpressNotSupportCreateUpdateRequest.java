package com.wanmi.sbc.goods.api.request.freight;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpressNotSupportCreateUpdateRequest {

    private Long id;
    //供应商id
    private Long supplierId;
    //不支持地区
    private String[] provinceId;
    //不支持地区名字
    private String[] provinceName;
    private String[] cityId;
    private String[] cityName;
}
