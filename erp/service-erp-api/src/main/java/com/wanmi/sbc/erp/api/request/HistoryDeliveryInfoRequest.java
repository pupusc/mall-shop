package com.wanmi.sbc.erp.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author huqingjie
 * @date 2021年04月29日
 * @description ERP发货单查询接口
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDeliveryInfoRequest {
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
