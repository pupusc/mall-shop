package com.wanmi.sbc.goods.api.response.presellsale;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.goods.bean.vo.PresellSaleVO;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
import java.util.Map;



@Data
public class PresellSalePageResponse {


    /**
     * 商品分页数据
     */
    private MicroServicePage<PresellSaleVO> presellSalesPage ;
}
