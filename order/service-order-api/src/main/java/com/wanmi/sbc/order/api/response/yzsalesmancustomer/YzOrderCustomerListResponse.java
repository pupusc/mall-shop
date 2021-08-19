package com.wanmi.sbc.order.api.response.yzsalesmancustomer;

import com.wanmi.sbc.order.bean.vo.YzOrderCustomerVO;
import com.wanmi.sbc.order.bean.vo.YzSalesmanCustomerVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YzOrderCustomerListResponse implements Serializable {

    @ApiModelProperty(value = "订单会员关系集合")
    private List<YzOrderCustomerVO> yzOrderCustomerVOList;

}
