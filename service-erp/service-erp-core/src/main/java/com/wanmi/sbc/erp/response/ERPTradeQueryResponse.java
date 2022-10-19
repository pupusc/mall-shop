package com.wanmi.sbc.erp.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wanmi.sbc.erp.entity.ERPTrade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author wugongjiang
 * @className ERPTradeQueryResponse
 * @description TODO
 * @date 2021/6/30 下午2:26
 **/
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ERPTradeQueryResponse extends ERPBaseResponse{

    /**
     * 订单列表
     */
    @JsonProperty("orders")
    private List<ERPTrade> orders;

}
