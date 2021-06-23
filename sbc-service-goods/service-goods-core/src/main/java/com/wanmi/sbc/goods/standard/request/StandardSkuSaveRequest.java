package com.wanmi.sbc.goods.standard.request;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.goods.standard.model.root.StandardSku;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 商品库Sku编辑请求
 * Created by daiyitian on 2017/3/24.
 */
@Data
public class StandardSkuSaveRequest extends BaseRequest {

    /**
     * 商品SKU信息
     */
    @NotNull
    private StandardSku goodsInfo;

}
