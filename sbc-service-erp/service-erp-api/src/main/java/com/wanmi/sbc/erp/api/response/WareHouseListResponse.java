package com.wanmi.sbc.erp.api.response;

import com.sbc.wanmi.erp.bean.vo.ERPWareHouseVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @program: sbc-background
 * @description: erp商品仓库列表
 * @author: 0F3685-wugongjiang
 * @create: 2021-02-07 15:39
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WareHouseListResponse implements Serializable {

    /**
     * 仓库列表集合
     */
    @ApiModelProperty(value = "仓库列表集合")
    private List<ERPWareHouseVO> wareHouseVOList;
}
