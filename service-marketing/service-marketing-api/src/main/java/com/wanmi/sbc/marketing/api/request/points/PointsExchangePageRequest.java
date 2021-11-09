package com.wanmi.sbc.marketing.api.request.points;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PointsExchangePageRequest extends BaseQueryRequest {


    private static final long serialVersionUID = 3761902374831057578L;


    private List<String> skuList;

    private List<String> spuList;

    private String goodsName;

    private Integer status;



}
