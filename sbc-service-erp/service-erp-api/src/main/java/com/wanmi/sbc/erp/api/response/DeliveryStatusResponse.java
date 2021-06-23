package com.wanmi.sbc.erp.api.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.sbc.wanmi.erp.bean.enums.DeliveryStatus;
import com.sbc.wanmi.erp.bean.vo.DeliveryInfoVO;
import com.wanmi.sbc.common.util.CustomLocalDateTimeDeserializer;
import com.wanmi.sbc.common.util.CustomLocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @program: sbc-background
 * @description: 发货单查询结果
 * @author: 0F3685-wugongjiang
 * @create: 2021-02-07 16:08
 **/
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusResponse implements Serializable {

    /**
     * 发货单对象
     */
    @ApiModelProperty(value = "发货单对象")
    private List<DeliveryInfoVO> deliveryInfoVOList;
}
