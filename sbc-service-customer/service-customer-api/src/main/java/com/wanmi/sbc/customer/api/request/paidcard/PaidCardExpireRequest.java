package com.wanmi.sbc.customer.api.request.paidcard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaidCardExpireRequest {
    
    private String phone;
    
    private String year;
    
    private String day;
    
    private String month;
    
    private String paidCardName;

    private String customerId;
    
}
