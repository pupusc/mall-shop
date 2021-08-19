package com.wanmi.sbc.erp.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * @program: sbc-background
 * @description: ERP仓库列表查询参数
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-27 22:10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ERPWareHouseQueryRequest extends ERPBaseRequest{

    /**
     * 页码
     */
    @JsonProperty("page_no")
    private String pageNo;

    /**
     *
     */
    @JsonProperty("page_size")
    private String pageSize;

    /**
     * 是否附带返回已删除的仓库数据(true：返回
     * false:不返回
     * 默认false)
     */
    @JsonProperty("has_del_data")
    private boolean hasDelData;
}
