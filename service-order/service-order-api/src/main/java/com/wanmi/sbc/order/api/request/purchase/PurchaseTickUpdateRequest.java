package com.wanmi.sbc.order.api.request.purchase;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class PurchaseTickUpdateRequest {
    @NotBlank
    private String customerId;
    private List<String> skuIds;
}
