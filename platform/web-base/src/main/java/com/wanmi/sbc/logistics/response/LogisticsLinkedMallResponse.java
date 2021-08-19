package com.wanmi.sbc.logistics.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class LogisticsLinkedMallResponse {

    @ApiModelProperty("物流信息")
    private List<Map<String,String>>  logisticsDetailList;

    @ApiModelProperty("物流公司标准编码")
    private String logisticStandardCode;

    @ApiModelProperty("物流号")
    private String logisticNo;

    @ApiModelProperty("物流公司名称")
    private String logisticCompanyName;

    @ApiModelProperty("发货日期")
    private String deliveryTime;
}
