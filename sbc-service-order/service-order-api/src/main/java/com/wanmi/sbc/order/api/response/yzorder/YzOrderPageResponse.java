package com.wanmi.sbc.order.api.response.yzorder;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.order.bean.dto.yzorder.YzOrderDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class YzOrderPageResponse implements Serializable {

    private static final long serialVersionUID = -2558692746255027504L;

    /**
     * 分页数据
     */
    @ApiModelProperty(value = "分页数据")
    private MicroServicePage<YzOrderDTO> yzOrderPage;

    /**
     * 当前页大小
     */
    private Integer currentSize;

    /**
     * 总数量
     */
    private Long total;
}
