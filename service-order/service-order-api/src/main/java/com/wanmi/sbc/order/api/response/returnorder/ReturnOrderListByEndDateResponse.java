package com.wanmi.sbc.order.api.response.returnorder;

import com.wanmi.sbc.order.bean.vo.ReturnOrderVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 根据结束时间查询退单列表响应结构
 * @Author: daiyitian
 * @Description:
 * @Date: 2018-11-16 16:39
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel
public class ReturnOrderListByEndDateResponse implements Serializable {

    private static final long serialVersionUID = -1076979847505660373L;

    /**
     * 退单列表
     */
    @ApiModelProperty(value = "退单列表")
    private List<ReturnOrderVO> returnOrderList;

}
