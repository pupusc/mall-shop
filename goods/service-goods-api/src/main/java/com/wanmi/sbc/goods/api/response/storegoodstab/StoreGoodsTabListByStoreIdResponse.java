package com.wanmi.sbc.goods.api.response.storegoodstab;

import com.wanmi.sbc.goods.bean.vo.StoreGoodsTabVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:33 2018/12/13
 * @Description:
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreGoodsTabListByStoreIdResponse {

    /**
     * 商品模板列表
     */
    @ApiModelProperty(value = "商品模板列表")
    private List<StoreGoodsTabVO> storeGoodsTabs;

}
