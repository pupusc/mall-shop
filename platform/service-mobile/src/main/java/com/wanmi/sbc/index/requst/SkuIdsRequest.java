package com.wanmi.sbc.index.requst;


import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class SkuIdsRequest implements Serializable {

    private static final long serialVersionUID = 3228778527828317959L;

    /**
     * skuId集合
     */
    private List<String> skuIds;



}
