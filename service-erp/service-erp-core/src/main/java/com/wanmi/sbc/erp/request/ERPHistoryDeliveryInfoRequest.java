package com.wanmi.sbc.erp.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 * @author huqingjie
 * @description: ERP发货单查询接口
 * @date 2021年04月22日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class ERPHistoryDeliveryInfoRequest extends ERPBaseRequest{

    /**
     * 商城订单号
     */
    @JsonProperty("outer_code")
    private String outerCode;

    /**
     * 发货时间开始段
     */
    @JsonProperty("start_delivery_date")
    private String startDeliveryDate;

    /**
     * 发货时间结束段
     */
    @JsonProperty("end_delivery_date")
    private String endDeliveryDate;

    @JsonProperty("page_size")
    private int pageSize;

    @JsonProperty("page_no")
    private int pageNum;
}
