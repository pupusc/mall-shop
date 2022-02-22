package com.wanmi.sbc.goods.bean.wx.request;

import lombok.Data;

import java.util.List;

@Data
public class WxGoodsSearchRequest {

    private String goodsName;
    private Integer saleStatus;
    private Integer auditStatus;
    private List<String> goodsIds;

    private Integer pageNum = 0;
    private Integer pageSize = 10;
}
