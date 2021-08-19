package com.wanmi.sbc.marketing.common.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@Data
public class MarketingStartRequest {

    /**
     * marketingId集合，逗号分割
     */
    @NotEmpty
    List<Long> marketingIds;



}
