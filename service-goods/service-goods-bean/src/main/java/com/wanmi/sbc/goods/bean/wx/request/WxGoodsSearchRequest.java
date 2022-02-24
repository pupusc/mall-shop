package com.wanmi.sbc.goods.bean.wx.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WxGoodsSearchRequest {

    private String goodsName;
    private Integer saleStatus;
    private Integer auditStatus;
    private List<String> goodsIds;

    private Integer pageNum = 0;
    private Integer pageSize = 10;
}
