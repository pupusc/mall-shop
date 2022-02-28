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

    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 销售状态 0-不可售 1-可售
     */
    private Integer saleStatus;
    /**
     * 审核状态 0-未审核 1-审核中 2-审核失败 3-审核成功
     */
    private Integer auditStatus;

    private List<String> goodsIds;

    private Integer pageNum = 0;
    private Integer pageSize = 10;
}
