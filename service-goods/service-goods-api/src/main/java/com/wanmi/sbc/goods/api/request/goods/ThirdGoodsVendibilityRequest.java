package com.wanmi.sbc.goods.api.request.goods;

import com.wanmi.sbc.common.enums.ThirdPlatformType;
import com.wanmi.sbc.goods.bean.dto.LinkedMallItemDelDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ThirdGoodsVendibilityRequest implements Serializable {

    private static final long serialVersionUID = 2527394107305015557L;

    @ApiModelProperty("是否可售，0不可售，1可售")
    private Integer vendibility;

    @ApiModelProperty("渠道类型,0 linkedmall")
    private ThirdPlatformType thirdPlatformType;
}

