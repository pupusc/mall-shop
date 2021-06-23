package com.wanmi.sbc.goods.api.response.standard;

import com.wanmi.sbc.goods.bean.vo.StandardSkuVO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品库Sku编辑响应
 * Created by daiyitian on 2017/3/24.
 */
@ApiModel
@Data
public class StandardSkuModifyResponse extends StandardSkuVO implements Serializable {

    private static final long serialVersionUID = -2137751302275895389L;
}
