package com.wanmi.sbc.windows.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class MarketingRequest implements Serializable {
    List<Long> marketingIdList ;
}
