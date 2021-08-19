package com.wanmi.sbc.erp.api.response;

import com.sbc.wanmi.erp.bean.vo.ERPGoodsInfoVO;
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
 * @description: ERP商品库存同步Response
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-28 18:07
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncGoodsInfoResponse implements Serializable {


    /**
     * erp商品库存信息
     */
    @ApiModelProperty(value = "erp商品库存信息")
    private List<ERPGoodsInfoVO> erpGoodsInfoVOList;
}
