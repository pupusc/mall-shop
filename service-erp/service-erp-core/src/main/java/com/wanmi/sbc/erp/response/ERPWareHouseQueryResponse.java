package com.wanmi.sbc.erp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanmi.sbc.erp.entity.ERPWareHouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @program: sbc-background
 * @description: ERP仓库列表接口返回报文对象
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-28 10:50
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPWareHouseQueryResponse extends ERPBaseResponse{


    /**
     * 仓库列表
     */
    @JsonProperty("warehouses")
    private List<ERPWareHouse> warehouses;

    /**
     * 总记录数
     */
    @JsonProperty("total")
    private int total;

}
