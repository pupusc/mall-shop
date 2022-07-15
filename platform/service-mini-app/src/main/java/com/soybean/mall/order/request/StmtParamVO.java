package com.soybean.mall.order.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Liang Jun
 * @date 2022-06-20 13:58:00
 */
@Data
public class StmtParamVO {
    /**
     * 订单营销信息快照
     */
    @NotEmpty
    private List<SettlementParamVO$Marketing> marketings = new ArrayList<>();
}
