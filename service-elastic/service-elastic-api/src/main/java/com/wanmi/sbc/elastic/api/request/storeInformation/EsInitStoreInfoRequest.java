package com.wanmi.sbc.elastic.api.request.storeInformation;

import com.wanmi.sbc.customer.bean.vo.EsStoreInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author yangzhen
 * @Description // 商家店铺信息
 * @Date 18:30 2020/12/7
 * @Param
 * @return
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class EsInitStoreInfoRequest {


    @ApiModelProperty(value = "批量处理数据")
    private List<EsStoreInfoVo> lists;






}
