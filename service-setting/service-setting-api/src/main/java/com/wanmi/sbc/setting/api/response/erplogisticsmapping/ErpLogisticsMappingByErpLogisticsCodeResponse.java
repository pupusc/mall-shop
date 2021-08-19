package com.wanmi.sbc.setting.api.response.erplogisticsmapping;


import com.wanmi.sbc.setting.bean.vo.ErpLogisticsMappingVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>erp系统物流编码映射VO</p>
 * @author weiwenhao
 * @date 2021-04-03 13:38:51
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpLogisticsMappingByErpLogisticsCodeResponse {

    /**
     * 物流编码映射实体
     */
    @ApiModelProperty(value = "物流编码映射实体")
    private ErpLogisticsMappingVO erpLogisticsMappingVO;



}
