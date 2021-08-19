package com.wanmi.sbc.goods.presellsale.response;

import com.wanmi.sbc.goods.presellsale.model.root.PresellSale;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class PresellSaleQueryResponse {
    /**
     * 商品分页数据
     */
    private Page<PresellSale> presellSalesPage = new PageImpl<>(new ArrayList<>());

    /**
     * 预售活动列表对应的下
     */
    private Map<String, TotalNum> countNumList;

}
