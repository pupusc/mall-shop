package com.wanmi.sbc.goods.api.response.SuspensionV2;


import com.wanmi.sbc.goods.bean.dto.SuspensionDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuspensionByIdResponse {
    /**
     * 悬浮穿信息
     */
    @ApiModelProperty(value = "悬浮窗信息")
    private SuspensionDTO suspensionDTO;
}
