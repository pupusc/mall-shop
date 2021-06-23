package com.wanmi.sbc.goods.api.response.storegoodstab;

import com.wanmi.sbc.goods.bean.vo.StoreGoodsTabVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午11:14 2018/12/13
 * @Description:
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreGoodsTabAddResponse {

    @ApiModelProperty(value = "店铺商品模板")
    private StoreGoodsTabVO storeGoodsTab;

}
