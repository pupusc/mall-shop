package com.wanmi.sbc.order.api.response.yzsalesmancustomer;

import com.wanmi.sbc.order.bean.vo.YzSalesmanCustomerVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>根据id查询任意（包含已删除）有赞销售员客户关系信息response</p>
 * @author he
 * @date 2021-03-02 10:10:55
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class YzSalesmanCustomerByIdResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 有赞销售员客户关系信息
     */
    @ApiModelProperty(value = "有赞销售员客户关系信息")
    private YzSalesmanCustomerVO yzSalesmanCustomerVO;
}
