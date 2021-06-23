package com.wanmi.sbc.order.api.response.returnorder;

import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 根据动态条件查询退单分页列表请求结构
 * Created by jinwei on 6/5/2017.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class ReturnOrderPageResponse implements Serializable {

    private static final long serialVersionUID = 5531232264401728496L;

    /**
     * 退单分页列表
     */
    @ApiModelProperty(value = "退单分页列表")
    private MicroServicePage<ReturnOrderVO> returnOrderPage;
}
