package com.wanmi.sbc.goods.api.request.storegoodstab;

import com.wanmi.sbc.goods.bean.dto.StoreGoodsTabDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 下午5:47 2018/12/21
 * @Description:
 */
@ApiModel
@Data
public class StoreGoodsTabEditSortRequest implements Serializable {

    private static final long serialVersionUID = -5757120441483448508L;

    @ApiModelProperty(value = "店铺商品模板")
    private List<StoreGoodsTabDTO> tabList;

}
